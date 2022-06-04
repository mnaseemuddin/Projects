package com.app.cryptok.go_live_module.live_features;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app.cryptok.R;
import com.app.cryptok.adapter.ItemClickListner;
import com.app.cryptok.go_live_module.LiveGiftPOJO;

import java.util.ArrayList;

public class SteakerFragment extends BottomSheetDialogFragment implements ItemClickListner {

    String url = "";
    RecyclerView rv_word_steakers;
    RecyclerView rv_gift_list;
    SteakerAdapter giftAdapter;
    SteakerAdapter adapter;
    ArrayList<LiveGiftPOJO> liveGiftPOJOS;
    Context context;
    Activity activity;
    ItemClickListner itemClickListner;

    public SteakerFragment(String url, ItemClickListner clickListner) {
        this.url = url;
        itemClickListner = clickListner;
    }

    View itemView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_steakers, container, false);
        //  liveGiftDialog(view);
       // ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        itemView = view;
        return view;
    }

    public <T extends android.view.View> T find(int id) {
        return itemView.findViewById(id);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        onLaunch();
    }

    protected void onLaunch() {
        context = activity = getActivity();
        rv_word_steakers = find(R.id.rv_gift_list1);
        rv_gift_list = find(R.id.rv_gift_list);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
        rv_gift_list.setLayoutManager(layoutManager);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
        rv_word_steakers.setLayoutManager(layoutManager1);

        liveGiftPOJOS = new ArrayList<>();
        giftAdapter = new SteakerAdapter(context, SteakerFragment.this,false);
        adapter = new SteakerAdapter(context, SteakerFragment.this,true);
        rv_gift_list.setAdapter(giftAdapter);
        rv_word_steakers.setAdapter(adapter);

    }

    @Override
    public void itemClick(int position, String clickType) {
        itemClickListner.itemClick(position, clickType);
    }
}
