package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.also_food_court_userApp.Models.ModelHorizontal;
import com.lgt.also_food_court_userApp.R;


import java.util.ArrayList;

/**
 * Created by Ranjan on 25/09/2019.
 */
public class AdapterHorizontal extends RecyclerView.Adapter<AdapterHorizontal.HolderHorizontal> {

    private ArrayList<ModelHorizontal> listHorizontal;
    private Context context;

    public AdapterHorizontal(ArrayList<ModelHorizontal> listHorizontal, Context context) {
        this.listHorizontal = listHorizontal;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderHorizontal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_horizontal,parent,false);
        return new HolderHorizontal(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHorizontal holder, int position) {

        LayerDrawable stars = (LayerDrawable) holder.ratingBar_Horizontal.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_ATOP);

        holder.tvLatestFood_Horizontal.setText(listHorizontal.get(position).getName());

        holder.ratingBar_Horizontal.setRating(Float.valueOf(listHorizontal.get(position).getRating()));

        Glide.with(context).load(listHorizontal.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.image_not_available_new).error(R.drawable.image_not_available_new)).into(holder.iv_Trending_Horizontal);

    }

    @Override
    public int getItemCount() {
        return listHorizontal.size();
    }

    public static class HolderHorizontal extends RecyclerView.ViewHolder {
        private ImageView iv_Trending_Horizontal;
        private TextView tvLatestFood_Horizontal;
        private RatingBar ratingBar_Horizontal;
        public HolderHorizontal(@NonNull View itemView) {
            super(itemView);

            iv_Trending_Horizontal = itemView.findViewById(R.id.iv_Trending_Horizontal);
            tvLatestFood_Horizontal = itemView.findViewById(R.id.tvLatestFood_Horizontal);

            ratingBar_Horizontal = itemView.findViewById(R.id.ratingBar_Horizontal);
        }
    }
}
