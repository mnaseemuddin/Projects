package com.app.arthasattva.adapter.Comment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.arthasattva.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentImageHolder extends RecyclerView.ViewHolder {

    public TextView tv_user_name,tv_comment_time,tv_comment_like,tv_comment_reply,tv_reply_user_name,tv_view_one_replies;
    public CircleImageView iv_comment_user,iv_replycomment_user;
    public ImageView iv_comment_image,iv_reply_comment_image;
    public LinearLayout ll_reply_layout;

    public CommentImageHolder(@NonNull View itemView) {
        super(itemView);
        tv_user_name=itemView.findViewById(R.id.tv_user_name);
        tv_comment_time=itemView.findViewById(R.id.tv_comment_time);
        tv_comment_like=itemView.findViewById(R.id.tv_comment_like);
        tv_comment_reply=itemView.findViewById(R.id.tv_comment_reply);
        iv_comment_user=itemView.findViewById(R.id.iv_comment_user);
        iv_comment_image=itemView.findViewById(R.id.iv_comment_image);
        tv_reply_user_name=itemView.findViewById(R.id.tv_reply_user_name);
        iv_replycomment_user=itemView.findViewById(R.id.iv_replycomment_user);
        iv_reply_comment_image=itemView.findViewById(R.id.iv_reply_comment_image);
        ll_reply_layout=itemView.findViewById(R.id.ll_reply_layout);
        tv_view_one_replies=itemView.findViewById(R.id.tv_view_one_replies);
    }
}
