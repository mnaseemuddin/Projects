package com.lgt.NeWay.activity.Gallary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lgt.NeWay.Neway.R;

import java.util.List;

public class AdapterGallary  extends RecyclerView.Adapter<AdapterGallary.Cityholder> {
    List<ModelGallary>mlist;
    Context context;
    DeleteGalleryImageInterface deleteGalleryImageInterface;

    public AdapterGallary(List<ModelGallary> mlist, Context context,DeleteGalleryImageInterface deleteGalleryImageInterface) {
        this.mlist = mlist;
        this.context = context;
        this.deleteGalleryImageInterface = deleteGalleryImageInterface;
    }

    @NonNull
    @Override
    public AdapterGallary.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adaptergallaryimage,parent,false);
        return new Cityholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGallary.Cityholder holder, int position) {
        String G_Tableid=mlist.get(position).getTbl_gallery_id();

        Log.e("sdsdsd",G_Tableid+"");
        Glide.with(context).load(mlist.get(position).getGallaryimage()).into(holder.gallaryimage);

        holder.iv_Deleteimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGalleryImageInterface.deleteImage(G_Tableid);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        ImageView gallaryimage,iv_Deleteimage;
        public Cityholder(@NonNull View itemView) {
            super(itemView);
            gallaryimage=itemView.findViewById(R.id.gallaryimage);
            iv_Deleteimage=itemView.findViewById(R.id.iv_Deleteimage);
        }
    }
}
