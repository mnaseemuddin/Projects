package com.lgt.fxtradingleague.MyTabFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.TradingPackage.ActivityResultTypeContainer;

import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_CRYPTO_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_INDI_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_WORLD_LEAGUE;
import static com.lgt.fxtradingleague.FragmentBottomMenu.MyContestFragment.myViewContestPager;

public class FragmentContestType extends Fragment {

    CardView cv_contest_daily_contest, cv_contest_weekly_contest, cv_contest_monthly_contest;
    String ContestType = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.layout_contest_type, container, false);
        // init view by id
        initView(mView);
        return mView;
    }

    private void initView(View mView) {
        cv_contest_daily_contest = mView.findViewById(R.id.cv_contest_daily_contest);
        cv_contest_weekly_contest = mView.findViewById(R.id.cv_contest_weekly_contest);
        cv_contest_monthly_contest = mView.findViewById(R.id.cv_contest_monthly_contest);

        clickEventforMyContest();
    }

    private void clickEventforMyContest() {
        cv_contest_daily_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContestType = "daily";
                if (myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()).toString().equalsIgnoreCase(KEY_WORLD_LEAGUE)) {
                    Intent inSideData = new Intent(getActivity(), ActivityResultTypeContainer.class);
                    inSideData.putExtra("KEY_RESULT_TYPE",myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()));
                    inSideData.putExtra("KEY_CONTEST_TYPE",ContestType);
                    startActivity(inSideData);
                } else if (myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()).toString().equalsIgnoreCase(KEY_INDI_LEAGUE)) {
                    Intent inSideData = new Intent(getActivity(), ActivityResultTypeContainer.class);
                    inSideData.putExtra("KEY_RESULT_TYPE",myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()));
                    inSideData.putExtra("KEY_CONTEST_TYPE",ContestType);
                    startActivity(inSideData);
                } else if (myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()).toString().equalsIgnoreCase(KEY_CRYPTO_LEAGUE)) {
                    Intent inSideData = new Intent(getActivity(), ActivityResultTypeContainer.class);
                    inSideData.putExtra("KEY_RESULT_TYPE",myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()));
                    inSideData.putExtra("KEY_CONTEST_TYPE",ContestType);
                    startActivity(inSideData);
                }
            }
        });
        cv_contest_weekly_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContestType = "weekly";
                if (myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()).toString().equalsIgnoreCase(KEY_WORLD_LEAGUE)) {
                    Intent inSideData = new Intent(getActivity(), ActivityResultTypeContainer.class);
                    inSideData.putExtra("KEY_RESULT_TYPE",myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()));
                    inSideData.putExtra("KEY_CONTEST_TYPE",ContestType);
                    startActivity(inSideData);
                } else if (myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()).toString().equalsIgnoreCase(KEY_INDI_LEAGUE)) {
                    Intent inSideData = new Intent(getActivity(), ActivityResultTypeContainer.class);
                    inSideData.putExtra("KEY_RESULT_TYPE",myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()));
                    inSideData.putExtra("KEY_CONTEST_TYPE",ContestType);
                    startActivity(inSideData);
                } else if (myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()).toString().equalsIgnoreCase(KEY_CRYPTO_LEAGUE)) {
                    Intent inSideData = new Intent(getActivity(), ActivityResultTypeContainer.class);
                    inSideData.putExtra("KEY_RESULT_TYPE",myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()));
                    inSideData.putExtra("KEY_CONTEST_TYPE",ContestType);
                    startActivity(inSideData);
                }
            }
        });
        cv_contest_monthly_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContestType = "monthly";
                if (myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()).toString().equalsIgnoreCase(KEY_WORLD_LEAGUE)) {
                    Intent inSideData = new Intent(getActivity(), ActivityResultTypeContainer.class);
                    inSideData.putExtra("KEY_RESULT_TYPE",myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()));
                    inSideData.putExtra("KEY_CONTEST_TYPE",ContestType);
                    startActivity(inSideData);
                } else if (myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()).toString().equalsIgnoreCase(KEY_INDI_LEAGUE)) {
                    Intent inSideData = new Intent(getActivity(), ActivityResultTypeContainer.class);
                    inSideData.putExtra("KEY_RESULT_TYPE",myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()));
                    inSideData.putExtra("KEY_CONTEST_TYPE",ContestType);
                    startActivity(inSideData);
                } else if (myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()).toString().equalsIgnoreCase(KEY_CRYPTO_LEAGUE)) {
                    Intent inSideData = new Intent(getActivity(), ActivityResultTypeContainer.class);
                    inSideData.putExtra("KEY_RESULT_TYPE",myViewContestPager.getAdapter().getPageTitle(myViewContestPager.getCurrentItem()));
                    inSideData.putExtra("KEY_CONTEST_TYPE",ContestType);
                    startActivity(inSideData);
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
