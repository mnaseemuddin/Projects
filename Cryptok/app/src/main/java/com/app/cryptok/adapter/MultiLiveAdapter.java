package com.app.cryptok.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.go_live_module.LiveClickConstants;

import pl.droidsonroids.gif.GifImageView;

public class MultiLiveAdapter extends RecyclerView.Adapter<MultiLiveAdapter.MultiLiveHolder> {
    int itemCount;
    ItemClickListner itemClickListner;
    private final static int VISIBLE_ITEM_TYPE = 1;
    private final static int INVISIBLE_ITEM_TYPE = 2;
    public MultiLiveAdapter(int count,ItemClickListner clickListner){
        itemCount=count;
        itemClickListner=clickListner;
    }

    @NonNull
    @Override
    public MultiLiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     // View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_live_view,parent,false);
        View view;
        if (viewType == INVISIBLE_ITEM_TYPE) {
            // The type is invisible, so just create a zero-height Space widget to hold the position.
            view = new Space(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_live_view, parent, false);
        }
        return new MultiLiveHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        // First 4 position don't show. The visibility of a position can be separately
        // determined if only, say, the first and third position should be invisible.
        if (itemCount==14 && position<2){
            return INVISIBLE_ITEM_TYPE;
        }
        return VISIBLE_ITEM_TYPE;
    }
    @Override
    public void onBindViewHolder(@NonNull MultiLiveHolder holder, int position) {
        try {
           if (itemCount==2){
               if (position== 0) {
                   holder.iv_live.setImageResource(R.drawable.live_image1);
               } else if (position == 1) {
                   holder.iv_live.setImageResource(R.drawable.live_image2);
               }
           }else if (itemCount==14){
               if ((position + 1) % 5 == 0) {
                   holder.iv_live.setImageResource(R.drawable.live_image5);
               }else if ((position + 1) % 4 == 0) {
                   holder.iv_live.setImageResource(R.drawable.live_image4);
               }else if ((position + 1) % 3 == 0) {
                   holder.iv_live.setImageResource(R.drawable.live_image3);
               } else if ((position + 1) % 2 == 0 && position>5) {
                   holder.iv_live.setImageResource(R.drawable.live_image2);
               } else if (position>5){
                   holder.iv_live.setImageResource(R.drawable.live_image1);
               }
           }else {
               if ((position + 1) % 5 == 0) {
                   holder.iv_live.setImageResource(R.drawable.live_image5);
               }else if ((position + 1) % 4 == 0) {
                   holder.iv_live.setImageResource(R.drawable.live_image4);
               }else if ((position + 1) % 3 == 0) {
                   holder.iv_live.setImageResource(R.drawable.live_image3);
               } else if ((position + 1) % 2 == 0) {
                   holder.iv_live.setImageResource(R.drawable.live_image2);
               } else {
                   holder.iv_live.setImageResource(R.drawable.live_image1);
               }
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListner.itemClick(position, LiveClickConstants.MULTI_LIVE_ITEM_CLICK);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public class MultiLiveHolder extends CustomHolder {
      GifImageView iv_live;
        public MultiLiveHolder(@NonNull View itemView) {
            super(itemView);
            iv_live=find(R.id.iv_live);
        }
    }
}
