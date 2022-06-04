package com.app.cryptok.LiveShopping.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.flashphoner.fpwcsapi.handler.CameraSwitchHandler;
import com.flashphoner.fpwcsapi.session.ConnectionQualityCallback;
import com.flashphoner.fpwcsapi.session.Session;
import com.flashphoner.fpwcsapi.session.SessionEvent;
import com.flashphoner.fpwcsapi.session.SessionOptions;
import com.flashphoner.fpwcsapi.session.Stream;
import com.flashphoner.fpwcsapi.session.StreamOptions;
import com.flashphoner.fpwcsapi.ws.ConnectionQuality;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.LiveShopping.Fragment.HostProductSheet;
import com.app.cryptok.LiveShopping.Model.LiveShoppingModel;
import com.app.cryptok.Notifications.InializeNotification;
import com.app.cryptok.Notifications.NotificationRequest;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.Notifications.Sender;
import com.app.cryptok.R;
import com.app.cryptok.activity.OfflineFun;
import com.app.cryptok.adapter.LiveStreamAdapter.AllJoinedStreamHolder;
import com.app.cryptok.databinding.ActivityLiveShoppingBinding;
import com.app.cryptok.fragment.Profile.BottomUserProfile;
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

public class LiveShoppingActivity extends AppCompatActivity implements Player.EventListener {
    private CollectionReference stream_ref;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase database;
    private LiveShoppingModel liveShoppingModel;
    //user info
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private ActivityLiveShoppingBinding binding;
    private Context context;
    private LiveShoppingActivity activity;
    private Session session;
    private Stream publishStream;
    private boolean isStream_started;

    //exo player
    private SimpleExoPlayer player;
    private SimpleCache simpleCache;
    private CacheDataSourceFactory cacheDataSourceFactory;

    //my info
    DatabaseReference viewers_ref;
    private FirebaseRecyclerOptions<StreamViewersModel> viewers_firebase_options;
    private FirebaseRecyclerAdapter<StreamViewersModel, AllJoinedStreamHolder> viewers_adapter;
    private String selected_user_id = "";

    private CollectionReference user_info;
    //comment
    private String mComment;
    private String comment_type = DBConstants.simple_type;
    private String last_comment_key;
    FirebaseRecyclerOptions<StreamCommentModel> firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<StreamCommentModel, StreamCommentsHolder> comments_adapter;
    private LinearLayoutManager comments_layout_manager;

