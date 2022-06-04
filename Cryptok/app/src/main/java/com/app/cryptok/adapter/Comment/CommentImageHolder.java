package com.app.cryptok.adapter.Comment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.databinding.CommentImagesLayoutBinding;

public class CommentImageHolder extends RecyclerView.ViewHolder {

    public CommentImagesLayoutBinding commentImagesLayoutBind;

    public CommentImageHolder(@NonNull View itemView) {
        super(itemView);
        commentImagesLayoutBind = DataBindingUtil.bind(itemView);
        if (commentImagesLayoutBind != null) {
            commentImagesLayoutBind.executePendingBindings();
        }
    }

    public CommentImagesLayoutBinding getCommentImagesLayoutBind() {
        return commentImagesLayoutBind;
    }
}
