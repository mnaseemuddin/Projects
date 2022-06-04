package com.app.cryptok.adapter.Comment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.databinding.CommentTextLayoutBinding;

public class CommentTextHolder extends RecyclerView.ViewHolder {

    public CommentTextLayoutBinding commentTextLayoutBind;

    public CommentTextHolder(@NonNull View itemView) {
        super(itemView);
        commentTextLayoutBind = DataBindingUtil.bind(itemView);
        if (commentTextLayoutBind != null) {
            commentTextLayoutBind.executePendingBindings();
        }
    }

    public CommentTextLayoutBinding getCommentTextLayoutBind() {
        return commentTextLayoutBind;
    }
}
