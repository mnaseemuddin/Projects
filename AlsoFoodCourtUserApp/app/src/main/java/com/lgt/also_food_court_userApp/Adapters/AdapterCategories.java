package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.also_food_court_userApp.Models.ModelCategories;
import com.lgt.also_food_court_userApp.Models.TopBrandAllProductListActivity;
import com.lgt.also_food_court_userApp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.SubCategoryHolder> {

    private List<ModelCategories> list;
    private Context context;

    public AdapterCategories(List<ModelCategories> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_categories,parent,false);
        return new SubCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryHolder holder, final int position) {

        holder.tv_Cat_Details.setText(list.get(position).getDetails());
        Glide.with(context).load(list.get(position).getImageURL()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.image_not_available_new)).into(holder.iv_catImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, TopBrandAllProductListActivity.class);
                intent.putExtra("BRAND_NAME",list.get(position).getDetails());
                intent.putExtra("Brand_ID",list.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SubCategoryHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_catImage;
        TextView tv_Cat_Details;
        public SubCategoryHolder(@NonNull View itemView) {
            super(itemView);

            iv_catImage = itemView.findViewById(R.id.iv_catImage);
            tv_Cat_Details = itemView.findViewById(R.id.tv_Cat_Details);

        }
    }


}
