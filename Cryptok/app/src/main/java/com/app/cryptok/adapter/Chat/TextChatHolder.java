package com.app.cryptok.adapter.Chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.databinding.TextChatLayoutBinding;

public class TextChatHolder extends RecyclerView.ViewHolder {

    private TextChatLayoutBinding text_chat_binding;

    public TextChatHolder(@NonNull View itemView) {
        super(itemView);

        text_chat_binding = DataBindingUtil.bind(itemView);
        if (text_chat_binding != null) {
            text_chat_binding.executePendingBindings();
        }
    }

    public TextChatLayoutBinding getText_chat_binding() {
        return text_chat_binding;
    }
}
