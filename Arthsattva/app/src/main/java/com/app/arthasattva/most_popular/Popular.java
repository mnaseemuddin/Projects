package com.app.arthasattva.most_popular;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.app.arthasattva.LiveShopping.Fragment.LiveShoppingFragment;
import com.app.arthasattva.R;
import com.app.arthasattva.activity.AllNotificationActivity;
import com.app.arthasattva.databinding.FragPopularBinding;
import com.app.arthasattva.fragment.home.PopularHotFragment;
import com.app.arthasattva.fragment.stream.AllPrivateStreams;

import java.util.ArrayList;
import java.util.List;


public class Popular extends Fragment {
    private FragPopularBinding binding;

    public Popular() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_popular, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewPager(binding.viewpager);

        binding.FragmentTab.setupWithViewPager(binding.viewpager);

        binding.FragmentTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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


        handleClick();
    }

    private void handleClick() {
        binding.ivNotification.setOnClickListener(view ->
                startActivity(new Intent(binding.getRoot().getContext(), AllNotificationActivity.class)));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PopularHotFragment(), "Live");
        adapter.addFragment(new AllPrivateStreams(), "Private Live");
        adapter.addFragment(new LiveShoppingFragment(), "Live Shopping");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
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