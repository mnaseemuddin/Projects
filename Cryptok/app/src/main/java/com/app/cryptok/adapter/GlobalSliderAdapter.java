package com.app.cryptok.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.activity.ViewImageActivity;
import com.app.cryptok.LiveShopping.Model.UsersImagesModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class GlobalSliderAdapter extends SliderViewAdapter<GlobalSliderAdapter.ViewHolder> {

    private List<UsersImagesModel> list;
    private Context context;



    public GlobalSliderAdapter(List<UsersImagesModel> list, Context context) {


        this.list = list;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.home_slider_layout,parent,false);
        return new GlobalSliderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        try {
            final UsersImagesModel model=list.get(position);

            Glide.with(context).load(model.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.iv_home_slider);

            viewHolder.iv_home_slider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewImageActivity.class);
                    intent.putExtra(DBConstants.user_image, model.getImage());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder {

        ImageView iv_home_slider;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_home_slider=itemView.findViewById(R.id.iv_home_slider);
        }
    }
}
