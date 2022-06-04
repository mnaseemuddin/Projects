package com.lgt.also_food_court_userApp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;



public class LoginOtpVerification extends AppCompatActivity {

    ImageView iv_back;
    TextView tv_login_mobile,tv_NoOTPget, tvnlogin_resendOTP;
    EditText et_otp_login_verification;
    Button btn_otp_login_verification;
    static String user_id, name, mobile, otp;
    String et_enter_otp;
    SharedPreferences.Editor editor;
    ProgressBar pb_resend_otplogin;
    private static CountDownTimer countDownTimer;

    private static final int SMS_CONSENT_REQUEST = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp_verification);

        editor=getApplicationContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE).edit();
        iv_back=findViewById(R.id.iv_back);
        tv_login_mobile=findViewById(R.id.tv_login_mobile);
        et_otp_login_verification=findViewById(R.id.et_otp_login_verification);
        btn_otp_login_verification=findViewById(R.id.btn_otp_login_verification);
        pb_resend_otplogin=findViewById(R.id.pb_resend_otplogin);
        tvnlogin_resendOTP=findViewById(R.id.tvnlogin_resendOTP);
        tv_NoOTPget=findViewById(R.id.tv_NoOTPget);

        Intent intent=getIntent();

        if (intent != null) {
            user_id = intent.getExtras().getString("user_id");
            otp = intent.getExtras().getString("otp");
            mobile = intent.getExtras().getString("mobile");
            name=intent.getExtras().getString("name");
            tv_login_mobile.setText(mobile);

        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        btn_otp_login_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent in =new Intent(LoginOtpVerification.this,MainActivity.class);
                startActivity(in);*/
                Inputvalidation();
            }
        });
        countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv_NoOTPget.setText("Didn't receive the OTP? Request for a new one in " + String.format("%d:%d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tvnlogin_resendOTP.setVisibility(View.VISIBLE);
                tv_NoOTPget.setVisibility(View.GONE);
            }

        }.start();
        tvnlogin_resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });

    }

    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
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
                        Log.e("somerrrorororor","errorooccured");
                        break;
                }
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();


        Task<Void> task = SmsRetriever.getClient(LoginOtpVerification.this).startSmsUserConsent(null /* or null */);
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        LoginOtpVerification.this.registerReceiver(smsVerificationReceiver, intentFilter);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void status) {
                Log.e("dsadsadstatus", status + "");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // ...
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    String oneTimeCode = parseOneTimeCode(message); // define this function
                    if(!message.isEmpty()){
                        Log.e("dsadsadsad",message+"");

                    }
                    else {

                    }

                    assert oneTimeCode != null;
                    String fullMsg=oneTimeCode.replaceAll("[^0-9]","");

                    Log.e("njnjnjnjnjnjnnjn",message+"");
                    Log.e("njnjnjnjnjnjnnjn",fullMsg+"");

                    et_otp_login_verification.setText(fullMsg+"");
                    Log.e("sadsadsaddsadas",oneTimeCode+"");

                    // send one time code to the server
                } else {
                    Log.e("dsadasdad","dsasadsad");
                    // Consent canceled, handle the error ...
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {

            LocalBroadcastManager.getInstance(LoginOtpVerification.this).unregisterReceiver(smsVerificationReceiver);
        } catch (Exception e) {
            Log.e("fgfggggggfgf", e.getMessage());
            e.printStackTrace();
        }
    }


    private String parseOneTimeCode(String message) {
        Log.e("messhhsagdh",message+"");
        return message;
    }



    private void resendOtp() {
        pb_resend_otplogin.setVisibility(View.VISIBLE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ResendOtp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pb_resend_otplogin.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    String User_id=jsonObject.getString("user_id");
                    if (status.equals("1")){
                        JSONObject object=jsonObject.getJSONObject("data");
                        String Name=object.getString("name");
                        String Email=object.getString("email");
                        String Mobile=object.getString("mobile");
                        String Otp=object.getString("otp");
                        Toast.makeText(LoginOtpVerification.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_resend_otplogin.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String>params=new HashMap<>();
                params.put("user_id",user_id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void Inputvalidation() {
        et_enter_otp=et_otp_login_verification.getText().toString().trim();
        if (TextUtils.isEmpty(et_enter_otp)){
            et_otp_login_verification.setError("Enter Otp");
            et_otp_login_verification.requestFocus();
            return;
        }
        if (!otp.equals(et_enter_otp)){
            Toast.makeText(getApplicationContext(),"Otp Not Match", Toast.LENGTH_SHORT).show();

        }else{
            editor.putString("user_id", user_id);
            editor.putString("name", name);
            editor.putString("mobile", mobile);
            editor.putString("otp", otp);
            editor.apply();
            Intent in=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(in);
            finishAffinity();
            Toast.makeText(this, "Welcome To "+name, Toast.LENGTH_SHORT).show();
        }
    }
}
