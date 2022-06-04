package com.lgt.fxtradingleague.Education.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.lgt.fxtradingleague.Education.Activity.EducationDetailsActivity;
import com.lgt.fxtradingleague.Education.Activity.VideoPlayerActivity;
import com.lgt.fxtradingleague.Education.Model.EducationVideosModel;
import com.lgt.fxtradingleague.Education.utils.Const;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.databinding.EducationVideosItemsBinding;

import java.util.List;

public class EducationVideosAdapter extends RecyclerView.Adapter<EducationVideosAdapter.HolderBeginners> {

    List<EducationVideosModel.Videos> mList;
    Context mContext;

    public EducationVideosAdapter(List<EducationVideosModel.Videos> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public EducationVideosAdapter.HolderBeginners onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_videos_items, parent, false);
        return new HolderBeginners(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationVideosAdapter.HolderBeginners holder, final int position) {
        EducationVideosModel.Videos model = mList.get(position);
        Glide.with(mContext)
                .load(model.getVideo_thumb())
                .thumbnail(0.05f)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.ivVideoThumb);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EducationDetailsActivity.class);
                intent.putExtra(Const.MODEL, new Gson().toJson(model));
                mContext.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class HolderBeginners extends RecyclerView.ViewHolder {

        private EducationVideosItemsBinding binding;

        public HolderBeginners(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }

        }
    }
}
