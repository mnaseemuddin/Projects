package com.app.cryptok.LiveShopping.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.app.cryptok.LiveShopping.Interface.OnAddressSelection;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Adapter.CartListAdapter;
import com.app.cryptok.databinding.ActivityCartListBinding;
import com.app.cryptok.LiveShopping.Fragment.AddressListBottomSheet;
import com.app.cryptok.LiveShopping.Model.AddressListModel;
import com.app.cryptok.LiveShopping.Model.CartListModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CartListActivity extends AppCompatActivity implements OnAddressSelection {
    private ActivityCartListBinding binding;
    private Context context;
    private CartListActivity activity;
    private String last_id = "";
    private CartListAdapter cartListAdapter = new CartListAdapter();
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private CompositeDisposable disposable = new CompositeDisposable();
    private CartListModel.Datum cart_list_model;
    public OnAddressSelection onAddressSelection;
    private AddressListModel.Datum selectedAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart_list);
        context = activity = this;
        onAddressSelection = this;
        iniViews();
        iniLayout();
        getCartList(false);
        iniObserver();
        handleClick();
    }

    private void handleClick() {
        binding.noAddress.btAddAddressToProceed.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddAddressActivity.class);
            context.startActivity(intent);
        });
        binding.btSelectAddress.setOnClickListener(view -> {

            showAddressSheet();
        });
        binding.btAddAddress.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddAddressActivity.class);
            context.startActivity(intent);
        });

        binding.placeOrder.tvChangeAddress.setOnClickListener(view -> {
            showAddressSheet();
        });

        binding.placeOrder.tvPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAddress != null) {
                    placeOrderApi();
                }

            }
        });
    }

    private void placeOrderApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        map.put("user_addressbook_id", selectedAddress.getAddressId() + "");
        map.put("payment_type", "COD");

        Commn.showDialog(activity);
        Commn.showDebugLog("placeOrderApi_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().placeOrderApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.hideDialog(activity);
                        Commn.showDebugLog("placeOrderApi_response:" + new Gson().toJson(dates));
                        getCartList(false);
                        Commn.myToast(context, dates.getMessage() + "");

                    }
                }));
    }

    private void showAddressSheet() {

        AddressListBottomSheet addressListBottomSheet = new AddressListBottomSheet(onAddressSelection);
        addressListBottomSheet.show(getSupportFragmentManager(), "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAddress();
    }

    private void checkAddress() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        Commn.showDebugLog("checkAddress_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().checkAddressAvailable(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("checkAddress_response:" + new Gson().toJson(dates));
                        if ("true".equalsIgnoreCase(dates.getStatus())) {
                            addressAvailbleUi();
                        } else {
                            addressNotAvailableUi();
                        }

                    }
                }));
    }

    private void addressNotAvailableUi() {
        binding.LLAddAddressToProceed.setVisibility(View.VISIBLE);
        binding.LLAddressAvailable.setVisibility(View.GONE);
    }

    private void addressAvailbleUi() {
        binding.LLAddAddressToProceed.setVisibility(View.GONE);
        if (binding.LLPlaceOrderUi.getVisibility() == View.VISIBLE) {
            binding.LLAddressAvailable.setVisibility(View.GONE);
        } else {
            binding.LLAddressAvailable.setVisibility(View.VISIBLE);
        }

    }

    private void iniObserver() {
        cartListAdapter.onCartClick = (type, quantity, position, model, holder_binding) -> {
            cart_list_model = model;
            switch (type) {
                case ConstantsKey.update_quantity:
                    updateQuantity(quantity);
                    break;
            }
        };
    }

    private void updateQuantity(int quantity) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tbl_cart_id", cart_list_model.getTblCartId() + "");
        map.put("quantity", String.valueOf(quantity));
        Commn.showDebugLog("updateQuantity_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().update_quantity(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("updateQuantity_response:" + new Gson().toJson(dates));

                        getCartList(false);

                    }
                }));
    }

    private void iniLayout() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvCartList.setLayoutManager(gridLayoutManager);
        binding.rvCartList.setAdapter(cartListAdapter);
        iniLayoutListner(gridLayoutManager);
    }

    private void iniViews() {
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        LocalBroadcastManager.getInstance(binding.getRoot().getContext()).registerReceiver(GetSelectedAddress, new IntentFilter(Commn.CHOOSE_TYPE));

    }

    private void getCartList(boolean isLoadMore) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        if (isLoadMore) {
            map.put(DBConstants.last_id, last_id);
        }
        Commn.showDebugLog("getCartList_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().cart_list_api(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("getCartList_response:" + new Gson().toJson(dates));
                        if ("0".equalsIgnoreCase(dates.getStatus())) {
                            if (!isLoadMore) {
                                hideContent();
                            }
                        } else {
                            showContent();
                            binding.placeOrder.tvTotalOrderAmount.setText(getString(R.string.Rs) + dates.getCart_total() + "");

                        }
                        if (isLoadMore) {
                            cartListAdapter.loadMore(dates.getData());
                        } else {
                            cartListAdapter.updateData(dates.getData());
                        }

                    }
                }));
    }

    private void showContent() {
        binding.rlContainer.setVisibility(View.VISIBLE);
    }

    private void hideContent() {
        binding.rlContainer.setVisibility(View.GONE);
        binding.tvEmptyCart.setVisibility(View.VISIBLE);
    }

    //pagintion
    private int total_item_count;
    private int first_visible_item;
    private int visible_item_count;
    private int previous_Total;
    private boolean load = true;

    //
    private void iniLayoutListner(LinearLayoutManager layoutManager) {
        binding.rvCartList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (cartListAdapter.mList != null) {
                        if (cartListAdapter.mList.size() > 0) {
                            last_id = cartListAdapter.mList.get(cartListAdapter.mList.size() - 1).getTblCartId();
                            getCartList(true);
                        }
                    }
                    load = true;
                }
            }
        });

    }

    @Override
    public void onAddressSelect(AddressListModel.Datum addressModel) {

        selectedAddress = addressModel;
        if (selectedAddress != null) {
            setSelectedAddress();
        }

    }

    private void setSelectedAddress() {

        showAddressSelectedUi();
        binding.placeOrder.tvAddress.setText(selectedAddress.getAddress() + "");
        binding.placeOrder.tvAddressType.setText(selectedAddress.getAddressTitle() + "");
    }

    private void showAddressSelectedUi() {
        binding.LLAddAddressToProceed.setVisibility(View.GONE);
        binding.LLAddressAvailable.setVisibility(View.GONE);
        binding.LLPlaceOrderUi.setVisibility(View.VISIBLE);
    }

    public BroadcastReceiver GetSelectedAddress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction() != null) {
                    if (Commn.CHOOSE_ADDRESS.equalsIgnoreCase(intent.getAction())) {
                        if (intent.hasExtra(Commn.MODEL)) {
                            selectedAddress = new Gson().fromJson(intent.getStringExtra(Commn.MODEL), AddressListModel.Datum.class);
                            setSelectedAddress();
                        }
                    }

                }
            }
        }
    };
}