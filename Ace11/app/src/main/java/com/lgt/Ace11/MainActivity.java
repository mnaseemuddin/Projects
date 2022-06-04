package com.lgt.Ace11;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView iv_animateBackground;
    TextView tv_LetsPlay;
    LinearLayout LL_StartLogin,LL_StartSignUp;
    Context context;
    MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = activity = this;
        initViews();

        LL_StartLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, com.lgt.Ace11.LoginActivity.class);
                startActivity(i);
            }
        });

        LL_StartSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity,RegistrationActivity.class);
                i.putExtra("Reffered","Yes");
                startActivity(i);
            }
        });

        tv_LetsPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity,RegistrationActivity.class);
                i.putExtra("Reffered","No");
                startActivity(i);
            }
        });

    }

    public void initViews(){
        iv_animateBackground = findViewById(R.id.iv_animateBackground);
        LL_StartSignUp = findViewById(R.id.LL_StartSignUp);
        LL_StartLogin = findViewById(R.id.LL_StartLogin);
        tv_LetsPlay = findViewById(R.id.tv_LetsPlay);
        animateBackground(iv_animateBackground);
    }

    private void animateBackground(ImageView iv_animateBackground) {
        TranslateAnimation animation = new TranslateAnimation(-400.0f, 0.0f,
                400.0f, 0.0f);        //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(5000);  // animation duration
        animation.setRepeatCount(1000);  // animation repeat count
        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
        // animation.setFillAfter(false);
        iv_animateBackground.startAnimation(animation);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
