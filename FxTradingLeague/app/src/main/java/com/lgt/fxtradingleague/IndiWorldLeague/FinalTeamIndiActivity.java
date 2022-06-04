package com.lgt.fxtradingleague.IndiWorldLeague;

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

import static com.lgt.fxtradingleague.Extra.ExtraData.JOIN_INDIAN_LEAGUE_CONTEST_TEAM_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_TEAM_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_USER_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEE;

public class FinalTeamIndiActivity extends AppCompatActivity {
    TextView tv_ConfirmationIndiaEntryFee, tv_ConfirmationIndiaJoinContest, tv_IndiaPackageBalance, tv_HeaderName;
    String userID, teamID, contestID, contestType, entryFee;
    ImageView im_back;
    ViewDialog viewDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_final_team_create);
        viewDialog = new ViewDialog();
        if (getIntent().hasExtra(KEYS_CONTEST_ID)) {
            userID = getIntent().getStringExtra(KEYS_USER_ID);
            teamID = getIntent().getStringExtra(KEYS_TEAM_ID);
            contestID = getIntent().getStringExtra(KEYS_CONTEST_ID);
            contestType = getIntent().getStringExtra(KEYS_CONTEST_TYPE);
            entryFee = getIntent().getStringExtra(KEY_ENTRY_FEE);
            Validations.common_log("shareData: " + userID + "," + teamID + "," + contestID + "," + contestType + "," + entryFee);
            initView();
        }
    }

    private void initView() {
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_IndiaPackageBalance = findViewById(R.id.tv_IndiaPackageBalance);
        tv_ConfirmationIndiaEntryFee = findViewById(R.id.tv_ConfirmationIndiaEntryFee);
        tv_ConfirmationIndiaJoinContest = findViewById(R.id.tv_ConfirmationIndiaJoinContest);
        tv_HeaderName.setText("CONFIRMATION");
        tv_ConfirmationIndiaEntryFee.setText(" " + entryFee);
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
        tv_ConfirmationIndiaJoinContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validations.showProgress(FinalTeamIndiActivity.this);
                getFinalConformationJoinIndiaLeague();
            }
        });
    }

    public void getFinalConformationJoinIndiaLeague() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JOIN_INDIAN_LEAGUE_CONTEST_TEAM_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("JoinIndiaLeague response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        Validations.hideProgress();
                        viewDialog.showDialog(FinalTeamIndiActivity.this, message,status);
                        //Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }else if(status.equalsIgnoreCase("2")){
                        Validations.hideProgress();
                        viewDialog.showDialog(FinalTeamIndiActivity.this, message,status);
                        //Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }else{
                        Validations.hideProgress();
                        viewDialog.showDialog(FinalTeamIndiActivity.this, message,status);
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
                Validations.common_log("JoinIndiaLeague param: " + param.toString());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
