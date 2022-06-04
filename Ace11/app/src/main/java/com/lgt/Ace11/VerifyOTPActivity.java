package com.lgt.Ace11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Class.SingletonRequestQueue;
import com.lgt.Ace11.APICallingPackage.Class.Validations;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.BIT_COIN_URL;
import static com.lgt.Ace11.Config.BIT_KEY;
import static com.lgt.Ace11.Config.FORGOTPASSWORD;
import static com.lgt.Ace11.Config.LOGIN;
import static com.lgt.Ace11.Config.SIGNUP;
import static com.lgt.Ace11.Config.VERIFYOTP;
import static com.lgt.Ace11.Constants.FORGOTPASSWORDTYPE;
import static com.lgt.Ace11.Constants.LOGINTYPE;
import static com.lgt.Ace11.Constants.RESENDOTPTYPE;
import static com.lgt.Ace11.Constants.SIGNUPTYPE;

public class VerifyOTPActivity extends AppCompatActivity implements ResponseManager {

    String code = "";
    private Context context;
    private VerifyOTPActivity activity;
    private FirebaseAuth mAuth;
    String verificationid = "";
    private boolean fillOTP = false;
    private CountDownTimer countDownTimer;
    String OTP;
    private static final int SMS_CONSENT_REQUEST = 2;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    EditText et_Otp1, et_Otp2, et_Otp3, et_Otp4, et_Otp5, et_Otp6;

    TextView tv_VerifyOTP, tv_OtpSendTo, tv_OtpTimer, tv_OtpResend, tv_HeaderName;
    String LoginType = "Normal";

    ImageView im_back;

    String IntentNumber, IntentName, IntentPassword, IntentEmailID, IntentRefer, IntentForgetType, IntentEmailorMobile,IntentType;
    //Auto Login
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = activity = this;
        responseManager = this;

        sessionManager = new SessionManager();
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        apiRequestManager = new APIRequestManager(activity);

        initView();
        iniFirebase();
        ClickEvents();

        Intent o = getIntent();
        if (o.hasExtra("type")) {
            if (o.getStringExtra("type").equalsIgnoreCase("Registration")) {
                IntentNumber = o.getStringExtra("Number");

                IntentName = o.getStringExtra("Name");
                IntentPassword = o.getStringExtra("Password");
                IntentEmailID = o.getStringExtra("Email");
                IntentRefer = o.getStringExtra("Refer");
                IntentType = o.getStringExtra("type");
                if (IntentNumber != null) {
                    if (!TextUtils.isEmpty(IntentNumber)) {
                        sendOtp();
                    }
                }
            } else if (o.getStringExtra("type").equalsIgnoreCase("ForgetPassword")) {
                IntentType = o.getStringExtra("type");
                IntentForgetType = o.getStringExtra("ForgetType");
                IntentEmailorMobile = o.getStringExtra("EmailorMobile");

                Log.e("jkdhjehdj",IntentEmailorMobile+"");

                if (IntentForgetType.equalsIgnoreCase("Email")){
                    Toast.makeText(context, "Please Enter Register Mobile Number.", Toast.LENGTH_SHORT).show();
                }else if (IntentForgetType.equalsIgnoreCase("Number")){
                    IntentNumber = IntentEmailorMobile;
                    sendOtp();
                    Log.e("hdhdhh",IntentNumber+"");
                }
            }
        }

