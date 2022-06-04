package com.lgt.Ace11.MyTabFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.BeanMyFixtures;
import com.lgt.Ace11.Config;
import com.lgt.Ace11.ContestListActivity;
import com.lgt.Ace11.HomeActivity;
import com.lgt.Ace11.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lgt.Ace11.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.lgt.Ace11.Config.MYFIXTURES;
import static com.lgt.Ace11.Constants.MYFIXTURESTYPE;


public class FragmentMyFixtures extends Fragment implements ResponseManager {


    HomeActivity activity;
    Context context;
    RecyclerView Rv_MyFixtures;
    AdapterFixturesList adapterFixturesList;

    SessionManager sessionManager;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_NoDataAvailable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_fixtures, container, false);
        context = activity = (HomeActivity)getActivity();
        sessionManager=new SessionManager();
        initViews(v);

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        Rv_MyFixtures.setHasFixedSize(true);
        Rv_MyFixtures.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        Rv_MyFixtures.setLayoutManager(mLayoutManager);
        Rv_MyFixtures.setItemAnimator(new DefaultItemAnimator());



        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(true);
                                    callMyFixtures(false);
                                }
                            }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callMyFixtures(false);
            }
        });


        return v;
    }

    public void initViews(View v){
        Rv_MyFixtures = v.findViewById(R.id.Rv_MyFixtures);
        tv_NoDataAvailable = v.findViewById(R.id.tv_NoDataAvailable);


    }


    private void callMyFixtures(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(MYFIXTURES,
                    createRequestJson(), context, activity, MYFIXTURESTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "Fixture");
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            try {
                Log.e("userreid",sessionManager.getUser(context).getUser_id());
            }catch (Exception e){e.printStackTrace();}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        swipeRefreshLayout.setRefreshing(false);
        tv_NoDataAvailable.setVisibility(View.GONE);
        Rv_MyFixtures.setVisibility(View.VISIBLE);

        try {
            JSONArray jsonArray = result.getJSONArray("data");
            Log.e("myFixturesress",result+"");
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("data_match",""+jsonObject.getString("match_id"));

            }
            List<BeanMyFixtures> beanHomeFixtures = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<BeanMyFixtures>>() {
            }.getType());

            adapterFixturesList = new AdapterFixturesList(beanHomeFixtures, activity);
            Rv_MyFixtures.setAdapter(adapterFixturesList);

        }
        catch (Exception e){
            e.printStackTrace();
        }

       adapterFixturesList.notifyDataSetChanged();

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        //ShowToast(context,message);
        Log.e("errrere",message+"");
        swipeRefreshLayout.setRefreshing(false);
        tv_NoDataAvailable.setVisibility(View.VISIBLE);
        Rv_MyFixtures.setVisibility(View.GONE);

    }

    public class AdapterFixturesList extends RecyclerView.Adapter<AdapterFixturesList.MyViewHolder> {
        private List<BeanMyFixtures> mListenerList;
        Context mContext;


        public AdapterFixturesList(List<BeanMyFixtures> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_TeamOneName, tv_TeamsName, tv_TimeRemained, tv_TeamTwoName,tv_JoinedContestCount,matchType;
            LinearLayout linearLayout;
            ImageView im_Team1, im_Team2;
            CountDownTimer countDownTimer;

            public MyViewHolder(View view) {
                super(view);

                im_Team1 = view.findViewById(R.id.im_Team1);
                tv_TeamOneName = view.findViewById(R.id.tv_TeamOneName);
            //    tv_TeamsName = view.findViewById(R.id.tv_TeamsName);
                tv_TimeRemained = view.findViewById(R.id.tv_TimeRemained);
                im_Team2 = view.findViewById(R.id.im_Team2);
                tv_TeamTwoName = view.findViewById(R.id.tv_TeamTwoName);
                tv_JoinedContestCount = view.findViewById(R.id.tv_JoinedContestCount);
                matchType=view.findViewById(R.id.matchType);

                // linearLayout=view.findViewById(R.id.linearlayout2);
                //linearLayout.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_my_match_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final String match_id = mListenerList.get(position).getMatch_id();
            String teamid1 = mListenerList.get(position).getTeamid1();
            String match_status = mListenerList.get(position).getMatch_status();
            final String type = mListenerList.get(position).getType();
            final int time = mListenerList.get(position).getTime();
            String teamid2 = mListenerList.get(position).getTeamid2();
            String team_name1 = mListenerList.get(position).getTeam_name1();
            String team_image1 = mListenerList.get(position).getTeam_image1();
            final String team_short_name1 = mListenerList.get(position).getTeam_short_name1();
            String team_name2 = mListenerList.get(position).getTeam_name2();
            String team_image2 = mListenerList.get(position).getTeam_image2();
            final String team_short_name2 = mListenerList.get(position).getTeam_short_name2();
            String contest_count = mListenerList.get(position).getContest_count();

            holder.matchType.setText(type);

            holder.tv_JoinedContestCount.setText(contest_count+" Contest Joined");

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
           // holder.tv_TeamsName.setText(team_short_name1 + " vs " + team_short_name2 + "\n" + type);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE + team_image1)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team1);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE + team_image2)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team2);

            if (match_status.equals("Fixture")) {
                holder.tv_TimeRemained.setCompoundDrawablesWithIntrinsicBounds(R.drawable.watch_icon, 0, 0, 0);
                holder.tv_TimeRemained.setText(time + "");


                if (holder.countDownTimer!= null) {
                    holder.countDownTimer.cancel();
                }

                try {

                    int FlashCount = time;
                    long FlashMiliSec = FlashCount * 1000;

                    holder.countDownTimer =    new CountDownTimer(FlashMiliSec, 1000) {

                        public void onTick(long millisUntilFinished) {

                            long Days = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                            long Hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                            long Minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                            long Seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                            String format = "%1$02d";
                            holder.tv_TimeRemained.setText(String.format(format, Days) + ":" + String.format(format, Hours) + ":" + String.format(format, Minutes) + ":" + String.format(format, Seconds));

                        }

                        public void onFinish() {
                            holder.tv_TimeRemained.setText("Entry Over!");
                        }

                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent k = new Intent(activity, MyJoinedFixtureContestListActivity.class);
                        k.putExtra("MatchId",match_id);
                        k.putExtra("Time",time+"");
                        ContestListActivity.IntentTime=String.valueOf(time);
                        k.putExtra("TeamsName",   team_short_name1 + " vs " + team_short_name2 + "\n" + type);

                      //  k.putExtra("TeamsName", holder.tv_TeamsName.getText().toString());
                        ContestListActivity.IntenTeamsName=team_short_name1 + " vs " + team_short_name2 + "\n" + type;
                      //  ContestListActivity.IntenTeamsName= holder.tv_TeamsName.getText().toString();
                        k.putExtra("TeamsOneName", team_short_name1);
                        ContestListActivity.IntentTeamOneName= team_short_name1;
                        k.putExtra("TeamsTwoName", team_short_name2);
                        k.putExtra("team1Image",mListenerList.get(position).getTeam_image1());
                        k.putExtra("team2Image",mListenerList.get(position).getTeam_image2());

                        ContestListActivity.IntentTeamTwoName= team_short_name2;
                        startActivity(k);
                    }
                });





            }

        }
    }

}
