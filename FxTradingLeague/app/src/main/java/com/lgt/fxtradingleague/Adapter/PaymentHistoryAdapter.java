package com.lgt.fxtradingleague.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.Bean.PayHistroyModel;
import com.lgt.fxtradingleague.R;

import java.util.List;
import java.util.Random;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHolder> {

    Context mContext;
    List<PayHistroyModel> mList;

    public PaymentHistoryAdapter(Context mContext, List<PayHistroyModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public PaymentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.payment_histroy,parent,false);
        return new PaymentHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHolder holder, int position) {
        // holder.tv_addbalance_amt.setText(getSaltString());
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class PaymentHolder extends RecyclerView.ViewHolder{
        // TextView tv_addbalance_amt,tv_addbalance_status;
        public PaymentHolder(@NonNull View itemView) {
            super(itemView);
            //tv_addbalance_amt = itemView.findViewById(R.id.tv_addbalance_amt);
            //tv_addbalance_status = itemView.findViewById(R.id.tv_addbalance_status);
        }
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 30) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
