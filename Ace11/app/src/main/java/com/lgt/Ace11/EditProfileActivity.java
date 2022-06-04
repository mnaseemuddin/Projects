package com.lgt.Ace11;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.UserDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.EDITPROFILE;
import static com.lgt.Ace11.Config.VIEWPROFILE;
import static com.lgt.Ace11.Constants.EDITPROFILETYPE;
import static com.lgt.Ace11.Constants.VIEWPROFILETYPE;

public class EditProfileActivity extends AppCompatActivity implements ResponseManager {

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

    String name,mobile,email,image,teamName,favriteTeam,dob,gender
            ,address,city,pincode,state,country;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;

    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context = activity = this;
        myCalendar = Calendar.getInstance();

        initViews();
        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        callViewProfile(true);
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


        et_EditCountry.setText("India");
        et_EditCountry.setEnabled(false);
        et_EditCountry.setFocusable(false);

    }

    private void callViewProfile(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(VIEWPROFILE,
                    createRequestJson(), context, activity, VIEWPROFILETYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void callEditProfile(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(EDITPROFILE,
                    createEditProfileJson(), context, activity, EDITPROFILETYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createEditProfileJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            jsonObject.put("name", et_EditName.getText().toString());
            jsonObject.put("mobile", et_EditMobile.getText().toString());
            jsonObject.put("email", et_EditEmail.getText().toString());
            jsonObject.put("favriteTeam", et_EditFavouriteTeam.getText().toString());
            jsonObject.put("dob", et_EditDob.getText().toString());
            jsonObject.put("gender", gender);
            jsonObject.put("address", et_EditAddress.getText().toString());
            jsonObject.put("city", et_EditCity.getText().toString());
            jsonObject.put("state", et_EditState.getText().toString());
            jsonObject.put("country", et_EditCountry.getText().toString());
            jsonObject.put("pincode", et_EditPincode.getText().toString());
            Log.d("SaveProfileParam",""+new Gson().toJson(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        Log.e("resssulttt",": "+result+", "+type);
        if (type.equals(EDITPROFILETYPE)){

            ShowToast(context,message);
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.commit();
            UserDetails userDetails = new UserDetails();
            userDetails.setUser_id(sessionManager.getUser(context).getUser_id());
            userDetails.setName(et_EditName.getText().toString());
            userDetails.setMobile(et_EditMobile.getText().toString());
            userDetails.setDob(et_EditDob.getText().toString());
            userDetails.setEmail(et_EditEmail.getText().toString());
            userDetails.setType(sessionManager.getUser(context).getType());
            userDetails.setReferral_code(sessionManager.getUser(context).getReferral_code());
            userDetails.setImage(sessionManager.getUser(context).getImage());
            userDetails.setAddress(et_EditAddress.getText().toString());
            userDetails.setCity(et_EditCity.getText().toString());
            userDetails.setPincode(et_EditPincode.getText().toString());
            userDetails.setState(et_EditState.getText().toString());
            userDetails.setVerify("1");
            sessionManager.setUser(context, userDetails);
            onBackPressed();
            finish();
        }
        else {
            try {
                Log.e("resssulttt",result+"");
                name = result.getString("name");
                mobile = result.getString("mobile");
                email = result.getString("email");
                image = result.getString("image");
                teamName = result.getString("teamName");
                favriteTeam = result.getString("favriteTeam");
                dob = result.getString("dob");

                gender = result.getString("gender");
                address = result.getString("address");
                city = result.getString("city");
                pincode = result.getString("pincode");
                state = result.getString("state");
                country = result.getString("country");


                if (name.equals("null")) {
                    et_EditName.setText("");
                } else {
                    et_EditName.setText(name);
                }
                if (mobile.equalsIgnoreCase("null")){
                    et_EditMobile.setText("");
                }else {
                    et_EditMobile.setText(mobile);

                }
                if (email.equalsIgnoreCase("null")){
                    et_EditEmail.setText("");

                }else {
                    et_EditEmail.setText(email);
                }
                if (dob.equalsIgnoreCase("null")){
                    et_EditDob.setText("");

                }else {

                    et_EditDob.setText(dob);
                }

                if (address.equalsIgnoreCase("null")){
                    et_EditAddress.setText("");
                }else {
                    et_EditAddress.setText(address);
                }

                et_EditFavouriteTeam.setText(favriteTeam);

                if (city.equalsIgnoreCase("null")){
                    et_EditCity.setText("");
                }else {
                    et_EditCity.setText(city);
                }

                if (state.equalsIgnoreCase("null")){
                    et_EditState.setText("");
                }else {
                    et_EditState.setText(state);
                }

                if (pincode.equalsIgnoreCase("null")){
                    et_EditPincode.setText("");
                }else {
                    et_EditPincode.setText(pincode);
                }


                if (gender.equals("male")) {
                    tv_EditMale.setBackgroundResource(R.drawable.gredient_bt);
                    tv_EditMale.setTextColor(getResources().getColor(R.color.white));
                    tv_EditFeMale.setBackgroundResource(R.drawable.add_cash_black);

                } else if (gender.equals("female")) {
                    tv_EditFeMale.setBackgroundResource(R.drawable.gredient_bt);
                    tv_EditFeMale.setTextColor(getResources().getColor(R.color.black));
                    tv_EditMale.setBackgroundResource(R.drawable.add_cash_black);
                }

                /*if (!email.equals("")) {
                    et_EditEmail.setEnabled(false);
                    et_EditEmail.setFocusable(false);
                }*/


                if (!mobile.equals("")) {
                    et_EditMobile.setEnabled(false);
                    et_EditMobile.setFocusable(false);
                }


            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {

    }



}
