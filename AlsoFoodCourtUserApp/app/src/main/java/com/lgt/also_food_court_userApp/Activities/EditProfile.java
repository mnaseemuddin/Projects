package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lgt.also_food_court_userApp.Fragments.FragmentTestForCrop;


import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.extra.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    private ProgressBar pbProfile;

    private Calendar mcalendar;

    private int day, month, year;
    TextView tv_DOBProfile, tv_profile;
    ImageView iv_back_profile, iv_profile;
    EditText et_UserNameProfile, et_EmailProfile, et_MobileProfile;
    RadioGroup rg_profile;
    RadioButton rb_Male, rb_Female;
    TextView tv_Pofile;
    private Bitmap bitmap;
    private Uri filePath;

    private LinearLayout llTest;

    int newYear, newMonth;

    private SharedPreferences sharedPreferences;

    private String user_id, gender;
    private String uName, uEmail, uMobileNo, uDob, uGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        sharedPreferences = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);

        tv_profile = findViewById(R.id.tv_profile);
        iv_profile = findViewById(R.id.iv_profile);
        et_UserNameProfile = findViewById(R.id.et_UserNameProfile);
        et_EmailProfile = findViewById(R.id.et_EmailProfile);
        et_MobileProfile = findViewById(R.id.et_MobileProfile);
        tv_DOBProfile = findViewById(R.id.tv_DOBProfile);
        rg_profile = findViewById(R.id.rg_profile);
        rb_Male =findViewById(R.id.rb_Male);
        rb_Female = findViewById(R.id.rb_Female);
        tv_Pofile = findViewById(R.id.tvProfile);
        pbProfile = findViewById(R.id.pbProfile);
        iv_back_profile = findViewById(R.id.iv_back_profile);

        llTest = findViewById(R.id.llTest);

        llTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FragmentTestForCrop.class));
            }
        });

        iv_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onBackPressed();
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for camera permission

                Dexter.withActivity(EditProfile.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        selectImage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(EditProfile.this, "Permissions are required", Toast.LENGTH_SHORT).show();
                        if(response.isPermanentlyDenied()){
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


            }
        });
        tv_Pofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputValidation();
            }
        });
        Calendar mcalendar = Calendar.getInstance();
        tv_DOBProfile.setOnClickListener(mClickListener);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);

        ApiProfileDataGet();

    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
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
        Uri uri = Uri.fromParts("package", Objects.requireNonNull(EditProfile.this).getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                bitmap = (Bitmap) data.getExtras().get("data");
                iv_profile.setImageBitmap(bitmap);
                //saveImage(bitmap);

                Log.e("6598744", bitmap + "");

                sendImage();

            } else if (requestCode == 2) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    Picasso.with(EditProfile.this).load(filePath).
                            resize(52, 52).into(iv_profile);


                    Log.e("25413", bitmap + "");
                    Log.e("589", filePath + "");

                    sendImage();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    private void sendImage() {

        //progressBar.setVisibility(View.VISIBLE);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AlsoFoodCourtApi.UploadProfileImage, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("UPLOADDDD", response + "");

                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        String user_images = object.getString("user_image");
                        Log.e("SSSSSS", user_images + "");
                        Toast.makeText(EditProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("RRRRRRR", response + "");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FFFFFFFF", error + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                Log.e("PPPPPPP", params + "");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                Log.e("PARAMS123", params + "");
                return params;
            }
        };
        Volley.newRequestQueue(EditProfile.this).add(volleyMultipartRequest);

    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PickDobDialog();
        }
    };

    private void PickDobDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                newMonth = monthOfYear + 1;
                newYear = year - 8;

                tv_DOBProfile.setText(dayOfMonth + "/" + newMonth + "/" + year);
                uDob = tv_DOBProfile.getText().toString().trim();
                Log.e("dsadadad", dayOfMonth + "---" + newMonth + "---" + year);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfile.this, listener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void InputValidation() {

        uName = et_UserNameProfile.getText().toString().trim();
        uEmail = et_EmailProfile.getText().toString().trim();
        uMobileNo = et_MobileProfile.getText().toString().trim();
        uDob = tv_DOBProfile.getText().toString().trim();
        int selectedId = rg_profile.getCheckedRadioButtonId();

        if (selectedId == R.id.rb_Female)
            gender = "Female";
        else
            gender = "Male";

        if (TextUtils.isEmpty(uName)) {
            et_UserNameProfile.setError("Enter Name");
            et_UserNameProfile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(uEmail)) {
            et_EmailProfile.setError("Enter Email");
            et_EmailProfile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(uMobileNo)) {
            et_MobileProfile.setError("Enter Mobile Number");
            et_MobileProfile.requestFocus();
            return;
        }
        if (uDob.equals("Enter date of birth")) {
            tv_DOBProfile.setError("Enter DOB");
            Log.e("SDadsadsadsadsa", uDob + "");
            return;
        }
        ApiUpdateProfile();
    }

    private void ApiUpdateProfile() {
        pbProfile.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.EditProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbProfile.setVisibility(View.GONE);
                Log.e("DDDDDDD", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {
                        ApiProfileDataGet();

                        Toast.makeText(EditProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbProfile.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("user_id", user_id);
                params.put("name", uName);
                params.put("email", uEmail);
                params.put("mobile", uMobileNo);
                params.put("birthday", uDob);
                params.put("gender", gender);

                Log.e("FTYFYTFTY", params + "");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);
    }

    private void ApiProfileDataGet() {

        pbProfile.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.GetProfileData, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                pbProfile.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        String User_name = object.getString("name");
                        String User_mobile = object.getString("mobile");
                        String User_email = object.getString("email");
                        String User_gender = object.getString("gender");
                        String User_birthday = object.getString("birthday");
                        String total_wallet_amount = object.getString("total_wallet_amount");
                        String USer_image = object.getString("user_image");

                        Log.e("DDDDDSSSS", response + "");

                        tv_profile.setText(User_name);
                        et_UserNameProfile.setText(User_name);
                        et_MobileProfile.setText(User_mobile);
                        et_EmailProfile.setText(User_email);

                        if (User_birthday.equals("")) {
                            tv_DOBProfile.setText("Enter date of birth");
                            tv_DOBProfile.setTextColor(Objects.requireNonNull(EditProfile.this).getResources().getColor(R.color.black));
                        } else {
                            tv_DOBProfile.setText(User_birthday);
                            tv_DOBProfile.setTextColor(Objects.requireNonNull(EditProfile.this).getResources().getColor(R.color.black));

                        }

                        if(User_gender.equals("")){
                            rb_Male.setChecked(true);
                        }
                        else if(User_gender.equals("Male")){
                            rb_Male.setChecked(true);
                        }
                        else {
                            rb_Female.setChecked(true);
                        }

                        Glide.with(Objects.requireNonNull(EditProfile.this))
                                .load(USer_image).diskCacheStrategy(DiskCacheStrategy.RESOURCE).
                                apply(new RequestOptions().placeholderOf(R.drawable.user).error(R.drawable.user)
                                        .override(192, 192)).into(iv_profile);


                        if (USer_image.equals("")) {
                            Glide.with(EditProfile.this).load(R.drawable.user).into(iv_profile);
                        }

                    } else {
                        Toast.makeText(EditProfile.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                        pbProfile.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    pbProfile.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbProfile.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                Log.e("HYTGTTG", params + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);
    }



}
