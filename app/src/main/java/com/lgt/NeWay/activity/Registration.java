package com.lgt.NeWay.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    public static String FullAddress="";
    public static String City="";
    public static String PinCode="";
    public static String State="";
    public static float LatitudeCode;
    public static float LongitudeCode;

    public EditText et_CoachingCenterName, et_mobileno_signup, et_Password, et_email_signup, et_owner_name_signup, et_Blockname_signup, et_Pincode_signup, et_Statename_signup;
    private TextView tv_Address_signup,tv_TNC;
    private ImageView iv_pointer,iv_back_signup;
    private ProgressBar progress_signup;
    private CheckBox signup_cheakbox;

    private String type, UAddress_signup, UCoachingCenterName, Umobileno_signup, UPassword, Uemail_signup, Uowner_name_signup, UBlockname_signup, UPincode_signup, UStatename_signup,Usignup_cheakbox;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private RadioButton rb_onlyclasses, OnlyCourese, rbBoth;
    Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        iniViews();
        iniSharedPref();
        handleClick();

    }

    private void iniViews() {

        rb_onlyclasses = findViewById(R.id.rb_onlyclasses);

        OnlyCourese = findViewById(R.id.OnlyCourese);
       tv_TNC= findViewById(R.id.tv_TNC);
        rbBoth = findViewById(R.id.rbBoth);
        signup_cheakbox = findViewById(R.id.signup_cheakbox);
        progress_signup = findViewById(R.id.progress_signup);
        iv_pointer = findViewById(R.id.iv_pointer);
        tv_Address_signup = findViewById(R.id.tv_Address_signup);
        et_CoachingCenterName = findViewById(R.id.et_CoachingCenterName);
        et_mobileno_signup = findViewById(R.id.et_mobileno_signup);
        et_Password = findViewById(R.id.et_Password);
        et_email_signup = findViewById(R.id.et_email_signup);
        et_owner_name_signup = findViewById(R.id.et_owner_name_signup);
        et_Blockname_signup = findViewById(R.id.et_Blockname_signup);
        et_Pincode_signup = findViewById(R.id.et_Pincode_signup);
        et_Statename_signup = findViewById(R.id.et_Statename_signup);
        btn_signup = findViewById(R.id.btn_signup);
        iv_back_signup = findViewById(R.id.iv_back_signup);
    }

    private void handleClick() {
        iv_pointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this, MapActivity.class);
                startActivity(i);
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation();
                /* */
            }
        });
        iv_back_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rb_onlyclasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rb_onlyclasses.isChecked()) {
                    type = "yes";
                    Log.e("checkradiobuttonclick", type + "");

                    if (rbBoth.isChecked()) {
                        rbBoth.setChecked(false);

                    }
                    if (OnlyCourese.isChecked()) {
                        OnlyCourese.setChecked(false);

                    }
                }
            }
        });

        OnlyCourese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (OnlyCourese.isChecked()) {
                    type = "no";
                    Log.e("checkradiobuttonclick", type + "");
                    if (rbBoth.isChecked()) {
                        rbBoth.setChecked(false);


                    }
                    if (rb_onlyclasses.isChecked()) {
                        rb_onlyclasses.setChecked(false);


                    }
                }
            }

        });

        rbBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rbBoth.isChecked()) {
                    type = "both";
                    Log.e("checkradiobuttonclick", type + "");
                    if (OnlyCourese.isChecked()) {
                        OnlyCourese.setChecked(false);


                    }
                    if (rb_onlyclasses.isChecked()) {
                        rb_onlyclasses.setChecked(false);

                    }
                }
            }
        });
        tv_TNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "http://neway.today/privacy_policy.php";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
    }

    private void iniSharedPref() {
        sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (FullAddress != null && !FullAddress.equalsIgnoreCase("")) {
            tv_Address_signup.setText(FullAddress);
            et_Blockname_signup.setText(City);
            et_Pincode_signup.setText(PinCode);
            et_Statename_signup.setText(State);
        }
    }

    private void validation() {
        UAddress_signup = tv_Address_signup.getText().toString();
        UBlockname_signup = et_Blockname_signup.getText().toString();
        UCoachingCenterName = et_CoachingCenterName.getText().toString();
        Uemail_signup = et_email_signup.getText().toString();
        Umobileno_signup = et_mobileno_signup.getText().toString();
        Uowner_name_signup = et_owner_name_signup.getText().toString();
        UPassword = et_Password.getText().toString();
        UPincode_signup = et_Pincode_signup.getText().toString();
        UStatename_signup = et_Statename_signup.getText().toString();


      if (TextUtils.isEmpty(Uemail_signup)) {
            et_email_signup.setError("Please Enter Email");
            et_email_signup.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(UBlockname_signup)) {
            et_Blockname_signup.setError("Please Enter Blockname");
            et_Blockname_signup.requestFocus();
            return;
        }

      /*  if (TextUtils.isEmpty(Usignup_cheakbox)) {
            signup_cheakbox.setError("Please Sele Blockname");
            signup_cheakbox.requestFocus();
            return;
        }*/

        if (TextUtils.isEmpty(UCoachingCenterName)) {
            et_CoachingCenterName.setError("Please Enter CoachingCenterName");
            et_CoachingCenterName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Umobileno_signup)) {
            et_mobileno_signup.setError("Please Enter mobile");
            et_mobileno_signup.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(Uowner_name_signup)) {
            et_owner_name_signup.setError("Please Enter owner_name");
            et_owner_name_signup.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(UPassword)) {
            et_Password.setError("Please Enter Password");
            et_Password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(UPincode_signup)) {
            et_Pincode_signup.setError("Please Enter Pincode_signup");
            et_Pincode_signup.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(UStatename_signup)) {
            et_Statename_signup.setError("Please Enter State_name");
            et_Statename_signup.requestFocus();
            return;
        }
        if (signup_cheakbox.isChecked()) {
            loadApidata();
            progress_signup.setVisibility(View.VISIBLE);
        }else {
            signup_cheakbox.setError("Please agree T&C");
        }
    }

    private void loadApidata() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checkapiresponse", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        JSONObject object = jsonArray.getJSONObject(0);

                        String tbl_coaching_id = object.getString("tbl_coaching_id");
                        String coaching_name = object.getString("coaching_name");
                        String contact_no = object.getString("contact_no");
                        String email_id = object.getString("email_id");
                        String owner_name = object.getString("owner_name");
                        String special_course = object.getString("special_course");
                        String block_no = object.getString("block_no");
                        String city = object.getString("city");
                        String state = object.getString("state");
                        String pincode = object.getString("pincode");
                        String full_address = object.getString("full_address");
                        String latitude = object.getString("latitude");
                        String longitude = object.getString("longitude");

                       // Toast.makeText(Registration.this, message + "", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Registration.this, MainActivity.class);
                        startActivity(intent);

                        Log.e("a2a2a2a2", intent + "");
                        editor.putString("tbl_coaching_id", tbl_coaching_id);
                        editor.putString("owner_name", owner_name);
                        editor.putString("contact_no", contact_no);
                        editor.putString("coaching_name", coaching_name);
                        editor.putString("full_address", full_address);
                        editor.putString("latitude", latitude);
                        editor.putString("longitude", longitude);
                        editor.putString("city", city);

                        editor.commit();
                        editor.apply();
                        finish();

                    } else if (status.equals("0")) {
                        progress_signup.setVisibility(View.GONE);

                        Toast.makeText(Registration.this, message + "", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("errorrrrrrr", error + "");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_coaching_id", "tbl_coaching_id");
                parem.put("name",UCoachingCenterName);
                parem.put("contact_no", Umobileno_signup);
                parem.put("email_id", Uemail_signup);
                parem.put("owner_name", Uowner_name_signup);
                parem.put("special_course", type);
                parem.put("password",UPassword);
                parem.put("city", UBlockname_signup);
                parem.put("state", UStatename_signup);
                parem.put("pincode", UPincode_signup);
                parem.put("full_address", UAddress_signup);
                parem.put("latitude", String.valueOf(LatitudeCode));
                parem.put("longitude", String.valueOf(LongitudeCode));

                Log.e("checkparemresponse", parem + "");

                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}