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
import com.lgt.also_food_court_userApp.Models.FamousRestaurentsModel;
import com.lgt.also_food_court_userApp.R;

import java.util.ArrayList;

public class FamousRestaurentsAdapter extends RecyclerView.Adapter<FamousRestaurentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FamousRestaurentsModel>list;

    public FamousRestaurentsAdapter(Context context, ArrayList<FamousRestaurentsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FamousRestaurentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.restaurent_layout,parent,false);
        return new FamousRestaurentsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FamousRestaurentsAdapter.ViewHolder holder, final int position) {

        FamousRestaurentsModel model=list.get(position);

        RequestOptions requestOptions=new RequestOptions();
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.image_not_available_new);
        Glide.with(context).applyDefaultRequestOptions(requestOptions).load(model.getFood_image()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_restaurant_product);
        holder.tv_food_title.setText(model.getFood_title());
        holder.tv_food_name.setText(model.getFood_name());
        holder.tv_discount.setText(model.getDiscount());
        holder.tv_delivery_time.setText(model.getDelivery_time());
        holder.tv_price.setText(model.getPrice());
        holder.tv_restaurantFamousAddress.setText(model.getTv_restaurantFamousAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, RestaurantDescription.class);
                intent.putExtra("KEY_RESTAURANT_ID",list.get(position).getRestaurantId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_restaurant_product;
        private TextView tv_food_title,tv_food_name,tv_discount,tv_delivery_time,tv_price,tv_restaurantFamousAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_restaurant_product=itemView.findViewById(R.id.iv_restaurant_product);
            tv_food_title=itemView.findViewById(R.id.tv_food_title);
            tv_food_name=itemView.findViewById(R.id.tv_food_name);
            tv_discount=itemView.findViewById(R.id.tv_discount);
            tv_delivery_time=itemView.findViewById(R.id.tv_delivery_time);
            tv_price=itemView.findViewById(R.id.tv_price);
            tv_restaurantFamousAddress=itemView.findViewById(R.id.tv_restaurantFamousAddress);
        }
    }
}
