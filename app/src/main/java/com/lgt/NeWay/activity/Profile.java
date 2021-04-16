package com.lgt.NeWay.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.internal.service.Common;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.VolleyMultipartRequest;
import com.lgt.NeWay.Fragment.Model.ModelUserAppliedJob;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.JobList.AddJob;
import com.lgt.NeWay.activity.JobList.Jobs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {


    private TextView tvToolbar;
    private ImageView ivBackFullDescription;
    String tb_cochngid;
    private CircleImageView ivProfileImage;
    private TextView tvNameProfile, tvUpdateProfile;
    EditText etUserNameProfile, etEmailProfile, etMobileProfile, etDOBProfile;
    Boolean isImageAvailable =false;
    private RadioGroup radioGroupGender;
    private RadioButton radioBtnMale, radioBtnFemale;
    private LinearLayout ll_editprofile;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ProgressBar pbProfile;

    private String mUserID, mEmail;
    private Common common;

    private String mUserName, mDateOfBirth, mGender;

    private Calendar myCalendar;
    private Uri filePath;
    private Bitmap bitmap, converetdImage;
    int bitmapSize;

    private static final String TAG = "Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iniSharedprefrense();


        loadprofiledata(tb_cochngid);


        tvToolbar = findViewById(R.id.tvToolbar);
        ivBackFullDescription = findViewById(R.id.ivBackFullDescription);
        pbProfile = findViewById(R.id.pbProfile);
        ll_editprofile = findViewById(R.id.ll_editprofile);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvNameProfile = findViewById(R.id.tvNameProfile);
        etUserNameProfile = findViewById(R.id.etUserNameProfile);
        etEmailProfile = findViewById(R.id.etEmailProfile);
        etMobileProfile = findViewById(R.id.etMobileProfile);
        etDOBProfile = findViewById(R.id.etDOBProfile);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioBtnMale = findViewById(R.id.radioBtnMale);
        radioBtnFemale = findViewById(R.id.radioBtnFemale);

        tvUpdateProfile = findViewById(R.id.tvUpdateProfile);

        tvToolbar.setText("My Profile");

        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }

        if (sharedPreferences.contains("KEY_EMAIL")) {
            mEmail = sharedPreferences.getString("KEY_EMAIL", "");
            etEmailProfile.setText(mEmail);
        }

        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                hideKeyBoard();
            }
        });


        myCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }

        };

        etDOBProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Profile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateField();
            }
        });



        ll_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ask for permission
                //After permission pick image and send to server

                askForPermission();
            }
        });


    }

    private void iniSharedprefrense() {
        sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        tb_cochngid = sharedPreferences.getString("tbl_coaching_id", "");
        editor = sharedPreferences.edit();
    }

    private void loadprofiledata(String tb_cochngid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checcccccccccclllll", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    String coaching_name = jsonObject.getString("coaching_name");
                    String contact_no = jsonObject.getString("contact_no");
                    String email_id = jsonObject.getString("email_id");
                    String image = jsonObject.getString("image");

                    etUserNameProfile.setText(coaching_name);
                    etMobileProfile.setText(contact_no);
                    etEmailProfile.setText(email_id);

                    Glide.with(Profile.this).load(image).apply(new RequestOptions()
                            .override(192, 192)).into(ivProfileImage);

                    Toast.makeText(Profile.this, message + "", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_coaching_id", tb_cochngid);
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void validateField() {

        // mDateOfBirth = etDOBProfile.getText().toString().trim();
        mUserName = etUserNameProfile.getText().toString().trim();

        if (TextUtils.isEmpty(mUserName)) {
            etUserNameProfile.setError("Enter your name");
            etUserNameProfile.requestFocus();
            return;

        }
        if (isImageAvailable.equals(false)) {
            Toast.makeText(Profile.this, "Please Upload Coaching Image", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Profile Updated", Toast.LENGTH_LONG).show();
        onBackPressed();


         updateProfileData();


    }

    private void updateProfileData() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, NeWayApi.Edit_Profile, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("vhehdhdh", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(Profile.this, "" + message, Toast.LENGTH_SHORT).show();
                       onBackPressed();


                    } else {
                        Toast.makeText(Profile.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();

                parem.put("tbl_coaching_id", tb_cochngid);
                parem.put("coaching_name", etUserNameProfile.getText().toString());
                Log.e("PPPPPPP", parem + "");
                return parem;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(converetdImage)));
                Log.e("PARAMS", params + "");
                return params;
            }
        };

        Volley.newRequestQueue(Profile.this).add(volleyMultipartRequest);

    }
    private byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void askForPermission() {

        Log.e("klskdlsakdsal", "askForPermission: ");
        Dexter.withActivity(Profile.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        Log.e(TAG, "onPermissionsChecked:dsada " + report + "");
                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(Profile.this, "Please allow all permissions from setting", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        } else {
                            Toast.makeText(Profile.this, "All permissions are required", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).onSameThread().check();

    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
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
        Uri uri = Uri.fromParts("package", (getPackageName()), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
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

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                isImageAvailable=true;
                bitmap = (Bitmap) data.getExtras().get("data");
                ivProfileImage.setImageBitmap(bitmap);
                Glide.with(Profile.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(ivProfileImage);
                converetdImage = getResizedBitmap(bitmap, 400);


                //converetdImage = getResizedBitmap(bitmap, 400);
                Log.e("CLICKEDINBYTES", bitmap.getAllocationByteCount() + "");
                Log.e("CLICKEDINKILLOBYTE", (bitmap.getAllocationByteCount() / 1024) + "");

            }

        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Log.e("gjhghhhhhhk", "called");

            filePath = data.getData();
            Log.e("filepaththh", filePath + "");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(Profile.this.getContentResolver(), filePath);
                bitmapSize = bitmap.getAllocationByteCount();
                Glide.with(Profile.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(ivProfileImage);
                converetdImage = getResizedBitmap(bitmap, 400);

                Log.e("oldbitmap", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallery", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallerydasdas", (bitmap.getAllocationByteCount() / 1024) + "");

                Log.e("dasdasddrerer", converetdImage.getAllocationByteCount() + "");
                Log.e("ytytyyuyutyuytuty", (converetdImage.getAllocationByteCount() / 1024) + "");

                if (converetdImage.getAllocationByteCount() > 512000) {

                } else {
                    Glide.with(Profile.this).load(bitmap).apply(new RequestOptions().override(192, 192)).
                            into(ivProfileImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
