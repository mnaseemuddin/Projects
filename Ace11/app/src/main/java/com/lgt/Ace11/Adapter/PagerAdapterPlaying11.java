package com.lgt.Ace11.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lgt.FragmentTeamB;
import com.lgt.Ace11.FragmentTeamA;

/**
 * Created by Ranjan on 2/4/2020.
 */
public class PagerAdapterPlaying11  extends FragmentPagerAdapter {

    public int numOfTabs;

    public PagerAdapterPlaying11(@NonNull FragmentManager fm,int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentTeamA();
            case 1:
                return new FragmentTeamB();

        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
