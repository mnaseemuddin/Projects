package com.lgt.fxtradingleague.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lgt.fxtradingleague.FragmentBottomMenu.HomeFragment;
import com.lgt.fxtradingleague.TradingPackage.MyContentFragment;

public class SectionsPagerAdapter  extends FragmentPagerAdapter {

    Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment homeFragment=new HomeFragment();
                return homeFragment;
            case 1:

                MyContentFragment myContestFragment = new MyContentFragment();
              //  Log.e("hhhhhh",myContestFragment+"");
                return myContestFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Matches";
            case 1:
                return "My Matches";

            default:
                return null;
        }
    }
}
