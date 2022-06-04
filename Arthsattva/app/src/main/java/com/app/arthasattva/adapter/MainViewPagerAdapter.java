package com.app.arthasattva.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.arthasattva.fragment.AllPostFragment;
import com.app.arthasattva.fragment.Explore;
import com.app.arthasattva.fragment.MyProfile;
import com.app.arthasattva.most_popular.Popular;


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
