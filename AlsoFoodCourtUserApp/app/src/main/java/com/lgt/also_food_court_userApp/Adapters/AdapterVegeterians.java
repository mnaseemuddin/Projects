package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lgt.also_food_court_userApp.Activities.RestaurantDescription;
import com.lgt.also_food_court_userApp.Models.ModelVegeterians;
import com.lgt.also_food_court_userApp.R;

import java.util.List;

public class AdapterVegeterians extends RecyclerView.Adapter<AdapterVegeterians.HolderVegeterian> {

    List<ModelVegeterians> list;
    Context context;

    public AdapterVegeterians(List<ModelVegeterians> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderVegeterian onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_vegeterians, parent, false);
        return new HolderVegeterian(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderVegeterian holder, final int position) {

        holder.tv_vege_name.setText(list.get(position).getName());
        holder.tv_vege_details.setText(list.get(position).getDetails());
        holder.tv_offer_percentage.setText(list.get(position).getDiscount());
        holder.tv_coupon.setText(list.get(position).getCoupon());
        holder.tv_wait_time.setText(list.get(position).getTime());
        holder.tv_amount.setText(list.get(position).getAmount());
        holder.tv_closeTime.setText(list.get(position).getTv_closeTime());
        holder.tv_Address.setText(list.get(position).getTv_Address());

        Glide.with(context).load(list.get(position).getImageURL()).into(holder.iv_vegeterians);

        Log.e("VEGEGEGEGEEGGE",list+"");
        Log.e("VEGEGEGEGEEGGE",list.size()+"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,RestaurantDescription.class);
                intent.putExtra("KEY_RESTAURANT_ID",list.get(position).getId());
                Log.e("MNHYUOP",list.get(position).getId()+"");
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderVegeterian extends RecyclerView.ViewHolder {

        ImageView iv_vegeterians;
        TextView tv_vege_name, tv_vege_details, tv_offer_percentage, tv_coupon, tv_wait_time, tv_amount,tv_closeTime,tv_Address;

        public HolderVegeterian(@NonNull View itemView) {
            super(itemView);

            iv_vegeterians = itemView.findViewById(R.id.iv_vegeterians);

            tv_vege_name = itemView.findViewById(R.id.tv_vege_name);
            tv_vege_details = itemView.findViewById(R.id.tv_vege_details);
            tv_offer_percentage = itemView.findViewById(R.id.tv_offer_percentage);
            tv_coupon = itemView.findViewById(R.id.tv_coupon);
            tv_wait_time = itemView.findViewById(R.id.tv_wait_time);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_closeTime=itemView.findViewById(R.id.tv_closeTime);
            tv_Address=itemView.findViewById(R.id.tv_Address);
        }
    }
}
