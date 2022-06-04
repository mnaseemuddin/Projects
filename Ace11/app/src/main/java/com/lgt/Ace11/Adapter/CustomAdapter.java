package com.lgt.Ace11.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.Ace11.Bean.WithdrawModel;
import com.lgt.Ace11.R;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends BaseAdapter {


    Context context;
    ArrayList<WithdrawModel> mList;

    public CustomAdapter(Context context, ArrayList<WithdrawModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.pay_type_picker, null);
        CircleImageView icon = view.findViewById(R.id.imageView);
        TextView names = view.findViewById(R.id.textView);
        Glide.with(Objects.requireNonNull(context)).load(mList.get(i).getPayTypeIcon()).diskCacheStrategy(DiskCacheStrategy.ALL).into(icon);
        names.setText(mList.get(i).getPayTypeName());
        return view;
    }
}
