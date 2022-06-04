package com.lgt.fxtradingleague.MyAccount;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Extra.VolleyMultipartRequest;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lgt.fxtradingleague.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.fxtradingleague.Extra.ExtraData.UPLOAD_DOCUMENTS_API;

public class UploadKYCActivity extends AppCompatActivity implements ResponseManager {

    UploadKYCActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;

    TextView tv_Upload, tv_EtDocNameBottomText, tv_EtDocNumberBottomText, tv_EtDocDobBottomText,
            tv_EtDocStateBottomText, tv_SubmitForVerification, tv_VerifyHead;
    EditText et_DocName, et_DocNumber, et_DocDob, et_DocState;


    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    int PICK_IMAGE_GALLERY = 100;
    int PICK_IMAGE_CAMERA = 101;
    ImageView im_ImagePreview;
    Bitmap bitmap;

    String UserName, DocNumber, DateofBirth, State;
    String PanOrAadhaar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_kyc);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = activity = this;
        initViews();
        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);

        PanOrAadhaar = getIntent().getStringExtra("DocType");


        if (PanOrAadhaar.equals("pan")) {
            //  tv_EtDocNameBottomText.setText("Name – As on PAN card");
            tv_VerifyHead.setText("VERIFY YOUR PAN");
            et_DocNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            et_DocNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            et_DocNumber.setHint("10 digit number");
            // tv_EtDocNumberBottomText.setText("PAN Number – 10 digit number");

        } else if(PanOrAadhaar.equals("aadhar")) {
            //tv_EtDocNameBottomText.setText("Name – As on Aadhaar card");
            tv_VerifyHead.setText("VERIFY YOUR AADHAAR");
            et_DocNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
            et_DocNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
            et_DocNumber.setHint("Aadhaar Number 12 digit number");
            et_DocDob.setHint("Date of Birth As on Aadhaar card");
        }

        tv_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions();
                } else {
                    ChooseImageDialog();
                }
            }
        });

        et_DocDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(activity,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new mdateListner(), year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources()
                        .getColor(android.R.color.transparent)));
                dialog.show();
            }
        });


        tv_SubmitForVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = et_DocName.getText().toString();
                DocNumber = et_DocNumber.getText().toString();
                DateofBirth = et_DocDob.getText().toString();
                State = et_DocState.getText().toString();
                if (bitmap == null) {
                    ShowToast(activity, "Please select image");
                } else if (UserName.equals("")) {
                    ShowToast(activity, "Please Enter Name");
                } else if (DocNumber.equals("")) {
                    ShowToast(activity, "Please Enter Document Number");
                } else if (DateofBirth.equals("")) {
                    ShowToast(activity, "Please Enter Document Number");
                } else if (State.equals("")) {
                    ShowToast(activity, "Please Enter State Name");
                } else {
                    uploadDocumentByType(PanOrAadhaar);
                }

            }
        });

    }

    private void uploadDocumentByType(final String panOrAadhaar) {
        Log.e("uploadDocumentByType", UPLOAD_DOCUMENTS_API+ "");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPLOAD_DOCUMENTS_API,
                new com.android.volley.Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("UPLOADDDD", response+ "");
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            Log.e("UPLOADDDD", status+ " | " +message);
                            if (status.equals("1")) {
                                Validations.hideProgress();
                                Toast.makeText(UploadKYCActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UploadKYCActivity.this, HomeActivity.class));
                                finishAffinity();
                            } else {
                                Validations.hideProgress();
                                Toast.makeText(UploadKYCActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Validations.hideProgress();
                            e.printStackTrace();
                        }
                        Log.e("MULTIPART", response + "");

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.hideProgress();
                Log.e("MULTIPART", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sessionManager.getUser(context).getUser_id());
                params.put("name", UserName);
                params.put("number", DocNumber);
                params.put("dob", DateofBirth);
                params.put("state", State);
                params.put("type", panOrAadhaar);
                Log.e("PPPPPPP", params + "");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                Log.e("PARAMS", params + "");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void initViews() {
        tv_Upload = findViewById(R.id.tv_Upload);
        im_ImagePreview = findViewById(R.id.im_ImagePreview);

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        tv_HeaderName.setText("UPLOAD DOCUMENT");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // tv_EtDocNameBottomText = findViewById(R.id.tv_EtDocNameBottomText);
        //  tv_EtDocNumberBottomText = findViewById(R.id.tv_EtDocNumberBottomText);
        //tv_EtDocDobBottomText = findViewById(R.id.tv_EtDocDobBottomText);
        // tv_EtDocStateBottomText = findViewById(R.id.tv_EtDocStateBottomText);
        tv_SubmitForVerification = findViewById(R.id.tv_SubmitForVerification);
        tv_VerifyHead = findViewById(R.id.tv_VerifyHead);
        et_DocName = findViewById(R.id.et_DocName);
        et_DocNumber = findViewById(R.id.et_DocNumber);
        et_DocDob = findViewById(R.id.et_DocDob);
        et_DocState = findViewById(R.id.et_DocState);

    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        ShowToast(activity, "" + message);
        startActivity(new Intent(UploadKYCActivity.this, HomeActivity.class));
        finishAffinity();
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        ShowToast(activity, "" + message);
    }

    public void ChooseImageDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
        builderSingle.setTitle("Choose Image");
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(activity,
                        android.R.layout.simple_list_item_1);
        arrayAdapter.add("Camera");
        arrayAdapter.add("Gallery");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                dialog.dismiss();
                if (strName.equals("Gallery")) {
                    bitmap = null;
                    im_ImagePreview.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
                } else {
                    bitmap = null;
                    im_ImagePreview.setVisibility(View.GONE);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
                }
            }
        });
        builderSingle.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY &&
                resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.
                        getBitmap(activity.getContentResolver(), filePath);
                im_ImagePreview.setVisibility(View.VISIBLE);
                im_ImagePreview.setImageBitmap(bitmap);

                Log.e("Image Path", "onActivityResult: " + filePath);
            } catch (Exception e) {
                bitmap = null;
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                im_ImagePreview.setVisibility(View.VISIBLE);
                im_ImagePreview.setImageBitmap(bitmap);

                Log.e("Image Path", "onActivityResult: ");
            } catch (Exception e) {
                bitmap = null;
                e.printStackTrace();
            }
        }

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), 100);
            return false;
        } else {
            ChooseImageDialog();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ChooseImageDialog();
            } else {

            }
            return;
        }
    }

    class mdateListner implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            {
                int actualMonth = month + 1;
                Date d = new Date(year, actualMonth, dayOfMonth);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(year, month, dayOfMonth, 0, 0, 0);
                Date chosenDate = cal.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String df_medium_us_str = dateFormat.format(chosenDate);
                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, month, dayOfMonth - 1);
                String dayOfWeek = simpledateformat.format(date);
                et_DocDob.setText(df_medium_us_str);
            }
        }
    }
}
