package com.app.cryptok.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;

public class LetsPartyAdapter extends RecyclerView.Adapter<LetsPartyAdapter.LetsPartyHolder> {
    int listCount;

    public LetsPartyAdapter(int listCount) {
        this.listCount = listCount;
    }

    @NonNull
    @Override
    public LetsPartyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_lets_party,parent,false);
        return new LetsPartyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LetsPartyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listCount;
    }

    public class LetsPartyHolder extends CustomHolder {
        public LetsPartyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
