package com.app.arthasattva.fragment.home;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.arthasattva.R;
import com.app.arthasattva.adapter.ItemClickListner;
import com.app.arthasattva.adapter.MultiLiveAdapter;
import com.app.arthasattva.utils.BaseFragment;

public class MultiLiveFragment extends BaseFragment implements ItemClickListner {
    @Override
    protected int setLayout() {
        return R.layout.frag_live_view;
    }

    MultiLiveAdapter adapter;
    RecyclerView rv_nearby;
    Context context;
    Activity activity;

    @Override
    protected void onLaunch() {
        context = activity = getActivity();
        rv_nearby = find(R.id.rv_nearby);
        rv_nearby.setLayoutManager(new GridLayoutManager(context,2));
        adapter = new MultiLiveAdapter(16,this);
        rv_nearby.setAdapter(adapter);
    }

    @Override
    public void itemClick(int position,String clickType) {

    }
}
