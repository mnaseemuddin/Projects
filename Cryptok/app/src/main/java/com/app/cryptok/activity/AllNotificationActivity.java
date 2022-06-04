package com.app.cryptok.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.LiveShopping.Activities.LiveShoppingPreviewActivity;
import com.app.cryptok.LiveShopping.Model.LiveShoppingModel;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.R;
import com.app.cryptok.adapter.AllNotificationHolder;
import com.app.cryptok.databinding.ActivityAllNotificationBinding;
import com.app.cryptok.databinding.NotificationsLayoutBinding;
import com.app.cryptok.go_live_module.LiveConst;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.model.RecievedNotificationModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.FastClickUtil;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.view_live_module.HotLiveModel;

import static com.app.cryptok.utils.Commn.FOLLOW_TYPE;

public class AllNotificationActivity extends AppCompatActivity {


    //firebase
    private FirestorePagingOptions<RecievedNotificationModel> firebaseRecyclerOptions;
    private FirestorePagingAdapter<RecievedNotificationModel, AllNotificationHolder> adapter;
    private CollectionReference user_info;
    private FirebaseFirestore firebaseFirestore;
    private ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    private Context context;
    private AllNotificationActivity activity;
    private ActivityAllNotificationBinding binding;

    private RecievedNotificationModel selected_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_notification);
        context = activity = this;
        iniPojo();
        handleClick();
        loadNotification();

    }

    private void loadNotification() {
        binding.notificationRefresh.post(new Runnable() {
            @Override
            public void run() {
                binding.notificationRefresh.setRefreshing(true);
                getNotificaiton();
            }
        });
        binding.notificationRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotificaiton();
            }
        });


    }


    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void iniPojo() {
        sessionManager = new SessionManager(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_info = firebaseFirestore.collection(DBConstants.UserInfo);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void getNotificaiton() {
        iniStreamFirebaseOptions();

        adapter = new FirestorePagingAdapter<RecievedNotificationModel, AllNotificationHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull AllNotificationHolder hotLiveHolder, int position, @NonNull RecievedNotificationModel hotLiveModel) {

                NotificationsLayoutBinding holder_bind = hotLiveHolder.getBinding();

                if (Notification_Const.NORMAL_NOTIFICATION_TYPE.equalsIgnoreCase(hotLiveModel.getNotification_type())) {
                    holder_bind.ivNotificationImage.setVisibility(View.GONE);
                    if (hotLiveModel.getMessage() != null) {
                        if (!hotLiveModel.getMessage().isEmpty()) {
                            holder_bind.tvNotificationMsg.setText(HtmlCompat.fromHtml(hotLiveModel.getMessage(), HtmlCompat.FROM_HTML_MODE_LEGACY));

                        } else {
                            holder_bind.tvNotificationMsg.setText(hotLiveModel.getMessage() + "");

                        }
                    } else {
                        holder_bind.tvNotificationMsg.setText(hotLiveModel.getMessage() + "");
                    }

                } else {
                    holder_bind.ivNotificationImage.setVisibility(View.VISIBLE);
                    if (hotLiveModel.getContext_message() != null) {
                        if (!hotLiveModel.getContext_message().isEmpty()) {
                            holder_bind.tvNotificationMsg.setText(HtmlCompat.fromHtml(hotLiveModel.getContext_message(), HtmlCompat.FROM_HTML_MODE_LEGACY));

                        } else {
                            holder_bind.tvNotificationMsg.setText(hotLiveModel.getContext_message() + "");

                        }
                    } else {
                        holder_bind.tvNotificationMsg.setText(hotLiveModel.getContext_message() + "");
                    }
                    Glide.with(getApplicationContext())
                            .load(hotLiveModel.getMessage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder_bind.ivNotificationImage);
                }

                holder_bind.tvTime.setText(Commn.getTimeAgo(hotLiveModel.getTimestamp().getTime()));

                holder_bind.ivMainUserImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected_model = hotLiveModel;
                        if (FastClickUtil.isFastClick()) {
                            return;
                        }
                        openProfile();
                    }
                });
                holder_bind.ivNotificationImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected_model = hotLiveModel;
                        if (FastClickUtil.isFastClick()) {
                            return;
                        }
                        if (Commn.POST_LIKE_TYPE.equalsIgnoreCase(selected_model.getAlert_type()) ||
                                Commn.POST_COMMENT_TYPE.equalsIgnoreCase(selected_model.getAlert_type())) {

                            openPost();
                        }
                    }
                });
                holder_bind.llNotificationInfo.setOnClickListener(view -> {
                    selected_model = hotLiveModel;
                    if (FastClickUtil.isFastClick()) {
                        return;
                    }
                    if (FOLLOW_TYPE.equalsIgnoreCase(selected_model.getAlert_type())) {

                        openProfile();
                    }
                    if (DBConstants.LiveShopping.equalsIgnoreCase(selected_model.getAlert_type())) {
                        getLiveShopping();
                    }
                    if (Commn.LIVE_TYPE.equalsIgnoreCase(selected_model.getAlert_type())) {
                        getLiveStream();
                    }

                    if (Commn.POST_LIKE_TYPE.equalsIgnoreCase(selected_model.getAlert_type()) ||
                            Commn.POST_COMMENT_TYPE.equalsIgnoreCase(selected_model.getAlert_type())) {

                        openPost();
                    }
                });

                getUserInfo(holder_bind, hotLiveModel);
            }

            @NonNull
            @Override
            public AllNotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_layout, parent, false);
                return new AllNotificationHolder(textView);
            }

            @Override
            public int getItemViewType(int position) {
                return position;
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
                        binding.notificationRefresh.setRefreshing(false);

                        Commn.showDebugLog("PAGING log:total_items loaded:" + getItemCount() + "");
                        break;
                    case FINISHED:
                        binding.notificationRefresh.setRefreshing(false);
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

        binding.rvNotificationsList.setAdapter(adapter);

    }

    private void openPost() {
        Intent intent = new Intent(context, SinglePostActivity.class);
        intent.putExtra(DBConstants.post_id, selected_model.getNotification_data() + "");
        startActivity(intent);

    }


    private void getLiveStream() {
        firebaseFirestore.collection(DBConstants.Single_Streams)
                .document(selected_model.getUser_id())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().exists()) {

                            HotLiveModel model = task.getResult().toObject(HotLiveModel.class);
                            Intent intent = new Intent(context, SingleLiveStreamPreview.class);
                            intent.putExtra(LiveConst.STREAM_MODEL, new Gson().toJson(model));
                            startActivity(intent);

                        } else {
                            Commn.myToast(context, "Broadcast Ended");

                        }
                    } else {
                        Commn.myToast(context, "Broadcast Ended");

                    }

                });
    }


    private void getLiveShopping() {
        firebaseFirestore.collection(DBConstants.LiveShopping)
                .document(selected_model.getUser_id())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().exists()) {
                            LiveShoppingModel model = task.getResult().toObject(LiveShoppingModel.class);
                            Intent intent = new Intent(context, LiveShoppingPreviewActivity.class);
                            intent.putExtra(DBConstants.LiveShoppingModel, new Gson().toJson(model));
                            startActivity(intent);

                        } else {
                            Commn.myToast(context, "Broadcast Ended");

                        }
                    } else {
                        Commn.myToast(context, "Broadcast Ended");

                    }

                });
    }

    private void openProfile() {
        Intent intent = new Intent(context, AnotherUserProfile.class);

        intent.putExtra(DBConstants.user_id, selected_model.getUser_id() + "");

        startActivity(intent);
    }


    private void iniStreamFirebaseOptions() {
        Query query = firebaseFirestore
                .collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .collection(DBConstants.all_notifications)
                .orderBy(DBConstants.timestamp, Query.Direction.DESCENDING);


        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(20)
                .setPageSize(10)
                .build();

        firebaseRecyclerOptions = new FirestorePagingOptions.Builder<RecievedNotificationModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, RecievedNotificationModel.class)
                .build();
    }

    private void getUserInfo(NotificationsLayoutBinding hotLiveHolder, RecievedNotificationModel model) {
        user_info.document(model.getUser_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String image = task.getResult().getString(DBConstants.image);
                            if (image != null) {
                                Glide.with(getApplicationContext()).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.fake_user_icon)
                                        .into(hotLiveHolder.ivMainUserImage);
                            }

                        }
                    }
                });
    }
}