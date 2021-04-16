package com.lgt.NeWay.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;

import java.util.List;

public class Splash extends AppCompatActivity {

  private   SharedPreferences sharedPreferences;
    ImageView iv_splash_logo;
 private  String USERID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splas);
        iv_splash_logo=findViewById(R.id.iv_splash_logo);

        sharedPreferences=getSharedPreferences(common.UserData,MODE_PRIVATE);
        if (sharedPreferences.contains(common.UseriD)){
            USERID=sharedPreferences.getString(common.UseriD,"");
            Log.e("hhhhhhhhhh",USERID);

        }
        Cheakpermission();
       /* */


    }

    private void Cheakpermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    // do you work now
                            /*Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "All Permission Granted Successfull", Snackbar.LENGTH_LONG);
                            snackbar.show();*/
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (USERID!=null&& !USERID.isEmpty()){
                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent= new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                                finishAffinity();

                            }


                        }
                    },2000);


                }else {

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
                    Toast.makeText(Splash.this, "Any permission permanently denied", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
            {
                token.continuePermissionRequest();
            }
        }).check();
    }
}