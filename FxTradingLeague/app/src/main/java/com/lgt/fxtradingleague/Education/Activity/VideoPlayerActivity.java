package com.lgt.fxtradingleague.Education.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.google.gson.Gson;
import com.lgt.fxtradingleague.Education.Model.EducationVideosModel;
import com.lgt.fxtradingleague.Education.utils.Const;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.databinding.ActivityVideoPlayerBinding;

import tcking.github.com.giraffeplayer2.VideoInfo;

public class VideoPlayerActivity extends AppCompatActivity {
    private EducationVideosModel.Videos videoModel;
    private Context context;
    private VideoPlayerActivity activity;
    private ActivityVideoPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_player);
        context = activity = this;
        hanldleClick();
        initIntent();
    }

    private void initIntent() {
        if (getIntent() != null) {
            if (getIntent().getStringExtra(Const.MODEL) != null) {
                videoModel = new Gson().fromJson(getIntent().getStringExtra(Const.MODEL), EducationVideosModel.Videos.class);
                initPlayer();
            }
        }
    }

    private void initPlayer() {
        binding.videoView.setVideoPath(videoModel.getVideo_url()).getPlayer().start();
        binding.videoView.getVideoInfo().setBgColor(Color.BLACK).setTitle(videoModel.getCourse_title())
                .setAspectRatio(VideoInfo.AR_MATCH_PARENT);
        binding.videoView.getPlayer().toggleFullScreen();
    }

    private void hanldleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}