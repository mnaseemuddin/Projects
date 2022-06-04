package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lgt.also_food_court_userApp.Activities.SingleProductDetails;
import com.lgt.also_food_court_userApp.Models.ModelTrendingFoods;
import com.lgt.also_food_court_userApp.R;


import java.util.List;

/**
 * Created by Ranjan on 25/09/2019.
 */
public class AdapterTrending extends RecyclerView.Adapter<AdapterTrending.HolderTrending> {

    private List<ModelTrendingFoods> list;
    Context context;

    public AdapterTrending(List<ModelTrendingFoods> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderTrending onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trending,parent,false);
        return new HolderTrending(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTrending holder, final int position) {

        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_ATOP);


        Glide.with(context).load(list.get(position).getImage()).into(holder.iv_Trending);


        holder.tvLatestFood.setText(list.get(position).getName());
        holder.tvRight.setText(list.get(position).getOffers());
        holder.tvReviews.setText(list.get(position).getReviews());
        holder.ratingBar.setRating(Float.valueOf(list.get(position).getRating()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SingleProductDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Products_id",list.get(position).getProductId());
                intent.putExtra("Products_Offer",list.get(position).getOffers());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderTrending extends RecyclerView.ViewHolder {
        private ImageView iv_Trending;
        private TextView tvLatestFood,tvRight,tvReviews;
        private RatingBar ratingBar;
        public HolderTrending(@NonNull View itemView) {
            super(itemView);

            iv_Trending = itemView.findViewById(R.id.iv_Trending);
            tvLatestFood = itemView.findViewById(R.id.tvLatestFood);
            tvRight = itemView.findViewById(R.id.tvRight);
            tvReviews = itemView.findViewById(R.id.tvReviews);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
