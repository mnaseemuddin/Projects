package com.lgt.fxtradingleague.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.R;

import java.util.List;

import Model.ModelPlaying11;

/**
 * Created by Ranjan on 2/4/2020.
 */
public class AdapterPlaying11 extends RecyclerView.Adapter<AdapterPlaying11.HolderPlaying11> {

    private List<ModelPlaying11> list;
    private Context context;

    public AdapterPlaying11(List<ModelPlaying11> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderPlaying11 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_playing11,parent,false);
        return new HolderPlaying11(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPlaying11 holder, int position) {
        holder.tvPlayingTeamName.setText(list.get(position).getPlayerName());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderPlaying11 extends RecyclerView.ViewHolder {

        private TextView tvPlayingTeamName;

        public HolderPlaying11(@NonNull View itemView) {
            super(itemView);

            tvPlayingTeamName = itemView.findViewById(R.id.tvPlayingTeamName);
        }
    }
}
