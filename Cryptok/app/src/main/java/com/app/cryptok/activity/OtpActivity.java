package com.app.cryptok.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.app.cryptok.Api.DBConstants.placeholder_user;

public class OtpActivity extends AppCompatActivity {

    public static TextView tv_resend_code, tv_OtpTimer;
    private EditText et_one, et_two, et_three, et_four, et_five, et_six;
    String OTP;
    ImageView iv_back;
    private TextView tv_verify;

    private boolean otp_verified = false;
    Context context;
    private boolean fillOTP = false;

    private CountDownTimer countDownTimer;
    private SessionManager sessionManager;
    OtpActivity activity;
    private FirebaseAuth mAuth;
    String verificationid;
    private ProgressBar bt_login_progress;
    //userinfo
    private String LOGIN_TYPE = Commn.MOBILE_TYPE;
    private String USER_IMAGE = " ";
    private String USER_NAME = " ";
    private String USER_MOBILE = " ";
    private String USER_ID = " ";
    private String USER_EMAIL = " ";
    private String COUNTRY_NAME = "+91";
    private String COUNTRY_CODE = "India";
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mAuth = FirebaseAuth.getInstance();

        context = activity = this;
        iniFirebase();
        if (getIntent().hasExtra(ConstantsKey.VERIFICATION_KEY)) {
            verificationid = getIntent().getStringExtra(ConstantsKey.VERIFICATION_KEY);
            USER_MOBILE = getIntent().getStringExtra(Commn.USER_MOBILE);
            COUNTRY_NAME = getIntent().getStringExtra(DBConstants.country_name);
            COUNTRY_CODE = getIntent().getStringExtra(DBConstants.country_code);
        }
        InializationViews();

    }

    private void iniFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    private String generateUserName() {
        String username = "Buzo@" + System.currentTimeMillis();
        return username;
    }

    private void InializationViews() {
        sessionManager = new SessionManager(this);
        tv_resend_code = findViewById(R.id.tv_resend_code);
        et_one = findViewById(R.id.et_one);
        et_two = findViewById(R.id.et_two);
        et_three = findViewById(R.id.et_three);
        et_four = findViewById(R.id.et_four);
        et_five = findViewById(R.id.et_five);
        et_six = findViewById(R.id.et_six);
        tv_verify = findViewById(R.id.tv_verify);
        tv_OtpTimer = findViewById(R.id.tv_OtpTimer);
        iv_back = findViewById(R.id.iv_back);
        bt_login_progress = findViewById(R.id.bt_login_progress);


        tv_verify.setOnClickListener(v -> {
            fillOTP = false;
            String Otp1 = et_one.getText().toString();
            String Otp2 = et_two.getText().toString();
            String Otp3 = et_three.getText().toString();
            String Otp4 = et_four.getText().toString();
            String Otp5 = et_five.getText().toString();
            String Otp6 = et_six.getText().toString();

        /*    Intent intent=new Intent(context,MainActivity.class);
            startActivity(intent);
            finishAffinity();*/

            if (Otp1.equals("")) {
                Commn.myToast(context, "Enter OTP");
                fillOTP = true;
            }
            if (Otp2.equals("")) {
                Commn.myToast(context, "Enter OTP");
                fillOTP = true;
            }
            if (Otp3.equals("")) {
                Commn.myToast(context, "Enter OTP");
                fillOTP = true;
            }
            if (Otp4.equals("")) {
                Commn.myToast(context, "Enter OTP");
                fillOTP = true;
            }
            if (Otp5.equals("")) {
                Commn.myToast(context, "Enter OTP");
                fillOTP = true;
            }
            if (Otp6.equals("")) {
                Commn.myToast(context, "Enter OTP");
                fillOTP = true;
            }
            if (!fillOTP) {
                loginInApp();

            }


        });

        iv_back.setOnClickListener(v -> onBackPressed());

        startCountDown();

        onTextChange();


        tv_resend_code.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finishAffinity();
            //callResendApi();

        });


    }


    private void loginInApp() {
        bt_login_progress.setVisibility(View.VISIBLE);
        tv_verify.setVisibility(View.GONE);
        String code = et_one.getText().toString().trim() + et_two.getText().toString().trim()
                + et_three.getText().toString().trim() + et_four.getText().toString().trim()
                + et_five.getText().toString().trim() + et_six.getText().toString().trim();
        if (code.equals("")) {
            Toast.makeText(activity, "Please Enter OTP", Toast.LENGTH_SHORT).show();
        } else if (code.length() < 6) {
            Toast.makeText(activity, "Please Enter Valid Otp", Toast.LENGTH_SHORT).show();
        } else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        bt_login_progress.setVisibility(View.GONE);
                        tv_verify.setVisibility(View.VISIBLE);
                        // Sign in success, update UI with the signed-in user's information
                        Commn.showDebugLog("signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                        Commn.showErrorLog(user.toString());
                        Commn.showErrorLog(user.getUid() + "\nPhone : " + user.getPhoneNumber());
                        sessionManager.setString(ConstantsKey.UUID, user.getUid());
                        sessionManager.setString(ConstantsKey.PHONE_NUMBER, user.getPhoneNumber());
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        USER_ID = firebaseUser.getUid();
                        checkUser();

                    } else {
                        bt_login_progress.setVisibility(View.GONE);
                        tv_verify.setVisibility(View.VISIBLE);
                        // Sign in failed, display a messageeee and update the UI
                        Commn.showDebugLog("signInWithCredential:failure : " + task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(activity, "Please Enter Valid Otp", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void checkUser() {

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(USER_ID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            ProfilePOJO profilePOJO = task.getResult().toObject(ProfilePOJO.class);
                            sessionManager.setString(ConstantsKey.PROFILE_KEY, new Gson().toJson(profilePOJO));
                            Commn.showDebugLog("user_already_exists");
                            saveUser();
                        } else {
                            Commn.showDebugLog("user_not_exists");
                            addNewUser();

                        }
                    }

                });

    }


    private void addNewUser() {

        USER_NAME = generateUserName();
        firebaseFirestore.collection(DBConstants.points_table)
                .document(DBConstants.points_table_key)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().exists()) {

                            int bonus = task.getResult().getLong(DBConstants.signup_bonus).intValue();
                            Commn.showErrorLog("table_signup_bonus:" + String.valueOf(bonus) + "");
                            registerUser(bonus);


                        } else {
                            Commn.showErrorLog("table_signup_bonus not available in table:");
                            registerUser(1000);
                        }
                    } else {
                        Commn.showErrorLog("table_signup_bonus not available in table:");
                        registerUser(1000);
                    }

                });


    }

    private void registerUser(int bonus) {
        try {
            ProfilePOJO profilePOJO = new ProfilePOJO();
            profilePOJO.setEmail(USER_EMAIL + "");
            profilePOJO.setName("");
            profilePOJO.setUserId(USER_ID + "");
            profilePOJO.setSuper_live_id("cryptok" + arrangeString(USER_ID) + "");
            profilePOJO.setGender("");
            profilePOJO.setBio("");
            profilePOJO.setDob("");
            profilePOJO.setHometown("");
            profilePOJO.setPoint(bonus);
            profilePOJO.setTotal_recieved_beans(0);
            profilePOJO.setTotal_sent_diamonds(0);
            profilePOJO.setCurrent_level("1");
            profilePOJO.setAuth_status("true");
            profilePOJO.setMobile(USER_MOBILE + "");
            profilePOJO.setImage(placeholder_user + "");
            profilePOJO.setLoginType(LOGIN_TYPE + "");
            profilePOJO.setCountry_name(COUNTRY_NAME + "");
            profilePOJO.setCountry_code(COUNTRY_CODE + "");
            sessionManager.setString(ConstantsKey.PROFILE_KEY, new Gson().toJson(profilePOJO));

            if (!activity.isFinishing()) {
                Commn.showDialog(activity);
            }
            updateFirebase(profilePOJO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String arrangeString(String SALTCHARS) {

        String str;
        if (SALTCHARS.contains("+")) {
            str = SALTCHARS.replace("+", "");
            Commn.showDebugLog("+ contained");

        } else {
            str = SALTCHARS;
            Commn.showDebugLog("+ not contained");

        }
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 7) { // length of the random string.
            int index = (int) (rnd.nextFloat() * str.length());
            salt.append(str.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr.trim();
    }

    private void saveUser() {
        sessionManager.setBoolean(ConstantsKey.IS_LOGIN, true);
        if (!activity.isFinishing()) {
            Commn.hideDialog(activity);
        }
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finishAffinity();


    }

    private void updateCountry() {

        Map messageMap = new HashMap();
        messageMap.put(DBConstants.country_name, COUNTRY_NAME);
        messageMap.put(DBConstants.country_code, COUNTRY_CODE);
        messageMap.put(DBConstants.country_id, COUNTRY_CODE);

        firebaseFirestore.collection(DBConstants.All_Countries)
                .document(COUNTRY_CODE)
                .set(messageMap)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Commn.showDebugLog("country_added:");
                        saveUser();
                    }

                }).addOnFailureListener(e -> {
            Commn.showDebugLog("country_added failure:" + e.getMessage() + "");
            FirebaseCrashlytics.getInstance().log(e.getMessage() + "");
        });

    }
    private void updateFirebase(ProfilePOJO profilePOJO) {

        //register new user
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .set(profilePOJO)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        updateCountry();

                    }
                });
    }

    private void startCountDown() {
        tv_OtpTimer.setVisibility(View.VISIBLE);
        tv_resend_code.setVisibility(View.GONE);
        countDownTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                //tv_Timer.setText("Resend OTP in: " + millisUntilFinished / 1000);
                tv_OtpTimer.setText("Didn't receive the OTP? Request for a new one in " + String.format("%d:%d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tv_resend_code.setVisibility(View.VISIBLE);
                tv_OtpTimer.setVisibility(View.GONE);
            }

        }.start();
    }

    private void onTextChange() {
        et_one.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_one.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_two.requestFocus();
                }
                if (et_one.getText().toString().length() == 0)     //size as per your requirement
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(et_four.getWindowToken(), 0);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        et_two.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_two.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_three.requestFocus();
                }
                if (et_two.getText().toString().length() == 0)     //size as per your requirement
                {
                    et_one.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        et_three.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_three.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_four.requestFocus();
                }
                if (et_three.getText().toString().length() == 0)     //size as per your requirement
                {
                    et_two.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_four.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_four.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_five.requestFocus();
                }
                if (et_four.getText().toString().length() == 0)     //size as per your requirement
                {
                    et_three.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_five.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_five.getText().toString().length() == 1)     //size as per your requirement
                {
                    et_six.requestFocus();
                }
                if (et_five.getText().toString().length() == 0)     //size as per your requirement
                {
                    et_four.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });
        et_six.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_six.getText().toString().length() == 1)     //size as per your requirement
                {
                    OTP = GetOTP();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(et_four.getWindowToken(), 0);
                }
                if (et_six.getText().toString().length() == 0)     //size as per your requirement
                {
                    et_five.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {
            }

        });

    }

    public String GetOTP() {
        String GETOTP = "";
        String Otp1 = et_one.getText().toString();
        String Otp2 = et_two.getText().toString();
        String Otp3 = et_three.getText().toString();
        String Otp4 = et_four.getText().toString();
        String Otp5 = et_five.getText().toString();
        String Otp6 = et_six.getText().toString();

        if (Otp1.equals("")) {
            Commn.myToast(context, "Enter OTP");
        } else if (Otp2.equals("")) {
            Commn.myToast(context, "Enter OTP");
        } else if (Otp3.equals("")) {
            Commn.myToast(context, "Enter OTP");
        } else if (Otp4.equals("")) {
            Commn.myToast(context, "Enter OTP");
        } else if (Otp5.equals("")) {
            Commn.myToast(context, "Enter OTP");
        } else if (Otp6.equals("")) {
            Commn.myToast(context, "Enter OTP");
        } else {
            GETOTP = Otp1 + Otp2 + Otp3 + Otp4 + Otp5 + Otp6;
        }

        return GETOTP;
    }

    protected void onResume() {


        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }


}