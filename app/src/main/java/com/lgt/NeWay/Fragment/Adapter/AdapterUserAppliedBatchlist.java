package com.lgt.NeWay.Fragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.NeWay.Fragment.Model.ModelUserAppliedJob;
import com.lgt.NeWay.Neway.R;

import java.util.List;

public class AdapterUserAppliedBatchlist extends RecyclerView.Adapter<AdapterUserAppliedBatchlist.Cityholder> {
    List<ModelUserAppliedJob>modelUserAppliedJobList;
    Context context;

    public AdapterUserAppliedBatchlist(List<ModelUserAppliedJob> modelUserAppliedJobList, Context context) {
        this.modelUserAppliedJobList = modelUserAppliedJobList;
        this.context = context;
    }

    @NonNull
    @Override
    public Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.batchlist,parent,false);
        return new Cityholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Cityholder holder, int position) {
        holder.tvBatchname.setText(modelUserAppliedJobList.get(position).getTvBatchname());

    }
    @Override
    public int getItemCount() {
        return modelUserAppliedJobList.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        TextView tvBatchname;

        public Cityholder(@NonNull View itemView) {
            super(itemView);
            tvBatchname=itemView.findViewById(R.id.tvBatchname);
        }
    }
}
