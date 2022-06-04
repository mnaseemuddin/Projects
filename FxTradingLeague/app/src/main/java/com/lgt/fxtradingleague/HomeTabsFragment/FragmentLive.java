package com.lgt.fxtradingleague.HomeTabsFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.TradingPackage.JoinPackageActivity;


public class FragmentLive extends Fragment{
    String CURRENCY_TYPE="NASDAQ";
    CardView cv_Nasdaq_monthly_contest,cv_Nasdaq_weekly_contest,cv_Nasdaq_daily_contest;
    HomeActivity activity;
    Context context;
    RecyclerView Rv_HomeLive;
    APIRequestManager apiRequestManager;
     TextView tv_NoDataAvailable,tv_Score_refresh;
    Handler mHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_live, container, false);
        context = activity = (HomeActivity)getActivity();
        initViews(v);
        apiRequestManager = new APIRequestManager(activity);
        Rv_HomeLive.setHasFixedSize(true);
        Rv_HomeLive.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        Rv_HomeLive.setLayoutManager(mLayoutManager);
        Rv_HomeLive.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    public void initViews(View v){
        Rv_HomeLive = v.findViewById(R.id.Rv_HomeLive);
        cv_Nasdaq_daily_contest = v.findViewById(R.id.cv_Nasdaq_daily_contest);
        cv_Nasdaq_weekly_contest = v.findViewById(R.id.cv_Nasdaq_weekly_contest);
        cv_Nasdaq_monthly_contest = v.findViewById(R.id.cv_Nasdaq_monthly_contest);
        tv_NoDataAvailable = v.findViewById(R.id.tv_NoDataAvailable);
        tv_Score_refresh=v.findViewById(R.id.tv_Score_refresh);

        cv_Nasdaq_daily_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent daily_contest = new Intent(getActivity(), JoinPackageActivity.class);
                daily_contest.putExtra("contest_type","Daily");
                daily_contest.putExtra("currency_type",CURRENCY_TYPE);
                startActivity(daily_contest);
            }
        });

        cv_Nasdaq_weekly_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weekly_contest = new Intent(getActivity(), JoinPackageActivity.class);
                weekly_contest.putExtra("contest_type","Weekly");
                weekly_contest.putExtra("currency_type",CURRENCY_TYPE);
                startActivity(weekly_contest);
            }
        });

        cv_Nasdaq_monthly_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monthly_contest = new Intent(getActivity(), JoinPackageActivity.class);
                monthly_contest.putExtra("contest_type","Monthly");
                monthly_contest.putExtra("currency_type",CURRENCY_TYPE);
                startActivity(monthly_contest);
            }
        });
    }

}
