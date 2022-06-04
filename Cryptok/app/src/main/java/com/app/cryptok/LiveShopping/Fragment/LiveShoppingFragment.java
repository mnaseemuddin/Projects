package com.app.cryptok.LiveShopping.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;

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
import com.app.cryptok.LiveShopping.Activities.LiveShoppingPreviewActivity;
import com.app.cryptok.adapter.LiveStreamAdapter.HotLiveHolder;
import com.app.cryptok.databinding.FragmentLiveShoppingBinding;
import com.app.cryptok.databinding.HotLiveLayoutBinding;
import com.app.cryptok.LiveShopping.Model.LiveShoppingModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;


public class LiveShoppingFragment extends Fragment {

    private FragmentLiveShoppingBinding binding;
    private FirestorePagingOptions<LiveShoppingModel> firebaseRecyclerOptions;
    private FirestorePagingAdapter<LiveShoppingModel, HotLiveHolder> adapter;

    private CollectionReference user_info;
    private FirebaseFirestore firebaseFirestore;
    private ProfilePOJO profilePOJO;
    private SessionManager sessionManager;


    public LiveShoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_shopping, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionManager = new SessionManager(binding.getRoot().getContext());
        iniPojo();
        loadLiveVideos();
    }

    private void iniPojo() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }


    private void loadLiveVideos() {
        binding.refreshPrivateStreams.post(() -> {
            binding.refreshPrivateStreams.setRefreshing(true);
            getLiveVideos();

        });
        binding.refreshPrivateStreams.setOnRefreshListener(this::getLiveVideos);
    }

    private void getLiveVideos() {
        iniStreamFirebaseOptions();
        adapter = new FirestorePagingAdapter<LiveShoppingModel, HotLiveHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull HotLiveHolder hotLiveHolder, int position, @NonNull LiveShoppingModel hotLiveModel) {

                HotLiveLayoutBinding holder_bind = hotLiveHolder.getHolder_binding();

                holder_bind.tvLiveTitle.setText(hotLiveModel.getStream_title());
                // hotLiveHolder.stream_thumb.setColorFilter(0xff555555, PorterDuff.Mode.MULTIPLY);


                hotLiveHolder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(holder_bind.getRoot().getContext(), LiveShoppingPreviewActivity.class);
                    intent.putExtra(DBConstants.LiveShoppingModel, new Gson().toJson(hotLiveModel));
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
                        binding.refreshPrivateStreams.setRefreshing(false);

                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");
                        break;
                    case FINISHED:
                        binding.refreshPrivateStreams.setRefreshing(false);
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

        binding.rvLiveShoppingStreams.setAdapter(adapter);
    }

    private void iniStreamFirebaseOptions() {
        Query query = firebaseFirestore.collection(DBConstants.LiveShopping)
                .whereNotEqualTo(DBConstants.user_id, profilePOJO.getUserId());

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        firebaseRecyclerOptions = new FirestorePagingOptions.Builder<LiveShoppingModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, LiveShoppingModel.class)
                .build();
    }

    private void getUserInfo(HotLiveLayoutBinding hotLiveHolder, String user_id) {
        user_info.document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        String image = task.getResult().getString(DBConstants.image);
                        if (image != null) {
                            if (getActivity() != null) {
                                if (!getActivity().isFinishing()) {
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