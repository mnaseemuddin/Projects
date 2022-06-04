package com.app.cryptok.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;

public class ViewImageActivity extends AppCompatActivity {

    PhotoView iv_post_image;

    private String post_image;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        setTheme(R.style.View_Image_Theme);
        Initialization();
    }

    private void Initialization() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (getIntent() != null) {
            if (getIntent().getStringExtra(DBConstants.user_image) != null) {
                post_image = getIntent().getStringExtra(DBConstants.user_image);

            }

        }

        iv_back = findViewById(R.id.iv_back);

        iv_post_image = findViewById(R.id.iv_post_image);
        Glide.with(getApplicationContext()).load(post_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_post_image);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}