package com.lgt.fxtradingleague.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.R;

import java.util.List;

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.ViewHolderList> {

    Context mContext;
    List<WithDrawModel> mList;

    public WithdrawAdapter(Context mContext, List<WithDrawModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_withdraw_item,parent,false);
        return new WithdrawAdapter.ViewHolderList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderList holder, int position) {
        holder.tv_HolderName.setText(mList.get(position).getBank_holder_name());
        holder.tv_Withdraw_price.setText("Withdrawn Amount: "+mList.get(position).getWithdrawn_amount());
        holder.tv_Withdraw_date.setText("Request Date: "+mList.get(position).getRequest_date());
        if (mList.get(position).getStatus().equals("0")){
            holder.iv_status.setImageResource(R.drawable.ic_pending);
            holder.tv_statusText.setText("Pending");
        }else if (mList.get(position).getStatus().equals("1")){
            holder.iv_status.setImageResource(R.drawable.ic_approved);
            holder.tv_statusText.setText("Approved");
        }else if (mList.get(position).getStatus().equals("2")){
            holder.iv_status.setImageResource(R.drawable.ic_cancel);
            holder.tv_statusText.setText("Reject");
        }
        holder.tv_HolderName.setText(mList.get(position).getBank_holder_name());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolderList extends RecyclerView.ViewHolder{
        ImageView iv_status;
        TextView tv_HolderName,tv_Withdraw_price,tv_Withdraw_date,tv_statusText;
        public ViewHolderList(@NonNull View itemView) {
            super(itemView);
            iv_status = itemView.findViewById(R.id.iv_status);
            tv_HolderName = itemView.findViewById(R.id.tv_HolderName);
            tv_statusText = itemView.findViewById(R.id.tv_statusText);
            tv_Withdraw_price = itemView.findViewById(R.id.tv_Withdraw_price);
            tv_Withdraw_date = itemView.findViewById(R.id.tv_Withdraw_date);
        }
    }
}
