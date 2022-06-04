package com.lgt.Ace11.MyTabFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Class.Validations;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.BeanMyFixLeaderboard;
import com.lgt.Ace11.Bean.BeanWiningInfoList;
import com.lgt.Ace11.Config;
import com.lgt.Ace11.Constants;
import com.lgt.Ace11.Playing11;
import com.lgt.Ace11.R;
import com.lgt.Ace11.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lgt.Ace11.SinglePlayerPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.MYLIVELEADERBOARD;
import static com.lgt.Ace11.Config.MYLIVELEADERBOARDTEAM;
import static com.lgt.Ace11.Config.WINNINGINFOLIST;
import static com.lgt.Ace11.Constants.MYLIVELEADERBORADTEAMTYPE;
import static com.lgt.Ace11.Constants.MYLIVELEADERBORADTYPE;
import static com.lgt.Ace11.Constants.WINNINGINFOLISTTYPE;

public class MyLiveContestDetailsActivity extends AppCompatActivity implements ResponseManager {


    MyLiveContestDetailsActivity activity;
    Context context;
    RecyclerView Rv_MyLiveLeaderboard;

    AdapterLeaderboard adapterLeaderboard;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    ImageView im_back;
    TextView tv_HeaderName, tv_ContestTimer, tv_ContestTeamsName;

    TextView tv_TotalPrice, tv_EntryFees, tv_WinnersCount, tv_TotalJoinedTeamCount,
            tv_TeamOneScore, tv_TeamTwoScore, tv_TeamOneOver, tv_TeamTwoOver, tv_MatchResult, tv_TeamOneName, tv_TeamTwoName, tv_Score_refresh;
    LinearLayout linearLayout;

    private RelativeLayout LL_GroundPlayer;
    String UserTeamId;
    BottomSheetDialog dialogGroundView = null;
    SessionManager sessionManager;
    String match_status, ApiUserId;
    List<BeanWiningInfoList> beanWinningLists;

    //Dialog dialog;
    BottomSheetDialog dialog;
    String prize_pool, contest_description;
    SwipeRefreshLayout swipeRefreshLayout;
    Handler mHandler;
    boolean Add_View = true;
    //header layout
    ImageView ivteam1Image, ivteam2Image;
    TextView team1Name, team2Name;
    TextView tv_CreateTeamTimer;

