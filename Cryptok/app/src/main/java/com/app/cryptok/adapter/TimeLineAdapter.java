package com.app.cryptok.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.like.LikeButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimeLineAdapter extends RecyclerView.ViewHolder {

    public ImageView iv_post_image,iv_comment,iv_more;
    public CircleImageView iv_user_image;
    public LikeButton iv_like;
    public TextView tv_user_name,tv_date_time,tv_caption,tv_like_count,tv_comment_count,tv_follow;
    public TimeLineAdapter(@NonNull View itemView) {
        super(itemView);
        iv_user_image=itemView.findViewById(R.id.iv_user_image);
        iv_post_image=itemView.findViewById(R.id.iv_post_image);
        tv_user_name=itemView.findViewById(R.id.tv_user_name);
        tv_date_time=itemView.findViewById(R.id.tv_date_time);
        tv_caption=itemView.findViewById(R.id.tv_caption);
        tv_like_count=itemView.findViewById(R.id.tv_like_count);
        tv_comment_count=itemView.findViewById(R.id.tv_comment_count);
        iv_like=itemView.findViewById(R.id.iv_like);
        iv_comment=itemView.findViewById(R.id.iv_comment);
        tv_follow=itemView.findViewById(R.id.tv_follow);
        iv_more=itemView.findViewById(R.id.iv_more);
    }








}
