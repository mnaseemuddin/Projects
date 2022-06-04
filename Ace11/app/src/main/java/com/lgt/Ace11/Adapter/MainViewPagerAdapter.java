package com.lgt.Ace11.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.lgt.Ace11.FragmentBottomMenu.HomeNewLayoutFragment;
import com.lgt.Ace11.FragmentBottomMenu.MyContestFragment;
import com.lgt.Ace11.FragmentBottomMenu.ProfileFragment;
 
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeNewLayoutFragment();
            case 1:
                return new MyContestFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new HomeNewLayoutFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
