package com.lgt.fxtradingleague.WorldLeague;

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
import com.lgt.fxtradingleague.Extra.WorldCompanyTrade;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_TEAM_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_USER_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEES;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_JOIN_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_JOIN_MATCH;
import static com.lgt.fxtradingleague.Extra.ExtraData.WORLD_COMPANY_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.WORLD_TEAM_LEAGUE_API;

public class ActivityWorldLeague extends AppCompatActivity implements WorldCompanyTrade {
    EditText et_searchWorldCompanyName;
    ImageView ivBackToolbar;
    TextView addWorldLeagueTeam, tvToolbarTitle, tvToolbarPriceUpdate, tv_world_Stock, tv_worldTargetMoney, tv_no_event;
    RecyclerView rv_world_league_list;
    ArrayList<WorldLeagueModel> mDataList;
    AdapterWorldLeague adapterWorldLeague;
    ProgressBar pb_priceWorldProgress, pb_WorldSelectProgress;
    // get date
    Date date = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    // local variable
    String ContextType = "", JoinMatch = "", JoinKeyId = "", EntryFees = "", UserLoginID = "";
    // set data to list
    Set<String> mCompanySymbol = new HashSet<>();
    Set<String> mSharedPrice = new HashSet<>();
    // show alert
    BottomSheetDialog mDialogCompanyInfo;
    TextView tv_coin_name,tv_coin_info_text,tv_closedPrice,tv_intraChanged,tv_priceFour,tv_priceThree,tv_priceTwo,tv_priceOne;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_world_league_activity);
        if (getIntent().hasExtra(KEYS_CONTEST_TYPE)) {
            ContextType = getIntent().getStringExtra(KEYS_CONTEST_TYPE);
            JoinMatch = getIntent().getStringExtra(KEY_JOIN_MATCH);
            JoinKeyId = getIntent().getStringExtra(KEY_JOIN_ID);
            EntryFees = getIntent().getStringExtra(KEY_ENTRY_FEES);
            UserLoginID = HomeActivity.sessionManager.getUser(getApplicationContext()).getUser_id();
        }
        setUpView();
    }

    private void setUpView() {
        String formattedDate = df.format(date);
        mDataList = new ArrayList<>();
        ivBackToolbar = findViewById(R.id.ivBackToolbar);
        tv_world_Stock = findViewById(R.id.tv_world_Stock);
        tv_no_event = findViewById(R.id.tv_no_event);
        tv_worldTargetMoney = findViewById(R.id.tv_worldTargetMoney);
        pb_WorldSelectProgress = findViewById(R.id.pb_WorldSelectProgress);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        addWorldLeagueTeam = findViewById(R.id.addWorldLeagueTeam);
        rv_world_league_list = findViewById(R.id.rv_world_league_list);
        tvToolbarPriceUpdate = findViewById(R.id.tvToolbarPriceUpdate);
        pb_priceWorldProgress = findViewById(R.id.pb_priceWorldProgress);
        et_searchWorldCompanyName = findViewById(R.id.et_searchWorldCompanyName);
        pb_priceWorldProgress.setVisibility(View.VISIBLE);
        tvToolbarPriceUpdate.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(ContextType + " World League");
        tvToolbarPriceUpdate.setText("" + formattedDate);
        setClickEvent();
        if (ContextType.equalsIgnoreCase("Daily")) {
            getDataFromWorldLeague();
        } else if (ContextType.equalsIgnoreCase("Weekly")) {
            //            pb_priceWorldProgress.setVisibility(View.GONE);
//            rv_world_league_list.setVisibility(View.GONE);
//            tv_no_event.setVisibility(View.VISIBLE);
            getDataFromWorldLeague();
        } else if (ContextType.equalsIgnoreCase("Monthly")) {
            getDataFromWorldLeague();
        }
    }

    private void setClickEvent() {
        ivBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        et_searchWorldCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    startSearch(charSequence.toString());
                } else {
                    setAdapterToDataWorldModel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addWorldLeagueTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCompanySymbol.size() == 11) {
                    pb_priceWorldProgress.setVisibility(View.VISIBLE);
                    setJoinDataToWorldLeague();
                } else {
                    Toast.makeText(ActivityWorldLeague.this, "Please Select 11 Items.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setJoinDataToWorldLeague() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WORLD_TEAM_LEAGUE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("JoinDataToWorld: " + response);
                pb_priceWorldProgress.setVisibility(View.GONE);
                try {
                    Toast.makeText(ActivityWorldLeague.this, "Save Team Successful!", Toast.LENGTH_SHORT).show();
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
                        Intent goToNext = new Intent(ActivityWorldLeague.this, ActivityBuyOrSaleWorldLeague.class);
                        goToNext.putExtra(KEYS_USER_ID, user_id);
                        goToNext.putExtra(KEYS_TEAM_ID, team_id);
                        goToNext.putExtra(KEYS_CONTEST_TYPE, type);
                        goToNext.putExtra(KEYS_CONTEST_ID, contest_id);
                        goToNext.putExtra(KEY_ENTRY_FEE, contest_price);
                        startActivity(goToNext);
                    } else {
                        Toast.makeText(ActivityWorldLeague.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_priceWorldProgress.setVisibility(View.GONE);
                Validations.common_log("JoinDataToWorld: error:  " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("company_symbol", getStringDataToList(mCompanySymbol.toString())); // loop
                param.put("share_rate", getStringDataToList(mSharedPrice.toString())); // loop
                param.put("contest_id", JoinKeyId); // event id
                param.put("contest_price", EntryFees); // event price
                param.put("user_id", UserLoginID); // login with user id
                param.put("type", ContextType); // event type
                Validations.common_log("JoinDataToWorld: param : " + param.toString());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ActivityWorldLeague.this);
        requestQueue.add(stringRequest);
    }

    private String getStringDataToList(String id) {
        return id.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
    }

    private void startSearch(String key) {
        ArrayList<WorldLeagueModel> tempLits = new ArrayList<>();
        for (WorldLeagueModel temp : mDataList) {
            if (temp.getCompany_name().toLowerCase().contains(key.toLowerCase())) {
                tempLits.add(temp);
            }
        }
        adapterWorldLeague = new AdapterWorldLeague(tempLits, ActivityWorldLeague.this, ActivityWorldLeague.this);
        rv_world_league_list.setAdapter(adapterWorldLeague);
        adapterWorldLeague.notifyDataSetChanged();
    }

    private void getDataFromWorldLeague() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WORLD_COMPANY_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log("WorldLeague: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                String tbl_world_league_id = data.getString("tbl_world_league_id");
                                String symbol = data.getString("symbol");
                                String images = data.getString("images");
                                String company_name = data.getString("company_name");
                                String open_price = data.getString("open_price");
                                String changesPercentage = data.getString("changesPercentage");
                                String daily_changes = data.getString("daily_changes");
                                String daily_low = data.getString("dayLow");
                                String currency_value = data.getString("currency_value");
                                String daily_high = data.getString("dayHigh");
                                String exchange_price = data.getString("exchange");
                                String previous_open = data.getString("previous_open_price");
                                String previous_close = data.getString("previousClose");
                                // set data to model
                                WorldLeagueModel worldLeagueModel = new WorldLeagueModel();
                                worldLeagueModel.setTbl_world_league_id(tbl_world_league_id);
                                worldLeagueModel.setSymbol(symbol);
                                worldLeagueModel.setImages(images);
                                worldLeagueModel.setCompany_name(company_name);
                                worldLeagueModel.setOpen_price(open_price);
                                worldLeagueModel.setChangesPercentage(changesPercentage);
                                worldLeagueModel.setDaily_changes(daily_changes);
                                worldLeagueModel.setDayLow(daily_low);
                                worldLeagueModel.setCurrency_value(currency_value);
                                worldLeagueModel.setDayHigh(daily_high);
                                worldLeagueModel.setExchange(exchange_price);
                                worldLeagueModel.setPrevious_open_price(previous_open);
                                worldLeagueModel.setPreviousClose(previous_close);
                                // set data to list
                                mDataList.add(worldLeagueModel);
                            }
                        }
                        setAdapterToDataWorldModel();
                    } else {
                        pb_priceWorldProgress.setVisibility(View.GONE);
                        Toast.makeText(ActivityWorldLeague.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pb_priceWorldProgress.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_priceWorldProgress.setVisibility(View.GONE);
                Validations.common_log("WorldLeague_error: " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setAdapterToDataWorldModel() {
        pb_priceWorldProgress.setVisibility(View.GONE);
        adapterWorldLeague = new AdapterWorldLeague(mDataList, ActivityWorldLeague.this, ActivityWorldLeague.this);
        rv_world_league_list.setHasFixedSize(true);
        rv_world_league_list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rv_world_league_list.setAdapter(adapterWorldLeague);
    }

    @Override
    public void selectedItemsCount(int selectedCount) {
        // set item selected
        tv_world_Stock.setText(selectedCount + "/");
        // set progress
        pb_WorldSelectProgress.setProgress(selectedCount);
        // set pending price
        switch (selectedCount) {
            case 0:
                tv_worldTargetMoney.setText("$ 11,00,000.00");
                break;
            case 1:
                tv_worldTargetMoney.setText("$ 10,00,000.00");
                break;
            case 2:
                tv_worldTargetMoney.setText("$ 9,00,000.00");
                break;
            case 3:
                tv_worldTargetMoney.setText("$ 8,00,000.00");
                break;
            case 4:
                tv_worldTargetMoney.setText("$ 7,00,000.00");
                break;
            case 5:
                tv_worldTargetMoney.setText("$ 6,00,000.00");
                break;
            case 6:
                tv_worldTargetMoney.setText("$ 5,00,000.00");
                break;
            case 7:
                tv_worldTargetMoney.setText("$ 4,00,000.00");
                break;
            case 8:
                tv_worldTargetMoney.setText("$ 3,00,000.00");
                break;
            case 9:
                tv_worldTargetMoney.setText("$ 2,00,000.00");
                break;
            case 10:
                tv_worldTargetMoney.setText("$ 1,00,000.00");
                break;
            case 11:
                tv_worldTargetMoney.setText("$ 00.00");
                break;
        }
    }

    @Override
    public void setDataOfSelectedList(String companySymbol, String shareRate) {
        if (!companySymbol.equalsIgnoreCase("")) {
            if (!mCompanySymbol.contains(companySymbol)) {
                mCompanySymbol.add(companySymbol);
                mSharedPrice.add(shareRate);
                Validations.common_log("Add: " + mSharedPrice.toString() + "  |  " + mCompanySymbol.toString());
            } else {
                mCompanySymbol.remove(companySymbol);
                mSharedPrice.remove(shareRate);
                Validations.common_log("Removed: " + mSharedPrice.toString() + "  |  " + mCompanySymbol.toString());
            }
        }
    }

    TextView tv_text_close_price,tv_text_open_price,tv_dateOne,tv_dateTwo,tv_dateThree,tv_datefour,tv_types_rate,tv_types;
    @Override
    public void showDetailsAboutCompany(String companyName,String Symbol,String openPrice,String dailyHigh,String dailyLow,String exchangePrice,String previousOpen,String previousClose,String changesPercentage,String daily_changes) {
        mDialogCompanyInfo = new BottomSheetDialog(this,R.style.SheetDialog);
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
        tv_dateThree.setText("Change Point");
        tv_datefour.setText("Change Percentage");
        // set data
        tv_coin_name.setText(Symbol);
        tv_coin_info_text.setText(companyName);
        tv_closedPrice.setText(openPrice);
        if (daily_changes.contains("-")){
            tv_priceThree.setTextColor(getResources().getColor(R.color.red_light));
        }
        if(changesPercentage.contains("-")){
            tv_priceFour.setTextColor(getResources().getColor(R.color.red_light));
        }
        tv_priceOne.setText(dailyHigh);
        tv_priceTwo.setText(dailyLow);
        tv_priceThree.setText(changesPercentage+"");
        tv_priceFour.setText(daily_changes+"%");
        tv_intraChanged.setText(""+previousClose);
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
