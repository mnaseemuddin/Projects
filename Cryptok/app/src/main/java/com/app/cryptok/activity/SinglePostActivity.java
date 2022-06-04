package com.app.cryptok.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
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
import com.app.cryptok.databinding.ActivitySinglePostBinding;
import com.app.cryptok.model.PostsModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.model.ReportReasonModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.HashMap;
import java.util.Map;

import static com.app.cryptok.Api.DBConstants.user_id;

public class SinglePostActivity extends AppCompatActivity {
    private ActivitySinglePostBinding binding;
    private Context context;
    private SinglePostActivity activity;

    ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    //database
    private FirebaseFirestore firebaseFirestore;
    private PostsModel postsModel;
    private String post_id;
    private CollectionReference user_info;
    ArrayAdapter<String> report_adapter;
    private DatabaseReference comment_Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_post);
        context = activity = this;
        iniFirbase();
        iniIntent();
        getPostInfo();
        handleClick();
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void iniFirbase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(context);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        comment_Ref = FirebaseDatabase.getInstance().getReference().child(DBConstants.Post_Comments);
        report_adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1);
    }

    private void iniIntent() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(DBConstants.post_id)) {
                if (getIntent().getStringExtra(DBConstants.post_id) != null) {
                    post_id = getIntent().getStringExtra(DBConstants.post_id);
                }
            }
        }

    }

    private void getPostInfo() {

        firebaseFirestore.collection(DBConstants.User_Posts)
                .document(post_id)
                .get()
                .addOnCompleteListener(task -> {

                    binding.progressBar.setVisibility(View.GONE);
                    if (task.getResult() != null) {
                        if (task.getResult().exists()) {
                            postAvailable();
                            postsModel = task.getResult().toObject(PostsModel.class);
                            if (postsModel != null) {
                                setPostInfo();
                            }

                        } else {
                            postNotAvailable();
                        }
                    } else {
                        postNotAvailable();
                    }
                });
    }

    private void postAvailable() {
        binding.llPost.setVisibility(View.VISIBLE);
        binding.tvNoPosts.setVisibility(View.GONE);
    }

    private void postNotAvailable() {
        binding.llPost.setVisibility(View.GONE);
        binding.tvNoPosts.setVisibility(View.VISIBLE);
    }

    private void setPostInfo() {
        binding.tvDateTime.setText(Commn.getTimeAgo(postsModel.getTimestamp().getTime()));
        binding.tvCaption.setText(postsModel.getPost_caption());
        if (postsModel.getPost_caption() != null) {
            if (TextUtils.isEmpty(postsModel.getPost_caption().trim())) {
                binding.tvCaption.setVisibility(View.GONE);
            } else {
                binding.tvCaption.setVisibility(View.VISIBLE);
            }
        }
        Glide.with(getApplicationContext())
                .load(postsModel.getPost_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivPostImage);

        binding.ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                intent.putExtra(DBConstants.user_image, postsModel.getPost_image());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.ivLike.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {

                updateLike(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                updateLike(false);
            }
        });
        getLikesFunc();
        getCommentCount();
        getInfo();
        binding.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra(Commn.MODEL, new Gson().toJson(postsModel));
                startActivity(intent);
            }
        });

        binding.ivMore.setOnClickListener(view -> {

            PopupMenu popupMenu = new PopupMenu(context, binding.ivMore);

            popupMenu.getMenu().add("Report Post");
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Report Post")) {
                    report_post();
                }

                return true;
            });

        });

        binding.ivUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AnotherUserProfile.class);

                intent.putExtra(user_id, postsModel.getUser_id());

                startActivity(intent);

            }
        });


    }

    private void report_post() {
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
            addReport(strName);
        });
        builderSingle.show();
    }

    private void addReport(String strName) {
        Map<String, Object> map = new HashMap<>();

        DocumentReference doc_ref = firebaseFirestore
                .collection(DBConstants.post_report_info)
                .document();
        String id = doc_ref.getId();
        map.put(DBConstants.report_reason, strName);
        map.put(DBConstants.report_id, id);
        map.put(DBConstants.post_id, postsModel.getPost_id());
        map.put(DBConstants.user_id, postsModel.getUser_id());
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

    private void getCommentCount() {
        comment_Ref.child(post_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            binding.tvCommentCount.setText(String.valueOf(snapshot.getChildrenCount()) + "");

                        } else {
                            binding.tvCommentCount.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getLikesFunc() {
        //getLikes
        firebaseFirestore.collection("User_Posts/" + postsModel.getPost_id() + "/User_Likes").document(profilePOJO.getUserId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot.exists()) {
                            binding.ivLike.setLiked(true);
                        } else {
                            binding.ivLike.setLiked(false);

                        }
                    }
                });
        //getlikes count
        firebaseFirestore.collection("User_Posts/" + postsModel.getPost_id() + "/User_Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if (queryDocumentSnapshots != null) {

                    int count = queryDocumentSnapshots.size();
                    binding.tvLikeCount.setText(String.valueOf(count));
                } else {
                    binding.tvLikeCount.setText(String.valueOf(0));

                }
            }
        });
    }

    private void updateLike(boolean isLike) {
        if (isLike) {
            updateMyLike();
        } else {
            removeMyLike();
        }
    }

    private void removeMyLike() {
        firebaseFirestore.collection(DBConstants.User_Posts + "/" + postsModel.getPost_id() + "/" + DBConstants.User_Likes).document(profilePOJO.getUserId())
                .get().addOnCompleteListener(task -> {
            if (task.getResult().exists()) {
                firebaseFirestore.collection("User_Posts/" + postsModel.getPost_id() + "/User_Likes")
                        .document(profilePOJO.getUserId()).delete();
            }

        });
    }

    private void updateMyLike() {

        firebaseFirestore.collection("User_Posts/" + postsModel.getPost_id() + "/User_Likes").document(profilePOJO.getUserId())

                .get().addOnCompleteListener(task -> {

            Map<String, Object> likesMap = new HashMap<>();
            likesMap.put("timestamp", FieldValue.serverTimestamp());
            firebaseFirestore.collection("User_Posts/" + postsModel.getPost_id() + "/User_Likes")
                    .document(profilePOJO.getUserId()).set(likesMap);

            if (!profilePOJO.getUserId().equalsIgnoreCase(postsModel.getUser_id())) {
                pushLikeNotification();
            }


        });


    }

    private void pushLikeNotification() {
        NotificationRequest notificationRequest = new NotificationRequest();
        if (null != profilePOJO.getName()) {
            if (!profilePOJO.getName().isEmpty()) {
                notificationRequest.setContext_message("<strong>" + profilePOJO.getName() + "</strong>" + " liked your post");
            } else {
                notificationRequest.setContext_message("<strong>" + profilePOJO.getSuper_live_id() + "</strong>" + " liked your post ");
            }
        } else {
            notificationRequest.setContext_message("<strong>" + profilePOJO.getSuper_live_id() + "</strong>" + " liked your post ");
        }
        notificationRequest.setNotification_type(Notification_Const.IMAGE_NOTIFICATION_TYPE);
        notificationRequest.setSuper_live_id(profilePOJO.getSuper_live_id() + "");
        notificationRequest.setUser_id(profilePOJO.getUserId() + "");
        notificationRequest.setMessage(postsModel.getPost_image() + "");
        notificationRequest.setAlert_type(Commn.POST_LIKE_TYPE);
        notificationRequest.setNotification_data(post_id);

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(postsModel.getUser_id())
                .collection(DBConstants.Tokens)
                .document(postsModel.getUser_id())
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

    private void getInfo() {

        user_info.document(postsModel.getUser_id())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().exists()) {
                        String name = task.getResult().getString(DBConstants.name);
                        String image = task.getResult().getString(DBConstants.image);
                        String buzo_id = task.getResult().getString(DBConstants.super_live_id);
                        if (name != null) {
                            if (name.equals("") || TextUtils.isEmpty(name)) {
                                binding.tvUserName.setText(buzo_id + "");
                            } else {
                                binding.tvUserName.setText(name + "");
                            }

                        }
                        if (image != null) {

                            Glide.with(getApplicationContext()).load(image).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder_user).into(binding.ivUserImage);


                        }

                    }

                });


    }
}