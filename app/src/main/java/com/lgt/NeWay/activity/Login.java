package com.lgt.NeWay.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText etMobile, etPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvForgotPassword;
    private String UetMobile, UetPassword, UbtnLogin, UtvSignUp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Dialog dialog;
    BottomSheetDialog dialogForgotpassword;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iniViews();
        onclick();
        iniSharedprefrences();
    }

    private void iniSharedprefrences() {
        sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void iniViews() {
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

    }

    private void onclick() {
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("cgxxxxxx", "aaaaaaaaaaaaa");
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheet();
            }
        });
    }


    private void Validation() {
        UetMobile = etMobile.getText().toString();
        UetPassword = etPassword.getText().toString();

        if (TextUtils.isEmpty(UetMobile)) {
            etMobile.setError("Please Enter MObile No.");
            etMobile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(UetPassword)) {
            etPassword.setError("Please Enter Password");
            etPassword.requestFocus();
            return;

        }

        LoginApi();
    }
    private void bottomsheet() {


        BottomSheetForgotpassword bottomSheetMatchStatus = new BottomSheetForgotpassword();
        bottomSheetMatchStatus.show(getSupportFragmentManager(), "BottomSheetForgotpassword");

        

    }
    private void LoginApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("checkuserid", response + "");
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject object = jsonArray.getJSONObject(0);

                        String tbl_coaching_id = object.getString("tbl_coaching_id");
                        String coaching_name = object.getString("coaching_name");
                        String contact_no = object.getString("contact_no");
                        String email_id = object.getString("email_id");
                        String owner_name = object.getString("owner_name");
                        String special_course = object.getString("special_course");
                        String block_no = object.getString("block_no");
                        String password = object.getString("password");
                        String state = object.getString("state");
                        String pincode = object.getString("pincode");
                        String full_address = object.getString("full_address");
                        String latitude = object.getString("latitude");
                        String longitude = object.getString("longitude");
                        Toast.makeText(Login.this, message + "", Toast.LENGTH_LONG).show();
                        editor.putString("tbl_coaching_id",tbl_coaching_id);
                        editor.putString("password",password);
                        editor.putString("coaching_name", coaching_name);
                        editor.putString("contact_no", contact_no);
                        editor.putString("owner_name", owner_name);
                        editor.putString("full_address", full_address);
                        editor.putString("latitude", latitude);
                        editor.putString("longitude", longitude);
                        editor.commit();
                        editor.apply();


                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivity(i);

                    }else {
                        if (status.equals("0")){
                            Toast.makeText(Login.this, message + "", Toast.LENGTH_LONG).show();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("contact_no", UetMobile);
                param.put("password", UetPassword);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}

