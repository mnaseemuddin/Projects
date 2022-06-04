package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.also_food_court_userApp.Models.ModelBottomSheet;
import com.lgt.also_food_court_userApp.R;


import java.util.List;


public class AdapterBottomSheet extends RecyclerView.Adapter<AdapterBottomSheet.HolderBottomSheet> {

    private List<ModelBottomSheet> list;
    private Context context;

    public AdapterBottomSheet(List<ModelBottomSheet> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderBottomSheet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_modal_sheet, parent, false);
        return new HolderBottomSheet(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderBottomSheet holder, final int position) {

        holder.tvNameOrProduct.setText(list.get(position).getName());
        holder.tvOldPrice.setText(list.get(position).getOldPrice());
        holder.tvPriceModalSheet.setText(list.get(position).getNewPrice());

        holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qty = Integer.parseInt(holder.tvQTY.getText().toString());
                qty = qty + 1;
                holder.tvQTY.setText(String.valueOf(qty));

            }
        });

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qty = Integer.parseInt(holder.tvQTY.getText().toString());

                if(qty>1){
                    qty = qty - 1;
                    holder.tvQTY.setText(String.valueOf(qty));
                }
                else {
                    Toast.makeText(context, "No items in cart", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderBottomSheet extends RecyclerView.ViewHolder {

        private ImageView ivVegOrNonVeg, ivMinus, ivPlus;
        private TextView tvNameOrProduct, tvOldPrice, tvPriceModalSheet, tvPriceDifference, tvQTY;
        private RelativeLayout rlModalItems;


        public HolderBottomSheet(@NonNull View itemView) {
            super(itemView);

            ivVegOrNonVeg = itemView.findViewById(R.id.ivVegOrNonVeg);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvNameOrProduct = itemView.findViewById(R.id.tvNameOrProduct);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvPriceModalSheet = itemView.findViewById(R.id.tvPriceModalSheet);
            tvPriceDifference = itemView.findViewById(R.id.tvPriceDifference);
            tvQTY = itemView.findViewById(R.id.tvQTY);
            rlModalItems = itemView.findViewById(R.id.rlModalItems);


        }
    }
}
