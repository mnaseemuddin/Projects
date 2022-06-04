package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.also_food_court_userApp.Models.modelSingleProductDetailsQty;
import com.lgt.also_food_court_userApp.R;

import java.util.List;


public class AdapterSingleProductQuantity extends RecyclerView.Adapter<AdapterSingleProductQuantity.AdapterViewHolder> {
    private List<modelSingleProductDetailsQty> ListmodelSingleProductDetailsQty;
    private Context context;

    public AdapterSingleProductQuantity(List<modelSingleProductDetailsQty> listmodelSingleProductDetailsQty, Context context) {
        ListmodelSingleProductDetailsQty = listmodelSingleProductDetailsQty;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_qty,parent,false);

       return new AdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSingleProductQuantity.AdapterViewHolder holder, int position) {
    holder.tv_Quntity.setText(ListmodelSingleProductDetailsQty.get(position).getTv_Quntity());
    holder.tvFullPrice.setText(ListmodelSingleProductDetailsQty.get(position).getTvFullPrice());
    }

    @Override
    public int getItemCount() {
        return ListmodelSingleProductDetailsQty.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Quntity,tvFullPrice;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Quntity=itemView.findViewById(R.id.tv_Quntity);
            tvFullPrice=itemView.findViewById(R.id.tvFullPrice);
        }
    }
}
