package com.lgt.fxtradingleague.Education.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.lgt.fxtradingleague.Education.Model.EducationSliderModel;
import com.lgt.fxtradingleague.Education.Model.EducationVideosModel;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.databinding.EducationCategoryBinding;
import com.lgt.fxtradingleague.databinding.ImageSliderLayoutItemBinding;

import java.util.ArrayList;
import java.util.List;

public class EducationSliderAdapter extends RecyclerView.Adapter<EducationSliderAdapter.HolderBeginners> {

    ArrayList<EducationSliderModel> mList = new ArrayList<>();
    Context mContext;

    @NonNull
    @Override
    public EducationSliderAdapter.HolderBeginners onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, parent, false);
        return new EducationSliderAdapter.HolderBeginners(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationSliderAdapter.HolderBeginners holder, final int position) {

        EducationSliderModel model = mList.get(position);

        Glide.with(mContext)
                .load(model.getSlider_image())
                .thumbnail(0.07f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivAutoImageSlider);


    }

    public void updateData(List<EducationSliderModel> list) {
        mList = (ArrayList<EducationSliderModel>) list;
        notifyDataSetChanged();
    }

    public void loadMore(List<EducationSliderModel> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class HolderBeginners extends RecyclerView.ViewHolder {
        ImageSliderLayoutItemBinding binding;

        public HolderBeginners(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }
}
