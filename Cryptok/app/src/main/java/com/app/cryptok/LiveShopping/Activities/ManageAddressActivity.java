package com.app.cryptok.LiveShopping.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Adapter.AddressListAdapter;
import com.app.cryptok.databinding.ActivityManageAddressBinding;
import com.app.cryptok.LiveShopping.Model.AddressListModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ManageAddressActivity extends AppCompatActivity {
    private ActivityManageAddressBinding binding;
    private Context context;
    private ManageAddressActivity activity;
    private AddressListAdapter addressListAdapter = new AddressListAdapter("");
    private CompositeDisposable disposable = new CompositeDisposable();
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private String last_id = "";
    private AddressListModel.Datum selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_address);
        context = activity = this;
        iniData();
        iniLayout();
        iniObserver();
        getAddressList(false);

        handleclick();
    }

    private void handleclick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddAddressActivity.class);

                context.startActivity(intent);
            }
        });
    }

    private void iniObserver() {
        addressListAdapter.onAddressHolder = (type, position, model, holder_binding) -> {
            selectedAddress = model;
            switch (type) {
                case 1:
                    deleteAddress(model, position);

                    break;
                case 2:
                    editAddress(model);
                    break;
                case 3:
                    choosedAddress();
                    break;
            }
        };
    }

    private void choosedAddress() {
        Intent intent = new Intent(Commn.CHOOSE_ADDRESS);
        intent.putExtra(Commn.MODEL, new Gson().toJson(selectedAddress));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        onBackPressed();
    }

    private void deleteAddress(AddressListModel.Datum model, int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("address_id", model.getAddressId() + "");
        Commn.showDebugLog("deleteAddress:" + new Gson().toJson(map));
        Commn.showProgress(context);
        disposable.add(MyApi.initRetrofit().deleteAddressApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.hideProgress();
                        Commn.showDebugLog("deleteAddress_response:" + new Gson().toJson(dates));
                        if ("1".equalsIgnoreCase(dates.getStatus()))
                            if (addressListAdapter.mList.size() > 0) {
                                addressListAdapter.mList.remove(position);
                                addressListAdapter.notifyItemRemoved(position);
                                addressListAdapter.notifyItemRangeRemoved(position, addressListAdapter.mList.size());
                            }
                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList(false);
    }

    private void editAddress(AddressListModel.Datum model) {

        Intent intent = new Intent(context, AddAddressActivity.class);
        intent.putExtra(Commn.MODEL, new Gson().toJson(model));
        context.startActivity(intent);
    }

    private void iniLayout() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvAddressList.setLayoutManager(gridLayoutManager);
        binding.rvAddressList.setAdapter(addressListAdapter);
        // iniLayoutListner(gridLayoutManager);
    }

    private void getAddressList(boolean isLoadMore) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        if (isLoadMore) {
            map.put(DBConstants.last_id, last_id);
        }
        Commn.showDebugLog("getAddressList_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().getAddressList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("getAddressList_response:" + new Gson().toJson(dates));
                        if (isLoadMore) {
                            addressListAdapter.loadMore(dates.getData());
                        } else {
                            addressListAdapter.updateData(dates.getData());
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
        binding.rvAddressList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (addressListAdapter.mList != null) {
                        if (addressListAdapter.mList.size() > 0) {
                            last_id = addressListAdapter.mList.get(addressListAdapter.mList.size() - 1).getAddressId();
                            getAddressList(true);
                        }
                    }
                    load = true;
                }
            }
        });

    }

    private void iniData() {
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);

    }
}