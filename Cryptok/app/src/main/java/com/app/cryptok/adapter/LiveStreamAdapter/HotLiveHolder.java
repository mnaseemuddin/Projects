package com.app.cryptok.adapter.LiveStreamAdapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.databinding.HotLiveLayoutBinding;

public class HotLiveHolder extends RecyclerView.ViewHolder {

    public HotLiveLayoutBinding holder_binding;

    public HotLiveHolder(@NonNull View itemView) {
        super(itemView);
        holder_binding = DataBindingUtil.bind(itemView);
        if (holder_binding != null) {
            holder_binding.executePendingBindings();
        }
    }

    public HotLiveLayoutBinding getHolder_binding() {
        return holder_binding;
    }
}
