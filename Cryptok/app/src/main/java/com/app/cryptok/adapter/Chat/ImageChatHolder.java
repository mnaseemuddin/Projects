package com.app.cryptok.adapter.Chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.databinding.ImageChatLayoutBinding;

public class ImageChatHolder extends RecyclerView.ViewHolder {
    private final ImageChatLayoutBinding image_binding;

    public ImageChatHolder(@NonNull View itemView) {
        super(itemView);
        image_binding = DataBindingUtil.bind(itemView);
        if (image_binding != null) {
            image_binding.executePendingBindings();
        }
    }

    public ImageChatLayoutBinding getImage_binding() {
        return image_binding;
    }
}
