package com.lgt.Ace11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.lgt.Ace11.Constants.SPLASH_TIME_OUT;


public class SplashScreen extends AppCompatActivity {

    //Working on my team list activity im_GroundPlayerImage.setOnClickListener(new View.OnClickListener() {
    //                                @Override
    //                                public void onClick(View view) {
    //                                    Toast.makeText(activity, "Name->"+player_shortname+"ID"+PlayerId, Toast.LENGTH_SHORT).show();
    //                                }
    //                            });


    ImageView iv_red_ball;
    SplashScreen activity;
    Context context;


    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = activity = this;

        iv_red_ball = findViewById(R.id.iv_red_ball);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        rotateBall(iv_red_ball);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startTopic();
                if (saveLogin == true) {
                    HomeScreen();
                } else {
                    LoginScreen();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void rotateBall(ImageView iv_red_ball) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slow_rotate);
        iv_red_ball.startAnimation(animation);

    }

    private void startTopic() {

        FirebaseMessaging.getInstance().subscribeToTopic("Global")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("faileddd", "faileddddd");
                        } else {
                            Log.e("succcsedss", "successsed");

                        }
                    }
                });
    }

    public void LoginScreen() {
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        finishAffinity();
    }

    public void HomeScreen() {
        Intent i = new Intent(SplashScreen.this, HomeActivity.class);
        startActivity(i);
        finishAffinity();
    }


}
