package com.app.cryptok.adapter.Packages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.app.cryptok.Interface.Packages.ExchangeDiamondsListener;
import com.app.cryptok.R;
import com.app.cryptok.databinding.PackagesListLayoutBinding;
import com.app.cryptok.model.Packages.ExchangeDiamondModel;
import com.app.cryptok.utils.Commn;

import java.util.List;

public class ExchangeDiamondsAdapter extends RecyclerView.Adapter<ExchangeDiamondsAdapter.ViewHolder> {
    private List<ExchangeDiamondModel> mList;
    private Context context;
    private PackagesListLayoutBinding binding;
    private ExchangeDiamondsListener diamondsListener;

    public ExchangeDiamondsAdapter(List<ExchangeDiamondModel> mList, Context context, ExchangeDiamondsListener diamondsListener) {
        this.mList = mList;
        this.context = context;
        this.diamondsListener = diamondsListener;
    }

    @NonNull
    @Override
    public ExchangeDiamondsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.packages_list_layout, parent, false);
        return new ExchangeDiamondsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeDiamondsAdapter.ViewHolder holder, int position) {

        try {
            ExchangeDiamondModel model = mList.get(position);
            Commn.showDebugLog("packages_list:" + new Gson().toJson(model) + "");

            binding.tvPackageAmount.setText("Beans "+model.getBeans());
            binding.tvPackageCoin.setText(model.getDiamonds());
            holder.itemView.setOnClickListener(view -> {

                diamondsListener.onExchangeClick(model);
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
