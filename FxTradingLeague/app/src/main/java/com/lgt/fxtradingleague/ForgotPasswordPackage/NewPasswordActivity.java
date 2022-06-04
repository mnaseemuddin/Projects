package com.lgt.fxtradingleague.ForgotPasswordPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.LoginActivity;
import com.lgt.fxtradingleague.MainActivity;
import com.lgt.fxtradingleague.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.lgt.fxtradingleague.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.fxtradingleague.Config.CHANGEPASSWORD;
import static com.lgt.fxtradingleague.Config.UPDATENEWPASSWORD;
import static com.lgt.fxtradingleague.Constants.CHANGEPASSWORDTPYE;
import static com.lgt.fxtradingleague.Constants.UPDATENEWPASSWORDTPYE;
import static com.lgt.fxtradingleague.Extra.ExtraData.RESET_PASSWORD_API;

public class NewPasswordActivity extends AppCompatActivity implements ResponseManager {

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    Context context;
    NewPasswordActivity activity;

    ImageView im_back;
    TextView tv_HeaderName,tv_SubmitNewPassword;
    TextInputLayout input_OldPassword,input_NewPassword,input_ConfirmNewPassword;
    EditText et_OldPassword,et_NewPassword,et_ConfirmNewPassword;

    String UserId,IntentActivity;
    String NewPassword,ConfirmNewPassword,OldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        context = activity = this;
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        initViews();

        UserId = getIntent().getStringExtra("UserId");
        IntentActivity = getIntent().getStringExtra("IntentActivity");

        if (IntentActivity.equals("ForgotPassword")){
            input_OldPassword.setVisibility(View.GONE);
        }
        else if (IntentActivity.equals("ChangePassword")){
            input_OldPassword.setVisibility(View.VISIBLE);
        }


        tv_SubmitNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPassword = et_NewPassword.getText().toString();
                ConfirmNewPassword = et_ConfirmNewPassword.getText().toString();

                if (IntentActivity.equals("ChangePassword")){
                    OldPassword = et_OldPassword.getText().toString();
                    if (OldPassword.equals("")){
                        ShowToast(context,"Enter Old Password");
                    }else if (OldPassword.length()<8&& !Validations.isValidPassword(OldPassword)){

                        ShowToast(context,"Please enter valid password.");
                    }

                    else if (NewPassword.equals("")){
                        ShowToast(context,"Enter New Password");
                    }
                    else if (NewPassword.length()<8&& !Validations.isValidPassword(NewPassword)){

                        ShowToast(context,"Please enter valid password.");
                    }
                    else if (ConfirmNewPassword.equals("")){
                        ShowToast(context,"Enter Confirm New Password");
                    }
                    else if (!NewPassword.equals(ConfirmNewPassword)){
                        ShowToast(context,"Change Password not matched with New Password.");
                    }
                    else {
                        callChangePasswordApi(true);
                    }
                }
                else {
                    if (NewPassword.equals("")) {
                        ShowToast(context, "Enter New Password");
                    }else if (NewPassword.length()<8&& !Validations.isValidPassword(NewPassword)){

                        ShowToast(context,"Password Pattern Not Macthed");
                    }
                    else if (ConfirmNewPassword.equals("")) {
                        ShowToast(context, "Enter Confirm New Password");
                    } else if (!NewPassword.equals(ConfirmNewPassword)) {
                        ShowToast(context, "Change Password not matched with New Password.");
                    } else {
                        // callUpdatePasswordApi(true);
                    }
                }

            }
        });

    }

    public void initViews(){

        tv_SubmitNewPassword = findViewById(R.id.tv_SubmitNewPassword);
        et_OldPassword = findViewById(R.id.et_OldPassword);
        et_NewPassword = findViewById(R.id.et_NewPassword);
        et_ConfirmNewPassword = findViewById(R.id.et_ConfirmNewPassword);

        input_OldPassword = findViewById(R.id.input_OldPassword);
        input_NewPassword = findViewById(R.id.input_NewPassword);
        input_ConfirmNewPassword = findViewById(R.id.input_ConfirmNewPassword);


        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_HeaderName.setText("CHANGE PASSWORD");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void callChangePasswordApi(boolean isShowLoader) {
        Validations.showProgress(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RESET_PASSWORD_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ChangePassword",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message =jsonObject.getString("message");
                    String status =jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        Validations.hideProgress();
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewPasswordActivity.this, MainActivity.class));
                        finish();
                    }else{
                        Validations.hideProgress();
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
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
                Log.d("ChangePassword",""+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("user_id",UserId);
                param.put("oldpassword",OldPassword);
                param.put("newpassword",NewPassword);
                Log.d("ChangePassword",""+param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


        try {
            apiRequestManager.callAPI(CHANGEPASSWORD,
                    createRequestJsonForgotPassword(), context, activity, CHANGEPASSWORDTPYE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callUpdatePasswordApi(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(UPDATENEWPASSWORD,
                    createRequestJsonForgotPassword(), context, activity, UPDATENEWPASSWORDTPYE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJsonForgotPassword() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", NewPassword);
            jsonObject.put("user_id", UserId);
            if (IntentActivity.equals("ChangePassword")) {
                jsonObject.put("old_password", OldPassword);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        if (IntentActivity.equals("ChangePassword")){
            Log.e("chengedd","Password changed successfully. Login now.");
            ShowToast(context,message);
            Intent i = new Intent(activity, HomeActivity.class);
            startActivity(i);
            finish();
        }
        else {
            Log.e("chengedd2",message);
            ShowToast(context,message);
            Intent i = new Intent(activity, LoginActivity.class);
            startActivity(i);
        }

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        ShowToast(context,"Invalid Old Password. Please enter registered password.");
        Log.e("mssssgggg",message);
    }

    @Override
    public void onBackPressed() {
        if (IntentActivity.equals("ForgotPassword")){
            Intent i = new Intent(activity,LoginActivity.class);
            startActivity(i);
        }
        else {
            super.onBackPressed();
        }
    }
}
