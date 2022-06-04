package com.app.cryptok.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.LiveShopping.Model.UsersImagesModel;
import com.app.cryptok.Notifications.InializeNotification;
import com.app.cryptok.Notifications.NotificationRequest;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.Notifications.Sender;
import com.app.cryptok.R;
import com.app.cryptok.adapter.GlobalSliderAdapter;
import com.app.cryptok.adapter.TimeLineAdapter;
import com.app.cryptok.databinding.ActivityAnotherUserProfileBinding;
import com.app.cryptok.model.PostsModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.model.ReportReasonModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.app.cryptok.utils.Commn.CHAT_TYPE;

public class AnotherUserProfile extends AppCompatActivity {


    private Context context;
    private AnotherUserProfile activity;
    private String user_id, user_name, user_image, buzo_id;

    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private FirebaseDatabase database;

    //get posts
    private FirebaseFirestore firebaseFirestore;

    private String blogPostId;
    FirestorePagingOptions<PostsModel> firestoreRecyclerOptions;
    private FirestorePagingAdapter<PostsModel, TimeLineAdapter> firebaseRecyclerAdapter;
    //fans
    CollectionReference user_info;
    private DatabaseReference comment_Ref;
    private ActivityAnotherUserProfileBinding binding;
    private List<UsersImagesModel> imagesModels = new ArrayList<>();
    ArrayAdapter<String> report_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_another_user_profile);

        context = activity = this;
        iniViews();
        getData();

    }


    private void iniViews() {
        binding.rvTimeline.setLayoutManager(new LinearLayoutManager(context));
        binding.rvTimeline.setNestedScrollingEnabled(false);
        report_adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1);
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.llMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(DBConstants.user_id, user_id);
                intent.putExtra(Commn.TYPE, CHAT_TYPE);

                startActivity(intent);
            }
        });
        binding.LLFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkFollow();


            }
        });
        binding.LLFollowing.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), MyConnectionsActivity.class);
            intent.putExtra(DBConstants.connection_type, DBConstants.following_type);
            intent.putExtra(DBConstants.user_id, user_id);
            startActivity(intent);
        });
        binding.LLFollowers.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), MyConnectionsActivity.class);
            intent.putExtra(DBConstants.connection_type, DBConstants.follower_type);
            intent.putExtra(DBConstants.user_id, user_id);
            startActivity(intent);
        });

        binding.llFriends.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), MyConnectionsActivity.class);
            intent.putExtra(DBConstants.connection_type, DBConstants.friends_type);
            intent.putExtra(DBConstants.user_id, user_id);
            startActivity(intent);
        });

    }


    private void checkFollow() {

        String followers_Ref = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Followers;
        String following_Ref = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Following;

        if (binding.tvFollowUnfollow.getText().toString().equalsIgnoreCase("Follow")) {
            setFollowing();
        } else {
            setUnFollow();
        }
        firebaseFirestore.collection(followers_Ref)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {

                        firebaseFirestore.collection(followers_Ref)
                                .document(profilePOJO.getUserId())
                                .delete();

                        deleteFollowing(following_Ref);
                        Commn.showDebugLog("already followed");
                        setUnFollow();

                    } else {
                        setFollowing();
                        follow_user();
                        addFollowing(following_Ref);

                    }
                });


    }

    private void addFollowing(String following_ref) {
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put(DBConstants.user_id, user_id);
        messageMap.put(DBConstants.follow_id, user_id);


        //follow user
        firebaseFirestore.collection(following_ref)
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

    private void deleteFollowing(String following_Ref) {
        firebaseFirestore.collection(following_Ref)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {

                        firebaseFirestore.collection(following_Ref)
                                .document(user_id)
                                .delete();


                    }
                });
    }


    private void getData() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        if (getIntent().hasExtra(DBConstants.user_id)) {
            user_id = getIntent().getStringExtra(DBConstants.user_id);
            checkUserid();
            new Handler(Looper.getMainLooper()).postDelayed(this::setInfo, 100);
            retriveData();
            handleClick();

            addFriendsEvent();
        }

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

    private void checkUserid() {
        if (profilePOJO.getUserId().equalsIgnoreCase(user_id)) {
            binding.llCheckContent.setVisibility(View.GONE);
        } else {
            binding.llCheckContent.setVisibility(View.VISIBLE);
        }
    }

    private void retriveData() {
        //get total fans
        String currentUserRef = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Followers;
        String following_Ref = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Following;
        firebaseFirestore.collection(currentUserRef)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        int count = value.size();
                        binding.tvTotalFans.setText(Commn.prettyCount(Long.parseLong(String.valueOf(count))));
                        Commn.showDebugLog("snapshot eXISTS: all_fans:" + count + "");
                    } else {
                        Commn.showDebugLog("snapshot NOT eXISTS:");
                        binding.tvTotalFans.setText("0");
                    }
                });

        //check follow or not
        firebaseFirestore.collection(currentUserRef)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {

                        setFollowing();

                    } else {

                        setUnFollow();
                    }
                });
        firebaseFirestore.collection(following_Ref)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        int count = value.size();
                        binding.tvTotalFollowing.setText(Commn.prettyCount(Long.parseLong(String.valueOf(count))));
                        Commn.showDebugLog("snapshot eXISTS: all_following:" + count + "");
                    } else {
                        Commn.showDebugLog("following snapshot NOT eXISTS:");
                        binding.tvTotalFollowing.setText("0");
                    }
                });

        getPosts();
    }

    private void setInfo() {

        String ref = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Images;
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        user_name = task.getResult().getString(DBConstants.name);
                        user_image = task.getResult().getString(DBConstants.image);
                        buzo_id = task.getResult().getString(DBConstants.super_live_id);
                        String bio = task.getResult().getString(DBConstants.bio);
                        String dob = task.getResult().getString(DBConstants.dob);
                        String gender = task.getResult().getString(DBConstants.gender);

                        if (user_name != null) {
                            binding.tvUserName.setText(user_name + "");
                        }
                        if (buzo_id != null) {
                            binding.tvBuzoId.setText(getString(R.string.app_name) + " ID : " + buzo_id + "");
                        }
                        if (bio != null) {
                            binding.tvBuzoBio.setText(bio + "");
                        }

                        if (dob != null) {

                            if (!TextUtils.isEmpty(dob.trim())) {
                                binding.tvAge.setText(String.valueOf(Commn.getAge(dob)) + "" + "");
                            }

                        }

                        int total_recived_beans = task.getResult().getLong(DBConstants.total_beans).intValue();
                        binding.tvTotalBeans.setText(Commn.prettyCount(Long.parseLong(String.valueOf(total_recived_beans))));
                        int total_sent_beans = task.getResult().getLong(DBConstants.total_sent_beans).intValue();
                        binding.tvTotalDiamond.setText(Commn.prettyCount(Long.parseLong(String.valueOf(total_sent_beans))));

                        if (gender != null) {
                            if (DBConstants.male.equalsIgnoreCase(gender)) {
                                Glide.with(getApplicationContext()).load(R.drawable.male).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivGender);
                            }
                            if (DBConstants.female.equalsIgnoreCase(gender)) {
                                Glide.with(getApplicationContext()).load(R.drawable.female).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivGender);
                            }
                        }
                        firebaseFirestore.collection(ref)
                                .document("1")
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (!task1.getResult().exists()) {
                                        updateFirstImage();
                                    }

                                });

                    }
                });

        firebaseFirestore.collection(ref)
                .addSnapshotListener((value, error) -> {
                    imagesModels.clear();
                    if (value != null) {
                        if (!value.isEmpty()) {
                            for (DocumentChange documentSnapshot : value.getDocumentChanges()) {
                                UsersImagesModel model = documentSnapshot.getDocument().toObject(UsersImagesModel.class);
                                imagesModels.add(model);
                            }
                            setAdapter();

                        }
                    }
                });
        String getFriends = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.Friends;
        firebaseFirestore.collection(getFriends)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        int count = value.size();
                        binding.tvFriendsCount.setText(Commn.prettyCount(Long.parseLong(String.valueOf(count + ""))));
                        Commn.showDebugLog("snapshot eXISTS: all_friends:" + count + "");
                    } else {
                        Commn.showDebugLog("all_friends snapshot NOT eXISTS:");
                        binding.tvFriendsCount.setText("0");
                    }
                });


    }

    private void updateFirstImage() {
        String ref = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Images;
        Map<String, Object> update = new HashMap<>();
        update.put(DBConstants.image, user_image + "");
        update.put(DBConstants.image_id, "1");
        firebaseFirestore.collection(ref)
                .document("1")
                .set(update)
                .addOnCompleteListener(task ->
                        Commn.showErrorLog("image uploaded:" + "1"));
    }

    private void setAdapter() {
        GlobalSliderAdapter detailSliderAdapter = new GlobalSliderAdapter(imagesModels, context);
        binding.homeSlider.setSliderAdapter(detailSliderAdapter);
        binding.homeSlider.setScrollTimeInSec(10);
        binding.homeSlider.startAutoCycle();

        binding.homeSlider.setIndicatorAnimation(IndicatorAnimations.SWAP);
        binding.homeSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        detailSliderAdapter.notifyDataSetChanged();
    }

    private void getPosts() {

        iniFirebaseOptions();
        user_info = FirebaseFirestore.getInstance().collection(DBConstants.UserInfo);
        comment_Ref = database.getReference().child(DBConstants.Post_Comments);
        List<String> size = new ArrayList<>();
        firebaseRecyclerAdapter = new FirestorePagingAdapter<PostsModel, TimeLineAdapter>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TimeLineAdapter holder, int position, @NonNull PostsModel model) {
                blogPostId = model.post_id;

                size.add(model.getUser_id());

                holder.tv_user_name.setText(model.getUser_name());
                holder.tv_date_time.setText(Commn.getTimeAgo(model.getTimestamp().getTime()));
                holder.tv_caption.setText(model.getPost_caption());

                Glide.with(getApplicationContext()).load(model.getUser_image()).placeholder(R.drawable.placeholder_user).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.iv_user_image);
                Glide.with(getApplicationContext()).load(model.getPost_image()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_post_image);


                holder.iv_post_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, ViewImageActivity.class);
                        intent.putExtra(DBConstants.user_image, model.getPost_image());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                if (blogPostId != null) {
                    getCommentCount(holder);
                }
                holder.iv_like.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        updateLike(true, model.getPost_id());
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        updateLike(false, model.getPost_id());
                    }
                });
                getLikesFunc(holder);
                getInfo(holder, model.getUser_id());

                holder.iv_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra(DBConstants.post_id, model.getPost_id());
                        intent.putExtra(DBConstants.user_id, model.getUser_id());
                        intent.putExtra(DBConstants.post_image, model.getPost_image());

                        startActivity(intent);
                    }
                });
                holder.iv_more.setOnClickListener(view -> {

                    PopupMenu popupMenu = new PopupMenu(context, holder.iv_more);
                    if (model.getUser_id().equalsIgnoreCase(profilePOJO.getUserId())) {
                        popupMenu.getMenu().add("Delete Post");
                    }
                    popupMenu.getMenu().add("Report Post");
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals("Delete Post")) {
                                deletePost(model);
                            }
                            if (item.getTitle().equals("Report Post")) {
                                report_post(model);
                            }


                            return true;
                        }
                    });

                });

            }

            @NonNull
            @Override
            public TimeLineAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_time_line, parent, false);
                return new TimeLineAdapter(textView);
            }


            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {

                    case ERROR:
                        Commn.showDebugLog("PAGING log:error loading notificationModel:");
                        break;
                    case LOADED:
                        if (size.size() > 0) {
                            binding.tvEmpty.setVisibility(View.GONE);
                        } else {
                            binding.tvEmpty.setVisibility(View.VISIBLE);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");
                        break;
                    case FINISHED:
                        if (size.size() > 0) {
                            binding.tvEmpty.setVisibility(View.GONE);
                        } else {
                            binding.tvEmpty.setVisibility(View.VISIBLE);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        Commn.showDebugLog("PAGING log:All notificationModel loaded:");
                        break;
                    case LOADING_MORE:
                        Commn.showDebugLog("PAGING log:loading next page");
                        break;
                    case LOADING_INITIAL:

                        Commn.showDebugLog("PAGING log:loading initial notificationModel");
                        break;

                }
            }
        };


        binding.rvTimeline.setAdapter(firebaseRecyclerAdapter);


    }

    private void report_post(PostsModel model) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);

        firebaseFirestore.collection(DBConstants.report_reasons)
                .get()
                .addOnCompleteListener(task -> {
                    report_adapter.clear();
                    for (DocumentChange documentChange : task.getResult().getDocumentChanges()) {
                        ReportReasonModel model1 = documentChange.getDocument().toObject(ReportReasonModel.class);
                        report_adapter.add(model1.getReport_reason());
                    }
                });

        builderSingle.setAdapter(report_adapter, (dialog, which) -> {
            String strName = report_adapter.getItem(which);
            dialog.dismiss();
            addReport(strName, model);
        });
        builderSingle.show();
    }

    private void addReport(String strName, PostsModel model) {
        Map<String, Object> map = new HashMap<>();

        DocumentReference doc_ref = firebaseFirestore
                .collection(DBConstants.post_report_info)
                .document();

        String id = doc_ref.getId();
        map.put(DBConstants.report_reason, strName);
        map.put(DBConstants.report_id, id);
        map.put(DBConstants.post_id, model.getPost_id());
        map.put(DBConstants.user_id, model.getUser_id());
        map.put(DBConstants.report_user_id, profilePOJO.getUserId());
        Commn.showDebugLog("addReport_params:" + map);
        firebaseFirestore
                .collection(DBConstants.post_report_info)
                .document(id)
                .set(map)
                .addOnCompleteListener(task -> {

                    Commn.myToast(context, "Report Added");
                    Commn.showDebugLog("report_added:" + task.isSuccessful());

                });
    }

    private void deletePost(PostsModel model) {
        firebaseFirestore.collection(DBConstants.User_Posts)
                .document(model.getPost_id())
                .delete()
                .addOnCompleteListener(task -> {
                    deletePostImage(model);
                    firebaseRecyclerAdapter.refresh();
                });
    }

    private void deletePostImage(PostsModel model) {
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(model.getPost_image());
        photoRef.delete().addOnSuccessListener(task -> {

            Commn.showDebugLog("delete_succes");
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Commn.showDebugLog("delete_onFailure");
            }
        });
    }

    private void getCommentCount(TimeLineAdapter holder) {

        comment_Ref.child(blogPostId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            holder.tv_comment_count.setText(String.valueOf(snapshot.getChildrenCount()) + "");
                        } else {
                            holder.tv_comment_count.setText("0");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getLikesFunc(TimeLineAdapter holder) {
        //getLikes
        firebaseFirestore.collection("User_Posts/" + blogPostId + "/User_Likes").document(profilePOJO.getUserId())
                .addSnapshotListener((documentSnapshot, e) -> {

                    if (documentSnapshot.exists()) {
                        holder.iv_like.setLiked(true);
                    } else {
                        holder.iv_like.setLiked(false);

                    }
                });
        //getlikes count
        firebaseFirestore.collection("User_Posts/" + blogPostId + "/User_Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (queryDocumentSnapshots != null) {

                    int count = queryDocumentSnapshots.size();
                    holder.tv_like_count.setText(String.valueOf(count));
                } else {
                    holder.tv_like_count.setText(String.valueOf(0));

                }
            }
        });
    }

    private void iniFirebaseOptions() {
        Commn.showDebugLog("anotherprofile_activity:" + user_id);
        Query query = firebaseFirestore.collection(DBConstants.User_Posts).orderBy(DBConstants.timestamp, Query.Direction.DESCENDING)
                .whereEqualTo(DBConstants.user_id, user_id);
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();
        firestoreRecyclerOptions = new FirestorePagingOptions.Builder<PostsModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, PostsModel.class)
                .build();


    }

    private void updateLike(boolean isLike, String postid) {
        if (isLike) {
            updateMyLike(postid);
        } else {
            removeMyLike(postid);
        }


    }

    private void removeMyLike(String postid) {
        firebaseFirestore.collection(DBConstants.User_Posts + "/" + postid + "/" + DBConstants.User_Likes).document(profilePOJO.getUserId())
                .get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                firebaseFirestore.collection("User_Posts/" + postid + "/User_Likes")
                        .document(profilePOJO.getUserId()).delete();
            }

        });
    }

    private void updateMyLike(String postid) {

        firebaseFirestore.collection("User_Posts/" + postid + "/User_Likes").document(profilePOJO.getUserId())

                .get().addOnCompleteListener(task -> {
            Map<String, Object> likesMap = new HashMap<>();
            likesMap.put("timestamp", FieldValue.serverTimestamp());
            firebaseFirestore.collection("User_Posts/" + postid + "/User_Likes")
                    .document(profilePOJO.getUserId()).set(likesMap);

        });


    }

    private void setFollowing() {
        binding.LLFollow.setBackground(getResources().getDrawable(R.drawable.following_bg));
        binding.tvFollowUnfollow.setText("Following");
        binding.tvFollowUnfollow.setTextColor(getResources().getColor(R.color.black));

    }

    private void setUnFollow() {

        binding.LLFollow.setBackground(getResources().getDrawable(R.drawable.btn_design));
        binding.tvFollowUnfollow.setText("Follow");
        binding.tvFollowUnfollow.setTextColor(getResources().getColor(R.color.white));


    }

    private void follow_user() {
        String followers_Ref = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Followers;
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put(DBConstants.user_id, profilePOJO.getUserId());
        messageMap.put(DBConstants.follow_id, profilePOJO.getUserId());

        //follow user
        firebaseFirestore.collection(followers_Ref)
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
        notificationRequest.setNotification_data("");
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

    @SuppressLint("SetTextI18n")
    private void getInfo(TimeLineAdapter holder, String user_id) {

        user_info.document(user_id)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().exists()) {
                        String name = task.getResult().getString(DBConstants.name);
                        String image = task.getResult().getString(DBConstants.image);
                        if (name != null) {
                            holder.tv_user_name.setText(name + "");
                        }
                        if (image != null) {
                            Glide.with(getApplicationContext()).load(image).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder_user).into(holder.iv_user_image);

                        }

                    }

                });
    }

}