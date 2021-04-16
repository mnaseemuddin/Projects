package com.lgt.NeWay.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lgt.NeWay.Fragment.BatchListFragment;
import com.lgt.NeWay.Fragment.DashBoardFragment;
import com.lgt.NeWay.Fragment.JoinBatchFragment;
import com.lgt.NeWay.Fragment.MoreFragment;
import com.lgt.NeWay.Neway.R;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView btmNavigation;

    private Fragment mSelectedFragment = null;
    private boolean shouldAddToBackStack;
    private FragmentManager mFragmentManager;
    private String mBackStackName, tagName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btmNavigation = findViewById(R.id.btmNavHomeScreen);
        btmNavigation.setOnNavigationItemSelectedListener(btmListener);

        mFragmentManager = getSupportFragmentManager();
        ///open data on 0 position
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeScreen,
                    new DashBoardFragment()).commit();
        }


        registerReceiver(broadcastReceiver, new IntentFilter("start.fragment.action"));


        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
            }
        });
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OpenBtachList();
        }
    };


    BottomNavigationView.OnNavigationItemSelectedListener btmListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.navigation_home:
                    mSelectedFragment = new DashBoardFragment();
                    mBackStackName = "FragmentHomeScreen";
                    tagName = "FragmentHomeScreen";
                    replaceFragment(mSelectedFragment, shouldAddToBackStack, mBackStackName, tagName);
                    return true;


                case R.id.navigation_Batch_List:
                    mSelectedFragment = new BatchListFragment();
                    mBackStackName = "FragmentJobs";
                    tagName = "FragmentJobs";
                    replaceFragment(mSelectedFragment, shouldAddToBackStack, mBackStackName, tagName);
                    return true;

                case R.id.navigation_Join_Batches:
                    mSelectedFragment = new JoinBatchFragment();
                    mBackStackName = "FragmentMore";
                    tagName = "FragmentMore";
                    replaceFragment(mSelectedFragment, shouldAddToBackStack, mBackStackName, tagName);
                    return true;

                case R.id.navigation_more:
                    mSelectedFragment = new MoreFragment();
                    mBackStackName = "FragmentSpecialCourses";
                    tagName = "FragmentSpecialCourses";
                    replaceFragment(mSelectedFragment, shouldAddToBackStack, mBackStackName, tagName);
                    return true;
            }

            return false;
        }


    };


    public void OpenBtachList() {
        mSelectedFragment = new BatchListFragment();
        mBackStackName = "FragmentJobs";
        tagName = "FragmentJobs";
        replaceFragment(mSelectedFragment, shouldAddToBackStack, mBackStackName, tagName);
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed() {

        if (mFragmentManager.getBackStackEntryCount() > 0) {
            for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
                mFragmentManager.popBackStack();
                btmNavigation.getMenu().getItem(0).setChecked(true);
            }
        } else {
            finish();
            btmNavigation.getMenu().getItem(0).setChecked(true);
        }
    }

    private void replaceFragment(Fragment mSelectedFragment, boolean shouldAddToBackStack, String mBackStackName, String tagName) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeScreen, mSelectedFragment, tagName).addToBackStack(mBackStackName).commit();
    }
}