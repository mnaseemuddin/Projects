package com.lgt.NeWay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lgt.NeWay.Neway.R;

public class WebViewActivity extends AppCompatActivity {

    private ProgressBar pbWebView;
    private WebView webViewTuicent;

    private TextView tvToolbar;
    private ImageView ivBackFullDescription;

    private String mURL = "";
    private String mTypeOfURL = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        pbWebView = findViewById(R.id.pbWebView);
        webViewTuicent = findViewById(R.id.webViewTuicent);
        tvToolbar = findViewById(R.id.tvToolbar);
        ivBackFullDescription = findViewById(R.id.ivBackFullDescription);

        pbWebView.setNestedScrollingEnabled(true);

        Intent getURL = getIntent();
        if (getURL != null) {
            if (getURL.hasExtra("KEY_WEB_URL")) {
                mURL = getURL.getStringExtra("KEY_WEB_URL");
                mTypeOfURL = getURL.getStringExtra("KEY_URL_TYPE");

                tvToolbar.setText(mTypeOfURL);

                if (mURL != null) {
                    if (!mURL.equalsIgnoreCase("")) {
                        webViewTuicent.loadUrl(mURL);
                    }
                    else {
                        Toast.makeText(WebViewActivity.this, "URL is not valid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }
        }

        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        WebSettings webSettings = webViewTuicent.getSettings();

        webViewTuicent.getSettings().setJavaScriptEnabled(true);
        webViewTuicent.getSettings().setUseWideViewPort(true);

        webSettings.setDomStorageEnabled(true);

        webViewTuicent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pbWebView.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pbWebView.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (webViewTuicent.canGoBack()) {
            webViewTuicent.goBack();
        } else {
            super.onBackPressed();
        }
    }

}