        tv_OtpSendTo.setText("OTP sent to " + IntentNumber);
    }


    private void ClickEvents() {
        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                //tv_Timer.setText("Resend OTP in: " + millisUntilFinished / 1000);
                tv_OtpTimer.setText("Didn't receive the OTP? Request for a new one in " + String.format("%d:%d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tv_OtpResend.setVisibility(View.VISIBLE);
                tv_OtpTimer.setVisibility(View.GONE);
            }

        }.start();

        et_Otp1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_Otp1.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_Otp2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        et_Otp2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_Otp2.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_Otp3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        et_Otp3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_Otp3.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_Otp4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_Otp4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_Otp4.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_Otp5.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_Otp5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_Otp5.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_Otp6.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_Otp6.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_Otp6.getText().toString().length() == 1)     //size as per your requirement
                {
                    code = GetOTP();
                    loginInApp();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });


        tv_VerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OTP = GetOTP();
                loginInApp();
                countDownTimer.cancel();
            }
        });

        tv_OtpResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp();
                tv_OtpTimer.setVisibility(View.VISIBLE);
                tv_OtpResend.setVisibility(View.GONE);
                countDownTimer = new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //tv_Timer.setText("Resend OTP in: " + millisUntilFinished / 1000);
                        tv_OtpTimer.setText("Didn't receive the OTP? Request for a new one in " + String.format("%d:%d sec",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    public void onFinish() {
                        tv_OtpResend.setVisibility(View.VISIBLE);
                        tv_OtpTimer.setVisibility(View.GONE);
                    }

                }.start();
            }
        });
    }

    private void callSignupApi(boolean isShowLoader) {
        Validations.showProgress(VerifyOTPActivity.this);
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
            jsonObject.put("mobile", IntentNumber);
            jsonObject.put("name", IntentName);
            jsonObject.put("email", IntentEmailID);
            jsonObject.put("code", IntentRefer);
            jsonObject.put("password", IntentPassword);
            jsonObject.put("type", LoginType);
            Log.e("mydatatatata", "" + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    // firebase otp

    private void iniFirebase() {
        mAuth = FirebaseAuth.getInstance();



    }


    private void sendOtp() {


        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+91" + IntentNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.d("onVerification", "onVerificationCompleted: " + new Gson().toJson(phoneAuthCredential));
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d("onVerification", "onVerificationFailed: " + new Gson().toJson(e));
                    }

                    @Override
                    public void onCodeSent(@NonNull String Id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Log.d("onVerification", "onCodeSent: " + Id);
                        Toast.makeText(getApplicationContext(), "OTP Send Succesfully", Toast.LENGTH_LONG).show();
                        tv_VerifyOTP.setVisibility(View.VISIBLE);
                        verificationid = Id;
                    }
                })          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        if (IntentType.equalsIgnoreCase("Registration")){
                            callSignupApi(false);
                        }else if(IntentType.equalsIgnoreCase("ForgetPassword")){
                            callForgotPasswordApi(true);
                        }
                        Log.d("onVerification", "signInWithCredential:success");
                    } else {
                        Log.d("onVerification", "signInWithCredential:failure : " + task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            ShowToast(activity, "Please Enter Valid Otp");
                        }
                    }
                });
    }


    private void callForgotPasswordApi(boolean isShowLoader) {
        try {
            // not in use
            /*           countDownTimer =  new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //tv_Timer.setText("Resend OTP in: " + millisUntilFinished / 1000);
                    tv_OtpTimer.setText("Didn't receive the OTP? Request for a new code in next "+ String.format("%d:%d sec.",
                            TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }
                public void onFinish() {
                    tv_OtpResend.setVisibility(View.VISIBLE);
                    tv_OtpTimer.setVisibility(View.GONE);
                }

            }.start();

*/
            apiRequestManager.callAPI(FORGOTPASSWORD,
                    createRequestJsonForgotPassword(), context, activity, FORGOTPASSWORDTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJsonForgotPassword() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmailorNumber", IntentEmailorMobile);
            jsonObject.put("type", IntentForgetType);
            Log.e("typpppee",IntentType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void loginInApp() {
        if (code.equals("")) {
            Toast.makeText(activity, "Please Enter OTP", Toast.LENGTH_SHORT).show();
        } else if (code.length() < 6) {
            Toast.makeText(activity, "Please Enter Valid Otp", Toast.LENGTH_SHORT).show();
        } else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*Task<Void> task = SmsRetriever.getClient(context).startSmsUserConsent(null);
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter);
        task.addOnSuccessListener(status -> Log.d("onVerification", "consent success:" + status + ""));

        task.addOnFailureListener(e -> {
            if (e != null) {
                Log.d("onVerification", "consent error:" + e.getMessage() + "");
            }

        });*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*switch (requestCode) {
            // ...
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    String oneTimeCode = parseOneTimeCode(message); // define this function
                    if (!message.isEmpty()) {
                        Log.d("onVerification", "recieveed complete msg:" + message + "");
                    } else {

                    }

                    assert oneTimeCode != null;
                    String fullMsg = oneTimeCode.replaceAll("[^0-9]", "");

                    char o1 = fullMsg.charAt(0);
                    char o2 = fullMsg.charAt(1);
                    char o3 = fullMsg.charAt(2);
                    char o4 = fullMsg.charAt(3);
                    char o5 = fullMsg.charAt(4);
                    char o6 = fullMsg.charAt(5);

                   *//* binding.etOne.setText(o1 + "");
                    binding.etTwo.setText(o2 + "");
                    binding.etThree.setText(o3 + "");
                    binding.etFour.setText(o4 + "");
                    binding.etFive.setText(o5 + "");
                    binding.etSix.setText(o6 + "");*//*

                    Log.d("onVerification", "got digit otp from  msg:" + oneTimeCode + "");
                    // send one time code to the server

                } else {
                    ShowToast(context, "Please enter OTP manually");
                    // Consent canceled, handle the error ...
                }
                break;
        }*/
    }

    public void initView() {

        et_Otp1 = findViewById(R.id.et_Otp1);
        et_Otp2 = findViewById(R.id.et_Otp2);
        et_Otp3 = findViewById(R.id.et_Otp3);
        et_Otp4 = findViewById(R.id.et_Otp4);
        et_Otp5 = findViewById(R.id.et_Otp5);
        et_Otp6 = findViewById(R.id.et_Otp6);


        tv_VerifyOTP = findViewById(R.id.tv_VerifyOTP);
        tv_OtpSendTo = findViewById(R.id.tv_OtpSendTo);
        tv_OtpTimer = findViewById(R.id.tv_OtpTimer);
        tv_OtpResend = findViewById(R.id.tv_OtpResend);
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    countDownTimer.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });
        tv_HeaderName.setText("VERIFY OTP");


    }

    public String GetOTP() {
        String GETOTP = "";
        String Otp1 = et_Otp1.getText().toString();
        String Otp2 = et_Otp2.getText().toString();
        String Otp3 = et_Otp3.getText().toString();
        String Otp4 = et_Otp4.getText().toString();
        String Otp5 = et_Otp5.getText().toString();
        String Otp6 = et_Otp6.getText().toString();

        if (Otp1.equals("")) {
            ShowToast(context, "Enter OTP 1");
        } else if (Otp2.equals("")) {
            ShowToast(context, "Enter OTP 2");
        } else if (Otp3.equals("")) {
            ShowToast(context, "Enter OTP 3");
        } else if (Otp4.equals("")) {
            ShowToast(context, "Enter OTP 4");
        } else if (Otp5.equals("")) {
            ShowToast(context, "Enter OTP 5");
        } else if (Otp6.equals("")) {
            ShowToast(context, "Enter OTP 6");
        } else {
            GETOTP = Otp1 + Otp2 + Otp3 + Otp4 + Otp5 + Otp6;
        }
        return GETOTP;
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
            jsonObject.put("mobile", IntentNumber);
            jsonObject.put("password", IntentPassword);
            jsonObject.put("type", "Normal");
            jsonObject.put("token", FirebaseInstanceId.getInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /*private BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };*/

    @Override
    protected void onPause() {
        super.onPause();
        /*try {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(smsVerificationReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        Validations.showProgress(context);

        Log.e("mssssgggg", message);

        if (message.equalsIgnoreCase("Registration done")) {
            ShowToast(getApplicationContext(), "Thanks for registration.");
        }

        Log.e("ressssssult", result.toString());

        if (type.equals(SIGNUPTYPE)) {
            Validations.hideProgress();
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
                // userDetails.setEmail(result.getString("email"));
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
        }else if (type.equals(FORGOTPASSWORDTYPE)){
            Validations.hideProgress();
            ShowToast(context,message);
            Log.e("ressspo22",message+"");

        } else {
            Validations.hideProgress();
        }
    }

    private void setUpPaymentWallet() {
        String url_path = BIT_COIN_URL + "?api_key=" + BIT_KEY + "&label=" + getSaltString();
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
                        userDetails.setBITCOIN_ADDRESS(address);
                        userDetails.setBITCOIN_LABEL(label);
                        userDetails.setBITCOIN_USERID(user_id);
                        sessionManager.setUser(getApplicationContext(), userDetails);
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
                    Toast.makeText(activity, "User Already Exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(VerifyOTPActivity.this).getRequestQueue();
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
        Log.e("error_type", type + ",message=" + message);
        Validations.hideProgress();
        ShowToast(context,message);
        if (type.equals(VERIFYOTP)) {
            ShowToast(context, "Invalid OTP");
        } else if (type.equals(LOGINTYPE)) {
            ShowToast(context, "Number Verified Successfully. Please Login to Continue");
            Intent i = new Intent(activity, LoginActivity.class);
            startActivity(i);
        } else if (type.equals(RESENDOTPTYPE)) {
            Log.e("sssssssss", message);
            //  ShowToast(context,message);
        } else if (type.equals(SIGNUPTYPE)) {
            Log.e("sssssssss", "SIGNUPTYPE: "+message);
            Intent i = new Intent(activity, RegistrationActivity.class);
            startActivity(i);
            ShowToast(context,message);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(activity, MainActivity.class);
        startActivity(i);
        finish();
    }

    private String parseOneTimeCode(String message) {
        return message;
    }
}
