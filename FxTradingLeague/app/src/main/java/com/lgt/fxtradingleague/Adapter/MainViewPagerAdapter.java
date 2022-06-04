package com.lgt.fxtradingleague.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lgt.fxtradingleague.FragmentBottomMenu.HomeFragment;
import com.lgt.fxtradingleague.FragmentBottomMenu.MoreFragment;
import com.lgt.fxtradingleague.FragmentBottomMenu.MyContestFragment;
import com.lgt.fxtradingleague.FragmentBottomMenu.NewHomeFragment;
import com.lgt.fxtradingleague.FragmentBottomMenu.ProfileFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewHomeFragment();
            case 1:
                return new HomeFragment();
            case 2:
                return new MyContestFragment();
            case 3:
                return new ProfileFragment();
            case 4:
                return new MoreFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
