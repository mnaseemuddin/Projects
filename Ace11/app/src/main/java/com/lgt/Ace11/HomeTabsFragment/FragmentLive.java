package com.lgt.Ace11.HomeTabsFragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.BeanHomeLive;
import com.lgt.Ace11.Config;
import com.lgt.Ace11.HomeActivity;
import com.lgt.Ace11.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lgt.Ace11.Config.HOMEFIXTURES;
import static com.lgt.Ace11.Constants.LIVEHOMETYPE;


public class FragmentLive extends Fragment implements ResponseManager {


    HomeActivity activity;
    Context context;
    RecyclerView Rv_HomeLive;
    AdapterLiveList adapterLiveList;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_NoDataAvailable,tv_Score_refresh;
    Handler mHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live, container, false);
        context = activity = (HomeActivity)getActivity();
        initViews(v);
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        Rv_HomeLive.setHasFixedSize(true);
        Rv_HomeLive.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        Rv_HomeLive.setLayoutManager(mLayoutManager);
        Rv_HomeLive.setItemAnimator(new DefaultItemAnimator());


        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        callHomeLive(false);
                                    }
                                }
        );
        tv_Score_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callHomeLive(true);

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callHomeLive(false);
            }
        });

        callHomeLive(true);
        // FragmentLive.this.mHandler = new Handler();

        // FragmentLive.this.mHandler.postDelayed(m_Runnable,15000);
        return v;

    }

    public void initViews(View v){
        Rv_HomeLive = v.findViewById(R.id.Rv_HomeLive);
        tv_NoDataAvailable = v.findViewById(R.id.tv_NoDataAvailable);
        tv_Score_refresh=v.findViewById(R.id.tv_Score_refresh);
    }

    private void callHomeLive(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(HOMEFIXTURES,
                    createRequestJson(), context, activity, LIVEHOMETYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "Live");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        swipeRefreshLayout.setRefreshing(false);
        Rv_HomeLive.setVisibility(View.VISIBLE);
        // tv_Score_refresh.setVisibility(View.VISIBLE);
        tv_NoDataAvailable.setVisibility(View.GONE);

        try {
            JSONArray jsonArray = result.getJSONArray("data");
            List<BeanHomeLive> beanHomeLive = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BeanHomeLive>>() {
            }.getType());
            adapterLiveList = new AdapterLiveList(beanHomeLive, activity);
            Rv_HomeLive.setAdapter(adapterLiveList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        adapterLiveList.notifyDataSetChanged();
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        swipeRefreshLayout.setRefreshing(false);
        Rv_HomeLive.setVisibility(View.GONE);
        tv_NoDataAvailable.setVisibility(View.VISIBLE);
        tv_Score_refresh.setVisibility(View.GONE);
        //ShowToast(context,message);

    }


    public class AdapterLiveList extends RecyclerView.Adapter<AdapterLiveList.MyViewHolder> {
        private List<BeanHomeLive> mListenerList;
        Context mContext;

        public AdapterLiveList(List<BeanHomeLive> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_TeamOneName,tv_TeamsName,tv_TimeRemained,tv_TeamTwoName,tv_TeamOneScore,tv_TeamTwoScore,matchType,
                    tv_TeamOneOver,tv_TeamTwoOver,tv_MatchResult;
            LinearLayout linearLayout;
            ImageView im_Team1,im_Team2;


            public MyViewHolder(View view) {
                super(view);

                im_Team1 = view.findViewById(R.id.im_Team1);
                tv_TeamOneName = view.findViewById(R.id.tv_TeamOneName);
              //  tv_TeamsName = view.findViewById(R.id.tv_TeamsName);
                tv_TimeRemained = view.findViewById(R.id.tv_TimeRemained);
                im_Team2 = view.findViewById(R.id.im_Team2);
                tv_TeamTwoName = view.findViewById(R.id.tv_TeamTwoName);
               // tv_TeamOneScore=view.findViewById(R.id.tv_TeamOneScore);
               // tv_TeamTwoScore=view.findViewById(R.id.tv_TeamTwoScore);
              //  tv_TeamOneOver=view.findViewById(R.id.tv_TeamOneOver);
              //  tv_TeamTwoOver=view.findViewById(R.id.tv_TeamTwoOver);
                tv_MatchResult=view.findViewById(R.id.tv_MatchResult);
                matchType=view.findViewById(R.id.matchType);

              //  linearLayout=view.findViewById(R.id.linearlayout2);
               // linearLayout.setVisibility(View.GONE);


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
            // final int time = mListenerList.get(position).getTime();
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
          //  holder.tv_TeamsName.setText(team_short_name1+" vs "+team_short_name2+"\n"+type);
            holder.matchType.setText(type);

            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE+team_image1)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team1);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE+team_image2)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team2);


            if (match_status.equals("Live")){
                holder.tv_TimeRemained.setText("Live");
            }
          //  holder.tv_TeamOneScore.setText("Score:-"+team_one_score);
         //   holder.tv_TeamTwoScore.setText("Score:-"+team_two_score);
           // holder.tv_TeamOneOver.setText("Over:-"+team_one_over);
           // holder.tv_TeamTwoOver.setText("Over:-"+team_two_over);
            holder.tv_MatchResult.setText(match_status_note);

            // 7/02/20 - Disabling click

            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent k = new Intent(activity, MyJoinedLiveContestListActivity.class);
                    k.putExtra("MatchId",match_id);
                    k.putExtra("Time",time+"");
                 //   k.putExtra("TeamsName", holder.tv_TeamsName.getText().toString());
                    k.putExtra("TeamsOneName", team_short_name1);
                    k.putExtra("TeamsTwoName", team_short_name2);
                    k.putExtra("team1Image",Config.TEAMFLAGIMAGE+team_image1);
                    k.putExtra("team2Image",Config.TEAMFLAGIMAGE+team_image2);
                    startActivity(k);
                }
            });*/


        }

    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            //Toast.makeText(context,"Score Is Updated",Toast.LENGTH_SHORT).show();
            // callHomeLive(false);
            // FragmentLive.this.mHandler.postDelayed(m_Runnable, 15000);
        }

    };

}
