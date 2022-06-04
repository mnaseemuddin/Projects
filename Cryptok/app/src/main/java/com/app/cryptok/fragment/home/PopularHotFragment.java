package com.app.cryptok.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.app.cryptok.adapter.StaticLiveVideosAdpterHome;
import com.app.cryptok.databinding.HotLiveLayoutBinding;
import com.app.cryptok.databinding.StaticLiveVideoAdapterHomeBinding;
import com.app.cryptok.model.StaticLiveVideosModel;
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
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.activity.SingleLiveStreamPreview;
import com.app.cryptok.adapter.LiveStreamAdapter.HotLiveHolder;

import com.app.cryptok.go_live_module.LiveConst;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.BaseFragment;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.view_live_module.HotLiveModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PopularHotFragment extends BaseFragment {
    @Override
    protected int setLayout() {
        return R.layout.popular_hot_fragment;
    }

    RecyclerView rv_nearby;
    Context context;
    Activity activity;
    ImageView ivOffilne;
    private SwipeRefreshLayout nearbyrefresh;
    //firebase
    private FirestorePagingOptions<HotLiveModel> firebaseRecyclerOptions;
    private FirestorePagingAdapter<HotLiveModel, HotLiveHolder> adapter;
    private StaticLiveVideoAdapterHomeBinding staticLiveVideoAdapterHomeBinding;
    StaticLiveVideosAdpterHome adpterHome;
    List<StaticLiveVideosModel> mList = new ArrayList<>();
    RecyclerView rv_nearbystatic;
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
        loadLiveVideos();
        StaticVideosLive();
        iniadapter();

    }



    private void iniPojo() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void iniViews() {
        rv_nearby = find(R.id.rv_nearby);
        nearbyrefresh = find(R.id.nearbyrefresh);
        rv_nearbystatic = find(R.id.rv_nearbystatic);
        ivOffilne = find(R.id.ivOffilne);
        ivOffilne.setVisibility(View.GONE);

    }

    private void iniadapter() {

        rv_nearby.setLayoutManager(new GridLayoutManager(context, 2));
        rv_nearby.setHasFixedSize(true);

    }

    private void loadLiveVideos() {
        nearbyrefresh.post(() -> {
            nearbyrefresh.setRefreshing(true);

            getLiveVideos();

        });
        nearbyrefresh.setOnRefreshListener(this::getLiveVideos);

    }

    private void getLiveVideos() {
        Log.e("checkVideolistComing","oktatvbye");


        iniStreamFirebaseOptions();


        adapter = new FirestorePagingAdapter<HotLiveModel, HotLiveHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull HotLiveHolder hotLiveHolder, int position, @NonNull HotLiveModel hotLiveModel) {

                //
                HotLiveLayoutBinding holder_bind = hotLiveHolder.getHolder_binding();

                holder_bind.tvLiveTitle.setText(hotLiveModel.getStream_title());
                // hotLiveHolder.stream_thumb.setColorFilter(0xff555555, PorterDuff.Mode.MULTIPLY);

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
                        Commn.showDebugLog("PAGING log:error loading notificationModel:");
                        break;
                    case LOADED:
                        //progress_bar.setVisibility(View.GONE);
                        // nearbyrefresh.setRefreshing(true);

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

    private void StaticVideosLive() {


        // mList.clear();
        mList.add(new StaticLiveVideosModel("https://petapixel.com/assets/uploads/2017/08/IMG_0513.jpg", "Charlotte",R.raw.dummyvideo1));
        mList.add(new StaticLiveVideosModel("https://asset1.modelmanagement.com/mm-eyJ0Ijp7InIiOnsibCI6/Ijc1MCIsImgiOiI1MTIi/fX0sImlkIjoiaTExNzIw/OTc2IiwiZiI6ImpwZyJ9.jpg", "Karlie",R.raw.dummyvideo2));
        mList.add(new StaticLiveVideosModel(
                "https://i.pinimg.com/236x/26/93/20/269320439bf3755e607f44c66d2a8fd9.jpg",
                "Victoria",R.raw.dummyvideo3));
        mList.add(new StaticLiveVideosModel(
                "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZmVtYWxlJTIwbW9kZWxzfGVufDB8fDB8fA%3D%3D&w=1000&q=80",
                "Sara",R.raw.dummyvideo4));

        mList.add(new StaticLiveVideosModel(
                "data:image/jpeg;base64,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",
                "Jamy",R.raw.dummyvideo5));
        mList.add(new StaticLiveVideosModel(
                "https://www.byrdie.com/thmb/s_cixjS80A_B4-42DzLEKyn2_V8=/500x350/filters:no_upscale():max_bytes(150000):strip_icc()/cdn.cliqueinc.com__cache__posts__194213__model-skincare-secrets-194213-1497385825039-main.700x0c-79b04602385344de88dd0a18db51f864-010369ad53b445c88ab1329e01a14d14.jpg",
                "Amina",R.raw.dummyvideo6));
        mList.add(new StaticLiveVideosModel(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqtXQa8Embi32-AlHbHyRVmzzwBrIbn-Yt3Q&usqp=CAU",
                "Jimmy",R.raw.dummyvideo7));
        Collections.shuffle(mList);
        //StaticLiveVideosModel random = mList.get(0);

        adpterHome = new StaticLiveVideosAdpterHome(mList,getContext());
        rv_nearbystatic.setLayoutManager(new GridLayoutManager(context, 2));
        rv_nearbystatic.setHasFixedSize(true);
        rv_nearbystatic.setAdapter(adpterHome);


    }
}
