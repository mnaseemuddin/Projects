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
import com.lgt.also_food_court_userApp.Activities.SingleProductDetails;
import com.lgt.also_food_court_userApp.Models.ModelFamousNearYou;
import com.lgt.also_food_court_userApp.R;

import java.util.List;

public class AdapterNewFood extends RecyclerView.Adapter<AdapterNewFood.HolderFamous> {

    List<ModelFamousNearYou> list;
    Context context;

    public AdapterNewFood(List<ModelFamousNearYou> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderFamous onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_famous,parent,false);
        return new HolderFamous(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFamous holder, final int position) {

        holder.tv_productName.setText(list.get(position).getProductName());


        Glide.with(context).load(list.get(position).getImageURL()).into(holder.iv_famous_item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, SingleProductDetails.class);
                intent.putExtra("Products_id",list.get(position).getProduct_id());
                context.startActivity(intent);



            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderFamous extends RecyclerView.ViewHolder {

        private ImageView iv_famous_item;
        private TextView tv_productName;

        public HolderFamous(@NonNull View itemView) {
            super(itemView);

            iv_famous_item = itemView.findViewById(R.id.iv_famous_item);
            tv_productName = itemView.findViewById(R.id.tv_productName);

        }
    }
}
