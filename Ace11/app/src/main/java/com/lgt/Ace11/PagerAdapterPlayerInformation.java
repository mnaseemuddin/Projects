package com.lgt.Ace11;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import Model.ModelPlayerInformation;

/**
 * Created by Ranjan on 1/30/2020.
 */
class PagerAdapterPlayerInformation extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public static int currentTab;


    private List<ModelPlayerInformation> list;

    public PagerAdapterPlayerInformation(@NonNull FragmentManager fm, int mNumOfTabs, List<ModelPlayerInformation> list, int currentTab) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
        this.list = list;
        this.currentTab = currentTab;
        Log.e("dsarerewrwe",currentTab+"");
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.e("sdarerwer",position+"");
        return FragmentPlayerInfo.newInstance(position, list, currentTab);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
