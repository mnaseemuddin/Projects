package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.also_food_court_userApp.Activities.ViewReviewForProduct;
import com.lgt.also_food_court_userApp.Fragments.BottomSheetAddProduct;
import com.lgt.also_food_court_userApp.Models.ModelProductList;
import com.lgt.also_food_court_userApp.Models.ModelparentProductCategory;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.Common;


import java.util.List;


public class AdapterRestaurantParentProductCategory extends RecyclerView.Adapter<AdapterRestaurantParentProductCategory.HolderRestaurantProducts> {

    private List<ModelparentProductCategory> listParentProductsData;
    private Context context;
    private String food_type;

    public AdapterRestaurantParentProductCategory(List<ModelparentProductCategory> listParentProductsData, Context context, String food_type) {
        this.listParentProductsData = listParentProductsData;
        this.context = context;
        this.food_type = food_type;
    }

    @NonNull
    @Override
    public HolderRestaurantProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_category_product_layout, parent, false);
        return new HolderRestaurantProducts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRestaurantProducts holder, final int position) {

        ModelparentProductCategory model=listParentProductsData.get(position);

        holder.tv_categoryName.setText(model.getCategoryName());
        AdapterRestaurantProductList adapterRestaurantProductList=new AdapterRestaurantProductList(model.getListProducts(),context,food_type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.rv_productData.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.rv_productData.setLayoutManager(layoutManager);
        holder.rv_productData.setAdapter(adapterRestaurantProductList);
        holder.rv_productData.setHasFixedSize(true);


    }

    @Override
    public int getItemCount() {
        Log.e("dsdsadsadsad", listParentProductsData.size() + "");
        return listParentProductsData.size();
    }

    public static class HolderRestaurantProducts extends RecyclerView.ViewHolder {
        private RecyclerView rv_productData;
        private TextView tv_categoryName;


        public HolderRestaurantProducts(@NonNull View itemView) {
            super(itemView);

            rv_productData = itemView.findViewById(R.id.rv_productData);
            tv_categoryName = itemView.findViewById(R.id.tv_categoryName);

        }
    }
}
