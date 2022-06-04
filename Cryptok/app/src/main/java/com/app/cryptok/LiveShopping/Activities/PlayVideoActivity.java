package com.app.cryptok.LiveShopping.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.app.cryptok.R;
import com.app.cryptok.activity.OfflineFun;
import com.app.cryptok.databinding.ActivityPlayVideoBinding;
import com.app.cryptok.LiveShopping.Model.ProductVideosModel;
import com.app.cryptok.utils.Commn;

public class PlayVideoActivity extends AppCompatActivity implements Player.EventListener {
    private SimpleExoPlayer player;
    private SimpleCache simpleCache;
    private CacheDataSourceFactory cacheDataSourceFactory;
    private PlayVideoActivity activity;
    private Context context;
    private ActivityPlayVideoBinding binding;
    private ProductVideosModel.VideosDatum video_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_video);
        context = activity = this;
        iniIntent();
        handleClick();
        playVideo();
    }

    private void handleClick() {
       binding.getRoot().setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (player != null) {
                   if (player.isPlaying()) {
                       pauseVideo();
                       binding.ivPause.setVisibility(View.VISIBLE);
                   } else {
                       binding.ivPause.setVisibility(View.GONE);
                       playVideoNow();
                   }
               }
           }
       });
    }

    private void iniIntent() {
        if (getIntent().hasExtra(Commn.MODEL)) {
            video_model = new Gson().fromJson(getIntent().getStringExtra(Commn.MODEL), ProductVideosModel.VideosDatum.class);

            Commn.showDebugLog("video_model:" + new Gson().toJson(video_model));

        }
    }

    private void playVideo() {
        releaseVideo();
        player = new SimpleExoPlayer.Builder(context).build();
        simpleCache = OfflineFun.simpleCache;
        cacheDataSourceFactory = new CacheDataSourceFactory(simpleCache,
                new DefaultHttpDataSourceFactory(Util.getUserAgent(context, binding
                        .getRoot().getContext().getString(R.string.app_name)))
                , CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        MediaSource progressiveMediaSource =
                new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(Uri.parse(video_model.getVideo()));
        binding.playerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.prepare(progressiveMediaSource, true, false);
        player.seekTo(0, 0);
        player.addListener(this);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        setDimension();
    }

    private void setDimension() {
        float videoProportion = getVideoProportion();
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        float screenProportion = (float) screenHeight / (float) screenWidth;
        android.view.ViewGroup.LayoutParams lp = binding.playerView.getLayoutParams();
        Log.e("screenProportion", screenProportion + "");
        if (videoProportion < screenProportion) {
            Log.e("screenProportion", "falsee");
            lp.height = screenHeight;
            lp.width = (int) ((float) screenHeight / videoProportion);
        } else {
            Log.e("screenProportion", "truee");
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth * videoProportion);
        }
        binding.playerView.setLayoutParams(lp);
    }

    private float getVideoProportion() {
        return 1.5f;
    }

    private void releaseVideo() {
        if (player != null) {
            player.removeListener(this);
            player.setPlayWhenReady(false);
            player.release();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playVideoNow();
    }

    private void playVideoNow() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    private void pauseVideo() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }
}