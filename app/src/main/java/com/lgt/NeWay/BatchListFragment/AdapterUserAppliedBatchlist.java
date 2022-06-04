package com.lgt.NeWay.BatchListFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.NeWay.Neway.R;

import java.util.List;

public class AdapterUserAppliedBatchlist extends RecyclerView.Adapter<AdapterUserAppliedBatchlist.Cityholder> {
    List<ModelUserAppliedJob>modelUserAppliedJobList;
    Context context;
    DeleteBatchInterFace deleteBatchInterFace;

    public AdapterUserAppliedBatchlist(List<ModelUserAppliedJob> modelUserAppliedJobList, Context context, DeleteBatchInterFace deleteBatchInterFace) {
        this.modelUserAppliedJobList = modelUserAppliedJobList;
        this.context = context;
        this.deleteBatchInterFace = deleteBatchInterFace;
    }

    @NonNull
    @Override
    public Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.batchlist,parent,false);
        return new Cityholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Cityholder holder, int position) {
        String id=modelUserAppliedJobList.get(position).getTbl_batches_id();
        holder.tvBatchname.setText(modelUserAppliedJobList.get(position).getTvBatchname());

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBatchInterFace.deletebatch(id);

            }
        });

    }
    @Override
    public int getItemCount() {
        return modelUserAppliedJobList.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        TextView tvBatchname;
        ImageView iv_delete;

        public Cityholder(@NonNull View itemView) {
            super(itemView);
            tvBatchname=itemView.findViewById(R.id.tvBatchname);
            iv_delete=itemView.findViewById(R.id.iv_delete);
        }
    }
}
