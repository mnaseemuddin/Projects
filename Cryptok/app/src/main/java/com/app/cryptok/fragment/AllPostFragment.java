package com.app.cryptok.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
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
import com.app.cryptok.Notifications.InializeNotification;
import com.app.cryptok.Notifications.NotificationRequest;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.Notifications.Sender;
import com.app.cryptok.R;
import com.app.cryptok.activity.AnotherUserProfile;
import com.app.cryptok.activity.CommentActivity;
import com.app.cryptok.activity.MainActivity;
import com.app.cryptok.activity.SendPostActivity;
import com.app.cryptok.activity.ViewImageActivity;
import com.app.cryptok.adapter.TimeLineAdapter;
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

public class AllPostFragment extends Fragment {
    public AllPostFragment() {

    }

    public static AllPostFragment newInstance() {
        AllPostFragment fragment = new AllPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_all_posts, container, false);
    }


    ImageView iv_post_image;
    MainActivity mainActivity;
    RecyclerView recyclerView;
    ProfilePOJO profilePOJO;
    private SwipeRefreshLayout swipe_referesh;
    private SessionManager sessionManager;
    //database
    private FirebaseFirestore firebaseFirestore;
    private LinearLayoutManager layoutManager;

    //
    FirestorePagingOptions<PostsModel> firestoreRecyclerOptions;
    private FirestorePagingAdapter<PostsModel, TimeLineAdapter> firebaseRecyclerAdapter;
    private PostsModel selected_model;
    private String blogPostId;


    //comments
    BottomCommentsFragment commentsFragment;
    FragmentManager fragmentManager;
    private ProgressBar progress_bar;
    private FirebaseDatabase database;
    private DatabaseReference comment_Ref;

    private CollectionReference user_info;
    ArrayAdapter<String> report_adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        iv_post_image = view.findViewById(R.id.iv_post_image);
        iniViews(view);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionManager = new SessionManager(mainActivity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        iniCommentFragment();

        swipe_referesh.post(() -> getPosts());

        swipe_referesh.setOnRefreshListener(() -> getPosts());


    }

    private void iniCommentFragment() {
        commentsFragment = new BottomCommentsFragment();
        fragmentManager = mainActivity.getSupportFragmentManager();
    }

    private void iniViews(View view) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        report_adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1);
        recyclerView = view.findViewById(R.id.rv_popular);
        swipe_referesh = view.findViewById(R.id.swipe_referesh);
        progress_bar = view.findViewById(R.id.progress_bar);
        layoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(layoutManager);
        handleClick();

    }

    private void handleClick() {
        iv_post_image.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SendPostActivity.class);
            startActivity(intent);
        });
    }

    private void getPosts() {
        iniFirebase();

        iniFirebaseOptions();

        firebaseRecyclerAdapter = new FirestorePagingAdapter<PostsModel, TimeLineAdapter>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TimeLineAdapter holder, int position, @NonNull PostsModel model) {
                blogPostId = model.post_id;
                holder.tv_date_time.setText(Commn.getTimeAgo(model.getTimestamp().getTime()));
                holder.tv_caption.setText(model.getPost_caption());

                if (model.getPost_caption() != null) {
                    if (TextUtils.isEmpty(model.getPost_caption().trim())) {
                        holder.tv_caption.setVisibility(View.GONE);
                    } else {
                        holder.tv_caption.setVisibility(View.VISIBLE);
                    }
                }

                Glide.with(getActivity())
                        .load(model.getPost_image())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.05f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.iv_post_image);

                holder.iv_post_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected_model = model;
                        Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                        intent.putExtra(DBConstants.user_image, model.getPost_image());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                holder.iv_like.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        selected_model = model;
                        updateLike(true, model.getPost_id());
                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                        updateLike(false, model.getPost_id());
                    }
                });
                getLikesFunc(holder);

                if (blogPostId != null) {
                    getCommentCount(holder);
                }

                getInfo(holder, model.getUser_id());
                holder.iv_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected_model = model;
                        Intent intent = new Intent(getActivity(), CommentActivity.class);
                        intent.putExtra(Commn.MODEL, new Gson().toJson(model));
                        startActivity(intent);
                    }
                });

                holder.iv_more.setOnClickListener(view -> {
                    selected_model = model;
                    PopupMenu popupMenu = new PopupMenu(getActivity(), holder.iv_more);
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
                //checkFollowOrNot(holder, model.getUser_id());

             /*   holder.tv_follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkFollow(holder, model.getUser_id());
                    }
                });
               */

                holder.iv_user_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected_model = model;
                        Intent intent = new Intent(getActivity(), AnotherUserProfile.class);

                        intent.putExtra(user_id, model.getUser_id());

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
                        break;
                    case LOADED:
                        progress_bar.setVisibility(View.GONE);
                        swipe_referesh.setRefreshing(false);
                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");
                        break;
                    case FINISHED:
                        progress_bar.setVisibility(View.GONE);
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

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    private void report_post(PostsModel model) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

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

                    Commn.myToast(getActivity(), "Report Added");
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

    private void iniFirebase() {
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        database = FirebaseDatabase.getInstance();
        comment_Ref = database.getReference().child(DBConstants.Post_Comments);
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
            if (!profilePOJO.getUserId().equalsIgnoreCase(selected_model.getUser_id())) {
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
        notificationRequest.setMessage(selected_model.getPost_image() + "");
        notificationRequest.setAlert_type(Commn.POST_LIKE_TYPE);
        notificationRequest.setNotification_data(selected_model.getPost_id());

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(selected_model.getUser_id())
                .collection(DBConstants.Tokens)
                .document(selected_model.getUser_id())
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


    private void iniFirebaseOptions() {
        Query query = firebaseFirestore.collection(DBConstants.User_Posts)
                .orderBy(DBConstants.timestamp, Query.Direction.DESCENDING);


        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();
        firestoreRecyclerOptions = new FirestorePagingOptions.Builder<PostsModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, PostsModel.class)
                .build();


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
                            if (getActivity() != null) {
                                Glide.with(getActivity()).load(image).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder_user).into(holder.iv_user_image);
                            }


                        }

                    }

                });

    }


}
