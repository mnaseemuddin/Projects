package com.lgt.also_food_court_userApp.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lgt.also_food_court_userApp.Adapters.AdapterSupport;
import com.lgt.also_food_court_userApp.Models.ModelSupport;
import com.lgt.also_food_court_userApp.R;


import java.util.ArrayList;
import java.util.List;


public class ActivitySupport extends AppCompatActivity {

    ImageView iv_back_support, iv_camera;
    EditText et_chatMessage;
    LinearLayout ll_send_chat; //Send button for chat

    List<ModelSupport> list = new ArrayList<>();
    AdapterSupport adapterSupport;
    RecyclerView rv_support_chat;

    private String mChatMessage;

    private TextView tvSendButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        rv_support_chat = findViewById(R.id.rv_support_chat);
        tvSendButton = findViewById(R.id.tvSendButton);
        et_chatMessage = findViewById(R.id.et_chatMessage);

        iv_back_support = findViewById(R.id.iv_back_support);
        iv_camera = findViewById(R.id.iv_camera);

        tvSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        iv_back_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivitySupport.this, "Coming soon...", Toast.LENGTH_LONG).show();
            }
        });
        loadRecyclerView();
    }


    private void validation() {
        mChatMessage = et_chatMessage.getText().toString().trim();

        if (TextUtils.isEmpty(mChatMessage)) {
            Toast.makeText(ActivitySupport.this, "Enter some message", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(ActivitySupport.this, "Under implementation, coming soon...", Toast.LENGTH_LONG).show();
    }

    private void loadRecyclerView() {

        list.add(new ModelSupport("1", "Hi, Welcome to O7 tech-support, my name is Ranjan, how may I help you?", "", "", "Ranjan Singh", "12:00 PM", 0));
        list.add(new ModelSupport("1", "Hi, my name is Linda, I have some concerns with my previous order.", "", "", "Linda", "1:30 PM", 1));
        list.add(new ModelSupport("1", "Sorry to hear that, we will help you. Please share us your order number to resolve the issue.", "", "", "Ranjan Singh", "12:45 PM", 0));
        list.add(new ModelSupport("1", "It's O712321, ordered on 25-September-2019", "", "", "Linda", "10:30 AM", 1));
        list.add(new ModelSupport("1", "Okay, we got your previous order of Burger, which was delivered on time, what's the issue?", "", "", "Ranjan Singh", "5:22 PM", 0));
        list.add(new ModelSupport("1", "I didn't get the cashback after successful purchase, it has been two days but cashback is not credited in my wallet", "", "", "Linda", "9:00 AM", 1));
        list.add(new ModelSupport("1", "Sorry, the cashback offer on that product was expired the day before your order", "", "", "Ranjan Singh", "10:30 AM", 0));
        list.add(new ModelSupport("1", "Ok, no problem, thanks for your help.", "", "", "Linda", "5:22 PM", 1));
        list.add(new ModelSupport("1", "Thank you for contacting tech-support, have a nice day, feel free to contact us our service is 24*7, enjoy our food. Bye", "", "", "Ranjan Singh", "9:00 AM", 0));


        adapterSupport = new AdapterSupport(list, ActivitySupport.this);

        rv_support_chat.setNestedScrollingEnabled(false);
        rv_support_chat.hasFixedSize();
        rv_support_chat.setLayoutManager(new LinearLayoutManager(ActivitySupport.this, RecyclerView.VERTICAL, false));
        rv_support_chat.setAdapter(adapterSupport);


    }


}
