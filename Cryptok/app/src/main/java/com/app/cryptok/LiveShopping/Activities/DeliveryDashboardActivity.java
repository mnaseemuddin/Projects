package com.app.cryptok.LiveShopping.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Adapter.DeliveryChargesAdapter;
import com.app.cryptok.databinding.ActivityDeliveryDashboardBinding;
import com.app.cryptok.LiveShopping.Model.DeliveryChargesModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DeliveryDashboardActivity extends AppCompatActivity {
    private ActivityDeliveryDashboardBinding binding;
    private Context context;
    private DeliveryDashboardActivity activity;
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private CompositeDisposable disposable = new CompositeDisposable();

    private DeliveryChargesAdapter adapter = new DeliveryChargesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_delivery_dashboard);
        context = activity = this;
        iniViews();
        handleClick();
        adapterListner();
        getHistory(false);
    }

    private void adapterListner() {
        adapter.onHolderClick = (type, position, model, holder_binding) -> {
            switch (type) {
                case 1:
                    remove(model, position);
                    break;
            }
        };
    }

    private void remove(DeliveryChargesModel.Datum model, int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tbl_delivery_charges_id", model.getTblDeliveryChargesId() + "");
        Commn.showProgress(context);
        Commn.showDebugLog("remove_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().deleteDeliveryCharges(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.hideProgress();
                        Commn.showDebugLog("remove_response:" + new Gson().toJson(dates));
                        if (adapter.mList.size() > 0) {
                            adapter.mList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeRemoved(position, adapter.mList.size());
                        }
                    }
                }));
    }

    private void getHistory(boolean isLoadMore) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");

        Commn.showDebugLog("getHistory_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().getDeliveryChargesList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("getHistory_response:" + new Gson().toJson(dates));

                        adapter.updateData(dates.getData());

                    }
                }));
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddDeliveryChargesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHistory(false);
    }

    private void iniViews() {
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);

        binding.rvDashboardList.setAdapter(adapter);

    }
}