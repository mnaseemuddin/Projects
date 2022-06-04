package com.lgt.fxtradingleague;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.lgt.fxtradingleague.Constants.SPLASH_TIME_OUT;


public class SplashScreen extends AppCompatActivity {

    SplashScreen activity;
    Context context;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = activity= this;


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startTopic();
                if (saveLogin == true) {
                    HomeScreen();
                }
                else {
                    LoginScreen();
                }

            }
        }, SPLASH_TIME_OUT);

    }

    private void startTopic() {

        FirebaseMessaging.getInstance().subscribeToTopic("Global")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("faileddd","faileddddd");
                        }else {
                            Log.e("succcsedss","successsed");

                        }
                    }});
    }

    public void LoginScreen(){
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        finishAffinity();
    }
    public void HomeScreen(){
        Intent i = new Intent(SplashScreen.this, HomeActivity.class);
        startActivity(i);
        finishAffinity();
    }



}
