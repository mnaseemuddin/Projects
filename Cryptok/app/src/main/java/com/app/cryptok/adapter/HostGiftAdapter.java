package com.app.cryptok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.app.cryptok.R;
import com.app.cryptok.go_live_module.LiveGiftPOJO;
import com.app.cryptok.utils.Commn;

import java.util.List;

public class HostGiftAdapter extends RecyclerView.Adapter<HostGiftAdapter.ViewHolder> {

    Context context;
    List<LiveGiftPOJO> list;

    public HostGiftAdapter(Context context, List<LiveGiftPOJO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HostGiftAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_live_gift, parent, false);
        return new HostGiftAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HostGiftAdapter.ViewHolder holder, int position) {
        LiveGiftPOJO pojo=list.get(position);

        Glide.with(context).load(pojo.giftImage).placeholder(R.drawable.placeholder_user).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_gift);
        holder.tv_beans_value.setText(pojo.getValue()+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Commn.myToast(context,"You can't send yourself...");
            }
        });
    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_gift;
        private TextView tv_beans_value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_gift=itemView.findViewById(R.id.iv_gift);
            tv_beans_value=itemView.findViewById(R.id.tv_beans_value);
        }
    }
}
