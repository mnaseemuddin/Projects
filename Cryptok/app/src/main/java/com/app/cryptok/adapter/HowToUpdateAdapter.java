package com.app.cryptok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.model.LevelPOJO;

import java.util.ArrayList;

public class HowToUpdateAdapter extends RecyclerView.Adapter<HowToUpdateAdapter.HowToUpdateHolder> {
    Context context;
    ArrayList<LevelPOJO> levelPOJOS;
    public HowToUpdateAdapter(Context context,ArrayList<LevelPOJO> pojoArrayList) {
        this.context = context;
        levelPOJOS=pojoArrayList;
    }

    @NonNull
    @Override
    public HowToUpdateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.inflate_how_to_update,parent,false);
        return new HowToUpdateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HowToUpdateHolder holder, int position) {
        try {
            holder.tv_title.setText(levelPOJOS.get(position).title);
            holder.tv_desc.setText(levelPOJOS.get(position).desc);
            holder.iv_icon.setImageResource(levelPOJOS.get(position).image);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return levelPOJOS.size();
    }

    public class HowToUpdateHolder extends CustomHolder {
        TextView tv_title;
        TextView tv_desc;
        ImageView iv_icon;
        public HowToUpdateHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=find(R.id.tv_title);
            tv_desc=find(R.id.tv_description);
            iv_icon=find(R.id.iv_icon);

        }
    }
}
