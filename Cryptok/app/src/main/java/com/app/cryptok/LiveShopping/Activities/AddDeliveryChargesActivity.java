package com.app.cryptok.LiveShopping.Activities;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.databinding.ActivityAddDeliveryChargesBinding;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddDeliveryChargesActivity extends AppCompatActivity {

    private ActivityAddDeliveryChargesBinding binding;
    private Context context;
    private AddDeliveryChargesActivity activity;
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private CompositeDisposable disposable = new CompositeDisposable();

    private String mCartTotal, mDeliveryCharges;
    private boolean isFilled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_delivery_charges);
        context = activity = this;
        iniViews();
        handleClick();
    }

    private void iniViews() {
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(view -> onBackPressed());

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    private void validate() {
        mCartTotal = binding.etCartTotal.getText().toString();
        mDeliveryCharges = binding.etDeliveryCharges.getText().toString();

        isFilled = true;

        if (TextUtils.isEmpty(mCartTotal)) {
            binding.etCartTotal.setError("Enter Cart Total");
            binding.etCartTotal.requestFocus();
            isFilled = false;

        }
        if (TextUtils.isEmpty(mDeliveryCharges)) {
            binding.etDeliveryCharges.setError("Enter Delivery Charges");
            binding.etDeliveryCharges.requestFocus();
            isFilled = false;

        }
        if (isFilled) {
            addDeliveryCharges();
        }
    }

    private void addDeliveryCharges() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        map.put("cart_total",  mCartTotal+ "");
        map.put("charges",mDeliveryCharges + "");
        Commn.showDebugLog("addDeliveryCharges_params:" + new Gson().toJson(map));
        Commn.showProgress(context);
        disposable.add(MyApi.initRetrofit().addDeliveryCharges(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.hideProgress();
                        Commn.showDebugLog("addDeliveryCharges_response:" + new Gson().toJson(dates));
                        Commn.myToast(context, dates.getMessage() + "");
                        onBackPressed();
                    }
                }));
    }
}