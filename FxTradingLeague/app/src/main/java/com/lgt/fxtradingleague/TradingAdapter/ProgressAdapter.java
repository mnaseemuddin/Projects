package com.lgt.fxtradingleague.TradingAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.R;

import java.util.ArrayList;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressHolder> {

    Context mContext;
    int Count;
    ArrayList<Integer> mListCount;

    public ProgressAdapter(Context mContext, int count) {
        this.mContext = mContext;
        this.Count = count;
    }

    @NonNull
    @Override
    public ProgressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.progress_count_item,parent,false);
        return new ProgressHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressHolder holder, int position) {
        int myColor = mContext.getResources().getColor(R.color.green);
        int myColorPreview = mContext.getResources().getColor(R.color.ptl_color);
        for (int i=0;i<Count;i++){
            Log.d("Count","outer : "+Count);

            if (Count == position){
                Log.d("Count","inner : "+Count);
                holder.ll_progressBox.setBackgroundColor(myColor);
            }
            if (holder.ll_progressBox.getDrawingCacheBackgroundColor() == myColor){
                holder.ll_progressBox.setBackgroundColor(myColor);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    public class ProgressHolder extends RecyclerView.ViewHolder{
        LinearLayout ll_progressBox;
        TextView tv_progressItemsView;
        public ProgressHolder(@NonNull View itemView) {
            super(itemView);
            tv_progressItemsView = itemView.findViewById(R.id.tv_progressItemsView);
            ll_progressBox = itemView.findViewById(R.id.ll_progressBox);
        }
    }
}
