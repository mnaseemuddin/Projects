package com.app.cryptok.most_popular;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.utils.BaseFragment;

public class PKLive extends BaseFragment {
    @Override
    protected int setLayout() {
        return R.layout.frag_pk_live;
    }
    RecyclerView rv_pk_live;
    Context context;
    Activity activity;
    @Override
    protected void onLaunch() {
        context=activity=getActivity();
        rv_pk_live=find(R.id.rv_pk_live);
    /*    rv_pk_live.setLayoutManager(new LinearLayoutManager(context));
        rv_pk_live.setAdapter(new PKLiveAdapter());*/
    }
}
