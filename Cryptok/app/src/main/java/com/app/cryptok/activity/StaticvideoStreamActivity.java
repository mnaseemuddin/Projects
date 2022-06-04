package com.app.cryptok.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.flashphoner.fpwcsapi.Flashphoner;
import com.flashphoner.fpwcsapi.bean.Connection;
import com.flashphoner.fpwcsapi.bean.Data;
import com.flashphoner.fpwcsapi.bean.StreamStatus;
import com.flashphoner.fpwcsapi.bean.StreamStatusInfo;
import com.flashphoner.fpwcsapi.constraints.AudioConstraints;
import com.flashphoner.fpwcsapi.constraints.Constraints;
import com.flashphoner.fpwcsapi.constraints.VideoConstraints;
import com.flashphoner.fpwcsapi.session.Session;
import com.flashphoner.fpwcsapi.session.SessionEvent;
import com.flashphoner.fpwcsapi.session.SessionOptions;
import com.flashphoner.fpwcsapi.session.Stream;
import com.flashphoner.fpwcsapi.session.StreamOptions;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.adapter.LiveStreamAdapter.AllJoinedStreamHolder;
import com.app.cryptok.databinding.ActivityStaticvideoStreamBinding;
import com.app.cryptok.fragment.Profile.BottomUserForStreamView;
import com.app.cryptok.fragment.stream.AllViewersFragmentForViewers;
import com.app.cryptok.go_live_module.LiveConst;
import com.app.cryptok.go_live_module.Model.GoLiveModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.model.StreamCommentModel;
import com.app.cryptok.model.StreamViewersModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.FastClickUtil;
import com.app.cryptok.utils.FlashphonerConst;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.view_live_module.GiftDialog;
import com.app.cryptok.view_live_module.HotLiveModel;
import com.app.cryptok.view_live_module.StreamCommentsHolder;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.webrtc.RendererCommon;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.app.cryptok.go_live_module.LiveConst.STREAM_MODEL;


public class StaticvideoStreamActivity extends AppCompatActivity {
    private String uName,stream_url, stream_title, stream_user_id, stream_image, stream_id, stream_user_name;
    private Context context;
    private StaticvideoStreamActivity activity;
    //comment
    private TextView tv_user_name;


    private boolean isAnotherGuestAvailable = false;
    ActivityStaticvideoStreamBinding binding;

    private boolean isShareClick = false;
    private VideoView videoViewPlay;
    private ProgressBar pbVideoView;
    int intValue;
    ImageView iv_user_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_staticvideo_stream);
        context = activity = this;
        Intent mIntent = getIntent();
        intValue = mIntent.getIntExtra("VideoPath", 0);
        uName = mIntent.getStringExtra("UserName");

        iniViews();
        initPlayer();
        initCustomView();
    }

    private void initCustomView() {
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load("android.resource://" + context.getPackageName() + "/" +intValue )
                .apply(requestOptions)
                .thumbnail(Glide.with(context).load("android.resource://" + context.getPackageName() + "/" + intValue))
                .into(iv_user_image);
    }

    private void iniViews() {

        videoViewPlay = findViewById(R.id.videoViewPlay);
        tv_user_name = findViewById(R.id.tv_user_name);
        pbVideoView = findViewById(R.id.pbVideoView);
        iv_user_image = findViewById(R.id.iv_user_image);
        tv_user_name.setText(uName);


    }

    private void initPlayer() {

        Uri url = Uri.parse("android.resource://" + getPackageName() + "/" + intValue);
        Log.e("checkVideoUrl", url + "");
        videoViewPlay.setKeepScreenOn(true);
        videoViewPlay.setVideoURI(url);
        pbVideoView.setVisibility(View.GONE);
        videoViewPlay.requestFocus();
        videoViewPlay.start();

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

        videoViewPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        binding.ivCloseStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}