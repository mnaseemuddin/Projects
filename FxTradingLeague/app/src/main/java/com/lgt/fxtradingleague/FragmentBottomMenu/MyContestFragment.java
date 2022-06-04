package com.lgt.fxtradingleague.FragmentBottomMenu;


import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgt.fxtradingleague.MyTabFragment.FragmentContestType;
import com.lgt.fxtradingleague.MyTabFragment.FragmentMyFixtures;
import com.lgt.fxtradingleague.R;

import java.util.ArrayList;
import java.util.List;

import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_CRYPTO_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_INDI_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_WORLD_LEAGUE;

public class MyContestFragment extends Fragment {


    public static ViewPager myViewContestPager;
    private TabLayout FragmentMyTab;
    private AppBarLayout appbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_contest, container, false);
        myViewContestPager = v.findViewById(R.id.myContestViewPager);
        setupViewPager(myViewContestPager);

        FragmentMyTab = v.findViewById(R.id.FragmentMyTab);
        FragmentMyTab.setupWithViewPager(myViewContestPager);

        appbar = v.findViewById(R.id.appbar);

        appbar.setBackgroundColor(getResources().getColor(R.color.bg_gray));

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myContestViewPager, new FragmentMyFixtures());
        transaction.commit();
        myViewContestPager.setOffscreenPageLimit(2);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentContestType(), KEY_CRYPTO_LEAGUE);
        adapter.addFragment(new FragmentContestType(), KEY_WORLD_LEAGUE);
        adapter.addFragment(new FragmentContestType(), KEY_INDI_LEAGUE);
        viewPager.setAdapter(adapter);
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public MyViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
