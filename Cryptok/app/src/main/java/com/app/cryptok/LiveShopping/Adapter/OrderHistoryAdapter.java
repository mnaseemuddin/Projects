package com.app.cryptok.LiveShopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.databinding.OrderHistoryLayoutBinding;
import com.app.cryptok.LiveShopping.Model.OrderHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    public ArrayList<OrderHistoryModel.Datum> mList = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_layout, parent, false);
        return new OrderHistoryAdapter.ViewHolder(view);
    }

    public void updateData(List<OrderHistoryModel.Datum> list) {
        mList = (ArrayList<OrderHistoryModel.Datum>) list;
        notifyDataSetChanged();
    }

    public void loadMore(List<OrderHistoryModel.Datum> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        OrderHistoryModel.Datum model = mList.get(position);

        holder.binding.tvDeliveryCharges.setText(context.getString(R.string.Rs)+model.getDeliveryCharges()+"");
        holder.binding.tvOrderAmount.setText(context.getString(R.string.Rs)+model.getOrderAmount()+"");
        holder.binding.tvOrderStatus.setText(model.getOrderStatus()+"");
        holder.binding.tvOrderDate.setText(model.getOrderDate()+"");
        holder.binding.tvPaymentType.setText(model.getPaymentType()+"");
        holder.binding.tvTotalOrderProducts.setText(model.getTotalOrderProducts()+"");
        holder.binding.tvOrderNumber.setText(model.getOrderNumber()+"");
        holder.binding.tvOrderSubamount.setText(context.getString(R.string.Rs)+model.getOrderSubtotal()+"");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private OrderHistoryLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }


}

