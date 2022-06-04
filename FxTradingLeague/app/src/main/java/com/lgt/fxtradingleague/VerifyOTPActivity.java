package com.lgt.fxtradingleague;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
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
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.SingletonRequestQueue;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Bean.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.lgt.fxtradingleague.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.fxtradingleague.Config.BIT_COIN_URL;
import static com.lgt.fxtradingleague.Config.BIT_KEY;
import static com.lgt.fxtradingleague.Config.LOGIN;
import static com.lgt.fxtradingleague.Config.RESENDOTP;
import static com.lgt.fxtradingleague.Config.VERIFYOTP;
import static com.lgt.fxtradingleague.Constants.LOGINTYPE;
import static com.lgt.fxtradingleague.Constants.RESENDOTPTYPE;
import static com.lgt.fxtradingleague.Constants.VERIFYOTPTYPE;

public class VerifyOTPActivity extends AppCompatActivity implements ResponseManager {


    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    Context context;
    VerifyOTPActivity activity;


    EditText et_Otp1,et_Otp2,et_Otp3,et_Otp4;

    TextView tv_VerifyOTP,tv_OtpSendTo,tv_OtpTimer,tv_OtpResend,tv_HeaderName;

    ImageView im_back;

    String OTP;
    String IntentNumber,IntentUserId,IntentPassword,IntentActivity;
    private static CountDownTimer countDownTimer;

    private static final int SMS_CONSENT_REQUEST = 2;
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
        apiRequestManager = new APIRequestManager(activity);
        initView();
        sessionManager = new SessionManager();

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        Intent o = getIntent();
        IntentNumber = o.getStringExtra("Number");
        IntentUserId = o.getStringExtra("UserId");
        IntentPassword = o.getStringExtra("Password");
        IntentActivity= o.getStringExtra("Activity");

        if (IntentActivity.equals("Login")){
            callResendOTPApi(true);
        }

        if (IntentActivity.equals("Registration")){
            callResendOTPApi(true);
        }

        tv_OtpSendTo.setText("OTP sent to "+IntentNumber);


