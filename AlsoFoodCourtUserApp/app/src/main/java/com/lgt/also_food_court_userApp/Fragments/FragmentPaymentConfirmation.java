package com.lgt.also_food_court_userApp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgt.also_food_court_userApp.Activities.MainActivity;
import com.lgt.also_food_court_userApp.R;


public class FragmentPaymentConfirmation extends AppCompatActivity {

    ImageView iv_back_ordering;
    private TextView tvOrderID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment_confirmation);


        tvOrderID = findViewById(R.id.tvOrderID);
        // Bundle bundle = getArguments();
        Intent intent=getIntent();

        if (intent != null) {
            if (intent.hasExtra("ORDER_ID")) {
                tvOrderID.setText(intent.getStringExtra("ORDER_ID"));

            }
        }

        iv_back_ordering = findViewById(R.id.iv_back_ordering);

        iv_back_ordering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack("Fragment_Order_Confirmation", FragmentManager.POP_BACK_STACK_INCLUSIVE);
*/
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                /*if (getApplicationContext()!=null){
                    getApplicationContext().finishAffinity();
                }*/

                startActivity(intent);
                finishAffinity();
            }
        }, 3000);





    }




}
