package com.lgt.fxtradingleague.FragmentBottomMenu;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Bean.BeanBanner;
import com.lgt.fxtradingleague.Config;
import com.lgt.fxtradingleague.HomeTabsFragment.FragmentFixtures;
import com.lgt.fxtradingleague.HomeTabsFragment.FragmentLive;
import com.lgt.fxtradingleague.HomeTabsFragment.FragmentResults;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.lgt.fxtradingleague.Config.HOMEBANNER;
import static com.lgt.fxtradingleague.Constants.HOMEBANNERTYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_CRYPTO_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_INDI_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_WORLD_LEAGUE;


public class HomeFragment extends Fragment implements ResponseManager {


    private TabLayout tabLayout;
    private ViewPager viewPager;

    ViewPager VP_Slider;
    LinearLayout sliderDotspanel;
    private int dotscount;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    SessionManager sessionManager;
    CollapsingToolbarLayout collapse_toolbar;
    AppBarLayout appbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        responseManager = this;
        apiRequestManager = new APIRequestManager(getActivity());
        sessionManager = new SessionManager();

        appbar = v.findViewById(R.id.appbar);

        appbar.setBackgroundColor(getResources().getColor(R.color.bg_gray));


        viewPager = v.findViewById(R.id.homepager);
        VP_Slider = v.findViewById(R.id.VP_Slider);
        sliderDotspanel = v.findViewById(R.id.SliderDots);
        collapse_toolbar = v.findViewById(R.id.collapse_toolbar);
        setupViewPager();

        tabLayout = v.findViewById(R.id.FragmentTab);
        tabLayout.setupWithViewPager(viewPager);

        // tabLayout.setVisibility(View.GONE);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.homepager, new FragmentFixtures());
        transaction.commit();
        viewPager.setOffscreenPageLimit(2);
        return v;
    }

    private void setupViewPager( ) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentResults(), KEY_CRYPTO_LEAGUE); //LEAGUE
        adapter.addFragment(new FragmentFixtures(), KEY_WORLD_LEAGUE); //LEAGUE
        adapter.addFragment(new FragmentLive(), KEY_INDI_LEAGUE); //LEAGUE
        viewPager.setAdapter(adapter);
    }

    // not in use
    private void callHomeBanner(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(HOMEBANNER,
                    createRequestJson(), getActivity(), getActivity(), HOMEBANNERTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(getActivity()).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        collapse_toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        collapse_toolbar.setVisibility(View.GONE);
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

    public class BannerAdapter extends PagerAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<BeanBanner> mListenerList;

        public BannerAdapter(List<BeanBanner> mListenerList, Context context) {
            this.context = context;
            this.mListenerList = mListenerList;
        }

        @Override
        public int getCount() {
            return mListenerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slider_banner, null);

            ImageView imageView = view.findViewById(R.id.im_banner);

            String Imagename = mListenerList.get(position).getBanner_image();
            final String Type = mListenerList.get(position).getType();

            Glide.with(getActivity()).load(Config.BANNERIMAGE + Imagename)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* if (Type.equals("2")){
                        Intent i = new Intent(getActivity(), InviteFriendsActivity.class);
                        startActivity(i);
                    }*/

                }
            });


            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }
    }
}
