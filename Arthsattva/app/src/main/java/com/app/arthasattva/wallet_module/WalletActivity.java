package com.app.arthasattva.wallet_module;

import androidx.viewpager.widget.ViewPager;

import com.app.arthasattva.R;
import com.app.arthasattva.adapter.FragmentAdapter;
import com.app.arthasattva.utils.BaseActivity;
import com.google.android.material.tabs.TabLayout;

public class WalletActivity extends BaseActivity {
    @Override
    protected int setLayout() {
        return R.layout.activity_wallet;
    }
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onLaunch() {
        setBackWithToolBar("Wallet");
        tabLayout=find(R.id.tabLayout);
        viewPager=find(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new DiamondFragment(), "Diamonds");
        adapter.addFragment(new BeansFragment(), "Beans");
        //adapter.addFragment(new CoinFragment(),"Coins");
       // adapter.addFragment(new PKLive(),"PK Live");

        viewPager.setAdapter(adapter);
    }
}
