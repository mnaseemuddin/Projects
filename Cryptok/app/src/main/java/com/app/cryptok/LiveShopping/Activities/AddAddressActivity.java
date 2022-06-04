package com.app.cryptok.LiveShopping.Activities;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.databinding.ActivityAddAddressBinding;
import com.app.cryptok.LiveShopping.Model.AddressListModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddAddressActivity extends AppCompatActivity {

    private ActivityAddAddressBinding binding;
    private Context context;
    private AddAddressActivity activity;
    private String mCountry, mName, mMobile, mEmail, mPincode, mNearby, mLandmark, mTownCity, mState, mFullAddress, mAddress_type;
    private boolean isFilled = false;
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private CompositeDisposable disposable = new CompositeDisposable();

    private boolean toAdd = true;
    private AddressListModel.Datum address_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address);
        context = activity = this;
        iniViews();
        iniIntent();
        handleClick();
    }

    private void iniIntent() {

        if (getIntent().hasExtra(Commn.MODEL)) {
            binding.btnAddAddress.setText("Update Address");
            binding.tvHeadTitle.setText("Update Address");
            toAdd = false;
            address_model = new Gson().fromJson(getIntent().getStringExtra(Commn.MODEL), AddressListModel.Datum.class);
            if (address_model != null) {
                setAddressInfo();
            }
        } else {
            toAdd = true;
        }
    }

    private void setAddressInfo() {

        binding.tvAddressType.setText(address_model.getAddressTitle() + "");
        binding.etFullName.setText(address_model.getName() + "");
        binding.etFullAddress.setText(address_model.getAddress() + "");
        binding.etNearby.setText(address_model.getNearBy() + "");
        binding.etState.setText(address_model.getState() + "");
        binding.etTownCity.setText(address_model.getCity() + "");
        binding.etLandmark.setText(address_model.getHouseNo() + "");
        binding.etPincode.setText(address_model.getPinCode() + "");
        binding.etSelectCountry.setText(address_model.getCountry() + "");
        binding.etEmail.setText(address_model.getEmail() + "");
        binding.etMobile.setText(address_model.getMobileNo() + "");
    }

    private void iniViews() {
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void handleClick() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toAdd) {
                    validateAddress();
                } else {
                    updateAddress();
                }

            }
        });
        binding.tvAddressType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddressType();
            }
        });
    }

    private void showAddressType() {
        PopupMenu popupMenu = new PopupMenu(activity, binding.tvAddressType);

        popupMenu.getMenu().add("Home");

        popupMenu.getMenu().add("Office");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                mAddress_type = item.getTitle().toString();
                binding.tvAddressType.setText(mAddress_type);
                if (item.getTitle().equals("Delete Post")) {

                }
                if (item.getTitle().equals("Report Post")) {

                }


                return true;
            }
        });
    }

    private void updateAddress() {
        getInputString();
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        map.put("tbl_user_addressbook_id", address_model.getAddressId() + "");
        map.put("address_title", mAddress_type + "");
        map.put("name", mName + "");
        map.put("mobileNo", mMobile + "");
        map.put("nearBy", mNearby + "");
        map.put("city", mTownCity + "");
        map.put("pin_code", mPincode + "");
        map.put("state", mState + "");
        map.put("country", mCountry + "");
        map.put("address", mFullAddress + "");
        map.put("houseNo", mLandmark + "");
        map.put("email", mEmail + "");
        Commn.showProgress(context);
        Commn.showDebugLog("updateAddressApi_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().updateAddressApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.hideProgress();
                        Commn.showDebugLog("updateAddress_response:" + new Gson().toJson(dates));
                        onBackPressed();
                    }
                }));
    }

    private void validateAddress() {

        getInputString();

        isFilled = true;

        if (TextUtils.isEmpty(mCountry)) {
            binding.etSelectCountry.setError("Enter Country Name");
            binding.etSelectCountry.requestFocus();
            isFilled = false;
        }
        if (TextUtils.isEmpty(mName)) {
            binding.etFullName.setError("Enter Full Name");
            binding.etFullName.requestFocus();
            isFilled = false;
        }
        if (TextUtils.isEmpty(mFullAddress)) {
            binding.etFullAddress.setError("Enter Full Address");
            binding.etFullAddress.requestFocus();
            isFilled = false;
        }
        if (TextUtils.isEmpty(mMobile)) {
            binding.etMobile.setError("Enter Mobile");
            binding.etMobile.requestFocus();
            isFilled = false;
        }
        if (TextUtils.isEmpty(mEmail)) {
            binding.etEmail.setError("Enter Email");
            binding.etEmail.requestFocus();
            isFilled = false;
        }
        if (TextUtils.isEmpty(mPincode)) {
            binding.etPincode.setError("Enter Pincode");
            binding.etPincode.requestFocus();
            isFilled = false;
        }
        if (TextUtils.isEmpty(mNearby)) {
            binding.etNearby.setError("Enter Nearby");
            binding.etNearby.requestFocus();
            isFilled = false;
        }

        if (TextUtils.isEmpty(mTownCity)) {
            binding.etTownCity.setError("Enter Town/City");
            binding.etTownCity.requestFocus();
            isFilled = false;
        }
        if (TextUtils.isEmpty(mState)) {
            binding.etState.setError("Enter Town/City");
            binding.etState.requestFocus();
            isFilled = false;
        }
        if (TextUtils.isEmpty(mAddress_type)) {
            binding.tvAddressType.setError("Enter Town/City");
            binding.tvAddressType.requestFocus();
            isFilled = false;
        }
        if (isFilled) {
            addAddressApi();
        }
    }

    private void getInputString() {
        mCountry = binding.etSelectCountry.getText().toString();
        mName = binding.etFullName.getText().toString();
        mMobile = binding.etMobile.getText().toString();
        mEmail = binding.etEmail.getText().toString();
        mPincode = binding.etPincode.getText().toString();
        mNearby = binding.etNearby.getText().toString();
        mLandmark = binding.etLandmark.getText().toString();
        mTownCity = binding.etTownCity.getText().toString();
        mState = binding.etState.getText().toString();
        mFullAddress = binding.etFullAddress.getText().toString();
        mAddress_type = binding.tvAddressType.getText().toString();
    }

    private void addAddressApi() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        map.put("address_title", mAddress_type + "");
        map.put("name", mName + "");
        map.put("mobileNo", mMobile + "");
        map.put("nearBy", mNearby + "");
        map.put("city", mTownCity + "");
        map.put("pin_code", mPincode + "");
        map.put("state", mState + "");
        map.put("country", mCountry + "");
        map.put("address", mFullAddress + "");
        map.put("houseNo", mLandmark + "");
        map.put("email", mEmail + "");

        Commn.showProgress(context);
        Commn.showDebugLog("addAddressApi_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().addAddressApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.hideProgress();
                        Commn.showDebugLog("addAddressApi_response:" + new Gson().toJson(dates));

                        onBackPressed();
                    }
                }));
    }
}