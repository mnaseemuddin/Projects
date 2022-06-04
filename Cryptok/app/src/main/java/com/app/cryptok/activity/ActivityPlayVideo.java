package com.app.cryptok.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.cryptok.R;
import com.google.gson.Gson;


public class ActivityPlayVideo extends AppCompatActivity {

  //  private EducationVideosModel.CategoryDatum videoModel;

    private VideoView videoViewPlay;
    private String mVideoURL = "";
    private ProgressBar pbVideoView;

    private MediaController mediaController;

   // String API_KEY = "AIzaSyAVZuFJmrcoWc1oYBpgnieb7YW6AzDEWzc";


    private static final String TAG = "ActivityPlayVideo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        videoViewPlay = findViewById(R.id.videoViewPlay);
        pbVideoView = findViewById(R.id.pbVideoView);
        mediaController = new MediaController(ActivityPlayVideo.this);

        initPlayer();

    }

   /* private void initIntent() {

        if (getIntent() != null) {
            if (getIntent().getStringExtra(Const.MODEL) != null) {
                videoModel = new Gson().fromJson(getIntent().getStringExtra(Const.MODEL), EducationVideosModel.CategoryDatum.class);
                //g.e("",videoModel+"");


            }
        }
    }*/

    private void initPlayer() {
        Log.e("getVideoUrl","okGotit");

        Uri videoURI = Uri.parse("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4");
        Log.e("getVideoUrl",videoURI+"okGotit");
        videoViewPlay.setVideoURI(videoURI);

        videoViewPlay.setMediaController(mediaController);
        mediaController.setAnchorView(videoViewPlay);
        videoViewPlay.start();
        pbVideoView.setVisibility(View.VISIBLE);

        videoViewPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        pbVideoView.setVisibility(View.GONE);
                        mp.start();
                    }
                });
            }
        });
    }
}
