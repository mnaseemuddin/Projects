package com.app.cryptok.LiveShopping.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Adapter.ProductVideosAdapter;
import com.app.cryptok.databinding.ActivityProductVideosBinding;
import com.app.cryptok.LiveShopping.Model.ProductVideosModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.FastClickUtil;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductVideosActivity extends AppCompatActivity {
    private Context context;
    private ProductVideosActivity activity;
    private ActivityProductVideosBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private ProductVideosAdapter productVideosAdapter = new ProductVideosAdapter();

    private String last_id = "";

    private ProductVideosModel.VideosDatum video_model;
    private int click_holder_position;
    private String action_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_videos);
        context = activity = this;
        handleClick();
        iniViews();
        getVideosList(false);
        iniLayout();
        iniObserver();
    }

    private void iniObserver() {
        productVideosAdapter.onVideoClick = (type, position, model, holder_binding) -> {
            if (FastClickUtil.isFastClick()) {
                return;
            }
            video_model = model;
            click_holder_position = position;
            switch (type) {
                case 2:
                    deleteVideo();
                    break;
                case 1:
                    if (Commn.CHOOSE_TYPE.equalsIgnoreCase(action_type)) {
                        sendSelectedVideo();
                    } else {
                        opnToplayVideo();
                    }

                    break;
            }
        };
    }

    private void sendSelectedVideo() {
        Intent intent = new Intent(Commn.CHOOSE_TYPE);
        intent.putExtra(Commn.MODEL, new Gson().toJson(video_model));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        onBackPressed();
    }

    private void opnToplayVideo() {

        Intent intent = new Intent(context, PlayVideoActivity.class);
        intent.putExtra(Commn.MODEL, new Gson().toJson(video_model));
        startActivity(intent);
    }

    private void deleteVideo() {
        AlertDialog.Builder ad = new AlertDialog.Builder(binding.getRoot().getContext());
        ad.setTitle("Delete");
        ad.setMessage("Do You Want To Delete this video?");

        ad.setNegativeButton("No", (dialog, i) -> dialog.dismiss());

        ad.setPositiveButton("Yes", (dialog, i) -> {
            dialog.dismiss();
            startDeleteVideo();
        });
        ad.create();
        ad.show();
    }

    private void startDeleteVideo() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        map.put("tbl_user_video_list_id", video_model.getTblUserVideoListId());
        Commn.showDebugLog("delete_Video_api_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().delete_Video_api(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("delete_Video_api_response:" + new Gson().toJson(dates));
                        if ("1".equalsIgnoreCase(dates.getStatus())) {
                            if (productVideosAdapter.mList.size() > 0) {
                                productVideosAdapter.mList.remove(click_holder_position);
                                productVideosAdapter.notifyItemRemoved(click_holder_position);
                                productVideosAdapter.notifyItemRangeRemoved(click_holder_position, productVideosAdapter.mList.size());
                            }
                        }
                    }
                }));
    }

    private void iniLayout() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        binding.rvProductVideosList.setLayoutManager(gridLayoutManager);
        binding.rvProductVideosList.setAdapter(productVideosAdapter);
        iniLayoutListner(gridLayoutManager);
    }

    //pagintion
    private int total_item_count;
    private int first_visible_item;
    private int visible_item_count;
    private int previous_Total;
    private boolean load = true;

    //
    private void iniLayoutListner(GridLayoutManager layoutManager) {
        binding.rvProductVideosList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visible_item_count = layoutManager.getChildCount();
                total_item_count = layoutManager.getItemCount();
                first_visible_item = layoutManager.findFirstVisibleItemPosition();
                Commn.showDebugLog("visible_item_count=" + visible_item_count + "," +
                        "total_item_count=" + total_item_count + ",first_visible_item=" + first_visible_item + "");

                if (load) {

                    if (total_item_count > previous_Total) {
                        previous_Total = total_item_count;
                        load = false;
                    }
                }
                if (!load && (first_visible_item + visible_item_count) >= total_item_count) {
                    Commn.showDebugLog("called_load_more");
                    if (productVideosAdapter.mList != null) {
                        if (productVideosAdapter.mList.size() > 0) {
                            last_id = productVideosAdapter.mList.get(productVideosAdapter.mList.size() - 1).getTblUserVideoListId();
                            getVideosList(true);
                        }
                    }
                    load = true;
                }
            }
        });

    }

    private void iniViews() {
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);

        if (getIntent().hasExtra(Commn.TYPE)) {
            action_type = getIntent().getStringExtra(Commn.TYPE);

        }

    }

    private void getVideosList(boolean isLoadMore) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        if (isLoadMore) {
            map.put(DBConstants.last_id, last_id);
        }
        Commn.showDebugLog("getVideosList_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().productVideosApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("getVideosList_response:" + new Gson().toJson(dates));
                        if (dates.getVideosData() != null) {
                            if (!dates.getVideosData().isEmpty()) {
                                if (isLoadMore) {
                                    productVideosAdapter.loadMore(dates.getVideosData());
                                } else {
                                    productVideosAdapter.updateData(dates.getVideosData());
                                }
                            }
                        }

                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVideosList(false);

    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddShppingVideoActivity.class);
                startActivity(intent);
            }
        });
    }
}