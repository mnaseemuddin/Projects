package com.app.cryptok.LiveShopping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.databinding.AddressListLayoutBinding;
import com.app.cryptok.LiveShopping.Model.AddressListModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.FastClickUtil;

import java.util.ArrayList;
import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {

    public ArrayList<AddressListModel.Datum> mList = new ArrayList<>();
    private Context context;

    public OnAddressHolder onAddressHolder;
    private String type = "";

    public AddressListAdapter(String chooseType) {
        this.type = chooseType;
    }

    @NonNull
    @Override
    public AddressListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.address_list_layout, parent, false);
        return new AddressListAdapter.ViewHolder(view);
    }

    public interface OnAddressHolder {
        void onAddressClick(int type, int position, AddressListModel.Datum model, AddressListLayoutBinding holder_binding);
    }

    public void updateData(List<AddressListModel.Datum> list) {
        mList = (ArrayList<AddressListModel.Datum>) list;
        notifyDataSetChanged();
    }

    public void loadMore(List<AddressListModel.Datum> data) {
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListAdapter.ViewHolder holder, int position) {
        AddressListModel.Datum model = mList.get(position);

        if (Commn.CHOOSE_TYPE.equalsIgnoreCase(type)) {
            holder.binding.llOtherOption.setVisibility(View.GONE);
        } else {
            holder.binding.llOtherOption.setVisibility(View.VISIBLE);
        }

        holder.binding.tvAddressType.setText(model.getAddressTitle());
        holder.binding.tvFullAddress.setText(model.getAddress());

        holder.binding.tvDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                onAddressHolder.onAddressClick(1, holder.getAdapterPosition(), model, holder.binding);
            }
        });
        holder.binding.tvEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                onAddressHolder.onAddressClick(2, holder.getAdapterPosition(), model, holder.binding);
            }
        });

        holder.binding.getRoot().setOnClickListener(view -> {
            if (FastClickUtil.isFastClick()) {
                return;
            }
            if (Commn.CHOOSE_TYPE.equalsIgnoreCase(type)) {
                onAddressHolder.onAddressClick(3, holder.getAdapterPosition(), model, holder.binding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private AddressListLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }
    }


}

