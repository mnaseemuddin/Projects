package com.app.cryptok.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.adapter.TimeLineAdapter;
import com.app.cryptok.databinding.ActivityMyPostsBinding;
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

public class MyPostsActivity extends AppCompatActivity {

    private Context context;
    private MyPostsActivity activity;
    private ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    //database
    private FirebaseFirestore firebaseFirestore;
    private FirestorePagingOptions<PostsModel> firestoreRecyclerOptions;
    private FirestorePagingAdapter<PostsModel, TimeLineAdapter> firebaseRecyclerAdapter;
    private String blogPostId;
    private CollectionReference user_info;
    private DatabaseReference comment_Ref;
    private ActivityMyPostsBinding binding;
    ArrayAdapter<String> report_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_posts);
        context = activity = this;
        iniViews();
        getPosts();
    }

    private void iniViews() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(this);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        report_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1);
        handleClick();
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(view -> onBackPressed());
    }

    private void getInfo(TimeLineAdapter holder, String user_id) {
        user_info.document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString(DBConstants.name);
                        String image = task.getResult().getString(DBConstants.image);
                        String buzo_id = task.getResult().getString(DBConstants.super_live_id);
                        if (name != null) {
                            if (name.equals("") || TextUtils.isEmpty(name)) {
                                holder.tv_user_name.setText(buzo_id + "");
                            } else {
                                holder.tv_user_name.setText(name + "");
                            }
                        }
                        if (image != null) {

                            Glide.with(getApplicationContext())
                                    .load(image)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.05f)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(holder.iv_user_image);
                        }


                    }

                });


    }

    private void getPosts() {

        iniFirebaseOptions();
        comment_Ref = FirebaseDatabase.getInstance().getReference().child(DBConstants.Post_Comments);
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        firebaseRecyclerAdapter = new FirestorePagingAdapter<PostsModel, TimeLineAdapter>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TimeLineAdapter holder, int position, @NonNull PostsModel model) {
                blogPostId = model.post_id;
                holder.tv_user_name.setText(model.getUser_name());
                holder.tv_date_time.setText(Commn.getTimeAgo(model.getTimestamp().getTime()));
                holder.tv_caption.setText(model.getPost_caption());


                Glide.with(getApplicationContext())
                        .load(model.getPost_image())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.05f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.iv_post_image);


                holder.iv_post_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, ViewImageActivity.class);
                        intent.putExtra(DBConstants.user_image, model.getPost_image());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                holder.tv_follow.setVisibility(View.GONE);
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
                if (blogPostId != null) {
                    getCommentCount(holder);
                }

                holder.iv_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra(Commn.MODEL, new Gson().toJson(model));
                        startActivity(intent);
                    }
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
                        binding.tvEmptyPosts.setVisibility(View.VISIBLE);
                        break;
                    case LOADED:
                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");
                        if (getItemCount() > 0) {

                            binding.tvEmptyPosts.setVisibility(View.GONE);

                        } else {
                            binding.tvEmptyPosts.setVisibility(View.VISIBLE);
                        }
                        break;
                    case FINISHED:
                        Commn.showDebugLog("PAGING log:All notificationModel loaded:");
                        if (firebaseRecyclerAdapter != null) {
                            if (firebaseRecyclerAdapter.getItemCount() > 0) {

                                binding.tvEmptyPosts.setVisibility(View.GONE);

                            } else {
                                binding.tvEmptyPosts.setVisibility(View.VISIBLE);
                            }
                        }
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
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot.exists()) {
                            holder.iv_like.setLiked(true);
                        } else {
                            holder.iv_like.setLiked(false);

                        }
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

    private void updateLike(boolean isLike, String postid) {
        if (isLike) {
            updateMyLike(postid);
        } else {
            removeMyLike(postid);
        }


    }

    private void removeMyLike(String postid) {
        firebaseFirestore.collection(DBConstants.User_Posts + "/" + postid + "/" + DBConstants.User_Likes).document(profilePOJO.getUserId())

                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    firebaseFirestore.collection("User_Posts/" + postid + "/User_Likes")
                            .document(profilePOJO.getUserId()).delete();
                }

            }
        });
    }

    private void updateMyLike(String postid) {

        firebaseFirestore.collection("User_Posts/" + postid + "/User_Likes").document(profilePOJO.getUserId())

                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                Map<String, Object> likesMap = new HashMap<>();
                likesMap.put("timestamp", FieldValue.serverTimestamp());
                firebaseFirestore.collection("User_Posts/" + postid + "/User_Likes")
                        .document(profilePOJO.getUserId()).set(likesMap);

            }
        });


    }


    private void iniFirebaseOptions() {
        Query query = firebaseFirestore.collection(DBConstants.User_Posts).orderBy(DBConstants.timestamp, Query.Direction.DESCENDING)
                .whereEqualTo(DBConstants.user_id, profilePOJO.getUserId());
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(4)
                .setPageSize(3)
                .build();
        firestoreRecyclerOptions = new FirestorePagingOptions.Builder<PostsModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, PostsModel.class)
                .build();


    }
}
