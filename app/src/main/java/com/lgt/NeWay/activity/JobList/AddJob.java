package com.lgt.NeWay.activity.JobList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.lgt.NeWay.Extra.VolleyMultipartRequest;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lgt.NeWay.Extra.NeWayApi.Board_List;
import static com.lgt.NeWay.Extra.NeWayApi.Class_ListApi;
import static com.lgt.NeWay.Extra.NeWayApi.Subject_ListApi;

public class AddJob extends AppCompatActivity {

    EditText et_JobTitle_name, et_Special_Courses, et_Working_Days, et_Total_No_of_Job;
    ImageView iv_uploadbatchimage;
    String selectedNameBoard = "";
    String selectedNameSubject = "";
    String selectedNameClass = "";
    String Mtable_id;
    Button btn_submit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Spinner sp_Select_Board, sp_Select_Subject, sp_Select_class;
    ArrayList<String> mSelect_Board = new ArrayList<>();
    ArrayList<String> mSelect_Subject = new ArrayList<>();
    ArrayList<String> mSelect_class = new ArrayList<>();
    private static final String TAG = "AddJobActivity";
    private Bitmap bitmap, converetdImage;
    Boolean isImageAvailable =false;
    private Uri filePath;
    int bitmapSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        iniview();
        iniOnclick();
        iniSharedp();
        // set data
        getDataFromServer(Board_List, sp_Select_Board, mSelect_Board, "one");
        getDataFromServer(Subject_ListApi, sp_Select_Subject, mSelect_Subject, "two");
        getDataFromServer(Class_ListApi, sp_Select_class, mSelect_class, "three");


    }

    private void iniSharedp() {
        sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        Mtable_id = sharedPreferences.getString("tbl_coaching_id", "");
    }

    private void iniview() {
        btn_submit = findViewById(R.id.btn_submit);
        et_JobTitle_name = findViewById(R.id.et_JobTitle_name);
        sp_Select_Board = findViewById(R.id.sp_Select_Board);
        sp_Select_Subject = findViewById(R.id.sp_Select_Subject);
        sp_Select_class = findViewById(R.id.sp_Select_class);
        et_Special_Courses = findViewById(R.id.et_Special_Courses);
        et_Working_Days = findViewById(R.id.et_Working_Days);
        et_Total_No_of_Job = findViewById(R.id.et_Total_No_of_Job);
        iv_uploadbatchimage = findViewById(R.id.iv_uploadbatchimage);

        sp_Select_Board.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("gsgsgsgsgsg", position + "position ");
                if (position == 0) {

                } else {
                    checkPosition(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_Select_Subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("gsgsgsgsgsg", position + "position ");
                if (position == 0) {

                } else {
                    checkPosition2(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_Select_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("gsgsgsgsgsg", position + "position ");
                if (position == 0) {

                } else {
                    checkPosition3(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void iniOnclick() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation();

            }

            private void Validation() {
                if (TextUtils.isEmpty(et_JobTitle_name.getText().toString())) {
                    et_JobTitle_name.setError("Fill Job Title");
                    et_JobTitle_name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(et_Special_Courses.getText().toString())) {
                    et_Special_Courses.setError("Fill Special Courses");
                    et_Special_Courses.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(et_Total_No_of_Job.getText().toString())) {
                    et_Total_No_of_Job.setError("Fill Number Of Job");
                    et_Total_No_of_Job.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(et_Working_Days.getText().toString())) {
                    et_Working_Days.setError("Fill Working Days");
                    et_Working_Days.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(selectedNameBoard)) {
                    Toast.makeText(AddJob.this, "selected Board Name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(selectedNameClass)) {
                    Toast.makeText(AddJob.this, "selected Class Name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(selectedNameSubject)) {
                    Toast.makeText(AddJob.this, "selected Subject Name", Toast.LENGTH_LONG).show();
                    return;
                }

                if (isImageAvailable.equals(false)) {
                    Toast.makeText(AddJob.this, "Please Upload Coaching Image", Toast.LENGTH_LONG).show();
                    return;
                }
                sendImage();
            }
        });

        iv_uploadbatchimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpermission();
            }
        });
    }

    private void checkpermission() {

        Log.e("klskdlsakdsal", "askForPermission: ");
        Dexter.withActivity(AddJob.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        Log.e(TAG, "onPermissionsChecked:dsada " + report + "");
                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(AddJob.this, "Please allow all permissions from setting", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        } else {
                            Toast.makeText(AddJob.this, "All permissions are required", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).onSameThread().check();

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddJob.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddJob.this);
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
                isImageAvailable=true;
                bitmap = (Bitmap) data.getExtras().get("data");
                iv_uploadbatchimage.setImageBitmap(bitmap);
                Glide.with(AddJob.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(iv_uploadbatchimage);
                converetdImage = getResizedBitmap(bitmap, 400);


                //converetdImage = getResizedBitmap(bitmap, 400);
                Log.e("CLICKEDINBYTES", bitmap.getAllocationByteCount() + "");
                Log.e("CLICKEDINKILLOBYTE", (bitmap.getAllocationByteCount() / 1024) + "");

            }

        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Log.e("gjhghhhhhhk", "called");
            isImageAvailable=true;
            filePath = data.getData();
            Log.e("filepaththh", filePath + "");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(AddJob.this.getContentResolver(), filePath);
                bitmapSize = bitmap.getAllocationByteCount();

                converetdImage = getResizedBitmap(bitmap, 400);

                Log.e("oldbitmap", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallery", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallerydasdas", (bitmap.getAllocationByteCount() / 1024) + "");
                Log.e("dasdasddrerer", converetdImage.getAllocationByteCount() + "");
                Log.e("ytytyyuyutyuytuty", (converetdImage.getAllocationByteCount() / 1024) + "");

                if (converetdImage.getAllocationByteCount() > 512000) {

                } else {
                    Glide.with(AddJob.this).load(bitmap).apply(new RequestOptions().override(192, 192)).
                            into(iv_uploadbatchimage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendImage() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, NeWayApi.Add_Job_List, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("vhehdhdh", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(AddJob.this, "" + message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Jobs.class));


                    } else {
                        Toast.makeText(AddJob.this, "" + message, Toast.LENGTH_SHORT).show();
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
                parem.put("user_id", Mtable_id);
                parem.put("job_title", et_JobTitle_name.getText().toString());
                parem.put("tbl_coaching_id", Mtable_id);
                parem.put("board_name", selectedNameBoard);
                parem.put("subject_name", selectedNameSubject);
                parem.put("class_name", selectedNameClass);
                parem.put("special_courses", et_Special_Courses.getText().toString());
                parem.put("working_days", et_Working_Days.getText().toString());
                parem.put("total_job", et_Total_No_of_Job.getText().toString());
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

        Volley.newRequestQueue(AddJob.this).add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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

    private void checkPosition(int position) {
        Log.d("selectedName", "mSelect_Board" + mSelect_Board.size());

        if (mSelect_Board.size() != 0) {

            selectedNameBoard = mSelect_Board.get(position - 1);
            Log.d("selectedName", "" + selectedNameBoard);


        } else {
            Log.d("selectedName", "Noting found!");
        }
    }

    private void checkPosition2(int position) {
        if (mSelect_Subject.size() != 0) {
            selectedNameSubject = mSelect_Subject.get(position - 1);
            Log.d("selectedName", "" + selectedNameSubject);
        } else {
            Log.d("selectedName", "Noting found!");
        }
    }
    private void checkPosition3(int position) {
        if (mSelect_class.size() != 0) {
            selectedNameClass = mSelect_class.get(position - 1);
            Log.d("selectedName", "" + selectedNameClass);
        } else {
            Log.d("selectedName", "Noting found!");
        }
    }

    // 8604111232 - pass 9883
    private void getDataFromServer(String url, Spinner spinner, ArrayList<String> mList, String type) {
        mList = new ArrayList<>();
        mList.clear();
        ArrayList<String> finalMList = mList;
        mList.add("Please select Option");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onresponsecheck", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; jsonArray.length() > i; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("name");
                            Log.e("JSONObject", name + "");
                            if (type.equals("one")) {
                                mSelect_Board.add(name);
                                finalMList.add(name);
                            } else if (type.equals("two")) {
                                mSelect_Subject.add(name);
                                finalMList.add(name);
                            } else if (type.equals("three")) {
                                mSelect_class.add(name);
                                finalMList.add(name);
                            }
                            Log.e("size: ", finalMList.size() + "");
                        }
                        setAdapterStatus(spinner, finalMList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void setAdapterStatus(Spinner sp_statuspending, ArrayList list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_statuspending.setAdapter(adapter);// set the selected value of the spinner

    }
}