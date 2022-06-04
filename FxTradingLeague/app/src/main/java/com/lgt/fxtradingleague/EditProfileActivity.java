package com.lgt.fxtradingleague;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.lgt.fxtradingleague.Extra.ExtraData.EDIT_PROFILE_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.PROFILE_API;

public class EditProfileActivity extends AppCompatActivity {

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    Context context;
    EditProfileActivity activity;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;

    EditText et_EditName,et_EditEmail,et_EditMobile,et_EditDob
            ,et_EditAddress,et_EditCity,et_EditPincode,et_EditState
            ,et_EditCountry,et_EditFavouriteTeam;
    TextView tv_EditMale,tv_EditFeMale,tv_EditUpdateProfile;

    String name,mobile,email,image,teamName,favriteTeam,dob,gender="male",address,city,pincode,state,country;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;
    RequestQueue requestQueue;
    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context = activity = this;
        requestQueue = Volley.newRequestQueue(this);
        myCalendar = Calendar.getInstance();
        initViews();
        callViewProfile(true);
        sessionManager = new SessionManager();
        apiRequestManager = new APIRequestManager(activity);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // callViewProfile(true);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        et_EditDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    DatePickerDialog datePickerDialog=new DatePickerDialog(EditProfileActivity.this,date
                            ,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show();
                }
            }
        });

        tv_EditUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEditProfile(true);
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_EditDob.setText(sdf.format(myCalendar.getTime()));
    }

    public void initViews() {
        et_EditName = findViewById(R.id.et_EditName);
        et_EditEmail = findViewById(R.id.et_EditEmail);
        et_EditMobile = findViewById(R.id.et_EditMobile);
        et_EditDob = findViewById(R.id.et_EditDob);
        et_EditAddress = findViewById(R.id.et_EditAddress);
        et_EditCity = findViewById(R.id.et_EditCity);
        et_EditPincode = findViewById(R.id.et_EditPincode);
        et_EditState = findViewById(R.id.et_EditState);
        et_EditCountry = findViewById(R.id.et_EditCountry);
        tv_EditMale = findViewById(R.id.tv_EditMale);
        tv_EditFeMale = findViewById(R.id.tv_EditFeMale);
        et_EditFavouriteTeam = findViewById(R.id.et_EditFavouriteTeam);
        tv_EditUpdateProfile = findViewById(R.id.tv_EditUpdateProfile);

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        tv_HeaderName.setText("PERSONAL DETAILS");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // default view start
        tv_EditMale.setBackgroundResource(R.drawable.gredient_bt);
        tv_EditMale.setTextColor(getResources().getColor(R.color.white));
        tv_EditFeMale.setTextColor(getResources().getColor(R.color.black));
        tv_EditFeMale.setBackgroundResource(R.drawable.add_cash_black);
        // default view end
        et_EditCountry.setText("India");
        et_EditCountry.setEnabled(false);
        et_EditCountry.setFocusable(false);

        tv_EditMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "male";
                tv_EditMale.setBackgroundResource(R.drawable.gredient_bt);
                tv_EditMale.setTextColor(getResources().getColor(R.color.white));
                tv_EditFeMale.setTextColor(getResources().getColor(R.color.black));
                tv_EditFeMale.setBackgroundResource(R.drawable.add_cash_black);
            }
        });
        tv_EditFeMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "female";
                tv_EditFeMale.setTextColor(getResources().getColor(R.color.white));
                tv_EditFeMale.setBackgroundResource(R.drawable.gredient_bt);
                tv_EditMale.setTextColor(getResources().getColor(R.color.black));
                tv_EditMale.setBackgroundResource(R.drawable.add_cash_black);
            }
        });


    }

    private void callViewProfile(boolean isShowLoader) {
        Validations.showProgress(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.hideProgress();
                Validations.common_log(""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        JSONObject data =jsonObject.getJSONObject("data");
                        String name = data.getString("name");
                        String gender = data.getString("gender");
                        String mobile = data.getString("mobile");
                        String email = data.getString("email");
                        String winning_amt = data.getString("winning_amt");
                        String deposite_amt = data.getString("deposite_amt");
                        String bonus_amt = data.getString("bonus_amt");
                        String birthday = data.getString("birthday");
                        String referral_code = data.getString("referral_code");
                        String user_image = data.getString("user_image");
                        et_EditName.setText(name);
                        et_EditEmail.setText(email);
                        et_EditMobile.setText(mobile);
                        et_EditDob.setText(birthday);
                        if (gender.equalsIgnoreCase("male")){
                            tv_EditMale.setBackgroundResource(R.drawable.gredient_bt);
                            tv_EditMale.setTextColor(getResources().getColor(R.color.white));
                            tv_EditFeMale.setTextColor(getResources().getColor(R.color.black));
                            tv_EditFeMale.setBackgroundResource(R.drawable.add_cash_black);
                        }else if(gender.equalsIgnoreCase("female")){
                            tv_EditFeMale.setTextColor(getResources().getColor(R.color.white));
                            tv_EditFeMale.setBackgroundResource(R.drawable.gredient_bt);
                            tv_EditMale.setTextColor(getResources().getColor(R.color.black));
                            tv_EditMale.setBackgroundResource(R.drawable.add_cash_black);
                        }
                    }else{
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.hideProgress();
                error.printStackTrace();
                Validations.common_log(""+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("user_id",sessionManager.getUser(context).getUser_id());
                Validations.common_log(""+param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
        /*try {

            apiRequestManager.callAPI(VIEWPROFILE,
                    createRequestJson(), context, activity, VIEWPROFILETYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    private void callEditProfile(boolean isShowLoader) {
        Validations.showProgress(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_PROFILE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Validations.common_log(""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        Validations.hideProgress();
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditProfileActivity.this,HomeActivity.class));
                        finish();
                    }else{
                        Validations.hideProgress();
                        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Validations.hideProgress();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.hideProgress();
                Validations.common_log(""+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("user_id",sessionManager.getUser(context).getUser_id());
                param.put("email",et_EditEmail.getText().toString());
                param.put("name",et_EditName.getText().toString());
                param.put("birthday",et_EditDob.getText().toString());
                param.put("gender",gender);
                Validations.common_log("EditProfile"+param);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    /*JSONObject createEditProfileJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            jsonObject.put("name", et_EditName.getText().toString());
            jsonObject.put("mobile", et_EditMobile.getText().toString());
            jsonObject.put("favriteTeam", et_EditFavouriteTeam.getText().toString());
            jsonObject.put("dob", et_EditDob.getText().toString());
            jsonObject.put("gender", gender);
            jsonObject.put("address", et_EditAddress.getText().toString());
            jsonObject.put("city", et_EditCity.getText().toString());
            jsonObject.put("state", et_EditState.getText().toString());
            jsonObject.put("country", et_EditCountry.getText().toString());
            jsonObject.put("pincode", et_EditPincode.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
