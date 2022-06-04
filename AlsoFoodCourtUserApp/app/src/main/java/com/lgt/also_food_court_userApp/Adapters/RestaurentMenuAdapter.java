package com.lgt.also_food_court_userApp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.also_food_court_userApp.Models.RestaurentMenuModel;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.ItemClickListener;

import java.util.ArrayList;

import static com.lgt.also_food_court_userApp.Activities.RestaurantDescription.restaurent_dialog;

public class RestaurentMenuAdapter extends RecyclerView.Adapter<RestaurentMenuAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RestaurentMenuModel>list;
    ItemClickListener itemClickListener;
    public RestaurentMenuAdapter(Context context, ArrayList<RestaurentMenuModel> list,ItemClickListener clickListener) {
        this.context = context;
        this.list = list;
        itemClickListener=clickListener;
    }


    @NonNull
    @Override
    public RestaurentMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.restaurent_menu_layout,parent,false);
        return new RestaurentMenuAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurentMenuAdapter.ViewHolder holder, final int position) {

        final RestaurentMenuModel model=list.get(position);
        holder.tv__food_name.setText(model.getFood_name());
        holder.tv_available_quantity.setText(model.getFood_quantity());


        holder.ll_ItemData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.itemClick(position);

                restaurent_dialog.dismiss();



            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv__food_name,tv_available_quantity;
        private LinearLayout ll_ItemData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv__food_name=itemView.findViewById(R.id.tv_food_name);
            tv_available_quantity=itemView.findViewById(R.id.tv_available_quantity);
            ll_ItemData=itemView.findViewById(R.id.ll_ItemData);
        }
    }
}
