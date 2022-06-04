package com.lgt.Ace11.ForgotPasswordPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.LoginActivity;
import com.lgt.Ace11.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.FORGOTPASSWORD;
import static com.lgt.Ace11.Config.VERIFYFORGOTPASSWORD;
import static com.lgt.Ace11.Constants.FORGOTPASSWORDTYPE;
import static com.lgt.Ace11.Constants.VERIFYFORGOTPASSWORDTYPE;

public class ForgotVerifyOTPActivity extends AppCompatActivity implements ResponseManager {


    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    Context context;
    ForgotVerifyOTPActivity activity;


    EditText et_Otp1,et_Otp2,et_Otp3,et_Otp4;

    TextView tv_VerifyOTP,tv_OtpSendTo,tv_OtpTimer,tv_OtpResend,tv_HeaderName;

    ImageView im_back;

    String OTP;
    String IntentType,IntentEmailorMobile;
    private static CountDownTimer countDownTimer;

    private static final int SMS_CONSENT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = activity = this;
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        initView();


        Intent o = getIntent();
        IntentType = o.getStringExtra("type");
        IntentEmailorMobile = o.getStringExtra("EmailorMobile");


        tv_OtpSendTo.setText("OTP sent to "+IntentEmailorMobile);

        callForgotPasswordApi(true);

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
                callForgotPasswordApi(true);
                tv_OtpTimer.setVisibility(View.VISIBLE);
                tv_OtpResend.setVisibility(View.GONE);

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


    /*private BroadcastReceiver smsVerificationReceiver= new  BroadcastReceiver() {
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
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onResume() {
        /*Task<Void> task = SmsRetriever.getClient(getApplicationContext()).startSmsUserConsent(null *//* or null *//*);
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
        registerReceiver(smsVerificationReceiver, intentFilter);*/
        super.onResume();
    }

    private void callVerifyOTPApi(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(VERIFYFORGOTPASSWORD,
                    createRequestJson(), context, activity, VERIFYFORGOTPASSWORDTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmailorNumber", IntentEmailorMobile);
            jsonObject.put("otp", OTP);
            jsonObject.put("type", IntentType);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    private void callForgotPasswordApi(boolean isShowLoader) {
        try {

            countDownTimer =  new CountDownTimer(60000, 1000) {

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
            jsonObject.put("type", IntentType);
            Log.e("typpppee",IntentType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }







    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {


        Log.e("ressspo",result+"");
        Log.e("ressspo22",message+"");

        if (type.equals(VERIFYFORGOTPASSWORDTYPE)) {
            ShowToast(context,message);
            Log.e("ressspo223",message+"");

            try {
                Intent i = new Intent(activity, NewPasswordActivity.class);
                i.putExtra("UserId", result.getString("user_id"));
                i.putExtra("IntentActivity", "ForgotPassword");

                startActivity(i);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (type.equals(FORGOTPASSWORDTYPE)){
            ShowToast(context,message);
            Log.e("ressspo22",message+"");

        }

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        //ShowToast(context,message);
        Log.e("ressspoErrr",message+"");

        if (type.equals(VERIFYFORGOTPASSWORDTYPE)){
            Log.e("ressspoErrr2",message+"");

            ShowToast(context,"Invalid OTP");
        }

        else if (type.equals(FORGOTPASSWORDTYPE)){
            Intent i = new Intent(activity,LoginActivity.class);
            startActivity(i);
            ShowToast(context,"Email or Mobile no not registered");
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(activity,LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode==SMS_CONSENT_REQUEST){
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
                ShowToast(activity,"ok enter manually..");
                // Consent canceled, handle the error ...
            }
        }*/
    }

    private String parseOneTimeCode(String message) {
        return message;
    }
}
