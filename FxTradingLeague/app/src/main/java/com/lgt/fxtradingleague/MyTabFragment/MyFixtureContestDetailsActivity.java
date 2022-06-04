package com.lgt.fxtradingleague.MyTabFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.Extra.ClickBottomPlayerInfo;
import com.lgt.fxtradingleague.ModelPTL.PlayerModeTeam;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;
import com.lgt.fxtradingleague.TradingAdapter.AdapterSelectedTeam;
import com.lgt.fxtradingleague.TradingAdapter.ContestAdapter;
import com.lgt.fxtradingleague.TradingModel.TeamModel;
import com.lgt.fxtradingleague.TradingPackage.ActivityResultTypeContainer;
import com.lgt.fxtradingleague.WorldLeague.AdapterSelectedWorldTeam;
import com.lgt.fxtradingleague.WorldLeague.DJWLeagueModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.lgt.fxtradingleague.Extra.ExtraData.CONTEST_JOIN_TEAM_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.INDIAN_JOIN_CONTEST_JOIN_TEAM_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.INDIAN_TEAM_VIEW_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.TEAM_JOIN_WORLD_CONTEST_LEAGUE_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.VIEW_TEAM_LIST;
import static com.lgt.fxtradingleague.Extra.ExtraData.VIEW_WORLD_TEAM_DISPLAY_API;

