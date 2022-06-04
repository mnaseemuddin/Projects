package com.lgt.fxtradingleague.IndiWorldLeague;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.Adapter.IndiCompanyAdapter;
import com.lgt.fxtradingleague.Extra.IndiCompanyTrade;
import com.lgt.fxtradingleague.IndiModel;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lgt.fxtradingleague.Extra.ExtraData.CREATE_INDI_COMPANY_JOIN_TEAM_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.INDI_COMPANY_RATE_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_TEAM_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_USER_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEES;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_JOIN_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_JOIN_MATCH;

public class ActivityIndiWorldLeague extends AppCompatActivity implements IndiCompanyTrade {
    // show alert
    BottomSheetDialog mDialogCompanyInfo;
    TextView tv_coin_name, tv_coin_info_text, tv_closedPrice, tv_intraChanged, tv_priceFour, tv_priceThree, tv_priceTwo, tv_priceOne;
    TextView tv_text_close_price, tv_text_open_price, tv_dateOne, tv_dateTwo, tv_dateThree, tv_datefour, tv_types_rate, tv_types;
    ImageView ivBackToolbar;
    // list recycler
    RecyclerView rv_IndiLeague_list;
    TextView tv_no_eventIndiLeague, tv_IndiLeague_Stock, tv_IndiLeagueTargetMoney, addIndiLeagueTeam;
    String ContextType = "", JoinMatch = "", JoinKeyId = "", EntryFees = "", UserLoginID = "";
    List<IndiModel> mIndiLeagueLis = new ArrayList<>();
    RequestQueue requestQueue;
    IndiCompanyAdapter indiCompanyAdapter;
    ProgressBar pb_IndiLeagueSelectProgress;
    EditText et_searchIndiLeagueName;
    // set data to list
    Set<String> mCompanySymbol = new HashSet<>();
    Set<String> mSharedPrice = new HashSet<>();
    // progress bar
    ProgressBar pb_priceIndiLeagueProgress;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_indi_world_league);
        sessionManager = new SessionManager();
        if (getIntent().hasExtra(KEYS_CONTEST_TYPE)) {
            ContextType = getIntent().getStringExtra(KEYS_CONTEST_TYPE);
            JoinMatch = getIntent().getStringExtra(KEY_JOIN_MATCH);
            JoinKeyId = getIntent().getStringExtra(KEY_JOIN_ID);
            EntryFees = getIntent().getStringExtra(KEY_ENTRY_FEES);
            UserLoginID = sessionManager.getUser(this).getUser_id();
        }
        initView();
    }

    private void initView() {
        mIndiLeagueLis.clear();
        mCompanySymbol.clear();
        mSharedPrice.clear();
        requestQueue = Volley.newRequestQueue(this);
        tv_IndiLeague_Stock = findViewById(R.id.tv_IndiLeague_Stock);
        rv_IndiLeague_list = findViewById(R.id.rv_IndiLeague_list);
        addIndiLeagueTeam = findViewById(R.id.addIndiLeagueTeam);
        ivBackToolbar = findViewById(R.id.ivBackToolbar);
        et_searchIndiLeagueName = findViewById(R.id.et_searchIndiLeagueName);
        tv_no_eventIndiLeague = findViewById(R.id.tv_no_eventIndiLeague);
        tv_IndiLeagueTargetMoney = findViewById(R.id.tv_IndiLeagueTargetMoney);
        pb_priceIndiLeagueProgress = findViewById(R.id.pb_priceIndiLeagueProgress);
        pb_IndiLeagueSelectProgress = findViewById(R.id.pb_IndiLeagueSelectProgress);
        // get data
        getIndiLeagueData();
        // set data
        addIndiLeagueTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCompanySymbol.size() == 11) {
                    pb_priceIndiLeagueProgress.setVisibility(View.VISIBLE);
                    setDataToAddTeam();
                } else {
                    Toast.makeText(ActivityIndiWorldLeague.this, "Please Select 11 Items.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        et_searchIndiLeagueName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    startSearch(charSequence.toString());
                } else {
                    setDataToAdapter();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void startSearch(String key) {
        ArrayList<IndiModel> tempLits = new ArrayList<>();
        for (IndiModel temp : mIndiLeagueLis) {
            if (temp.getCompany_name().toLowerCase().contains(key.toLowerCase())) {
                tempLits.add(temp);
            }
        }
        indiCompanyAdapter = new IndiCompanyAdapter(this, tempLits, this);
        rv_IndiLeague_list.setAdapter(indiCompanyAdapter);
        indiCompanyAdapter.notifyDataSetChanged();
    }

    private void setDataToAddTeam() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_INDI_COMPANY_JOIN_TEAM_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("send_data: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String team_id = jsonObject.getString("team_id");
                        String user_id = jsonObject.getString("user_id");
                        String type = jsonObject.getString("type");
                        String contest_price = jsonObject.getString("contest_price");
                        String contest_id = jsonObject.getString("contest_id");
                        String created_date = jsonObject.getString("created_date");
                         pb_priceIndiLeagueProgress.setVisibility(View.GONE);
                        Intent goToNext = new Intent(ActivityIndiWorldLeague.this, ActivityBuyOrSellIndiLeague.class);
                        goToNext.putExtra(KEYS_USER_ID, user_id);
                        goToNext.putExtra(KEYS_TEAM_ID, team_id);
                        goToNext.putExtra(KEYS_CONTEST_TYPE, type);
                        goToNext.putExtra(KEYS_CONTEST_ID, contest_id);
                        goToNext.putExtra(KEY_ENTRY_FEE, contest_price);
                        startActivity(goToNext);
                        Toast.makeText(ActivityIndiWorldLeague.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        pb_priceIndiLeagueProgress.setVisibility(View.GONE);
                        Toast.makeText(ActivityIndiWorldLeague.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_priceIndiLeagueProgress.setVisibility(View.GONE);
                Validations.common_log("send_data: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("company_symbol", getStringDataToList(mCompanySymbol.toString()));
                param.put("company_name", getStringDataToList(mSharedPrice.toString()));
                param.put("user_id", UserLoginID);
                param.put("contest_id", JoinKeyId);
                param.put("contest_price", EntryFees);
                param.put("type", ContextType);
                Validations.common_log("send_data: " + param.toString());
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String getStringDataToList(String id) {
        return id.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
    }

    private void getIndiLeagueData() {
        pb_priceIndiLeagueProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, INDI_COMPANY_RATE_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String tbl_indian_league_id = data.getString("tbl_indian_league_id");
                            String image = data.getString("image");
                            String symbol = data.getString("symbol");
                            String symbol_to_display = data.getString("symbol_to_display");
                            String company_name = data.getString("company_name");
                            String open_price = data.getString("open_price");
                            String close_price = data.getString("close_price");
                            String currency_value = data.getString("currency_value");
                            String day_high_price = data.getString("day_high_price");
                            String day_low_price = data.getString("day_low_price");
                            String volume = data.getString("volume");
                            String privious_close_price = data.getString("privious_close_price");
                            IndiModel indiModel = new IndiModel();
                            indiModel.setTbl_indian_league_id(tbl_indian_league_id);
                            indiModel.setImage(image);
                            indiModel.setSymbol(symbol);
                            indiModel.setSymbol_to_display(symbol_to_display);
                            indiModel.setCompany_name(company_name);
                            indiModel.setCurrency_value(currency_value);
                            indiModel.setOpen_price(open_price);
                            indiModel.setClose_price(close_price);
                            indiModel.setDay_high_price(day_high_price);
                            indiModel.setDay_low_price(day_low_price);
                            indiModel.setVolume(volume);
                            indiModel.setPrivious_close_price(privious_close_price);
                            mIndiLeagueLis.add(indiModel);
                        }
                        setDataToAdapter();
                    } else {
                        pb_priceIndiLeagueProgress.setVisibility(View.GONE);
                        Validations.common_log("no data found!");
                        Toast.makeText(ActivityIndiWorldLeague.this, "" + message, Toast.LENGTH_SHORT).show();
                        tv_no_eventIndiLeague.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_priceIndiLeagueProgress.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_priceIndiLeagueProgress.setVisibility(View.GONE);
                Validations.common_log("Error: " + error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }

    private void setDataToAdapter() {
        pb_priceIndiLeagueProgress.setVisibility(View.GONE);
        indiCompanyAdapter = new IndiCompanyAdapter(this, mIndiLeagueLis, this);
        rv_IndiLeague_list.setHasFixedSize(true);
        rv_IndiLeague_list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rv_IndiLeague_list.setAdapter(indiCompanyAdapter);
    }

    @Override
    public void selectedItemsCount(int size) {
        // set item selected
        tv_IndiLeague_Stock.setText(size + "/");
        // set progress
        pb_IndiLeagueSelectProgress.setProgress(size);
        // set pending price
        switch (size) {
            case 0:
                tv_IndiLeagueTargetMoney.setText("₹ 11,00,000.00");
                break;
            case 1:
                tv_IndiLeagueTargetMoney.setText("₹ 10,00,000.00");
                break;
            case 2:
                tv_IndiLeagueTargetMoney.setText("₹ 9,00,000.00");
                break;
            case 3:
                tv_IndiLeagueTargetMoney.setText("₹ 8,00,000.00");
                break;
            case 4:
                tv_IndiLeagueTargetMoney.setText("₹ 7,00,000.00");
                break;
            case 5:
                tv_IndiLeagueTargetMoney.setText("₹ 6,00,000.00");
                break;
            case 6:
                tv_IndiLeagueTargetMoney.setText("₹ 5,00,000.00");
                break;
            case 7:
                tv_IndiLeagueTargetMoney.setText("₹ 4,00,000.00");
                break;
            case 8:
                tv_IndiLeagueTargetMoney.setText("₹ 3,00,000.00");
                break;
            case 9:
                tv_IndiLeagueTargetMoney.setText("₹ 2,00,000.00");
                break;
            case 10:
                tv_IndiLeagueTargetMoney.setText("₹ 1,00,000.00");
                break;
            case 11:
                tv_IndiLeagueTargetMoney.setText("₹ 00.00");
                break;
        }
    }

    @Override
    public void setDataOfSelectedList(String symbol, String price) {
        if (!symbol.equalsIgnoreCase("")) {
            if (!mCompanySymbol.contains(symbol)) {
                mCompanySymbol.add(symbol);
                mSharedPrice.add(price);
                Validations.common_log("Add: " + mSharedPrice.toString() + "  |  " + mCompanySymbol.toString());
            } else {
                mCompanySymbol.remove(symbol);
                mSharedPrice.remove(price);
                Validations.common_log("Removed: " + mSharedPrice.toString() + "  |  " + mCompanySymbol.toString());
            }
        }
    }

    @Override
    public void showDetailsAboutCompany(IndiModel indiModel) {
        mDialogCompanyInfo = new BottomSheetDialog(this, R.style.SheetDialog);
        mDialogCompanyInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogCompanyInfo.setContentView(R.layout.coin_info_dailoug_layout);
        // find ids
        tv_text_close_price = mDialogCompanyInfo.findViewById(R.id.tv_text_close_price);
        tv_text_open_price = mDialogCompanyInfo.findViewById(R.id.tv_text_open_price);
        tv_types_rate = mDialogCompanyInfo.findViewById(R.id.tv_types_rate);
        tv_types = mDialogCompanyInfo.findViewById(R.id.tv_types);
        tv_dateOne = mDialogCompanyInfo.findViewById(R.id.tv_dateOne);
        tv_dateTwo = mDialogCompanyInfo.findViewById(R.id.tv_dateTwo);
        tv_dateThree = mDialogCompanyInfo.findViewById(R.id.tv_dateThree);
        tv_datefour = mDialogCompanyInfo.findViewById(R.id.tv_datefour);
        tv_coin_name = mDialogCompanyInfo.findViewById(R.id.tv_coin_name);
        tv_coin_info_text = mDialogCompanyInfo.findViewById(R.id.tv_coin_info_text);
        tv_closedPrice = mDialogCompanyInfo.findViewById(R.id.tv_closedPrice);
        tv_intraChanged = mDialogCompanyInfo.findViewById(R.id.tv_intraChanged);
        tv_priceFour = mDialogCompanyInfo.findViewById(R.id.tv_priceFour);
        tv_priceThree = mDialogCompanyInfo.findViewById(R.id.tv_priceThree);
        tv_priceTwo = mDialogCompanyInfo.findViewById(R.id.tv_priceTwo);
        tv_priceOne = mDialogCompanyInfo.findViewById(R.id.tv_priceOne);
        ImageView im_CloseIcon = mDialogCompanyInfo.findViewById(R.id.im_CloseIcon);
        // set title
        tv_types.setText("STATUS");
        tv_types_rate.setText("AMOUNT");
        tv_text_close_price.setText("Open Price");
        tv_text_open_price.setText("Close Price");
        tv_dateOne.setText("Daily High");
        tv_dateTwo.setText("Daily Low");
        tv_dateThree.setText("Volume");
        tv_datefour.setText("Previous Close");
        // set data
        tv_coin_name.setText(indiModel.getSymbol_to_display());
        tv_coin_info_text.setText(indiModel.getCompany_name());
        tv_closedPrice.setText(indiModel.getOpen_price());
        tv_intraChanged.setText("" + indiModel.getClose_price());
        /* if (daily_changes.contains("-")){
            tv_priceThree.setTextColor(getResources().getColor(R.color.red_light));
        }
        if(changesPercentage.contains("-")){
            tv_priceFour.setTextColor(getResources().getColor(R.color.red_light));
        } */
        tv_priceOne.setText(indiModel.getDay_high_price());
        tv_priceTwo.setText(indiModel.getDay_low_price());
        tv_priceThree.setText(indiModel.getVolume());
        tv_priceFour.setText(indiModel.getPrivious_close_price());

        // click event
        im_CloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogCompanyInfo.dismiss();
            }
        });
        mDialogCompanyInfo.show();
    }
}
