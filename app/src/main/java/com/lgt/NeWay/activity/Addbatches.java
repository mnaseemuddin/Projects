
package com.lgt.NeWay.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
import android.webkit.PermissionRequest;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.lgt.NeWay.activity.BoardList.BaordList;
import com.lgt.NeWay.activity.JobList.AddJob;
import com.lgt.NeWay.activity.SubjectList.SubjectList;
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

import static com.lgt.NeWay.Extra.NeWayApi.Added_TeacherList;
import static com.lgt.NeWay.Extra.NeWayApi.Batch_Type;
import static com.lgt.NeWay.Extra.NeWayApi.Board_List;
import static com.lgt.NeWay.Extra.NeWayApi.Class_ListApi;
import static com.lgt.NeWay.Extra.NeWayApi.Subject_ListApi;

public class Addbatches extends AppCompatActivity {
    private Bitmap bitmap, converetdImage;
    EditText et_Batch_name, et_ClassFee_signup, et_Duration, et_timing, et_Open_Days, et_TotalSeatAvailable, et_TotalLeftSeat;
    String selectedNameBoard = "";
    String selectedNameSubject = "";
    String selectedNameClass = "";
    String selectedNameTeacher = "";
    String selectedNameId = "";
    String selectedBatchType = "";
    RadioGroup Rg_button;
    RadioButton bt_yes, bt_no;
    private Button btn_submit;
    ArrayList<String> mTracherId = new ArrayList<>();
    SharedPreferences sharedPreferences;
    TextView tvToolbar;
    ImageView iv_uploadbatchimage;
    Boolean isImageAvailable = false;
    private Uri filePath;
    int bitmapSize;
    Spinner sp_batch_Type, sp_Select_Board, sp_Select_Subject, sp_Select_Class, sp_Select_Teacher;
    String selectedName = "", Mtable_id, Type;
    List<String> stringList = new ArrayList<>();
    ArrayList<String> mSelect_Board = new ArrayList<>();
    ArrayList<String> mSelect_Subject = new ArrayList<>();
    ArrayList<String> mSelect_class = new ArrayList<>();
    ArrayList<String> mSelect_teacher = new ArrayList<>();
    ArrayList<String> mBatch_type = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbatches);
        mTracherId.clear();
        iniviews();
        iniSharedpref();
        onitemselectListener();
        onclickListner();

        //for get Api Data
        getDataFromServer(Board_List, sp_Select_Board, mSelect_Board, "one");
        getDataFromServer(Subject_ListApi, sp_Select_Subject, mSelect_Subject, "two");
        getDataFromServer(Class_ListApi, sp_Select_Class, mSelect_class, "three");
        //for post Api Data
        getpostdatafromserver(Batch_Type, sp_batch_Type, mBatch_type, "one");
        getpostdatafromserver(Added_TeacherList, sp_Select_Teacher, mSelect_teacher, "two");
    }

    private void iniSharedpref() {
        sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        Mtable_id = sharedPreferences.getString("tbl_coaching_id", "");
    }

    private void getpostdatafromserver(String url, Spinner spinner, ArrayList<String> mList, String type) {
        mList = new ArrayList<>();
        mList.clear();
        ArrayList<String> finalMList = mList;
        mList.add("Please select Option");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("gffgffffffffff", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; jsonArray.length() > i; i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (type.equals("one")) {
                                    String title = jsonObject1.getString("title");
                                    mBatch_type.add(title);
                                    finalMList.add(title);
                                } else {
                                    if (type.equals("two")) {
                                        String tbl_teacher_id = jsonObject1.getString("tbl_teacher_id");
                                        String name = jsonObject1.getString("name");
                                        mTracherId.add(tbl_teacher_id);
                                        mSelect_teacher.add(name);
                                        finalMList.add(name);

                                    }
                                }
                                setAdapterStatus(spinner, finalMList);

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
                Map<String, String> param = new HashMap<>();
                param.put("tbl_coaching_id", Mtable_id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

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
                    if (status.equals("1")) {
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
                                } else if (type.equals("four")) {
                                    mSelect_teacher.add(name);
                                    finalMList.add(name);
                                }
                                Log.e("size: ", finalMList.size() + "");
                            }
                            setAdapterStatus(spinner, finalMList);
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
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void onitemselectListener() {
        sp_batch_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkPosition1(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_Select_Board.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkPosition2(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_Select_Subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkPosition3(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_Select_Class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkPosition4(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_Select_Teacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkPosition5(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void checkPosition5(int position) {
        if (position != 0) {
            if (mSelect_teacher.size() != 0) {
                selectedNameTeacher = mSelect_teacher.get(position -1);
                selectedNameId = mTracherId.get(position -1);
                Log.d("selectedName", "" + selectedNameTeacher);
            } else {
                Log.d("selectedName", "Noting found!");
            }
        }

    }

    private void checkPosition4(int position) {
        if (position != 0) {
            if (mSelect_class.size() != 0) {
                selectedNameClass = mSelect_class.get(position - 1);
                Log.d("selectedName", "" + selectedNameClass);
            } else {
                Log.d("selectedName", "Noting found!");
            }
        }

    }

    private void checkPosition3(int position) {
        if (position != 0) {
            if (mSelect_Subject.size() != 0) {
                selectedNameSubject = mSelect_Subject.get(position - 1);
                Log.d("selectedName", "" + selectedNameSubject);
            } else {
                Log.d("selectedName", "Noting found!");
            }
        }
    }

    private void checkPosition2(int position) {
        if (position != 0) {
            if (mSelect_Board.size() != 0) {

                selectedNameBoard = mSelect_Board.get(position - 1);
                Log.d("selectedName", "" + selectedNameBoard);
            } else {
                Log.d("selectedName", "Noting found!");
            }
        }
    }

    private void checkPosition1(int position) {
        if (position != 0) {
            if (mBatch_type.size() != 0) {
                selectedBatchType = mBatch_type.get(position - 1);
                Log.d("selectedName", "" + selectedBatchType);
            } else {
                Log.d("selectedName", "Noting found!");
            }
        }
    }

    private void iniviews() {

        iv_uploadbatchimage = findViewById(R.id.iv_uploadbatchimage);
        btn_submit = findViewById(R.id.btn_submit);
        sp_batch_Type = findViewById(R.id.sp_batch_Type);
        sp_Select_Board = findViewById(R.id.sp_Select_Board);
        sp_Select_Subject = findViewById(R.id.sp_Select_Subject);
        sp_Select_Class = findViewById(R.id.sp_Select_Class);
        sp_Select_Teacher = findViewById(R.id.sp_Select_Teacher);
        tvToolbar = findViewById(R.id.tvToolbar);
        Rg_button = findViewById(R.id.Rg_button);
        et_Batch_name = findViewById(R.id.et_Batch_name);
        et_ClassFee_signup = findViewById(R.id.et_ClassFee_signup);
        et_Duration = findViewById(R.id.et_Duration);
        et_timing = findViewById(R.id.et_timing);
        et_Open_Days = findViewById(R.id.et_Open_Days);
        et_TotalSeatAvailable = findViewById(R.id.et_TotalSeatAvailable);
        et_TotalLeftSeat = findViewById(R.id.et_TotalLeftSeat);
        bt_yes = findViewById(R.id.bt_yes);
        bt_no = findViewById(R.id.bt_no);
        tvToolbar.setText("Add Batch");
    }

    private void onclickListner() {
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_yes.isChecked()) {
                    Type = "yes";
                    Log.e("checkradiobuttonclick", Type + "");


                    if (bt_no.isChecked()) {
                        bt_no.setChecked(false);
                    }
                }
            }
        });
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_no.isChecked()) {
                    Type = "No";
                    Log.e("checkradiobuttonclick", Type + "");

                    if (bt_yes.isChecked()) {
                        bt_yes.setChecked(false);
                    }
                }
            }
        });
        iv_uploadbatchimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionForApp();
            }

        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
    }

    private void Validation() {

        if (TextUtils.isEmpty(et_Batch_name.getText())) {
            et_Batch_name.setError("All Field Required");
            et_Batch_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_ClassFee_signup.getText())) {
            et_ClassFee_signup.setError("All Field Required");
            et_ClassFee_signup.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_Duration.getText())) {
            et_Duration.setError("All Field Required");
            et_Duration.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_timing.getText())) {
            et_timing.setError("All Field Required");
            et_timing.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_Open_Days.getText())) {
            et_Open_Days.setError("All Field Required");
            et_Open_Days.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_TotalSeatAvailable.getText())) {
            et_TotalSeatAvailable.setError("All Field Required");
            et_TotalSeatAvailable.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_TotalLeftSeat.getText())) {
            et_TotalLeftSeat.setError("All Field Required");
            et_TotalLeftSeat.requestFocus();
            return;
        }
        if (isImageAvailable.equals(false)) {
            Toast.makeText(Addbatches.this, "Please Upload Batch Image", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(selectedNameBoard)) {
            Toast.makeText(Addbatches.this, "selected  Board Name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(selectedNameClass)) {
            Toast.makeText(Addbatches.this, "selected Class Name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(selectedNameSubject)) {
            Toast.makeText(Addbatches.this, "selected Subject Name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(selectedBatchType)) {
            Toast.makeText(Addbatches.this, "selected Batch Name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(selectedNameTeacher)) {
            Toast.makeText(Addbatches.this, "selected Subject Name", Toast.LENGTH_LONG).show();
            return;
        }
        Addbatche();
    }


    private void Addbatche() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, NeWayApi.Add_Batch, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("vhehdhdh", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(Addbatches.this, "" + message, Toast.LENGTH_SHORT).show();

                        onBackPressed();

                    } else {
                        Toast.makeText(Addbatches.this, "" + message, Toast.LENGTH_SHORT).show();
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

                parem.put("batch_type", selectedBatchType);
                parem.put("batch_name", et_Batch_name.getText().toString());
                parem.put("board_name", selectedNameBoard);
                parem.put("subject_name", selectedNameSubject);
                parem.put("class_fee1", et_ClassFee_signup.getText().toString());
                parem.put("duration", et_Duration.getText().toString());
                parem.put("timing", et_timing.getText().toString());
                parem.put("open_days", et_Open_Days.getText().toString());
                parem.put("tbl_teacher_id_1", selectedNameId);
                parem.put("tbl_coaching_id", Mtable_id);
                parem.put("total_sheet", et_TotalSeatAvailable.getText().toString());
                parem.put("left_sheet", et_TotalLeftSeat.getText().toString());
                parem.put("monthly_fee_avl", Type);

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

        Volley.newRequestQueue(Addbatches.this).add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void setAdapterStatus(Spinner sp_statuspending, ArrayList list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_statuspending.setAdapter(adapter);// set the selected value of the spinner

    }

    private void checkPermissionForApp() {

        try {
            Dexter.withActivity(Addbatches.this).withPermissions(Manifest.permission.CAMERA,
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
                                Toast.makeText(Addbatches.this, "All permission are required to use this app", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(Addbatches.this, "Permission Error occurred...", Toast.LENGTH_SHORT).show();
                        }
                    }).onSameThread().check();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Addbatches.this);
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
                iv_uploadbatchimage.setImageBitmap(bitmap);
                Glide.with(Addbatches.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(iv_uploadbatchimage);
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
                bitmap = MediaStore.Images.Media.getBitmap(Addbatches.this.getContentResolver(), filePath);
                bitmapSize = bitmap.getAllocationByteCount();
                Glide.with(Addbatches.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(iv_uploadbatchimage);
                converetdImage = getResizedBitmap(bitmap, 400);

                Log.e("oldbitmap", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallery", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallerydasdas", (bitmap.getAllocationByteCount() / 1024) + "");

                Log.e("dasdasddrerer", converetdImage.getAllocationByteCount() + "");
                Log.e("ytytyyuyutyuytuty", (converetdImage.getAllocationByteCount() / 1024) + "");

                if (converetdImage.getAllocationByteCount() > 512000) {

                } else {
                    Glide.with(Addbatches.this).load(bitmap).apply(new RequestOptions().override(192, 192)).
                            into(iv_uploadbatchimage);
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

    private void showSettingDialog() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Addbatches.this);
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
}