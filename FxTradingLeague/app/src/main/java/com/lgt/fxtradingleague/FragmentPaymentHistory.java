package com.lgt.fxtradingleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.Adapter.PaymentHistoryAdapter;
import com.lgt.fxtradingleague.Bean.PayHistroyModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentPaymentHistory extends Fragment {

    // init
    RecyclerView rv_payment_history;
    PaymentHistoryAdapter paymentHistoryAdapter;
    List<PayHistroyModel> mList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.payment_histroy, container, false);
        // init view
        initView(mView);

        return mView;
    }

    private void initView(View mView) {
        rv_payment_history = mView.findViewById(R.id.rv_payment_history);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rv_payment_history.setLayoutManager(linearLayoutManager);
        rv_payment_history.hasFixedSize();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter();
    }

    private void setAdapter() {
        paymentHistoryAdapter = new PaymentHistoryAdapter(getContext(),mList);
        paymentHistoryAdapter.notifyDataSetChanged();
        rv_payment_history.setAdapter(paymentHistoryAdapter);
    }
}
