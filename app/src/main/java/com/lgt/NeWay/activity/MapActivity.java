package com.lgt.NeWay.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    ImageView iv_back_ArrowFindLocation;
    private ProgressBar progressBar;
    private TextView tv_cityName, tv_fullAddressDetails;
    private Button btn_AddressChange, btn_ConfirmLocation;
    private RippleBackground rippleBg;

    String PinCode;
    //location variables
    private GoogleMap mMap;
    private String address_action = "";
    String apply_api, CityName, fullAddress;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    LocationCallback locationCallback;
    public static double latitude, longitude;

    public static int UPDATE_INTERVAL = 2000; //5 SEC
    public static int FASTEST_INTERVAL = UPDATE_INTERVAL / 2; //5 SEC
    public static final int REQUEST_CHECK_SETTINGS = 1;
    FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;
    float latitudeCode, longitudeCode;
    //

    private Context context;
    private MapActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        context = activity = this;
        iniViews();
        getLocation();


    }


    private void iniViews() {
        // rippleBg = findViewById(R.id.ripple_bg);
        tv_cityName = findViewById(R.id.tv_cityName);
        tv_fullAddressDetails = findViewById(R.id.tv_fullAddressDetails);
        btn_AddressChange = findViewById(R.id.btn_AddressChange);
        btn_ConfirmLocation = findViewById(R.id.btn_ConfirmLocation);
        progressBar = findViewById(R.id.progressBar);
        iv_back_ArrowFindLocation = findViewById(R.id.iv_back_ArrowFindLocation);


        btn_ConfirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
               /* Intent intent=new Intent(getApplicationContext(),ActivitySignUp.class);
                intent.putExtra("Block_NAME",CityName);
                intent.putExtra("FullAddress",fullAddress);
                intent.putExtra("Latitude",latitudeCode);
                intent.putExtra("Longitude",longitudeCode);
                intent.putExtra("PinCode",PinCode);
                Log.e("QWSA4587",CityName+"");
                Log.e("QWSA587",fullAddress+"");
                Log.e("QWSA87",latitudeCode+"");
                Log.e("QWSA7",longitudeCode+"");
                Log.e("QWSA",PinCode+"");
                startActivity(intent);
                finish();*/

            }
        });
        iv_back_ArrowFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //create Hazards
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
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
        tv_cityName.setText("Locating.....");
        tv_fullAddressDetails.setText("");
        btn_ConfirmLocation.setBackground(getResources().getDrawable(R.drawable.backgroundwhitecolor));
        //start ripple effect for handler
        //   rippleBg.startRippleAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                ChangeLocationUpdate();
                btn_ConfirmLocation.setBackground(getResources().getDrawable(R.drawable.btn_background_green));
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
                fullAddress = addresses.get(0).getAddressLine(0);
                Registration.FullAddress = fullAddress;


                // Log.d("location",""+new Gson().toJson(addresses));
                String BlockName = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                PinCode = addresses.get(0).getPostalCode();
                latitudeCode = (float) addresses.get(0).getLatitude();
                longitudeCode = (float) addresses.get(0).getLongitude();
                Registration.State = state;
                Registration.City = BlockName;
                Registration.PinCode = PinCode;
                Registration.LatitudeCode = latitudeCode;
                Registration.LongitudeCode = longitudeCode;

                Log.e("KMJNHBG", latitude + "");
                Log.e("KMJNHBG123", longitude + "");

                Log.e("LK896", state + "");
                if (BlockName == null) {
                    tv_cityName.setText(state);
                } else {
                    tv_cityName.setText(BlockName);
                }
                CityName = tv_cityName.getText().toString();
                tv_fullAddressDetails.setText(fullAddress);


                Log.e("JNHBG", BlockName + "");
                Log.e("JNHBG123", fullAddress + "");
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
        if (common.checkPlayServices(context)) {

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
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
    }}