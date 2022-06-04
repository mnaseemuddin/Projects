package com.app.cryptok.LiveShopping.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.app.cryptok.R;
import com.app.cryptok.databinding.ProductListLayoutBinding;
import com.app.cryptok.LiveShopping.Model.ProductModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.FastClickUtil;

import java.util.ArrayList;
import java.util.List;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    public ArrayList<ProductModel.ProductsDatum> mList = new ArrayList<>();
    private Context context;
    public OnProductClick onProductClick;
    private String choose_type;

    public ProductListAdapter(String action_type) {
        this.choose_type = action_type;
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_layout, parent, false);
        return new ProductListAdapter.ViewHolder(view);
    }

    public interface OnProductClick {
        void onProductClick(int type, int position, ProductModel.ProductsDatum model, ProductListLayoutBinding holder_binding);

        void onProductSelection(int type, int position, ProductModel.ProductsDatum model, ProductListLayoutBinding holder_binding);
    }

    public void updateData(List<ProductModel.ProductsDatum> list) {
        mList = (ArrayList<ProductModel.ProductsDatum>) list;
        notifyDataSetChanged();
    }

    public void loadMore(List<ProductModel.ProductsDatum> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {
        ProductModel.ProductsDatum model = mList.get(position);


        holder.binding.ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (Commn.CHOOSE_TYPE.equalsIgnoreCase(choose_type)) {
                    if (model.isSelected()) {
                        model.setSelected(false);
                    } else {
                        model.setSelected(true);
                    }
                    notifyDataSetChanged();
                }

            }
        });

        if (Commn.CHOOSE_TYPE.equalsIgnoreCase(choose_type)) {
            if (model.isSelected()) {
                holder.binding.ivSelectProduct.setVisibility(View.VISIBLE);
                onProductClick.onProductSelection(1, holder.getAdapterPosition(), model, holder.binding);
            } else {
                holder.binding.ivSelectProduct.setVisibility(View.GONE);
                onProductClick.onProductSelection(0, holder.getAdapterPosition(), model, holder.binding);
            }
        }


        if (model.getProimages().size() > 0) {
            Glide.with(context)
                    .load(model.getProimages().get(0).getImages())
                    .thumbnail(0.05f)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivProductImage);
        }

        holder.binding.tvMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                onProductClick.onProductClick(1, holder.getAdapterPosition(), model, holder.binding);

            }
        });
       /* holder.binding.ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                onProductClick.onProductClick(2, holder.getAdapterPosition(), model, holder.binding);


            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProductListLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }


}

