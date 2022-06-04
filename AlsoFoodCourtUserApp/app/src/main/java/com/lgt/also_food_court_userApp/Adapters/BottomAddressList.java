package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.also_food_court_userApp.Models.ModelAddress;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.Common;

import java.util.List;

public class BottomAddressList extends RecyclerView.Adapter<BottomAddressList.ViewHolder> {

    private Context context;
    private List<ModelAddress>list;

    public BottomAddressList(Context context, List<ModelAddress> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BottomAddressList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_list_layout, parent, false);
        return new BottomAddressList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomAddressList.ViewHolder holder, int position) {

        final ModelAddress modelAddress=list.get(position);

        holder.tv_address_type.setText(modelAddress.getTv_address_type());

        holder.tv_full_address.setText(modelAddress.getTv_full_address());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("Update_Address");
                intent.putExtra(Common.address,modelAddress.getTv_full_address());
                intent.putExtra(Common.address_id,modelAddress.getAddress_id());
                intent.putExtra(Common.latitude,modelAddress.getLatitude());
                Log.e("FRDESW",modelAddress.getLatitude()+"");
                intent.putExtra(Common.logitude,modelAddress.getLogitude());
                Log.e("FRDESW123",modelAddress.getLogitude()+"");

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_address_icon;
        private TextView tv_address_type,tv_full_address,tv_delivery_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address_type=itemView.findViewById(R.id.tv_address_type);
            tv_full_address=itemView.findViewById(R.id.tv_full_address);
        }
    }
}
