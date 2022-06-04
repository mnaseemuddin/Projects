package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivitySignUp extends AppCompatActivity {
    CheckBox signup_cheakbox;
    ProgressBar progressBar;
    EditText etName, etEmail, etMobile;
    Button btnRegisterUser;
    LinearLayout ll_back_sign_up;
    String UName, UEmail, UMobile, UOtp;
    String Name, Email, Mobile, USER_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);


        progressBar = findViewById(R.id.progress_bar_signup);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        ll_back_sign_up = findViewById(R.id.ll_back_sign_up);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);
        signup_cheakbox = findViewById(R.id.signup_cheakbox);
        ll_back_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputValidation();
            }
        });

    }

    private void InputValidation() {
        Name = etName.getText().toString().trim();
        Mobile = etMobile.getText().toString().trim();
        Email = etEmail.getText().toString().trim();


        if (TextUtils.isEmpty(Name)) {
            etName.setError("Enter name");
            etName.requestFocus();
            return;
        }

        if (Name.length() < 3) {
            etName.setError("Name must be 3 Character");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Mobile)) {
            etMobile.setError("Enter mobile number");
            etMobile.requestFocus();
            return;
        }

        if (Mobile.length() < 10) {
            etMobile.setError("Mobile number must be 10 digits");
            etMobile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(Email)) {
            etEmail.setError("Enter email Id");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            etEmail.setError("Email id is not valid");
            etEmail.requestFocus();
            return;
        }

        if (!signup_cheakbox.isChecked()) {
            signup_cheakbox.setError("Select terms and conditions");
            signup_cheakbox.requestFocus();
            return;
        }
        ApiSignUp();
    }

    private void ApiSignUp() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                try {
                    Log.e("GGDFFFFFFF", response + "");
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    Log.e("ftftftftft", status + "");

                    if (status.equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        USER_ID = object.getString("user_id");
                        UName = object.getString("name");
                        UEmail = object.getString("email");
                        UMobile = object.getString("mobile");
                        String total_wallet_amount = object.getString("total_wallet_amount");
                        UOtp = object.getString("otp");
                        Log.e("5558855", response + "");

                        Intent intent = new Intent(ActivitySignUp.this, SignUpOtpVerification.class);
                        intent.putExtra("user_id", USER_ID);
                        intent.putExtra("name", UName);
                        intent.putExtra("email", UEmail);
                        intent.putExtra("mobile", UMobile);
                        intent.putExtra("otp", UOtp);

                        startActivity(intent);
                        Toast.makeText(ActivitySignUp.this, message + "", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ActivitySignUp.this, "" + message, Toast.LENGTH_SHORT).show();
                        Log.e("Kkkkk", status + "");
                        Intent intent = new Intent(ActivitySignUp.this, ActivityLogin.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", Name);
                params.put("email", Email);
                params.put("mobile", Mobile);

                Log.e("R25698GG", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
