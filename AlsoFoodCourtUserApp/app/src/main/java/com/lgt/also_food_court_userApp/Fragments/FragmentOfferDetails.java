package com.lgt.also_food_court_userApp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.tabs.TabLayout;
import com.lgt.also_food_court_userApp.R;


import java.util.HashMap;

public class FragmentOfferDetails extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, TabLayout.OnTabSelectedListener {

    SliderLayout slider_offerDetailed;

    public FragmentOfferDetails() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_offer_details, container, false);
        slider_offerDetailed = view.findViewById(R.id.slider_offerDetailed);

        bannerSlider();

        return view;
    }

    private void bannerSlider() {

        {
            HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
            file_maps.put("Veg Burger", R.drawable.burger1);
            file_maps.put("Cheese Burger", R.drawable.burger2);
            file_maps.put("Egg Burger", R.drawable.burger3);
            file_maps.put("Chicken Burger", R.drawable.burger4);

            for (String name : file_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(getActivity());

                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);

                slider_offerDetailed.addSlider(textSliderView);
            }
            slider_offerDetailed.setPresetTransformer(SliderLayout.Transformer.Accordion);
            slider_offerDetailed.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider_offerDetailed.setCustomAnimation(new DescriptionAnimation());
            slider_offerDetailed.setDuration(4000);
            slider_offerDetailed.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
