package com.app.cryptok.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.databinding.NotificationsLayoutBinding;

public class AllNotificationHolder extends RecyclerView.ViewHolder {

    public NotificationsLayoutBinding binding;

    public AllNotificationHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        if (binding != null) {
            binding.executePendingBindings();
        }
    }

    public NotificationsLayoutBinding getBinding() {
        return binding;
    }
}
