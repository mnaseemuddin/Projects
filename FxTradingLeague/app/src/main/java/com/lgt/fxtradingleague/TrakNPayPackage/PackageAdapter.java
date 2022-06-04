package com.lgt.fxtradingleague.TrakNPayPackage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.PaymentOptionActivity;
import com.lgt.fxtradingleague.R;

import java.util.ArrayList;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackHolder> {

    Context mContext;
    ArrayList<PackModel> mList;

    public PackageAdapter(Context mContext, ArrayList<PackModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public PackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.package_list, parent, false);
        return new PackHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PackHolder holder, final int position) {
        holder.tv_pack_name.setText(mList.get(position).package_name);
        holder.tv_pack_price.setText("₹ "+mList.get(position).package_value);
        holder.tv_pack_coin.setText(mList.get(position).package_coins +" Coin  =");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, PaymentOptionActivity.class);
                in.putExtra("FinalAmount",mList.get(position).package_value);
                in.putExtra("FinalCoin",mList.get(position).package_coins);
                mContext.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class PackHolder extends RecyclerView.ViewHolder {
        ImageView iv_pack_icon;
        TextView tv_pack_name, tv_pack_coin, tv_pack_price;
        public PackHolder(@NonNull View itemView) {
            super(itemView);
            iv_pack_icon = itemView.findViewById(R.id.iv_pack_icon);
            tv_pack_name = itemView.findViewById(R.id.tv_pack_name);
            tv_pack_coin = itemView.findViewById(R.id.tv_pack_coin);
            tv_pack_price = itemView.findViewById(R.id.tv_pack_price);
        }
    }
}
