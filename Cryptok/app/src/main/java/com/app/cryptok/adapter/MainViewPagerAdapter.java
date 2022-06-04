package com.app.cryptok.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.cryptok.fragment.AllPostFragment;
import com.app.cryptok.fragment.Explore;
import com.app.cryptok.fragment.MyProfile;
import com.app.cryptok.most_popular.Popular;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Popular();
            case 1:
                return new Explore();
            case 2:
                return new AllPostFragment();
            default:
                return new MyProfile();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
