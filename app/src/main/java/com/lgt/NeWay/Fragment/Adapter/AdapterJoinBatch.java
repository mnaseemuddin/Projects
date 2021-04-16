package com.lgt.NeWay.Fragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.DiscriptionJoinBatch;

import java.util.List;

public class AdapterJoinBatch extends RecyclerView.Adapter<AdapterJoinBatch.Cityholder> {
    List<ModelJoinBatch>modelJoinBatchList;
    Context context;

    public AdapterJoinBatch(List<ModelJoinBatch> modelJoinBatchList, Context context) {
        this.modelJoinBatchList = modelJoinBatchList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterJoinBatch.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_joinbatch,parent,false);

        return new Cityholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterJoinBatch.Cityholder holder, int position) {
        holder.tvStudentName.setText(modelJoinBatchList.get(position).getTvStudentName());
        holder.tvStatuspending.setText(modelJoinBatchList.get(position).getTvStatuspending());
        holder.tv_Mobile.setText(modelJoinBatchList.get(position).getTv_Mobile());
        holder.tv_Subject.setText(modelJoinBatchList.get(position).getTv_Subject());
        holder.tv_FeeType.setText(modelJoinBatchList.get(position).getTv_FeeType());
        holder.tv_Feepaydate.setText(modelJoinBatchList.get(position).getTv_Feepaydate());

        holder.ll_useraBatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DiscriptionJoinBatch.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return modelJoinBatchList.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        LinearLayout ll_useraBatchlist;
        TextView tvStudentName,tvStatuspending,tv_Mobile,tv_Subject,tv_FeeType,tv_Feepaydate,tv_View;
        Button bt_View;

        public Cityholder(@NonNull View itemView) {
            super(itemView);

            tvStudentName=itemView.findViewById(R.id.tvStudentName);
            ll_useraBatchlist=itemView.findViewById(R.id.ll_useraBatchlist);
            tvStatuspending=itemView.findViewById(R.id.tvStatuspending);
            tv_Subject=itemView.findViewById(R.id.tv_Subject);
            tv_Mobile=itemView.findViewById(R.id.tv_Mobile);
            tv_FeeType=itemView.findViewById(R.id.tv_FeeType);
            tv_Feepaydate=itemView.findViewById(R.id.tv_Feepaydate);
            tv_View=itemView.findViewById(R.id.tv_View);

        }
    }
}