public class MyFixtureContestDetailsActivity extends AppCompatActivity implements ClickBottomPlayerInfo {
    BottomSheetDialog dialogPlayerInfo, dialogGroundView;
    ArrayList<TeamModel> mTeamDataList = new ArrayList<>();
    ArrayList<PlayerModeTeam> mDataList = new ArrayList<>();
    ArrayList<DJWLeagueModel> mDialougList = new ArrayList<>();
    ContestAdapter contestAdapter;
    MyFixtureContestDetailsActivity activity;
    Context context;
    RecyclerView Rv_MyFixLeaderboard, rv_selectedPlayerList;
    AdapterSelectedTeam adapterSelectedTeam;
    AdapterSelectedWorldTeam adapterSelectedWorldTeam;
    ImageView im_back;
    TextView tv_HeaderName, tv_ContestTimer, tv_ContestTeamsName;
    private BottomSheetBehavior mBehavior;
    TextView tv_TotalWinning, tv_EntryFess, tv_JoinedWithTeam, tv_TotalJoinedTeamCount, tv_user_name, tv_user_rank;
    String UserTeamId, contest_id, match_status, register_session_user_id, joing_date;
    private String ContextType = "";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fixture_contest_details);
        context = activity = this;

        sessionManager = new SessionManager();

        if (!getIntent().getStringExtra("user_id").equalsIgnoreCase("")) {
            UserTeamId = getIntent().getStringExtra("user_id");
            contest_id = getIntent().getStringExtra("contest_id");
            match_status = getIntent().getStringExtra("match_status");
            joing_date = getIntent().getStringExtra("joing_date");
            register_session_user_id = sessionManager.getUser(getApplicationContext()).getUser_id();
            ContextType = ActivityResultTypeContainer.Result_Type;
            Validations.common_log("TeamList: " + ContextType);
        }

        initViews();
    }

    public void initViews() {
        Rv_MyFixLeaderboard = findViewById(R.id.Rv_MyFixLeaderboard);
        tv_ContestTimer = findViewById(R.id.tv_ContestTimer);
        tv_ContestTeamsName = findViewById(R.id.tv_ContestTeamsName);

        tv_TotalJoinedTeamCount = findViewById(R.id.tv_TotalJoinedTeamCount);
        tv_JoinedWithTeam = findViewById(R.id.tv_JoinedWithTeam);
        tv_EntryFess = findViewById(R.id.tv_EntryFess);
        tv_TotalWinning = findViewById(R.id.tv_TotalWinning);

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        tv_HeaderName.setText("CONTESTS");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // TEAM_JOIN_WORLD_CONTEST_LEAGUE_API
        if (!ContextType.equalsIgnoreCase("")) {
            if (ContextType.equalsIgnoreCase("World League")) {
                Validations.common_log("World");
                getUserData(TEAM_JOIN_WORLD_CONTEST_LEAGUE_API);
            } else if (ContextType.equalsIgnoreCase("Indie League")) {
                Validations.common_log("Indie League");
                getUserData(INDIAN_JOIN_CONTEST_JOIN_TEAM_LIST_API);
            } else if (ContextType.equalsIgnoreCase("Crypto League")) {
                Validations.common_log("Crypto League");
                getUserData(CONTEST_JOIN_TEAM_LIST_API);
            }
        }
    }

    private void getUserData(final String RequestUrl) {
        Validations.showProgress(this);
        Log.d("getUserData",""+RequestUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RequestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log(ContextType + "-> UserData: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        String entry_fee = jsonObject.getString("entry_fee");
                        String total_winning = jsonObject.getString("total_winning");
                        tv_EntryFess.setText(entry_fee);
                        tv_TotalWinning.setText(total_winning);
                        Validations.hideProgress();
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject innerData = data.getJSONObject(i);
                            String tbl_join_contest_id = innerData.getString("tbl_join_contest_id");
                            String user_id = innerData.getString("user_id");
                            String type = innerData.getString("type");
                            String team_id = innerData.getString("team_id");
                            String contest_id = innerData.getString("contest_id");
                            String overall_profit = innerData.getString("overall_profit");
                            String rank = innerData.getString("rank");
                            String name = innerData.getString("name");
                            TeamModel teamModel = new TeamModel();
                            teamModel.setContest_id(tbl_join_contest_id);
                            teamModel.setOverall_profit(overall_profit);
                            teamModel.setName(name);
                            teamModel.setRank(rank);
                            teamModel.setTbl_join_contest_id(contest_id);
                            teamModel.setTeam_id(team_id);
                            teamModel.setType(type);
                            teamModel.setUser_id(user_id);
                            mTeamDataList.add(teamModel);
                        }
                        setAdapterData();
                    } else {
                        Validations.hideProgress();
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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
                Log.d("UserData", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", UserTeamId);
                param.put("match_status", match_status);
                param.put("contest_id", contest_id);
                param.put("joing_date", joing_date);
                Log.d("UserData", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setAdapterData() {
        contestAdapter = new ContestAdapter(mTeamDataList, this, register_session_user_id, match_status, this);
        Rv_MyFixLeaderboard.setHasFixedSize(true);
        Rv_MyFixLeaderboard.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        Rv_MyFixLeaderboard.setLayoutManager(mLayoutManager);
        Rv_MyFixLeaderboard.setAdapter(contestAdapter);
        Rv_MyFixLeaderboard.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void clickToViewPlayer(String TeamId, String name, String rank) {
        dialogGroundView = new BottomSheetDialog(this, R.style.SheetDialog);
        dialogGroundView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(this, R.layout.dialog_ground_view, null);
        dialogGroundView.setContentView(view);
        rv_selectedPlayerList = dialogGroundView.findViewById(R.id.rv_selectedPlayerList);
        tv_user_name = dialogGroundView.findViewById(R.id.tv_user_name);
        tv_user_rank = dialogGroundView.findViewById(R.id.tv_user_rank);
        tv_user_name.setText(name);
        if (!rank.equalsIgnoreCase("")) {
            tv_user_rank.setText("Overall Profit : " + rank);
        } else {
            tv_user_rank.setText("Overall Profit : 0");
        }
        ImageView im_CloseIcon = dialogGroundView.findViewById(R.id.im_CloseIcon);
        Validations.common_log("ClickEvent: "+ContextType);
        // VIEW_WORLD_TEAM_DISPLAY_API
        if (ContextType.equalsIgnoreCase("World League")) {
            Validations.common_log("World Dialog Open");
            displayWorldPlayerDataInRv(TeamId, VIEW_WORLD_TEAM_DISPLAY_API);
        } else if (ContextType.equalsIgnoreCase("Indie League")) {
            Validations.common_log("Indie League Dialog Open");
            displayWorldPlayerDataInRv(TeamId, INDIAN_TEAM_VIEW_LIST_API);
        } else if (ContextType.equalsIgnoreCase("Crypto League")) {
            Validations.common_log("Crypto League Dialog Open");
            displayPlayerDataInRv(TeamId, VIEW_TEAM_LIST);
        }
        im_CloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGroundView.dismiss();
            }
        });
        dialogGroundView.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog dialogc = (BottomSheetDialog) dialogInterface;
                setupFullHeight(dialogc);
            }
        });
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void displayPlayerDataInRv(final String teamId, final String RequestUrl) {
        mDataList.clear();
        Log.d("displayPlayerDataInRv", "" + RequestUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RequestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("displayPlayerData", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject innerData = jsonArray.getJSONObject(i);
                            String tbl_my_team_player_id = innerData.getString("tbl_my_team_player_id");
                            String currency = innerData.getString("currency");
                            String currency_id = innerData.getString("currency_id");
                            String symbol = innerData.getString("symbol");
                            String currency_image = innerData.getString("currency_image");
                            String currency_rate = innerData.getString("currency_rate");
                            String share_qnt = innerData.getString("share_qnt");
                            String current_rate = innerData.getString("current_rate");
                            String profit_value = innerData.getString("profit_value");
                            String position = innerData.getString("position");
                            PlayerModeTeam playerModeTeam = new PlayerModeTeam();
                            playerModeTeam.setProfit_value(profit_value);
                            playerModeTeam.setCurrency(currency);
                            playerModeTeam.setCurrency_id(currency_id);
                            playerModeTeam.setCurrency_image(currency_image);
                            playerModeTeam.setCurrency_rate(currency_rate);
                            playerModeTeam.setPosition(position);
                            playerModeTeam.setShare_qnt(share_qnt);
                            playerModeTeam.setCurrent_rate(current_rate);
                            playerModeTeam.setSymbol(symbol);
                            playerModeTeam.setTbl_my_team_player_id(tbl_my_team_player_id);
                            mDataList.add(playerModeTeam);
                        }
                        setDataToAdapter();
                    } else {
                        Validations.hideProgress();
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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
                Log.d("displayPlayerData", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("team_id", teamId);
                Log.d("displayPlayerData", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void displayWorldPlayerDataInRv(final String teamId, final String RequestUrl) {
        mDialougList.clear();
        Log.d("displayWorldPlayerDataInRv", "" + RequestUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RequestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("displayWorldPD", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject innerData = jsonArray.getJSONObject(i);
                            String tbl_my_team_player_id = innerData.getString("tbl_my_team_player_id");
                            String currency = innerData.getString("currency");
                            String company_name = innerData.getString("company_name");
                            String company_image = innerData.getString("company_image");
                            String open_price = innerData.getString("open_price");
                            String share_qnt = innerData.getString("share_qnt");
                            String current_rate = innerData.getString("current_rate");
                            String profit_value = innerData.getString("profit_value");
                            String position = innerData.getString("position");
                            DJWLeagueModel playerModeTeam = new DJWLeagueModel();
                            playerModeTeam.setProfit_value(profit_value);
                            playerModeTeam.setCurrency(currency);
                            playerModeTeam.setCompany_name(company_name);
                            playerModeTeam.setCompany_image(company_image);
                            playerModeTeam.setOpen_price(open_price);
                            playerModeTeam.setPosition(position);
                            playerModeTeam.setShare_qnt(share_qnt);
                            playerModeTeam.setCurrent_rate(current_rate);
                            playerModeTeam.setTbl_my_team_player_id(tbl_my_team_player_id);
                            mDialougList.add(playerModeTeam);
                        }
                        setDataToWorldAdapter();
                    } else {
                        Validations.hideProgress();
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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
                Log.d("displayWorldPD", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("team_id", teamId);
                Log.d("displayWorldPD", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setDataToAdapter() {
        Validations.hideProgress();
        rv_selectedPlayerList.setHasFixedSize(true);
        // rv_selectedPlayerList.setLayoutManager(new GridLayoutManager(this, 3));
        rv_selectedPlayerList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapterSelectedTeam = new AdapterSelectedTeam(mDataList, this);
        rv_selectedPlayerList.setAdapter(adapterSelectedTeam);
        rv_selectedPlayerList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                v.onTouchEvent(event);
                return true;
            }
        });
        dialogGroundView.show();
    }

    private void setDataToWorldAdapter() {
        Validations.hideProgress();
        rv_selectedPlayerList.setHasFixedSize(true);
        // rv_selectedPlayerList.setLayoutManager(new GridLayoutManager(this, 3));
        rv_selectedPlayerList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapterSelectedWorldTeam = new AdapterSelectedWorldTeam(mDialougList, this);
        rv_selectedPlayerList.setAdapter(adapterSelectedWorldTeam);
        rv_selectedPlayerList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                v.onTouchEvent(event);
                return true;
            }
        });
        dialogGroundView.show();
    }
}
