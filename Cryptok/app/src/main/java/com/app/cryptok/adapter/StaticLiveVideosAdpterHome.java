package com.app.cryptok.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.activity.SingleLiveStreamPreview;
import com.app.cryptok.activity.StaticvideoStreamActivity;
import com.app.cryptok.databinding.HotLiveLayoutBinding;
import com.app.cryptok.databinding.StaticLiveVideoAdapterHomeBinding;
import com.app.cryptok.go_live_module.LiveConst;
import com.app.cryptok.model.StaticLiveVideosModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StaticLiveVideosAdpterHome extends RecyclerView.Adapter<StaticLiveVideosAdpterHome.Cityholder> {
    List<StaticLiveVideosModel> mList;
    Context context;


    public StaticLiveVideosAdpterHome(List<StaticLiveVideosModel> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public StaticLiveVideosAdpterHome.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.static_live_video_adapter_home, parent, false);
        return new Cityholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaticLiveVideosAdpterHome.Cityholder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();

        holder.tv_live_title.setText(mList.get(position).getTv_live_title());
        //Glide.with(context).load(mList.get(position).getIv_live_preview()).into(holder.iv_live_preview);

        Glide.with(context)
                .load("android.resource://" + context.getPackageName() + "/" + mList.get(position).getVideopath() )
                .apply(requestOptions)
                .thumbnail(Glide.with(context).load("android.resource://" + context.getPackageName() + "/" + mList.get(position).getVideopath()))
                .into(holder.iv_live_preview);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, StaticvideoStreamActivity.class);
            intent.putExtra("VideoPath",mList.get(position).getVideopath());
            intent.putExtra("UserName",mList.get(position).getTv_live_title());
            //intent.putExtra("UserImage",Glide.with(context).load("android.resource://" + context.getPackageName() + "/" + mList.get(position).getVideopath());

            Log.e("ccggysgdgsyd",mList.get(position).getVideopath()+"");
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class Cityholder extends RecyclerView.ViewHolder {
        TextView  tv_live_title;
        ImageView iv_live_preview;

        public Cityholder(@NonNull View itemView) {
            super(itemView);

            tv_live_title=itemView.findViewById(R.id.tv_live_title);
            iv_live_preview=itemView.findViewById(R.id.iv_live_preview);

        }
    }

}