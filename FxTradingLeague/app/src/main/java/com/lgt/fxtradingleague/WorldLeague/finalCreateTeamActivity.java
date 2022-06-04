package com.lgt.fxtradingleague.WorldLeague;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.Extra.ViewDialog;
import com.lgt.fxtradingleague.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.lgt.fxtradingleague.Extra.ExtraData.FINAL_JOIN_WORLD_LEAGUE_CONTEST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_TEAM_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_USER_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEE;

public class finalCreateTeamActivity extends AppCompatActivity {
    TextView tv_WorldPackageBalance, tv_ConfirmationWorldEntryFee, tv_ConfirmationWorldJoinContest, tv_HeaderName;
    String userID, teamID, contestID, contestType, entryFee;
    ImageView im_back;
    ViewDialog viewDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_layout_join_activity);
        viewDialog = new ViewDialog();
        if (getIntent().hasExtra(KEYS_CONTEST_ID)) {
            userID = getIntent().getStringExtra(KEYS_USER_ID);
            teamID = getIntent().getStringExtra(KEYS_TEAM_ID);
            contestID = getIntent().getStringExtra(KEYS_CONTEST_ID);
            contestType = getIntent().getStringExtra(KEYS_CONTEST_TYPE);
            entryFee = getIntent().getStringExtra(KEY_ENTRY_FEE);
            Validations.common_log("shareData: " + userID + "," + teamID + "," + contestID + "," + contestType + "," + entryFee);
        }
        initView();
    }

    private void initView() {
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_WorldPackageBalance = findViewById(R.id.tv_WorldPackageBalance);
        tv_ConfirmationWorldEntryFee = findViewById(R.id.tv_ConfirmationWorldEntryFee);
        tv_ConfirmationWorldJoinContest = findViewById(R.id.tv_ConfirmationWorldJoinContest);
        tv_HeaderName.setText("CONFIRMATION");
        tv_ConfirmationWorldEntryFee.setText(entryFee);
        tv_WorldPackageBalance.setText(entryFee);
        setEventClick();
    }

    private void setEventClick() {
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // final conformation
        tv_ConfirmationWorldJoinContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validations.showProgress(finalCreateTeamActivity.this);
                getFinalConformationJoinWorldLeague();
            }
        });
    }

    public void getFinalConformationJoinWorldLeague() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FINAL_JOIN_WORLD_LEAGUE_CONTEST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        Validations.hideProgress();
                        viewDialog.showDialog(finalCreateTeamActivity.this, message,status);
                        //Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }else if(status.equalsIgnoreCase("2")){
                        Validations.hideProgress();
                        viewDialog.showDialog(finalCreateTeamActivity.this, message,status);
                        //Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }else{
                        Validations.hideProgress();
                        viewDialog.showDialog(finalCreateTeamActivity.this, message,status);
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
                Validations.common_log("error, " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("team_id", teamID);
                param.put("user_id", userID);
                param.put("contest_id", contestID);
                param.put("payment", entryFee);
                param.put("contest_name", contestType);
                Validations.common_log("param: " + param.toString());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
