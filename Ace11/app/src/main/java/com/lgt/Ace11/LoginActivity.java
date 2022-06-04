package com.lgt.Ace11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Class.Validations;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.UserDetails;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.LOGIN;
import static com.lgt.Ace11.Constants.LOGINTYPE;

public class LoginActivity extends AppCompatActivity implements ResponseManager, GoogleApiClient.OnConnectionFailedListener {

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    String LoginType = "Normal";
    String EmailId, Password, Name,facebook_image_url,facebook_id;

    Context context;
    LoginActivity activity;

    EditText et_EmailMobile, et_Password;
    TextView tv_LoginNext, tv_Login, tv_UserEmailMob, tv_HeaderName, tv_LFacebookLogin, tv_LGoogleLogin, tv_ForgotPassword;
    LinearLayout LL_LRegister;
    RelativeLayout RL_EnterEmail, RL_EnterPassword,Rl_Facebook,RL_Google;
    String EmailorMobile;
    ImageView im_back;

    private final String TAG = "RegistrationActivity";
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;

    LoginButton loginButton;
    SignInButton signInButton;

    private CallbackManager callbackManager;
    AccessToken accessTokan;

    String SEmailId, SPassword, SLoginType;

    //Auto Login
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;


    String Back = "0";
    SessionManager sessionManager;

    //redefining it solutions beyond your imagination


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = activity = this;
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        sessionManager = new SessionManager();
        initViews();

