package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lgt.also_food_court_userApp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;


public class AdapterSliderImage extends SliderViewAdapter< AdapterSliderImage.ViewHolder> {
    private List<String> ImageSliderArrayList;
    private Context context;


    public AdapterSliderImage(List<String> ImageSliderArrayList,Context context){
        this.ImageSliderArrayList=ImageSliderArrayList;
        this.context=context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_slider_image,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(ImageSliderArrayList.get(position)).into(holder.slider_images);
    }

    @Override
    public int getCount() {
        return ImageSliderArrayList.size();
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder{
        private ImageView slider_images;
        public ViewHolder(View itemView) {
            super(itemView);
            slider_images=itemView.findViewById(R.id.slider_images);
        }
    }
}
