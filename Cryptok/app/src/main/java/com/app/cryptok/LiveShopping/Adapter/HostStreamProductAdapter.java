package com.app.cryptok.LiveShopping.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.app.cryptok.R;
import com.app.cryptok.databinding.HostProductSheetLayoutBinding;
import com.app.cryptok.LiveShopping.Model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class HostStreamProductAdapter extends RecyclerView.Adapter<HostStreamProductAdapter.ViewHolder> {

    public ArrayList<ProductModel.ProductsDatum> mList = new ArrayList<>();
    private Context context;
    @NonNull
    @Override
    public HostStreamProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.host_product_sheet_layout, parent, false);
        return new HostStreamProductAdapter.ViewHolder(view);
    }

    public void updateData(List<ProductModel.ProductsDatum> list ) {
        mList = (ArrayList<ProductModel.ProductsDatum>) list;

        notifyDataSetChanged();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull HostStreamProductAdapter.ViewHolder holder, int position) {
        ProductModel.ProductsDatum model = mList.get(position);

        Glide.with(context)
                .load(model.getProimages().get(0).getImages())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivProductImage);

        holder.binding.tvProductName.setText(model.getProName());
        holder.binding.tvProductPrice.setText(context.getString(R.string.Rs) + model.getProPrice());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private HostProductSheetLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }


}

