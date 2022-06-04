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
import com.lgt.also_food_court_userApp.Activities.SingleProductDetails;
import com.lgt.also_food_court_userApp.Models.ModelTopBrandList;
import com.lgt.also_food_court_userApp.R;

import java.util.List;


public class AdapterTopBrandListProduct extends RecyclerView.Adapter<AdapterTopBrandListProduct.HolderTopBrand> {
   private List<ModelTopBrandList>TopBrandListProduct;
    private Context context;

    public AdapterTopBrandListProduct(List<ModelTopBrandList> topBrandListProduct, Context context) {
        TopBrandListProduct = topBrandListProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderTopBrand onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.top_brandlist_product,parent,false);
       return new HolderTopBrand(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTopBrandListProduct.HolderTopBrand holder, final int position) {

        Glide.with(context).load(TopBrandListProduct.get(position).getIv_TopBrandsItem()).into(holder.iv_TopBrandsItem);
        holder.tv_TopBrandProductName.setText(TopBrandListProduct.get(position).getTopBrandProductName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SingleProductDetails.class);
                intent.putExtra("Products_id",TopBrandListProduct.get(position).getBrandId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return TopBrandListProduct.size();
    }

    public class HolderTopBrand extends RecyclerView.ViewHolder {
        private ImageView iv_TopBrandsItem;
        private TextView tv_TopBrandProductName,tv_productOffer;
        public HolderTopBrand(@NonNull View itemView) {
            super(itemView);

            iv_TopBrandsItem=itemView.findViewById(R.id.iv_TopBrandsItem);
            tv_TopBrandProductName=itemView.findViewById(R.id.tv_TopBrandProductName);
            tv_productOffer=itemView.findViewById(R.id.tv_productOffer);


        }
    }
}
