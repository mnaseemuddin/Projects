package com.app.cryptok.most_popular;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.adapter.CustomHolder;

public class PKLiveAdapter extends RecyclerView.Adapter<PKLiveAdapter.PKLiveHolder> {
    @NonNull
    @Override
    public PKLiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_pk_live, parent, false);

        return new PKLiveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PKLiveHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class PKLiveHolder extends CustomHolder {
        public PKLiveHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
