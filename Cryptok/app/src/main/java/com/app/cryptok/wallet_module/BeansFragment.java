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
import com.app.cryptok.activity.ExchangeDiamondsActivity;
import com.app.cryptok.databinding.FragBeansBinding;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

public class BeansFragment extends Fragment {

    private FragBeansBinding binding;
    private FirebaseFirestore firebaseFirestore;
    ProfilePOJO profilePOJO;
    SessionManager sessionManager;

    private int current_recieved_beans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.frag_beans, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniFirebase();
        getInfo();
        handleClick();

    }

    private void iniFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void getInfo() {
        firebaseFirestore.collection(DBConstants.UserInfo
                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {

                    if (value.exists()) {
                        current_recieved_beans = value.getLong(DBConstants.current_recieved_beans).intValue();
                        Commn.showDebugLog("current_recieved_beans:" + current_recieved_beans + "");
                        binding.tvCurrentBeans.setText(String.valueOf(current_recieved_beans));
                    } else {
                        Commn.showDebugLog("current_recieved_beans:" + "0" + "");
                        binding.tvCurrentBeans.setText("0");
                    }
                });
    }


    private void handleClick() {
        binding.btExchangeDiamond.setOnClickListener(view -> {

            startActivity(new Intent(binding.getRoot().getContext(), ExchangeDiamondsActivity.class));


        });
    }
}
