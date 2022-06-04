package com.app.arthasattva.adapter.Comment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.arthasattva.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentTextHolder extends RecyclerView.ViewHolder {
    public TextView tv_user_name,tv_comment,tv_comment_time,tv_comment_like,tv_comment_reply,tv_replyuser_name,tv_reply_comment,tv_view_one_replies;
    public CircleImageView iv_comment_user,iv_replycomment_user;
    public LinearLayout ll_reply_layout;

    public CommentTextHolder(@NonNull View itemView) {
        super(itemView);

        tv_user_name=itemView.findViewById(R.id.tv_user_name);
        tv_comment_time=itemView.findViewById(R.id.tv_comment_time);
        tv_comment_like=itemView.findViewById(R.id.tv_comment_like);
        tv_comment_reply=itemView.findViewById(R.id.tv_comment_reply);
        iv_comment_user=itemView.findViewById(R.id.iv_comment_user);
        tv_comment=itemView.findViewById(R.id.tv_comment);
        tv_replyuser_name=itemView.findViewById(R.id.tv_replyuser_name);
        tv_reply_comment=itemView.findViewById(R.id.tv_reply_comment);
        iv_replycomment_user=itemView.findViewById(R.id.iv_replycomment_user);
        ll_reply_layout=itemView.findViewById(R.id.ll_reply_layout);
        tv_view_one_replies=itemView.findViewById(R.id.tv_view_one_replies);

    }
}
