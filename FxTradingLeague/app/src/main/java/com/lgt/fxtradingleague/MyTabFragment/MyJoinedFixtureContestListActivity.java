package com.lgt.fxtradingleague.MyTabFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Bean.BeanMyJoinedContestList;
import com.lgt.fxtradingleague.Bean.BeanWiningInfoList;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lgt.fxtradingleague.Config.MYJOINCONTESTLIST;
import static com.lgt.fxtradingleague.Config.WINNINGINFOLIST;
import static com.lgt.fxtradingleague.Constants.MYJOINCONTESTLISTTYPE;
import static com.lgt.fxtradingleague.Constants.WINNINGINFOLISTTYPE;

public class MyJoinedFixtureContestListActivity extends AppCompatActivity implements ResponseManager {

    MyJoinedFixtureContestListActivity activity;
    Context context;
    RecyclerView Rv_MyJoinedContestList;
    AdapterMyJoinedContestList adapterMyJoinedContestList;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    public static String IntentMatchId,IntentTime,IntenTeamsName,IntentTeamOneName,IntentTeamTwoName;
    //Dialog dialog;
    BottomSheetDialog dialog;
    public static String ContestId,Matchid;
    List<BeanWiningInfoList> beanWinningLists;
    String prize_pool,contest_description;


    ImageView im_back;
    TextView tv_HeaderName,tv_MyJoinedContestTimer,tv_MyJoinedContestTeamsName;
    SessionManager sessionManager;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_NoDataAvailable;

