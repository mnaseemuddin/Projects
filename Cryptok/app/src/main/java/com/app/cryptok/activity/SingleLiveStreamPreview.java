package com.app.cryptok.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
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
import com.app.cryptok.databinding.ActivitySingleLiveStreamPreviewBinding;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.app.cryptok.go_live_module.LiveConst.STREAM_MODEL;


public class SingleLiveStreamPreview extends AppCompatActivity {

    private Session session, guest_session, guest_sessionForAll;
    private Stream playStream, myAsGuestStream, myAsGuestStreamForAll;
    private String stream_url, stream_title, stream_user_id, stream_image, stream_id, stream_user_name;
    private Context context;
    private SingleLiveStreamPreview activity;
    //comment
    private String mComment;
    private String comment_type = DBConstants.simple_type;
    //comment

    //getcomments
    private FirebaseDatabase database;
    FirebaseRecyclerOptions<StreamCommentModel> firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<StreamCommentModel, StreamCommentsHolder> comments_adapter;

    //getcomments

    //user info
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    //use info
    //follow following

    private boolean isSTREAM_READY = false;
    //stream

    private boolean isMyStream_started = false;
    String my_requested_stream_url;
    //get joined stream users
    private FirebaseRecyclerOptions<StreamViewersModel> viewers_firebase_options;
    private FirebaseRecyclerAdapter<StreamViewersModel, AllJoinedStreamHolder> viewers_adapter;
    DatabaseReference viewers_ref;
    private FirebaseRecyclerOptions<StreamViewersModel> viewers_firebase_options_bottom;
    private FirebaseRecyclerAdapter<StreamViewersModel, AllJoinedStreamHolder> viewers_adapter_bottom;
    //get joined stream users
    private FirebaseFirestore firebaseFirestore;
    //share stream
    //count view time
    private CountDownTimer stream_watch_timer;
    //
    private ActivitySingleLiveStreamPreviewBinding binding;
    public static GiftDialog giftDialog;
    private boolean isAnotherGuestAvailable = false;
    private View view;
    private DatabaseReference gift_ref;

    private boolean isShareClick = false;

    //comments
    private LinearLayoutManager comments_layout_manager;
    private boolean isScrolling = false;
    private boolean islastCommentReached = false;
    private int visible_item_count;
    private int first_visible_item;
    private int total_visible_count;

    private String last_comment_key;
    private CollectionReference user_info;
    //
    int current_recieved_beans;
    private HotLiveModel hotLiveModel;

