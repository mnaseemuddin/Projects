package com.app.cryptok.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.android.volley.Request;

import com.android.volley.toolbox.StringRequest;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.Interface.Packages.PaymentGatwayListener;
import com.app.cryptok.PaymentGateway.RazorpayActivity;
import com.app.cryptok.PaymentGateway.TrakNpayActivity;
import com.app.cryptok.R;
import com.app.cryptok.adapter.Packages.PackagesAdapter;
import com.app.cryptok.databinding.ActivityPurchaseCoinsBinding;
import com.app.cryptok.model.Packages.PackageModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;


public class PurchaseCoinsActivity extends AppCompatActivity implements PaymentGatwayListener {

    private ActivityPurchaseCoinsBinding binding;
    ProfilePOJO profilePOJO;
    SessionManager sessionManager;
    private FirebaseFirestore firebaseFirestore;
    private Context context;
    private PurchaseCoinsActivity activity;
    private String ORDER_ID = "";
    private List<PackageModel> mList = new ArrayList<>();
    PackagesAdapter adapter;
    private PaymentGatwayListener gatwayListener;
    private String PAYMENT_MODE="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase_coins);
        context = activity = this;

        gatwayListener = this;
        getInfo();
        getPackageList();
        handleClick();
    }


    private void handleClick() {
        binding.ivBack.setOnClickListener(view -> onBackPressed());
    }

    private void getInfo() {
        if (getIntent().hasExtra(DBConstants.PAYMENT_MODE)){
            PAYMENT_MODE=getIntent().getStringExtra(DBConstants.PAYMENT_MODE);
        }
        sessionManager = new SessionManager(context);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getCurrentCoins();

    }

    private void getCurrentCoins() {
        firebaseFirestore.collection(DBConstants.UserInfo
                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {

                    if (value.exists()) {
                        int current_coins = value.getLong(DBConstants.current_coins).intValue();
                        Commn.showDebugLog("current_coins:" + current_coins + "");
                        binding.tvCurrentCoins.setText(String.valueOf(current_coins));
                    } else {
                        Commn.showDebugLog("current_coins:" + "0" + "");
                        binding.tvCurrentCoins.setText("0");
                    }
                });
    }

    private void getPackageList() {

        mList.clear();
        iniAdapter();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MyApi.packages_list_api, response -> {

            binding.progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Commn.showDebugLog("packages_list_api:" + response.toString() + "");
                String status = jsonObject.getString("status");
                if ("1".equalsIgnoreCase(status)) {

                    JSONArray jsonArray = jsonObject.getJSONArray("package_data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String tbl_package_id = jsonObject1.getString("tbl_package_id");
                            String package_name = jsonObject1.getString("package_name");
                            String coin = jsonObject1.getString("coin");
                            String total_coins = jsonObject1.getString("total_coins");
                            String amount = jsonObject1.getString("amount");

                            mList.add(new PackageModel(tbl_package_id, package_name, coin, total_coins, amount));

                        }
                        adapter.notifyDataSetChanged();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            binding.progressBar.setVisibility(View.GONE);

        });
        Commn.requestQueue(getApplicationContext(), stringRequest);

    }

    private void iniAdapter() {
        adapter = new PackagesAdapter(mList, context, this);
        binding.rvPackagesList.setAdapter(adapter);

    }


    @Override
    public void onPackageClick(PackageModel model) {
        Commn.showDebugLog("onPackageClick:" + new Gson().toJson(model).toString() + "");


        if (DBConstants.RAZORPAY.equalsIgnoreCase(PAYMENT_MODE)){
            Intent intent=new Intent(context, RazorpayActivity.class);
            intent.putExtra(DBConstants.DIAMONDS_PACKAGE_JSON,new Gson().toJson(model));
            startActivity(intent);
        }
        if (DBConstants.TRAKNPAY.equalsIgnoreCase(PAYMENT_MODE)){
            Intent intent=new Intent(context, TrakNpayActivity.class);
            intent.putExtra(DBConstants.DIAMONDS_PACKAGE_JSON,new Gson().toJson(model));
            startActivity(intent);
        }



    }
}