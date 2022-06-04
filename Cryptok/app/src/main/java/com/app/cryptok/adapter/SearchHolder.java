package com.app.cryptok.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchHolder extends RecyclerView.ViewHolder {

    public CircleImageView iv_user_image;
    public TextView tv_user_name,tv_buzo_id;

    public SearchHolder(@NonNull View itemView) {
        super(itemView);
        iv_user_image=itemView.findViewById(R.id.iv_user_image);
        tv_user_name=itemView.findViewById(R.id.tv_user_name);
        tv_buzo_id=itemView.findViewById(R.id.tv_buzo_id);
    }
}