        countDownTimer =  new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                //tv_Timer.setText("Resend OTP in: " + millisUntilFinished / 1000);
                tv_OtpTimer.setText("Didn't receive the OTP? Request for a new one in "+ String.format("%d:%d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
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
                   OTP = GetOTP();
                   callVerifyOTPApi(true);

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

                OTP  = GetOTP();

                callVerifyOTPApi(true);
                countDownTimer.cancel();

            }
        });

        tv_OtpResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callResendOTPApi(true);

                tv_OtpTimer.setVisibility(View.VISIBLE);
                tv_OtpResend.setVisibility(View.GONE);
                countDownTimer =  new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //tv_Timer.setText("Resend OTP in: " + millisUntilFinished / 1000);
                        tv_OtpTimer.setText("Didn't receive the OTP? Request for a new one in "+ String.format("%d:%d sec",
                                TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
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

    public void initView(){
        et_Otp1 = findViewById(R.id.et_Otp1);
        et_Otp2 = findViewById(R.id.et_Otp2);
        et_Otp3 = findViewById(R.id.et_Otp3);
        et_Otp4 = findViewById(R.id.et_Otp4);


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
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });
        tv_HeaderName.setText("VERIFY OTP");


    }

    public String GetOTP(){
        String GETOTP = "";
        String Otp1 = et_Otp1.getText().toString();
        String Otp2 = et_Otp2.getText().toString();
        String Otp3 = et_Otp3.getText().toString();
        String Otp4 = et_Otp4.getText().toString();

        if (Otp1.equals("")){
            ShowToast(context,"Enter OTP");
        }
        else if (Otp2.equals("")){
            ShowToast(context,"Enter OTP");
        }else if (Otp3.equals("")){
            ShowToast(context,"Enter OTP");
        }else if (Otp4.equals("")){
            ShowToast(context,"Enter OTP");
        }
        else {
            GETOTP = Otp1+Otp2+Otp3+Otp4;
        }

        return GETOTP;
    }

    private BroadcastReceiver smsVerificationReceiver= new  BroadcastReceiver() {
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
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(smsVerificationReceiver);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {

        Task<Void> task = SmsRetriever.getClient(getApplicationContext()).startSmsUserConsent(null /* or null */);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("succcessss","done");

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("failerrr",e.toString());
            }
        });

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter);

        super.onResume();
    }



    private void callVerifyOTPApi(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(VERIFYOTP,
                    createRequestJson(), context, activity, VERIFYOTPTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", IntentNumber);
            jsonObject.put("otp", OTP);
            jsonObject.put("user_id", IntentUserId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void callResendOTPApi(boolean isShowLoader) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id",IntentUserId);
            apiRequestManager.callAPI(RESENDOTP,
                    jsonObject, context, activity, RESENDOTPTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callLoginApi(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(LOGIN,
                    createRequestJsonLogin(), context, activity, LOGINTYPE,
                    isShowLoader,responseManager);

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


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }




    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        Log.e("ressspsoss",result+"");
        Log.e("ressspsoss_type",type+"");


        ShowToast(context,message);
        if (type.equals(VERIFYOTPTYPE)) {
            callLoginApi(true);
            Log.e("ressspsoss",result+"");

        }
        else if (type.equals(LOGINTYPE)){

            Log.e("ressspsoss",result+"");
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
                userDetails.setReferral_code(result.getString("referral_code"));
                userDetails.setImage(result.getString("image"));
                userDetails.setCity(" ");
                userDetails.setState(" ");
                userDetails.setPincode(" ");
                userDetails.setDob(result.getString("dob"));
                userDetails.setAddress(" ");
                sessionManager.setUser(context,userDetails);
                setUpPaymentWallet();
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e("sessionException",e.getMessage());
            }
        }
        else if (type.equals(RESENDOTPTYPE)){
            ShowToast(context,message);
        }
    }


    private void setUpPaymentWallet() {
        String url_path = BIT_COIN_URL+"?api_key="+BIT_KEY+"&label="+getSaltString();
        Log.d("bit_url",""+url_path);
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
                        Intent i = new Intent(activity,HomeActivity.class);
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

        Log.e("error_type",type+",message="+message);

        //ShowToast(context,message);
        if (type.equals(VERIFYOTP)){
            ShowToast(context,"Invalid OTP");
        }
        else if (type.equals(LOGINTYPE)) {
            ShowToast(context,"Number Verified Successfully. Please Login to Continue");
            Intent i = new Intent(activity,LoginActivity.class);
            startActivity(i);
        }
        else if (type.equals(RESENDOTPTYPE)){
            Log.e("sssssssss",message);
          //  ShowToast(context,message);
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(activity,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SMS_CONSENT_REQUEST){
            if (resultCode == RESULT_OK) {
                // Get SMS message content
                assert data != null;
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                // Extract one-time code from the message and complete verification
                // `sms` contains the entire text of the SMS message, so you will need
                // to parse the string.
                String oneTimeCode = parseOneTimeCode(message); // define this function


                assert oneTimeCode != null;
                String fullMsg=oneTimeCode.replaceAll("[^0-9]","");

                char o1 = fullMsg.charAt(0);
                char o2 = fullMsg.charAt(1);
                char o3 = fullMsg.charAt(2);
                char o4 = fullMsg.charAt(3);


                et_Otp1.setText(o1+"");
                et_Otp2.setText(o2+"");
                et_Otp3.setText(o3+"");
                et_Otp4.setText(o4+"");

                GetOTP();
                callVerifyOTPApi(true);
                countDownTimer.cancel();
                Log.e("codeeeee",oneTimeCode.replaceAll("[^0-9]",""));

                //   ToastMessage.makeText(getActivity(),oneTimeCode,ToastMessage.LENGTH_SHORT).show();
                // send one time code to the server
            } else {
                // Consent canceled, handle the error ...
            }
        }
    }

    private String parseOneTimeCode(String message) {
        return message;
    }
}
