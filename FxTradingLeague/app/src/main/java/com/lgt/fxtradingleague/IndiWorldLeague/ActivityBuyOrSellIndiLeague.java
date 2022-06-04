package com.lgt.fxtradingleague.IndiWorldLeague;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.Extra.IndimBuyOrSaleClick;
import com.lgt.fxtradingleague.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_TEAM_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_USER_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEE;
import static com.lgt.fxtradingleague.Extra.ExtraData.SELECTED_PLAYER_INDIAN_LEAGUE_TEAM_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.UPDATE_SELL_PLAYER_INDIAN_LEAGUE_TEAM_API;

public class ActivityBuyOrSellIndiLeague extends AppCompatActivity implements IndimBuyOrSaleClick {

    String contestType, JoinUserId, ContestId, TeamId, entryFee;
    RequestQueue requestQueue;
    ArrayList<IndiBuySellModel> mDataList = new ArrayList<>();
    IndiAdapterBuyOrSell AdapterBuyOrSell;
    RecyclerView Rv_FinalPlayerListIndia;
    ImageView im_back;
    // set selected
    ProgressBar pb_progressBarSaveIndia;
    TextView tv_TeamNextIndia, tv_HeaderName;
    // set selected
    Set<String> mBuy = new HashSet<>();
    Set<String> mSale = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_buy_or_sell);
        requestQueue = Volley.newRequestQueue(this);
        if (getIntent().hasExtra(KEYS_CONTEST_TYPE)) {
            contestType = getIntent().getStringExtra(KEYS_CONTEST_TYPE);
            JoinUserId = getIntent().getStringExtra(KEYS_USER_ID);
            ContestId = getIntent().getStringExtra(KEYS_CONTEST_ID);
            TeamId = getIntent().getStringExtra(KEYS_TEAM_ID);
            entryFee = getIntent().getStringExtra(KEY_ENTRY_FEE);
            Validations.common_log("recivedData: " + contestType + ", " + JoinUserId + ", " + ContestId + ", " + TeamId + ", " + entryFee);
        }
        mDataList.clear();

        InitView();
        getDataOfJoinedTeam();
    }

    private void InitView() {
        im_back = findViewById(R.id.im_back);
        tv_TeamNextIndia = findViewById(R.id.tv_TeamNextIndia);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        pb_progressBarSaveIndia = findViewById(R.id.pb_progressBarSaveIndia);
        Rv_FinalPlayerListIndia = findViewById(R.id.Rv_FinalPlayerListIndia);
        tv_HeaderName.setText("INDIAN LEAGUE BUY OR SELL");
        setClickEvent();
    }

    private void setClickEvent() {

        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_TeamNextIndia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("total_size", "Buy : " + mBuy.size() + ",   Sale : " + mSale.size());
                int totalSize = mBuy.size() + mSale.size();
                Log.d("total_size", "" + totalSize);
                if (totalSize == 11) {
                    pb_progressBarSaveIndia.setVisibility(View.VISIBLE);
                    setJoinContestDetails();
                    Log.d("total_size", "" + totalSize);
                } else {
                    Toast.makeText(ActivityBuyOrSellIndiLeague.this, "Select Total 11 Stocks", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setJoinContestDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_SELL_PLAYER_INDIAN_LEAGUE_TEAM_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        pb_progressBarSaveIndia.setVisibility(View.GONE);
                        Intent comformation = new Intent(ActivityBuyOrSellIndiLeague.this, FinalTeamIndiActivity.class);
                        comformation.putExtra(KEYS_CONTEST_ID,ContestId);
                        comformation.putExtra(KEYS_TEAM_ID,TeamId);
                        comformation.putExtra(KEYS_USER_ID,JoinUserId);
                        comformation.putExtra(KEY_ENTRY_FEE,entryFee);
                        comformation.putExtra(KEYS_CONTEST_TYPE,contestType);
                        startActivity(comformation);
                        finish();
                    } else {
                        pb_progressBarSaveIndia.setVisibility(View.GONE);
                        Toast.makeText(ActivityBuyOrSellIndiLeague.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pb_progressBarSaveIndia.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_progressBarSaveIndia.setVisibility(View.GONE);
                Validations.common_log("error: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("team_id", TeamId);
                param.put("user_id", JoinUserId);
                param.put("contest_id", ContestId);
                param.put("s_player_id", getStringDataToList(mSale.toString()));
                param.put("b_player_id", getStringDataToList(mBuy.toString()));
                Validations.common_log("sendData: " + param.toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String getStringDataToList(String id) {
        return id.replaceAll("\\[", "").replaceAll("\\]", "");
    }

    private void getDataOfJoinedTeam() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SELECTED_PLAYER_INDIAN_LEAGUE_TEAM_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray innerData = jsonObject.getJSONArray("data");
                        for (int i = 0; i < innerData.length(); i++) {
                            JSONObject data = innerData.getJSONObject(i);
                            String tbl_indianleage_team_player_id = data.getString("tbl_indianleage_team_player_id");
                            String images = data.getString("images");
                            String company_name = data.getString("company_name");
                            String company_symbol = data.getString("company_symbol");
                            String share_qnt = data.getString("share_qnt");
                            String share_rate = data.getString("share_rate");
                            String currency_value = data.getString("currency_value");
                            String team_id = data.getString("team_id");
                            String position = data.getString("position");
                            String user_id = data.getString("user_id");
                            String contest_id = data.getString("contest_id");
                            String type = data.getString("type");
                            IndiBuySellModel indiBuySellModel = new IndiBuySellModel();
                            indiBuySellModel.setTbl_indianleage_team_player_id(tbl_indianleage_team_player_id);
                            indiBuySellModel.setImages(images);
                            indiBuySellModel.setCompany_name(company_name);
                            indiBuySellModel.setCompany_symbol(company_symbol);
                            indiBuySellModel.setShare_qnt(share_qnt);
                            indiBuySellModel.setShare_rate(share_rate);
                            indiBuySellModel.setCurrency_value(currency_value);
                            indiBuySellModel.setTeam_id(team_id);
                            indiBuySellModel.setPosition(position);
                            indiBuySellModel.setUser_id(user_id);
                            indiBuySellModel.setContest_id(contest_id);
                            indiBuySellModel.setType(type);
                            mDataList.add(indiBuySellModel);
                        }
                        setAdapterDataList();
                    } else {
                        Toast.makeText(ActivityBuyOrSellIndiLeague.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.common_log("error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("team_id", TeamId);
                param.put("user_id", JoinUserId);
                param.put("contest_id", ContestId);
                param.put("type", contestType);
                Validations.common_log("param: " + param.toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void setAdapterDataList() {
        AdapterBuyOrSell = new IndiAdapterBuyOrSell(this, mDataList, this);
        Rv_FinalPlayerListIndia.setHasFixedSize(true);
        Rv_FinalPlayerListIndia.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Rv_FinalPlayerListIndia.setAdapter(AdapterBuyOrSell);
    }

    @Override
    public void getSaleOrBuyData(String SaleID, String type_add) {
        Validations.common_log("SaleOrBuyData: " + SaleID + "  |  " + type_add);
        if (type_add.equalsIgnoreCase("Buy")) {
            if (!SaleID.equalsIgnoreCase("")) {
                if (!mSale.contains(SaleID)) {
                    if (!mBuy.contains(SaleID)) {
                        mBuy.add(SaleID);
                        Log.d("SelectSB", "Buy Add : " + mBuy.toString());
                    } else {
                        mBuy.remove(SaleID);
                        Log.d("SelectSB", "Buy Remove : " + mBuy.toString());
                    }
                } else {
                    mSale.remove(SaleID);
                    mBuy.add(SaleID);
                }
            }
        } else if (type_add.equalsIgnoreCase("Sale")) {
            if (!SaleID.equalsIgnoreCase("")) {
                if (!mBuy.contains(SaleID)) {
                    if (!mSale.contains(SaleID)) {
                        mSale.add(SaleID);
                        Log.d("SelectSB", "Sale Add : " + mSale.toString());
                    } else {
                        mSale.remove(SaleID);
                        Log.d("SelectSB", "Sale Remove : " + mSale.toString());
                    }
                } else {
                    mBuy.remove(SaleID);
                    mSale.add(SaleID);
                }
            }
        }
    }

}
