package com.lgt.Ace11.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lgt.Ace11.FragmentBottomMenu.HomeFragment;
import com.lgt.Ace11.FragmentBottomMenu.MyContestFragment;

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


                MyContestFragment myContestFragment = new MyContestFragment();
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
