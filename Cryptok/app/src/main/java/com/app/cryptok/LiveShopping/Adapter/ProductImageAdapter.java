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
import com.app.cryptok.databinding.ProductImagesLayoutBinding;
import com.app.cryptok.LiveShopping.Model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ViewHolder> {

    public ArrayList<ProductModel.Proimage> mList = new ArrayList<>();
    private Context context;

    public OnImageClick onImageClick;
    @NonNull
    @Override
    public ProductImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.product_images_layout, parent, false);
        return new ProductImageAdapter.ViewHolder(view);
    }

    public interface OnImageClick {
        void onImageClick(int type, int position, ProductModel.Proimage model, ProductImagesLayoutBinding holder_binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageAdapter.ViewHolder holder, int position) {
        ProductModel.Proimage model = mList.get(position);


        Glide.with(context)
                .load(model.getImages())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivProductImage);

        holder.binding.tvTotalImages.setText(holder.getAdapterPosition() + 1 + "/" + mList.size());

        holder.binding.ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClick.onImageClick(1,holder.getAdapterPosition(),model,holder.binding);

            }
        });
        holder.binding.ivEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClick.onImageClick(2,holder.getAdapterPosition(),model,holder.binding);
            }
        });

    }

    public void updateData(List<ProductModel.Proimage> list) {
        mList = (ArrayList<ProductModel.Proimage>) list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProductImagesLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }


}

