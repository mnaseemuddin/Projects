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
import com.lgt.also_food_court_userApp.Activities.OrderTrackHistory;
import com.lgt.also_food_court_userApp.Models.ModelMyCurrentOrder;
import com.lgt.also_food_court_userApp.R;

import java.util.List;


public class AdapterMyCurrentOrder extends RecyclerView.Adapter<AdapterMyCurrentOrder.CurrentViewHolder> {
   private List<ModelMyCurrentOrder>listcurrentOrder;
    private Context context;

    public AdapterMyCurrentOrder(List<ModelMyCurrentOrder> listcurrentOrder, Context context) {
        this.listcurrentOrder = listcurrentOrder;
        this.context = context;
    }

    @NonNull
    @Override
    public CurrentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.current_order_layout,parent,false);
        return new CurrentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMyCurrentOrder.CurrentViewHolder holder, final int position) {
        Glide.with(context).load(listcurrentOrder.get(position).getProductImage()).into(holder.iv_productImage);

        holder.tv_orderId.setText(listcurrentOrder.get(position).getOrderId());
        holder.tv_productName.setText(listcurrentOrder.get(position).getProductName());
        holder.tv_productPrice.setText(context.getString(R.string.rs)+" "+listcurrentOrder.get(position).getProductPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, OrderTrackHistory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("product_No",listcurrentOrder.get(position).getOrderId());
                Log.e("kdfhjh",listcurrentOrder.get(position).getOrderId()+"");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listcurrentOrder.size();
    }

    public class CurrentViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_productImage;
        private TextView tv_orderId,tv_productName,tv_productPrice;
        public CurrentViewHolder(@NonNull View itemView) {
            super(itemView);


            iv_productImage=itemView.findViewById(R.id.iv_productImage);
            tv_orderId=itemView.findViewById(R.id.tv_orderId);
            tv_productName=itemView.findViewById(R.id.tv_productName);
            tv_productPrice=itemView.findViewById(R.id.tv_productPrice);
        }
    }
}
