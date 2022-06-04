package com.lgt.also_food_court_userApp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lgt.also_food_court_userApp.Activities.ActivityOrderFullDetail;
import com.lgt.also_food_court_userApp.Models.ModelOrderHistory;
import com.lgt.also_food_court_userApp.R;

import java.util.List;


public class AdapterFullHistory extends RecyclerView.Adapter<AdapterFullHistory.HolderOrder> {

    private List<ModelOrderHistory> listOrders;
    private Context context;

    public AdapterFullHistory(List<ModelOrderHistory> listOrders, Context context) {
        this.listOrders = listOrders;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_full_order_history,parent,false);
        return new HolderOrder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HolderOrder holder, final int position) {
        Log.e("Data"+position, new Gson().toJson(listOrders.get(position)));
        Glide.with(context).load(listOrders.get(position).getProduct_image()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.image_not_available_new).error(R.drawable.image_not_available_new)).into(holder.iv_ProductOrder);

        holder.tvNameOrder.setText(listOrders.get(position).getProduct_name());
        holder.tvOrderID.setText(listOrders.get(position).getOrder_number());
        holder.tvAmountOrder.setText(listOrders.get(position).getProduct_price());
        holder.tvQtyOrder.setText(listOrders.get(position).getQuantity());
        holder.tvSize.setText(listOrders.get(position).getSize());
        //holder.tvOrderDate.setText(listOrders.get(position).getCreated_date());
        holder.tv_orderStatus.setText(listOrders.get(position).getOrder_status());
       /* if (listOrders.get(position).getSize().equalsIgnoreCase("Size")){
            Log.e("mdkf",holder.tvSize+"");
            holder.ll_size.setVisibility(View.GONE);
        }else{
            holder.ll_size.setVisibility(View.VISIBLE);
        }*/


        /* if (listOrders.get(position).getOrder_status().equals("1")){
            holder.tv_orderStatus.setText("Order Conformed");
            holder.tv_orderStatus.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if (listOrders.get(position).getOrder_status().equalsIgnoreCase("Delivered")){
              holder.tv_orderStatus.setTextColor(context.getResources().getColor(R.color.lightGreen));
        }
        else{
            holder.tv_orderStatus.setText(listOrders.get(position).getOrder_status());
          holder.tv_orderStatus.setTextColor(context.getResources().getColor(R.color.red));
        }*/

      /*  holder.cardViewMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityOrderFullDetail.class);
                intent.putExtra("KEY_ORDER_ID",listOrders.get(position).getOrder_number());
                context.startActivity(intent);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    public static class HolderOrder extends RecyclerView.ViewHolder {
        private LinearLayout ll_size;
        private CardView cardViewMyOrders;
        private ImageView iv_ProductOrder;
        private TextView tvNameOrder,tvOrderID,tvAmountOrder,tvQtyOrder,tvSize,tvOrderDate,tv_orderStatus;


        public HolderOrder(@NonNull View itemView) {
            super(itemView);

            cardViewMyOrders = itemView.findViewById(R.id.cardViewMyOrders);
            iv_ProductOrder = itemView.findViewById(R.id.iv_ProductOrder);
            tvNameOrder = itemView.findViewById(R.id.tvNameOrder);
            tvOrderID = itemView.findViewById(R.id.tvOrderID);
            tvAmountOrder = itemView.findViewById(R.id.tvAmountOrder);
            tvQtyOrder = itemView.findViewById(R.id.tvQtyOrder);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tv_orderStatus=itemView.findViewById(R.id.tv_orderStatus);
            ll_size=itemView.findViewById(R.id.ll_size);

        }
    }
}
