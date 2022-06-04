package com.app.cryptok.fragment.stream;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.activity.SingleLiveStreamPreview;
import com.app.cryptok.adapter.LiveStreamAdapter.HotLiveHolder;
import com.app.cryptok.databinding.FragmentAllPrivateStreamsBinding;
import com.app.cryptok.databinding.HotLiveLayoutBinding;
import com.app.cryptok.databinding.JoinPrivateStreamDialogBinding;
import com.app.cryptok.go_live_module.LiveConst;
import com.app.cryptok.go_live_module.Model.GoLiveModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.view_live_module.HotLiveModel;
import com.app.cryptok.wallet_module.WalletActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AllPrivateStreams extends Fragment {

    private FragmentAllPrivateStreamsBinding binding;
    private FirestorePagingOptions<HotLiveModel> firebaseRecyclerOptions;
    private FirestorePagingAdapter<HotLiveModel, HotLiveHolder> adapter;

    private CollectionReference user_info;
    private FirebaseFirestore firebaseFirestore;
    private ProfilePOJO profilePOJO;
    private SessionManager sessionManager;

    //selected stream info
    private AlertDialog stream_dialog;

    public AllPrivateStreams() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_private_streams, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniPojo();
        handleClick();
        iniadapter();
        loadLiveVideos();
    }

    private void handleClick() {

    }

    private void iniPojo() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void iniadapter() {
        binding.rvPrivateStreams.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 2));
        binding.rvPrivateStreams.setHasFixedSize(true);

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
        adapter = new FirestorePagingAdapter<HotLiveModel, HotLiveHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull HotLiveHolder hotLiveHolder, int position, @NonNull HotLiveModel hotLiveModel) {

                HotLiveLayoutBinding holder_bind = hotLiveHolder.getHolder_binding();
                holder_bind.tvLiveTitle.setText(hotLiveModel.getStream_title());
                hotLiveHolder.itemView.setOnClickListener(view -> {
                    showSelectedStreamInfo(hotLiveModel);
                    // checkMyDiamond(hotLiveModel);
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

        binding.rvPrivateStreams.setAdapter(adapter);
    }

    private void checkMyDiamond(HotLiveModel hotLiveModel) {

        if (profilePOJO == null || hotLiveModel == null) {
            return;
        }
        Commn.showProgress(binding.getRoot().getContext());
        GoLiveModel goLiveModel = new Gson().fromJson(hotLiveModel.getGO_LIVE_PARAMS(), GoLiveModel.class);

        FirebaseDatabase.getInstance()
                .getReference()
                .child(DBConstants.Current_Viewer)
                .child(hotLiveModel.getUser_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Commn.hideProgress();
                        if (snapshot.exists()) {
                            int current_viewers = (int) snapshot.getChildrenCount();
                            int total_entry_limit = Integer.parseInt(goLiveModel.getTotal_peoples_allow());
                            Commn.showDebugLog("current_viewers:" + current_viewers + ",total_entry_limit:" +
                                    "" + total_entry_limit);

                            iniPrivateStream(current_viewers, total_entry_limit, hotLiveModel);
                        } else {
                            iniPrivateStream(0, Integer.parseInt(goLiveModel.getTotal_peoples_allow())
                                    , hotLiveModel);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (error != null) {
                            Commn.hideProgress();
                        }

                    }
                });


    }

    private void showSelectedStreamInfo(HotLiveModel hotLiveModel) {
        if (hotLiveModel == null||hotLiveModel.getGO_LIVE_PARAMS()==null) {
            return;
        }
        GoLiveModel goLiveModel = new Gson().fromJson(hotLiveModel.getGO_LIVE_PARAMS(), GoLiveModel.class);
        if (goLiveModel == null) {
            return;
        }
        JoinPrivateStreamDialogBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(binding.getRoot().getContext())
                , R.layout.join_private_stream_dialog,
                (ViewGroup) binding.getRoot(), false);
        stream_dialog = new AlertDialog.Builder(binding.getRoot().getContext()).create();
        stream_dialog.setView(dialogBinding.getRoot());
        stream_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBinding.tvEntryDiamond.setText(goLiveModel.getEntry_diamonds());
        dialogBinding.tvEntryLimit.setText(goLiveModel.getTotal_peoples_allow());
        stream_dialog.show();

        dialogBinding.tvDissmiss.setOnClickListener(view -> {

            hideStreamDialog();
        });
        dialogBinding.btJoinStream.setOnClickListener(view -> {

            checkMyDiamond(hotLiveModel);

        });

    }

    private void hideStreamDialog() {
        if (stream_dialog != null) {
            if (stream_dialog.isShowing()) {
                stream_dialog.dismiss();
            }
        }
    }

    private void iniPrivateStream(int current_viewers, int total_entry_limit
            , HotLiveModel hotLiveModel) {

        if (current_viewers < total_entry_limit) {

            firebaseFirestore.collection(DBConstants.UserInfo)
                    .document(profilePOJO.getUserId())
                    .collection(DBConstants.my_all_joined_broadcast)
                    .document(hotLiveModel.getStream_id())
                    .get()
                    .addOnCompleteListener(task -> {
                        hideStreamDialog();
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                if (task.getResult().exists()) {
                                    Commn.showDebugLog("you already paid to join this stream");
                                    joinStream(hotLiveModel);
                                } else {
                                    Commn.showDebugLog("you did't paid join this stream yet");
                                    iniToJoin(hotLiveModel);
                                }
                            }
                        }
                    });
        } else {
            entryOver();
        }
    }

    private void entryOver() {

        lowCoins(LiveConst.entry_over_title,
                LiveConst.entry_over_msg,
                LiveConst.entry_over);
    }

    private void iniToJoin(HotLiveModel hotLiveModel) {
        GoLiveModel goLiveModel = new Gson().fromJson(hotLiveModel.getGO_LIVE_PARAMS(), GoLiveModel.class);
        Commn.showDebugLog("goLiveModel:" + hotLiveModel.getGO_LIVE_PARAMS());
        firebaseFirestore.collection(DBConstants.UserInfo
                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        int current_coins = task.getResult().getLong(DBConstants.current_coins).intValue();
                        Commn.showDebugLog("current_coins:" + current_coins + ",selected_coins:" + goLiveModel.getEntry_diamonds() + "");
                        if (Integer.parseInt(goLiveModel.getEntry_diamonds()) <= current_coins) {

                            Intent intent = new Intent(binding.getRoot().getContext(), SingleLiveStreamPreview.class);
                            intent.putExtra(LiveConst.STREAM_MODEL, new Gson().toJson(hotLiveModel));
                            startActivity(intent);

                        } else {

                            lowCoins(LiveConst.low_diamonds_title,
                                    LiveConst.low_diamonds_msg,
                                    LiveConst.low_diamonds);

                        }
                    } else {
                        lowCoins(LiveConst.low_diamonds_title,
                                LiveConst.low_diamonds_msg,
                                LiveConst.low_diamonds);
                        Commn.showDebugLog("current_coins:" + "0" + "");

                    }
                });

    }

    private void joinStream(HotLiveModel hotLiveModel) {
        Intent intent = new Intent(binding.getRoot().getContext(), SingleLiveStreamPreview.class);
        intent.putExtra(LiveConst.STREAM_MODEL, new Gson().toJson(hotLiveModel));
        startActivity(intent);
    }

    private void lowCoins(String title, String msg, String type) {

        final SweetAlertDialog dialog = new SweetAlertDialog(binding.getRoot().getContext(), SweetAlertDialog.ERROR_TYPE);

        dialog.setTitleText(title)
                .setContentText(msg)
                .show();

        dialog.setCancelClickListener(sweetAlertDialog -> {
            dialog.dismiss();
        });

        dialog.setConfirmClickListener(sweetAlertDialog -> {
            dialog.dismiss();

            if (type.equalsIgnoreCase(LiveConst.low_diamonds)) {
                Intent intent = new Intent(binding.getRoot().getContext(), WalletActivity.class);
                binding.getRoot().getContext().startActivity(intent);
            } else {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        });
    }

    private void iniStreamFirebaseOptions() {
        Query query = firebaseFirestore.collection(DBConstants.private_stream)
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
                });
    }
}