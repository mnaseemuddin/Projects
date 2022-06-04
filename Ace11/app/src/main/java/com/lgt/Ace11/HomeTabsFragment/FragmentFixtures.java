package com.lgt.Ace11.HomeTabsFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.BeanHomeFixtures;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Model.ModelOffers;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.HOMEFIXTURES;
import static com.lgt.Ace11.Constants.FIXTURESHOMETYPE;



public class FragmentFixtures extends Fragment implements ResponseManager {


    HomeActivity activity;
    Context context;
    RecyclerView Rv_HomeFixtures;
    AdapterFixturesList adapterFixturesList;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    TextView tv_NoDataAvailable;
    SessionManager sessionManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ModelOffers>arrayList;
    RecyclerView rv_offers;
    NestedScrollView fixtureNested;
    static FragmentFixtures fixtureInstance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fixtures, container, false);
        context = activity = (HomeActivity)getActivity();
        initViews(v);
        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);

        fixtureInstance=this;

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                    loadingBanner();
                                    }
                                }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callHomeFixtures(false);

            }
        });

        callHomeFixtures(true);
        arrayList=new ArrayList<>();

        rv_offers=v.findViewById(R.id.rv_offers);


        return v;
    }

    public static FragmentFixtures getFixtureInstance(){
        return fixtureInstance;
    }
    public void loadingBanner() {


        arrayList.add(new ModelOffers("https://d13ir53smqqeyp.cloudfront.net/d11-static-pages/images/champion/D11_Champ1.jpg"));
        arrayList.add(new ModelOffers("https://www.playwiththebest.com/media/bannerslider/s/o/socialmediadiscount10.jpg"));

        com.lgt.Ace11.Adapter.BannerAdapter bannerAdapter = new com.lgt.Ace11.Adapter.BannerAdapter(arrayList, getActivity());
        rv_offers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_offers.setHasFixedSize(true);
        rv_offers.setNestedScrollingEnabled(true);
        rv_offers.setAdapter(bannerAdapter);
    }

    public void initViews(View v) {
        Rv_HomeFixtures = v.findViewById(R.id.Rv_HomeFixtures);
        tv_NoDataAvailable = v.findViewById(R.id.tv_NoDataAvailable);
        fixtureNested= v.findViewById(R.id.fixtureNested);

        Rv_HomeFixtures.setHasFixedSize(true);
        Rv_HomeFixtures.setNestedScrollingEnabled(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        Rv_HomeFixtures.setLayoutManager(mLayoutManager);
        Rv_HomeFixtures.setItemAnimator(new DefaultItemAnimator());





        fixtureNested.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i("counttscroll", "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.i("counttscroll", "Scroll UP");
                }

                if (scrollY == 0) {
                    Log.i("counttscroll", "TOP SCROLL");
                }

            }

        });
    }


    public void callHomeFixtures(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(HOMEFIXTURES,
                    createRequestJson(), context, activity, FIXTURESHOMETYPE,
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
            jsonObject.put("token", FirebaseInstanceId.getInstance().getToken());
            Log.e("tokennnn",sessionManager.getUser(context).getUser_id()+" | "+FirebaseInstanceId.getInstance().getToken()+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        swipeRefreshLayout.setRefreshing(false);
        Rv_HomeFixtures.setVisibility(View.VISIBLE);
        tv_NoDataAvailable.setVisibility(View.GONE);

        try {
            JSONArray jsonArray = result.getJSONArray("data");
            Log.e("uabbbbbFixx",result+"");
            List<BeanHomeFixtures> beanHomeFixtures = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<BeanHomeFixtures>>() {
            }.getType());
            adapterFixturesList = new AdapterFixturesList(beanHomeFixtures, activity);
            Rv_HomeFixtures.setAdapter(adapterFixturesList);

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
        swipeRefreshLayout.setRefreshing(false);
        Rv_HomeFixtures.setVisibility(View.GONE);
        tv_NoDataAvailable.setVisibility(View.VISIBLE);
        //ShowToast(context,message);

    }

    public class AdapterFixturesList extends RecyclerView.Adapter<AdapterFixturesList.MyViewHolder> {
        private List<BeanHomeFixtures> mListenerList;
        Context mContext;


        public AdapterFixturesList(List<BeanHomeFixtures> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_TeamOneName,tv_TeamsName,tv_TimeRemained,tv_TeamTwoName,matchType;
            ImageView im_Team1,im_Team2;
            CountDownTimer countDownTimer;

            public MyViewHolder(View view) {
                super(view);

                im_Team1 = view.findViewById(R.id.im_Team1);
                tv_TeamOneName = view.findViewById(R.id.tv_TeamOneName);
              //  tv_TeamsName = view.findViewById(R.id.tv_TeamsName);
                tv_TimeRemained = view.findViewById(R.id.tv_TimeRemained);
                im_Team2 = view.findViewById(R.id.im_Team2);
                tv_TeamTwoName = view.findViewById(R.id.tv_TeamTwoName);
                matchType=view.findViewById(R.id.matchType);


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
            final String team_name1 = mListenerList.get(position).getTeam_name1();
            final String team_image1 = mListenerList.get(position).getTeam_image1();
            final String team_short_name1 = mListenerList.get(position).getTeam_short_name1();
            final String team_name2 = mListenerList.get(position).getTeam_name2();
            final String team_image2 = mListenerList.get(position).getTeam_image2();
            final String team_short_name2 = mListenerList.get(position).getTeam_short_name2();

            final String unique_id = mListenerList.get(position).getUnique_id();


            String team1=team_short_name1.replaceAll(" ","");
            String team2 =team_short_name2.replaceAll(" ","");

            if (team1.length()>3){
                holder.tv_TeamOneName.setText(team1.substring(0,3));
            }else {
                holder.tv_TeamOneName.setText(team1);
            }

            if (team2.length()>3){
                holder.tv_TeamTwoName.setText(team2.substring(0,3));
            }else {
                holder.tv_TeamTwoName.setText(team2);
            }

        //    holder.tv_TeamsName.setText(team_short_name1+" vs "+team_short_name2+"\n"+type);
            holder.matchType.setText(type);
            holder.matchType.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE+team_image1)

                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team1);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE+team_image2)

                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team2);

        if (match_status.equals("Fixture")){
            holder.tv_TimeRemained.setCompoundDrawablesWithIntrinsicBounds( R.drawable.watch_icon, 0, 0, 0);
            holder.tv_TimeRemained.setText(time+"");
            if (holder.countDownTimer!= null) {
                holder.countDownTimer.cancel();
            }

            try {
                int FlashCount = time;
                long millisUntilFinished = FlashCount * 1000;
                /*
                long Days = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long Hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                long Minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long Seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
                */
                //String format = "%1$02d";
                //holder.tv_TimeRemained.setText(String.format(format, Days) + ":" + String.format(format, Hours) + ":" + String.format(format, Minutes) + ":" + String.format(format, Seconds));
               holder.countDownTimer =  new CountDownTimer(millisUntilFinished, 1000) {

                    public void onTick(long millisUntilFinished) {

                        long Days = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                        long Hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                        long Minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                        long Seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                        String format = "%1$02d";
                        holder.tv_TimeRemained.setText(String.format(format, Days) + ":" + String.format(format, Hours) + ":" + String.format(format, Minutes) + ":" + String.format(format, Seconds));

                       // Log.e("dsfdsfdsfdsfdsfsd",(String.format(format, Days) + ":" + String.format(format, Hours) + ":" + String.format(format, Minutes) + ":" + String.format(format, Seconds)+""));

                    }

                    public void onFinish() {
                        holder.tv_TimeRemained.setText("Entry Over!");
                    }

                }.start();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(holder.tv_TimeRemained.getText().toString().equals("Entry Over!")){
                        ShowToast(context,"Entry Over");
                    }
                    else {
                        Intent k = new Intent(activity, ContestListActivity.class);
                        k.putExtra("MatchId", match_id);
                        k.putExtra("Time", time + "");
                        k.putExtra("UNIQUE_ID", unique_id + "");
                   //     k.putExtra("TeamsName", holder.tv_TeamsName.getText().toString());
                        k.putExtra("TeamsOneName", team_short_name1);
                        k.putExtra("TeamsTwoName", team_short_name2);
                        k.putExtra("team1Image",Config.TEAMFLAGIMAGE+team_image1);
                        k.putExtra("team2Image",Config.TEAMFLAGIMAGE+team_image2);
                        startActivity(k);
                    }
                }
            });
        }

        }

    }

}
