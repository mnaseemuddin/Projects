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
import com.google.gson.reflect.TypeToken;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Activities.CartListActivity;
import com.app.cryptok.LiveShopping.Adapter.StreamProductAdapter;
import com.app.cryptok.databinding.FragmentProductSheetBinding;
import com.app.cryptok.databinding.StreamProductsLayoutBinding;
import com.app.cryptok.LiveShopping.Model.ProductModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class ProductSheetFragment extends BottomSheetDialogFragment {

    private FragmentProductSheetBinding binding;

    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private StreamProductAdapter streamProductAdapter = new StreamProductAdapter();
    private CompositeDisposable disposable = new CompositeDisposable();
    ArrayList<ProductModel.ProductsDatum> recieved_product_model = new ArrayList<>();


    public ProductSheetFragment() {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_sheet, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        iniViews();
        iniIntent();
        adapterInit();
        handleClick();

    }

    private void adapterInit() {
        streamProductAdapter.onProductClick = new StreamProductAdapter.OnProductClick() {
            @Override
            public void onProductClick(int type, int position, ProductModel.ProductsDatum model, StreamProductsLayoutBinding holder_binding) {

                switch (type) {
                    case 2:

                        break;
                    case 3:
                        addToCartApi(model, holder_binding);
                        break;
                }
            }
        };
    }

    private void iniViews() {
        sessionManager = new SessionManager(binding.getRoot().getContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
    }

    private void handleClick() {

        binding.btProceed.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), CartListActivity.class);
            startActivity(intent);
        });
    }

    private void addToCartApi(ProductModel.ProductsDatum model, StreamProductsLayoutBinding holder_binding) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        map.put("product_id", model.getTblUserProductsId());
        map.put("price", model.getProPrice());
        Commn.showDebugLog("addToCartApi_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().addToCartApi(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("addToCartApi_response:" + new Gson().toJson(dates));
                        holder_binding.ivSelectProduct.setImageDrawable(binding.getRoot().getResources().getDrawable(R.drawable.ic_baseline_check_24));
                        Commn.myToast(binding.getRoot().getContext(), dates.getMessage());

                    }
                }));
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