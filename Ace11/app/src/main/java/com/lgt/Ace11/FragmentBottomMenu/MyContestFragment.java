package com.lgt.Ace11.FragmentBottomMenu;


import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgt.Ace11.MyTabFragment.FragmentMyFixtures;
import com.lgt.Ace11.MyTabFragment.FragmentMyLive;
import com.lgt.Ace11.MyTabFragment.FragmentMyResults;
import com.lgt.Ace11.R;

import java.util.ArrayList;
import java.util.List;

public class MyContestFragment extends Fragment {

    private TabLayout FragmentMyTab;
    private ViewPager myviewpager;
    private AppBarLayout appbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_my_contest, container, false);

        myviewpager = v.findViewById(R.id.myviewpager);
        setupViewPager(myviewpager);

        FragmentMyTab = v.findViewById(R.id.FragmentMyTab);
        FragmentMyTab.setupWithViewPager(myviewpager);

        appbar=v.findViewById(R.id.appbar);

        appbar.setBackgroundColor(getResources().getColor(R.color.bg_gray));

       /* FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
       FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myviewpager, new FragmentMyFixtures());
        transaction.commit();*/
        myviewpager.setOffscreenPageLimit(2);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentMyFixtures(), "FIXTURES");
        adapter.addFragment(new FragmentMyLive(), "LIVE");
        adapter.addFragment(new FragmentMyResults(), "RESULTS");
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
