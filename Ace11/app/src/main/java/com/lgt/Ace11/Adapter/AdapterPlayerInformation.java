package com.lgt.Ace11.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.Ace11.R;

import java.util.List;

import Model.ModelPlayerInformation;

/**
 * Created by Ranjan on 1/30/2020.
 */
public class AdapterPlayerInformation extends RecyclerView.Adapter<AdapterPlayerInformation.HolderPlayer> {

    private Context context;
    private List<ModelPlayerInformation> listPlayer;

    public AdapterPlayerInformation(Context context, List<ModelPlayerInformation> listPlayer) {
        this.context = context;
        this.listPlayer = listPlayer;
    }

    @NonNull
    @Override
    public HolderPlayer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_player_infromation,parent,false);
        return new HolderPlayer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPlayer holder, int position) {



    }

    @Override
    public int getItemCount() {
        return listPlayer.size();
    }


    public static class HolderPlayer extends RecyclerView.ViewHolder {
        private TextView tvEvent,tvActual,tvPoints;
        public HolderPlayer(@NonNull View itemView) {
            super(itemView);

            tvEvent = itemView.findViewById(R.id.tvEvent);
            tvActual = itemView.findViewById(R.id.tvActual);
            tvPoints = itemView.findViewById(R.id.tvPoints);
        }
    }


}
