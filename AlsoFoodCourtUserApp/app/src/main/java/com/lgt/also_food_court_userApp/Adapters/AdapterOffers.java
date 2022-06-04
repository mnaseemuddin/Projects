package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.also_food_court_userApp.Activities.RestaurantDescription;
import com.lgt.also_food_court_userApp.Models.ModelOffer;
import com.lgt.also_food_court_userApp.R;

import java.util.List;

public class AdapterOffers extends RecyclerView.Adapter<AdapterOffers.HolderOffer> {

    private List<ModelOffer> list;
    private Context context;

    public AdapterOffers(List<ModelOffer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderOffer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_offer,parent,false);
        return new HolderOffer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOffer holder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.image_not_available_new).error(R.drawable.image_not_available_new)).into(holder.iv_image_offer);
        holder.tv_offer_product_name.setText(list.get(position).getName());
        holder.tv_offer_Description.setText(list.get(position).getTv_Description());
        holder.tv_offer_discount_percent.setText(list.get(position).getDiscountPercent()+" off");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, RestaurantDescription.class);
                intent.putExtra("KEY_RESTAURANT_ID",list.get(position).getRestaurant_Id());
                context.startActivity(intent);
            }
        });



       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main,new FragmentRestaurantList()).addToBackStack("Fragment_BannerData").commit();


            }
        });*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderOffer extends RecyclerView.ViewHolder {

        ImageView iv_image_offer;

        TextView tv_offer_product_name,tv_offer_discount_percent,tv_offer_Description;

        public HolderOffer(@NonNull View itemView) {
            super(itemView);

            iv_image_offer = itemView.findViewById(R.id.iv_image_offer);
            tv_offer_product_name = itemView.findViewById(R.id.tv_offer_product_name);
           // tv_Description = itemView.findViewById(R.id.tv_Description);
            tv_offer_discount_percent = itemView.findViewById(R.id.tv_offer_discount_percent);
            tv_offer_Description=itemView.findViewById(R.id.tv_offer_Description);

        }
    }
}
