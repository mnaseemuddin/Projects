package com.app.cryptok.LiveShopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.databinding.DeliveryChargesListBinding;
import com.app.cryptok.LiveShopping.Model.DeliveryChargesModel;
import com.app.cryptok.utils.FastClickUtil;

import java.util.ArrayList;
import java.util.List;

public class DeliveryChargesAdapter extends RecyclerView.Adapter<DeliveryChargesAdapter.ViewHolder> {

    public ArrayList<DeliveryChargesModel.Datum> mList = new ArrayList<>();
    private Context context;

    public OnHolderClick onHolderClick;

    @NonNull
    @Override
    public DeliveryChargesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_charges_list, parent, false);
        return new DeliveryChargesAdapter.ViewHolder(view);
    }


    public interface OnHolderClick {
        void onHolderClick(int type, int position, DeliveryChargesModel.Datum model, DeliveryChargesListBinding holder_binding);
    }

    public void updateData(List<DeliveryChargesModel.Datum> list) {
        mList = (ArrayList<DeliveryChargesModel.Datum>) list;

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryChargesAdapter.ViewHolder holder, int position) {
        DeliveryChargesModel.Datum model = mList.get(position);

        holder.binding.tvCartTotal.setText(context.getString(R.string.Rs) + model.getCartTotal());
        holder.binding.tvDeliveryCharges.setText(context.getString(R.string.Rs) + model.getCharges());

        holder.binding.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                onHolderClick.onHolderClick(1, holder.getAdapterPosition(), model, holder.binding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private DeliveryChargesListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }


}

