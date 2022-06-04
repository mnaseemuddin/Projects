package com.app.cryptok.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomHolder extends RecyclerView.ViewHolder {
    View view;
    public CustomHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
    }
    public final <T extends android.view.View> T find(int id) {
        /* compiled code */
        return view.findViewById(id);
    }
}
