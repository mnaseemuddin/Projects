package com.lgt.NeWay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.gms.common.internal.service.Common;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.SingletonRequestQueue;
import com.lgt.NeWay.Neway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityChangePassword extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private ImageView ivBackFullDescription;
    private TextView tvToolbar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ProgressBar pbChangePassword;
    private String mOldPassword, mNewPassword, mConfirmPassword, mSharedPrefPassword, mUserID;

    private Common common;
    private static final String TAG = "ActivityChangePassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);





        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        ivBackFullDescription = findViewById(R.id.ivBackFullDescription);
        tvToolbar = findViewById(R.id.tvToolbar);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        pbChangePassword = findViewById(R.id.pbChangePassword);

       /* if (sharedPreferences.contains("KEY_PASSWORD")) {
            mSharedPrefPassword = sharedPreferences.getString("KEY_PASSWORD", "");
        }

        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");

        }*/

        tvToolbar.setText("Change password");

        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
               /* if (common.isConnectingToInternet()) {

                } else {
                    Toast.makeText(ActivityChangePassword.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

    }

    private void validation() {

        mOldPassword = etOldPassword.getText().toString().trim();
        mNewPassword = etNewPassword.getText().toString().trim();
        mConfirmPassword = etConfirmPassword.getText().toString().trim();


        if (TextUtils.isEmpty(mOldPassword)) {
            etOldPassword.setError("Enter old password");
            etOldPassword.requestFocus();
            return;
        }

        if (mOldPassword.length() < 4) {
            etOldPassword.setError("Old password must be at least 4 characters");
            etOldPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mNewPassword)) {
            etNewPassword.setError("Enter new password");
            etNewPassword.requestFocus();
            return;
        }

        if (mNewPassword.length() < 4) {
            etNewPassword.setError("Password must be at least 4 characters");
            etNewPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mConfirmPassword)) {
            etConfirmPassword.setError("Enter confirm password");
            etConfirmPassword.requestFocus();
            return;
        }

        if (mConfirmPassword.length() < 4) {
            etConfirmPassword.setError("Password must be at least 4 characters");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!mOldPassword.equals(mSharedPrefPassword)) {
            etOldPassword.setError("Entered old password is wrong");
            etOldPassword.requestFocus();
            return;
        }

        if (!mNewPassword.equals(mConfirmPassword)) {
            etConfirmPassword.setError("Old password & confirm password should be same");
            etConfirmPassword.requestFocus();
            return;
        }

        apiChangePassword();

    }

    private void apiChangePassword() {

        pbChangePassword.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.ChangePassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbChangePassword.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {

                        Toast.makeText(ActivityChangePassword.this, ""+message, Toast.LENGTH_SHORT).show();

                        editor.clear();
                        editor.commit();

                        Intent homeScreenIntent = new Intent(ActivityChangePassword.this, MainActivity.class);
                        homeScreenIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeScreenIntent);

                        finish();


                    } else {
                        Toast.makeText(ActivityChangePassword.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityChangePassword.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                params.put("newpassword", mNewPassword);
                params.put("oldpassword", mOldPassword);

                Log.e(TAG, "getParams: " + params);
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityChangePassword.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    }
