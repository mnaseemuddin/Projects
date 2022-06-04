package com.app.cryptok.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.flashphoner.fpwcsapi.handler.CameraSwitchHandler;
import com.flashphoner.fpwcsapi.session.Session;
import com.flashphoner.fpwcsapi.session.SessionEvent;
import com.flashphoner.fpwcsapi.session.SessionOptions;
import com.flashphoner.fpwcsapi.session.Stream;
import com.flashphoner.fpwcsapi.session.StreamOptions;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.Notifications.InializeNotification;
import com.app.cryptok.Notifications.NotificationRequest;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.Notifications.Sender;
import com.app.cryptok.R;
import com.app.cryptok.adapter.LiveStreamAdapter.AllJoinedStreamHolder;
import com.app.cryptok.databinding.ActivityLiveBinding;
import com.app.cryptok.fragment.HostGiftSheet;
import com.app.cryptok.fragment.Profile.BottomUserProfile;
import com.app.cryptok.fragment.stream.AllViewersForHost;
import com.app.cryptok.go_live_module.LiveConst;
import com.app.cryptok.go_live_module.Model.GoLiveModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.model.StreamCommentModel;
import com.app.cryptok.model.StreamViewersModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.FastClickUtil;
import com.app.cryptok.utils.FlashphonerConst;
import com.app.cryptok.utils.Permissions;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.view_live_module.StreamCommentsHolder;

