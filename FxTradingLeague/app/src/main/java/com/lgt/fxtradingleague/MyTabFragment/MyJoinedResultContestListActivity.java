package com.lgt.fxtradingleague.MyTabFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Bean.BeanMyLiveJoinedContest;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lgt.fxtradingleague.Config.MYJOINRESULTCONTESTLIST;
import static com.lgt.fxtradingleague.Constants.MYJOINRESULTCONTESTLISTTYPE;

public class MyJoinedResultContestListActivity extends AppCompatActivity implements ResponseManager {



    MyJoinedResultContestListActivity activity;
    Context context;
    RecyclerView Rv_MyJoinedContestList;
    AdapterMyJoinedContestList adapterMyJoinedContestList;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    public static String IntentMatchId,IntentTime,IntenTeamsName,IntentTeamOneName,IntentTeamTwoName,team1Image,team2Image;

    public static String ContestId,Matchid;

    ImageView im_back;
    TextView tv_HeaderName,tv_MyJoinedContestTimer,tv_MyJoinedContestTeamsName;
    SessionManager sessionManager;

    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_NoDataAvailable;

    ImageView ivteam1Image,ivteam2Image;
    TextView team1Name,team2Name;
    TextView tv_CreateTeamTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_joined_result_contest_list);

        context = activity = this;
        initViews();

        sessionManager = new SessionManager();
        IntentMatchId = getIntent().getStringExtra("MatchId");
        IntentTime = getIntent().getStringExtra("Time");
        IntenTeamsName = getIntent().getStringExtra("TeamsName");
        IntentTeamOneName = getIntent().getStringExtra("TeamsOneName");
        IntentTeamTwoName = getIntent().getStringExtra("TeamsTwoName");
        team1Image=getIntent().getStringExtra("team1Image");
        team2Image=getIntent().getStringExtra("team2Image");

        tv_MyJoinedContestTeamsName.setText(IntenTeamsName);
        tv_MyJoinedContestTimer.setText(IntentTime);


        String team1=IntentTeamOneName.replaceAll(" ","");
        String team2 =IntentTeamTwoName.replaceAll(" ","");

        Glide.with(context).load(team1Image).into(ivteam1Image);
        Glide.with(context).load(team2Image).into(ivteam2Image);
        if (team1.length()>3){
            team1Name.setText(team1.substring(0,3));

        }else {
            team1Name.setText(team1);
        }
        if (team2.length()>3){
            team2Name.setText(team2.substring(0,3));
        }else {
            team2Name.setText(team2);
        }
        Validations.CountDownTimer(IntentTime,tv_CreateTeamTimer);


        if (tv_CreateTeamTimer.getText().toString().equalsIgnoreCase("Entry Over!")){
            tv_CreateTeamTimer.setText("Completed");
        }

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        Rv_MyJoinedContestList.setHasFixedSize(true);
        Rv_MyJoinedContestList.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        Rv_MyJoinedContestList.setLayoutManager(mLayoutManager);
        Rv_MyJoinedContestList.setItemAnimator(new DefaultItemAnimator());



        Validations.CountDownTimer(IntentTime, tv_MyJoinedContestTimer);
        tv_MyJoinedContestTimer.setText("Completed");

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(true);
                                    callMyJoinedContestList(false);
                                }
                            }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callMyJoinedContestList(false);
            }
        });

    }

    public void initViews(){
        Rv_MyJoinedContestList =  findViewById(R.id.Rv_MyJoinedContestList);
        tv_MyJoinedContestTimer =  findViewById(R.id.tv_MyJoinedContestTimer);
        tv_MyJoinedContestTeamsName =  findViewById(R.id.tv_MyJoinedContestTeamsName);

        tv_NoDataAvailable =  findViewById(R.id.tv_NoDataAvailable);
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        tv_CreateTeamTimer=findViewById(R.id.tv_CreateTeamTimer);



        ivteam1Image=findViewById(R.id.team1Image);
        ivteam2Image=findViewById(R.id.team2Image);
        team1Name=findViewById(R.id.team1Name);
        team2Name=findViewById(R.id.team2Name);

        tv_HeaderName.setText("JOIN CONTESTS");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

    }

    private void callMyJoinedContestList(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(MYJOINRESULTCONTESTLIST,
                    createRequestJson(), context, activity, MYJOINRESULTCONTESTLISTTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("match_id", IntentMatchId);
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        try {

            tv_NoDataAvailable.setVisibility(View.GONE);
            Rv_MyJoinedContestList.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);

            JSONArray jsonArray = result.getJSONArray("data");
            if (jsonArray!=null){
                List<BeanMyLiveJoinedContest> beanContestLists = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<List<BeanMyLiveJoinedContest>>() {
                        }.getType());
                adapterMyJoinedContestList = new AdapterMyJoinedContestList(beanContestLists, activity);
                Rv_MyJoinedContestList.setAdapter(adapterMyJoinedContestList);
                adapterMyJoinedContestList.notifyDataSetChanged();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        //ShowToast(context,message);
        tv_NoDataAvailable.setVisibility(View.VISIBLE);
        Rv_MyJoinedContestList.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    public class AdapterMyJoinedContestList extends RecyclerView.Adapter<AdapterMyJoinedContestList.MyViewHolder> {
        private List<BeanMyLiveJoinedContest> mListenerList;
        Context mContext;


        public AdapterMyJoinedContestList(List<BeanMyLiveJoinedContest> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_LiveContestName,tv_LiveContestDesc,tv_LiveContestFees,
                    tv_LiveJoinedWith,tv_ContestPoints
                    ,tv_ContestRank,tv_WinningAmount ;

            public MyViewHolder(View view) {
                super(view);

                tv_LiveContestName = view.findViewById(R.id.tv_LiveContestName);
                tv_LiveContestDesc = view.findViewById(R.id.tv_LiveContestDesc);
                tv_LiveContestFees = view.findViewById(R.id.tv_LiveContestFees);
                tv_LiveJoinedWith = view.findViewById(R.id.tv_LiveJoinedWith);
                tv_ContestPoints = view.findViewById(R.id.tv_ContestPoints);
                tv_ContestRank = view.findViewById(R.id.tv_ContestRank);
                tv_WinningAmount = view.findViewById(R.id.tv_WinningAmount);


            }

        }
        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_live_contest_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {



            String contest_name= mListenerList.get(position).getContest_name();
            String contest_tag= mListenerList.get(position).getContest_tag();
            String winners= mListenerList.get(position).getWinners();

            String total_team= mListenerList.get(position).getTotal_team();
            String join_team= mListenerList.get(position).getJoin_team();
            String entry= mListenerList.get(position).getEntry();

            String contest_note1 = mListenerList.get(position).getContest_note1();
            String contest_note2= mListenerList.get(position).getContest_note2();
            String match_id= mListenerList.get(position).getMatch_id();
            String type= mListenerList.get(position).getType();
            String remaining_team= mListenerList.get(position).getRemaining_team();
            String points= mListenerList.get(position).getPoints();
            String rank= mListenerList.get(position).getRank();
            String JoinedWithTeamName = mListenerList.get(position).getTeam_name();
            String winning_amount = mListenerList.get(position).getWinning_amount();

            holder.tv_WinningAmount.setVisibility(View.VISIBLE);
            holder.tv_WinningAmount.setText("₹ "+winning_amount);

            holder.tv_LiveContestName.setText(contest_name);
            holder.tv_LiveContestDesc.setText(contest_tag);
            holder.tv_LiveContestFees.setText("₹ "+entry);
            holder.tv_ContestRank.setText(rank);
            holder.tv_ContestPoints.setText(points);
            holder.tv_LiveJoinedWith.setText(JoinedWithTeamName);



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Matchid = IntentMatchId;
                    ContestId = mListenerList.get(position).getContest_id();
                    Intent i = new Intent(activity, MyResultContestDetailsActivity.class);
                    startActivity(i);
                }
            });
        }

    }
}
