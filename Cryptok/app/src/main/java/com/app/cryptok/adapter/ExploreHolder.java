package com.app.cryptok.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.databinding.ExploreStreamLayoutBinding;

public class ExploreHolder extends RecyclerView.ViewHolder {

    public ExploreStreamLayoutBinding binding;
    public ExploreHolder(@NonNull View itemView) {
        super(itemView);

        binding= DataBindingUtil.bind(itemView);
    }
    public ExploreStreamLayoutBinding getHolder_binding() {
        return binding;
    }
}