    String mUniqueID;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_live_contest_details);

        context = activity = this;
        initViews();
        sessionManager = new SessionManager();

        tv_ContestTeamsName.setText(MyJoinedLiveContestListActivity.IntenTeamsName);
        tv_ContestTimer.setText(MyJoinedLiveContestListActivity.IntentTime);

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        Rv_MyLiveLeaderboard.setHasFixedSize(true);
        Rv_MyLiveLeaderboard.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        Rv_MyLiveLeaderboard.setLayoutManager(mLayoutManager);
        Rv_MyLiveLeaderboard.setItemAnimator(new DefaultItemAnimator());


        Validations.CountDownTimer(MyJoinedLiveContestListActivity.IntentTime, tv_ContestTimer);
        tv_ContestTimer.setText("In Progress");

        tv_TeamOneName.setText(MyJoinedLiveContestListActivity.IntentTeamOneName);
        tv_TeamTwoName.setText(MyJoinedLiveContestListActivity.IntentTeamTwoName);
        tv_WinnersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callWinningInfoList(true);

            }
        });

        callLeaderboardList(false);


        tv_Score_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callWinningInfoList(true);
                // callLeaderboardList(true);

            }
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);
                                        // callLeaderboardList(false);
                                    }
                                }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                // callLeaderboardList(false);
            }
        });

        // MyLiveContestDetailsActivity.this.mHandler = new Handler();

        // MyLiveContestDetailsActivity.this.mHandler.postDelayed(m_Runnable, 15000);

    }

    public void initViews() {
        Rv_MyLiveLeaderboard = findViewById(R.id.Rv_MyLiveLeaderboard);
        tv_ContestTimer = findViewById(R.id.tv_ContestTimer);
        tv_ContestTeamsName = findViewById(R.id.tv_ContestTeamsName);

        tv_WinnersCount = findViewById(R.id.tv_WinnersCount);
        tv_EntryFees = findViewById(R.id.tv_EntryFees);
        tv_TotalPrice = findViewById(R.id.tv_TotalPrice);
        tv_TotalJoinedTeamCount = findViewById(R.id.tv_TotalJoinedTeamCount);


        tv_TeamOneScore = findViewById(R.id.tv_TeamOneScore);
        tv_TeamTwoScore = findViewById(R.id.tv_TeamTwoScore);
        tv_TeamOneOver = findViewById(R.id.tv_TeamOneOver);
        tv_TeamTwoOver = findViewById(R.id.tv_TeamTwoOver);
        tv_MatchResult = findViewById(R.id.tv_MatchResult);
        tv_TeamOneName = findViewById(R.id.tv_TeamOneName);
        tv_TeamTwoName = findViewById(R.id.tv_TeamTwoName);
        //tv_MatchResult.setVisibility(View.VISIBLE);
        tv_Score_refresh = findViewById(R.id.tv_Score_refresh);
        tv_Score_refresh.setVisibility(View.GONE);
        linearLayout = findViewById(R.id.linearlayout2);
        //linearLayout.setVisibility(View.VISIBLE);

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        tv_HeaderName.setText("CONTESTS");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        //header layout
        ivteam1Image = findViewById(R.id.team1Image);
        ivteam2Image = findViewById(R.id.team2Image);
        team1Name = findViewById(R.id.team1Name);
        team2Name = findViewById(R.id.team2Name);
        tv_CreateTeamTimer = findViewById(R.id.tv_CreateTeamTimer);

        if (MyJoinedLiveContestListActivity.team1.length() > 3) {
            team1Name.setText(MyJoinedLiveContestListActivity.team1.substring(0, 3));

        } else {
            team1Name.setText(MyJoinedLiveContestListActivity.team1);
        }
        if (MyJoinedLiveContestListActivity.team2.length() > 3) {
            team2Name.setText(MyJoinedLiveContestListActivity.team2.substring(0, 3));
        } else {
            team2Name.setText(MyJoinedLiveContestListActivity.team2);
        }

        Validations.CountDownTimer(MyJoinedLiveContestListActivity.IntentTime, tv_CreateTeamTimer);

        if (tv_CreateTeamTimer.getText().toString().equalsIgnoreCase("Entry Over!")) {
            tv_CreateTeamTimer.setText("In Progress");
        }
        Glide.with(context).load(MyJoinedLiveContestListActivity.team1Image).into(ivteam1Image);
        Glide.with(context).load(MyJoinedLiveContestListActivity.team2Image).into(ivteam2Image);
        //
    }


    private void callWinningInfoList(boolean isShowLoader) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contest_id", MyJoinedLiveContestListActivity.ContestId);
            apiRequestManager.callAPI(WINNINGINFOLIST,
                    jsonObject, context, activity, WINNINGINFOLISTTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void callLeaderboardList(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(MYLIVELEADERBOARD,
                    createRequestJson(), context, activity, MYLIVELEADERBORADTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contest_id", MyJoinedLiveContestListActivity.ContestId);
            jsonObject.put("match_id", MyJoinedLiveContestListActivity.IntentMatchId);
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());

            Log.e("ASdasdrwerwer", MyJoinedLiveContestListActivity.ContestId + "---" + MyJoinedLiveContestListActivity.IntentMatchId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void callLeaderboardTeam(boolean isShowLoader) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("team_id", UserTeamId);
            jsonObject.put("match_id", MyJoinedLiveContestListActivity.Matchid);

            Log.e("sdadasdsdasd", UserTeamId + "--=-=-" + MyJoinedLiveContestListActivity.Matchid);


            apiRequestManager.callAPI(MYLIVELEADERBOARDTEAM,
                    jsonObject, context, activity, MYLIVELEADERBORADTEAMTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        if (type.equals(MYLIVELEADERBORADTEAMTYPE)) {

            Log.e("asddasdasdrerew", result + "");

            DialogGroundView(result);
        } else if (type.equals(WINNINGINFOLISTTYPE)) {
            try {
                JSONArray jsonArray = result.getJSONArray("data");

                Log.e("asddasdasdrerew", jsonArray + "");

                beanWinningLists = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<List<BeanWiningInfoList>>() {
                        }.getType());

                dialog = new BottomSheetDialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_winning_breakups);
                final TextView tv_DClose = dialog.findViewById(R.id.tv_DClose);
                final TextView tv_DTotalWinning = dialog.findViewById(R.id.tv_DTotalWinning);
                final TextView tv_DBottomNote = dialog.findViewById(R.id.tv_DBottomNote);
                final LinearLayout LL_WinningBreackupList = dialog.findViewById(R.id.LL_WinningBreackupList);
                dialog.show();
                tv_DTotalWinning.setText("" + prize_pool);
                tv_DBottomNote.setText("Note: " + contest_description);
                tv_DClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                for (int i = 0; i < beanWinningLists.size(); i++) {

                    View to_add = LayoutInflater.from(context).inflate(R.layout.item_winning_breakup,
                            LL_WinningBreackupList, false);
                    TextView tv_Rank = to_add.findViewById(R.id.tv_Rank);
                    TextView tv_Price = to_add.findViewById(R.id.tv_Price);

                    tv_Rank.setText("Rank: " + beanWinningLists.get(i).getRank());
                    tv_Price.setText("" + beanWinningLists.get(i).getPrice());

                    LL_WinningBreackupList.addView(to_add);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Constants.commonLog("mylivecontesdt"+ result + "");
            swipeRefreshLayout.setRefreshing(false);

            try {

                prize_pool = result.getString("prize_pool");
                String entry = result.getString("entry");
                String all_team_count = result.getString("all_team_count");
                String user_team_count = result.getString("user_team_count");
                String winners = result.getString("winners");
                contest_description = result.getString("contest_description");

                match_status = result.getString("match_status");
                String team_one_score = result.getString("team1Score");
                String team_two_score = result.getString("team2Score");
                String team_one_over = result.getString("team1Over");
                String team_two_over = result.getString("team2Over");
                String match_status_note = result.getString("match_status_note");

                tv_EntryFees.setText("" + entry);
                tv_TotalPrice.setText("" + prize_pool);
                tv_WinnersCount.setText(winners);
                tv_TotalJoinedTeamCount.setText(all_team_count + " Teams");
                tv_TeamOneScore.setText("Score " + team_one_score);
                tv_TeamTwoScore.setText("Score " + team_two_score);
                tv_TeamOneOver.setText("Over " + team_one_over);
                tv_TeamTwoOver.setText("Over " + team_two_over);
                JSONArray jsonArray = result.getJSONArray("leaderboard");
                List<BeanMyFixLeaderboard> beanContestLists = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<List<BeanMyFixLeaderboard>>() {
                        }.getType());
                adapterLeaderboard = new AdapterLeaderboard(beanContestLists, activity);
                Rv_MyLiveLeaderboard.setAdapter(adapterLeaderboard);

            } catch (Exception e) {
                e.printStackTrace();
            }
            adapterLeaderboard.notifyDataSetChanged();
        }
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        //ShowToast(context,message);
        swipeRefreshLayout.setRefreshing(false);

    }

    public class AdapterLeaderboard extends RecyclerView.Adapter<AdapterLeaderboard.MyViewHolder> {
        private List<BeanMyFixLeaderboard> mListenerList;
        Context mContext;


        public AdapterLeaderboard(List<BeanMyFixLeaderboard> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_LeaderboardPlayerTeamName, tv_LeaderboardPlayerRank, tv_LeaderboardPlayerPoints;
            ImageView im_LeaderboardPlayerAvtar;
            RelativeLayout RL_LeaderboardMain;


            public MyViewHolder(View view) {
                super(view);

                im_LeaderboardPlayerAvtar = view.findViewById(R.id.im_LeaderboardPlayerAvtar);
                tv_LeaderboardPlayerTeamName = view.findViewById(R.id.tv_LeaderboardPlayerTeamName);
                tv_LeaderboardPlayerRank = view.findViewById(R.id.tv_LeaderboardPlayerRank);
                tv_LeaderboardPlayerPoints = view.findViewById(R.id.tv_LeaderboardPlayerPoints);
                RL_LeaderboardMain = view.findViewById(R.id.RL_LeaderboardMain);
            }

        }

        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_live_leaderboard_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            ApiUserId = mListenerList.get(position).getUser_id();
            String name = mListenerList.get(position).getName();
            String rank = mListenerList.get(position).getRank();
            String id = mListenerList.get(position).getId();
            String Image = mListenerList.get(position).getImage();
            String Team = mListenerList.get(position).getTeam();
            String Points = mListenerList.get(position).getPoints();

            holder.tv_LeaderboardPlayerTeamName.setText(name + "(T" + Team + ")");
            holder.tv_LeaderboardPlayerRank.setText(rank);
            holder.tv_LeaderboardPlayerPoints.setText(Points + " Points");

            Glide.with(activity).load(Config.LEADERBOARDPLAYERIMAGE + Image)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_LeaderboardPlayerAvtar);

            if (ApiUserId.equals(sessionManager.getUser(activity).getUser_id())) {
                holder.RL_LeaderboardMain.setBackgroundResource(R.drawable.leaderboard_dark_rectangle);
                holder.tv_LeaderboardPlayerTeamName.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_LeaderboardPlayerPoints.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_LeaderboardPlayerRank.setTextColor(Color.parseColor("#ffffff"));
            } else {
                holder.RL_LeaderboardMain.setBackgroundResource(R.drawable.white_rectangle);
                holder.tv_LeaderboardPlayerTeamName.setTextColor(Color.parseColor("#1e1e1e"));
                holder.tv_LeaderboardPlayerPoints.setTextColor(Color.parseColor("#1e1e1e"));
                holder.tv_LeaderboardPlayerRank.setTextColor(Color.parseColor("#1e1e1e"));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constants.commonLog("match_status:"+match_status+"");
                    UserTeamId = mListenerList.get(position).getTeamid();
                    ApiUserId = mListenerList.get(position).getUser_id();
                    if (ApiUserId.equals(sessionManager.getUser(activity).getUser_id())) {
                        Constants.commonLog("match_status:"+match_status+"");
                        callLeaderboardTeam(true);
                    } else {
                        if (match_status.equals("1")) {
                            callLeaderboardTeam(true);
                        } else {
                            ShowToast(context, "Please Wait! Match has not started yet.");
                        }
                    }
                }
            });
        }
    }

    public void DialogGroundView(JSONObject result) {
       /* dialogGroundView = new BottomSheetDialog(activity);
        dialogGroundView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGroundView.setContentView(R.layout.dialog_ground_view);

        final LinearLayout LL_GroundWK = dialogGroundView.findViewById(R.id.LL_GroundWK);
        final LinearLayout LL_GroundBAT = dialogGroundView.findViewById(R.id.LL_GroundBAT);
        final LinearLayout LL_GroundAR = dialogGroundView.findViewById(R.id.LL_GroundAR);
        final LinearLayout LL_GroundBOWL = dialogGroundView.findViewById(R.id.LL_GroundBOWL);
        ImageView im_CloseIcon = dialogGroundView.findViewById(R.id.im_CloseIcon);
        dialogGroundView.show();
        im_CloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGroundView.dismiss();
            }
        });*/
        if (dialogGroundView == null) {
            dialogGroundView = new BottomSheetDialog(activity);
            dialogGroundView.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogGroundView.setContentView(R.layout.dialog_ground_view); // initiate it the way you need
            dialogGroundView.show();

        } else if (!dialogGroundView.isShowing()) {
            dialogGroundView.show();
        }


        TextView tvPlaying11 = dialogGroundView.findViewById(R.id.tvPlaying11);


        LL_GroundPlayer = dialogGroundView.findViewById(R.id.LL_GroundPlayer);

        final LinearLayout LL_GroundWK = dialogGroundView.findViewById(R.id.LL_GroundWK);
        final LinearLayout LL_GroundBAT = dialogGroundView.findViewById(R.id.LL_GroundBAT);
        final LinearLayout LL_GroundAR = dialogGroundView.findViewById(R.id.LL_GroundAR);
        final LinearLayout LL_GroundBOWL = dialogGroundView.findViewById(R.id.LL_GroundBOWL);
        ImageView im_CloseIcon = dialogGroundView.findViewById(R.id.im_CloseIcon);
        ImageView im_Refresh = dialogGroundView.findViewById(R.id.im_Refresh);
        ImageView im_Editteam = dialogGroundView.findViewById(R.id.im_Editteam);

        tvPlaying11.setVisibility(View.VISIBLE);
        im_Editteam.setVisibility(View.GONE);
        im_Refresh.setVisibility(View.GONE);
        im_CloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGroundView.dismiss();
                dialogGroundView = null;
                Add_View = true;
            }
        });

        tvPlaying11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open playing 11 activty
                Intent intentPlaying11 = new Intent(MyLiveContestDetailsActivity.this, Playing11.class);
                intentPlaying11.putExtra("KEY_UNIQUE_ID", mUniqueID);
                Log.e("hjkhjklhhjkl", mUniqueID + "");
                startActivity(intentPlaying11);
            }
        });

        im_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLeaderboardTeam(true);
                Add_View = false;
            }
        });
        dialogGroundView.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                dialogGroundView = null;
                Add_View = true;

            }
        });
        try {
            JSONArray jsonArray = result.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {

                Log.e("ASdasrwewe", "rweewrffsd");
                final JSONObject userData = jsonArray.getJSONObject(i);

                final String PlayerId = userData.getString("player_id");
                String IsSelected = userData.getString("is_select");
                String Role = userData.getString("short_term");
                final String player_shortname = userData.getString("player_shortname");
                String PlayerImage = userData.getString("image");
                final String PlayerCredit = userData.getString("credit_points");
                String PlayerPoint = userData.getString("points");
                String is_captain = userData.getString("is_captain");
                String is_vicecaptain = userData.getString("is_vicecaptain");

                final String newPid = userData.getString("pid");

                mUniqueID = userData.getString("unique_id");

                if (is_captain == null) {
                    is_captain = "0";
                }
                if (is_vicecaptain == null) {
                    is_vicecaptain = "0";
                }


                if (IsSelected.equals("1")) {
                    if (Role.equals("WK")) {
                        View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                LL_GroundWK, false);
                        ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                        TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                        TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                        TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);

                        Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)

                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(im_GroundPlayerImage);
                        tv_GroundPlayerName.setText(player_shortname);
                        tv_GroundPlayerCredit.setText(PlayerPoint + " Pt");

                        if (is_captain.equals("1")) {
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText(" C ");
                        }
                        if (is_vicecaptain.equals("1")) {
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText("VC");
                        }
                        // LL_GroundWK.addView(to_add);
                        if (Add_View) {
                            LL_GroundWK.addView(to_add);
                        }

                        im_GroundPlayerImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("Dasdasrewrwerwer", newPid + "" + player_shortname);
                                sendPidToSinglePlayerPoints(newPid, mUniqueID);
                            }
                        });


                    } else if (Role.equals("BAT")) {
                        View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                LL_GroundBAT, false);
                        ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                        TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                        TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                        TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);
                        Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)

                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(im_GroundPlayerImage);
                        tv_GroundPlayerName.setText(player_shortname);
                        tv_GroundPlayerCredit.setText(PlayerPoint + " Pt");
                        if (is_captain.equals("1")) {
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText(" C ");
                        }
                        if (is_vicecaptain.equals("1")) {
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText("VC");
                        }
                        // LL_GroundBAT.addView(to_add);
                        if (Add_View) {
                            LL_GroundBAT.addView(to_add);
                        }

                        im_GroundPlayerImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendPidToSinglePlayerPoints(newPid, mUniqueID);
                                Log.e("Dasdasrewrwerwer", newPid + "" + player_shortname);
                            }
                        });


                    } else if (Role.equals("AR")) {
                        View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                LL_GroundAR, false);
                        ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                        TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                        TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                        TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);
                        Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)

                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(im_GroundPlayerImage);
                        tv_GroundPlayerName.setText(player_shortname);
                        tv_GroundPlayerCredit.setText(PlayerPoint + " Pt");
                        if (is_captain.equals("1")) {
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText(" C ");
                        }
                        if (is_vicecaptain.equals("1")) {
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText("VC");
                        }
                        //  LL_GroundAR.addView(to_add);
                        if (Add_View) {
                            LL_GroundAR.addView(to_add);
                        }

                        im_GroundPlayerImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendPidToSinglePlayerPoints(newPid, mUniqueID);
                            }
                        });


                    } else if (Role.equals("BOWL")) {
                        View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                LL_GroundBOWL, false);
                        ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                        TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                        TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                        TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);
                        Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)

                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(im_GroundPlayerImage);

                        tv_GroundPlayerName.setText(player_shortname);
                        tv_GroundPlayerCredit.setText(PlayerPoint + " Pt");
                        if (is_captain.equals("1")) {
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText(" C ");
                        }
                        if (is_vicecaptain.equals("1")) {
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText("VC");
                        }
                        //LL_GroundBOWL.addView(to_add);
                        if (Add_View) {
                            LL_GroundBOWL.addView(to_add);
                        }

                        im_GroundPlayerImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendPidToSinglePlayerPoints(newPid, mUniqueID);
                            }
                        });


                    }
                }

            }
            Add_View = false;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendPidToSinglePlayerPoints(String newPid, String mMatchUniqueID) {
        Intent intent = new Intent(MyLiveContestDetailsActivity.this, SinglePlayerPoints.class);
        intent.putExtra("KEY_NEW_PID", newPid);
        intent.putExtra("KEY_MATCH_UNIQUE_ID", mMatchUniqueID);
        startActivity(intent);
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            // callLeaderboardList(false);
            // MyLiveContestDetailsActivity.this.mHandler.postDelayed(m_Runnable, 15000);
        }

    };
}
