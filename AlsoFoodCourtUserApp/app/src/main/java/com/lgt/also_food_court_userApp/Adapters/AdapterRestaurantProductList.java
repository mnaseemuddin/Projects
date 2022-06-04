package com.lgt.also_food_court_userApp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.also_food_court_userApp.Activities.ViewReviewForProduct;
import com.lgt.also_food_court_userApp.Fragments.BottomSheetAddProduct;
import com.lgt.also_food_court_userApp.Models.ModelProductList;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.Common;

import java.util.List;

public class AdapterRestaurantProductList extends RecyclerView.Adapter<AdapterRestaurantProductList.AdapterHolder> {
    private List<ModelProductList> listProducts;
    private Context context;
    private String food_type;

    public AdapterRestaurantProductList(List<ModelProductList> listProducts, Context context, String food_type) {
        this.listProducts = listProducts;
        this.context = context;
        this.food_type = food_type;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_restaurant_products, parent, false);
        return new AdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRestaurantProductList.AdapterHolder holder, final int position) {
        Glide.with(context).load(listProducts.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.image_not_available_new).error(R.drawable.image_not_available_new)).into(holder.ivPImage);





        holder.tvVotes.setText(listProducts.get(position).getReview_count()+" Votes");
        holder.tvPPrice.setText(listProducts.get(position).getPrice());
        holder.tvReview.setText(listProducts.get(position).getReview());


        holder.tv_fullPrice.setText(listProducts.get(position).getTv_fullPrice());
        holder.tv_fullPrice.setPaintFlags(holder.tv_fullPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        // holder.tvCustomizable.setText(listProducts.get(position).getIsCustomizable());

        holder.ratingBarPList.setRating(Float.parseFloat(listProducts.get(position).getRating()));





        Log.e("typereee",food_type);

        if(listProducts.get(position).getProduct_type().equalsIgnoreCase(food_type)){

            if (food_type.equalsIgnoreCase("Veg")){
                holder.itemView.setVisibility(View.VISIBLE);
                holder.LL_Products.setVisibility(View.VISIBLE);

                holder.ivProductType.setImageDrawable(context.getDrawable(R.drawable.veg));
                holder.tvFoodType.setText(listProducts.get(position).getProduct_type());
                holder.tvPName.setText(listProducts.get(position).getProducts_name());


            }else {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.LL_Products.setVisibility(View.VISIBLE);
                holder.ivProductType.setImageDrawable(context.getDrawable(R.drawable.non_veg_icon));
                holder.tvFoodType.setText(listProducts.get(position).getProduct_type());
                holder.tvPName.setText(listProducts.get(position).getProducts_name());
            }


        }
        holder.llAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send to single product description if any

                BottomSheetAddProduct bottomSheetAddProduct = new BottomSheetAddProduct();
                Bundle bundle = new Bundle();
                bundle.putString("KEY_PRODUCT_ID",listProducts.get(position).getTbl_restaurant_products_id());
                bundle.putString("KEY_PRODUCT_NAME",listProducts.get(position).getProducts_name());
                bottomSheetAddProduct.setArguments(bundle);
                bottomSheetAddProduct.show(((FragmentActivity)context).getSupportFragmentManager(),"");
            }
        });
        holder.tvVotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ViewReviewForProduct.class);
                intent.putExtra(Common.tbl_restaurant_productId,listProducts.get(position).getTbl_restaurant_products_id());
                context.startActivity(intent);
            }
        });
        holder.cardViewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewReviewForProduct.class);
                intent.putExtra(Common.tbl_restaurant_productId,listProducts.get(position).getTbl_restaurant_products_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
//        Log.e("dsdsadsadsad", listProducts.size() + "");
        return listProducts.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder {
        private CardView cardViewProducts;
        private ImageView ivPImage, ivProductType;
        private TextView tvPName, tvFoodType, tvVotes, tvPPrice, tvReview, tvCustomizable, tvPType,tv_fullPrice;
        private RatingBar ratingBarPList;
        private LinearLayout llAddProducts;
        private RelativeLayout rlRestaurantProducts;
        private LinearLayout LL_Products;
        public AdapterHolder(@NonNull View itemView) {
            super(itemView);

            ivPImage = itemView.findViewById(R.id.ivPImage);
            tvPName = itemView.findViewById(R.id.tvPName);
            tvFoodType = itemView.findViewById(R.id.tvFoodType);
            tvVotes = itemView.findViewById(R.id.tvVotes);
            tvPPrice = itemView.findViewById(R.id.tvPPrice);
            tvReview = itemView.findViewById(R.id.tvReview);
            tvCustomizable = itemView.findViewById(R.id.tvCustomizable);
            ratingBarPList = itemView.findViewById(R.id.ratingBarPList);
            llAddProducts = itemView.findViewById(R.id.llAddProducts);
            rlRestaurantProducts = itemView.findViewById(R.id.rlRestaurantProducts);
            ivProductType = itemView.findViewById(R.id.ivProductType);
            tvPType = itemView.findViewById(R.id.tvPType);
            LL_Products= itemView.findViewById(R.id.LL_Products);
            cardViewProducts=itemView.findViewById(R.id.cardViewProducts);

            tv_fullPrice=itemView.findViewById(R.id.tv_fullPrice);


        }
    }
}
