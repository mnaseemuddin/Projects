package com.app.cryptok.go_live_module;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.app.cryptok.R;
import com.app.cryptok.adapter.ItemClickListner;
import com.app.cryptok.utils.BaseFragment;

import java.util.ArrayList;

public class GiftFragment extends BaseFragment implements ItemClickListner {
    @Override
    protected int setLayout() {
        return R.layout.frag_live_gift;
    }
    String url="";
    RecyclerView rv_gift_list;
    LiveGiftAdapter giftAdapter;
    ArrayList<LiveGiftPOJO> liveGiftPOJOS;
    Context context;
    Activity activity;
    ItemClickListner itemClickListner;
    public GiftFragment(String url,ItemClickListner clickListner){
        this.url=url;
        itemClickListner=clickListner;
    }
    @Override
    protected void onLaunch() {
        context=activity=getActivity();
        rv_gift_list=find(R.id.rv_gift_list);
      /*  GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),4,RecyclerView.HORIZONTAL,false);
        rv_gift_list.setLayoutManager(layoutManager);
        liveGiftPOJOS=new ArrayList<>();
        giftAdapter=new LiveGiftAdapter(context,liveGiftPOJOS);
        rv_gift_list.setAdapter(giftAdapter);*/
    }



    @Override
    public void itemClick(int position,String clickType) {
        itemClickListner.itemClick(position,clickType);
    }
}
