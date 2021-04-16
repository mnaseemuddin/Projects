package com.lgt.NeWay.activity.UserBatchRequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.NeWay.Neway.R;

import java.util.List;

public class AdapterUserbatchRequest extends RecyclerView.Adapter<AdapterUserbatchRequest.Cityholder> {
    List<ModelUserBatchRequest>mlist;
    Context context;

    public AdapterUserbatchRequest(List<ModelUserBatchRequest> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterUserbatchRequest.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapteruserbatchrequest,parent,false);
        return new Cityholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUserbatchRequest.Cityholder holder, int position) {
        holder.tv_Uname.setText(mlist.get(position).getTv_Uname());
        holder.tv_batch_Name.setText(mlist.get(position).getTv_batch_Name());
        holder.tv_MobileNo.setText(mlist.get(position).getTv_MobileNo());


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        TextView tv_Uname,tv_batch_Name,tv_MobileNo;
        Spinner sp_Statuspending;
        ImageView iv_delete;
        public Cityholder(@NonNull View itemView) {
            super(itemView);

            tv_Uname=itemView.findViewById(R.id.tv_Uname);
            tv_batch_Name=itemView.findViewById(R.id.tv_batch_Name);
            tv_MobileNo=itemView.findViewById(R.id.tv_MobileNo);
            sp_Statuspending=itemView.findViewById(R.id.sp_Statuspending);
            iv_delete=itemView.findViewById(R.id.iv_delete);


        }
    }
}
