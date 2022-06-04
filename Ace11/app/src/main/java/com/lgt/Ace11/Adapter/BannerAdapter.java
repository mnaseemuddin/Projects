package com.lgt.Ace11.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lgt.Ace11.R;

import java.util.List;

import Model.ModelOffers;

public class BannerAdapter  extends RecyclerView.Adapter<BannerAdapter.OfferHolder> {

    List<ModelOffers> list;
    Context context;

    public BannerAdapter(List<ModelOffers> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OfferHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_offers,viewGroup,false);
        return new OfferHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferHolder offerHolder, int i) {



        Glide.with(context).load(list.get(i).getImage()).into(offerHolder.iv_Offers);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OfferHolder extends RecyclerView.ViewHolder{

        ImageView iv_Offers;


        public OfferHolder(@NonNull View itemView) {
            super(itemView);

            iv_Offers = itemView.findViewById(R.id.iv);
        }
    }
}

