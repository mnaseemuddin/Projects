package com.lgt.fxtradingleague.HomeTabsFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;
import com.lgt.fxtradingleague.TradingPackage.JoinPackageActivity;

import java.util.ArrayList;

import Model.ModelOffers;

import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_CURRENCY_TYPE;


public class FragmentFixtures extends Fragment {

    String TypeOfContest = "",CurrencyType="";
    HomeActivity activity;
    Context context;
    CardView cv_daily_world_league_contest,cv_weekly_world_league_contest,cv_monthly_world_league_contest;
    APIRequestManager apiRequestManager;
    SessionManager sessionManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ModelOffers> arrayList;
    NestedScrollView fixtureNested;
    static FragmentFixtures fixtureInstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fixtures, container, false);
        context = activity = (HomeActivity) getActivity();
        initViews(v);
        sessionManager = new SessionManager();
        apiRequestManager = new APIRequestManager(activity);
        fixtureInstance = this;

        // get data from intent
        /*if (savedInstanceState.getString("EventType") != null){
            EventType=savedInstanceState.getString("EventType");
        }*/
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        arrayList = new ArrayList<>();
        return v;
    }

    public static FragmentFixtures getFixtureInstance() {
        return fixtureInstance;
    }

    public void initViews(View v) {
        cv_daily_world_league_contest = v.findViewById(R.id.cv_daily_world_league_contest);
        cv_weekly_world_league_contest = v.findViewById(R.id.cv_weekly_world_league_contest);
        cv_monthly_world_league_contest = v.findViewById(R.id.cv_monthly_world_league_contest);
        clickEvent();
    }

    private void clickEvent() {
        cv_daily_world_league_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TypeOfContest="Daily";
                CurrencyType = "WorldLeague";
                Intent world_league = new Intent(getActivity(), JoinPackageActivity.class);
                world_league.putExtra(KEYS_CONTEST_TYPE,TypeOfContest);
                world_league.putExtra(KEY_CURRENCY_TYPE,CurrencyType);
                startActivity(world_league);
            }
        });
        cv_weekly_world_league_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TypeOfContest="Weekly";
                CurrencyType = "WorldLeague";
                Intent world_league = new Intent(getActivity(), JoinPackageActivity.class);
                world_league.putExtra(KEYS_CONTEST_TYPE,TypeOfContest);
                world_league.putExtra(KEY_CURRENCY_TYPE,CurrencyType);
                startActivity(world_league);
            }
        });
        cv_monthly_world_league_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TypeOfContest="Monthly";
                CurrencyType = "WorldLeague";
                Intent world_league = new Intent(getActivity(), JoinPackageActivity.class);
                world_league.putExtra(KEYS_CONTEST_TYPE,TypeOfContest);
                world_league.putExtra(KEY_CURRENCY_TYPE,CurrencyType);
                startActivity(world_league);
            }
        });
    }


}
