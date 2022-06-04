package com.lgt.fxtradingleague.TradingPackage;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.MyTabFragment.FragmentMyFixtures;
import com.lgt.fxtradingleague.MyTabFragment.FragmentMyLive;
import com.lgt.fxtradingleague.MyTabFragment.FragmentMyResults;
import com.lgt.fxtradingleague.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityResultTypeContainer extends AppCompatActivity {
    private TabLayout FragmentMyTab;
    private AppBarLayout appbar;
    ViewPager vp_result_type_view_pager;
    public static String Result_Type = "",Context_Type="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_result_type_container);
        if (getIntent().hasExtra("KEY_RESULT_TYPE")){
            Result_Type = getIntent().getStringExtra("KEY_RESULT_TYPE");
            Context_Type = getIntent().getStringExtra("KEY_CONTEST_TYPE");
            Validations.common_log("RESULT_TYPE: "+Result_Type+","+Context_Type);
        }

        initView();
    }

    private void initView() {
        FragmentMyTab = findViewById(R.id.FragmentMyResultTypeTab);
        vp_result_type_view_pager =  findViewById(R.id.vp_result_type_view_pager);
        FragmentMyTab.setupWithViewPager(vp_result_type_view_pager);

        appbar=findViewById(R.id.appbarResultType);

        appbar.setBackgroundColor(getResources().getColor(R.color.bg_gray));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.vp_result_type_view_pager, new FragmentMyFixtures());
        transaction.commit();
        vp_result_type_view_pager.setOffscreenPageLimit(2);
        setupViewPager(vp_result_type_view_pager);
    }

    private void setupViewPager(ViewPager vp_result_type_view_pager) {
        mResultPagerAdapter mResultPagerAdapter = new mResultPagerAdapter(getSupportFragmentManager());
        mResultPagerAdapter.addFragment(new FragmentMyFixtures(), "Fixtures");
        mResultPagerAdapter.addFragment(new FragmentMyLive(), "Live");
        mResultPagerAdapter.addFragment(new FragmentMyResults(), "Results");
        vp_result_type_view_pager.setAdapter(mResultPagerAdapter);
    }

    class mResultPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public mResultPagerAdapter(FragmentManager manager) {
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
