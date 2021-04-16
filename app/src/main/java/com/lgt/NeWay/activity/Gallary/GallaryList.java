package com.lgt.NeWay.activity.Gallary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.VolleyMultipartRequest;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.ContactList.DeleteContactInterface;
import com.lgt.NeWay.activity.TeacherList.TeacherList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GallaryList extends AppCompatActivity implements DeleteGalleryImageInterface {
    List<ModelGallary> mlist = new ArrayList<>();
    AdapterGallary adapterGallary;
    RecyclerView rv_ImageList;
    private static final String TAG = "TeacherList";
    RelativeLayout rl_AddIamgeContainer;
    Boolean isImageAvailable = false;
    private Bitmap bitmap, converetdImage;
    private Uri filePath;
    int bitmapSize;
    ImageView iv_uploadimage,ivBackFullDescription;
    TextView tvUploadImage;
    SharedPreferences sharedPreferences;
    String Mtable_id;
    ProgressBar progress_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary_list);


        inisharedpref();
        iniView();
        onclick();
        Loadgallarylist();
    }

    private void inisharedpref() {
        sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        Mtable_id = sharedPreferences.getString("tbl_coaching_id", "");
    }


    private void iniView() {
        rv_ImageList = findViewById(R.id.rv_ImageList);
        rl_AddIamgeContainer = findViewById(R.id.rl_AddIamgeContainer);
        iv_uploadimage = findViewById(R.id.iv_uploadimage);
        tvUploadImage = findViewById(R.id.tvUploadImage);
        progress_signup = findViewById(R.id.progress_signup);
        ivBackFullDescription = findViewById(R.id.ivBackFullDescription);
    }


    private void onclick() {
        iv_uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission();
            }
        });

        tvUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation();

            }
        });
        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
               finish();
            }
        });

    }

    private void Validation() {

        if (isImageAvailable.equals(false)) {
            Toast.makeText(GallaryList.this, "Please Upload Image", Toast.LENGTH_SHORT).show();
        } else {
            sendImage();

        }
    }

    private void sendImage() {
        progress_signup.setVisibility(View.VISIBLE);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, NeWayApi.AddGallery_image_API, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("vhehdhdh", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        //String status = jsonObject.getString("status");
                        progress_signup.setVisibility(View.GONE);
                        iv_uploadimage.setImageResource(R.drawable.galleryicon);
                        Toast.makeText(GallaryList.this, "" + message, Toast.LENGTH_LONG).show();
                        Loadgallarylist();

                    } else {
                        Toast.makeText(GallaryList.this, "" + message, Toast.LENGTH_LONG).show();
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

                parem.put("tbl_coaching_id", Mtable_id);
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

        Volley.newRequestQueue(GallaryList.this).add(volleyMultipartRequest);
    }

  /*  @Override
    protected void onResume() {
        mlist.clear();
        Loadgallarylist();
        super.onResume();
    }*/

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void askForPermission() {

        Log.e("klskdlsakdsal", "askForPermission: ");
        Dexter.withActivity(GallaryList.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        Log.e(TAG, "onPermissionsChecked:dsada " + report + "");
                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(GallaryList.this, "Please allow all permissions from setting", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        } else {
                            Toast.makeText(GallaryList.this, "All permissions are required", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).onSameThread().check();

    }

    private void showSettingsDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GallaryList.this);
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GallaryList.this);
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
                isImageAvailable = true;
                bitmap = (Bitmap) data.getExtras().get("data");
                iv_uploadimage.setImageBitmap(bitmap);
                Glide.with(GallaryList.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(iv_uploadimage);
                converetdImage = getResizedBitmap(bitmap, 400);


                //converetdImage = getResizedBitmap(bitmap, 400);
                Log.e("CLICKEDINBYTES", bitmap.getAllocationByteCount() + "");
                Log.e("CLICKEDINKILLOBYTE", (bitmap.getAllocationByteCount() / 1024) + "");

            }

        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            isImageAvailable = true;
            Log.e("gjhghhhhhhk", "called");

            filePath = data.getData();
            Log.e("filepaththh", filePath + "");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(GallaryList.this.getContentResolver(), filePath);
                bitmapSize = bitmap.getAllocationByteCount();
                Glide.with(GallaryList.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(iv_uploadimage);
                converetdImage = getResizedBitmap(bitmap, 400);

                Log.e("oldbitmap", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallery", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallerydasdas", (bitmap.getAllocationByteCount() / 1024) + "");

                Log.e("dasdasddrerer", converetdImage.getAllocationByteCount() + "");
                Log.e("ytytyyuyutyuytuty", (converetdImage.getAllocationByteCount() / 1024) + "");

                if (converetdImage.getAllocationByteCount() > 512000) {

                } else {
                    Glide.with(GallaryList.this).load(bitmap).apply(new RequestOptions().override(192, 192)).
                            into(iv_uploadimage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

    private void Loadgallarylist() {
        progress_signup.setVisibility(View.VISIBLE);
        mlist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.AddedGallery_image_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        progress_signup.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; jsonArray.length() > i; i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String tbl_gallery_id = jsonObject1.getString("tbl_gallery_id");
                                String image = jsonObject1.getString("image");
                                Log.e("cehcheggggg", image + "");

                                mlist.add(new ModelGallary(image, tbl_gallery_id));
                                Toast.makeText(GallaryList.this, message + "", Toast.LENGTH_SHORT).show();

                            }

                            adapterGallary = new AdapterGallary(mlist, getApplicationContext(), GallaryList.this);
                            rv_ImageList.setAdapter(adapterGallary);
                            rv_ImageList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.VERTICAL, false));
                            rv_ImageList.setHasFixedSize(true);
                            adapterGallary.notifyDataSetChanged();

                        }
                    } else {
                        Toast.makeText(GallaryList.this, message + "", Toast.LENGTH_SHORT).show();
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
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_coaching_id", Mtable_id);
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void deleteImage(String id) {
        deletegallaryimage(id);

    }

    private void deletegallaryimage(String G_id) {
        progress_signup.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.DeleteGallery_image_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ffffffffffff", response + "");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        progress_signup.setVisibility(View.GONE);
                        adapterGallary.notifyDataSetChanged();
                        Toast.makeText(GallaryList.this, message + "", Toast.LENGTH_LONG).show();
                        Loadgallarylist();
                    } else {
                        Toast.makeText(GallaryList.this, message + "", Toast.LENGTH_LONG).show();
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
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("tbl_coaching_id", Mtable_id);
                param.put("tbl_gallery_id", G_id);
                Log.e("asasasasa", param + "");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}