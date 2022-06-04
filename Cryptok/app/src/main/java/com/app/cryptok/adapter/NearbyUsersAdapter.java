package com.app.cryptok.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.databinding.InflateUsersBinding;

public class NearbyUsersAdapter extends RecyclerView.ViewHolder {

    public InflateUsersBinding holder_binding;

    public NearbyUsersAdapter(@NonNull View itemView) {

        super(itemView);
        holder_binding= DataBindingUtil.bind(itemView);
        if (holder_binding!=null){
            holder_binding.executePendingBindings();
        }


    }
    public InflateUsersBinding getBinding(){
        return holder_binding;
    }


}
