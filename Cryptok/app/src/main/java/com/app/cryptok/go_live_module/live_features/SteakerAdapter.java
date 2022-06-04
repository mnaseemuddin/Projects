package com.app.cryptok.go_live_module.live_features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.adapter.CustomHolder;
import com.app.cryptok.adapter.ItemClickListner;
import com.app.cryptok.go_live_module.LiveClickConstants;

public class SteakerAdapter extends RecyclerView.Adapter<SteakerAdapter.SteakerHolder> {
    Context context;
    ItemClickListner clickListner;
    Boolean isWordSteakers;
    public SteakerAdapter(Context context, ItemClickListner clickListner,Boolean isWordSteakers) {
        this.context = context;
        this.clickListner = clickListner;
        this.isWordSteakers=isWordSteakers;
    }


    @NonNull
    @Override
    public SteakerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.inflate_steakers,parent,false);
        return new SteakerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SteakerHolder holder, int position) {
        if (isWordSteakers){
            if (position==0){
                holder.iv_steaker.setImageResource(R.drawable.facebook_steakers);

            }else if (position==1){
                holder.iv_steaker.setImageResource(R.drawable.insta_steakers);
            }else if (position==2){
                holder.iv_steaker.setImageResource(R.drawable.youtube_steakers);
            }
        }else {
            if (position==0){
                holder.iv_steaker.setImageResource(R.drawable.steaker1);

            }else if (position==1){
                holder.iv_steaker.setImageResource(R.drawable.steaker2);

            }else if (position%3==0){
                holder.iv_steaker.setImageResource(R.drawable.steaker1);

            }else if (position%2==0){
                holder.iv_steaker.setImageResource(R.drawable.steaker3);

            }else {
                holder.iv_steaker.setImageResource(R.drawable.steaker2);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListner.itemClick(position, LiveClickConstants.STEAKER_ITEM_CLICK);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isWordSteakers){
            return 3;
        }
        return 10;

    }

    public class SteakerHolder extends CustomHolder {
    ImageView iv_steaker;
        public SteakerHolder(@NonNull View itemView) {
            super(itemView);
            iv_steaker=find(R.id.iv_steaker);
        }
    }
}
