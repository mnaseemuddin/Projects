package com.app.cryptok.LiveShopping.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.cryptok.R;
import com.app.cryptok.databinding.ActivityShoppingZoneBinding;
import com.app.cryptok.utils.Commn;

public class ShoppingZoneActivity extends AppCompatActivity {

    private ActivityShoppingZoneBinding binding;
    private Context context;
    private ShoppingZoneActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_zone);
        context = activity = this;
        handleClick();
    }

    private void handleClick() {
        binding.ivBack.setOnClickListener(view -> onBackPressed());

        binding.llProductList.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductListActivity.class);
            intent.putExtra(Commn.TYPE, Commn.NORMAL_TYPE);
            startActivity(intent);
        });
        binding.llVideos.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductVideosActivity.class);
            intent.putExtra(Commn.TYPE, Commn.NORMAL_TYPE);
            startActivity(intent);
        });
        binding.llMyCart.setOnClickListener(view -> {
            Intent intent = new Intent(context, CartListActivity.class);
            startActivity(intent);
        });
        binding.llManageAddress.setOnClickListener(view -> {
            Intent intent = new Intent(context, ManageAddressActivity.class);
            startActivity(intent);
        });
        binding.llOrderHistory.setOnClickListener(view -> {
            Intent intent = new Intent(context, OrderHistoryActivity.class);
            startActivity(intent);
        });

        binding.llSellOrderHistory.setOnClickListener(view -> {
            Intent intent = new Intent(context, SellOrderHistoryActivity.class);
            startActivity(intent);
        });
        binding.llDeliveryDashboard.setOnClickListener(view -> {
            Intent intent = new Intent(context, DeliveryDashboardActivity.class);
            startActivity(intent);
        });
    }

}