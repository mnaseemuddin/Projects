package com.app.cryptok.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.cryptok.R;

import java.util.ArrayList;

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ImagePreviewHolder> {
    Context context;
    ArrayList<String> imagePathList;
    OnRemove remove;
    public ImagePreviewAdapter(Context context,ArrayList<String> imagePathList,OnRemove onRemove) {
        this.context = context;
        this.imagePathList = imagePathList;

        remove=onRemove;
    }
    public interface OnRemove{
        void onRemove(int position);
    }

    @NonNull
    @Override
    public ImagePreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_image_preview,parent,false);
        return new ImagePreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePreviewHolder holder, int position) {
        try {
            Glide.with(context).load(Uri.parse(imagePathList.get(position)).getPath()).placeholder(R.drawable.ic_image_placeholder).into(holder.iv_preview);
            holder.iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove.onRemove(position);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return imagePathList.size();
    }

    public class ImagePreviewHolder extends CustomHolder{
     ImageView iv_preview;
     ImageView iv_remove;
        public ImagePreviewHolder(@NonNull View itemView) {
            super(itemView);
            iv_preview=find(R.id.iv_preview);
            iv_remove=find(R.id.iv_remove);

        }
    }
}
