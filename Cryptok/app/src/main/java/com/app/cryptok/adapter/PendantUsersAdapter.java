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

import de.hdodenhof.circleimageview.CircleImageView;

public class PendantUsersAdapter extends RecyclerView.Adapter<PendantUsersAdapter.PendentUsersHolder> {
    Context context;

    public PendantUsersAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PendentUsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.inflate_pendant_users,parent,false);
        return new PendentUsersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendentUsersHolder holder, int position) {
        if (position==0){
            holder.iv_users.setImageResource(R.drawable.timeline3);
            holder.iv_frame.setImageResource(R.drawable.silver);
            holder.tv_level.setText("48 - 59");
        }else if (position==1){
            holder.iv_users.setImageResource(R.drawable.timeline1);
            holder.iv_frame.setImageResource(R.drawable.diamond_gold);
            holder.tv_level.setText("60 - 73");
        }else {
            holder.iv_users.setImageResource(R.drawable.timeline2);
            holder.iv_frame.setImageResource(R.drawable.gold);
            holder.tv_level.setText("74 - 91");
        }
        holder.tv_name.setText("Name" + position);

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class PendentUsersHolder extends RecyclerView.ViewHolder {
    CircleImageView iv_users;
    ImageView iv_frame;
    TextView tv_name;
    TextView tv_level;
        public PendentUsersHolder(@NonNull View itemView) {
            super(itemView);
            iv_users=itemView.findViewById(R.id.iv_users);
            iv_frame=itemView.findViewById(R.id.iv_frame);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_level=itemView.findViewById(R.id.tv_level);

        }
    }
}
