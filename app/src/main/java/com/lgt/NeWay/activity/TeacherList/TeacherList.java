
package com.lgt.NeWay.activity.TeacherList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lgt.NeWay.activity.ClassList.ModelClass;
import com.lgt.NeWay.activity.JobList.Jobs;
import com.lgt.NeWay.activity.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherList extends AppCompatActivity implements UpdateTeacherListInterFace {
    AdapterTteacher adapterTteacher;
    List<ModelTeacher> mlist = new ArrayList<>();
    RelativeLayout rl_AddTeacherContainer;
    private Dialog download_dialog;
    RecyclerView rv_TeacherList;
    EditText et_Teacher_Email, et_Teacher_Contact_No, et_Teacher_Qualification, et_Teacher_Name;
    Button bt_cancel, bt_submitboard;
    public String selectedName;
    Boolean isImageAvailable = false;
    private Bitmap bitmap, converetdImage;
    CircleImageView cv_TeacherImage;
    private static final String TAG = "TeacherList";
    private Uri filePath;
    SharedPreferences sharedPreferences;
    int bitmapSize;
    Context context;
    private TeacherList actvity;
    String Mtableid;
    TextView tvToolbar;
    public static List<String> JstatusList = new ArrayList<>();
   ImageView ivBackFullDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        LoadTeacherAddedList();
        inisharedp();
        iniViews();
        inionclick();


        context = actvity = this;
        JstatusList.clear();
        JstatusList.add("Active");
        JstatusList.add("Inactive");
    }

    private void inisharedp() {
        sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        Mtableid = sharedPreferences.getString("tbl_coaching_id", "");
    }


    private void iniViews() {
        rl_AddTeacherContainer = findViewById(R.id.rl_AddTeacherContainer);
        rv_TeacherList = findViewById(R.id.rv_TeacherList);
        ivBackFullDescription = findViewById(R.id.ivBackFullDescription);
        tvToolbar = findViewById(R.id.tvToolbar);

        tvToolbar.setText("Teacher List");

    }

    private void inionclick() {
        rl_AddTeacherContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDilough();
            }
        });
        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
    }

    private void openDilough() {
        View customView = LayoutInflater.from(this).inflate(R.layout.addteacherdilough, null);
        download_dialog = new AlertDialog.Builder(this).create();

        ((AlertDialog) download_dialog).setView(customView);
        download_dialog.setCanceledOnTouchOutside(false);


        et_Teacher_Name = customView.findViewById(R.id.et_Teacher_Name);
        et_Teacher_Name.setCursorVisible(true);
        et_Teacher_Name.requestFocus();
        et_Teacher_Qualification = customView.findViewById(R.id.et_Teacher_Qualification);
        et_Teacher_Contact_No = customView.findViewById(R.id.et_Teacher_Contact_No);
        et_Teacher_Email = customView.findViewById(R.id.et_Teacher_Email);
        bt_cancel = customView.findViewById(R.id.bt_cancel);
        bt_submitboard = customView.findViewById(R.id.bt_submitboard);
        cv_TeacherImage = customView.findViewById(R.id.cv_TeacherImage);

        download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        download_dialog.show();

        //loadclasses();

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download_dialog.dismiss();
            }
        });

        cv_TeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission();
            }
        });

        bt_submitboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation();


            }
        });


        //progressbar.setVisibility(View.VISIBLE);

    }

    private void Validation() {
        if (TextUtils.isEmpty(et_Teacher_Name.getText())) {
            et_Teacher_Name.setError("All Field Required");
            et_Teacher_Name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_Teacher_Contact_No.getText())) {
            et_Teacher_Contact_No.setError("All Field Required");
            et_Teacher_Contact_No.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_Teacher_Email.getText())) {
            et_Teacher_Email.setError("All Field Required");
            et_Teacher_Email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_Teacher_Qualification.getText())) {
            et_Teacher_Qualification.setError("All Field Required");
            et_Teacher_Qualification.requestFocus();
            return;
        }

        if (isImageAvailable.equals(false)) {
            Toast.makeText(TeacherList.this, "Please Upload Teacher Image", Toast.LENGTH_SHORT).show();
        }else {
            AddTeacher();
        }


    }

    private void LoadTeacherAddedList() {
        mlist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Added_TeacherList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checcccccccccc", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; jsonArray.length() > i; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String tbl_teacher_id = object.getString("tbl_teacher_id");
                                String tbl_coaching_id = object.getString("tbl_coaching_id");
                                String name = object.getString("name");
                                String qualification = object.getString("qualification");
                                String contact_no = object.getString("contact_no");
                                String email = object.getString("email");
                                String TeacherStatus = object.getString("status");
                                String image = object.getString("image");


                                mlist.add(new ModelTeacher(name, qualification, contact_no, email, TeacherStatus, tbl_teacher_id, image));


                            }
                            adapterTteacher = new AdapterTteacher(mlist, getApplicationContext(), TeacherList.this);
                            rv_TeacherList.setAdapter(adapterTteacher);
                            rv_TeacherList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            rv_TeacherList.setHasFixedSize(true);
                            adapterTteacher.notifyDataSetChanged();

                        }


                    } else {
                        Toast.makeText(TeacherList.this, message + "", Toast.LENGTH_LONG).show();
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
                parem.put("tbl_coaching_id", Mtableid);
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    private void askForPermission() {

        Log.e("klskdlsakdsal", "askForPermission: ");
        Dexter.withActivity(TeacherList.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        Log.e(TAG, "onPermissionsChecked:dsada " + report + "");
                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(TeacherList.this, "Please allow all permissions from setting", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        } else {
                            Toast.makeText(TeacherList.this, "All permissions are required", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).onSameThread().check();

    }

    private void showSettingsDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TeacherList.this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                isImageAvailable = true;
                bitmap = (Bitmap) data.getExtras().get("data");
                cv_TeacherImage.setImageBitmap(bitmap);
                Glide.with(TeacherList.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(cv_TeacherImage);
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
                bitmap = MediaStore.Images.Media.getBitmap(TeacherList.this.getContentResolver(), filePath);
                bitmapSize = bitmap.getAllocationByteCount();
                Glide.with(TeacherList.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(cv_TeacherImage);
                converetdImage = getResizedBitmap(bitmap, 400);

                Log.e("oldbitmap", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallery", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallerydasdas", (bitmap.getAllocationByteCount() / 1024) + "");

                Log.e("dasdasddrerer", converetdImage.getAllocationByteCount() + "");
                Log.e("ytytyyuyutyuytuty", (converetdImage.getAllocationByteCount() / 1024) + "");

                if (converetdImage.getAllocationByteCount() > 512000) {

                } else {
                    Glide.with(TeacherList.this).load(bitmap).apply(new RequestOptions().override(192, 192)).
                            into(cv_TeacherImage);
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

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TeacherList.this);
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

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void AddTeacher() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, NeWayApi.Add_Teacher, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("vhehdhdh", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(TeacherList.this, "" + message, Toast.LENGTH_SHORT).show();
                        download_dialog.dismiss();
                        LoadTeacherAddedList();
                        Toast.makeText(TeacherList.this, "" + message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(TeacherList.this, "" + message, Toast.LENGTH_SHORT).show();
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

                parem.put("tbl_coaching_id", Mtableid);
                parem.put("contact_no", et_Teacher_Contact_No.getText().toString());
                parem.put("teacher_name", et_Teacher_Name.getText().toString());
                parem.put("qualification", et_Teacher_Qualification.getText().toString());
                parem.put("email", et_Teacher_Email.getText().toString());
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

        Volley.newRequestQueue(TeacherList.this).add(volleyMultipartRequest);
    }

    @Override
    public void updateList(String id) {
        deleteTeacherName(id);

    }


    private void deleteTeacherName(String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Delete_TeacherList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {
                        Toast.makeText(getApplicationContext(), message + "", Toast.LENGTH_SHORT).show();
                        LoadTeacherAddedList();
                        adapterTteacher.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), message + "", Toast.LENGTH_SHORT).show();
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
                param.put("tbl_teacher_id", id);
                param.put("tbl_coaching_id", Mtableid);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}