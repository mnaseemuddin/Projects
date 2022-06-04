package com.lgt.also_food_court_userApp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.Common;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Add_AddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView iv_back_ArrowFindLocation;
    private ProgressBar progressBar;
    private TextView tv_fullAddressDetails,tv_pinCode;
    private Button btn_AddressChange, btn_confirmLocation;
    private RippleBackground rippleBg;

     private EditText et_ApartmentNo,et_mobileNO,et_landmark,et_AddressType;
    //location variables
    private GoogleMap mMap;
    private String address_action = "";
    String apply_api,CityName,addressLine,uApartmentNo,uMobileNo,uLandmark,uAddressType,mUserID,PinCode;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    LocationCallback locationCallback;
    public static double latitude, longitude;
    double LatitudeCode,LongitudeCode;
    public static int UPDATE_INTERVAL = 2000; //5 SEC
    public static int FASTEST_INTERVAL = UPDATE_INTERVAL / 2; //5 SEC
    public static final int REQUEST_CHECK_SETTINGS = 1;
    FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;
     private SharedPreferences sharedPreferences;

     private String AddressType;
    private String FullAddress;
    private String UAddID;
    private String Lat;
    private String Long;


    //

    private Context context;
    private Add_AddressActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__address);
        tv_fullAddressDetails=findViewById(R.id.tv_fullAddressDetails);
        et_AddressType=findViewById(R.id.et_AddressType);
        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")) {

            mUserID = sharedPreferences.getString("user_id", "");

        }

        Intent intent=getIntent();

            if (intent.hasExtra("KEY_ADD_ID")){
                UAddID = intent.getStringExtra("KEY_ADD_ID");
                Log.e("deswaq",UAddID+"");
                AddressType= intent.getStringExtra("KEY_ADD_Type");
                FullAddress= intent.getStringExtra("KEY_ADD_Full");
                Lat= intent.getStringExtra(Common.latitude);
                Long= intent.getStringExtra(Common.logitude);


                tv_fullAddressDetails.setText(FullAddress);
                et_AddressType.setText(AddressType);

            }


       /* Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("KEY_ADD_ID")) {

                mAddID = bundle.getString("KEY_ADD_ID");
                etNameAddress.setText(bundle.getString("KEY_ADD_NAME"));
                etMobileAddress.setText(bundle.getString("KEY_ADD_MOBILE_NUMBER"));
                etHousenoAddress.setText(bundle.getString("KEY_HOUSE_NO"));
                etNearbyPlace.setText(bundle.getString("KEY_NEAR_BY"));
                etPinCodeAddress.setText(bundle.getString("KEY_PIN_CODE"));

            }
        }*/
        context = activity = this;
        iniViews();
        getLocation();




    }

    private void iniViews() {
        et_ApartmentNo=findViewById(R.id.et_ApartmentNo);
        et_mobileNO=findViewById(R.id.et_mobileNO);
        et_landmark=findViewById(R.id.et_landmark);
        tv_pinCode=findViewById(R.id.tv_pinCode);
        rippleBg = findViewById(R.id.ripple_bg);
        btn_AddressChange = findViewById(R.id.btn_AddressChange);
        btn_confirmLocation = findViewById(R.id.btn_confirmLocation);
        progressBar = findViewById(R.id.progressBar);
        iv_back_ArrowFindLocation = findViewById(R.id.iv_back_ArrowFindLocation);


        btn_confirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("CITY_NAME",CityName);
                intent.putExtra("FullAddress",addressLine);
                startActivity(intent);
*/
               Validation();
            }
        });
        iv_back_ArrowFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }

    private void updateAddress() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.EDIT_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("JJJJJJhhn"," "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equals("1")){
                        Toast.makeText(getApplicationContext(), "Address updated", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("user_addressbook_id",UAddID);
                params.put("user_id", mUserID);
                params.put("title", uAddressType);
                params.put("name", "");
                params.put("houseNo", "");
                params.put("mobileNo", uMobileNo);
                params.put("email", "");
                params.put("nearBy", uLandmark);
                params.put("district", "");
                params.put("city", "");
                params.put("location", "");
                params.put("pin_code", "");
                params.put("state", "");
                params.put("country", "India");
                params.put("address", addressLine);
                params.put("latitude", String.valueOf(LatitudeCode));
                params.put("longitute", String.valueOf(LongitudeCode));

                Log.e("gtyuhji"," " + params);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Add_AddressActivity.this);
        requestQueue.add(stringRequest);

    }




    private void Validation() {
         uApartmentNo=et_ApartmentNo.getText().toString();
         uMobileNo=et_mobileNO.getText().toString();
         uLandmark=et_landmark.getText().toString();
         uAddressType=et_AddressType.getText().toString();


         if (TextUtils.isEmpty(uApartmentNo)){
             et_ApartmentNo.setError("Enter Apartment No");
             et_ApartmentNo.requestFocus();
             return;
         }
        if (TextUtils.isEmpty(uMobileNo)){
            et_mobileNO.setError("Enter Mobile No");
            et_mobileNO.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(uLandmark)){
            et_landmark.setError("Enter Landmark/Area");
            et_landmark.requestFocus();
        }
        if (TextUtils.isEmpty(uAddressType)){
            et_AddressType.setError("Enter Address Type");
            et_AddressType.requestFocus();
            return;
        }


        if(UAddID!=null){
            if(!UAddID.equals("")){

                updateAddress();
            }
        }  else {

            AddressAddApiData();
        }


    }

    private void AddressAddApiData() {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ADD_NEW_ADDRESS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("FRDESW" ,""+ response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {

                           /* if (from_order_review){
                                forwordToOrderReview();
                            }else {
                                FragmentManager fragmentManager = getFragmentManager();
                                assert fragmentManager != null;
                                fragmentManager.popBackStack("FragmentAddAddress", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }*/
                            Toast.makeText(getApplicationContext(), "Address added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Address not added", Toast.LENGTH_SHORT).show();

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

                    Map<String, String> params = new HashMap<>();

                    params.put("user_id", mUserID);
                    params.put("title", uAddressType);
                    params.put("name", "");
                    params.put("houseNo", "");
                    params.put("mobileNo", uMobileNo);
                    params.put("email", "");
                    params.put("nearBy", uLandmark);
                    params.put("district", "");
                    params.put("city", "");
                    params.put("location", "");
                    params.put("pin_code",PinCode);
                    params.put("state", "");
                    params.put("country", "India");
                    params.put("address", addressLine);
                    params.put("latitude", String.valueOf(LatitudeCode));
                    params.put("longitute", String.valueOf(LongitudeCode));

                    Log.e("HBGVFC"," " + params);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Add_AddressActivity.this);
            requestQueue.add(stringRequest);



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //create Hazards
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        setUpLocation();

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                latitude = cameraPosition.target.latitude;
                longitude = cameraPosition.target.longitude;


                Log.e("KJJHHHHHH", latitude + "");
                Log.e("KJJHHHHHH", longitude + "");

                startHandler();

            }
        });
    }

    private void startHandler() {
        progressBar.setVisibility(View.VISIBLE);
        tv_fullAddressDetails.setText("Locating.....");
        //tv_fullAddressDetails.setText("");
        btn_confirmLocation.setBackground(getResources().getDrawable(R.drawable.backgroundwhitecolor));
        //start ripple effect for handler
        //   rippleBg.startRippleAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                ChangeLocationUpdate();
                btn_confirmLocation.setBackground(getResources().getDrawable(R.drawable.btn_background_login));
                //  rippleBg.stopRippleAnimation();
                // stop ripple effect for handler
            }
        }, 2000);
    }

    private void ChangeLocationUpdate() {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (!addresses.isEmpty()) {
                addressLine = addresses.get(0).getAddressLine(0);
                 PinCode = addresses.get(0).getPostalCode();
                String state=addresses.get(0).getAdminArea();
                LatitudeCode= addresses.get(0).getLatitude();
                LongitudeCode= addresses.get(0).getLongitude();

                Log.e("LK896",state+"");

                tv_fullAddressDetails.setText(addressLine);
                tv_pinCode.setText(PinCode);

                Log.e("JNHBG", PinCode + "");

                Log.e("78954", LatitudeCode + "");
                Log.e("ewrfv", LongitudeCode + "");
                //   Log.e("city_name",city_name+"");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLocation() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        MapsInitializer.initialize(context);
        setUpLocation();
    }

    private void setUpLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    lastLocation = location;

                    Log.e("location_request_status", lastLocation.getSpeed() + "");


                }


            }
        };
        if (Common.checkPlayServices(context)) {

            registerToGetLocation();

        }
    }

    private void registerToGetLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableGpsDialog();
        }

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                updateLocation();
            }
        });
        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private void enableGpsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        updateLocation();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateLocation() {
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful()) {

                            lastLocation = task.getResult();

                            Log.e("location_request_status", "connected");
                            if (lastLocation != null) {

                                displayLocation();
                            } else {
                                Log.e("location_request_status", "lastlocation_is_null");

                                requestLocationUpdates();
                            }

                        } else {
                            Log.e("location_request_status", "not_connected");

                        }
                    }
                });

    }

    private void requestLocationUpdates() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    lastLocation = location;

                    Log.e("location_request_status", lastLocation.getSpeed() + "");

                }
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                displayLocation();

            }
        };
    }

    private void displayLocation() {


        if (UAddID!=null){
            latitude = Double.parseDouble(Lat);
            longitude = Double.parseDouble(Long);
            final LatLng currentMark = new LatLng(latitude, longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentMark)
                    .zoom(15)
                    .build();
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.animateCamera(camUpd3);
        }else {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            Log.e("MYLOCATIONN", "latitude=" + latitude + ",longitude=" + longitude);
            final LatLng currentMark = new LatLng(latitude, longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentMark)
                    .zoom(15)
                    .build();
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.animateCamera(camUpd3);
            getDeviceLocation();
        }




    }

    private void getDeviceLocation() {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (!addresses.isEmpty()) {
                String addressLine = addresses.get(0).getAddressLine(0);


                //   Log.e("city_name",city_name+"");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}


