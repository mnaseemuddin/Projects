package com.app.cryptok.adapter.Packages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app.cryptok.Interface.Packages.PaymentGatwayListener;
import com.app.cryptok.R;
import com.app.cryptok.databinding.PackagesListLayoutBinding;
import com.app.cryptok.model.Packages.PackageModel;
import com.app.cryptok.utils.Commn;

import java.util.List;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.ViewHolder> {
    private List<PackageModel> mList;
    private Context context;
    private PackagesListLayoutBinding binding;
    private PaymentGatwayListener gatwayListener;

    public PackagesAdapter(List<PackageModel> mList, Context context, PaymentGatwayListener gatwayListener) {
        this.mList = mList;
        this.context = context;
        this.gatwayListener = gatwayListener;
    }

    @NonNull
    @Override
    public PackagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.packages_list_layout, parent, false);
        return new PackagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackagesAdapter.ViewHolder holder, int position) {

        try {
            PackageModel model = mList.get(position);
            Commn.showDebugLog("packages_list:" + new Gson().toJson(model) + "");

            binding.tvPackageAmount.setText("INR "+model.getAmount());
            binding.tvPackageCoin.setText(model.getPackage_name());
            holder.itemView.setOnClickListener(view -> {

                gatwayListener.onPackageClick(model);
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }
}
