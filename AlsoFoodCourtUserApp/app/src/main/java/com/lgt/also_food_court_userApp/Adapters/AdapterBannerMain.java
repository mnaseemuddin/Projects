package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.also_food_court_userApp.Fragments.FragmentRestaurantList;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.beans.RestaurantOffer.Datum;


import java.util.List;

public class AdapterBannerMain extends RecyclerView.Adapter<AdapterBannerMain.BannerHolder> {

    List<Datum> list;
    Context context;

    public AdapterBannerMain(List<Datum> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_banner_main, parent, false);
        return new BannerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, final int position) {

        Glide.with(context).load(list.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.image_not_available_new)
                        .error(R.drawable.image_not_available_new)).into(holder.iv_bannerImage);
          //holder.iv_bannerImage.setColorFilter(0xff555555, PorterDuff.Mode.MULTIPLY);
        holder.iv_bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new FragmentRestaurantList();
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);

                Bundle bundle = new Bundle();
                bundle.putString("KEY_OFFER_PERCENT", list.get(position).getOffer());
                fragmentTransaction.addToBackStack("Fragment_Banner");
                fragment.setArguments(bundle);
                fragmentTransaction.commit();
                Log.e("SDDSDSADAD", list.get(position).getTblOfferBannerId() + "");
                Log.e("bhbhbhbhbbhbhbh", list.get(position).getOffer() + "");

            }
        });


    }

    @Override
    public int getItemCount() {
        Log.e("klkojojojojojo", list.size() + "");
        return list.size();
    }

    public static class BannerHolder extends RecyclerView.ViewHolder {
        ImageView iv_bannerImage;

        public BannerHolder(@NonNull View itemView) {
            super(itemView);
            iv_bannerImage = itemView.findViewById(R.id.iv_bannerImage);
        }
    }
}
