package com.lgt.NeWay.activity.UploadOtherCertificate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.VolleyMultipartRequest;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.Addbatches;
import com.lgt.NeWay.activity.ContactList.Adaptercontact;
import com.lgt.NeWay.activity.ContactList.AddContactNumber;
import com.lgt.NeWay.activity.ContactList.ModelContact;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadOtherCertificate extends AppCompatActivity implements DeleteCertificateInterface {
    RecyclerView rv_UploadCertificate;
    List<MOdelUploadCertificate> mlist = new ArrayList<>();
    AdapterUploadCertificate adapterUploadCertificate;
    TextView tvToolbar;
    SharedPreferences sharedPreferences;
    String Mtable_id;
    ProgressBar Pb_Progressbar;
    RelativeLayout rl_Uploadcertificate;
    public AlertDialog download_dialog;
    CircleImageView cv_CertificateImage;
    Spinner sp_Select_Number;
    EditText et_Enter_Title, et_Type_Message;
    Button bt_cancel, bt_submitboard;
    Boolean isImageAvailable = false;
    private Uri filePath;
    int bitmapSize;
    private Bitmap bitmap, converetdImage;
    List<String> stringList = new ArrayList<>();
    String selectedNumber = "";
    public static List<String> statusList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_other_certificate);



        tvToolbar = findViewById(R.id.tvToolbar);
        rv_UploadCertificate = findViewById(R.id.rv_UploadCertificate);
        Pb_Progressbar = findViewById(R.id.Pb_Progressbar);
        rl_Uploadcertificate = findViewById(R.id.rl_Uploadcertificate);
        tvToolbar.setText("Upload Certificate");
        iniSharedpref();
        onClick();

        statusList.clear();
        statusList.add("Active");
        statusList.add("Inactive");
    }

    private void iniSharedpref() {
        sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        Mtable_id = sharedPreferences.getString("tbl_coaching_id", "");
    }

    private void onClick() {

        rl_Uploadcertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diloughopen();
            }
        });


    }

    private void checkPosition(int position) {
        if (position != 0) {
            if (stringList.size() != 0) {
                selectedNumber = stringList.get(position);
                Log.d("selectedNumber", "" + selectedNumber);
            } else {
                Log.d("selectedNumber", "Noting found!");
            }
        }

    }

    private void diloughopen() {
        Pb_Progressbar.setVisibility(View.GONE);

        View customView = LayoutInflater.from(this).inflate(R.layout.diloughuploadcertificate, null);
        download_dialog = new AlertDialog.Builder(this).create();
        download_dialog.setView(customView);
        download_dialog.setCanceledOnTouchOutside(false);

        cv_CertificateImage = customView.findViewById(R.id.cv_CertificateImage);
        sp_Select_Number = customView.findViewById(R.id.sp_Select_Number);
        et_Type_Message = customView.findViewById(R.id.et_Type_Message);
        et_Enter_Title = customView.findViewById(R.id.et_Enter_Title);
        bt_cancel = customView.findViewById(R.id.bt_cancel);
        bt_submitboard = customView.findViewById(R.id.bt_submitboard);

        LoadContactlist();


        cv_CertificateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionForApp();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download_dialog.dismiss();
            }
        });


        bt_submitboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNumber.equals("")) {
                    Log.d("setOnClickListener", "setOnClickListener Nothing");
                } else {
                    Log.d("setOnClickListener", "select Name is : " + selectedNumber);

                    Validation();

                }
            }
        });
        download_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //LoadClassAddedList();
            }
        });

        // tv_current_size_progress = customView.findViewById(R.id.tv_current_size_progress);
        download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        download_dialog.show();
        Pb_Progressbar.setVisibility(View.VISIBLE);

        sp_Select_Number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("gsgsgsgsgsg", position + "position ");
                checkPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Validation() {

        if (TextUtils.isEmpty(et_Enter_Title.getText())) {
            et_Enter_Title.setError("All Field Required");
            et_Enter_Title.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_Type_Message.getText())) {
            et_Type_Message.setError("All Field Required");
            et_Type_Message.requestFocus();
            return;
        }
        if (isImageAvailable.equals(false)) {
            Toast.makeText(UploadOtherCertificate.this, "Please Upload Certificate Image", Toast.LENGTH_SHORT).show();

        }
        if (TextUtils.isEmpty(selectedNumber)) {
            Toast.makeText(UploadOtherCertificate.this, "Select Contact Number", Toast.LENGTH_LONG).show();
            return;
        }
        uploadcertificate();


    }

    private void uploadcertificate() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, NeWayApi.Upload_Certificate, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("vhehdhdh", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(UploadOtherCertificate.this, "" + message, Toast.LENGTH_SHORT).show();

                        download_dialog.dismiss();
                        loadlist();

                    } else {
                        Toast.makeText(UploadOtherCertificate.this, "" + message, Toast.LENGTH_SHORT).show();
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
                parem.put("mobile_number", selectedNumber);
                parem.put("title", et_Enter_Title.getText().toString());
                parem.put("description", et_Type_Message.getText().toString());

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

        Volley.newRequestQueue(UploadOtherCertificate.this).add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void LoadContactlist() {
        stringList.clear();
        stringList.add("Please Choose Contact");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Contact_ListAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("chechechehceh", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String tbl_coaching_user_contact_number_id = object.getString("tbl_coaching_user_contact_number_id");
                                String mobile = object.getString("mobile");
                                String name = object.getString("name");
                                stringList.add(mobile);
                                setSpinnerAdapter(stringList);


                            }
                        }

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
                Log.e("checjjjjjjj", parem + "");
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setSpinnerAdapter(List<String> stringList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Select_Number.setAdapter(adapter);
    }

    private void checkPermissionForApp() {

        try {
            Dexter.withActivity(UploadOtherCertificate.this).withPermissions(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                            if (report.areAllPermissionsGranted()) {
                                selectImage();
                            }

                            if (!report.areAllPermissionsGranted()) {
                                Toast.makeText(UploadOtherCertificate.this, "All permission are required to use this app", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                //Some permission are permanently denied, send user to app setting to allow them manually
                                showSettingDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {

                        }
                    })

                    .withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Toast.makeText(UploadOtherCertificate.this, "Permission Error occurred...", Toast.LENGTH_SHORT).show();
                        }
                    }).onSameThread().check();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSettingDialog() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(UploadOtherCertificate.this);
            builder.setTitle("Need Permission");
            builder.setMessage("This app needs permissions to use it's features");
            builder.setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    openSetting();

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();

                }
            });
        }
    }

    private void openSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                isImageAvailable = true;
                bitmap = (Bitmap) data.getExtras().get("data");
                cv_CertificateImage.setImageBitmap(bitmap);
                Glide.with(UploadOtherCertificate.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(cv_CertificateImage);
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
                bitmap = MediaStore.Images.Media.getBitmap(UploadOtherCertificate.this.getContentResolver(), filePath);
                bitmapSize = bitmap.getAllocationByteCount();
                Glide.with(UploadOtherCertificate.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(cv_CertificateImage);
                converetdImage = getResizedBitmap(bitmap, 400);

                Log.e("oldbitmap", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallery", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallerydasdas", (bitmap.getAllocationByteCount() / 1024) + "");

                Log.e("dasdasddrerer", converetdImage.getAllocationByteCount() + "");
                Log.e("ytytyyuyutyuytuty", (converetdImage.getAllocationByteCount() / 1024) + "");

                if (converetdImage.getAllocationByteCount() > 512000) {

                } else {
                    Glide.with(UploadOtherCertificate.this).load(bitmap).apply(new RequestOptions().override(192, 192)).
                            into(cv_CertificateImage);
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UploadOtherCertificate.this);
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


    private void loadlist() {
        Pb_Progressbar.setVisibility(View.VISIBLE);
        mlist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Uploaded_Certificate_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Pb_Progressbar.setVisibility(View.GONE);
                Log.e("checlllllll", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; jsonArray.length() > i; i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String tbl_upload_other_certificate_id = jsonObject1.getString("tbl_upload_other_certificate_id");
                                String user_id = jsonObject1.getString("user_id");
                                String tbl_coaching_id = jsonObject1.getString("tbl_coaching_id");
                                String username = jsonObject1.getString("username");
                                String mobile_number = jsonObject1.getString("mobile_number");
                                String title = jsonObject1.getString("title");
                                String images = jsonObject1.getString("images");
                                String Certificate_status = jsonObject1.getString("status");

                                mlist.add(new MOdelUploadCertificate(images, username, title, mobile_number, tbl_upload_other_certificate_id,Certificate_status));
                            }
                            adapterUploadCertificate = new AdapterUploadCertificate(mlist, getApplicationContext(), UploadOtherCertificate.this);
                            rv_UploadCertificate.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            rv_UploadCertificate.setHasFixedSize(true);
                            rv_UploadCertificate.setAdapter(adapterUploadCertificate);
                        }
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
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        loadlist();
        super.onResume();
    }

    @Override
    public void deletecertificate(String id) {
        deletecertificateApi(id);

    }

    private void deletecertificateApi(String CertiFi_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Delete_Certificate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        Toast.makeText(UploadOtherCertificate.this, message+"", Toast.LENGTH_SHORT).show();
                        loadlist();
                    }else {
                        Toast.makeText(UploadOtherCertificate.this, message+"", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>param=new HashMap<>();
                param.put("tbl_coaching_id",Mtable_id);
                param.put("tbl_upload_other_certificate_id",CertiFi_id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}