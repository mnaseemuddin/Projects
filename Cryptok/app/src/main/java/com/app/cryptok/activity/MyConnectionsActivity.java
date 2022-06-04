package com.app.cryptok.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.adapter.SearchHolder;
import com.app.cryptok.databinding.ActivityMyConnectionsBinding;
import com.app.cryptok.model.Follow.FollowModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

public class MyConnectionsActivity extends AppCompatActivity {

    private boolean isFollower = false;
    private boolean isFollowing = false;
    private boolean isFriend = false;
    private ActivityMyConnectionsBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirestorePagingOptions<FollowModel> firebaseRecyclerOptions;
    private FirestorePagingAdapter<FollowModel, SearchHolder> adapter;
    ProfilePOJO profilePOJO;
    SessionManager sessionManager;
    private Context context;
    private MyConnectionsActivity activity;
    private String user_id;
    private CollectionReference user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_connections);

        context = activity = this;
        iniFirebase();
        handleClick();
        getConnectionType();

        loadConnections();


    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
    }

    private void loadConnections() {
        binding.swipeReferesh.post(() -> {
            binding.swipeReferesh.setRefreshing(true);
            getConnections();
        });
        binding.swipeReferesh.setOnRefreshListener(() ->
                getConnections());
    }

    private void getConnectionType() {

        if (getIntent().hasExtra(DBConstants.user_id)) {
            user_id = getIntent().getStringExtra(DBConstants.user_id);
        }

        if (getIntent().hasExtra(DBConstants.connection_type)) {
            String type = getIntent().getStringExtra(DBConstants.connection_type);
            iniFirebaseOptions(type);
        }
    }

    String currentUserRef;

    private void iniFirebaseOptions(String type) {
        if (type.equalsIgnoreCase(DBConstants.follower_type)) {
            currentUserRef = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Followers;
            binding.tvViewType.setText("Followers");
        }
        if (type.equalsIgnoreCase(DBConstants.following_type)) {
            binding.tvViewType.setText("Following");
            currentUserRef = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.User_Following;
        }
        if (type.equalsIgnoreCase(DBConstants.friends_type)) {
            binding.tvViewType.setText("Friends");
            currentUserRef = DBConstants.UserInfo + "/" + user_id + "/" + DBConstants.Friends;
        }

        Query query = firebaseFirestore.collection(currentUserRef)
                .orderBy(DBConstants.user_id, Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        firebaseRecyclerOptions = new FirestorePagingOptions.Builder<FollowModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, FollowModel.class)
                .build();
    }

    private void getConnections() {
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        adapter = new FirestorePagingAdapter<FollowModel, SearchHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull SearchHolder hotLiveHolder, int position, @NonNull FollowModel hotLiveModel) {

                getUserInfo(hotLiveHolder, hotLiveModel.getUser_id());
                hotLiveHolder.itemView.setOnClickListener(view -> {

                    Intent intent = new Intent(context, AnotherUserProfile.class);
                    intent.putExtra(DBConstants.user_id, hotLiveModel.getUser_id());
                    startActivity(intent);

                });

            }

            @NonNull
            @Override
            public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_users_list, parent, false);
                return new SearchHolder(textView);
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {

                    case ERROR:
                        Commn.showDebugLog("PAGING log:error loading notificationModel:");

                        break;
                    case LOADED:
                        binding.swipeReferesh.setRefreshing(false);
                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");

                        break;
                    case FINISHED:
                        binding.swipeReferesh.setRefreshing(false);
                        Commn.showDebugLog("PAGING log:All notificationModel loaded:" + getItemCount() + "");
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
        binding.rvMyConnections.setAdapter(adapter);


    }

    private void getUserInfo(SearchHolder hotLiveHolder, String user_id) {
        user_info.document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        String image = task.getResult().getString(DBConstants.image);
                        String name = task.getResult().getString(DBConstants.name);
                        String buzoid = task.getResult().getString(DBConstants.super_live_id);
                        if (image != null) {

                            Glide.with(context).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.placeholder_user)
                                    .into(hotLiveHolder.iv_user_image);

                        }

                        if (name != null) {
                            if ("".equalsIgnoreCase(name)){
                                hotLiveHolder.tv_buzo_id.setText(buzoid + "");
                                hotLiveHolder.tv_user_name.setVisibility(View.GONE);
                            }else {
                                hotLiveHolder.tv_user_name.setText(name + "");
                                if (buzoid != null) {
                                    hotLiveHolder.tv_buzo_id.setText(buzoid + "");

                                }
                            }


                        }

                    }
                });

    }


    private void iniFirebase() {

        sessionManager = new SessionManager(context);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();

    }
}