    //comment
    PhoneStateListener phoneStateListener;
    private boolean isONCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_shopping);
        context = activity = this;
        sessionManager = new SessionManager(activity);
        iniFirebase();
        iniIntent();
        setUserInfo();
        handleClick();


    }

    private void getDeviceEvent() {
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        //Incoming call: Pause music
                        Commn.showDebugLog("Incoming call:" + incomingNumber);
                        pauseStream();
                        isONCall = true;
                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //A call is dialing, active or on hold
                        pauseStream();
                        Commn.showDebugLog("A call is dialing, active or on hold");
                        isONCall = true;
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Commn.showDebugLog("Not in call");
                        playStream();
                        isONCall = false;
                        //Not in call: Play music
                        break;
                }

                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }


    }


    private void pauseStream() {
        if (session != null) {
            if (publishStream != null) {
                publishStream.muteVideo();
            }
        }
    }

    private void playStream() {
        if (session != null) {
            if (publishStream != null) {
                publishStream.unmuteVideo();
            }
        }
    }

    private void handleClick() {
        binding.ivCloseStream.setOnClickListener(view -> {

            if (!isStream_started) {
                onBackPressed();
            } else {
                endStreamDialog();
            }
        });
        binding.ivAllViewers.setOnClickListener(view -> openAllViewersSheet(liveShoppingModel.getUser_id()));
        binding.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.rlSaySomething.getVisibility() == View.VISIBLE) {
                    hideSaySomething();
                }
            }
        });
        binding.ivChat.setOnClickListener(view -> {
            if (isStream_started) {
                showCommentVisible();
            } else {
                Commn.myToast(context, "wait ,you not started yet....");
            }
        });
        commentTextChangeListener();
        binding.ivSendComment.setOnClickListener(view -> {
            mComment = binding.etSaySomething.getText().toString();
            comment_type = DBConstants.simple_type;
            checkCommentAuth();
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
        binding.ivFlipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session != null) {
                    if (publishStream.isPublished()) {
                        switchCamera();
                    }
                }

            }
        });
    }

    public void switchCamera() {

        publishStream.switchCamera(new CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean b) {

                Commn.showErrorLog("onCameraSwitchDone:" + b + "");
            }

            @Override
            public void onCameraSwitchError(String s) {
                Commn.showErrorLog("onCameraSwitchError:" + s + "");
            }
        });


    }

    private void openProductSheet() {
        HostProductSheet productSheetFragment = new HostProductSheet();
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


    private void openAllViewersSheet(String user_id) {
        Bundle bundle = new Bundle();
        bundle.putString(DBConstants.user_id, user_id);
        AllViewersFragmentForViewers bottomUserProfile = new AllViewersFragmentForViewers();
        bottomUserProfile.setArguments(bundle);
        bottomUserProfile.show(getSupportFragmentManager(), "");
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

    @Override
    public void onBackPressed() {

        if (isStream_started) {
            if (binding.rlSaySomething.getVisibility() == View.VISIBLE) {
                hideSaySomething();
            }
            endStreamDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void endStreamDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("End Broadcast");
        builder.setMessage("Do you want to end this broadcast? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                endStream();
                deleteCurrentStreamMuted();
                deleteCurrentStreamKicks();
                deleteStreamComments();
                deleteCurrentGift();
                deleteLikes();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void deleteCurrentStreamKicks() {
        database.getReference().child(DBConstants.Kick_Users)
                .child(liveShoppingModel.getStream_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void deleteCurrentStreamMuted() {
        database.getReference().child(DBConstants.Mute_Users)
                .child(liveShoppingModel.getStream_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void deleteCurrentGift() {
        DatabaseReference gift_ref = database.getReference().child(DBConstants.Current_Gift_Stream).child(profilePOJO.getUserId()).
                child(liveShoppingModel.getStream_id());
        gift_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void deleteLikes() {
        DatabaseReference reference = database.getReference().child(DBConstants.Stream_Likes).child(liveShoppingModel.getStream_id());

        if (liveShoppingModel.getStream_id() != null) {

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        reference.getRef().removeValue();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void deleteStreamComments() {

        DatabaseReference streamRef = database.getReference().child(DBConstants.Stream_Comments);
        streamRef.child(liveShoppingModel.getStream_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Commn.myToast(context, "Ended");
                    snapshot.getRef().removeValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void endStream() {
        try {
            runOnUiThread(() -> {
                removeMyStream();
            });
            endFlashphonerPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void endFlashphonerPlayer() {
        if (session != null) {
            session.disconnect();
        }
    }

    private void endedStreamUi() {

        binding.rlSaySomething.setVisibility(View.GONE);
        binding.ivGiftStream.setVisibility(View.GONE);
        binding.rlBottomContent.setVisibility(View.GONE);
        binding.rlUserInfo.setVisibility(View.GONE);

    }

    private void removeMyStream() {
        firebaseFirestore.collection(DBConstants.LiveShopping)
                .document(liveShoppingModel.getUser_id())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        firebaseFirestore.collection(DBConstants.LiveShopping)
                                .document(liveShoppingModel.getUser_id())
                                .delete();
                        Commn.myToast(context, "Ended");
                        releaseVideo();
                        endedStreamUi();
                        isStream_started = false;
                    }
                });
    }

    private void setUserInfo() {
        binding.tvUserName.setText(profilePOJO.getName());
        Glide.with(getApplicationContext()).
                load(profilePOJO.getImage()).placeholder(R.drawable.placeholder_user)
                .into(binding.ivUserImage);


        binding.ivShareStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (isStream_started) {
                    String name = "";
                    if (profilePOJO.getName() != null) {
                        if (profilePOJO.getName().isEmpty()) {
                            name = profilePOJO.getSuper_live_id();
                        } else {
                            name = profilePOJO.getName();
                        }

                    } else {
                        name = profilePOJO.getSuper_live_id();
                    }
                    generateShareLink(name);
                }

            }
        });
    }

    private void generateShareLink(String finalName) {

        String title = finalName + " is live now";

        Commn.showDebugLog("ShareJson" + "Json Object: " + title);

        String dynamicQuery = "https://superlive.page.link" + "/?" +
                "link=" + MyApi.web_url + "/myrefer.php?" + "userid=" + liveShoppingModel.getUser_id() + "-" + DBConstants.LiveShopping +
                "&apn=" + getPackageName() +
                "&st=" + title +
                "&sd=" + getString(R.string.app_name)+
                "&si=" + profilePOJO.getImage();
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

    private void iniIntent() {

        if (getIntent().hasExtra(DBConstants.LiveShoppingModel)) {
            liveShoppingModel = new Gson().fromJson(getIntent().getStringExtra(DBConstants.LiveShoppingModel), LiveShoppingModel.class);

            Commn.showDebugLog("recieved_live_shopping_model:" + new Gson().toJson(liveShoppingModel));
            if (liveShoppingModel != null) {
                iniStream();
            }
        }
    }

    private void iniFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        stream_ref = firebaseFirestore.collection(DBConstants.LiveShopping);
        database = FirebaseDatabase.getInstance();
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void iniStream() {
        if (DBConstants.Live_Streaming_Type.equalsIgnoreCase(liveShoppingModel.getStream_type())) {
            startPublishStream();
            getDeviceEvent();
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
        if (!isONCall) {
            playStream();
        }
    }

    private void pauseVideo() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        if (!isONCall) {
            pauseStream();
        }

    }

    private void enableExoPlayerUi() {
        binding.flashphonerLayout.setVisibility(View.GONE);
        binding.exoPlayerLayout.setVisibility(View.VISIBLE);
        binding.ivFlipCamera.setVisibility(View.GONE);
    }

    private void addingStreamOnFirebase() {

        Map<String, Object> streamMap = new HashMap<>();
        streamMap.put(DBConstants.stream_title, liveShoppingModel.getStream_title() + "");
        streamMap.put(DBConstants.stream_url, liveShoppingModel.getStream_url() + "");
        streamMap.put(DBConstants.stream_id, liveShoppingModel.getStream_id() + "");
        streamMap.put(DBConstants.user_id, liveShoppingModel.getUser_id() + "");
        streamMap.put(DBConstants.country_name, liveShoppingModel.getCountry_name() + "");
        streamMap.put(DBConstants.stream_type, liveShoppingModel.getStream_type() + "");

        if (liveShoppingModel.getPRODUCT_MODEL() != null) {
            streamMap.put(Commn.PRODUCT_MODEL, liveShoppingModel.getPRODUCT_MODEL() + "");
        }

        if (liveShoppingModel.getVIDEO_MODEL() != null) {
            streamMap.put(DBConstants.VIDEO_MODEL, liveShoppingModel.getVIDEO_MODEL() + "");
        }
        stream_ref.document(liveShoppingModel.getUser_id())
                .set(streamMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Commn.showDebugLog("stream added in database:" + new Gson().toJson(streamMap));
                    }
                }).addOnFailureListener(e -> Commn.showDebugLog("stream added in database failure :" + e.getMessage() + " "));
    }

    private void enableFlashPhonerUi() {
        binding.flashphonerLayout.setVisibility(View.VISIBLE);
        binding.exoPlayerLayout.setVisibility(View.GONE);
        binding.ivFlipCamera.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {

        } else if (playbackState == Player.STATE_READY) {
            isStream_started = true;
            addingStreamOnFirebase();
            inializeJoinedUsers();
            binding.tvConnecting.setVisibility(View.GONE);
            getComments();
            pushLiveNotificatoin();
        }
    }

    private void getComments() {
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        DatabaseReference current_stream_Ref = database.getReference().child(DBConstants.Stream_Comments).
                child(liveShoppingModel.getStream_id());
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<StreamCommentModel>().setQuery(current_stream_Ref, StreamCommentModel.class).build();

        comments_adapter = new FirebaseRecyclerAdapter<StreamCommentModel, StreamCommentsHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull StreamCommentsHolder hotLiveHolder, int position, @NonNull StreamCommentModel hotLiveModel) {

                hotLiveHolder.tv_comment.setText(hotLiveModel.getStream_comment());
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvComments.setLayoutManager(layoutManager);
        binding.rvComments.setAdapter(comments_adapter);

        comments_adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int friendlyMessageCount = comments_adapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added messageeee.
                Log.e("countttmess", friendlyMessageCount + "");
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
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
                                    if (!current_level.equalsIgnoreCase("")) {
                                        holder.tv_user_level.setText("Lv" + current_level + "");

                                    }
                                }
                            }
                        }
                    }


                });
    }

    private void inializeJoinedUsers() {

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
        selected_user_id = stream_comment_user_id;
        Bundle bundle = new Bundle();
        bundle.putString(DBConstants.user_id, selected_user_id);
        bundle.putString(DBConstants.stream_id, liveShoppingModel.getStream_id());
        bundle.putString(Commn.TYPE, DBConstants.LiveShopping);
        BottomUserProfile bottomUserProfile = new BottomUserProfile();
        bottomUserProfile.setArguments(bundle);
        bottomUserProfile.show(getSupportFragmentManager(), "");
    }

    private void inViewersFirebaseOptions() {

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


    private void startPublishStream() {
        enableFlashPhonerUi();

        Flashphoner.init(activity);
        URI u = null;
        try {
            u = new URI(FlashphonerConst.BASE_URL);
        } catch (URISyntaxException e) {
            Commn.showErrorLog("URISyntaxException:" + e.getMessage() + "," + e.getReason() + "");
            e.printStackTrace();
        }

        String url = null;
        if (u != null) {
            url = u.getScheme() + "://" + u.getHost() + ":" + u.getPort();
            Commn.showErrorLog("get url:" + url + "");
        } else {
            Commn.showErrorLog("null uri:" + "");
        }

        Flashphoner.getAudioManager().setUseBluetoothSco(true);
        Flashphoner.getAudioManager().setUseSpeakerPhone(true);
        Flashphoner.getAudioManager().getAudioManager().setMicrophoneMute(false);

        SessionOptions sessionOptions = new SessionOptions(url);
        sessionOptions.setLocalRenderer(binding.localRender);
        binding.localRender.setZOrderMediaOverlay(true);
        binding.preview.setPosition(0, 0, 100, 100);
        binding.localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        binding.localRender.setMirror(false);
        binding.localRender.requestLayout();
        session = Flashphoner.createSession(sessionOptions);


        session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {

                Commn.showErrorLog("onAppData" + data.toString() + "");

            }

            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(() -> {
                    Commn.showErrorLog("my_stream_name:" + liveShoppingModel.getStream_url() + "");
                    StreamOptions streamOptions = new StreamOptions(liveShoppingModel.getStream_url());
                    VideoConstraints videoConstraints = new VideoConstraints();
                    //videoConstraints.setCameraId(((MediaDevice)mCameraSpinner.getSpinner().getSelectedItem()).getId());
                    videoConstraints.setCameraId(Flashphoner.getMediaDevices().getVideoList().get(1).getId());
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    Commn.showErrorLog("my resolution:" + "width:" + metrics.widthPixels + ",height:" + metrics.heightPixels + "");
                    videoConstraints.setResolution(metrics.widthPixels, metrics.heightPixels);
                    AudioConstraints audioConstraints = new AudioConstraints();
                    audioConstraints.setUseStereo(true);
                    audioConstraints.setUseFEC(true);
                    streamOptions.getConstraints().setVideoConstraints(videoConstraints);
                    streamOptions.setConstraints(new Constraints(audioConstraints, videoConstraints));
                    publishStream = session.createStream(streamOptions);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            publishStream.publish();
                        }
                    }, 2000);

                    publishStream.on((stream, streamStatus) -> {

                        Commn.showDebugLog("onConnected" + String.valueOf(streamStatus));
                        runOnUiThread(() -> {
                            if (StreamStatus.PUBLISHING.equals(streamStatus)) {
                                binding.rlBottomContent.setVisibility(View.VISIBLE);
                                isStream_started = true;
                                binding.tvConnecting.setText("Connected");
                                binding.tvConnecting.setVisibility(View.GONE);
                                addingStreamOnFirebase();
                            }
                        });
                        if (StreamStatus.FAILED.equals(streamStatus)) {
                            isStream_started = false;
                            switch (stream.getInfo()) {
                                case StreamStatusInfo.SESSION_DOES_NOT_EXIST:
                                    Commn.showErrorLog(streamStatus + ": Actual session does not exist");
                                    break;
                                case StreamStatusInfo.STOPPED_BY_PUBLISHER_STOP:
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
                            if (publishStream != null) {
                                publishStream.setConnectionQualityCallback(new ConnectionQualityCallback() {
                                    @Override
                                    public void onVideoRateStat(ConnectionQuality connectionQuality, double v, double v1) {
                                        Commn.showDebugLog("onVideoRateStat:" + new Gson().toJson(connectionQuality));
                                    }
                                });
                            }
                            pushLiveNotificatoin();
                            inializeJoinedUsers();
                            getComments();
                            Commn.showErrorLog(streamStatus.toString());
                        }
                    });


                });

            }

            @Override
            public void onRegistered(Connection connection) {
                Commn.showErrorLog("onRegistered" + connection.getStatus() + "");

            }

            @Override
            public void onDisconnection(final Connection connection) {
                runOnUiThread(() -> {
                    Commn.showErrorLog("onDisconnection:" + connection.toString() + "," + connection.getStatus() + "");
                    isStream_started = false;
                    binding.rlBottomContent.setVisibility(View.GONE);
                });
            }
        });
        session.connect(new Connection());
    }

    private void pushLiveNotificatoin() {
        NotificationRequest notificationRequest = new NotificationRequest();
        if (profilePOJO.getName() != null) {
            if (!profilePOJO.getName().isEmpty()) {
                notificationRequest.setContext_message("<strong>" + profilePOJO.getName() + "</strong>" + " gone live in" + getString(R.string.app_name));
            } else {
                notificationRequest.setContext_message("<strong>" + profilePOJO.getSuper_live_id() + "</strong>" + " gone live in " + getString(R.string.app_name));
            }
        } else {
            notificationRequest.setContext_message("<strong>" + profilePOJO.getSuper_live_id() + "</strong>" + " gone live in " + getString(R.string.app_name));
        }

        notificationRequest.setNotification_type(Notification_Const.IMAGE_NOTIFICATION_TYPE);
        notificationRequest.setSuper_live_id(profilePOJO.getSuper_live_id() + "");
        notificationRequest.setUser_id(profilePOJO.getUserId() + "");
        notificationRequest.setMessage(profilePOJO.getImage() + "");
        notificationRequest.setAlert_type(DBConstants.LiveShopping);
        notificationRequest.setNotification_data("");
        Sender sender = new Sender(notificationRequest, Notification_Const.global_notification_topic);
        Commn.showDebugLog("notification_send_model:" + new Gson().toJson(sender.getData()));
        new InializeNotification().sendNotification(sender);

    }

}