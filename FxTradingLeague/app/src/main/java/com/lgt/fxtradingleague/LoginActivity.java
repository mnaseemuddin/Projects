package com.lgt.fxtradingleague;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Bean.UserDetails;
import com.lgt.fxtradingleague.ForgotPasswordPackage.ForgotVerifyOTPActivity;

import com.google.android.gms.common.api.GoogleApiClient;


import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.lgt.fxtradingleague.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.fxtradingleague.Extra.ExtraData.LOGIN_API;

public class LoginActivity extends AppCompatActivity {

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    Context context;
    LoginActivity activity;

    EditText et_EmailMobile, et_Password;
    TextView tv_LoginNext, tv_Login, tv_UserEmailMob, tv_HeaderName, tv_LFacebookLogin, tv_LGoogleLogin, tv_ForgotPassword;
    LinearLayout LL_LRegister;
    RelativeLayout RL_EnterEmail, RL_EnterPassword;
    String EmailorMobile, Password;

    ImageView im_back;


    private final String TAG = "RegistrationActivity";
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;


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
        sessionManager = new SessionManager();
        initViews();


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
                    callLoginApi(EmailorMobile, Password);
                }
                // not in use
                /* else if (TextUtils.isDigitsOnly(EmailorMobile)) {
                    if (!EmailorMobile.matches(Validations.MobilePattern)) {
                        et_EmailMobile.requestFocus();
                        YoYo.with(Techniques.Wave)
                                .duration(600)
                                .repeat(0)
                                .playOn(et_EmailMobile);
                        et_EmailMobile.setError("Please enter valid Email or Mobile No.");
                        ShowToast(context, "Please enter valid Email or Mobile No.");
                    }*/
            }
        });

        tv_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailorMobile = et_EmailMobile.getText().toString();
                String type = "";
                if (EmailorMobile.contains("@")) {
                    type = "Email";
                } else if (TextUtils.isDigitsOnly(EmailorMobile)) {
                    type = "Number";
                }
                Intent i = new Intent(activity, ForgotVerifyOTPActivity.class);
                i.putExtra("type", type);
                i.putExtra("EmailorMobile", EmailorMobile);
                startActivity(i);
            }
        });

        LL_LRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, RegistrationActivity.class);
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

        et_EmailMobile = findViewById(R.id.et_EmailMobile);
        et_Password = findViewById(R.id.et_Password);
        ;

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

    }


    private void callLoginApi(final String numberOrEmail, final String Password) {
        Validations.showProgress(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("callSignUpApi", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        Validations.hideProgress();
                        JSONObject data = jsonObject.getJSONObject("data");
                        String user_id = data.getString("user_id");
                        String mobile = data.getString("mobile");
                        String referral_code = data.getString("referral_code");
                        String email = data.getString("email");
                        String name = data.getString("name");
                        String birthday = data.getString("birthday");
                        saveLoginInformation(user_id,name,email,mobile,referral_code,birthday);
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                    }else{
                        Validations.hideProgress();
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Validations.hideProgress();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.hideProgress();
                Log.d("callSignUpApi", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("email", numberOrEmail);
                param.put("password", Password);
                Log.d("callLoginApi", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveLoginInformation(String user_id, String name, String email, String mobile, String referral_code, String birthday) {
        if (true) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.commit();
            try {
                UserDetails userDetails = new UserDetails();
                userDetails.setUser_id(user_id);
                userDetails.setName(name);
                userDetails.setMobile(mobile);
                userDetails.setEmail(email);
                userDetails.setType("normal");
                userDetails.setVerify("true");
                userDetails.setDob(birthday);
                userDetails.setReferral_code(referral_code);
                userDetails.setImage("default.png");
                sessionManager.setUser(context, userDetails);
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

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", EmailorMobile);

            jsonObject.put("password", Password);
            Log.e("hgasfa", EmailorMobile + "password=" + Password);
            jsonObject.put("type", SLoginType);
            jsonObject.put("token", FirebaseInstanceId.getInstance().getToken());
            Log.e("jsonObject.put", jsonObject + "");
            Log.e("jsonObject.put", EmailorMobile + "");
            Log.e("jsonObject.put", Password + "");
            Log.e("jsonObject.put", SLoginType + "");
            Log.e("jsonObject.put", FirebaseInstanceId.getInstance().getToken() + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void callSignUpApi() {

    }

    JSONObject createRequestJsonSignUp() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", "");
            jsonObject.put("email", SEmailId);
            jsonObject.put("password", SPassword);
            jsonObject.put("code", "");
            jsonObject.put("type", SLoginType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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
