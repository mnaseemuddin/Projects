package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.lgt.also_food_court_userApp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class LoginSliderAdapter extends SliderViewAdapter<LoginSliderAdapter.ViewHolder> {

    ArrayList<String>list;
    private Context context;


    public LoginSliderAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.login_slider_layout,parent,false);
        return new LoginSliderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Glide.with(context).load(list.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.iv_slider_images);

        viewHolder.iv_slider_images.setColorFilter(0xff555555, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder {

        ImageView iv_slider_images;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_slider_images=itemView.findViewById(R.id.iv_slider_images);
        }
    }
}
  /*  ImageView iv_slider_images=view.findViewById(R.id.iv_slider_images);
        Glide.with(context).load(model).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_slider_images);*/