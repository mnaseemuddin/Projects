package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lgt.also_food_court_userApp.R;

import java.util.List;


public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);

        mUserID = sharedPreferences.getString("user_id", "");

        Log.e("5555555", mUserID + "");

       // startActivity(new Intent(SplashActivity.this,KotlinLoginTest.class));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Dexter.withActivity(SplashActivity.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (sharedPreferences.contains("user_id")) {

                                if (!mUserID.equals("")) {

                                    Intent in = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(in);
                                    finishAffinity();

                                }


                            } else {
                                Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                                startActivity(intent);
                                finishAffinity();


                            }

                        } else {

                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "All Permission not Granted", Snackbar.LENGTH_LONG);
                    /*//change snackbar background colour
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);*/
                            snackbar.show();
                            for (int i = 0; i < report.getDeniedPermissionResponses().size(); i++) {
                                Log.d("dennial permision res",
                                        report.getDeniedPermissionResponses().get(i).getPermissionName());

                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            Toast.makeText(SplashActivity.this, "Any permission permanently denied", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


            }
        },2000);

    }

}
