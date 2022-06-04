package com.lgt.fxtradingleague.WorldLeague;

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
import com.lgt.fxtradingleague.Extra.WorldBuyOrSaleClick;
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
import static com.lgt.fxtradingleague.Extra.ExtraData.UPDATE_WORLD_TEAM_BUY_OR_SALE_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.WORLD_TEAM_SELECTED_PLAYER_API;

public class ActivityBuyOrSaleWorldLeague extends AppCompatActivity implements WorldBuyOrSaleClick {
    String contestType, JoinUserId, ContestId, TeamId,entryFee;
    ImageView im_back;
    ProgressBar pb_progressBarSaveWorld;
    RecyclerView Rv_FinalPlayerListWorld;
    // get data from server api list
    ArrayList<BuyOrSaleModel> mDataListBY;
    BuyOrSaleAdapter mBuyOrSaleAdapter;
    // set selected
    TextView tv_TeamNextWorld, tv_HeaderName;
    // set selected
    Set<String> mBuy = new HashSet<>();
    Set<String> mSale = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_buy_or_sale);
        if (getIntent().hasExtra(KEYS_CONTEST_TYPE)) {
            contestType = getIntent().getStringExtra(KEYS_CONTEST_TYPE);
            JoinUserId = getIntent().getStringExtra(KEYS_USER_ID);
            ContestId = getIntent().getStringExtra(KEYS_CONTEST_ID);
            TeamId = getIntent().getStringExtra(KEYS_TEAM_ID);
            entryFee = getIntent().getStringExtra(KEY_ENTRY_FEE);
            Validations.common_log("recivedData: " + contestType + ", " + JoinUserId + ", " + ContestId + ", " + TeamId);
        }
        initView();
    }

    private void initView() {
        mBuy.clear();
        mSale.clear();
        mDataListBY = new ArrayList<>();
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_TeamNextWorld = findViewById(R.id.tv_TeamNextWorld);
        Rv_FinalPlayerListWorld = findViewById(R.id.Rv_FinalPlayerListWorld);
        pb_progressBarSaveWorld = findViewById(R.id.pb_progressBarSaveWorld);
        tv_HeaderName.setText("CHOOSE BUY OR SELL");
        setupClickEvents();
        getDataFromJoinTeam();
    }

    private void getDataFromJoinTeam() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WORLD_TEAM_SELECTED_PLAYER_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("DataFromJoin: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject innerData = jsonArray.getJSONObject(i);
                            String tbl_worldleage_team_player_id = innerData.getString("tbl_worldleage_team_player_id");
                            String images = innerData.getString("images");
                            String company_symbol = innerData.getString("company_symbol");
                            String share_qnt = innerData.getString("share_qnt");
                            String share_rate = innerData.getString("share_rate");
                            String currency_value = innerData.getString("currency_value");
                            String team_id = innerData.getString("team_id");
                            String position = innerData.getString("position");
                            String user_id = innerData.getString("user_id");
                            String contest_id = innerData.getString("contest_id");
                            String type = innerData.getString("type");
                            BuyOrSaleModel model = new BuyOrSaleModel();
                            model.setCompany_symbol(company_symbol);
                            model.setImages(images);
                            model.setContest_id(contest_id);
                            model.setCurrency_value(currency_value);
                            model.setPosition(position);
                            model.setShare_qnt(share_qnt);
                            model.setShare_rate(share_rate);
                            model.setTbl_worldleage_team_player_id(tbl_worldleage_team_player_id);
                            model.setTeam_id(team_id);
                            model.setUser_id(user_id);
                            model.setType(type);
                            mDataListBY.add(model);
                        }
                        setAdapterToRecyclerView();
                    } else {
                        Toast.makeText(ActivityBuyOrSaleWorldLeague.this, message, Toast.LENGTH_SHORT).show();
                    }
                    Validations.common_log("SendRequest: " + jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.common_log("SendRequest: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("type", contestType);
                param.put("contest_id", ContestId);
                param.put("user_id", JoinUserId);
                param.put("team_id", TeamId);
                Validations.common_log("SendRequest: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setAdapterToRecyclerView() {
        mBuyOrSaleAdapter = new BuyOrSaleAdapter(mDataListBY, this, this);
        Rv_FinalPlayerListWorld.setHasFixedSize(true);
        Rv_FinalPlayerListWorld.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Rv_FinalPlayerListWorld.setAdapter(mBuyOrSaleAdapter);
    }

    private void setupClickEvents() {
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_TeamNextWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("total_size", "Buy : " + mBuy.size() + ",   Sale : " + mSale.size());
                int totalSize = mBuy.size() + mSale.size();
                Log.d("total_size", "" + totalSize);
                if (totalSize == 11) {
                    pb_progressBarSaveWorld.setVisibility(View.VISIBLE);
                    setJoinContestDetails();
                    Log.d("total_size", "" + totalSize);
                } else {
                    Toast.makeText(ActivityBuyOrSaleWorldLeague.this, "Select Total 11 Stocks", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setJoinContestDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_WORLD_TEAM_BUY_OR_SALE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("JoinContestDetails: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        pb_progressBarSaveWorld.setVisibility(View.GONE);
                        Intent comformation = new Intent(ActivityBuyOrSaleWorldLeague.this, finalCreateTeamActivity.class);
                        comformation.putExtra(KEYS_CONTEST_ID,ContestId);
                        comformation.putExtra(KEYS_TEAM_ID,TeamId);
                        comformation.putExtra(KEYS_USER_ID,JoinUserId);
                        comformation.putExtra(KEY_ENTRY_FEE,entryFee);
                        comformation.putExtra(KEYS_CONTEST_TYPE,contestType);
                        startActivity(comformation);
                        finish();
                    } else {
                        pb_progressBarSaveWorld.setVisibility(View.GONE);
                        Toast.makeText(ActivityBuyOrSaleWorldLeague.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_progressBarSaveWorld.setVisibility(View.GONE);
                Validations.common_log("JoinContestDetails: " + error.getMessage());
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
                Validations.common_log("JoinContestDetails: " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ActivityBuyOrSaleWorldLeague.this);
        requestQueue.add(stringRequest);
    }

    private String getStringDataToList(String id) {
        return id.replaceAll("\\[", "").replaceAll("\\]", "");
    }

    @Override
    public void getSaleOrBuyData(String Buy, String type) {
        Validations.common_log("SaleOrBuyData: " + Buy + "  |  " + type);
        if (type.equalsIgnoreCase("Buy")) {
            if (!Buy.equalsIgnoreCase("")) {
                if (!mSale.contains(Buy)) {
                    if (!mBuy.contains(Buy)) {
                        mBuy.add(Buy);
                        Log.d("SelectSB", "Buy Add : " + mBuy.toString());
                    } else {
                        mBuy.remove(Buy);
                        Log.d("SelectSB", "Buy Remove : " + mBuy.toString());
                    }
                } else {
                    mSale.remove(Buy);
                    mBuy.add(Buy);
                }
            }
        } else if (type.equalsIgnoreCase("Sale")) {
            if (!Buy.equalsIgnoreCase("")) {
                if (!mBuy.contains(Buy)) {
                    if (!mSale.contains(Buy)) {
                        mSale.add(Buy);
                        Log.d("SelectSB", "Sale Add : " + mSale.toString());
                    } else {
                        mSale.remove(Buy);
                        Log.d("SelectSB", "Sale Remove : " + mSale.toString());
                    }
                } else {
                    mBuy.remove(Buy);
                    mSale.add(Buy);
                }
            }
        }
    }
}
