package com.app.cryptok.adapter.Comment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.app.cryptok.Interface.OnCommentSelection;
import com.app.cryptok.R;
import com.app.cryptok.model.Comment.GifModel;


import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.ViewHolder> {

    private List<GifModel>list;
    private Context context;

    private OnCommentSelection onCommentSelection;
    public GifAdapter(List<GifModel> list, Context context, OnCommentSelection selection) {
        this.list = list;
        this.context = context;
        this.onCommentSelection =selection;
    }

    @NonNull
    @Override
    public GifAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gif_custom_layout, parent, false);

        return new GifAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GifAdapter.ViewHolder holder, int position) {

        GifModel model=list.get(position);

        Glide.with(context).load(model.getGif_url()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_gif_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("my_click_url",model.getGif_url()+"");
                onCommentSelection.OnGifSelection(model.getGif_url());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private GifImageView iv_gif_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_gif_image=itemView.findViewById(R.id.iv_gif_image);
        }

    }
}
