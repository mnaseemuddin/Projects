package com.app.arthasattva.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.app.arthasattva.Api.DBConstants;
import com.app.arthasattva.R;
import com.app.arthasattva.activity.SingleLiveStreamPreview;
import com.app.arthasattva.adapter.LiveStreamAdapter.HotLiveHolder;
import com.app.arthasattva.databinding.HotLiveLayoutBinding;
import com.app.arthasattva.go_live_module.LiveConst;
import com.app.arthasattva.model.ProfilePOJO;
import com.app.arthasattva.utils.BaseFragment;
import com.app.arthasattva.utils.Commn;
import com.app.arthasattva.utils.ConstantsKey;
import com.app.arthasattva.utils.SessionManager;
import com.app.arthasattva.view_live_module.HotLiveModel;

public class PopularHotFragment extends BaseFragment {
    @Override
    protected int setLayout() {
        return R.layout.popular_hot_fragment;
    }
    RecyclerView rv_nearby;
    Context context;
    Activity activity;
    private SwipeRefreshLayout nearbyrefresh;
    //firebase
    private FirestorePagingOptions<HotLiveModel> firebaseRecyclerOptions;
    private FirestorePagingAdapter<HotLiveModel, HotLiveHolder> adapter;

    private CollectionReference user_info;
    private FirebaseFirestore firebaseFirestore;
    private ProfilePOJO profilePOJO;
    private SessionManager sessionManager;

    @Override
    protected void onLaunch() {
        context = activity = getActivity();
        sessionManager = new SessionManager(context);
        iniPojo();
        iniViews();
        iniadapter();
        loadLiveVideos();

    }


    private void iniPojo() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void iniViews() {
        rv_nearby = find(R.id.rv_nearby);
        nearbyrefresh = find(R.id.nearbyrefresh);
    }

    private void iniadapter() {
        rv_nearby.setLayoutManager(new GridLayoutManager(context, 2));
        rv_nearby.setHasFixedSize(true);

    }

    private void loadLiveVideos() {
        nearbyrefresh.post(new Runnable() {
            @Override
            public void run() {
                nearbyrefresh.setRefreshing(true);
                getLiveVideos();

            }
        });
        nearbyrefresh.setOnRefreshListener(this::getLiveVideos);
    }

    private void getLiveVideos() {

        iniStreamFirebaseOptions();

        adapter = new FirestorePagingAdapter<HotLiveModel, HotLiveHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull HotLiveHolder hotLiveHolder, int position, @NonNull HotLiveModel hotLiveModel) {

                HotLiveLayoutBinding holder_bind = hotLiveHolder.getHolder_binding();

                holder_bind.tvLiveTitle.setText(hotLiveModel.getStream_title());
                hotLiveHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(holder_bind.getRoot().getContext(), SingleLiveStreamPreview.class);
                    intent.putExtra(LiveConst.STREAM_MODEL, new Gson().toJson(hotLiveModel));
                    startActivity(intent);
                });
                getUserInfo(holder_bind, hotLiveModel.getUser_id());
            }

            @NonNull
            @Override
            public HotLiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_live_layout, parent, false);
                return new HotLiveHolder(textView);
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {

                    case ERROR:
                        Commn.showDebugLog("PAGING log:error loading data:");
                        break;
                    case LOADED:

                        //progress_bar.setVisibility(View.GONE);
                        nearbyrefresh.setRefreshing(false);

                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");
                        break;
                    case FINISHED:
                        nearbyrefresh.setRefreshing(false);
                        // progress_bar.setVisibility(View.GONE);
                        Commn.showDebugLog("PAGING log:All data loaded:");
                        break;
                    case LOADING_MORE:

                        Commn.showDebugLog("PAGING log:loading next page");
                        break;
                    case LOADING_INITIAL:
                        Commn.showDebugLog("PAGING log:loading initial data");
                        break;
                }
            }
        };

        rv_nearby.setAdapter(adapter);
    }

    private void iniStreamFirebaseOptions() {
        Query query = firebaseFirestore.collection(DBConstants.Single_Streams)
                .whereNotEqualTo(DBConstants.user_id, profilePOJO.getUserId());

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        firebaseRecyclerOptions = new FirestorePagingOptions.Builder<HotLiveModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, HotLiveModel.class)
                .build();
    }

    private void getUserInfo(HotLiveLayoutBinding hotLiveHolder, String user_id) {
        user_info.document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        String image = task.getResult().getString(DBConstants.image);
                        if (image != null) {
                            if (!activity.isFinishing()) {
                                if (getActivity() != null) {
                                    Glide.with(getActivity())
                                            .load(image).
                                            diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .thumbnail(0.05f)
                                            .transition(DrawableTransitionOptions.withCrossFade())
                                            .into(hotLiveHolder.ivLivePreview);
                                }

                            }

                        }

                    }
                });
    }
}
