package com.lgt.Ace11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.Ace11.APICallingPackage.Class.Validations;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ROIWithdrawActivity extends AppCompatActivity {

    private TextView tvToolbarTitle,tvProceed;
    private EditText et_MobileNo,etWithDrawAmount,etbank_holder_name,et_account_no,et_ifsc;
    private ImageView ivBackToolbar;
    private boolean all_filled=false;
    private String mobile, mAmount,holder_name,bank_account_no,ifsc,client_id;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roi_withdraw);

        Initialization();

    }

    private void Initialization() {
        sessionManager = new SessionManager();
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        ivBackToolbar = findViewById(R.id.ivBackToolbar);

        tvProceed = findViewById(R.id.tvProceed);
        et_MobileNo = findViewById(R.id.et_MobileNo);
        etbank_holder_name = findViewById(R.id.etbank_holder_name);
        et_account_no = findViewById(R.id.et_account_no);
        et_ifsc = findViewById(R.id.et_ifsc);

        etWithDrawAmount = findViewById(R.id.etWithDrawAmount);

        ivBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvToolbarTitle.setText("Withdraw");


        tvProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void validation() {
        mobile=et_MobileNo.getText().toString().trim();
        mAmount = etWithDrawAmount.getText().toString().trim();
        holder_name=etbank_holder_name.getText().toString().trim();
        bank_account_no=et_account_no.getText().toString().trim();
        ifsc=et_ifsc.getText().toString().trim();
        all_filled=false;
        if (!mobile.matches(Validations.MobilePattern)){
            et_MobileNo.setError("Enter Valid Mobile");
            et_MobileNo.requestFocus();
            all_filled=true;
        }

        if(TextUtils.isEmpty(mAmount)){
            etWithDrawAmount.setError("Enter amount to withdraw");
            etWithDrawAmount.requestFocus();
            all_filled=true;
        }

        if (!TextUtils.isEmpty(mAmount)){
            if(Float.parseFloat(mAmount)<10){
                etWithDrawAmount.setError("Minimum amount should be 10 Rs");
                etWithDrawAmount.requestFocus();
                all_filled=true;
            }
        }

        if (TextUtils.isEmpty(holder_name)){
            etbank_holder_name.setError("Enter Bank Holder Name");
            etbank_holder_name.requestFocus();
            all_filled=true;
        }
        if (TextUtils.isEmpty(bank_account_no)){
            et_account_no.setError("Enter Account Number");
            et_account_no.requestFocus();
            all_filled=true;
        }
        if (TextUtils.isEmpty(ifsc)){
            et_ifsc.setError("Enter Your Bank IFSC");
            et_ifsc.requestFocus();
            all_filled=true;
        }

        if (!all_filled){

            transferApi();
        }

        //Toast.makeText(ROIWithdrawActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();

    }

    private void transferApi() {
        client_id=System.currentTimeMillis()+"-"+sessionManager.getUser(ROIWithdrawActivity.this).getUser_id()+"-"+mAmount;
        Validations.showProgress(ROIWithdrawActivity.this);
        StringRequest request=new StringRequest(Request.Method.POST, Config.roi_withdraw, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.hideProgress();
                Log.e("my_roi_withdraw",response+"");

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String message=jsonObject.getString("message");
                    if (status.equalsIgnoreCase("0")){
                        successDialog(message);
                    }
                    if (status.equalsIgnoreCase("2")){
                        errorDialog(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Validations.hideProgress();
                errorDialog(error.getMessage());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String>params_headers=new HashMap<>();
                params_headers.put("Accept","application/json");
                params_headers.put("Authorization",Constants.access_token);
                return params_headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("mobile_number",mobile);
                params.put("amount",mAmount);
                params.put("beneficiary_name",holder_name);
                params.put("account_number",bank_account_no);
                params.put("ifsc",ifsc);
                params.put("channel_id","2");
                params.put("client_id",client_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ROIWithdrawActivity.this);
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(true);
        requestQueue.add(request);
    }

    private void successDialog(String message) {

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText(message)
                .show();
    }

    private void errorDialog(String message) {

        new SweetAlertDialog(ROIWithdrawActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(message)
                .show();
    }
}
