package com.app.superlive.LiveShopping.Activities;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app.superlive.Api.DBConstants;
import com.app.superlive.Api.MyApi;
import com.app.superlive.R;
import com.app.superlive.adapter.OrderHistoryAdapter;
import com.app.superlive.databinding.ActivityOrderHistoryBinding;
import com.app.superlive.model.ProfilePOJO;
import com.app.superlive.utils.Commn;
import com.app.superlive.utils.ConstantsKey;
import com.app.superlive.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OrderHistoryActivity extends AppCompatActivity {
    private ActivityOrderHistoryBinding binding;
    private Context context;
    private OrderHistoryActivity activity;
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private CompositeDisposable disposable = new CompositeDisposable();
    OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter();
    private String last_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_history);
        context = activity = this;
        iniViews();
        handleClick();
        getOrderHistory(false);
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getOrderHistory(boolean isLoadMore) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        if (isLoadMore) {
            map.put(DBConstants.last_id, last_id + "");
        }
        Commn.showDebugLog("getOrderHistory_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().orderHistoryApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("getOrderHistory_response:" + new Gson().toJson(dates));
                        if (isLoadMore) {
                            orderHistoryAdapter.loadMore(dates.getData());
                        } else {
                            orderHistoryAdapter.updateData(dates.getData());
                        }
                    }
                }));
    }

    //pagintion
    private int total_item_count;
    private int first_visible_item;
    private int visible_item_count;
    private int previous_Total;
    private boolean load = true;

    //
    private void iniLayoutListner(LinearLayoutManager layoutManager) {
        binding.rvOrderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (orderHistoryAdapter.mList != null) {
                        if (orderHistoryAdapter.mList.size() > 0) {
                            last_id = orderHistoryAdapter.mList.get(orderHistoryAdapter.mList.size() - 1).getTblOrdersId();
                            getOrderHistory(true);
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
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvOrderList.setLayoutManager(gridLayoutManager);
        binding.rvOrderList.setAdapter(orderHistoryAdapter);
        iniLayoutListner(gridLayoutManager);

    }
}