    ImageView ivteam1Image,ivteam2Image;
    TextView team1Name,team2Name;
    TextView tv_CreateTeamTimer;
  public static String  team1Image,team2Image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_joined_contest_list);

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


        Validations.CountDownTimer(IntentTime,tv_CreateTeamTimer);
        Glide.with(context).load(team1Image).into(ivteam1Image);
        Glide.with(context).load(team2Image).into(ivteam2Image);
        if (IntentTeamOneName.length()>3){
            team1Name.setText(IntentTeamOneName.substring(0,3));

        }else {
            team1Name.setText(IntentTeamOneName);
        }
        if (IntentTeamTwoName.length()>3){
            team2Name.setText(IntentTeamTwoName.substring(0,3));
        }else {
            team2Name.setText(IntentTeamTwoName);
        }



        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        Rv_MyJoinedContestList.setHasFixedSize(true);
        Rv_MyJoinedContestList.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        Rv_MyJoinedContestList.setLayoutManager(mLayoutManager);
        Rv_MyJoinedContestList.setItemAnimator(new DefaultItemAnimator());


        Validations.CountDownTimer(IntentTime,tv_MyJoinedContestTimer);

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

        tv_HeaderName.setText("JOIN CONTESTS");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        tv_CreateTeamTimer=findViewById(R.id.tv_CreateTeamTimer);
        ivteam1Image=findViewById(R.id.team1Image);
        ivteam2Image=findViewById(R.id.team2Image);
        team1Name=findViewById(R.id.team1Name);
        team2Name=findViewById(R.id.team2Name);

       // Log.e("removeee", ContestListActivity.IntentTeamTwoName.replaceAll(" ",""));

       /* String team1=IntentTeamOneName.replaceAll(" ","");
        String team2 =IntentTeamTwoName.replaceAll(" ","");*/
       // tv_CreateTeamTimer.setText(IntentTime);
    /*    Validations.CountDownTimer(IntentTime,tv_CreateTeamTimer);
        Glide.with(context).load(team1Image).into(ivteam1Image);
        Glide.with(context).load(team2Image).into(ivteam2Image);
        team1Name.setText(IntentTeamOneName);
        team2Name.setText(IntentTeamOneName);*/


    }


    private void callMyJoinedContestList(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(MYJOINCONTESTLIST,
                    createRequestJson(), context, activity, MYJOINCONTESTLISTTYPE,
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

            Log.e("dasdasdasdas",IntentMatchId+"");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void callWinningInfoList(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(WINNINGINFOLIST,
                    createRequestJsonWin(), context, activity, WINNINGINFOLISTTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJsonWin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contest_id", ContestId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {



        if (type.equals(WINNINGINFOLISTTYPE)){
            try {
                JSONArray jsonArray = result.getJSONArray("data");

                beanWinningLists = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<List<BeanWiningInfoList>>() {
                        }.getType());

                dialog = new BottomSheetDialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_winning_breakups);
                final TextView tv_DClose = dialog.findViewById(R.id.tv_DClose);
                final TextView tv_DTotalWinning =dialog.findViewById(R.id.tv_DTotalWinning);
                final TextView tv_DBottomNote =dialog.findViewById(R.id.tv_DBottomNote);
                final LinearLayout LL_WinningBreackupList=dialog.findViewById(R.id.LL_WinningBreackupList);
                dialog.show();
                tv_DTotalWinning.setText(""+prize_pool);
                tv_DBottomNote.setText("Note: "+contest_description);
                tv_DClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                for (int i = 0; i <beanWinningLists .size(); i++) {

                    View to_add = LayoutInflater.from(context).inflate(R.layout.item_winning_breakup,
                            LL_WinningBreackupList,false);
                    TextView tv_Rank = to_add.findViewById(R.id.tv_Rank);
                    TextView tv_Price = to_add.findViewById(R.id.tv_Price);

                    tv_Rank.setText("Rank: "+beanWinningLists.get(i).getRank());
                    tv_Price.setText(""+beanWinningLists.get(i).getPrice());

                    LL_WinningBreackupList.addView(to_add);
                }


            }
            catch (Exception e){
                e.printStackTrace();
            }




        }
        else {

            tv_NoDataAvailable.setVisibility(View.GONE);
            Rv_MyJoinedContestList.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);

            try {
                JSONArray jsonArray = result.getJSONArray("data");
                Log.e("sasasasa",result+"");
                List<BeanMyJoinedContestList> beanContestLists = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<List<BeanMyJoinedContestList>>() {
                        }.getType());
                adapterMyJoinedContestList = new AdapterMyJoinedContestList(beanContestLists, activity);
                Rv_MyJoinedContestList.setAdapter(adapterMyJoinedContestList);

            } catch (Exception e) {
                e.printStackTrace();
            }

            adapterMyJoinedContestList.notifyDataSetChanged();

        }
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        //ShowToast(context,message);
        if (type.equals(MYJOINCONTESTLISTTYPE)){

            tv_NoDataAvailable.setVisibility(View.VISIBLE);
            Rv_MyJoinedContestList.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    public class AdapterMyJoinedContestList extends RecyclerView.Adapter<AdapterMyJoinedContestList.MyViewHolder> {
        private List<BeanMyJoinedContestList> mListenerList;
        Context mContext;


        public AdapterMyJoinedContestList(List<BeanMyJoinedContestList> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_TotalPrice,tv_WinnersCount,tv_EntryFees,tv_TeamLeftCount,tv_TotalTeamCount
                    ,tv_JoinContest,tv_MyJoinedTeamCount,tv_LiveContestName,tv_LiveContestDesc;

            ProgressBar PB_EntryProgress;


            public MyViewHolder(View view) {
                super(view);

                tv_TotalPrice = view.findViewById(R.id.tv_TotalPrice);
                tv_WinnersCount = view.findViewById(R.id.tv_WinnersCount);
                tv_EntryFees = view.findViewById(R.id.tv_EntryFees);
                tv_TeamLeftCount = view.findViewById(R.id.tv_TeamLeftCount);
                tv_TotalTeamCount = view.findViewById(R.id.tv_TotalTeamCount);
                PB_EntryProgress = view.findViewById(R.id.PB_EntryProgress);
                tv_JoinContest = view.findViewById(R.id.tv_JoinContest);
                tv_MyJoinedTeamCount = view.findViewById(R.id.tv_MyJoinedTeamCount);
                tv_LiveContestName = view.findViewById(R.id.tv_LiveContestName);
                tv_LiveContestDesc = view.findViewById(R.id.tv_LiveContestDesc);

            }

        }
        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_my_joined_contest_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.tv_JoinContest.setVisibility(View.VISIBLE);

            String contest_name= mListenerList.get(position).getContest_name();
            String contest_tag= mListenerList.get(position).getContest_tag();
            String winners= mListenerList.get(position).getWinners();
            prize_pool= mListenerList.get(position).getPrize_pool();
            String total_team= mListenerList.get(position).getTotal_team();
            String join_team= mListenerList.get(position).getJoin_team();
            String entry= mListenerList.get(position).getEntry();

            String contest_note1 = mListenerList.get(position).getContest_note1();
            String contest_note2= mListenerList.get(position).getContest_note2();
            String match_id= mListenerList.get(position).getMatch_id();
            String type= mListenerList.get(position).getType();
            String remaining_team= mListenerList.get(position).getRemaining_team();
            String joinedteamcount= mListenerList.get(position).getTeam_count();


            holder.tv_MyJoinedTeamCount.setText("Joined with "+joinedteamcount+" Team");
            holder.tv_TotalPrice.setText(""+prize_pool);
            holder.tv_WinnersCount.setText(winners);
            holder.tv_EntryFees.setText("Entry Token : "+ ""+entry);
            holder.tv_JoinContest.setText(""+entry);
            holder.tv_TeamLeftCount.setText(remaining_team+" Spots Left");
            holder.tv_TotalTeamCount.setText(total_team+" Teams");

            // float JoinTeamPercent =  Integer.parseInt(join_team)*100/Integer.parseInt(total_team);

            holder.PB_EntryProgress.setMax(Integer.parseInt(total_team));
            holder.PB_EntryProgress.setProgress(Integer.parseInt(join_team));
            holder.tv_LiveContestName.setText(contest_name);
            holder.tv_LiveContestDesc.setText(contest_tag);
            holder.tv_WinnersCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContestId = mListenerList.get(position).getContest_id();
                    contest_description= mListenerList.get(position).getContest_description();
                    prize_pool= mListenerList.get(position).getPrize_pool();

                    callWinningInfoList(true);
                }
            });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matchid = IntentMatchId;
                ContestId = mListenerList.get(position).getContest_id();
                Intent i = new Intent(activity, MyFixtureContestDetailsActivity.class);
                startActivity(i);
            }
        });

        }

    }

}
