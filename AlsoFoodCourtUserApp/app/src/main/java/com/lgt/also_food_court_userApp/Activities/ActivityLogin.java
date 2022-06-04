package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lgt.also_food_court_userApp.Adapters.LoginSliderAdapter;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.extra.DummyUrls;
import com.lgt.also_food_court_userApp.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity {

    private ImageView iv_back;
    private TextView tv_forgotPassword, tvSignup_Login;
    private EditText et_LoginMobile;
    private Button btnLoginUser;
    private String Uuser_id, Uname, Uemail, Umobile, Uotp, UMOBILE;
    private SharedPreferences.Editor editor;
    private ProgressDialog dialog;
    private LinearLayout LL_Signup;
    //Slider

    private SliderView login_sliders;
    private ArrayList<String>slider_images_list=new ArrayList<>();
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        editor = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE).edit();

        Initialization();

        loadSliders();
    }



    private void Initialization() {

        iv_back = findViewById(R.id.iv_back);
        tv_forgotPassword = findViewById(R.id.tv_forgotPassword);
        btnLoginUser = findViewById(R.id.btnLoginUser);
        et_LoginMobile = findViewById(R.id.et_LoginMobile);
        tvSignup_Login = findViewById(R.id.tvSignup_Login);
        dialog=new ProgressDialog(this);
        LL_Signup= findViewById(R.id.LL_Signup);
        login_sliders= findViewById(R.id.login_sliders);

        login_sliders.setScrollTimeInSec(4);
        login_sliders.startAutoCycle();
        tvSignup_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ActivityLogin.this, ActivityForgotPassword.class));
            }
        });

        btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check permission
                askForPermission();

            }
        });
        LL_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
            }
        });
    }
    private void loadSliders() {
        slider_images_list.clear();
        slider_images_list.add(DummyUrls.slider1);
        slider_images_list.add(DummyUrls.slider2);
        slider_images_list.add(DummyUrls.slider3);
        LoginSliderAdapter detailSliderAdapter=new LoginSliderAdapter(slider_images_list,ActivityLogin.this);
        login_sliders.setSliderAdapter(detailSliderAdapter);

        login_sliders.setIndicatorAnimation(IndicatorAnimations.SWAP);
        login_sliders.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        detailSliderAdapter.notifyDataSetChanged();

    }
    private void askForPermission() {

        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (report.areAllPermissionsGranted()) {
                    //startActivity(new Intent(ActivityLogin.this, LoginOtpVerification.class));
                    Inputvalidation();
                } else if (!report.areAllPermissionsGranted()) {
                      Toast.makeText(ActivityLogin.this, "All permissions not granted", Toast.LENGTH_SHORT).show();
                } else {
                    if (report.isAnyPermissionPermanentlyDenied()) ;
                    {
                        showSettingsDialog();
                        Toast.makeText(ActivityLogin.this, "Any permission permanently denied", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).onSameThread().check();

    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLogin.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(),null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void Inputvalidation() {

        UMOBILE = et_LoginMobile.getText().toString().trim();
        if (TextUtils.isEmpty(UMOBILE)) {
            et_LoginMobile.setError("Enter Mobile Number");
            et_LoginMobile.requestFocus();
            return;
        }
        if (UMOBILE.length() < 10) {
            et_LoginMobile.setError("Mobile Number must be 10 digit");
            et_LoginMobile.requestFocus();
            return;
        }

        ApiLogin();
    }

    private void ApiLogin() {
        dialog.setMessage("Please wait...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("repokokokok",response+"");

                dialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {

                        JSONObject object = jsonObject.getJSONObject("data");

                        Uuser_id = object.getString("user_id");
                        Uname = object.getString("name");
                        Uemail = object.getString("email");
                        Umobile = object.getString("mobile");
                        Uotp = object.getString("otp");
                        Log.e("5285522", response + "");

                        Log.e("dasdasdasd",Uotp+"");

                        Intent intent = new Intent(ActivityLogin.this, LoginOtpVerification.class);
                        intent.putExtra("user_id", Uuser_id);
                        intent.putExtra("name", Uname);
                        intent.putExtra("email", Uemail);
                        intent.putExtra("mobile", Umobile);
                        intent.putExtra("otp", Uotp);

                        startActivity(intent);

                        //Toast.makeText(ActivityLogin.this, "Login successful", Toast.LENGTH_SHORT).show();

                    } else if (status.equals("0")) {

                        Toast.makeText(ActivityLogin.this, "" + message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ActivityLogin.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // params.put("email", email);
                params.put("mobile", UMOBILE);

                Log.e("AAAASSSS", params + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ActivityLogin.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
