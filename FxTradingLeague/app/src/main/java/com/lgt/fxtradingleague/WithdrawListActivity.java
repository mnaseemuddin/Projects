package com.lgt.fxtradingleague;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.Adapter.WithDrawModel;
import com.lgt.fxtradingleague.Adapter.WithdrawAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lgt.fxtradingleague.Extra.ExtraData.AMOUNT_WITHDRAW_LIST_API;

public class WithdrawListActivity extends AppCompatActivity {
    ImageView im_back;
    TextView tv_HeaderName,tv_no_data;
    WithdrawAdapter withdrawAdapter;
    RecyclerView rv_withdraw_list;
    SessionManager sessionManager;
    String client_id;
    List<WithDrawModel> mListData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdrawl_list_layout);
        sessionManager = new SessionManager();
        initView();
    }

    private void initView() {
        rv_withdraw_list = findViewById(R.id.rv_withdraw_list);
        im_back = findViewById(R.id.im_back);
        tv_no_data = findViewById(R.id.tv_no_data);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_HeaderName.setText("Withdraw History");
        getApplyWithdrawList();
    }

    private void getApplyWithdrawList() {
         client_id=sessionManager.getUser(WithdrawListActivity.this).getUser_id();
        Validations.showProgress(WithdrawListActivity.this);
        StringRequest request=new StringRequest(Request.Method.POST, AMOUNT_WITHDRAW_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.hideProgress();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String message=jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){
                        Log.e("getApplyWithdrawList",response+"");
                        if (jsonObject.getJSONArray("data") != null){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                String tbl_withdrawn_request_id = data.getString("tbl_withdrawn_request_id");
                                String user_id = data.getString("user_id");
                                String mobile = data.getString("mobile");
                                String withdrawn_amount = data.getString("withdrawn_amount");
                                String bank_holder_name = data.getString("bank_holder_name");
                                String account_number = data.getString("account_number");
                                String IFSC = data.getString("IFSC");
                                String approval_status = data.getString("status");
                                String request_date = data.getString("request_date");
                                WithDrawModel model = new WithDrawModel(tbl_withdrawn_request_id,user_id,
                                        mobile,withdrawn_amount,bank_holder_name,account_number,IFSC,approval_status,request_date);
                                mListData.add(model);
                            }
                            setAdapter(mListData);
                        }
                    }else{
                        tv_no_data.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.hideProgress();
                Log.e("getApplyWithdrawList",error+"");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("user_id",client_id);
                Log.e("getApplyWithdrawList",params+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(WithdrawListActivity.this);
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(true);
        requestQueue.add(request);
    }

    private void setAdapter(List<WithDrawModel> mListData) {
        rv_withdraw_list.setHasFixedSize(true);
        rv_withdraw_list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        withdrawAdapter = new WithdrawAdapter(this, mListData);
        rv_withdraw_list.setAdapter(withdrawAdapter);
    }

}
