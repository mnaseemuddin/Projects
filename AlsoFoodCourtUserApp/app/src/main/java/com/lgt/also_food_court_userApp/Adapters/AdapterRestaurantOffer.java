package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.also_food_court_userApp.Activities.RestaurantDescription;
import com.lgt.also_food_court_userApp.Models.ModelRestaurantOffer;
import com.lgt.also_food_court_userApp.R;

import java.util.List;

public class AdapterRestaurantOffer extends RecyclerView.Adapter<AdapterRestaurantOffer.ViewHolder> {
    private Context context;
    private List<ModelRestaurantOffer>list;

    public AdapterRestaurantOffer(Context context, List<ModelRestaurantOffer> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterRestaurantOffer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.food_layout,parent,false);
        return new AdapterRestaurantOffer.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRestaurantOffer.ViewHolder holder, final int position) {

        ModelRestaurantOffer model=list.get(position);
        Glide.with(context).load(model.getFood_image()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.image_not_available_new)).into(holder.iv_food_image);
        holder.tv_food_name.setText(model.getFood_name());
        holder.tv_restaurantDiscounted.setText(model.getTv_restaurantDiscounted());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, RestaurantDescription.class);
                intent.putExtra("KEY_RESTAURANT_ID",list.get(position).getRestaurant_Id());
                context.startActivity(intent);
            }
        });


      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, new FragmentOffer()).addToBackStack("Fragment_Offers").commit();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_food_image;
        private TextView tv_food_name,tv_restaurantDiscounted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_food_image=itemView.findViewById(R.id.iv_food_image);
            tv_food_name=itemView.findViewById(R.id.tv_food_name);
            tv_restaurantDiscounted=itemView.findViewById(R.id.tv_restaurantDiscounted);
        }
    }
}
