package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.also_food_court_userApp.Activities.RestaurantDescription;
import com.lgt.also_food_court_userApp.Models.ModelRestaurantList;
import com.lgt.also_food_court_userApp.R;


import java.util.List;

public class AdapterRestaurantList extends RecyclerView.Adapter<AdapterRestaurantList.HolderRestaurantList> {

    List<ModelRestaurantList> list;
    Context context;

    public AdapterRestaurantList(List<ModelRestaurantList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderRestaurantList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_restaurant_list, parent, false);
        return new HolderRestaurantList(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HolderRestaurantList holder, final int position) {


        Glide.with(context).load(list.get(position).getRestaurantImage()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.image_not_available_new).error(R.drawable.image_not_available_new)).into(holder.ivRestaurantList);

        holder.tvRestaurantNameList.setText(list.get(position).getRestaurantName());
        holder.tvRestaurantLocation.setText(list.get(position).getRestaurantLocation());
        holder.tvRatingRestaurant.setText(list.get(position).getRating());

        holder.rlRestaurantDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open restaurant description
                Intent intent = new Intent(context, RestaurantDescription.class);
                intent.putExtra("KEY_RESTAURANT_ID",list.get(position).getId());
                Log.e("kjkjkjkjkj",list.get(position).getId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderRestaurantList extends RecyclerView.ViewHolder {

        private ImageView ivRestaurantList;
        private TextView tvRestaurantNameList, tvRestaurantLocation, tvRatingRestaurant;
        private RelativeLayout rlRestaurantDesc;

        public HolderRestaurantList(@NonNull View itemView) {
            super(itemView);

            ivRestaurantList = itemView.findViewById(R.id.ivRestaurantList);
            tvRestaurantNameList = itemView.findViewById(R.id.tvRestaurantNameList);
            tvRestaurantLocation = itemView.findViewById(R.id.tvRestaurantLocation);
            tvRatingRestaurant = itemView.findViewById(R.id.tvRatingRestaurant);
            rlRestaurantDesc = itemView.findViewById(R.id.rlRestaurantDesc);


        }
    }
}
