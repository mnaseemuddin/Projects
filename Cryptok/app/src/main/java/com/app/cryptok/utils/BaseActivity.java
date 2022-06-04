package com.app.cryptok.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.cryptok.R;

import java.io.Serializable;
import java.util.HashMap;

abstract public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        onLaunch();
    }
   protected abstract int setLayout();
   protected abstract void onLaunch();


    public void setCustomToolBar(Toolbar toolBar){
        setSupportActionBar(toolBar);
    }
    Toolbar toolbar;
    public void setBackWithToolBar(String title){
        toolbar=find(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        toolbar.setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public <T extends android.view.View> T find(int id) {
        return findViewById(id);
    }
    public void goTo(Class className,Boolean isFinish){
        Intent intent=new Intent(this,className);
        startActivity(intent);
        if (isFinish){
            finish();
        }
    }
    public void goTo(Class className, Boolean isFinish, HashMap hashMap){
        Intent intent=new Intent(this,className);
        intent.putExtra(UrlContainer.TRANSFER_DATA,hashMap);
        startActivity(intent);
        if (isFinish){
            finish();
        }
    }
    public void goTo(Class className, Boolean isFinish, Object serializableObject){
        Intent intent=new Intent(this,className);
        intent.putExtra(UrlContainer.TRANSFER_DATA, (Serializable) serializableObject);
        startActivity(intent);
        if (isFinish){
            finish();
        }
    }


}
