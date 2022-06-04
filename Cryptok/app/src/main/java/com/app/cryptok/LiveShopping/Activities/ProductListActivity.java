package com.app.cryptok.LiveShopping.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.LiveShopping.Adapter.ProductListAdapter;
import com.app.cryptok.databinding.ActivityProductListBinding;
import com.app.cryptok.databinding.ProductListLayoutBinding;
import com.app.cryptok.LiveShopping.Model.ProductModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.util.HashMap;
import java.util.LinkedHashSet;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductListActivity extends AppCompatActivity {
    private ActivityProductListBinding binding;
    private Context context;
    private ProductListActivity activity;
    ProductListAdapter productListAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    private SessionManager sessionManager;
    private ProfilePOJO profilePOJO;
    private String last_id = "";
    private String action_type;
    ProductModel.ProductsDatum selected_model;
    LinkedHashSet<ProductModel.ProductsDatum> selection_list = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_list);
        context = activity = this;
        iniViews();
        getProductList(false);
        adapterListener();
        handleClick();


    }

    private void adapterListener() {
        selection_list.clear();
        productListAdapter.onProductClick = new ProductListAdapter.OnProductClick() {
            @Override
            public void onProductClick(int type, int position, ProductModel.ProductsDatum model, ProductListLayoutBinding holder_binding) {
                selected_model = model;

                switch (type) {
                    case 1:
                        Intent intent = new Intent(context, AddCatalogsActivity.class);
                        intent.putExtra(Commn.MODEL, new Gson().toJson(model));
                        intent.putExtra(Commn.TYPE, Commn.EDIT_TYPE);
                        context.startActivity(intent);
                        break;
                    case 2:

                        /*if (Commn.CHOOSE_TYPE.equalsIgnoreCase(action_type)) {
                            sendSelectedVideo();
                        }*/
                        break;
                }
            }

            @Override
            public void onProductSelection(int type, int position, ProductModel.ProductsDatum model, ProductListLayoutBinding holder_binding) {
                selected_model = model;

                switch (type) {
                    case 0:
                        selection_list.remove(selected_model);
                        break;
                    case 1:
                        selection_list.add(selected_model);
                        break;
                }
                if (selection_list.size() > 0) {
                    binding.tvDoneSelection.setVisibility(View.VISIBLE);
                } else {
                    binding.tvDoneSelection.setVisibility(View.GONE);
                }
                Commn.showDebugLog("all_Added_list:" + new Gson().toJson(selection_list) + ",total_size:" + selection_list.size());
            }
        };

    }

    private void sendSelectedProducts() {
        Intent intent = new Intent(Commn.CHOOSE_TYPE);
        intent.putExtra(Commn.PRODUCT_MODEL, new Gson().toJson(selection_list));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        onBackPressed();
    }

    private void iniViews() {
        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        if (getIntent().hasExtra(Commn.TYPE)) {
            action_type = getIntent().getStringExtra(Commn.TYPE);
            productListAdapter = new ProductListAdapter(action_type);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        binding.rvProductList.setLayoutManager(gridLayoutManager);
        binding.rvProductList.setAdapter(productListAdapter);
        iniLayoutListner(gridLayoutManager);


    }

    //pagintion
    private int total_item_count;
    private int first_visible_item;
    private int visible_item_count;
    private int previous_Total;
    private boolean load = true;

    //
    private void iniLayoutListner(GridLayoutManager layoutManager) {
        binding.rvProductList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (productListAdapter.mList != null) {
                        if (productListAdapter.mList.size() > 0) {
                            last_id = productListAdapter.mList.get(productListAdapter.mList.size() - 1).getTblUserProductsId();
                            getProductList(true);
                        }
                    }
                    load = true;
                }
            }
        });

    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCatalogsActivity.class);
                intent.putExtra(Commn.TYPE, Commn.ADD_TYPE);
                startActivity(intent);
            }
        });

        binding.tvDoneSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSelectedProducts();
            }
        });
    }

    private void getProductList(boolean isLoadMore) {
        selection_list.clear();
        HashMap<String, Object> map = new HashMap<>();
        map.put(DBConstants.user_id, profilePOJO.getUserId() + "");
        if (isLoadMore){
            map.put(DBConstants.last_id, last_id);
        }

        Commn.showDebugLog("getProductList_params:" + new Gson().toJson(map));
        disposable.add(MyApi.initRetrofit().product_list_Api(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {
                        Commn.showDebugLog("getProductList_response:" + new Gson().toJson(dates));

                        if ("1".equalsIgnoreCase(dates.getStatus())) {
                            if (dates.getProductsData() != null) {
                                if (!dates.getProductsData().isEmpty()) {
                                    if (isLoadMore) {
                                        productListAdapter.loadMore(dates.getProductsData());
                                    } else {
                                        productListAdapter.updateData(dates.getProductsData());
                                    }
                                }
                            }
                        }
                    }
                }));


    }

    @Override
    protected void onResume() {
        super.onResume();
        getProductList(false);
    }
}