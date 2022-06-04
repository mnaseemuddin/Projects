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
import com.app.cryptok.databinding.ProductVideosLayoutBinding;
import com.app.cryptok.LiveShopping.Model.ProductVideosModel;

import java.util.ArrayList;
import java.util.List;

public class ProductVideosAdapter extends RecyclerView.Adapter<ProductVideosAdapter.ViewHolder> {

    public ArrayList<ProductVideosModel.VideosDatum> mList = new ArrayList<>();
    private Context context;

    public OnVideoClick onVideoClick;

    @NonNull
    @Override
    public ProductVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.product_videos_layout, parent, false);
        return new ProductVideosAdapter.ViewHolder(view);
    }

    public interface OnVideoClick {
        void onVideoClick(int type, int position, ProductVideosModel.VideosDatum model, ProductVideosLayoutBinding holder_binding);
    }

    public void updateData(List<ProductVideosModel.VideosDatum> list) {
        mList = (ArrayList<ProductVideosModel.VideosDatum>) list;
        notifyDataSetChanged();
    }

    public void loadMore(List<ProductVideosModel.VideosDatum> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVideosAdapter.ViewHolder holder, int position) {
        ProductVideosModel.VideosDatum model = mList.get(position);
        Glide.with(context)
                .load(model.getVideoThumb())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivVideoImage);

        holder.binding.ivVideoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onVideoClick.onVideoClick(1, holder.getAdapterPosition(), model, holder.binding);
            }
        });
        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onVideoClick.onVideoClick(2, holder.getAdapterPosition(), model, holder.binding);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProductVideosLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }


}