        FacebookSdk.sdkInitialize(getApplicationContext());
        if(AccessToken.getCurrentAccessToken() != null) {
           // LoginManager.getInstance().logOut();
        }
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);


        tv_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailorMobile = et_EmailMobile.getText().toString();
                Password = et_Password.getText().toString();
                if (EmailorMobile.equals("")) {
                    YoYo.with(Techniques.Wave)
                            .duration(600)
                            .repeat(0)
                            .playOn(et_EmailMobile);
                    ShowToast(context, "Please enter valid Email or Mobile No.");
                    et_EmailMobile.setError("Please enter valid Email or Mobile No.");
                } else if (Password.equals("")) {
                    et_Password.setError("Please enter valid password.");
                    ShowToast(context, "Please enter valid password.");
                    YoYo.with(Techniques.Wave)
                            .duration(600)
                            .repeat(0)
                            .playOn(et_Password);
                } else if (Password.length() < 8 && !Validations.isValidPassword(Password)) {
                    YoYo.with(Techniques.Wave)
                            .duration(600)
                            .repeat(0)
                            .playOn(et_Password);
                    et_Password.setError("Please enter valid password.");

                    ShowToast(context, "Please enter valid password.");
                } else {
                    Back = "1";
                    tv_UserEmailMob.setText(EmailorMobile);
                    SLoginType = "Normal";
                    callLoginApi(true);
                }
            }
        });

        tv_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailorMobile = et_EmailMobile.getText().toString();
                if (EmailorMobile.equals("")) {
                    YoYo.with(Techniques.Wave)
                            .duration(600)
                            .repeat(0)
                            .playOn(et_EmailMobile);
                    ShowToast(context, "Please enter Mobile No.");
                    et_EmailMobile.setError("Please enter valid Mobile No.");
                }else{
                    String type = "";
                    if (EmailorMobile.contains("@")) {
                        type = "Email";
                    } else if (TextUtils.isDigitsOnly(EmailorMobile)) {
                        type = "Number";
                    }
                    Intent i = new Intent(activity, com.lgt.Ace11.VerifyOTPActivity.class);
                    i.putExtra("type", "ForgetPassword");
                    i.putExtra("ForgetType", type);
                    i.putExtra("EmailorMobile", EmailorMobile);
                    startActivity(i);
                }
            }
        });

        LL_LRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, com.lgt.Ace11.RegistrationActivity.class);
                i.putExtra("Reffered", "No");
                startActivity(i);
            }
        });

    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void initViews() {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        et_EmailMobile = findViewById(R.id.et_EmailMobile);
        et_Password = findViewById(R.id.et_Password);

        Rl_Facebook = findViewById(R.id.Rl_Facebook);
        RL_Google = findViewById(R.id.RL_Google);
        signInButton = findViewById(R.id.google_Loginbutton);
        loginButton = findViewById(R.id.login_FaceBookbutton);

        tv_Login = findViewById(R.id.tv_Login);

        RL_EnterEmail = findViewById(R.id.RL_EnterEmail);
        RL_EnterPassword = findViewById(R.id.RL_EnterPassword);
        tv_UserEmailMob = findViewById(R.id.tv_UserEmailMob);
        LL_LRegister = findViewById(R.id.LL_LRegister);

        tv_ForgotPassword = findViewById(R.id.tv_ForgotPassword);


        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_HeaderName.setText("LOG IN");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Rl_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login", "FacebookLogin");
                loginButton.performClick();
                initFbObject(context);
                LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
            }
        });

        RL_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login", "GoogleLogin");
                signInButton.performClick();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                ((LoginActivity) context).startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });
    }

    private void getTokenDevice() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    if (task.getResult()!= null){
                        com.lgt.Ace11.Config.FIREBASE_DEVICE_TOKEN = task.getResult();
                        Log.wtf("FIREBASE_DEVICE_TOKEN",""+task.getResult());
                    }
                }
            }
        });
    }

    private void initFbObject(Context context) {
        callbackManager = CallbackManager.Factory.create();
        //Facebook
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //showProgressDialog();
                String userId = loginResult.getAccessToken().getUserId();
                accessTokan = loginResult.getAccessToken();

                try {
                    GraphRequest request = GraphRequest.newMeRequest(loginResult
                            .getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(final JSONObject object, GraphResponse response) {
                            Log.i(TAG, response.toString());
                            // Get facebook data from login
                            getFacebookData(object);
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,link,picture");
                    request.setParameters(parameters);
                    request.executeAsync();
                } catch (Exception e) {
                    Log.e("fb_response", e.getMessage() + "");
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FacebookError", "" + error);
            }
        });
    }

    private void getFacebookData(JSONObject object) {
        try {
            if (object != null) {
                Log.e("fb_response", object.toString());
                LoginType = "Social";
                Name = object.optString("name");
                EmailorMobile = object.optString("id");
                Password = object.optString("id");
                facebook_image_url = "https://graph.facebook.com/" + facebook_id + "/picture?width=9999";
                //LoginManager.getInstance().logOut();
                callLoginApi(false);
                Log.e(TAG, object.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("fb_response", e.getMessage() + "");
        }
    }


    private void callLoginApi(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(LOGIN,
                    createRequestJsonLogin(), context, activity, LOGINTYPE,
                    isShowLoader, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJsonLogin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", EmailorMobile);
            jsonObject.put("password", Password);
            jsonObject.put("type", LoginType);
            // jsonObject.put("token", FirebaseInstanceId.getInstance().getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("onActivityResult",resultCode+" | "+requestCode);

        if (data != null && callbackManager != null){
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }

        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            com.google.android.gms.tasks.Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        Validations.showProgress(context);
        Log.e("ressssese2", result + "");
        if (type.equals(LOGINTYPE)) {
            String Verify = "1";
            String MobNumber = "";
            String UserId = "";
            Log.e("MainActivity>>", "getResult: >>>" + result + "\n" + type);
            //ShowToast(context,message);
            try {
                // Verify = result.getString("verify");
                MobNumber = result.getString("mobile");
                UserId = result.getString("user_id");

                if (Verify.equals("0")) {
                    Validations.hideProgress();
                    et_EmailMobile.setText("");
                    et_Password.setText("");
                    // not in use
                    /*Intent i = new Intent(activity, VerifyOTPActivity.class);
                    i.putExtra("Number", MobNumber);
                    i.putExtra("Activity", "Login");
                    i.putExtra("UserId", UserId);
                    i.putExtra("Password", Password);
                    startActivity(i);*/

                } else {
                    Validations.hideProgress();
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.commit();

                    UserDetails userDetails = new UserDetails();
                    userDetails.setUser_id(UserId);
                    userDetails.setName(result.getString("name"));
                    userDetails.setDob(result.getString("dob"));
                    userDetails.setMobile(result.getString("mobile"));
                    userDetails.setEmail(result.getString("email"));
                    userDetails.setType(result.getString("type"));
                    userDetails.setReferral_code(result.getString("referral_code"));
                    userDetails.setImage(result.getString("image"));
                    userDetails.setAddress(" ");
                    userDetails.setCity(" ");
                    userDetails.setPincode(" ");
                    userDetails.setState(" ");
                    userDetails.setVerify(Verify);
                    sessionManager.setUser(context, userDetails);
                    et_EmailMobile.setText("");
                    et_Password.setText("");
                    Intent i = new Intent(activity, HomeActivity.class);
                    startActivity(i);
                    finishAffinity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void handleSignInResult(com.google.android.gms.tasks.Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("Google_login", "signInResult:failed code=" + account.getId() + "," +
                    "token=" + Config.ACCESS_TOKEN + ",firebase_device_token=" + Config.FIREBASE_DEVICE_TOKEN + "");
            LoginType = "Social";
            if (account != null) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("device_token", Config.FIREBASE_DEVICE_TOKEN);
                hashMap.put("user_email", account.getEmail());
                hashMap.put("full_name", account.getDisplayName());
                hashMap.put("login_type", LoginType);
                hashMap.put("user_name", Objects.requireNonNull(account.getEmail()).split("@")[0]);
                hashMap.put("identity", account.getEmail());
                //registerUser(hashMap);
                EmailorMobile = account.getEmail();
                Name = account.getDisplayName();
                Password = account.getId();
               // LoginManager.getInstance().logOut();
                callLoginApi(true);
                Log.e("Google_login", "handleSignInResult: " + account.getEmail());
                Log.e("Google_login", "handleSignInResult: " + account.getDisplayName());
                Log.e("Google_login", "handleSignInResult: " + Config.FIREBASE_DEVICE_TOKEN);
            }
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            Log.d("Google_login", "signInResult:failed code=" + e.getMessage());
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Google_login", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        Log.e("excceeee", message);
        if (type.equals(LOGINTYPE)) {
            ShowToast(context, "Invalid Email or Mobile No. and Password. Please try again.");
            System.out.println("responsess" + message);
            try {
                revokeAccess();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // updateUI(false);
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    @Override
    public void onBackPressed() {
        if (Back.equals("1")) {
            Back = "0";
            RL_EnterEmail.setVisibility(View.VISIBLE);
            RL_EnterPassword.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }


}
