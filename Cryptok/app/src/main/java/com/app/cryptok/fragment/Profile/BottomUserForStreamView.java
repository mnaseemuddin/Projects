package com.app.cryptok.fragment.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Notifications.InializeNotification;
import com.app.cryptok.Notifications.NotificationRequest;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.Notifications.Sender;
import com.app.cryptok.R;
import com.app.cryptok.databinding.FragmentBottomUserForStreamViewBinding;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;


public class BottomUserForStreamView extends BottomSheetDialogFragment {
    private FragmentBottomUserForStreamViewBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    ProfilePOJO profilePOJO;
    private SessionManager sessionManager;

    public BottomUserForStreamView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(DBConstants.user_id)) {
                user_id = getArguments().getString(DBConstants.user_id);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_user_for_stream_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMyInfo();
        iniFireabase();
        new Handler(Looper.getMainLooper())
                .postDelayed(this::getUser, 200);

        handleClick();
        addFriendsEvent();
    }

    private void handleClick() {
    }

    private void getMyInfo() {

        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);


    }

    private void iniFireabase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getUser() {
        if (getActivity() == null) {
            return;
        }
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            Commn.showDebugLog("getInfoo" + "exists");
                            ProfilePOJO profilePOJO = task.getResult().toObject(ProfilePOJO.class);

                            getUserInfo(profilePOJO);
                        }

                    } else {
                        Commn.showDebugLog("getInfoo" + " not exists");
                    }
                });


    }

    private void getUserInfo(ProfilePOJO profile) {
        String currentUserRef = DBConstants.UserInfo + "/" + profile.getUserId() + "/" + DBConstants.User_Followers;
        String following_Ref = DBConstants.UserInfo + "/" + profile.getUserId() + "/" + DBConstants.User_Following;
        if (profilePOJO.getUserId().equalsIgnoreCase(profile.getUserId())) {
            binding.LLFollow.setVisibility(View.GONE);
        } else {
            binding.LLFollow.setVisibility(View.VISIBLE);
        }
        binding.tvUserNameBottom.setText(profile.getName());

        Glide.with(binding.getRoot().getContext()).load(profile.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.placeholder_user)
                .into(binding.ivUserFollowImage);

        binding.LLFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkFollow(profile);

            }
        });


        firebaseFirestore.collection(currentUserRef)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        int count = value.size();
                        binding.tvTotalFans.setText(String.valueOf(count + ""));
                        Commn.showDebugLog("snapshot eXISTS: all_fans:" + count + "");
                    } else {
                        Commn.showDebugLog("snapshot NOT eXISTS:");
                        binding.tvTotalFans.setText("0");
                    }
                });
        firebaseFirestore.collection(currentUserRef)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {
                    if (value.exists()) {
                        Commn.showDebugLog("you already followed");
                        setFollowing();
                    } else {
                        Commn.showDebugLog("you are not followed");
                        setUnFollow();
                    }
                });
        firebaseFirestore.collection(following_Ref)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            int count = value.size();
                            binding.tvTotalFollowing.setText(String.valueOf(count + ""));
                            Commn.showDebugLog("snapshot eXISTS: all_following:" + count + "");
                        } else {
                            Commn.showDebugLog("following snapshot NOT eXISTS:");
                            binding.tvTotalFollowing.setText("0");
                        }
                    }
                });
        String another_user = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.Friends;

        firebaseFirestore.collection(another_user)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {

                        int count = value.size();
                        binding.tvUserFriends.setText(String.valueOf(count + ""));
                        Commn.showDebugLog("snapshot eXISTS: Friends:" + count + "");
                    } else {
                        Commn.showDebugLog("all_friends snapshot NOT eXISTS:");
                        binding.tvUserFriends.setText("0");
                    }
                });
    }

    private void addFriendsEvent() {

        String followersRef = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Followers;
        String following_Ref = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Following;

        firebaseFirestore.collection(followersRef)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {
                    if (value.exists()) {
                        firebaseFirestore.collection(following_Ref)
                                .document(profilePOJO.getUserId())
                                .addSnapshotListener((value2, error2) -> {
                                    if (value2.exists()) {

                                        addFriends();

                                    } else {

                                        removeFromFriends();

                                    }
                                });
                    } else {

                        removeFromFriends();

                    }
                });


    }

    private void removeFromFriends() {
        String current_user = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.Friends;
        String another_user = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.Friends;

        firebaseFirestore.collection(current_user)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        firebaseFirestore.collection(current_user)
                                .document(user_id)
                                .delete();
                    }
                });
        firebaseFirestore.collection(another_user)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        firebaseFirestore.collection(another_user)
                                .document(profilePOJO.getUserId())
                                .delete();
                    }
                });
    }

    private void addFriends() {
        String current_user = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.Friends;
        String another_user = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.Friends;
        Map<String, Object> current_map = new HashMap<>();
        current_map.put(DBConstants.user_id, user_id);

        Map<String, Object> abother_map = new HashMap<>();
        abother_map.put(DBConstants.user_id, profilePOJO.getUserId());

        firebaseFirestore.collection(current_user)
                .document(user_id)
                .set(current_map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Commn.showErrorLog("now you are friend with :" + user_id + "");
                    }
                });
        firebaseFirestore.collection(another_user)
                .document(profilePOJO.getUserId())
                .set(abother_map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Commn.showErrorLog("now he is friend with you :" + "");
                    }
                });
    }

    private void setFollowing() {
        binding.LLFollow.setBackground(binding.getRoot().getContext().getResources().getDrawable(R.drawable.following_bg));
        binding.tvFollowUnfollow.setText("Following");
        binding.tvFollowUnfollow.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.black));

    }

    private void setUnFollow() {

        binding.LLFollow.setBackground(binding.getRoot().getContext().getResources().getDrawable(R.drawable.btn_design));
        binding.tvFollowUnfollow.setText("Follow");
        binding.tvFollowUnfollow.setTextColor(binding.getRoot().getContext().getResources().getColor(R.color.white));


    }

    private void checkFollow(ProfilePOJO profile) {

        String currentUserRef = DBConstants.UserInfo + "/" + profile.getUserId() + "/" + DBConstants.User_Followers;
        String following_Ref = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Following;
        firebaseFirestore.collection(currentUserRef)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        firebaseFirestore.collection(currentUserRef)
                                .document(profilePOJO.getUserId())
                                .delete();
                        deleteFollowing(following_Ref, profile.getUserId());
                        setUnFollow();

                        Commn.showDebugLog("already followed");
                    } else {

                        setFollowing();
                        follow_user(profile);
                        addFollowing(following_Ref, profile.getUserId());
                    }

                });
    }

    private void follow_user(ProfilePOJO profile) {
        String currentUserRef = DBConstants.UserInfo + "/" + profile.getUserId() + "/" + DBConstants.User_Followers;
        Map messageMap = new HashMap();

        messageMap.put(DBConstants.user_id, profilePOJO.getUserId());
        messageMap.put(DBConstants.follow_id, profilePOJO.getUserId());
        messageMap.put(DBConstants.user_name, profilePOJO.getName());
        messageMap.put(DBConstants.user_image, profilePOJO.getImage());

        firebaseFirestore.collection(currentUserRef)
                .document(profilePOJO.getUserId())
                .set(messageMap)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Commn.showDebugLog("followed success");
                        sendNotification();
                    }

                }).addOnFailureListener(e -> Commn.showDebugLog("followed Exception:" + e.getMessage() + " "));
    }
    private void sendNotification() {
        NotificationRequest notificationRequest = new NotificationRequest();
        if (profilePOJO.getName() != null) {
            if (!profilePOJO.getName().isEmpty()) {
                notificationRequest.setMessage("<strong>" + profilePOJO.getName() + "</strong>" +" followed you in "+getString(R.string.app_name));
            } else {
                notificationRequest.setMessage("<strong>" + profilePOJO.getSuper_live_id() + "</strong>" + " followed you in "+getString(R.string.app_name));
            }
        } else {
            notificationRequest.setMessage("<strong>" + profilePOJO.getSuper_live_id() + "</strong>" +" followed you in "+getString(R.string.app_name));
        }

        notificationRequest.setNotification_type(Notification_Const.NORMAL_NOTIFICATION_TYPE);
        notificationRequest.setSuper_live_id(profilePOJO.getSuper_live_id() + "");
        notificationRequest.setUser_id(profilePOJO.getUserId() + "");
        notificationRequest.setContext_message("");
        notificationRequest.setAlert_type(Commn.FOLLOW_TYPE);

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(user_id)
                .collection(DBConstants.Tokens)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            if (task.getResult().exists()) {
                                if (task.getResult().getString(DBConstants.user_token) != null) {
                                    String token = task.getResult().getString(DBConstants.user_token);
                                    Commn.showDebugLog("got that user token:" + token);
                                    Sender sender = new Sender(notificationRequest, token);
                                    Commn.showDebugLog("notification_send_model:" + new Gson().toJson(sender.getData()));
                                    new InializeNotification().sendNotification(sender);
                                }

                            }

                        }
                    }
                });

    }

    private void addFollowing(String following_Ref, String user_id) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put(DBConstants.user_id, user_id);
        messageMap.put(DBConstants.follow_id, user_id);


        //follow user
        firebaseFirestore.collection(following_Ref)
                .document(user_id)
                .set(messageMap)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Commn.showDebugLog("following success");
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Commn.showDebugLog("followed Exception:" + e.getMessage() + " ");
            }
        });
    }

    private void deleteFollowing(String following_ref, String user_id) {
        firebaseFirestore.collection(following_ref)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {

                        firebaseFirestore.collection(following_ref)
                                .document(user_id)
                                .delete();


                    }
                });
    }


}