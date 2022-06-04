package com.lgt.fxtradingleague.TradingPackage;

import android.annotation.SuppressLint;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.TradingAdapter.JoinAdapter;
import com.lgt.fxtradingleague.TradingModel.Datum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.lgt.fxtradingleague.Extra.ExtraData.CONTEST_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.INDI_CONTEST_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.WORLD_LEAGUE_CONTEST_LIST_API;

public class JoinPackageActivity extends AppCompatActivity {
    RecyclerView Rv_JoinedContestList;
    JoinAdapter mJoinAdapter;
    ArrayList<String> mListItems;
    ImageView ivBackToolbar;
    TextView tvToolbarTitle,tv_notification_no_data_found;
    private ProgressBar pb_loaderInner;
    List<Datum> mJoinList = new ArrayList<>();
    String currency_type="",contest_type="";
    List<Datum.WinningInformation> mWinningInformation = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_package_activity);
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        Rv_JoinedContestList=findViewById(R.id.Rv_JoinedContestList);
        tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        ivBackToolbar=findViewById(R.id.ivBackToolbar);
        tv_notification_no_data_found=findViewById(R.id.tv_notification_no_data_found);
        pb_loaderInner=findViewById(R.id.pb_loaderInner);
        if (getIntent().hasExtra("contest_type")){
            contest_type = getIntent().getStringExtra("contest_type");
            currency_type = getIntent().getStringExtra("currency_type");
            tvToolbarTitle.setText(contest_type +" Contest");
            Log.d("event_type",""+contest_type);
        }
        ivBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (currency_type.equalsIgnoreCase("CRYPTO")){
            getJoinDataCrypto();
        }else if(currency_type.equalsIgnoreCase("WorldLeague")){
            getJoinDataWorldLeague();
        }else if(currency_type.equalsIgnoreCase("NASDAQ")){
            getJoinDataIndiLeague();
        }
    }

    private void getJoinDataCrypto() {
        pb_loaderInner.setVisibility(View.VISIBLE);
        Log.d("getJoinDataCrypto",CONTEST_LIST_API);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CONTEST_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getJoinData",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            Log.d("getJoinData","Outer Array : "+jsonArray.length());
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String tbl_contest_id = jsonObject1.getString("tbl_contest_id");
                            String contest_name = jsonObject1.getString("contest_name");
                            String contest_tag = jsonObject1.getString("contest_tag");
                            String winner = jsonObject1.getString("winner");
                            String prize_pool = jsonObject1.getString("prize_pool");
                            String total_team = jsonObject1.getString("total_team");
                            String join_team = jsonObject1.getString("join_team");
                            String entry_fee = jsonObject1.getString("entry_fee");
                            String match_id = jsonObject1.getString("match_id");
                            String contest_time = jsonObject1.getString("contest_time");
                            String contest_date = jsonObject1.getString("contest_date");
                            Datum datum = new Datum();
                            datum.setContestDate(contest_date);
                            datum.setContestName(contest_name);
                            datum.setContestTag(contest_tag);
                            datum.setContestTime(contest_time);
                            datum.setTotalTeam(total_team);
                            datum.setEntryFee(entry_fee);
                            datum.setJoinTeam(join_team);
                            datum.setMatchId(match_id);
                            datum.setWinner(winner);
                            datum.setPrizePool(prize_pool);
                            datum.setTblContestId(tbl_contest_id);
                            JSONArray winner_info = jsonObject1.getJSONArray("winning_information");
                            for (int f=0;f<winner_info.length();f++){
                                Log.d("getJoinData","Inner Array : "+winner_info.length());
                                JSONObject jsonObject2 = winner_info.getJSONObject(f);
                                String tbl_winning_info_id = jsonObject2.getString("tbl_winning_info_id");
                                String from_rank = jsonObject2.getString("from_rank");
                                String to_rank = jsonObject2.getString("to_rank");
                                String price = jsonObject2.getString("price");
                                Datum.WinningInformation winningInformation = new Datum.WinningInformation();
                                winningInformation.setFromRank(from_rank);
                                winningInformation.setPrice(price);
                                winningInformation.setToRank(to_rank);
                                winningInformation.setTblWinningInfoId(tbl_winning_info_id);
                                mWinningInformation.add(winningInformation);
                            }
                            datum.setWinningInformation(mWinningInformation);
                            mJoinList.add(datum);
                            pb_loaderInner.setVisibility(View.GONE);
                        }
                        setupJoinContest();
                    }else{
                        Toast.makeText(JoinPackageActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loaderInner.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getJoinData",""+error);
                pb_loaderInner.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getJoinDataWorldLeague() {
        pb_loaderInner.setVisibility(View.VISIBLE);
        Log.d("getJoinDataWorldLeague",WORLD_LEAGUE_CONTEST_LIST_API);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WORLD_LEAGUE_CONTEST_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getDataWorld",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            Log.d("getJoinData","Outer Array : "+jsonArray.length());
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String tbl_contest_id = jsonObject1.getString("tbl_contest_id");
                            String contest_name = jsonObject1.getString("contest_name");
                            String contest_tag = jsonObject1.getString("contest_tag");
                            String winner = jsonObject1.getString("winner");
                            String prize_pool = jsonObject1.getString("prize_pool");
                            String total_team = jsonObject1.getString("total_team");
                            String join_team = jsonObject1.getString("join_team");
                            String entry_fee = jsonObject1.getString("entry_fee");
                            String match_id = jsonObject1.getString("match_id");
                            String contest_time = jsonObject1.getString("contest_time");
                            String contest_date = jsonObject1.getString("contest_date");
                            Datum datum = new Datum();
                            datum.setContestDate(contest_date);
                            datum.setContestName(contest_name);
                            datum.setContestTag(contest_tag);
                            datum.setContestTime(contest_time);
                            datum.setTotalTeam(total_team);
                            datum.setEntryFee(entry_fee);
                            datum.setJoinTeam(join_team);
                            datum.setMatchId(match_id);
                            datum.setWinner(winner);
                            datum.setPrizePool(prize_pool);
                            datum.setTblContestId(tbl_contest_id);
                            JSONArray winner_info = jsonObject1.getJSONArray("winning_information");
                            for (int f=0;f<winner_info.length();f++){
                                Log.d("getJoinData","Inner Array : "+winner_info.length());
                                JSONObject jsonObject2 = winner_info.getJSONObject(f);
                                String tbl_winning_info_id = jsonObject2.getString("tbl_winning_info_id");
                                String from_rank = jsonObject2.getString("from_rank");
                                String to_rank = jsonObject2.getString("to_rank");
                                String price = jsonObject2.getString("price");
                                Datum.WinningInformation winningInformation = new Datum.WinningInformation();
                                winningInformation.setFromRank(from_rank);
                                winningInformation.setPrice(price);
                                winningInformation.setToRank(to_rank);
                                winningInformation.setTblWinningInfoId(tbl_winning_info_id);
                                mWinningInformation.add(winningInformation);
                            }
                            datum.setWinningInformation(mWinningInformation);
                            mJoinList.add(datum);
                            pb_loaderInner.setVisibility(View.GONE);
                        }
                        setupJoinContest();
                    }else{
                        Toast.makeText(JoinPackageActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loaderInner.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getJoinData",""+error);
                pb_loaderInner.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getJoinDataIndiLeague() {
        pb_loaderInner.setVisibility(View.VISIBLE);
        Log.d("getJoinDataIndiLeague",""+INDI_CONTEST_LIST_API);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, INDI_CONTEST_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getDataWorld",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            Log.d("getJoinData","Outer Array : "+jsonArray.length());
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String tbl_contest_id = jsonObject1.getString("tbl_contest_id");
                            String contest_name = jsonObject1.getString("contest_name");
                            String contest_tag = jsonObject1.getString("contest_tag");
                            String winner = jsonObject1.getString("winner");
                            String prize_pool = jsonObject1.getString("prize_pool");
                            String total_team = jsonObject1.getString("total_team");
                            String join_team = jsonObject1.getString("join_team");
                            String entry_fee = jsonObject1.getString("entry_fee");
                            String match_id = jsonObject1.getString("match_id");
                            String contest_time = jsonObject1.getString("contest_time");
                            String contest_date = jsonObject1.getString("contest_date");
                            Datum datum = new Datum();
                            datum.setContestDate(contest_date);
                            datum.setContestName(contest_name);
                            datum.setContestTag(contest_tag);
                            datum.setContestTime(contest_time);
                            datum.setTotalTeam(total_team);
                            datum.setEntryFee(entry_fee);
                            datum.setJoinTeam(join_team);
                            datum.setMatchId(match_id);
                            datum.setWinner(winner);
                            datum.setPrizePool(prize_pool);
                            datum.setTblContestId(tbl_contest_id);
                            JSONArray winner_info = jsonObject1.getJSONArray("winning_information");
                            for (int f=0;f<winner_info.length();f++){
                                Log.d("getJoinData","Inner Array : "+winner_info.length());
                                JSONObject jsonObject2 = winner_info.getJSONObject(f);
                                String tbl_winning_info_id = jsonObject2.getString("tbl_winning_info_id");
                                String from_rank = jsonObject2.getString("from_rank");
                                String to_rank = jsonObject2.getString("to_rank");
                                String price = jsonObject2.getString("price");
                                Datum.WinningInformation winningInformation = new Datum.WinningInformation();
                                winningInformation.setFromRank(from_rank);
                                winningInformation.setPrice(price);
                                winningInformation.setToRank(to_rank);
                                winningInformation.setTblWinningInfoId(tbl_winning_info_id);
                                mWinningInformation.add(winningInformation);
                            }
                            datum.setWinningInformation(mWinningInformation);
                            mJoinList.add(datum);
                            pb_loaderInner.setVisibility(View.GONE);
                        }
                        setupJoinContest();
                    }else{
                        Toast.makeText(JoinPackageActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loaderInner.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getJoinData",""+error);
                pb_loaderInner.setVisibility(View.GONE);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setupJoinContest() {
        pb_loaderInner.setVisibility(View.GONE);
        mListItems=new ArrayList<String>();
        mJoinAdapter = new JoinAdapter(this,mJoinList,contest_type,currency_type);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        Rv_JoinedContestList.setHasFixedSize(true);
        Rv_JoinedContestList.setLayoutManager(linearLayoutManager);
        Rv_JoinedContestList.setAdapter(mJoinAdapter);
    }

}