import org.webrtc.RendererCommon;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveActivity extends AppCompatActivity {

    private Context context;
    private LiveActivity activity;

    private String mComment, comment_type = "simple";
    //comment

    //user info
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    //use info
    //stream

    private boolean isStream_started = false;

    private String STREAM_NAME, Stream_Title, stream_id;
    private GoLiveModel goLiveModel;
    //stream

    //firebase
    private FirebaseDatabase database;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap bitmap;
    FirebaseRecyclerOptions<StreamCommentModel> firebaseRecyclerOptions;
    FirebaseRecyclerAdapter<StreamCommentModel, StreamCommentsHolder> comments_adapter;
    //invitation request

    //
    private boolean cancel_type = false;
    //my info
    DatabaseReference viewers_ref;
    private FirebaseRecyclerOptions<StreamViewersModel> viewers_firebase_options;
    private FirebaseRecyclerAdapter<StreamViewersModel, AllJoinedStreamHolder> viewers_adapter;
    private String selected_user_id = "";
    //
    //follow following
    private BottomSheetDialog bottomSheetDialog;

    //get gift
    private DatabaseReference gift_ref;
    private View view;
    public static HostGiftSheet giftDialog;

    private ActivityLiveBinding binding;
    int current_points;
    int current_stream_points;

    int current_recieved_beans;
    String gift_value = "5";

    //stream ini
    private Session session, guest_session;
    private Stream publishStream, myAsGuestStream;
    private String guest_buzo_id = "";
    //
    private CollectionReference user_info;
    public static boolean isPublicStream = false;
    PhoneStateListener phoneStateListener;
    private boolean isONCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live);
        context = activity = this;
        sessionManager = new SessionManager(activity);
        iniFirebase();
        getData();
        iniViews();
        setInfo();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                checkStreamPermission();
            }
        }, 500);

        getDeviceEvent();
    }

    private void getDeviceEvent() {
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    //Incoming call: Pause music
                    Commn.showDebugLog("Incoming call:" + incomingNumber);
                    pauseStream();
                    isONCall = true;

                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    Commn.showDebugLog("Not in call");
                    playStream();
                    isONCall = false;
                    //Not in call: Play music
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    //A call is dialing, active or on hold
                    pauseStream();
                    Commn.showDebugLog("A call is dialing, active or on hold");
                    isONCall = true;
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

    private void iniFirebase() {
        database = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void openGift() {
        try {
            giftDialog = new HostGiftSheet();
            giftDialog.show(getSupportFragmentManager(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getGift() {

        gift_ref = database.getReference().child(DBConstants.Current_Gift_Stream).child(profilePOJO.getUserId()).child(stream_id);
        gift_ref.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        binding.llGiftLayout.setVisibility(View.VISIBLE);
                        if (dataSnapshot.hasChild(DBConstants.gift)) {
                            String gift = dataSnapshot.child(DBConstants.gift).getValue().toString();
                            if (dataSnapshot.hasChild(DBConstants.gift_quantity)) {
                                String gift_quantity = dataSnapshot.child(DBConstants.gift_quantity).getValue().toString();
                                binding.tvGiftQuantity.setText(gift_quantity);

                                if (dataSnapshot.hasChild(DBConstants.gift_value)) {
                                    gift_value = dataSnapshot.child(DBConstants.gift_value).getValue().toString();
                                }
                                Commn.showErrorLog("Current_Gift_Stream:" + gift_quantity + ", " + gift + "");
                                Commn.showDebugLog("gift_recieved:" + gift);
                                Glide.with(getApplicationContext()).load(gift).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivRecieveGift);

                                startAnimation();
                                updateMyGiftPoints(gift_quantity, Commn.RECIEVE_GIFT);
                            }


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

    int pointsToAdd;
    int beansToAdd;
    int recieve_value = 3;

    private void updateMyGiftPoints(String quantity, String type) {

        if (type.equalsIgnoreCase(Commn.SHARE)) {

            if (sessionManager.getPoints() != null) {
                pointsToAdd = sessionManager.getPoints().getOn_share();
            } else {
                pointsToAdd = 30;
            }

        }
        if (type.equalsIgnoreCase(Commn.RECIEVE_GIFT)) {
            if (sessionManager.getPoints() != null) {
                recieve_value = sessionManager.getPoints().getOn_recieve_gift();
            } else {
                recieve_value = 3;
            }


            //beans calculation
            beansToAdd = Integer.parseInt(gift_value) * Integer.parseInt(quantity);
            current_recieved_beans = current_recieved_beans + beansToAdd;
            //beans calculation
            //points calculation
            pointsToAdd = beansToAdd * recieve_value;
            //points calculation
            current_stream_points = current_stream_points + pointsToAdd;
            Commn.showDebugLog("points to be add:" + pointsToAdd + "");
            Commn.showDebugLog("beans to be add:" + beansToAdd + ",gift_value:" + gift_value + "");
            Commn.showDebugLog("current_stream_points:" + current_stream_points + "");
            Commn.showDebugLog("current_recieved_beans:" + current_recieved_beans + "");

        }

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(DBConstants.point, task.getResult().getLong(DBConstants.point).intValue() + pointsToAdd);

                        if (type.equalsIgnoreCase(Commn.RECIEVE_GIFT)) {
                            Commn.showDebugLog("recieve gift type");
                            map.put(DBConstants.total_beans, task.getResult().getLong(DBConstants.total_beans).intValue() + beansToAdd);//total recieved beans(beans)
                            firebaseFirestore.collection(DBConstants.Single_Streams)
                                    .document(profilePOJO.getUserId())
                                    .get()
                                    .addOnCompleteListener(stream_task -> {
                                        if (stream_task.getResult().exists()) {
                                            //adding current recieved beans
                                            Map<String, Object> current_beans = new HashMap<>();
                                            current_beans.put(DBConstants.current_stream_beans,
                                                    stream_task.getResult().getLong(DBConstants.current_stream_beans).intValue() + beansToAdd);
                                            Commn.showDebugLog("current recieved beans_params:" + new Gson().toJson(current_beans).toString() + " ");
                                            //update current recieved beans
                                            firebaseFirestore.collection(DBConstants.Single_Streams)
                                                    .document(profilePOJO.getUserId())
                                                    .update(current_beans)
                                                    .addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            Commn.showDebugLog("updated current recieved beans success:");
                                                        } else {
                                                            Commn.showDebugLog("current recieved beans not updated:");
                                                        }
                                                    });
                                        }
                                    });

                            updatingCurrentBeans(beansToAdd);

                        }
                        Commn.showDebugLog("update_points_params:" + new Gson().toJson(map).toString() + " ");
                        firebaseFirestore.collection(DBConstants.UserInfo)
                                .document(profilePOJO.getUserId())
                                .update(map)//updating all beans
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {

                                        Commn.showDebugLog("recieved gift points updated success:");
                                    } else {
                                        Commn.showDebugLog("recieved gift points not updated:");
                                    }
                                });


                    }
                });

    }

    private void updatingCurrentBeans(int beansToAdd) {
        Commn.showDebugLog("updating in current beans:" + beansToAdd + "");
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
                                        //updating record
                                        int current_beans = task1.getResult().getLong(DBConstants.current_recieved_beans).intValue();//current diamonds

                                        Map<String, Object> current_coins_map = new HashMap<>();
                                        //deducting coins from current coins
                                        int toUpdate = updateFinalBeans(current_beans, beansToAdd);

                                        current_coins_map.put(DBConstants.current_recieved_beans, toUpdate);
                                        firebaseFirestore.collection(DBConstants.UserInfo + "/" + profilePOJO.getUserId()
                                                + "/" + DBConstants.User_Coins_Info)
                                                .document(profilePOJO.getUserId())
                                                .update(current_coins_map)
                                                .addOnCompleteListener(task2 -> {
                                                    Commn.showDebugLog("updated_beans_successfully:" +
                                                            "current_beans=>" + current_beans + ",beansToAdd=>" + beansToAdd + "=" + toUpdate + "");

                                                });
                                    } else {
                                        //creating record
                                        Map<String, Object> current_coins_map = new HashMap<>();
                                        current_coins_map.put(DBConstants.current_recieved_beans, beansToAdd);
                                        firebaseFirestore.collection(DBConstants.UserInfo + "/" + profilePOJO.getUserId()
                                                + "/" + DBConstants.User_Coins_Info)
                                                .document(profilePOJO.getUserId())
                                                .update(current_coins_map)
                                                .addOnCompleteListener(task2 -> {
                                                    Commn.showDebugLog("added_beans_successfully:ye table exist nahi thi,abhi create ki h humne");

                                                });
                                    }


                                });


                    }

                });

    }

    private int updateFinalBeans(int current_coins, int beansToAdd) {
        return current_coins + beansToAdd;
    }

    private void startAnimation() {
        ViewAnimator.animate(view, binding.llGiftLayout)
                .flash()
                .start();

        new Handler(Looper.getMainLooper()).postDelayed(() ->
                binding.llGiftLayout.setVisibility(View.GONE), 6000);
    }

    private void setInfo() {
        binding.tvUserName.setText(profilePOJO.getName());
        Glide.with(getApplicationContext()).load(profilePOJO.getImage()).placeholder(R.drawable.placeholder_user).into(binding.ivUserImage);

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
                } else {
                    Commn.myToast(context, "wait ! you are not live yet");
                }

            }
        });

    }

    private void generateShareLink(String finalName) {

        String title = finalName + " is live now";
        Commn.showDebugLog("ShareJson" + "Json Object: " + title);
        String dynamicQuery = "https://superlive.page.link" + "/?" +
                "link=" + MyApi.web_url + "/myrefer.php?" + "userid=" + profilePOJO.getUserId() + "-" + Commn.LIVE_TYPE +
                "&apn=" + getPackageName() +
                "&st=" + title +
                "&sd=" + getString(R.string.app_name) +
                "&si=" + profilePOJO.getImage();
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(dynamicQuery))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, task -> {
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
                });
    }

    private void getData() {
        if (getIntent().hasExtra(LiveConst.GO_LIVE_PARAMS)) {
            if (getIntent().getStringExtra(LiveConst.GO_LIVE_PARAMS) != null) {
                goLiveModel = new Gson().fromJson(getIntent().getStringExtra(LiveConst.GO_LIVE_PARAMS), GoLiveModel.class);
                Stream_Title = goLiveModel.getStream_title();
                if (Stream_Title == null) {
                    Stream_Title = "";
                }

                if (goLiveModel == null) {
                    return;
                }
                if (goLiveModel.getStream_type().equalsIgnoreCase(LiveConst.PUBLIC_STREAM)) {
                    isPublicStream = true;
                } else {
                    isPublicStream = false;
                }
            }

        }
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        stream_id = String.valueOf(System.currentTimeMillis());
        getMyCurrentPoints();
        getAcceptedRequest();
    }

    private void getMyCurrentPoints() {
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        if (value.exists()) {

                            current_points = value.getLong(DBConstants.point).intValue();
                            int total_beans = value.getLong(DBConstants.total_beans).intValue();
                            binding.tvTotalBeans.setText(String.valueOf(total_beans) + "");

                        }
                    }
                });

        //calculating stars
        firebaseFirestore.collection(DBConstants.Single_Streams)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {
                    if (value.exists()) {
                        if (value.getLong(DBConstants.current_stream_beans) != null) {
                            int current_beans = value.getLong(DBConstants.current_stream_beans).intValue();
                            binding.tvCurrentPoints.setText("Total Recieved Beans : " + String.valueOf(current_beans) + "");
                            getMyCurrentStars(current_beans);
                            if (current_beans > 10000) {

                                Glide.with(getApplicationContext()).load(R.drawable.gift).into(binding.ivStar);

                            }
                        }

                    }
                });


    }

    private void getMyCurrentStars(int current_beans) {
        firebaseFirestore.collection(DBConstants.beans_stars)
                .orderBy(DBConstants.beans_value, Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo(DBConstants.beans_value, current_beans)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
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

                            } else {
                                binding.tvCurrentRecievedStar.setText("5 Star");
                                binding.llStar.setBackground(context.getResources().getDrawable(R.drawable.dark_gold_bg));
                            }

                        }

                    }
                });
    }


    private void checkStreamPermission() {
        TedPermission.with(context)
                .setPermissionListener(stream_permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Permissions.permissions)
                .check();

    }

    PermissionListener stream_permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            iniStream();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {

            checkStreamPermission();
        }
    };

    private void iniStream() {


        startStream();


        getComments();

    }

    private void startStream() {

        Flashphoner.init(this);
        Flashphoner.getAudioManager().setUseSpeakerPhone(true);
        binding.localRender.setZOrderMediaOverlay(true);
        binding.preview.setPosition(0, 0, 100, 100);
        binding.localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        binding.localRender.setMirror(true);
        binding.localRender.requestLayout();

        startPublish();
    }

    private void startPublish() {
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

        SessionOptions sessionOptions = new SessionOptions(url);
        sessionOptions.setLocalRenderer(binding.localRender);

        session = Flashphoner.createSession(sessionOptions);
        session.connect(new Connection());
        session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {

                Commn.showErrorLog("onAppData" + data.toString() + "");

            }

            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(() -> {

                    Commn.showErrorLog("my_stream_name:" + stream_id + "");
                    StreamOptions streamOptions = new StreamOptions(stream_id);
                    VideoConstraints videoConstraints = new VideoConstraints();
                    //videoConstraints.setCameraId(((MediaDevice)mCameraSpinner.getSpinner().getSelectedItem()).getId());
                    videoConstraints.setCameraId(Flashphoner.getMediaDevices().getVideoList().get(1).getId());
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    Commn.showErrorLog("my resolution:" + "width:" + metrics.widthPixels + ",height:" + metrics.heightPixels + "");

                    videoConstraints.setResolution(metrics.widthPixels, metrics.heightPixels);
                    streamOptions.getConstraints().setVideoConstraints(videoConstraints);
                    streamOptions.setConstraints(new Constraints(new AudioConstraints(), videoConstraints));

                    publishStream = session.createStream(streamOptions);
                    publishStream.publish();
                    publishStream.on((stream, streamStatus) -> {

                        Commn.showDebugLog("onConnected" + String.valueOf(streamStatus));

                        runOnUiThread(() -> {

                            if (StreamStatus.PUBLISHING.equals(streamStatus)) {
                                binding.rlBottomContent.setVisibility(View.VISIBLE);
                                isStream_started = true;
                                binding.tvConnecting.setText("Connected");
                                STREAM_NAME = profilePOJO.getSuper_live_id();
                                Commn.showDebugLog("MY_URL" + STREAM_NAME + "");
                                addStreamStartedTime();
                                binding.tvConnecting.setVisibility(View.GONE);
                                addStreamFirebase();
                                getStreamViewer();

                                if (isStream_started) {
                                    getGift();
                                }
                                getLikes();
                            }


                        });

                        if (StreamStatus.FAILED.equals(streamStatus)) {

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
                            pushLiveNotificatoin();
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
                    Commn.showErrorLog("onDisconnectionFull:"+new Gson().toJson(connection));
                    isStream_started = false;
                    endStream();
                    binding.rlBottomContent.setVisibility(View.GONE);
                });
            }
        });





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
        notificationRequest.setAlert_type(Commn.LIVE_TYPE);
        notificationRequest.setNotification_data("");
        Sender sender = new Sender(notificationRequest, Notification_Const.global_notification_topic);
        Commn.showDebugLog("notification_send_model:" + new Gson().toJson(sender.getData()));
        new InializeNotification().sendNotification(sender);

    }


    private void iniViews() {

        binding.ivCloseStream.setOnClickListener(view -> {

            if (!isStream_started) {
                onBackPressed();
            } else {
                endStreamDialog();
            }


        });

        commentTextChangeListener();

        iniCommentFunctionality();

        handleClicks();

    }

    private void handleClicks() {


        binding.ivAllViewers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AllViewersForHost fragment = new AllViewersForHost();
                Bundle bundle = new Bundle();
                bundle.putString(DBConstants.user_id, profilePOJO.getUserId());

                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "");

                //getAllViewersList();

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

        binding.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.rlSaySomething.getVisibility() == View.VISIBLE) {
                    hideSaySomething();
                }
            }
        });


        binding.ivGiftStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGift();
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

        binding.joinedVideoFrame.setOnClickListener(view -> {
            if (guest_session != null) {
                if (myAsGuestStream != null) {

                    showRemoveGuestDialog();

                }
            }
        });


    }

    private void showRemoveGuestDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(binding.getRoot().getContext());
        ad.setTitle("Remove Guest");
        ad.setMessage("Do You Want To remove Guest from your stream?");

        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();

            }
        });

        ad.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();

                hideGuestView();

                removeGuest();

            }
        });

        ad.create();
        ad.show();
    }

    private void hideGuestView() {
        binding.llGuestFrame.setVisibility(View.GONE);
        binding.joinedVideoFrame.setVisibility(View.GONE);
        binding.joinedSurfaceView.setVisibility(View.GONE);
    }

    private void showGuestView() {
        binding.llGuestFrame.setVisibility(View.VISIBLE);
        binding.joinedVideoFrame.setVisibility(View.VISIBLE);
        binding.joinedSurfaceView.setVisibility(View.VISIBLE);
    }

    private void removeGuest() {

        database.getReference().child(DBConstants.SingleStreamInvitation).child(profilePOJO.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void showCommentVisible() {
        binding.rlSaySomething.setVisibility(View.VISIBLE);
        binding.etSaySomething.requestFocus();

        binding.rlBottomContent.setVisibility(View.GONE);

    }


    private void iniCommentFunctionality() {

        binding.ivSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComment = binding.etSaySomething.getText().toString();
                comment_type = DBConstants.simple_type;

                startComment();

            }
        });

    }

    private void startComment() {
        if (isStream_started) {

            pushComment();

        } else {
            Commn.myToast(context, "please wait broadcast not started yet...");
        }
    }

    private void pushComment() {

        DatabaseReference reference = database.getReference();
        String currentUserRef = DBConstants.Stream_Comments + "/" + stream_id;


        DatabaseReference user_message_push = reference.child(DBConstants.Stream_Comments).
                child(stream_id).push();
        String push_id = user_message_push.getKey();
        Map messageMap = new HashMap();
        messageMap.put(DBConstants.stream_cmnt_id, push_id);
        messageMap.put(DBConstants.stream_comment, mComment);
        messageMap.put(DBConstants.stream_comment_user_name, profilePOJO.getName());
        messageMap.put(DBConstants.stream_comment_user_id, profilePOJO.getUserId());
        messageMap.put(DBConstants.comment_type, comment_type);


        Map messageUserMap = new HashMap();
        messageUserMap.put(currentUserRef + "/" + push_id, messageMap);
        binding.etSaySomething.setText("");
        reference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Log.d("comment_check", "comment_added");

                if (error != null) {
                    Log.d("comment_check", "error=" + error.getMessage() + "");
                }
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


    private void endStream() {
        try {
            runOnUiThread(() -> {
                removeMyStream();
                deleteCurrentStreamMuted();
                deleteCurrentStreamKicks();
                deleteStreamComments();
                removeInvitation();
            });
            if (session != null) {
                session.disconnect();

            }
            if (guest_session != null) {
                guest_session.disconnect();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        deleteCurrentGift();
        endedStreamUI();

    }

    private void removeInvitation() {
        database.getReference().child(DBConstants.SingleStreamInvitation)
                .child(profilePOJO.getUserId())
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

    private void removeMyStream() {

        firebaseFirestore.collection(DBConstants.Single_Streams)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        firebaseFirestore.collection(DBConstants.Single_Streams)
                                .document(profilePOJO.getUserId())
                                .delete();
                        Commn.myToast(context, "Ended");
                        deleteLikes();
                    }
                });
        firebaseFirestore.collection(DBConstants.private_stream)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        firebaseFirestore.collection(DBConstants.private_stream)
                                .document(profilePOJO.getUserId())
                                .delete();
                        deleteLikes();
                    }
                });
    }

    private void deleteCurrentStreamKicks() {
        database.getReference().child(DBConstants.Kick_Users)
                .child(stream_id)
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
                .child(stream_id)
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
        DatabaseReference gift_ref = database.getReference().child(DBConstants.Current_Gift_Stream).child(profilePOJO.getUserId()).child(stream_id);
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
        DatabaseReference reference = database.getReference().child(DBConstants.Stream_Likes).child(stream_id);

        if (stream_id != null) {

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
        streamRef.child(stream_id).addListenerForSingleValueEvent(new ValueEventListener() {
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


    private void addStreamStartedTime() {

        sessionManager.saveStreamStarted(System.currentTimeMillis());
    }


    private void getStreamViewer() {
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

    private void inViewersFirebaseOptions() {
        binding.rvStreamingViewers.setHasFixedSize(true);
        binding.rvStreamingViewers.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewers_ref = database.getReference().child(DBConstants.Current_Viewer).child(profilePOJO.getUserId());
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

    private void getLikes() {

        if (stream_id != null) {
            DatabaseReference reference = database.getReference().child(DBConstants.Stream_Likes).child(stream_id);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String like_status = snapshot.child(DBConstants.like_stream).getValue().toString();
                        if (like_status.equalsIgnoreCase("true")) {

                            visibleBigIcon();
                            Log.d("like_status", "true");
                        } else {
                            hideBigIcon();
                            Log.d("like_status", "false");
                        }

                    } else {
                        Log.d("like_status", "snapshot not exists");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void hideBigIcon() {
        binding.ivBigLike.setVisibility(View.GONE);
    }

    private void visibleBigIcon() {
        binding.ivBigLike.setVisibility(View.VISIBLE);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.ivBigLike.setVisibility(View.GONE);
            }
        }, 2000);

    }

    private void getComments() {
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        DatabaseReference current_stream_Ref = database.getReference().child(DBConstants.Stream_Comments).child(stream_id);
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

    private void shareStream() {

        updateMyGiftPoints("1", Commn.SHARE);
        String share_content = "Hey someone streaming now watch it : " + STREAM_NAME +
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

    private void openBottomSheet(String stream_comment_user_id) {
        selected_user_id = stream_comment_user_id;
        Bundle bundle = new Bundle();
        bundle.putString(DBConstants.user_id, stream_comment_user_id);
        bundle.putString(DBConstants.stream_id, stream_id);
        bundle.putString(Commn.TYPE, "");
        BottomUserProfile bottomUserProfile = new BottomUserProfile();
        bottomUserProfile.setArguments(bundle);
        bottomUserProfile.show(getSupportFragmentManager(), "");
    }


    private void addStreamFirebase() {
        updateStreamInfo();

        //addNotification();

    }

    /* private void addNotification() {
         Map<String,Object>map=new HashMap<>();
         map.put(DBConstants.user_id,profilePOJO.getUserId());
         map.put(DBConstants.stream_id,stream_id);
         map.put(DBConstants.stream_url,STREAM_URL);
         FirebaseFirestore.getInstance().collection(DBConstants.Notification)
                // .add()

     }*/
    CollectionReference stream_ref;

    private void updateStreamInfo() {

        if (goLiveModel != null) {
            if (LiveConst.PUBLIC_STREAM.equalsIgnoreCase(goLiveModel.getStream_type())) {
                stream_ref = firebaseFirestore.collection(DBConstants.Single_Streams);
            } else {
                stream_ref = firebaseFirestore.collection(DBConstants.private_stream);
            }
        } else {
            stream_ref = firebaseFirestore.collection(DBConstants.Single_Streams);
        }
        Map<String, Object> streamMap = new HashMap<>();
        streamMap.put(DBConstants.stream_url, stream_id + "");
        streamMap.put(DBConstants.stream_title, Stream_Title + "");
        streamMap.put(DBConstants.stream_id, stream_id + "");
        streamMap.put(DBConstants.current_stream_beans, 0);
        streamMap.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        streamMap.put(DBConstants.country_name, profilePOJO.getCountry_name() + "");
        if (goLiveModel != null) {
            streamMap.put(LiveConst.GO_LIVE_PARAMS, new Gson().toJson(goLiveModel) + "");
        }

        stream_ref.document(profilePOJO.getUserId())
                .set(streamMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Commn.showDebugLog("stream added in database:" + new Gson().toJson(streamMap));

                    }
                }).addOnFailureListener(e -> Commn.showDebugLog("stream added in database failure :" + e.getMessage() + " "));
    }

    private void getAcceptedRequest() {

        DatabaseReference streamRef = database.getReference().child(DBConstants.SingleStreamInvitation)
                .child(profilePOJO.getUserId());

        streamRef.limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {

                    if (snapshot.hasChild(DBConstants.request_status)) {
                        String check_status = snapshot.child(DBConstants.request_status).getValue().toString();
                        if (snapshot.hasChild(DBConstants.user_id)) {
                            String userid = snapshot.child(DBConstants.user_id).getValue().toString();
                            if (!profilePOJO.getUserId().equalsIgnoreCase(userid)) {
                                if (check_status.equalsIgnoreCase("true")) {

                                    showGuestView();
                                    Commn.myToast(context, "request accepted by guest");
                                    //   Commn.showDebugLog("requested_accepted_by_guest");
                                    startGuestStream(userid);

                                }
                            }
                        }


                    }

                } else {
                    Commn.showDebugLog("no snapshot ");

                    //binding.myRequestedCamera.stopCamera();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //  binding.joinedVideoFrame.setVisibility(View.GONE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        checkMyStreamAsGuestStatus();
    }

    private void checkMyStreamAsGuestStatus() {
        database.getReference().child(DBConstants.SingleStreamInvitation).child(profilePOJO.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            hideGuestView();
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

    private void hideSaySomething() {
        binding.rlBottomContent.setVisibility(View.VISIBLE);
        binding.rlSaySomething.setVisibility(View.GONE);
    }

    private void endStreamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("End Broadcast");
        builder.setMessage("Do you want to end this broadcast? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                endStream();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });
        builder.show();
    }

    private String genetrateStreamId() {

        return profilePOJO.getSuper_live_id();

    }

    private void endedStreamUI() {

        binding.llEndedLayout.setVisibility(View.VISIBLE);
        binding.rlSaySomething.setVisibility(View.GONE);
        binding.rlUserInfo.setVisibility(View.GONE);
        binding.preview.setVisibility(View.GONE);
        binding.llGuestFrame.setVisibility(View.GONE);
        binding.tvStreamTotalTime.setText("Total Broadcast Time : " + String.valueOf(Commn.convertDuration(sessionManager.getStreamStarted(), System.currentTimeMillis())) + "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Commn.showDebugLog("onResume");
        if (!isONCall) {
            Commn.showDebugLog("notcall");
            playStream();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Commn.showDebugLog("onPause");
        if (!isONCall) {
            pauseStream();
        }
        // endStream();
       /* if (mJoinedStreamaxiaPlayer!=null){
            mJoinedStreamaxiaPlayer.pause();
        }*/

    }

    @Override
    protected void onDestroy() {
        Commn.showDebugLog("offline" + "destroy");
        endStream();
        super.onDestroy();
    }


    private void startGuestStream(String userid) {
        Flashphoner.init(this);
        Flashphoner.getAudioManager().setUseSpeakerPhone(true);


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
        sessionOptions.setRemoteRenderer(binding.joinedSurfaceView);

        guest_session = Flashphoner.createSession(sessionOptions);

        guest_session.on(new SessionEvent() {
            @Override
            public void onAppData(Data data) {


            }

            @Override
            public void onConnected(final Connection connection) {
                runOnUiThread(() -> {
                    showGuestView();
                    firebaseFirestore.collection(DBConstants.UserInfo)
                            .document(userid)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.getResult() != null) {
                                    if (task.getResult().exists()) {
                                        guest_buzo_id = task.getResult().getString(DBConstants.super_live_id);
                                        StreamOptions streamOptions = new StreamOptions(guest_buzo_id);

                                        Commn.showDebugLog("guest_buzo_id:" + guest_buzo_id + "");
                                        myAsGuestStream = guest_session.createStream(streamOptions);

                                        myAsGuestStream.on((stream, streamStatus) -> runOnUiThread(() -> {

                                            Commn.showErrorLog("Stream_status" + String.valueOf(streamStatus));


                                        }));

                                        myAsGuestStream.play();
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
                    Commn.showDebugLog("player_status" + "stateENDED" + "");
                    Commn.myToast(context, "Ended");
                    hideGuestView();

                });
            }
        });

        guest_session.connect(new Connection());


    }


}