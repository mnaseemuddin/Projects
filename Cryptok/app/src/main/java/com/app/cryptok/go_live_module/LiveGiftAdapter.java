package com.app.cryptok.go_live_module;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.adapter.ItemClickListner;
import com.app.cryptok.utils.Commn;

import java.util.List;


public class LiveGiftAdapter extends RecyclerView.Adapter<LiveGiftAdapter.LiveGiftHolder> {
    Context context;
    List<LiveGiftPOJO> liveGiftPOJOS;
    private ItemClickListner itemClickListner;


    public LiveGiftAdapter(Context context, List<LiveGiftPOJO> liveGiftPOJOS, ItemClickListner itemClickListner) {
        this.context = context;
        this.liveGiftPOJOS = liveGiftPOJOS;

        this.itemClickListner = itemClickListner;
    }

    @NonNull
    @Override
    public LiveGiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inflate_live_gift, parent, false);
        return new LiveGiftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveGiftHolder holder, int position) {

        LiveGiftPOJO pojo = liveGiftPOJOS.get(position);

        Glide.with(context).load(pojo.getGiftImage()).placeholder(R.drawable.placeholder_user).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_gift);

        holder.tv_beans_value.setText(pojo.getValue() + "");

        holder.itemView.setOnClickListener(view -> {

            Commn.showErrorLog("selected_adapter:" + pojo.getGiftImage() + "");
            Intent intent = new Intent(DBConstants.SEND_GIFT_ADAPTER);
            intent.putExtra(DBConstants.SEND_GIFT_ADAPTER, pojo.getGiftImage());
            intent.putExtra(DBConstants.SEND_GIFT_VALUE, pojo.getValue());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        });
    }


    @Override
    public int getItemCount() {
        return liveGiftPOJOS.size();
    }

    public class LiveGiftHolder extends RecyclerView.ViewHolder {

        private ImageView iv_gift;
        private TextView tv_beans_value ;


        public LiveGiftHolder(@NonNull View itemView) {
            super(itemView);
            iv_gift = itemView.findViewById(R.id.iv_gift);
            tv_beans_value = itemView.findViewById(R.id.tv_beans_value);



        }
    }
}
