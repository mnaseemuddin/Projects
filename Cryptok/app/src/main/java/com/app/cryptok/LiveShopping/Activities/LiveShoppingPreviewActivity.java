package com.app.cryptok.LiveShopping.Activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.LiveShopping.Fragment.ProductSheetFragment;
import com.app.cryptok.LiveShopping.Model.LiveShoppingModel;
import com.app.cryptok.R;
import com.app.cryptok.activity.OfflineFun;
import com.app.cryptok.adapter.LiveStreamAdapter.AllJoinedStreamHolder;
import com.app.cryptok.databinding.ActivityLiveShoppingPreviewBinding;
import com.app.cryptok.fragment.Profile.BottomUserForStreamView;
import com.app.cryptok.fragment.stream.AllViewersFragmentForViewers;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.model.StreamCommentModel;
import com.app.cryptok.model.StreamViewersModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.FastClickUtil;
import com.app.cryptok.utils.FlashphonerConst;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.view_live_module.StreamCommentsHolder;

import org.webrtc.RendererCommon;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;



public class LiveShoppingPreviewActivity extends AppCompatActivity implements Player.EventListener {

    private ActivityLiveShoppingPreviewBinding binding;
    private Context context;
    private LiveShoppingPreviewActivity activity;
    private CollectionReference stream_ref;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference user_info;

    private LiveShoppingModel liveShoppingModel;
    private FirebaseDatabase database;
    //user info
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;

    private boolean isStream_started = false;

    //stream
    private Session session;
    private Stream playStream;
    //exo player
    private SimpleExoPlayer player;
    private SimpleCache simpleCache;
    private CacheDataSourceFactory cacheDataSourceFactory;

    //my info
    DatabaseReference viewers_ref;
    private FirebaseRecyclerOptions<StreamViewersModel> viewers_firebase_options;
    private FirebaseRecyclerAdapter<StreamViewersModel, AllJoinedStreamHolder> viewers_adapter;
    private String selected_user_id = "";

    //comment
    private String mComment;
    private String comment_type = DBConstants.simple_type;
    private String last_comment_key;
    FirebaseRecyclerOptions<StreamCommentModel> firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<StreamCommentModel, StreamCommentsHolder> comments_adapter;
    private LinearLayoutManager comments_layout_manager;
    //comment

