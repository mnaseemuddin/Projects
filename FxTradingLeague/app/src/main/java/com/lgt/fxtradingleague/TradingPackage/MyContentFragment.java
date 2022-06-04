package com.lgt.fxtradingleague.TradingPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.lgt.fxtradingleague.FragmentBottomMenu.MyContestFragment;
import com.lgt.fxtradingleague.R;

public class MyContentFragment extends Fragment {
    public static String EventType = "";
    CardView cv_mc_daily_contest,cv_mc_weekly_contest,cv_mc_monthly_contest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_content_fragment_layout, container, false);

        initView(v);

        return v;
    }

    private void initView(View v) {
        cv_mc_daily_contest=v.findViewById(R.id.cv_mc_daily_contest);
        cv_mc_weekly_contest=v.findViewById(R.id.cv_mc_weekly_contest);
        cv_mc_monthly_contest=v.findViewById(R.id.cv_mc_monthly_contest);
        cv_mc_daily_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventType="Daily";
                startFragmentContentView(view,EventType);
            }
        });
        cv_mc_weekly_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventType="Weekly";
                startFragmentContentView(view,EventType);
            }
        });
        cv_mc_monthly_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventType="Monthly";
                startFragmentContentView(view,EventType);
            }
        });
    }

    private void startFragmentContentView(View vi,String type) {
        AppCompatActivity activity = (AppCompatActivity) vi.getContext();
        MyContestFragment myFragment = new MyContestFragment();
        Bundle bundle=new Bundle();
        bundle.putString("EventType", type);
        myFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
    }
}
