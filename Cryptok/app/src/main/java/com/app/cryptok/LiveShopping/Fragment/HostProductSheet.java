package com.app.cryptok.LiveShopping.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Adapter.HostStreamProductAdapter;
import com.app.cryptok.databinding.FragmentHostProductSheetBinding;
import com.app.cryptok.LiveShopping.Model.ProductModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;


public class HostProductSheet extends BottomSheetDialogFragment {

    private FragmentHostProductSheetBinding binding;
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private HostStreamProductAdapter streamProductAdapter = new HostStreamProductAdapter();
    private CompositeDisposable disposable = new CompositeDisposable();
    ArrayList<ProductModel.ProductsDatum> recieved_product_model = new ArrayList<>();

    public HostProductSheet() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_host_product_sheet, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniViews();
        iniIntent();

        handleClick();
    }

    private void iniViews() {
        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void handleClick() {


    }


    private void iniIntent() {
        if (getArguments() != null) {
            if (getArguments().containsKey(DBConstants.LiveShoppingModel)) {


                getProductList();

            }


        }

    }

    private void getProductList() {
        binding.rvProductImages.setAdapter(streamProductAdapter);
        String product_model = getArguments().getString(DBConstants.LiveShoppingModel);
        Commn.showDebugLog("getProductList:" + new Gson().toJson(product_model));

        recieved_product_model = new Gson().fromJson(product_model,
                new TypeToken<ArrayList<ProductModel.ProductsDatum>>() {
                }.getType());

        streamProductAdapter.updateData(recieved_product_model);
    }
}