    private CountDownTimer audio_events_countdown;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_shopping_preview);
        context = activity = this;

        iniFirebase();
        iniIntent();
        if (liveShoppingModel != null) {
            setUserInfo();
        }

        getAllViewers();
        handleClick();
        checkStreamAvailable();

        getAudioSerive();
    }

    private void getAudioSerive() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audio_events_countdown == null) {
            audio_events_countdown = new CountDownTimer(1000, 20000) {
                @Override
                public void onTick(long l) {
                    getAudioEvents();
                }

                @Override
                public void onFinish() {

                    audio_events_countdown.start();
                }
            }.start();
        }
    }

    private void getAudioEvents() {
        if (audioManager.isWiredHeadsetOn()) {
            Commn.showDebugLog("earphone connected");
            if (session != null) {
                if (playStream != null) {
                    Flashphoner.getAudioManager().getAudioManager().setSpeakerphoneOn(false);
                    Flashphoner.getAudioManager().setUseBluetoothSco(true);
                }
            }
        } else {
            Commn.showDebugLog("earphone disconnected");
            if (session != null) {
                if (playStream != null) {
                    Flashphoner.getAudioManager().getAudioManager().setSpeakerphoneOn(true);
                    Flashphoner.getAudioManager().setUseBluetoothSco(false);
                }
            }
        }


    }


    private void setUserInfo() {
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(liveShoppingModel.getUser_id())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() == null) {
                        return;
                    }
                    if (task.getResult().exists()) {
                        String name = "";
                        String image = "";
                        if (task.getResult().getString(DBConstants.name) != null) {
                            name = task.getResult().getString(DBConstants.name);
                        } else {
                            name = task.getResult().getString(DBConstants.super_live_id);
                        }
                        binding.tvUserName.setText(name + "");
                        if (task.getResult().getString(DBConstants.image) != null) {
                            image = task.getResult().getString(DBConstants.image);
                            Glide.with(getApplicationContext()).
                                    load(image).placeholder(R.drawable.placeholder_user)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivUserImage);
                        }

                        String finalImage = image;
                        binding.ivShareStream.setOnClickListener(v -> {
                            if (FastClickUtil.isFastClick()) {
                                return;
                            }
                            if (isStream_started) {
                                String name1 = "";

                                if (profilePOJO.getName() != null) {
                                    if (profilePOJO.getName().isEmpty()) {
                                        name1 = profilePOJO.getSuper_live_id();
                                    } else {
                                        name1 = profilePOJO.getName();
                                    }

                                } else {
                                    name1 = profilePOJO.getSuper_live_id();
                                }
                                generateShareLink(name1, finalImage);
                            }

                        });


                    }

                });

    }

    private void checkStreamAvailable() {
        firebaseFirestore.collection(DBConstants.LiveShopping)
                .document(liveShoppingModel.getUser_id())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value == null) {
                            return;
                        }
                        if (!value.exists()) {
                            endedStreamUi();
                        }
                    }
                });
    }

    private void handleClick() {

        binding.ivAllViewers.setOnClickListener(view -> openAllViewersSheet(liveShoppingModel.getUser_id()));
        binding.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.rlSaySomething.getVisibility() == View.VISIBLE) {
                    hideSaySomething();
                }
            }
        });
        binding.ivChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStream_started) {
                    showCommentVisible();
                } else {
                    Commn.myToast(context, "wait ,you not started yet....");
                }
            }
        });
        commentTextChangeListener();
        binding.ivSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mComment = binding.etSaySomething.getText().toString();
                comment_type = DBConstants.simple_type;
                checkCommentAuth();

            }
        });
        binding.ivGiftStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStream_started) {

                    openProductSheet();
                } else {
                    Commn.myToast(context, "wait stream is not started yet...");
                }
            }
        });
    }

    private void openProductSheet() {
        ProductSheetFragment productSheetFragment = new ProductSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DBConstants.LiveShoppingModel, liveShoppingModel.getPRODUCT_MODEL());
        productSheetFragment.setArguments(bundle);
        productSheetFragment.show(getSupportFragmentManager(), "");
    }

    private void checkCommentAuth() {

        database.getReference().child(DBConstants.Mute_Users)
                .child(liveShoppingModel.getStream_id())
                .child(profilePOJO.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Commn.myToast(context, "You are muted by host");
                        } else {
                            startComment(profilePOJO.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void startComment(String name) {
        DatabaseReference reference = database.getReference();
        String currentUserRef = DBConstants.Stream_Comments + "/" + liveShoppingModel.getStream_id();

        DatabaseReference user_message_push = reference.child(DBConstants.Stream_Comments).
                child(liveShoppingModel.getStream_id()).push();
        String push_id = user_message_push.getKey();
        Map messageMap = new HashMap();

        messageMap.put(DBConstants.stream_cmnt_id, push_id);
        messageMap.put(DBConstants.stream_comment, mComment);
        messageMap.put(DBConstants.stream_comment_user_name, name);
        messageMap.put(DBConstants.stream_comment_user_id, profilePOJO.getUserId());
        messageMap.put(DBConstants.comment_type, comment_type);


        Map messageUserMap = new HashMap();
        messageUserMap.put(currentUserRef + "/" + push_id, messageMap);

        binding.etSaySomething.setText("");
        reference.updateChildren(messageUserMap, (error, ref) -> {
            Log.d("comment_check", "comment_added");

            if (error != null) {
                Log.d("comment_check", "error=" + error.getMessage() + "");
            }
        });
    }

    private void commentTextChangeListener() {
        binding.etSaySomething.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().trim().length() > 0) {
                    visibleSendImageView();
                } else {
                    hideSendImageView();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void hideSendImageView() {
        binding.ivSendComment.setVisibility(View.GONE);
    }

    private void visibleSendImageView() {

        binding.ivSendComment.setVisibility(View.VISIBLE);
    }

    private void showCommentVisible() {
        binding.rlSaySomething.setVisibility(View.VISIBLE);
        binding.etSaySomething.requestFocus();
        binding.rlBottomContent.setVisibility(View.GONE);

    }

    private void hideSaySomething() {
        binding.rlBottomContent.setVisibility(View.VISIBLE);
        binding.rlSaySomething.setVisibility(View.GONE);
    }

    private void getAllViewers() {
        inViewersFirebaseOptions();
        viewers_adapter = new FirebaseRecyclerAdapter<StreamViewersModel, AllJoinedStreamHolder>(viewers_firebase_options) {
            @Override
            protected void onBindViewHolder(@NonNull AllJoinedStreamHolder holder, int i, @NonNull StreamViewersModel model) {

                Glide.with(getApplicationContext()).load(model.getUser_image()).placeholder(R.drawable.placeholder_user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_user_image);


                holder.iv_user_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        openBottomSheet(model.getUser_id());
                    }
                });
            }

            @NonNull
            @Override
            public AllJoinedStreamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View TextChatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_viewer_layout, parent, false);
                return new AllJoinedStreamHolder(TextChatView);
            }
        };
        viewers_adapter.startListening();
        binding.rvStreamingViewers.setAdapter(viewers_adapter);

    }

    private void openBottomSheet(String stream_comment_user_id) {
        Bundle bundle = new Bundle();
        bundle.putString(DBConstants.user_id, stream_comment_user_id);
        BottomUserForStreamView bottomUserProfile = new BottomUserForStreamView();
        bottomUserProfile.setArguments(bundle);
        bottomUserProfile.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {

        } else if (playbackState == Player.STATE_READY) {
            isStream_started = true;
            addJoinStream();
            getComments();

        }
    }

    private void addJoinStream() {
        DatabaseReference reference = database.getReference();
        String ref = DBConstants.Current_Viewer + "/" + liveShoppingModel.getUser_id() + "/" + profilePOJO.getUserId();
        Map map = new HashMap();
        map.put(DBConstants.user_id, profilePOJO.getUserId());
        map.put(DBConstants.user_name, profilePOJO.getName());
        map.put(DBConstants.user_image, profilePOJO.getImage());
        map.put(DBConstants.stream_id, liveShoppingModel.getStream_id() + "");
        Map finalMap = new HashMap();
        finalMap.put(ref, map);

        reference.updateChildren(finalMap, (error, ref1) -> {
            if (error != null) {
                Commn.showDebugLog("error_occured to add in join stream:" + error.getMessage() + "");
            } else {
                Commn.showDebugLog("joined stream:");
            }
        });

        addJoinedInFirestore();

    }

    private void addJoinedInFirestore() {

        Map<String, Object> map = new HashMap<>();
        map.put(DBConstants.stream_id, liveShoppingModel.getStream_id());
        map.put(DBConstants.user_id, liveShoppingModel.getUser_id());
        map.put(DBConstants.timestamp, FieldValue.serverTimestamp());
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .collection(DBConstants.my_all_joined_broadcast)
                .document(liveShoppingModel.getStream_id())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (!task.getResult().exists()) {
                            firebaseFirestore.collection(DBConstants.UserInfo)
                                    .document(profilePOJO.getUserId())
                                    .collection(DBConstants.my_all_joined_broadcast)
                                    .document(liveShoppingModel.getStream_id())
                                    .set(map)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {

                                            Commn.showDebugLog("i am added successfully in broadcast...");
                                        }
                                    });
                        }
                    }
                });

    }

    private void openAllViewersSheet(String user_id) {
        Bundle bundle = new Bundle();
        bundle.putString(DBConstants.user_id, user_id);
        AllViewersFragmentForViewers bottomUserProfile = new AllViewersFragmentForViewers();
        bottomUserProfile.setArguments(bundle);
        bottomUserProfile.show(getSupportFragmentManager(), "");
    }

    private void inViewersFirebaseOptions() {
        binding.rvStreamingViewers.setHasFixedSize(true);
        binding.rvStreamingViewers.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewers_ref = database.getReference().child(DBConstants.Current_Viewer).child(liveShoppingModel.getUser_id());
        Query query = viewers_ref.limitToFirst(10);
        viewers_firebase_options = new FirebaseRecyclerOptions.Builder<StreamViewersModel>().setQuery(query, StreamViewersModel.class).build();

        getTotalViewers();
    }

    private void getTotalViewers() {
        viewers_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    binding.tvTotalViewer.setText(String.valueOf(snapshot.getChildrenCount()) + "");


                } else {
                    binding.tvTotalViewer.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniFirebase() {
        database = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void iniIntent() {
        if (getIntent().hasExtra(DBConstants.LiveShoppingModel)) {
            liveShoppingModel = new Gson().fromJson(getIntent().getStringExtra(DBConstants.LiveShoppingModel), LiveShoppingModel.class);

            Commn.showDebugLog("recieved_live_shopping_model:" + new Gson().toJson(liveShoppingModel));
            if (liveShoppingModel != null) {
                iniStream();
            }
        }
    }

    private void iniStream() {
        if (DBConstants.Live_Streaming_Type.equalsIgnoreCase(liveShoppingModel.getStream_type())) {
            inializeFlashphonerStream();
        } else {
            inializeExoPlayer();
        }
    }

    private void inializeExoPlayer() {
        enableExoPlayerUi();
        releaseVideo();
        player = new SimpleExoPlayer.Builder(context).build();
        simpleCache = OfflineFun.simpleCache;
        cacheDataSourceFactory = new CacheDataSourceFactory(simpleCache,
                new DefaultHttpDataSourceFactory(Util.getUserAgent(context, binding
                        .getRoot().getContext().getString(R.string.app_name)))
                , CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        MediaSource progressiveMediaSource =
                new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(Uri.parse(liveShoppingModel.getStream_url()));
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
        cancleAudioEvents();
        pauseVideo();
        deleteMe();
    }

    @Override
    protected void onResume() {
        super.onResume();

        playVideoNow();
        if (audio_events_countdown == null) {
            getAudioSerive();
        }
    }

    private void cancleAudioEvents() {
        if (audio_events_countdown != null) {
            audio_events_countdown.cancel();
        }
    }

    private void deleteMe() {
        DatabaseReference reference = database.getReference().child(DBConstants.Current_Viewer)
                .child(liveShoppingModel.getUser_id())
                .child(profilePOJO.getUserId());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Commn.showDebugLog("available current viewers");
                    snapshot.getRef().removeValue();
                } else {

                    Commn.showDebugLog("removed current viewers");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        if (session != null) {
            session.disconnect();
            if (playStream != null) {
                playStream.stop();
            }

        }
    }

    private void enableExoPlayerUi() {
        binding.flashphonerLayout.setVisibility(View.GONE);
        binding.exoPlayerLayout.setVisibility(View.VISIBLE);
    }

    private void inializeFlashphonerStream() {
        enableFlashPhonerUi();
        Flashphoner.init(this);

        Flashphoner.getAudioManager().setUseBluetoothSco(true);
        Flashphoner.getAudioManager().setUseSpeakerPhone(true);
        Flashphoner.getAudioManager().getAudioManager().setMicrophoneMute(false);
        //  binding.surfaceView.setZOrderMediaOverlay(true);

        URI u = null;
        try {
            u = new URI(FlashphonerConst.BASE_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String url = null;
        if (u != null) {
            url = u.getScheme() + "://" + u.getHost() + ":" + u.getPort();
        }

        SessionOptions sessionOptions = new SessionOptions(url);
        sessionOptions.setRemoteRenderer(binding.surfaceView);
        session = Flashphoner.createSession(sessionOptions);
        if (session == null) {
            return;
        }
        session.connect(new Connection());
        session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {

            }

            @Override
            public void onConnected(final Connection connection) {
                Commn.showErrorLog("Connection" + connection.toString());
                runOnUiThread(() -> {

                    StreamOptions streamOptions = new StreamOptions(liveShoppingModel.getStream_url());
                    VideoConstraints videoConstraints = new VideoConstraints();
                    streamOptions.getConstraints().setVideoConstraints(videoConstraints);
                    streamOptions.setConstraints(new Constraints(new AudioConstraints(), videoConstraints));
                    playStream = session.createStream(streamOptions);

                    if (playStream == null) {
                        return;
                    }
                    playStream.on((stream, streamStatus) -> runOnUiThread(() -> {

                        Commn.showErrorLog("host_stream_status" + String.valueOf(streamStatus));
                        if (StreamStatus.FAILED.equals(streamStatus)) {

                            endedStreamUi();
                            switch (stream.getInfo()) {
                                case StreamStatusInfo.SESSION_DOES_NOT_EXIST:
                                    Commn.showErrorLog(streamStatus + ": Actual session does not exist");
                                    break;
                                case StreamStatusInfo.STOPPED_BY_PUBLISHER_STOP:
                                    // pauseStatus();
                                    Commn.showErrorLog(streamStatus + ": Related publisher stopped its stream or lost connection");
                                    break;
                                case StreamStatusInfo.SESSION_NOT_READY:
                                    Commn.showErrorLog(streamStatus + ": Session is not initialized or terminated on play ordinary stream");
                                    break;
                                case StreamStatusInfo.RTSP_STREAM_NOT_FOUND:
                                    Commn.showErrorLog(streamStatus + ": Rtsp stream not found where agent received '404-Not Found'");
                                    break;
                                case StreamStatusInfo.FAILED_TO_CONNECT_TO_RTSP_STREAM:
                                    Commn.showErrorLog(streamStatus + ": Failed to connect to rtsp stream");
                                    break;
                                case StreamStatusInfo.FILE_NOT_FOUND:
                                    Commn.showErrorLog(streamStatus + ": File does not exist, check filename");
                                    break;
                                case StreamStatusInfo.FILE_HAS_WRONG_FORMAT:
                                    Commn.showErrorLog(streamStatus + ": File has wrong format on play vod, this format is not supported");
                                    break;
                                case StreamStatusInfo.STREAM_NAME_ALREADY_IN_USE:
                                    Commn.myToast(context, "Server already has a publish stream with the same name, try using different one");
                                    Commn.showErrorLog(streamStatus + ": Server already has a publish stream with the same name, try using different one");
                                    break;
                                default: {
                                    Commn.showErrorLog(stream.getInfo());

                                }
                            }
                        } else {
                            // playStatus();
                            isStream_started = true;

                            addJoinStream();
                            getComments();
                        }

                    }));
                    playStream.play();
                });

            }

            @Override
            public void onRegistered(Connection connection) {

            }

            @Override
            public void onDisconnection(final Connection connection) {
                runOnUiThread(() -> {
                    Commn.showDebugLog("player_status" + "streaming is disconnected" + "");
                });
            }
        });
        binding.videoFrame.setPosition(0, 0, 100, 100);
        binding.surfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        binding.surfaceView.setMirror(false);
        binding.surfaceView.requestLayout();

    }

    private void generateShareLink(String name, String image) {
        String title = name + " is live now";


        Commn.showDebugLog("ShareJson" + "Json Object: " + title);
      /*  DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(MyApi.web_url))
                .setDomainUriPrefix("https://superlive.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder(getPackageName()).build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();*/

        String dynamicQuery = "https://superlive.page.link" + "/?" +
                "link=" + MyApi.web_url + "/myrefer.php?" + "userid=" + liveShoppingModel.getUser_id() + "-" + DBConstants.LiveShopping +
                "&apn=" + getPackageName() +
                "&st=" + title +
                "&sd=" + getString(R.string.app_name) +
                "&si=" + image;
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(dynamicQuery))
                .buildShortDynamicLink()

                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Commn.showDebugLog("shortLink:" + shortLink + ",flowchartLink" + flowchartLink);
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);

                        } else {
                            Commn.showDebugLog("error on generating dynamic url:" + task.getException().getMessage() + "");
                        }
                    }
                });
    }

    private void playStatus() {
        binding.tvBroadcastStatus.setText("Connected");
        binding.tvBroadcastStatus.setVisibility(View.GONE);

    }

    private void pauseStatus() {
        binding.tvBroadcastStatus.setVisibility(View.VISIBLE);
        binding.tvBroadcastStatus.setText("Broadcast Paused");
    }

    private void getComments() {
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        DatabaseReference current_stream_Ref = database.getReference().child(DBConstants.Stream_Comments)
                .child(liveShoppingModel.getStream_id());
        //   scrollListener();
        current_stream_Ref.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (snapshot1.hasChild(DBConstants.stream_cmnt_id)) {
                            last_comment_key = snapshot1.child(DBConstants.stream_cmnt_id).getValue().toString();
                            Commn.showErrorLog("snapshot.exists()" + "");
                            iniCommentsOptions(true);
                            Commn.showErrorLog("last_comments_snapshot:" + last_comment_key + "");
                        }

                    }

                } else {
                    Commn.showErrorLog("snapshot not exists()" + "");
                    iniCommentsOptions(false);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    Query query;

    private void iniCommentsOptions(boolean isAdded) {

        DatabaseReference current_stream_Ref = database.getReference().child(DBConstants.Stream_Comments).
                child(liveShoppingModel.getStream_id());

        if (isAdded) {
            Commn.showErrorLog("already_comments_present:" + last_comment_key + "");
            query = current_stream_Ref.orderByChild(DBConstants.stream_cmnt_id)
                    .startAt(last_comment_key);
            // Query query=current_stream_Ref.orderByChild("").
            firebaseRecyclerOptions = new FirebaseRecyclerOptions
                    .Builder<StreamCommentModel>()
                    .setQuery(query, StreamCommentModel.class)
                    .build();
        } else {
            Commn.showErrorLog("no comments...");
            firebaseRecyclerOptions = new FirebaseRecyclerOptions
                    .Builder<StreamCommentModel>()
                    .setQuery(current_stream_Ref, StreamCommentModel.class)
                    .build();
        }

        comments_adapter = new FirebaseRecyclerAdapter<StreamCommentModel, StreamCommentsHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull StreamCommentsHolder hotLiveHolder, int position, @NonNull StreamCommentModel hotLiveModel) {


                hotLiveHolder.tv_comment.setText(hotLiveModel.getStream_comment() + "");
                hotLiveHolder.tv_comment_username.setText(hotLiveModel.getStream_comment_user_name() + "");

                Log.d("my_comment", hotLiveModel.getStream_comment() + "");

                hotLiveHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        openBottomSheet(hotLiveModel.getStream_comment_user_id());
                    }
                });

                if (hotLiveModel.getComment_type() != null) {
                    if (DBConstants.gift_type.equalsIgnoreCase(hotLiveModel.getComment_type())) {
                        hotLiveHolder.tv_comment.setTextColor(context.getResources().getColor(R.color.premium_color));
                    }
                }
                getLevel(hotLiveModel.getStream_comment_user_id(), hotLiveHolder);
            }

            @NonNull
            @Override
            public StreamCommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_comments_layout, parent, false);
                return new StreamCommentsHolder(textView);
            }


        };
        comments_adapter.startListening();
        binding.rvComments.setHasFixedSize(true);
        comments_layout_manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvComments.setLayoutManager(comments_layout_manager);
        binding.rvComments.setAdapter(comments_adapter);

        comments_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int friendlyMessageCount = comments_adapter.getItemCount();
                int lastVisiblePosition = comments_layout_manager.findLastCompletelyVisibleItemPosition();

                Log.e("countttmess", friendlyMessageCount + "");
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {

                    binding.commentsNested.smoothScrollTo(0, binding.rvComments.getBottom());
                    binding.rvComments.scrollToPosition(positionStart);
                }
            }
        });

    }

    private void getLevel(String userid, StreamCommentsHolder holder) {
        user_info.document(userid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            if (task.getResult().exists()) {
                                if (task.getResult().getString(ConstantsKey.current_level) != null) {
                                    String current_level = task.getResult().getString(ConstantsKey.current_level);
                                    holder.tv_user_level.setText("Lv" + current_level + "");
                                }
                            }
                        }
                    }


                });
    }

    private void endedStreamUi() {
        binding.rlSaySomething.setVisibility(View.GONE);
        binding.ivGiftStream.setVisibility(View.GONE);
        binding.rlBottomContent.setVisibility(View.GONE);
        binding.rlUserInfo.setVisibility(View.GONE);
        releaseVideo();
        endFlashphonerPlayer();
        binding.tvBroadcastStatus.setVisibility(View.VISIBLE);
        binding.tvBroadcastStatus.setText("Broadcast Ended");
    }

    private void endFlashphonerPlayer() {
        if (session != null) {
            session.disconnect();
        }
    }

    private void enableFlashPhonerUi() {
        binding.flashphonerLayout.setVisibility(View.VISIBLE);
        binding.exoPlayerLayout.setVisibility(View.GONE);
    }
}