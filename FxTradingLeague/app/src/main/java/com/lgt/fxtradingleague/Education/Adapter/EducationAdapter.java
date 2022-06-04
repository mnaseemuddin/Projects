package com.lgt.fxtradingleague.Education.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.Education.Model.EducationVideosModel;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.databinding.EducationCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.HolderBeginners> {

    ArrayList<EducationVideosModel> mList = new ArrayList<>();
    Context mContext;

    @NonNull
    @Override
    public HolderBeginners onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_category, parent, false);
        return new HolderBeginners(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBeginners holder, final int position) {

        EducationVideosModel model = mList.get(position);
        holder.binding.tvEducationCategory.setText(model.getEducation_category());
        if (model.getVideosList() != null) {
            if (model.getVideosList().size() > 0) {
                EducationVideosAdapter educationVideosAdapter = new EducationVideosAdapter(model.getVideosList());
                holder.binding.rvVideosList.setAdapter(educationVideosAdapter);
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void updateData(List<EducationVideosModel> list) {
        mList = (ArrayList<EducationVideosModel>) list;
        notifyDataSetChanged();
    }

    public void loadMore(List<EducationVideosModel> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class HolderBeginners extends RecyclerView.ViewHolder {
        EducationCategoryBinding binding;

        public HolderBeginners(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }
}
