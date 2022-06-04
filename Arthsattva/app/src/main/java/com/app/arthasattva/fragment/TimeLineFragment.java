package com.app.arthasattva.fragment;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.arthasattva.R;
import com.app.arthasattva.adapter.TimeLineAdapter;
import com.app.arthasattva.utils.BaseFragment;

public class TimeLineFragment extends BaseFragment {
    @Override
    protected int setLayout() {
        return R.layout.frag_nearby;
    }

    TimeLineAdapter adapter;
    RecyclerView rv_timeline;
    Context context;
    Activity activity;

    @Override
    protected void onLaunch() {
        context = activity = getActivity();
        rv_timeline = find(R.id.rv_nearby);
        rv_timeline.setLayoutManager(new LinearLayoutManager(context));
       /* adapter = new TimeLineAdapter();
        rv_timeline.setAdapter(adapter);*/
    }
}