    private CountDownTimer audio_events_countdown;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_live_stream_preview);

        context = activity = this;
        iniFirebase();
        iniViews();
        getData();
        handleClick();
        reciveGiftListener();
        getAllViewers();
        getCommentsGuidlines();
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

    private void getCommentsGuidlines() {

        firebaseFirestore.collection(DBConstants.App_Guidelines)
                .document(DBConstants.guidlines_table_key)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        if (task.getResult().getString(DBConstants.comments_rules) != null) {
                            String guidelines = task.getResult().getString(DBConstants.comments_rules);
                            binding.tvPrivacyAdvice.setText(guidelines + "");
                        }
                    }
                });

    }

    private void iniFirebase() {
        database = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


    }

    private void reciveGiftListener() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(giftBroadcast, new IntentFilter(DBConstants.SEND_GIFT));

        getGift();

    }

    private void iniViews() {

        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        commentTextChangeListener();

        iniCommentFunctionality();

        binding.ivCloseStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopMyWatchTimer();
                deleteMe();

                if (session != null) {

                    session.disconnect();
                }
                if (guest_session != null) {

                    guest_session.disconnect();
                }
                onBackPressed();
            }
        });
        binding.ivGiftStream.setOnClickListener(view -> openGift());
        setDimension();
    }

    private void deleteMe() {
        DatabaseReference reference = database.getReference().child(DBConstants.Current_Viewer)
                .child(stream_user_id)
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

    private float getVideoProportion() {
        return 1.5f;
    }

    private void setDimension() {
        float videoProportion = getVideoProportion();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float screenProportion = (float) screenHeight / (float) screenWidth;
        android.view.ViewGroup.LayoutParams lp = binding.videoFrame.getLayoutParams();
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
        binding.videoFrame.setLayoutParams(lp);
    }

    @Override
    public void onBackPressed() {
        if (binding.rlSaySomething.getVisibility() == View.VISIBLE) {
            hideSaySomething();
            deleteMe();

        } else {
            isShareClick = false;
            deleteMe();
            stopMyWatchTimer();

            super.onBackPressed();

        }
    }

    private void handleClick() {
        binding.ivAllViewers.setOnClickListener(view -> openAllViewersSheet(stream_user_id));

        binding.ivChat.setOnClickListener(view -> {
            if (isSTREAM_READY) {

                if (binding.rlSaySomething.getVisibility() == View.VISIBLE) {
                    hideSaySomething();
                } else {
                    showCommentVisible();
                }

            } else {
                Commn.myToast(context, "wait ,you not started yet....");
            }
        });

        binding.ivShareStream.setOnClickListener(v -> {
            if (FastClickUtil.isFastClick()) {
                return;
            }
            if (isSTREAM_READY) {
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
                generateShareLink(name1);
            }else {
                Commn.myToast(context, "wait ! you are not live yet");
            }

        });


        binding.ivUserImage.setOnClickListener(view ->
                openBottomSheet(stream_user_id));

        binding.guestStreamingFrame.setOnClickListener(view -> {
            if (guest_session != null) {
                if (myAsGuestStream != null) {

                    showRemoveGuestDialog();

                }
            }
        });

        binding.ivCameraVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guest_session != null) {

                    if (myAsGuestStream.isVideoMuted()) {
                        myAsGuestStream.muteVideo();
                    } else {
                        myAsGuestStream.unmuteVideo();
                    }

                }
            }
        });
        binding.ivCallStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guest_session != null) {

                    hideMyStreamAsGuestView();
                    removeGuest();

                }
            }
        });
    }
    private void generateShareLink(String name) {
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
                "link=" + MyApi.web_url + "/myrefer.php?" + "userid=" + profilePOJO.getUserId() + "-" + Commn.LIVE_TYPE +
                "&apn=" + getPackageName() +
                "&st=" + title +
                "&sd=" + getString(R.string.app_name) +
                "&si=" + stream_image;
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
                            updateMyGiftPoints(Commn.SHARE, "1", "1");
                        } else {
                            Commn.showDebugLog("error on generating dynamic url:" + task.getException().getMessage() + "");
                        }
                    }
                });
    }

    private void shareStream() {
        String share_content = "Hey someone streaming now watch it : " +
                "\n\nDownload now from Google playstore. " + Commn.play_store_base_url + getPackageName();
        Commn.showDebugLog(share_content + "");


        /*Create an ACTION_SEND Intent*/
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        /*This will be the actual content you wish you share.*/

        /*The type of the content is text, obviously.*/
        intent.setType("text/plain");
        /*Applying information Subject and Body.*/

        intent.putExtra(android.content.Intent.EXTRA_TEXT, share_content);
        /*Fire!*/
        startActivity(Intent.createChooser(intent, share_content));
    }

    int pointsToAdd = 30;
    int total_beans;
    int send_value = 6;

    private void updateMyGiftPoints(String type, String gift_quantity, String gift_value) {

        if (type.equalsIgnoreCase(Commn.SHARE)) {
            if (sessionManager.getPoints() != null) {
                pointsToAdd = sessionManager.getPoints().getOn_share();
            } else {
                pointsToAdd = 30;
            }
            Commn.showDebugLog("share type");
        }
        if (type.equalsIgnoreCase(Commn.SEND_GIFT)) {
            if (sessionManager.getPoints() != null) {
                send_value = sessionManager.getPoints().getOn_send_gift();
            } else {
                send_value = 6;
            }

            total_beans = Integer.parseInt(gift_value) * Integer.parseInt(gift_quantity);
            pointsToAdd = total_beans * send_value;
            Commn.showDebugLog("beans to be add:" + "total_beans:" + total_beans + ",gift_value:" + gift_value + "");
            Commn.showDebugLog("points to be add:" + pointsToAdd + "");
            Commn.showDebugLog("send type");
        }
        if (type.equalsIgnoreCase(Commn.WATCH_TYPE)) {
            if (sessionManager.getPoints() != null) {
                pointsToAdd = sessionManager.getPoints().getWatch_stream();
            } else {
                pointsToAdd = 30;
            }

            Commn.showDebugLog("watch type");
        }
        Commn.showDebugLog("point to add on send:" + pointsToAdd + "");
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(DBConstants.point, task.getResult().getLong(DBConstants.point).intValue() + pointsToAdd);
                        if (type.equalsIgnoreCase(Commn.SEND_GIFT)) {
                            map.put(DBConstants.total_sent_beans, task.getResult().getLong(DBConstants.total_sent_beans).intValue() + total_beans);
                            Commn.showDebugLog("point to add on send:" + map.toString() + "");
                            firebaseFirestore.collection(DBConstants.UserInfo)
                                    .document(profilePOJO.getUserId())
                                    .update(map)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            deductDiamonds(total_beans);
                                            Commn.showDebugLog("send gift points updated success:");
                                        } else {
                                            Commn.showDebugLog("send gift points not updated:");
                                        }
                                    });

                        }

                        Commn.showDebugLog("point to add:" + map.toString() + "");
                        firebaseFirestore.collection(DBConstants.UserInfo)
                                .document(profilePOJO.getUserId())
                                .update(map)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {

                                        Commn.showDebugLog("points updated success:");
                                    } else {
                                        Commn.showDebugLog(" points not updated:");
                                    }
                                });

                    }
                });

    }

    private void deductDiamonds(int gift_value) {

        //check user exists
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        //getting current coins
                        firebaseFirestore.collection(DBConstants.UserInfo
                                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                                .document(profilePOJO.getUserId())
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.getResult().exists()) {
                                        int current_coins = task1.getResult().getLong(DBConstants.current_coins).intValue();//current diamonds
                                        if (current_coins > 0 && current_coins >= gift_value) {
                                            Map<String, Object> current_coins_map = new HashMap<>();
                                            //deducting coins from current coins
                                            int toDeduct = getFinalCoins(current_coins, gift_value);
                                            current_coins_map.put(DBConstants.current_coins, toDeduct);
                                            firebaseFirestore.collection(DBConstants.UserInfo + "/" + profilePOJO.getUserId()
                                                    + "/" + DBConstants.User_Coins_Info)
                                                    .document(profilePOJO.getUserId())
                                                    .update(current_coins_map)
                                                    .addOnCompleteListener(task2 -> {

                                                        Commn.showDebugLog("dedcuted_coins_successfully:itne deduct krne they=>" +
                                                                toDeduct + ",itne m se=>" + current_coins + "");
                                                    });
                                        }

                                    }

                                });


                    }

                });


    }

    private int getFinalCoins(int current_coins, int ToAddCoins) {
        return current_coins - ToAddCoins;
    }

    private void showCommentVisible() {
        binding.rlSaySomething.setVisibility(View.VISIBLE);
        binding.etSaySomething.requestFocus();

        binding.rlBottomContent.setVisibility(View.GONE);

    }


    private void openGift() {
        try {
            giftDialog = new GiftDialog();
            Bundle bundle = new Bundle();
            bundle.putString(DBConstants.user_id, stream_user_id);
            bundle.putString(DBConstants.stream_id, stream_id);
            giftDialog.setArguments(bundle);
            giftDialog.show(getSupportFragmentManager(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void iniCommentFunctionality() {

        binding.ivSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mComment = binding.etSaySomething.getText().toString();
                comment_type = DBConstants.simple_type;
                checkCommentAuth();

            }
        });

    }

    private void checkCommentAuth() {

        database.getReference().child(DBConstants.Mute_Users)
                .child(stream_id)
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
        String currentUserRef = DBConstants.Stream_Comments + "/" + stream_id;

        DatabaseReference user_message_push = reference.child(DBConstants.Stream_Comments).
                child(stream_id).push();
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

    private void getData() {
        if (getIntent().hasExtra(STREAM_MODEL)) {
            hotLiveModel = new Gson().fromJson(getIntent().getStringExtra(STREAM_MODEL), HotLiveModel.class);
            stream_user_id = hotLiveModel.getUser_id();
            stream_url = hotLiveModel.getStream_url();
            stream_title = hotLiveModel.getStream_title();
            stream_id = hotLiveModel.getStream_id();
            firebaseFirestore.collection(DBConstants.UserInfo)
                    .document(stream_user_id)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getResult().exists()) {
                            stream_image = task.getResult().getString(DBConstants.image);
                            stream_user_name = task.getResult().getString(DBConstants.name);
                            if (stream_user_name.equals("")) {
                                stream_user_name = task.getResult().getString(DBConstants.super_live_id);
                            }
                            Glide.with(getApplicationContext()).load(stream_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivStreamThumb);
                            Commn.showDebugLog("my_stream_url" + "on recived=" + stream_url + "" + stream_id + "");
                            binding.tvUserName.setText(stream_user_name);
                            Glide.with(getApplicationContext()).load(stream_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUserImage);
                        }

                        checkInitialWatchAuth();
                        getMyCurrentPoints();

                    });


        }


        likesFunctionality();
        binding.rlContainer.setOnClickListener(view -> {

            if (binding.rlSaySomething.getVisibility() == View.VISIBLE) {
                hideSaySomething();
            }

        });

    }

    private void checkInitialWatchAuth() {
        database.getReference().child(DBConstants.Kick_Users)
                .child(stream_id)
                .child(profilePOJO.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            endedStreamUI();
                            Commn.myToast(context, "You kicked out by Host...try again in another stream");
                        } else {
                            prepareStream();
                            getRequestStreamRequest();
                            checkGuestStreamAvailable();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkGuestStreamAvailable() {
        database.getReference().child(DBConstants.SingleStreamInvitation)
                .child(stream_user_id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                        if (snapshot.exists()) {
                            if (snapshot.hasChild(DBConstants.user_id) && snapshot.hasChild(DBConstants.request_status)) {
                                if (snapshot.child(DBConstants.request_status).getValue().toString().equalsIgnoreCase("true")) {
                                    String user_id = snapshot.child(DBConstants.user_id).getValue().toString();
                                    if (!profilePOJO.getUserId().equals(user_id)) {
                                        prepareGuestStreamForAll(user_id);
                                    }

                                }

                            }

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {


                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void prepareGuestStreamForAll(String user_id) {
        hideMyStreamAsGuestView();
        showMyStreamAsGuestViewForAll();
        Flashphoner.init(this);
        Flashphoner.getAudioManager().setUseSpeakerPhone(true);

        binding.joinedVideoFrameForAll.setPosition(0, 0, 100, 100);
        binding.joinedSurfaceViewForAll.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        binding.joinedSurfaceViewForAll.setMirror(false);
        binding.joinedSurfaceViewForAll.requestLayout();

        URI u = null;
        try {
            u = new URI("ws://13.235.209.102:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String url = null;
        if (u != null) {
            url = u.getScheme() + "://" + u.getHost() + ":" + u.getPort();
        }
        SessionOptions sessionOptions = new SessionOptions(url);
        sessionOptions.setRemoteRenderer(binding.joinedSurfaceViewForAll);

        guest_sessionForAll = Flashphoner.createSession(sessionOptions);

        guest_sessionForAll.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {

            }

            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(() -> {

                    firebaseFirestore.collection(DBConstants.UserInfo)
                            .document(user_id)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.getResult() != null) {
                                    if (task.getResult().exists()) {
                                        String guest_buzo_id = task.getResult().getString(DBConstants.super_live_id);
                                        StreamOptions streamOptions = new StreamOptions(guest_buzo_id);

                                        Commn.showDebugLog("guest_buzo_id(for all):" + guest_buzo_id + "");
                                        myAsGuestStreamForAll = guest_sessionForAll.createStream(streamOptions);

                                        myAsGuestStreamForAll.on((stream, streamStatus) -> runOnUiThread(() -> {

                                            Commn.showErrorLog("myAsGuestStreamForAll" + String.valueOf(streamStatus));


                                        }));

                                        myAsGuestStreamForAll.play();
                                    }
                                }
                            });

                });

            }

            @Override
            public void onRegistered(Connection connection) {

            }

            @Override
            public void onDisconnection(final Connection connection) {
                runOnUiThread(() -> {


                    Commn.myToast(context, "Ended");
                    hideMyStreamAsGuestViewForAll();

                });
            }
        });

        guest_sessionForAll.connect(new Connection());
    }


    private void getRequestStreamRequest() {

        DatabaseReference reference = database.getReference().child(DBConstants.SingleStreamInvitation);
        reference.child(stream_user_id).child(profilePOJO.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.hasChild(DBConstants.request_status)) {
                        String status = snapshot.child(DBConstants.request_status).getValue().toString();

                        if (snapshot.hasChild(DBConstants.user_id)) {

                            String user_id = snapshot.child(DBConstants.user_id).getValue().toString();
                            if (profilePOJO.getUserId().equalsIgnoreCase(user_id)) {
                                if (status.equalsIgnoreCase("false")) {

                                    Commn.showErrorLog("requested by host:");
                                    if (!activity.isFinishing()) {
                                        //startStreambyRequest("true");
                                        showRequestDialog();
                                    }

                                }
                            }

                        }


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


    }

    private void showRequestDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(binding.getRoot().getContext());
        ad.setTitle("Invitation");
        ad.setMessage(stream_user_name + " " + "sent you request to join stream");

        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();

            }
        });

        ad.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                showMyStreamAsGuestView();
                startStreambyRequest("true");

            }
        });

        ad.create();
        ad.show();
    }


    private void startStreambyRequest(String request) {

        DatabaseReference streamRef = database.getReference().child(DBConstants.SingleStreamInvitation)
                .child(stream_user_id).child(profilePOJO.getUserId());

        streamRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    streamRef.child(DBConstants.request_status).setValue(request)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    Commn.showDebugLog("SingleStreamInvitation" + "you accpted host request...");

                                    iniMyStreamAsGuestStream();
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void iniMyStreamAsGuestStream() {

        Flashphoner.init(this);

        Flashphoner.getAudioManager().setUseSpeakerPhone(true);

        binding.guestStreamingSurfaceView.setZOrderMediaOverlay(true);

        binding.guestStreamingFrame.setPosition(0, 0, 100, 100);
        binding.guestStreamingSurfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        binding.guestStreamingSurfaceView.setMirror(true);
        binding.guestStreamingSurfaceView.requestLayout();

        URI u = null;
        try {
            u = new URI("ws://13.235.209.102:8080");
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

        SessionOptions sessionOptions = new SessionOptions(url);
        sessionOptions.setLocalRenderer(binding.guestStreamingSurfaceView);

        guest_session = Flashphoner.createSession(sessionOptions);

        guest_session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {

                Commn.showErrorLog("onAppData" + data.toString() + "");

            }


            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(() -> {

                    Commn.showErrorLog("my_stream_as_guest_name:" + profilePOJO.getSuper_live_id() + "");
                    StreamOptions streamOptions = new StreamOptions(profilePOJO.getSuper_live_id());

                    VideoConstraints videoConstraints = new VideoConstraints();
                    //videoConstraints.setCameraId(((MediaDevice)mCameraSpinner.getSpinner().getSelectedItem()).getId());
                    videoConstraints.setCameraId(Flashphoner.getMediaDevices().getVideoList().get(1).getId());
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    Commn.showErrorLog("my resolution:" + "width:" + metrics.widthPixels + ",height:" + metrics.heightPixels + "");
                    videoConstraints.setResolution(metrics.widthPixels, metrics.heightPixels);
                    videoConstraints.setQuality(200);
                    streamOptions.getConstraints().setVideoConstraints(videoConstraints);
                    myAsGuestStream = guest_session.createStream(streamOptions);
                    myAsGuestStream.publish();
                    myAsGuestStream.on((stream, streamStatus) -> {

                        Commn.showDebugLog("onConnected" + String.valueOf(streamStatus));

                        runOnUiThread(() -> {


                        });

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
                    hideMyStreamAsGuestView();
                });
            }
        });


        guest_session.connect(new Connection());


        checkMyStreamAsGuestStatus();

    }
    private void playStatus() {
        binding.tvStreamEnded.setText("Connected");
        binding.tvStreamEnded.setVisibility(View.GONE);

    }

    private void pauseStatus() {
        binding.tvStreamEnded.setVisibility(View.VISIBLE);
        binding.tvStreamEnded.setText("Broadcast Paused");
    }
    private void checkMyStreamAsGuestStatus() {
        database.getReference().child(DBConstants.SingleStreamInvitation).child(stream_user_id)
                .child(profilePOJO.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            hideMyStreamAsGuestView();
                            if (guest_session != null) {
                                guest_session.disconnect();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showRemoveGuestDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(binding.getRoot().getContext());
        ad.setTitle("Remove");
        ad.setMessage("Do You Want To disconnect ?");

        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();

            }
        });

        ad.setPositiveButton("Disconnect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                hideMyStreamAsGuestView();
                removeGuest();

            }
        });

        ad.create();
        ad.show();
    }

    private void hideMyStreamAsGuestView() {
        binding.llGuestFrame.setVisibility(View.GONE);
        binding.guestStreamingSurfaceView.setVisibility(View.GONE);
        binding.guestStreamingFrame.setVisibility(View.GONE);
    }

    private void showMyStreamAsGuestView() {
        binding.llGuestFrame.setVisibility(View.VISIBLE);
        binding.guestStreamingSurfaceView.setVisibility(View.VISIBLE);
        binding.guestStreamingFrame.setVisibility(View.VISIBLE);
    }

    private void hideMyStreamAsGuestViewForAll() {
        Commn.showDebugLog("hideMyStreamAsGuestViewForAll:" + "called");
        binding.llGuestFrameForAll.setVisibility(View.GONE);
        binding.joinedSurfaceViewForAll.setVisibility(View.GONE);
        binding.joinedVideoFrameForAll.setVisibility(View.GONE);
    }

    private void showMyStreamAsGuestViewForAll() {
        binding.llGuestFrameForAll.setVisibility(View.VISIBLE);
        binding.joinedSurfaceViewForAll.setVisibility(View.VISIBLE);
        binding.joinedVideoFrameForAll.setVisibility(View.VISIBLE);
    }

    private void removeGuest() {

        database.getReference().child(DBConstants.SingleStreamInvitation).child(stream_user_id).child(profilePOJO.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            snapshot.getRef().removeValue();
                            if (guest_session != null) {
                                guest_session.disconnect();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getMyCurrentPoints() {
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(stream_user_id)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        if (value.exists()) {

                            int total_beans = value.getLong(DBConstants.total_beans).intValue();
                            binding.tvTotalBeans.setText(String.valueOf(total_beans) + "");

                        }
                    }
                });

        //calculating stars
        firebaseFirestore.collection(DBConstants.Single_Streams)
                .document(stream_user_id)
                .addSnapshotListener((value, error) -> {
                    if (value.exists()) {
                        int current_beans = value.getLong(DBConstants.current_stream_beans).intValue();
                        Commn.showDebugLog("stream_beans:" + current_beans + "");
                        getMyCurrentStars(current_beans);
                        if (current_beans > 10000) {
                            Glide.with(getApplicationContext()).load(R.drawable.gift).into(binding.ivStar);

                        }
                    }
                });
    }

    private void getMyCurrentStars(int current_beans) {
        firebaseFirestore.collection(DBConstants.beans_stars)
                .orderBy(DBConstants.beans_value, com.google.firebase.firestore.Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo(DBConstants.beans_value, current_beans)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {

                    if (!task.getResult().isEmpty()) {
                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {

                            if (snapshot.getString(DBConstants.star_name) != null) {
                                Commn.showDebugLog("my_current_star:" + snapshot.toString() + "");
                                String my_current_star = snapshot.getString(DBConstants.star_name);

                                binding.tvCurrentRecievedStar.setText(my_current_star + "");
                            }
                            if (snapshot.getString(DBConstants.star_id) != null) {
                                String star_value = snapshot.getString(DBConstants.star_id);
                                if ("0".equalsIgnoreCase(star_value)) {

                                    if (context != null) {
                                        binding.llStar.setBackground(context.getResources().getDrawable(R.drawable.transparent_et));
                                    }


                                }
                                if ("1".equalsIgnoreCase(star_value)) {

                                    if (context != null) {
                                        binding.llStar.setBackground(context.getResources().getDrawable(R.drawable.sky_blue_bg));
                                    }

                                }
                                if ("2".equalsIgnoreCase(star_value)) {
                                    if (context != null) {
                                        binding.llStar.setBackground(context.getResources().getDrawable(R.drawable.light_purple_bg));
                                    }


                                }
                                if ("3".equalsIgnoreCase(star_value)) {
                                    if (context != null) {
                                        binding.llStar.setBackground(context.getResources().getDrawable(R.drawable.dark_pppurple));
                                    }


                                }
                                if ("4".equalsIgnoreCase(star_value)) {
                                    if (context != null) {
                                        binding.llStar.setBackground(context.getResources().getDrawable(R.drawable.light_gold_bg));
                                    }


                                }
                                if ("5".equalsIgnoreCase(star_value)) {
                                    if (context != null) {
                                        binding.llStar.setBackground(context.getResources().getDrawable(R.drawable.dark_gold_bg));
                                    }


                                }

                            }
                        }
                        Commn.showErrorLog("not coming star:" + "");
                    } else {

                        binding.tvCurrentRecievedStar.setText("5 star");
                        binding.ivStar.setColorFilter(ContextCompat.getColor(context, R.color.dark_gold),
                                android.graphics.PorterDuff.Mode.SRC_IN);


                    }

                });
    }


    @Override
    protected void onDestroy() {

        Commn.showDebugLog("offline" + "destroy");
        deleteMe();
        if (session != null) {
            session.disconnect();
        }
        if (guest_session != null) {
            guest_session.disconnect();
            removeGuest();
        }
        if (guest_sessionForAll != null) {
            guest_sessionForAll.disconnect();
        }
        super.onDestroy();
    }


    private void hideSaySomething() {
        binding.rlBottomContent.setVisibility(View.VISIBLE);
        binding.rlSaySomething.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        cancleAudioEvents();

        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(giftBroadcast);

    }
    @Override
    protected void onResume() {
        super.onResume();
        Commn.showErrorLog("called on resumed");
        if (audio_events_countdown == null) {
            getAudioSerive();
        }
    }
    private void cancleAudioEvents() {
        if (audio_events_countdown != null) {
            audio_events_countdown.cancel();
            audio_events_countdown=null;

        }
    }
    private void likesFunctionality() {


        binding.ivLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {


                addLike(true);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                addLike(false);

            }
        });

    }

    private void addLike(boolean isLike) {
        DatabaseReference reference = database.getReference().child(DBConstants.Stream_Likes).child(stream_id);
        if (isLike) {

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    reference.child(DBConstants.like_stream).setValue("true");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        reference.child(DBConstants.like_stream).setValue("false");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    private void prepareStream() {

        Flashphoner.init(this);
        Flashphoner.getAudioManager().setUseSpeakerPhone(true);
        //  binding.surfaceView.setZOrderMediaOverlay(true);

        URI u = null;
        try {
            u = new URI("ws://13.235.209.102:8080");
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

                    StreamOptions streamOptions = new StreamOptions(stream_url);
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
                            deleteMe();
                            switch (stream.getInfo()) {
                                case StreamStatusInfo.SESSION_DOES_NOT_EXIST:
                                    Commn.showErrorLog(streamStatus + ": Actual session does not exist");
                                    endedStreamUI();

                                    break;
                                case StreamStatusInfo.STOPPED_BY_PUBLISHER_STOP:
                                    pauseStatus();
                                    endedStreamUI();
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
                            binding.progressBar.setVisibility(View.GONE);
                            binding.tvPrivacyAdvice.setVisibility(View.VISIBLE);

                            playStatus();
                            startedStreamUI();
                            Commn.showDebugLog("player_status" + "stateREADY" + "");
                            isSTREAM_READY = true;
                            addJoinStream();
                            joinedComment();
                            checkWatchAuth();
                            startMyWatchTimer();
                            Commn.showDebugLog("stream not failed");
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
                    //  Commn.myToast(context, "Ended");

                    endedStreamUI();
                    stopMyWatchTimer();
                    binding.guestStreamingFrame.setVisibility(View.GONE);


                });
            }
        });
        binding.videoFrame.setPosition(0, 0, 100, 100);
        binding.surfaceView.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        binding.surfaceView.setMirror(false);
        binding.surfaceView.requestLayout();


        getComments();

    }

    private void checkWatchAuth() {
        database.getReference().child(DBConstants.Kick_Users)
                .child(stream_id)
                .child(profilePOJO.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            endedStreamUI();
                            Commn.myToast(context, "You kicked out by Host...try again in another stream");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void joinedComment() {
        mComment = " Joined your broadcast.";
        comment_type = DBConstants.joined_type;
        startComment(profilePOJO.getName());
    }


    private void getComments() {
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        DatabaseReference current_stream_Ref = database.getReference().child(DBConstants.Stream_Comments).child(stream_id);
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
        Log.d("streamm_idd", stream_id + "");

    }

    Query query;

    private void iniCommentsOptions(boolean isAdded) {

        DatabaseReference current_stream_Ref = database.getReference().child(DBConstants.Stream_Comments).child(stream_id);

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

    private void openBottomSheet(String stream_comment_user_id) {
        Bundle bundle = new Bundle();
        bundle.putString(DBConstants.user_id, stream_comment_user_id);
        BottomUserForStreamView bottomUserProfile = new BottomUserForStreamView();
        bottomUserProfile.setArguments(bundle);
        bottomUserProfile.show(getSupportFragmentManager(), "");
    }


    private void stopMyWatchTimer() {
        if (stream_watch_timer != null) {
            stream_watch_timer.cancel();
        }

    }

    private void endedStreamUI() {
        binding.tvStreamEnded.setVisibility(View.VISIBLE);
        binding.ivStreamThumb.setVisibility(View.VISIBLE);
        binding.rlSaySomething.setVisibility(View.GONE);
        binding.ivGiftStream.setVisibility(View.GONE);

        binding.rlBottomContent.setVisibility(View.GONE);
        binding.rlUserInfo.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.GONE);
        if (guest_session != null) {

            guest_session.disconnect();
            hideMyStreamAsGuestView();
        }
        if (guest_sessionForAll != null) {

            guest_sessionForAll.disconnect();
            hideMyStreamAsGuestViewForAll();
        }
        stopMyWatchTimer();

    }


    private void startedStreamUI() {
        binding.tvStreamEnded.setVisibility(View.GONE);
        binding.ivStreamThumb.setVisibility(View.GONE);
        //  rl_say_something.setVisibility(View.VISIBLE);
        binding.ivGiftStream.setVisibility(View.VISIBLE);

        binding.rlUserInfo.setVisibility(View.VISIBLE);

    }

    private long min_watch_time = 300000;

    private void startMyWatchTimer() {
        if (stream_watch_timer == null) {
            if (sessionManager.getPoints() != null) {
                min_watch_time = Long.parseLong(sessionManager.getPoints().getMin_watch_time());
            } else {
                min_watch_time = 300000;
            }
            stream_watch_timer = new CountDownTimer(min_watch_time, 1000) {
                @Override
                public void onTick(long l) {

                    Commn.showDebugLog("my_view_time:" + String.valueOf(l) + "");
                }

                @Override
                public void onFinish() {
                    Commn.showDebugLog("now yo are elegible to get points on watch stream:" + "");

                    updateMyGiftPoints(Commn.WATCH_TYPE, "1", "1");
                }
            }.start();
        }

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
        viewers_ref = database.getReference().child(DBConstants.Current_Viewer).child(stream_user_id);
        viewers_firebase_options = new FirebaseRecyclerOptions.Builder<StreamViewersModel>().setQuery(viewers_ref, StreamViewersModel.class).build();

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

    private void addJoinStream() {
        DatabaseReference reference = database.getReference();
        String ref = DBConstants.Current_Viewer + "/" + stream_user_id + "/" + profilePOJO.getUserId();
        Map map = new HashMap();
        map.put(DBConstants.user_id, profilePOJO.getUserId());
        map.put(DBConstants.user_name, profilePOJO.getName());
        map.put(DBConstants.user_image, profilePOJO.getImage());
        map.put(DBConstants.stream_id, hotLiveModel.getStream_id() + "");
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
        GoLiveModel goLiveModel = new Gson().fromJson(hotLiveModel.getGO_LIVE_PARAMS(), GoLiveModel.class);
        Map<String, Object> map = new HashMap<>();
        map.put(DBConstants.stream_id, hotLiveModel.getStream_id());
        map.put(DBConstants.user_id, hotLiveModel.getUser_id());
        map.put(DBConstants.timestamp, FieldValue.serverTimestamp());
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .collection(DBConstants.my_all_joined_broadcast)
                .document(hotLiveModel.getStream_id())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (!task.getResult().exists()) {
                            firebaseFirestore.collection(DBConstants.UserInfo)
                                    .document(profilePOJO.getUserId())
                                    .collection(DBConstants.my_all_joined_broadcast)
                                    .document(hotLiveModel.getStream_id())
                                    .set(map)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            if (LiveConst.PRIVATE_STREAM.equalsIgnoreCase(goLiveModel.getStream_type())) {
                                                deductDiamondsForPrivateStream(goLiveModel.getEntry_diamonds());
                                            }
                                            Commn.showDebugLog("i am added successfully in broadcast...");
                                        }
                                    });
                        }
                    }


                });

    }

    private void deductDiamondsForPrivateStream(String entry_diamonds) {
        Commn.showDebugLog("gone to deduct diamonds for join the broadcast:to deduct;-" +
                "" + entry_diamonds);
        deductDiamonds(Integer.parseInt(entry_diamonds));
    }





    private void addStreamURL() {
        DatabaseReference streamRef = database.getReference().child(DBConstants.SingleStreamInvitation)
                .child(stream_user_id);

        streamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    streamRef.child(DBConstants.stream_url).setValue(my_requested_stream_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Commn.showDebugLog("my_stream_url_added");
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public BroadcastReceiver giftBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                if (intent.getAction().equalsIgnoreCase(DBConstants.SEND_GIFT)) {

                    if (intent.hasExtra(DBConstants.SEND_GIFT)) {

                        String image = intent.getStringExtra(DBConstants.SEND_GIFT);
                        String QUANTITY = intent.getStringExtra(DBConstants.SEND_GIFT_QUANTITY);
                        String SEND_GIFT_VALUE = intent.getStringExtra(DBConstants.SEND_GIFT_VALUE);

                        binding.llGiftLayout.setVisibility(View.VISIBLE);
                        binding.tvGiftQuantity.setText(QUANTITY + "");
                        Commn.showDebugLog("got gift:" + image + "");
                        mComment = " Sent" + " a gift x " + QUANTITY;
                        comment_type = DBConstants.gift_type;
                        if (TextUtils.isEmpty(profilePOJO.getName())) {
                            startComment(profilePOJO.getSuper_live_id());
                        } else {
                            startComment(profilePOJO.getName());
                        }

                        Glide.with(getApplicationContext()).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivRecieveGift);
                        startAnimation();

                        updateMyGiftPoints(Commn.SEND_GIFT, QUANTITY, SEND_GIFT_VALUE);


                    }

                }
            }

        }
    };

    private void getGift() {

        Commn.showDebugLog("stream_id:" + stream_id + ",stream_user_id:" + stream_user_id + "");
        gift_ref = database.getReference().child(DBConstants.Current_Gift_Stream).child(stream_user_id).child(stream_id);
        gift_ref.limitToLast(2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        binding.llGiftLayout.setVisibility(View.VISIBLE);
                        if (dataSnapshot.hasChild(DBConstants.gift)) {
                            String gift = dataSnapshot.child(DBConstants.gift).getValue().toString();
                            String quantity = dataSnapshot.child(DBConstants.gift_quantity).getValue().toString();
                            String user_name = dataSnapshot.child(DBConstants.user_name).getValue().toString();
                            Commn.showDebugLog("gift_recieved:" + gift);
                            binding.tvGiftQuantity.setText(quantity + "");
                            Glide.with(getApplicationContext()).load(gift).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivRecieveGift);
                            startAnimation();

                        }
                    }

                } else {

                    Commn.showDebugLog("gift not recieved:");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startAnimation() {
        ViewAnimator.animate(view, binding.llGiftLayout)
                .flash()
                .start();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.llGiftLayout.setVisibility(View.GONE);
            }
        }, 6000);
    }


}