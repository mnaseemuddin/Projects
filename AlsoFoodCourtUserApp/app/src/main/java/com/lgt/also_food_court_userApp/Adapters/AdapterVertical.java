package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.also_food_court_userApp.Models.ModelHorizontal;
import com.lgt.also_food_court_userApp.Models.ModelVertical;
import com.lgt.also_food_court_userApp.R;


import java.util.ArrayList;

/**
 * Created by Ranjan on 25/09/2019.
 */
public class AdapterVertical extends RecyclerView.Adapter<AdapterVertical.VerticalHolder> {

    private ArrayList<ModelVertical> listVertical;
    private Context context;

    public AdapterVertical(ArrayList<ModelVertical> listVertical, Context context) {
        this.listVertical = listVertical;
        this.context = context;


    }

    @NonNull
    @Override
    public VerticalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_vertical_items,parent,false);
        return new VerticalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalHolder holder, int position) {

        holder.tvVerticalNewCategory.setText(listVertical.get(position).getCategory_name());
        Log.e("verticalcatname",listVertical.get(position).getCategory_name()+"");
        Log.e("dsadsaasdsadsa",listVertical.get(1).getCategory_name()+"");

        Log.e("Dsadsaverticalsie",listVertical.size()+"");

        ArrayList<ModelHorizontal> listHorizontal = listVertical.get(position).getModelHorizontals();
        AdapterHorizontal adapterHorizontal = new AdapterHorizontal(listHorizontal,context);
        holder.rv_vertical.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        holder.rv_vertical.setHasFixedSize(true);
        holder.rv_vertical.setNestedScrollingEnabled(false);
        holder.rv_vertical.setAdapter(adapterHorizontal);

    }

    @Override
    public int getItemCount() {

        Log.e("dadadasd",listVertical.size()+"");
        return listVertical.size();
    }

    public static class VerticalHolder extends RecyclerView.ViewHolder {
        private TextView tvVerticalNewCategory;
        private RecyclerView rv_vertical;
        public VerticalHolder(@NonNull View itemView) {
            super(itemView);

            tvVerticalNewCategory = itemView.findViewById(R.id.tvVerticalNewCategory);
            rv_vertical = itemView.findViewById(R.id.rv_vertical);
        }
    }
}
