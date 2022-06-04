package com.lgt.fxtradingleague;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Bean.UserDetails;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.lgt.fxtradingleague.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.fxtradingleague.Extra.ExtraData.SIGN_UP_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.TERM_AND_CONDITION_LINK;

public class RegistrationActivity extends AppCompatActivity {

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    Context context;
    RegistrationActivity activity;
    CheckBox cb_termAndConditionCheck;
    TextView tv_TearmsandConditionsDialoug;
    TextInputLayout input_RegPassword, input_RegRefCode;
    EditText et_MobileNo, et_Email, et_name, et_Password, et_ReferralCode;
    TextView tv_RegNext, tv_TearmsandConditions, tv_HeaderName, et_dob;
    LinearLayout LL_Login;
    ImageView im_back;

    String MobileNumber, EmailId, Password, ReferralCode, Name, dateOfBirth;


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
        apiRequestManager = new APIRequestManager(activity);
        myCalendar = Calendar.getInstance();
        initViews();
        sessionManager = new SessionManager();
        // BITCOIN_ADDRESS = getSaltString();
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);


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
                } else if (EmailId.equals("")) {
                    et_Email.requestFocus();
                    ShowToast(context, "Please enter valid email id.");
                } else if (!Validations.isValidEmail(EmailId)) {
                    et_Email.requestFocus();
                    ShowToast(context, "Enter Valid Email Id");
                } else if (Password.equals("")) {
                    et_Password.requestFocus();
                    ShowToast(context, "Please enter password.");
                } else if (Password.length() < 8 && !Validations.isValidPassword(Password)) {
                    ShowToast(context, "Please enter valid password.");
                } else if (dateOfBirth.equals("")) {
                    ShowToast(context, "Please Select date of birth.");
                } else if (!cb_termAndConditionCheck.isChecked()) {
                    ShowToast(context, "Please Accept Term & Condition.");
                } else {
                    LoginType = "Normal";
                    callSignupApi(EmailId, ReferralCode, Password, Name, dateOfBirth, MobileNumber);
                    // not in user
                    /* if (Build.VERSION.SDK_INT >= 23) {
                        checkPermissions();
                    } else {
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


        /*      GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();     */

        tv_TearmsandConditionsDialoug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading", "TERMS & CONDITIONS");
                i.putExtra("URL", TERM_AND_CONDITION_LINK);
                startActivity(i);
            }
        });
    }

    public void initViews() {

        tv_TearmsandConditionsDialoug = findViewById(R.id.tv_TearmsandConditionsDialoug);
        cb_termAndConditionCheck = findViewById(R.id.cb_termAndConditionCheck);
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

        et_MobileNo = findViewById(R.id.et_MobileNo);
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);
        et_ReferralCode = findViewById(R.id.et_ReferralCode);


        tv_RegNext = findViewById(R.id.tv_RegNext);
        tv_TearmsandConditions = findViewById(R.id.tv_TearmsandConditions);
        // tv_SFacebookLogin = findViewById(R.id.tv_SFacebookLogin);
        //  tv_SGoogleLogin = findViewById(R.id.tv_SGoogleLogin);
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

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_dob.setText(sdf.format(myCalendar.getTime()));
    }


    private void callSignupApi(final String mEmailId, final String mReferralCode, final String mPassword, final String mName, final String mDateOfBirth, final String mMobileNumber) {
        Validations.showProgress(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGN_UP_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("callSignUpApi", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        Validations.hideProgress();
                        if (jsonObject.getJSONObject("data") != null) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String user_id = data.getString("user_id");
                            String mobile = data.getString("mobile");
                            String referral_code = data.getString("referral_code");
                            String email = data.getString("email");
                            String name = data.getString("name");
                            String birthday = data.getString("birthday");
                            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            saveLoginInformation(user_id, name, email, mobile, referral_code, birthday);
                        } else {
                            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Validations.hideProgress();
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Validations.hideProgress();
                    e.printStackTrace();
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
                param.put("email", mEmailId);
                param.put("mobile", mMobileNumber);
                param.put("referal_code_used", mReferralCode);
                param.put("name", mName);
                param.put("birthday", mDateOfBirth);
                param.put("password", mPassword);
                Log.d("callSignupApi", "" + param);
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

    /*private void setUpPaymentWallet() {
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
    }*/

    /*protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 30) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }*/

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
            // callSignupApi(EmailId,ReferralCode,Password,Name,dateOfBirth,MobileNumber);
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
