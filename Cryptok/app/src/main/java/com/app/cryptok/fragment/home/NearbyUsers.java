package com.app.cryptok.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.activity.AnotherUserProfile;
import com.app.cryptok.adapter.NearbyUsersAdapter;
import com.app.cryptok.databinding.InflateUsersBinding;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.BaseFragment;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;
import java.util.Map;

import static com.app.cryptok.Api.DBConstants.user_id;

public class NearbyUsers extends BaseFragment {

    @Override
    protected int setLayout() {

        return R.layout.frag_nearby;
    }

    private FirebaseFirestore firebaseFirestore;
    private SwipeRefreshLayout nearbyrefresh;
    RecyclerView rv_nearby;
    Context context;
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    Activity activity;
    private FirestorePagingOptions<ProfilePOJO> firebaseRecyclerOptions;
    private FirestorePagingAdapter<ProfilePOJO, NearbyUsersAdapter> users_adapter;

    private CollectionReference user_info;

    @Override
    protected void onLaunch() {
        context = activity = getActivity();
        rv_nearby = find(R.id.rv_nearby);
        nearbyrefresh = find(R.id.nearbyrefresh);

        iniFirebase();
        loadUsers();

    }

    private void loadUsers() {
        nearbyrefresh.post(() -> {
            nearbyrefresh.setRefreshing(true);
            getUsers();

        });
        nearbyrefresh.setOnRefreshListener(() ->
                getUsers());
    }


    private void iniFirebase() {
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);

    }

    private void getUsers() {
        iniFirebaseOptions();


        users_adapter = new FirestorePagingAdapter<ProfilePOJO, NearbyUsersAdapter>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull NearbyUsersAdapter hotLiveHolder, int position, @NonNull ProfilePOJO hotLiveModel) {

                InflateUsersBinding holder_binding = hotLiveHolder.getBinding();
                holder_binding.tvCity.setText(hotLiveModel.getCountry_name());
                if (hotLiveModel.getName().equals("")) {
                    holder_binding.tvName.setText(hotLiveModel.getSuper_live_id());
                } else {
                    holder_binding.tvName.setText(hotLiveModel.getName());
                }

                // holder_binding.ivUser.setColorFilter(0xff555555, PorterDuff.Mode.MULTIPLY);
                Glide.with(context).
                        load(hotLiveModel.getImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.05f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder_binding.ivUser);

                holder_binding.ivUser.setClipToOutline(true);

                holder_binding.ivUser.setOnClickListener(view -> {

                    Intent intent = new Intent(context, AnotherUserProfile.class);

                    intent.putExtra(user_id, hotLiveModel.getUserId());

                    startActivity(intent);

                });

                holder_binding.tvFollow.setOnClickListener(view -> checkUser(hotLiveModel.getUserId(), holder_binding.tvFollow));


            }

            @NonNull
            @Override
            public NearbyUsersAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_users, parent, false);
                return new NearbyUsersAdapter(textView);
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {

                    case ERROR:
                        Commn.showDebugLog("PAGING log:error loading notificationModel:");
                        break;
                    case LOADED:

                        //progress_bar.setVisibility(View.GONE);
                        nearbyrefresh.setRefreshing(false);

                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");
                        break;
                    case FINISHED:
                        nearbyrefresh.setRefreshing(false);
                        // progress_bar.setVisibility(View.GONE);
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
        rv_nearby.setAdapter(users_adapter);


    }



    private void iniFirebaseOptions() {
        String user_id;

        if (profilePOJO.getUserId() == null) {
            user_id = "temp123";
        } else {
            user_id = profilePOJO.getUserId();
        }
        Query query = firebaseFirestore.collection(DBConstants.UserInfo)
                .orderBy(DBConstants.userId, Query.Direction.DESCENDING)
                .whereNotEqualTo(DBConstants.userId, user_id);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        firebaseRecyclerOptions = new FirestorePagingOptions.Builder<ProfilePOJO>()
                .setLifecycleOwner(this)
                .setQuery(query, config, ProfilePOJO.class)
                .build();


    }


    private void checkUser(String userId, TextView tv_follow) {

        String ref = DBConstants.UserInfo + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Followers;
        firebaseFirestore.collection(ref)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            firebaseFirestore.collection(ref)
                                    .document(profilePOJO.getUserId())
                                    .delete();

                            tv_follow.setText("Follow");

                        } else {
                            tv_follow.setText("Following");
                            follow_user(userId);
                        }
                    }
                });

    }

    private void follow_user(String userId) {
        String currentUserRef = DBConstants.UserInfo + "/" + userId + "/" + DBConstants.User_Followers;
        Map messageMap = new HashMap();
        messageMap.put(user_id, profilePOJO.getUserId());
        messageMap.put(DBConstants.follow_id, profilePOJO.getUserId());
        messageMap.put(DBConstants.user_name, profilePOJO.getName());
        messageMap.put(DBConstants.user_image, profilePOJO.getImage());

        firebaseFirestore.collection(currentUserRef)
                .document(profilePOJO.getUserId())
                .set(messageMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Commn.showDebugLog("followed success");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Commn.showDebugLog("followed Exception:" + e.getMessage() + " ");
            }
        });

    }


}
