package com.app.arthasattva.go_live_module;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.app.arthasattva.LiveShopping.Fragment.GoLiveShppingFragment;
import com.app.arthasattva.R;


import com.app.arthasattva.databinding.ActivityGoLiveBinding;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Facing;

import java.util.ArrayList;
import java.util.List;


public class GoLiveActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    public static CameraView camera;
    private GoLiveActivity activity;
    private Context context;
    private ActivityGoLiveBinding binding;
    private String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_go_live);
        context = activity = this;
        setViewPagerSetup();
        handleClick();
        initCameraViews();
    }
    private void handleClick() {
        binding.ivClose.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    private void setViewPagerSetup() {
        setupViewPager(binding.viewpager);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        binding.viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initCameraViews() {
        camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);

        camera.setFacing(Facing.FRONT);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onResume() {
        super.onResume();

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GoSingleLiveFragment(), "Public");
        adapter.addFragment(new GoPrivateLive(), "Private");
        adapter.addFragment(new GoLiveShppingFragment(), "Live Shopping");
        viewPager.setAdapter(adapter);
    }




    class ViewPagerAdapter extends FragmentPagerAdapter {
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








