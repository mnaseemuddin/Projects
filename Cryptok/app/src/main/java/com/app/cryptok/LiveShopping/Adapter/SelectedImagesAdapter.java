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
import com.app.cryptok.databinding.SelectedImagesLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class SelectedImagesAdapter extends RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder> {

    public ArrayList<String> mList = new ArrayList<>();
    private Context context;


    public OnHolderClick onHolderClick;

    @NonNull
    @Override
    public SelectedImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.selected_images_layout, parent, false);
        return new SelectedImagesAdapter.ViewHolder(view);
    }

    public interface OnHolderClick {
        void onHolderClick(int type, int position, String model, SelectedImagesLayoutBinding holder_binding);
    }

    public void updateData(List<String> list) {
        mList = (ArrayList<String>) list;

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedImagesAdapter.ViewHolder holder, int position) {


        Glide.with(context)
                .load(mList.get(position))
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivImage);

        holder.binding.ivRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHolderClick.onHolderClick(1, holder.getAdapterPosition(), mList.get(position), holder.binding);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SelectedImagesLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }


}

