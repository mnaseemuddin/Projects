package com.app.arthasattva.adapter.Chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.arthasattva.databinding.TextChatLayoutBinding;

public class TextChatHolder extends RecyclerView.ViewHolder {

    private final TextChatLayoutBinding textChatBinding;

    public TextChatHolder(@NonNull View itemView) {
        super(itemView);
        textChatBinding = DataBindingUtil.bind(itemView);
        if (textChatBinding != null) {
            textChatBinding.executePendingBindings();
        }

    }

    public TextChatLayoutBinding getTextChatBinding() {
        return textChatBinding;
    }
}
