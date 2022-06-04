package com.lgt.fxtradingleague.FragmentBottomMenu;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Education.Adapter.EducationAdapter;
import com.lgt.fxtradingleague.Education.Adapter.EducationSliderAdapter;
import com.lgt.fxtradingleague.Education.Model.EducationSliderModel;
import com.lgt.fxtradingleague.Education.Model.EducationVideosModel;
import com.lgt.fxtradingleague.Education.utils.Const;
import com.lgt.fxtradingleague.R;

import com.lgt.fxtradingleague.databinding.HomeNewFragmentBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewHomeFragment extends Fragment implements ResponseManager {
    EducationAdapter educationAdapter = new EducationAdapter();
    ;
    private HomeNewFragmentBinding binding;
    private EducationSliderAdapter educationSliderAdapter = new EducationSliderAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_new_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCustomNewSlider();
        getFitnessSlider();

    }

    private void getCustomNewSlider() {

        binding.pagerSliderEducation.setAdapter(educationSliderAdapter);
        ArrayList<EducationSliderModel> modelArrayList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i % 2 == 1) {
                EducationSliderModel educationSliderModel = new EducationSliderModel();
                educationSliderModel.setSlider_id("1");
                educationSliderModel.setSlider_image(Const.thumb1);

                modelArrayList.add(educationSliderModel);
            } else {

                EducationSliderModel educationSliderModel = new EducationSliderModel();
                educationSliderModel.setSlider_id("3");
                educationSliderModel.setSlider_image(Const.thumb2);

                modelArrayList.add(educationSliderModel);

            }
        }

        educationSliderAdapter.updateData(modelArrayList);
        binding.dotsIndicator.setViewPager2(binding.pagerSliderEducation);

    }

    private void getFitnessSlider() {
        binding.forBignnersTutsList.setAdapter(educationAdapter);

        List<EducationVideosModel> mEducationList = new ArrayList<>();
        List<EducationVideosModel.Videos> mVideosList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i % 2 == 1) {
                EducationVideosModel educationVideosModel = new EducationVideosModel();
                educationVideosModel.setEducation_category("New Added");
                educationVideosModel.setEducation_category_id(String.valueOf(i));
                for (int j = 0; j < 3; j++) {
                    EducationVideosModel.Videos videosModel = new EducationVideosModel.Videos();
                    videosModel.setVideo_id(String.valueOf(j));
                    videosModel.setVideo_url(Const.video1);
                    videosModel.setVideo_thumb(Const.thumb1);
                    videosModel.setCourse_title(Const.course_heading);
                    videosModel.setCourse_desc(Const.course_desc);
                    mVideosList.add(videosModel);
                    educationVideosModel.setVideosList(mVideosList);
                }
                mEducationList.add(educationVideosModel);
            } else {
                EducationVideosModel educationVideosModel = new EducationVideosModel();
                educationVideosModel.setEducation_category("Trending");
                educationVideosModel.setEducation_category_id(String.valueOf(i));
                for (int j = 0; j < 3; j++) {
                    EducationVideosModel.Videos videosModel = new EducationVideosModel.Videos();
                    videosModel.setVideo_id(String.valueOf(j));
                    videosModel.setVideo_url(Const.video2);
                    videosModel.setVideo_thumb(Const.thumb2);
                    videosModel.setCourse_title(Const.course_heading);
                    videosModel.setCourse_desc(Const.course_desc);
                    mVideosList.add(videosModel);
                    educationVideosModel.setVideosList(mVideosList);
                }
                mEducationList.add(educationVideosModel);
            }


        }
        educationAdapter.updateData(mEducationList);

    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {

    }
}
