package com.lgt.fxtradingleague.HomeTabsFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Bean.BeanHomeResult;
import com.lgt.fxtradingleague.Config;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lgt.fxtradingleague.TradingPackage.JoinPackageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lgt.fxtradingleague.Constants.RESULTHOMETYPE;


public class FragmentResults extends Fragment implements ResponseManager {
    CardView cv_crypto_daily_contest,cv_crypto_weekly_contest,cv_crypto_monthly_contest;
    HomeActivity activity;
    Context context;
    RecyclerView Rv_HomeResult;
    AdapterResultList adapterResultList;
    String CURRENCY_TYPE="CRYPTO";
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_NoDataAvailable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_results, container, false);

        context = activity = (HomeActivity)getActivity();
        initViews(v);
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);

        Rv_HomeResult.setHasFixedSize(true);
        Rv_HomeResult.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        Rv_HomeResult.setLayoutManager(mLayoutManager);
        Rv_HomeResult.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        // callHomeResult(false);
                                    }
                                }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // callHomeResult(false);
            }
        });

        // callHomeResult(true);
        return v;
    }

    public void initViews(View v){
        Rv_HomeResult = v.findViewById(R.id.Rv_HomeResult);
        cv_crypto_monthly_contest = v.findViewById(R.id.cv_crypto_monthly_contest);
        cv_crypto_weekly_contest = v.findViewById(R.id.cv_crypto_weekly_contest);
        cv_crypto_daily_contest = v.findViewById(R.id.cv_crypto_daily_contest);
        tv_NoDataAvailable = v.findViewById(R.id.tv_NoDataAvailable);

        cv_crypto_daily_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent daily_contest = new Intent(getActivity(), JoinPackageActivity.class);
                daily_contest.putExtra("contest_type","Daily");
                daily_contest.putExtra("currency_type",CURRENCY_TYPE);
                startActivity(daily_contest);
            }
        });

        cv_crypto_weekly_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weekly_contest = new Intent(getActivity(), JoinPackageActivity.class);
                weekly_contest.putExtra("contest_type","Weekly");
                weekly_contest.putExtra("currency_type",CURRENCY_TYPE);
                startActivity(weekly_contest);
            }
        });

        cv_crypto_monthly_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monthly_contest = new Intent(getActivity(), JoinPackageActivity.class);
                monthly_contest.putExtra("contest_type","Monthly");
                monthly_contest.putExtra("currency_type",CURRENCY_TYPE);
                startActivity(monthly_contest);
            }
        });
    }

    private void callHomeResult(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI("",
                    createRequestJson(), context, activity, RESULTHOMETYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "Result");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        swipeRefreshLayout.setRefreshing(false);
        Rv_HomeResult.setVisibility(View.VISIBLE);
        tv_NoDataAvailable.setVisibility(View.GONE);

        Log.e("resultttt",result+"");
        try {
            JSONArray jsonArray = result.getJSONArray("data");
            List<BeanHomeResult> beanHomeResult = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<BeanHomeResult>>() {
            }.getType());
            adapterResultList = new AdapterResultList(beanHomeResult, activity);
            Rv_HomeResult.setAdapter(adapterResultList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        adapterResultList.notifyDataSetChanged();
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        Rv_HomeResult.setVisibility(View.GONE);
        tv_NoDataAvailable.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        //ShowToast(context,message);
    }

    public class AdapterResultList extends RecyclerView.Adapter<AdapterResultList.MyViewHolder> {
        private List<BeanHomeResult> mListenerList;
        Context mContext;

        public AdapterResultList(List<BeanHomeResult> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_TeamOneName,tv_TeamsName,tv_TimeRemained,tv_TeamTwoName,tv_TeamOneScore,tv_TeamTwoScore,
                    tv_TeamOneOver,tv_TeamTwoOver,tv_MatchResult,matchType;
            LinearLayout linearLayout;
            ImageView im_Team1,im_Team2;


            public MyViewHolder(View view) {
                super(view);

                im_Team1 = view.findViewById(R.id.im_Team1);
                tv_TeamOneName = view.findViewById(R.id.tv_TeamOneName);
            //   tv_TeamsName = view.findViewById(R.id.tv_TeamsName);
                tv_TimeRemained = view.findViewById(R.id.tv_TimeRemained);
                im_Team2 = view.findViewById(R.id.im_Team2);
                tv_TeamTwoName = view.findViewById(R.id.tv_TeamTwoName);
              //  tv_TeamOneScore=view.findViewById(R.id.tv_TeamOneScore);
              //  tv_TeamTwoScore=view.findViewById(R.id.tv_TeamTwoScore);
              //  tv_TeamOneOver=view.findViewById(R.id.tv_TeamOneOver);
             //   tv_TeamTwoOver=view.findViewById(R.id.tv_TeamTwoOver);
                tv_MatchResult=view.findViewById(R.id.tv_MatchResult);
                tv_MatchResult.setVisibility(View.VISIBLE);
                matchType=view.findViewById(R.id.matchType);

              //  linearLayout=view.findViewById(R.id.linearlayout2);
              //  linearLayout.setVisibility(View.GONE);
            }
        }
        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_fixtures_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final String match_id = mListenerList.get(position).getMatch_id();
            String teamid1 = mListenerList.get(position).getTeamid1();
            String match_status = mListenerList.get(position).getMatch_status();
            String type = mListenerList.get(position).getType();
            final int time = mListenerList.get(position).getTime();
            String teamid2 = mListenerList.get(position).getTeamid2();
            String team_name1 = mListenerList.get(position).getTeam_name1();
            final String team_image1 = mListenerList.get(position).getTeam_image1();
            final String team_short_name1 = mListenerList.get(position).getTeam_short_name1();
            String team_name2 = mListenerList.get(position).getTeam_name2();
            final String team_image2 = mListenerList.get(position).getTeam_image2();
            final String team_short_name2 = mListenerList.get(position).getTeam_short_name2();
            final String team_one_score=mListenerList.get(position).getTeam1Score();
            final String team_two_score=mListenerList.get(position).getTeam2Score();
            final String team_one_over=mListenerList.get(position).getTeam1Over();
            final  String team_two_over=mListenerList.get(position).getTeam2Over();
            final String match_status_note=mListenerList.get(position).getMatch_status_note();

            if (team_name1.length()>3){
                holder.tv_TeamOneName.setText(team_name1.substring(0,3));

            }else {
                holder.tv_TeamOneName.setText(team_name1);
            }
            if (team_name2.length()>3){
                holder.tv_TeamTwoName.setText(team_name2.substring(0,3));
            }else {
                holder.tv_TeamTwoName.setText(team_name2);
            }
         //   holder.tv_TeamsName.setText(team_short_name1+" vs "+team_short_name2+"\n"+type);
            holder.matchType.setText(type);

            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE+team_image1)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team1);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE+team_image2)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team2);

            if (match_status.equals("Result")){
                holder.tv_TimeRemained.setText("Completed");

            }

            // holder.tv_TeamOneScore.setText("Score:-"+team_one_score);
            // holder.tv_TeamTwoScore.setText("Score:-"+team_two_score);
            // holder.tv_TeamOneOver.setText("Over:-"+team_one_over);
            // holder.tv_TeamTwoOver.setText("Over:-"+team_two_over);

            holder.tv_MatchResult.setText(match_status_note);
            //Disabling click 07-02-2020

           /* holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent k = new Intent(activity, MyJoinedResultContestListActivity.class);
                    k.putExtra("MatchId",match_id);
                    k.putExtra("Time",time+"");
                  //  k.putExtra("TeamsName", holder.tv_TeamsName.getText().toString());
                    k.putExtra("TeamsOneName", team_short_name1);
                    k.putExtra("TeamsTwoName", team_short_name2);
                    k.putExtra("team1Image",Config.TEAMFLAGIMAGE+team_image1);
                    k.putExtra("team2Image",Config.TEAMFLAGIMAGE+team_image2);
                    startActivity(k);
                }
            });*/

        }

    }
}
