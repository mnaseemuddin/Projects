package com.lgt.fxtradingleague.Education.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.lgt.fxtradingleague.Education.Model.EducationVideosModel;
import com.lgt.fxtradingleague.Education.utils.Const;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.databinding.ActivityEducationDetailsBinding;

public class EducationDetailsActivity extends AppCompatActivity {
    private EducationDetailsActivity activity;
    private Context context;
    private ActivityEducationDetailsBinding binding;
    private EducationVideosModel.Videos videoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_education_details);
        context = activity = this;
        initIntent();
        handleClick();
    }

    private void initIntent() {
        if (getIntent() != null) {
            if (getIntent().getStringExtra(Const.MODEL) != null) {
                videoModel = new Gson().fromJson(getIntent().getStringExtra(Const.MODEL), EducationVideosModel.Videos.class);
                getScreenData();
            }
        }
    }

    private void getScreenData() {
        binding.tvCourseDesc.setText(videoModel.getCourse_desc());
        binding.tvCourseTitle.setText(videoModel.getCourse_title());
        Glide.with(getApplicationContext())
                .load(videoModel.getVideo_thumb())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivAutoImageSlider);
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(v -> onBackPressed());
        binding.ivAutoImageSlider.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra(Const.MODEL, new Gson().toJson(videoModel));
            startActivity(intent);
        });
    }
}