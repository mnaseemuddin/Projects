package com.app.cryptok.LiveShopping.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.LiveShopping.Interface.OnAddressSelection;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Activities.AddAddressActivity;
import com.app.cryptok.LiveShopping.Adapter.AddressListAdapter;
import com.app.cryptok.databinding.FragmentAddressListBottomSheetBinding;
import com.app.cryptok.LiveShopping.Model.AddressListModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class AddressListBottomSheet extends BottomSheetDialogFragment {

    private AddressListAdapter addressListAdapter = new AddressListAdapter(Commn.CHOOSE_TYPE);
    private CompositeDisposable disposable = new CompositeDisposable();
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    AddressListModel.Datum selected_address;
    private FragmentAddressListBottomSheetBinding binding;

    public OnAddressSelection onAddressSelection;

    public AddressListBottomSheet(OnAddressSelection onAddressSelection) {
        // Required empty public constructor
        this.onAddressSelection = onAddressSelection;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_address_list_bottom_sheet, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniViews();
        getAddressList(false);
        handleClick();
        iniObserver();
    }

    private void handleClick() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void iniObserver() {
        addressListAdapter.onAddressHolder = (type, position, model, holder_binding) -> {
            selected_address = model;
            switch (type) {
                case 1:
                    deleteAddress(model, position);
                    break;
                case 2:
                    editAddress(model);
                    break;

                case 3:

                    setAddressinfo();
                    break;
            }
        };
    }

    private void setAddressinfo() {

        onAddressSelection.onAddressSelect(selected_address);
        dismiss();
    }

    private void editAddress(AddressListModel.Datum model) {

        Intent intent = new Intent(binding.getRoot().getContext(), AddAddressActivity.class);
        intent.putExtra(Commn.MODEL, new Gson().toJson(model));
        binding.getRoot().getContext().startActivity(intent);
    }

    private void deleteAddress(AddressListModel.Datum model, int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("address_id", model.getAddressId() + "");
        Commn.showDebugLog("deleteAddress:" + new Gson().toJson(map));

        disposable.add(MyApi.initRetrofit().deleteAddressApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {

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

    private void iniViews() {
        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        binding.rvAddressList.setAdapter(addressListAdapter);
    }

    private void getAddressList(boolean isLoadMore) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");

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
}