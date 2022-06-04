package com.lgt.NeWay.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityChangePassword extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private ImageView ivBackFullDescription;
    private TextView tvToolbar,tv_countinue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ProgressBar pbChangePassword;
    private String mOldPassword, mNewPassword, mConfirmPassword, mSharedPrefPassword, mUserID;
    public AlertDialog download_dialog;

    private static final String TAG = "ActivityChangePassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);

         inisharedpref();



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

            }
        });

    }

    private void inisharedpref() {
        sharedPreferences=getSharedPreferences(common.UserData,MODE_PRIVATE);
        mSharedPrefPassword=sharedPreferences.getString("password","");
        mUserID=sharedPreferences.getString("tbl_coaching_id","");


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
            Log.e("dhsdshdhdsj",etOldPassword+"");
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

                        diloughopen();

                        Toast.makeText(ActivityChangePassword.this, ""+message, Toast.LENGTH_SHORT).show();

                      /*  editor.clear();
                        editor.commit();

                        Intent homeScreenIntent = new Intent(ActivityChangePassword.this, MainActivity.class);
                        homeScreenIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeScreenIntent);*/

                        //
                        //
                        // finish();


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
                params.put("tbl_coaching_id", mUserID);
                params.put("new_password", mNewPassword);
                params.put("old_password", mOldPassword);

                Log.e(TAG, "getParams: " + params);
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityChangePassword.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void diloughopen() {
        View customView = LayoutInflater.from(this).inflate(R.layout.changepasswordalert, null);
        download_dialog = new AlertDialog.Builder(this).create();
        download_dialog.setView(customView);
        download_dialog.setCanceledOnTouchOutside(false);

        tv_countinue = customView.findViewById(R.id.tv_countinue);
        ivBackFullDescription = customView.findViewById(R.id.ivBackFullDescription);

        tv_countinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inshareprefrence();
            }
        });

        // tv_current_size_progress = customView.findViewById(R.id.tv_current_size_progress);
        download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        download_dialog.show();


    }

    private void inshareprefrence() {
        sharedPreferences = getSharedPreferences(common.UserData, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(common.UseriD)) {
            mUserID = sharedPreferences.getString(common.UseriD, "");
            editor = sharedPreferences.edit();
            if (mUserID!=null&& !mUserID.isEmpty()){
                editor.clear();
                editor.commit();
                editor.apply();

                startActivity(new Intent(getApplicationContext(), Login.class));

            }
            Log.e("hhhhhhhhhh",mUserID);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    }
