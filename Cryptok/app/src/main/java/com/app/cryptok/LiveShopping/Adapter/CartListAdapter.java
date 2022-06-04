package com.app.cryptok.LiveShopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.app.cryptok.R;
import com.app.cryptok.databinding.CartListLayoutBinding;
import com.app.cryptok.LiveShopping.Model.CartListModel;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.FastClickUtil;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    public ArrayList<CartListModel.Datum> mList = new ArrayList<>();
    private Context context;

    public OnCartClick onCartClick;

    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_layout, parent, false);
        return new CartListAdapter.ViewHolder(view);
    }

    public interface OnCartClick {
        void onCartClick(int type, int quantity, int position, CartListModel.Datum model, CartListLayoutBinding holder_binding);
    }

    public void updateData(List<CartListModel.Datum> list) {
        mList = (ArrayList<CartListModel.Datum>) list;
        notifyDataSetChanged();
    }

    public void loadMore(List<CartListModel.Datum> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        CartListModel.Datum model = mList.get(position);


        Glide.with(context)
                .load(model.getProImages())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.imgbag);
        holder.binding.txtprdtname.setText(model.getProName());
        holder.binding.txtcurrentprice.setText(context.getString(R.string.Rs) + model.getPrice());
        holder.binding.tvQuantity.setText(model.getQuantity());
        holder.binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                int total = Integer.parseInt(holder.binding.tvQuantity.getText().toString());
                total = total + 1;
                holder.binding.tvQuantity.setText("" + total);
                model.setQuantity(String.valueOf(total));
                onCartClick.onCartClick(ConstantsKey.update_quantity, total, holder.getAdapterPosition(), model, holder.binding);
            }
        });
        holder.binding.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                int total = Integer.parseInt(holder.binding.tvQuantity.getText().toString());
                total = total - 1;
                holder.binding.tvQuantity.setText("" + total);
                model.setQuantity(String.valueOf(total));
                onCartClick.onCartClick(ConstantsKey.update_quantity,  total, holder.getAdapterPosition(), model, holder.binding);
            }
        });
        holder.binding.ivDeleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                onCartClick.onCartClick(ConstantsKey.update_quantity,0, holder.getAdapterPosition(), model, holder.binding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CartListLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }


}

