package com.lgt.fxtradingleague;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Extra.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.lgt.fxtradingleague.Extra.ExtraData.JOIN_CONTEST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_TEAM_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_USER_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEE;

public class PaymentConfirmationActivity extends AppCompatActivity {
    ViewDialog viewDialog;
    TextView tv_ConfirmationJoinContest, tv_ConfirmationToPay, tv_ConfirmationEntryFee,
            tv_WalletBalance, tv_ConfirmationBonus;
    PaymentConfirmationActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    TextView tv_HeaderName, tv_join_terms_condition;
    SessionManager sessionManager;
    String User_id, Contest_id, Team_id, Entry_fee,mKEYS_CONTEST_TYPE;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        context = activity = this;
        apiRequestManager = new APIRequestManager(activity);
        sessionManager = new SessionManager();
        viewDialog = new ViewDialog();
        if (getIntent().hasExtra(KEYS_USER_ID)) {
            Contest_id = getIntent().getStringExtra(KEYS_CONTEST_ID);
            Team_id = getIntent().getStringExtra(KEYS_TEAM_ID);
            User_id = getIntent().getStringExtra(KEYS_USER_ID);
            Entry_fee = getIntent().getStringExtra(KEY_ENTRY_FEE);
            mKEYS_CONTEST_TYPE = getIntent().getStringExtra(KEYS_CONTEST_TYPE);
            Log.d("event_type",""+mKEYS_CONTEST_TYPE);
        }
        initViews();
        tv_ConfirmationJoinContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validations.showProgress(PaymentConfirmationActivity.this);
                JoinFinalContestData();
            }
        });
    }


    private void JoinFinalContestData() {
        Log.d("JoinFinalContestData", "API: " + JOIN_CONTEST_API);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JOIN_CONTEST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JoinFinalContestData", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        Validations.hideProgress();
                        viewDialog.showDialog(activity, message,status);
                        //Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }else if(status.equalsIgnoreCase("2")){
                        Validations.hideProgress();
                        viewDialog.showDialog(activity, message,status);
                        //Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }else{
                        Validations.hideProgress();
                        viewDialog.showDialog(activity, message,status);
                        //Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
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
                Log.d("JoinFinalContestData", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("payment", Entry_fee);
                param.put("team_id", Team_id);
                param.put("user_id", User_id);
                param.put("contest_id", Contest_id);
                param.put("contest_name", mKEYS_CONTEST_TYPE);
                Log.d("JoinFinalContestData", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void initViews() {
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_ConfirmationJoinContest = findViewById(R.id.tv_ConfirmationJoinContest);
        tv_ConfirmationToPay = findViewById(R.id.tv_ConfirmationToPay);
        tv_ConfirmationEntryFee = findViewById(R.id.tv_ConfirmationEntryFee);
        tv_WalletBalance = findViewById(R.id.tv_WalletBalance);
        tv_ConfirmationBonus = findViewById(R.id.tv_ConfirmationBonus);
        tv_join_terms_condition = findViewById(R.id.tv_join_terms_condition);
        tv_join_terms_condition.setText("By Joining this contest, you accept " + getString(R.string.app_name) + "'s T & C and confirm that you are not a resident of Assam, Odisha, Telangana, Nagaland or Sikkim.");
        tv_join_terms_condition.setVisibility(View.GONE);
        tv_HeaderName.setText("CONFIRMATION");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_ConfirmationEntryFee.setText(Entry_fee);
    }

}
