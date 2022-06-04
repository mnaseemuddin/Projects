package com.lgt.fxtradingleague.TradingAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.R;

import java.util.ArrayList;

public class CoinDetailsAdapter extends RecyclerView.Adapter<CoinDetailsAdapter.Detailsholder> {

    ArrayList<String> mListDetails;
    Context mContext;

    public CoinDetailsAdapter(ArrayList<String> mListDetails, Context mContext) {
        this.mListDetails = mListDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Detailsholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.coin_update_items,parent,false);
        return new Detailsholder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Detailsholder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "You Clicked "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class Detailsholder extends RecyclerView.ViewHolder{
        TextView tv_update_date,tv_change_pt,tv_change_per;
        public Detailsholder(@NonNull View itemView) {
            super(itemView);
            tv_change_per = itemView.findViewById(R.id.tv_change_per);
            tv_update_date = itemView.findViewById(R.id.tv_update_date);
            tv_change_pt = itemView.findViewById(R.id.tv_change_pt);
        }
    }
}
