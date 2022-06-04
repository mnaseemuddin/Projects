package com.lgt.Ace11;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Class.SingletonRequestQueue;
import com.lgt.Ace11.APICallingPackage.Class.Validations;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.UserDetails;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.BIT_COIN_URL;
import static com.lgt.Ace11.Config.BIT_KEY;
import static com.lgt.Ace11.Config.LOGIN;
import static com.lgt.Ace11.Config.SIGNUP;
import static com.lgt.Ace11.Constants.LOGINTYPE;
import static com.lgt.Ace11.Constants.SIGNUPTYPE;

public class RegistrationActivity extends AppCompatActivity implements ResponseManager, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 100;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    Context context;
    RegistrationActivity activity;

    RelativeLayout Rl_Facebook, RL_Google;
    LoginButton loginButton;
    SignInButton signInButton;
    TextInputLayout input_RegPassword, input_RegRefCode;
    EditText et_MobileNo, et_Email, et_name, et_Password, et_ReferralCode;
    TextView tv_RegNext, tv_TearmsandConditions, tv_HeaderName, et_dob;
    LinearLayout LL_Login;
    ImageView im_back;

    String MobileNumber="", EmailId="", Password="", ReferralCode="", Name="", dateOfBirth;

    private String login_type;
    private String facebook_image_url, facebook_id, facebook_name, come_from_check;
    private CallbackManager callbackManager;
    AccessToken accessTokan;


    private final String TAG = "RegistrationActivity";

    private GoogleApiClient mGoogleApiClient;

    String LoginType = "Normal";
    private String BITCOIN_ADDRESS = null;
    Calendar myCalendar;
    //Auto Login
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;

    //SMS Permission
    String[] permissions = new String[]{
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.RECEIVE_SMS
    };

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_regitration);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = activity = this;
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        myCalendar = Calendar.getInstance();
        initViews();
        sessionManager = new SessionManager();
        // BITCOIN_ADDRESS = getSaltString();
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        getTokenDevice();
        tv_RegNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EmailId = et_Email.getText().toString().trim();
                ReferralCode = et_ReferralCode.getText().toString();
                Password = et_Password.getText().toString();
                Name = et_name.getText().toString();
                dateOfBirth = et_dob.getText().toString();
                MobileNumber = et_MobileNo.getText().toString().trim();

                Log.e("sdsadsadsad", MobileNumber + " " + EmailId + "  " + Password + "  " + dateOfBirth + "  " + ReferralCode);

                // not in use
                if (MobileNumber.equals("")) {
                    ShowToast(context, "Please enter 10 digits mobile number.");
                    et_MobileNo.requestFocus();
                } else if (!MobileNumber.matches(Validations.MobilePattern)) {
                    et_MobileNo.requestFocus();
                    ShowToast(context, "Please enter 10 digits mobile number.");
                } else if (Password.equals("")) {
                    et_Password.requestFocus();
                    ShowToast(context, "Please enter password.");
                } else if (Password.length() < 8 && !Validations.isValidPassword(Password)) {
                    ShowToast(context, "Please enter valid password.");
                } else if (Name.equals("")) {
                    ShowToast(context, "Please Enter Name");
                } else {
                    LoginType = "Normal";
                    // callSignupApi(true);
                    callOtpActivity(MobileNumber);
                    // not in user
                    /* if (Build.VERSION.SDK_INT >= 23) {
                        checkPermissions();
                    } else {
                    // email verification
                     else if (EmailId.equals("")) {
                    et_Email.requestFocus();
                    ShowToast(context, "Please enter valid email id.");
                } else if (!Validations.isValidEmail(EmailId)) {
                    et_Email.requestFocus();
                    ShowToast(context, "Enter Valid Email Id");
                }
                        callSignupApi(true);
                        else if (BITCOIN_ADDRESS.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Payment Address not found!", Toast.LENGTH_SHORT).show();
                }
                    }*/
                }
            }


        });


        LL_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, LoginActivity.class);
                startActivity(i);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        tv_TearmsandConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading", "TERMS & CONDITIONS");
                i.putExtra("URL", Config.TERMSANDCONDITIONSURL);
                startActivity(i);*/
            }
        });
    }

    private void getTokenDevice() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    if (task.getResult()!= null){
                        Config.FIREBASE_DEVICE_TOKEN = task.getResult();
                        Log.wtf("FIREBASE_DEVICE_TOKEN",""+task.getResult());
                    }
                }
            }
        });
    }

    private void callOtpActivity(String mobile) {
        Intent otpActivity = new Intent(RegistrationActivity.this,VerifyOTPActivity.class);
        otpActivity.putExtra("type","Registration");
        otpActivity.putExtra("Email",EmailId);
        otpActivity.putExtra("Refer",ReferralCode);
        otpActivity.putExtra("Number",mobile);
        otpActivity.putExtra("Name",Name);
        otpActivity.putExtra("Password",Password);
        startActivity(otpActivity);
    }

    public void initViews() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        Rl_Facebook = findViewById(R.id.Rl_Facebook);
        RL_Google = findViewById(R.id.RL_Google);
        loginButton = findViewById(R.id.login_button);
        signInButton = findViewById(R.id.google_button);
        input_RegPassword = findViewById(R.id.input_RegPassword);
        input_RegRefCode = findViewById(R.id.input_RegRefCode);
        et_name = findViewById(R.id.et_name);
        et_dob = findViewById(R.id.et_dob);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this, date
                            , myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show();
                }

               /* new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
            }
        });

        Rl_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login", "FacebookLogin");
                loginButton.performClick();
                initFbObject(context);
            }
        });

        RL_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login", "GoogleLogin");
                signInButton.performClick();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                ((RegistrationActivity) context).startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        et_MobileNo = findViewById(R.id.et_MobileNo);
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);
        et_ReferralCode = findViewById(R.id.et_ReferralCode);


        tv_RegNext = findViewById(R.id.tv_RegNext);
        tv_TearmsandConditions = findViewById(R.id.tv_TearmsandConditions);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        LL_Login = findViewById(R.id.LL_Login);
        im_back = findViewById(R.id.im_back);

        tv_HeaderName.setText("REGISTER & PLAY");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                // EmailId = object.optString("id");
                Password = object.optString("id");
                facebook_image_url = "https://graph.facebook.com/" + facebook_id + "/picture?width=9999";
                //LoginManager.getInstance().logOut();
                callSignupApi(false);
                Log.e(TAG, object.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("fb_response", e.getMessage() + "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("onActivityResult",resultCode+" | "+requestCode);

        if (requestCode==100){
            Toast.makeText(context, " You Already Registered", Toast.LENGTH_SHORT).show();

        }else if (requestCode==64206){
            Toast.makeText(context, " You Already Registered", Toast.LENGTH_SHORT).show();
        }
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

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_dob.setText(sdf.format(myCalendar.getTime()));
    }


    private void callSignupApi(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(SIGNUP,
                    createRequestJson(), context, activity, SIGNUPTYPE,
                    isShowLoader, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", MobileNumber);
            jsonObject.put("name", Name);
            jsonObject.put("email", EmailId);
            jsonObject.put("password", Password);
            jsonObject.put("code", ReferralCode);
            jsonObject.put("type", LoginType);
            Log.e("mydatatatata", "" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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
            jsonObject.put("mobile", EmailId);
            jsonObject.put("password", Password);
            jsonObject.put("type", LoginType);
            // jsonObject.put("token", FirebaseInstanceId.getInstance().getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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
                // EmailId = account.getEmail();
                Name = account.getDisplayName();
                Password = account.getId();
                //LoginManager.getInstance().logOut();
                callSignupApi(true);
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
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        Validations.showProgress(context);

        Log.e("mssssgggg", message +" , "+ type);

        if (message.equalsIgnoreCase("Registration done")) {
            ShowToast(getApplicationContext(), "Thanks for registration.");
        }

        Log.e("ressssssult", result.toString());

        if (type.equals(SIGNUPTYPE)) {
            Log.e("RegistrationActivity>>", "getResult: >>>" + result + "\n" + type);
            try {
                // not in use
                /* String UserId = result.getString("user_id");
                String mobile = result.getString("mobile");
                String email = result.getString("email");
                String LoginType = result.getString("type");*/
                callLoginApi(true);
                // not in use
               /* if (LoginType.equals("Normal")) {
                    Validations.hideProgress();
                    Intent i = new Intent(activity, VerifyOTPActivity.class);
                    i.putExtra("Number", mobile);
                    i.putExtra("Activity", "Registration");
                    i.putExtra("UserId", UserId);
                    i.putExtra("Password", Password);
                    startActivity(i);

                } else {
                    Validations.hideProgress();
                    callLoginApi(true);
                // } */

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equals(LOGINTYPE)) {

            Validations.hideProgress();
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.commit();

            try {
                UserDetails userDetails = new UserDetails();
                userDetails.setUser_id(result.getString("user_id"));
                userDetails.setName(result.getString("name"));
                userDetails.setMobile(result.getString("mobile"));
                userDetails.setEmail(result.getString("email"));
                userDetails.setType(result.getString("type"));
                userDetails.setVerify(result.getString("verify"));
                // userDetails.setBITCOIN_USERID(result.getString("coin_address"));
                userDetails.setReferral_code(result.getString("referral_code"));
                userDetails.setImage(result.getString("image"));
                sessionManager.setUser(context, userDetails);
                // setUpPaymentWallet();
                Intent i = new Intent(activity, HomeActivity.class);
                startActivity(i);
                finishAffinity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Validations.hideProgress();
        }
    }


    private void setUpPaymentWallet() {
        String url_path = BIT_COIN_URL + "?api_key=" + BIT_KEY + "&label=" + BITCOIN_ADDRESS;
        Log.d("bit_url", "" + url_path);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_path, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("bit_response", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String network = data.getString("network");
                        String user_id = data.getString("user_id");
                        String address = data.getString("address");
                        String label = data.getString("label");
                        UserDetails userDetails = sessionManager.getUser(context); // save user details
                        userDetails.setBITCOIN_ADDRESS(address + "");
                        userDetails.setBITCOIN_LABEL(label + "");
                        userDetails.setBITCOIN_USERID(user_id + "");
                        sessionManager.setUser(context, userDetails);
                        Validations.hideProgress();
                        Log.d("get_data", network + " | " + user_id + " | " + address + " | " + label);
                        Intent i = new Intent(activity, HomeActivity.class);
                        startActivity(i);
                        finishAffinity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 404) {
                    Toast.makeText(activity, "Payment Not Setup! User Full", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(RegistrationActivity.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 30) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {

        Log.e("exceptiooon33", message);

        if (type.equals(SIGNUPTYPE)) {
            //ShowToast(context, "Something went wrong. Please Login with our other options or try again with "+LoginType);
            //callLoginApi(true);

            if (message.equalsIgnoreCase("Mobile Number exist")) {
                ShowToast(context, "" + "Mobile No. already registered with us. Please login.");
                et_MobileNo.requestFocus();
                Log.e("errormessg", message);
            }
            if (message.equalsIgnoreCase("Email id exist")) {
                ShowToast(context, "Email already registered with us. Please login.");
                et_Email.requestFocus();
                Log.e("errormessg2", message);

            }
        } else if (type.equals(LOGINTYPE)) {
            ShowToast(context, "Some system error has occurred. Please try again.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        } else {
            callSignupApi(true);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // callSignupApi(true);
                Log.d("request_permission", "yes");
            } else {
                // callSignupApi(true);
                Log.d("request_permission", "not");
            }
            return;
        }
    }


}
