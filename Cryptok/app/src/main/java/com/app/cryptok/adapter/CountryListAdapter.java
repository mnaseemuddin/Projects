package com.app.cryptok.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;

public class CountryListAdapter extends RecyclerView.ViewHolder {


    public TextView tv_country;

    public CountryListAdapter(@NonNull View itemView) {

        super(itemView);

        tv_country = itemView.findViewById(R.id.tv_country);

    }


}
