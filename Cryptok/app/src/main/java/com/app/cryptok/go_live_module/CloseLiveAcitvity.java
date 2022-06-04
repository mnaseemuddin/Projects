package com.app.cryptok.go_live_module;

import android.view.View;
import android.widget.ImageView;

import com.app.cryptok.R;
import com.app.cryptok.utils.BaseActivity;

public class CloseLiveAcitvity extends BaseActivity {
    @Override
    protected int setLayout() {
        return R.layout.activity_close_live;
    }
    ImageView iv_close;

    @Override
    protected void onLaunch() {
        iv_close=find(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
