package com.app.cryptok.most_popular;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.LiveShopping.Fragment.LiveShoppingFragment;
import com.app.cryptok.R;
import com.app.cryptok.activity.AllNotificationActivity;
import com.app.cryptok.databinding.FragPopularBinding;
import com.app.cryptok.fragment.home.PopularHotFragment;
import com.app.cryptok.fragment.stream.AllPrivateStreams;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;


public class Popular extends Fragment {

    private FragPopularBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;

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
        binding.viewpager.setOffscreenPageLimit(2);
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
       // getUnreadNotification();
    }

    private void getUnreadNotification() {
        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (profilePOJO == null) {
            return;
        }
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .collection(DBConstants.all_notifications)
                .whereEqualTo(DBConstants.seen, false)
                .addSnapshotListener((value, error) -> {
                    if (value == null) {
                        binding.tvUnreadNotificationCount.setVisibility(View.GONE);
                        return;
                    }
                    if (!value.isEmpty()) {
                        binding.tvUnreadNotificationCount.setVisibility(View.VISIBLE);
                        binding.tvUnreadNotificationCount.setText(String.valueOf(value.size()));
                    } else {
                        binding.tvUnreadNotificationCount.setVisibility(View.GONE);
                    }
                });
    }

    private void handleClick() {

        binding.ivNotification.setOnClickListener(view ->
                startActivity(new Intent(binding.getRoot().getContext(), AllNotificationActivity.class)));

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        // adapter.addFragment(new NearbyUsers(), "Nearby");
        adapter.addFragment(new PopularHotFragment(), "Live");
        adapter.addFragment(new AllPrivateStreams(), "Private Live");
        adapter.addFragment(new LiveShoppingFragment(), "Live Shopping");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
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