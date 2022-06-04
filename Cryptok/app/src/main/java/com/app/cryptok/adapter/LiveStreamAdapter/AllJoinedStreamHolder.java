package com.app.cryptok.adapter.LiveStreamAdapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllJoinedStreamHolder extends RecyclerView.ViewHolder {
    public CircleImageView iv_user_image;
    public TextView tv_user_name;


    public AllJoinedStreamHolder(@NonNull View itemView) {
        super(itemView);
        iv_user_image=itemView.findViewById(R.id.iv_user_image);
        tv_user_name=itemView.findViewById(R.id.tv_user_name);

    }
}
