package com.lgt.fxtradingleague.TradingPackage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lgt.fxtradingleague.ChooseCandVCActivity;
import com.lgt.fxtradingleague.ClickInterFace.ClickInterFace;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.TradingAdapter.CoinDetailsAdapter;
import com.lgt.fxtradingleague.TradingAdapter.ProgressAdapter;
import com.lgt.fxtradingleague.TradingAdapter.TradeListAdapter;
import com.lgt.fxtradingleague.TradingModel.TradingListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.lgt.fxtradingleague.Extra.ExtraData.CREATE_TEAM_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.CURRENCY_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_TEAM_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_USER_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEE;

public class SelectTradingMaster extends AppCompatActivity implements ClickInterFace {

    Date date = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    String formattedDate = df.format(date);
    View v_1, v_2, v_3, v_4, v_5, v_6, v_7, v_8, v_9, v_10, v_11;
    LinearLayout ll_barContainer;
    ImageView ivBackToolbar;
    EditText et_searchCoinName;
    TextView tv_targetMoneyWeHave, tv_previewData, tvToolbarTitle, tv_totalSelectedStock, tv_disclaimer, tvToolbarPriceUpdate, addStockTeamBtn;
    ProgressBar pb_priceProgress, pb_teamSelectProgress;
    RecyclerView rv_company_trader_list, rv_progressDotedBar;
    TextView tv_coin_name, tv_coin_info_text, tv_closedPrice, tv_intraChanged, tv_priceFour, tv_priceThree, tv_priceTwo, tv_priceOne;
    ArrayList<TradingListModel> mTradeListData = new ArrayList<>();
    TradeListAdapter mTradeListAdapter;
    long MasterPriceDummy = 100000;
    int countAdapter = 0;
    Set<Integer> MyCounter = new HashSet<>();
    ProgressAdapter progressAdapter;
    String KEY_JOIN_MATCH = "", KEY_JOIN_ID = "", EntryFee = "", mContestType = "";
    Set<String> mCurrencyDetails = new HashSet<>();
    Set<String> mActualPrice = new HashSet<>();
    Set<String> mPurchasedToken = new HashSet<>();
    boolean touch = false;
    BottomSheetDialog dialogPlayerInfo;
    CoinDetailsAdapter coinDetailsAdapter;
    ArrayList<String> mDetailsList = new ArrayList<>();
    Float MasterPrice = 0.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_trading_activity);
        // progress
        v_1 = findViewById(R.id.v_1);
        v_2 = findViewById(R.id.v_2);
        v_3 = findViewById(R.id.v_3);
        v_4 = findViewById(R.id.v_4);
        v_5 = findViewById(R.id.v_5);
        v_6 = findViewById(R.id.v_6);
        v_7 = findViewById(R.id.v_7);
        v_8 = findViewById(R.id.v_8);
        v_9 = findViewById(R.id.v_9);
        v_10 = findViewById(R.id.v_10);
        v_11 = findViewById(R.id.v_11);
        // progress end
        pb_teamSelectProgress = findViewById(R.id.pb_teamSelectProgress);
        pb_teamSelectProgress.setMax(11);
        tv_targetMoneyWeHave = findViewById(R.id.tv_targetMoneyWeHave);
        ll_barContainer = findViewById(R.id.ll_barContainer);
        tv_previewData = findViewById(R.id.tv_previewData);
        et_searchCoinName = findViewById(R.id.et_searchCoinName);
        tv_totalSelectedStock = findViewById(R.id.tv_totalSelectedStock);
        addStockTeamBtn = findViewById(R.id.addStockTeamBtn);
        ivBackToolbar = findViewById(R.id.ivBackToolbar);
        tv_disclaimer = findViewById(R.id.tv_disclaimer);
        tvToolbarPriceUpdate = findViewById(R.id.tvToolbarPriceUpdate);
        pb_priceProgress = findViewById(R.id.pb_priceProgress);
        rv_progressDotedBar = findViewById(R.id.rv_progressDotedBar);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        rv_company_trader_list = findViewById(R.id.rv_company_trader_list);
        tvToolbarTitle.setText("Select Players");
        tvToolbarPriceUpdate.setVisibility(View.VISIBLE);
        tv_disclaimer.setVisibility(View.VISIBLE);
        tv_disclaimer.setText("disclaimer");
        tvToolbarPriceUpdate.setText("Date " + formattedDate);
        if (getIntent().hasExtra("KEY_JOIN_MATCH")) {
            KEY_JOIN_MATCH = getIntent().getStringExtra("KEY_JOIN_MATCH");
            KEY_JOIN_ID = getIntent().getStringExtra("KEY_JOIN_ID");
            EntryFee = getIntent().getStringExtra("EntryFee");
            mContestType = getIntent().getStringExtra("ContestType");
            Log.d("event_type", "" + mContestType);
        }
        setProgressAdapter();
        ivBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_previewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectTradingMaster.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
        addStockTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrencyDetails.size() == 11) {
                    pb_priceProgress.setVisibility(View.VISIBLE);
                    sendJoinContestApi();
                } else {
                    Toast.makeText(SelectTradingMaster.this, "Please Select 11 Items.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_searchCoinName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    startSearch(charSequence.toString());
                } else {
                    setDataToList();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        displayDataFromAPI();
    }

    private void startSearch(String key) {
        ArrayList<TradingListModel> tempLits = new ArrayList<>();
        for (TradingListModel temp : mTradeListData) {
            if (temp.getName().toLowerCase().contains(key.toLowerCase())) {
                tempLits.add(temp);
            }
        }
        mTradeListAdapter = new TradeListAdapter(tempLits, this, MasterPriceDummy, this);
        rv_company_trader_list.setAdapter(mTradeListAdapter);
        mTradeListAdapter.notifyDataSetChanged();
    }

    private void sendJoinContestApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_TEAM_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("joinContest", "Response : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    pb_priceProgress.setVisibility(View.GONE);
                    if (status.equalsIgnoreCase("1")) {
                        String team_id = jsonObject.getString("team_id");
                        String user_id = jsonObject.getString("user_id");
                        String contest_id = jsonObject.getString("contest_id");
                        Intent update_sell_buy = new Intent(SelectTradingMaster.this, ChooseCandVCActivity.class);
                        update_sell_buy.putExtra(KEYS_TEAM_ID, team_id);
                        update_sell_buy.putExtra(KEYS_CONTEST_ID, contest_id);
                        update_sell_buy.putExtra(KEY_ENTRY_FEE, EntryFee);
                        update_sell_buy.putExtra(KEYS_CONTEST_TYPE, mContestType);
                        update_sell_buy.putExtra(KEYS_USER_ID, HomeActivity.sessionManager.getUser(getApplicationContext()).getUser_id());
                        startActivity(update_sell_buy);
                        finish();
                    } else {
                        Toast.makeText(SelectTradingMaster.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_priceProgress.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("joinContest", "Error : " + error);
                pb_priceProgress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("currency", getStringDataToList(mCurrencyDetails.toString()));
                param.put("share_qnt", getStringDataToList(mPurchasedToken.toString()));
                param.put("currency_rate", getStringDataToList(mActualPrice.toString()));
                param.put("user_id", HomeActivity.sessionManager.getUser(getApplicationContext()).getUser_id());
                param.put("contest_id", KEY_JOIN_ID);
                param.put("type", mContestType);
                Log.d("joinContest", "Data : " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String getStringDataToList(String id) {
        return id.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
    }

    private void displayDataFromAPI() {
        pb_priceProgress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CURRENCY_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("API_DATA", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray innerData = jsonObject.getJSONArray("data");
                        for (int i = 0; i < innerData.length(); i++) {
                            JSONObject insideData = innerData.getJSONObject(i);
                            String tbl_crypto_currency_list_id = insideData.getString("tbl_crypto_currency_list_id");
                            String currency_image = insideData.getString("currency_image");
                            String currency_id = insideData.getString("currency_id");
                            String symbol = insideData.getString("symbol");
                            String name = insideData.getString("name");
                            String currency_rate = insideData.getString("currency_rate");
                            String closing_rate = insideData.getString("closing_rate");
                            String currency_value = insideData.getString("currency_value");
                            String price_change_percentage_24h = insideData.getString("price_change_percentage_24h");
                            String price_change_percentage_7d = insideData.getString("price_change_percentage_7d");
                            String price_change_percentage_14d = insideData.getString("price_change_percentage_14d");
                            String price_change_percentage_30d = insideData.getString("price_change_percentage_30d");
                            TradingListModel tradingListModel = new TradingListModel(tbl_crypto_currency_list_id, currency_id, symbol, name, currency_image, currency_rate, false);
                            tradingListModel.setClosing_rate(closing_rate);
                            tradingListModel.setCurrency_value(currency_value);
                            tradingListModel.setPrice_change_percentage_7d(price_change_percentage_7d);
                            tradingListModel.setPrice_change_percentage_14d(price_change_percentage_14d);
                            tradingListModel.setPrice_change_percentage_24h(price_change_percentage_24h);
                            tradingListModel.setPrice_change_percentage_30d(price_change_percentage_30d);
                            mTradeListData.add(tradingListModel);
                        }
                        setDataToList();
                    } else {
                        Toast.makeText(SelectTradingMaster.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pb_priceProgress.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_priceProgress.setVisibility(View.GONE);
                Log.d("API_DATA", "" + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setDataToList() {
        pb_priceProgress.setVisibility(View.GONE);
        mTradeListAdapter = new TradeListAdapter(mTradeListData, this, MasterPriceDummy, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_company_trader_list.setLayoutManager(linearLayoutManager);
        rv_company_trader_list.setHasFixedSize(true);
        rv_company_trader_list.setAdapter(mTradeListAdapter);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void itemCountList(int itemsSelect, String check, TradeListAdapter.TradeHolder holder) {
        if (mCurrencyDetails != null) {

            tv_totalSelectedStock.setText(itemsSelect + "/");
            pb_teamSelectProgress.setProgress(mCurrencyDetails.size());

        } else {
            Log.d("two:", "    >>>     " + mCurrencyDetails.size());
        }

        if (itemsSelect >= 11) {
            touch = true;
            // holder.cb_selectCompanyBox.setChecked(false);
            // Toast.makeText(this, "You can select Only 11 Stock.", Toast.LENGTH_SHORT).show();
        } else {
            touch = false;
        }
        Log.d("list_size:", touch + "    >>>     " + itemsSelect);

        switch (itemsSelect) {
            case 0:
                tv_targetMoneyWeHave.setText("$ 11,00,000.00");
                break;
            case 1:
                tv_targetMoneyWeHave.setText("$ 10,00,000.00");
                break;
            case 2:
                tv_targetMoneyWeHave.setText("$ 9,00,000.00");
                break;
            case 3:
                tv_targetMoneyWeHave.setText("$ 8,00,000.00");
                break;
            case 4:
                tv_targetMoneyWeHave.setText("$ 7,00,000.00");
                break;
            case 5:
                tv_targetMoneyWeHave.setText("$ 6,00,000.00");
                break;
            case 6:
                tv_targetMoneyWeHave.setText("$ 5,00,000.00");
                break;
            case 7:
                tv_targetMoneyWeHave.setText("$ 4,00,000.00");
                break;
            case 8:
                tv_targetMoneyWeHave.setText("$ 3,00,000.00");
                break;
            case 9:
                tv_targetMoneyWeHave.setText("$ 2,00,000.00");
                break;
            case 10:
                tv_targetMoneyWeHave.setText("$ 1,00,000.00");
                break;
            case 11:
                tv_targetMoneyWeHave.setText("$ 00.00");
                break;
        }
    }

    @Override
    public void savedSelectTradingDetails(String CurrencyName, String ActualPrice, String PurchasedTokenPrice) {
        MasterPrice = 100000 / Float.valueOf(ActualPrice);
        Log.d("CurrencyList", "  Name : " + CurrencyName + " Rate : " + ActualPrice + " Purchased Token : " + MasterPrice);
        if (!CurrencyName.equalsIgnoreCase("")) {
            if (!mCurrencyDetails.contains(CurrencyName)) {
                mPurchasedToken.add(String.valueOf(MasterPrice));
                mCurrencyDetails.add(CurrencyName);
                mActualPrice.add(ActualPrice);
                Log.d("ActualPrice", "Add : " + mPurchasedToken.toString());
            } else {
                mPurchasedToken.remove(String.valueOf(MasterPrice));
                mCurrencyDetails.remove(CurrencyName);
                mActualPrice.remove(ActualPrice);
                Log.d("ActualPrice", "Remove : " + mPurchasedToken.toString());
            }
            Log.d("CurrencyList", mCurrencyDetails.toString() + " Total Count : " + mPurchasedToken.toString());
        }
    }
    // tv_coin_name,tv_coin_info_text,tv_closedPrice,tv_intraChanged,tv_priceFour,tv_priceThree,tv_priceTwo,tv_priceOne

    @SuppressLint("SetTextI18n")
    @Override
    public void displayCoinDetails(final String name,final  String symbol, final String CurrentRate, final String closingRate, final String OneDay, final String sevenDays, final String FourteenDays, final String month) {
        Log.d("displayCoinDetails", "" + name + "," + symbol + "," + CurrentRate + "," + closingRate + "," + OneDay + "," + sevenDays + "," + FourteenDays + "," + month);
        double final_intra_day = 0;
        DecimalFormat decimalFormat = new DecimalFormat("###.####");
        if (CurrentRate == null && TextUtils.isEmpty(CurrentRate)) {
            final_intra_day = 0.00;
        } else {
            if (closingRate == null && TextUtils.isEmpty(closingRate)) {
            } else {
                final_intra_day = Double.parseDouble(CurrentRate) - Double.parseDouble(closingRate);
            }
        }
        
        dialogPlayerInfo = new BottomSheetDialog(this, R.style.SheetDialog);
        dialogPlayerInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPlayerInfo.setContentView(R.layout.coin_info_dailoug_layout);
        // find ids
        tv_coin_name = dialogPlayerInfo.findViewById(R.id.tv_coin_name);
        tv_coin_info_text = dialogPlayerInfo.findViewById(R.id.tv_coin_info_text);
        tv_closedPrice = dialogPlayerInfo.findViewById(R.id.tv_closedPrice);
        tv_intraChanged = dialogPlayerInfo.findViewById(R.id.tv_intraChanged);
        tv_priceFour = dialogPlayerInfo.findViewById(R.id.tv_priceFour);
        tv_priceThree = dialogPlayerInfo.findViewById(R.id.tv_priceThree);
        tv_priceTwo = dialogPlayerInfo.findViewById(R.id.tv_priceTwo);
        tv_priceOne = dialogPlayerInfo.findViewById(R.id.tv_priceOne);
        ImageView im_CloseIcon = dialogPlayerInfo.findViewById(R.id.im_CloseIcon);
        // set data
        tv_coin_name.setText(name);
        tv_coin_info_text.setText(symbol);
        tv_closedPrice.setText(closingRate);
        tv_intraChanged.setText(OneDay+"%");

        if (OneDay.contains("-")) {
            tv_intraChanged.setTextColor(getResources().getColor(R.color.red_light));
        } else if (sevenDays.contains("-")) {
            tv_priceTwo.setTextColor(getResources().getColor(R.color.red_light));
        } else if (FourteenDays.contains("-")) {
            tv_priceThree.setTextColor(getResources().getColor(R.color.red_light));
        } else if (month.contains("-")) {
            tv_priceFour.setTextColor(getResources().getColor(R.color.red_light));
        }

        tv_priceTwo.setText(sevenDays);
        tv_priceThree.setText(FourteenDays);
        tv_priceFour.setText(month);
        // tv_intraChanged.setText("" + decimalFormat.format(final_intra_day));
        // click event
        im_CloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlayerInfo.dismiss();
            }
        });
        dialogPlayerInfo.show();
    }

    private void notifyProgressAdapter(int itemsSelect) {
        countAdapter = itemsSelect;
        setProgressAdapter();
    }

    private void setProgressAdapter() {
        progressAdapter = new ProgressAdapter(this, countAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rv_progressDotedBar.setLayoutManager(linearLayoutManager);
        rv_progressDotedBar.setHasFixedSize(true);
        rv_progressDotedBar.setNestedScrollingEnabled(false);
        rv_progressDotedBar.setAdapter(progressAdapter);
    }
}
