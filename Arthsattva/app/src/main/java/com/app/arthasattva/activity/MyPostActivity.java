package com.app.arthasattva.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.arthasattva.R;
import com.app.arthasattva.fragment.home.NearbyUsers;
import com.app.arthasattva.most_popular.Multiguest;
import com.app.arthasattva.most_popular.PKLive;
import com.app.arthasattva.utils.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MyPostActivity extends BaseActivity {
    @Override
    protected int setLayout() {
        return R.layout.activity_my_post;
    }
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onLaunch() {
        viewPager = find(R.id.viewpager);
        //  VP_Slider = view.findViewById(R.id.VP_Slider);
        // sliderDotspanel = view.findViewById(R.id.SliderDots);
        setupViewPager(viewPager);

        tabLayout = find(R.id.FragmentTab);
        tabLayout.setupWithViewPager(viewPager);

        // tabLayout.setVisibility(View.GONE);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NearbyUsers(), "Nearby");
        adapter.addFragment(new Multiguest(),"Multi Guest");
        adapter.addFragment(new PKLive(),"PK Live");

        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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
