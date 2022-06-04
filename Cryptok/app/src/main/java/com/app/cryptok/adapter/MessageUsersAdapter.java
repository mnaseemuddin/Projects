package com.app.cryptok.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.databinding.InflateMessagesBinding;

public class MessageUsersAdapter extends RecyclerView.ViewHolder{

    public InflateMessagesBinding holder_binding;

    public MessageUsersAdapter(@NonNull View itemView) {
        super(itemView);

        holder_binding= DataBindingUtil.bind(itemView);

        if (holder_binding!=null){
            holder_binding.executePendingBindings();
        }

    }
    public InflateMessagesBinding getHolder_binding(){
        return holder_binding;
    }
}
