package com.app.cryptok.wallet_module;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.activity.PurchaseCoinsActivity;
import com.app.cryptok.databinding.FragDiamondWalletBinding;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

public class DiamondFragment extends Fragment {

    private FragDiamondWalletBinding binding;
    private FirebaseFirestore firebaseFirestore;
    ProfilePOJO profilePOJO;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_diamond_wallet, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniFirebase();
        getCurrentCoins();
        handleClick();
    }

    private void iniFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
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

    private void handleClick() {

        binding.llTraknpay.setOnClickListener(view -> startActivity(new Intent(binding.getRoot().getContext(), PurchaseCoinsActivity.class)
                .putExtra(DBConstants.PAYMENT_MODE, DBConstants.TRAKNPAY)

        ));
        binding.llRazorpay.setOnClickListener(view -> startActivity(new Intent(binding.getRoot().getContext(), PurchaseCoinsActivity.class)
                .putExtra(DBConstants.PAYMENT_MODE, DBConstants.RAZORPAY)

        ));


    }
}
