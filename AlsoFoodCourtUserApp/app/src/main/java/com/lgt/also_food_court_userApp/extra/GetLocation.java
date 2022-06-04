package com.lgt.also_food_court_userApp.extra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.List;

public  class GetLocation implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button btnFind;
    private RippleBackground rippleBg;

    List<Address> addressList;

    Context context;
    private final float DEFAULT_ZOOM = 15;


    @SuppressLint("MissingPermission")
    public  String getDeviceLocation() {

        Log.e("dsadsadsadsadsa","ddssadsadsadds");
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                Geocoder geocoder = new Geocoder(context);
                                try {
                                    addressList= geocoder.getFromLocation(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude(),1);



                                    Log.e("ggggggggg",addressList.get(0).getFeatureName()+"");//Okhla
                                    Log.e("fdddddddddddd",addressList.get(0).getAddressLine(0)+"");//Block G28,Saheen Bagh,Near Masjid 110025
                                    Log.e("fdfdfdfdfffffff",addressList.get(0).getSubLocality()+""); //Okhla



                                    String str = addressList.get(0).getLocality();
                                    str += addressList.get(0).getPostalCode();
                                    str += addressList.get(0).getAdminArea();
                                    str += addressList.get(0).getLocale();
                                    str += addressList.get(0).getPremises();

                                    Log.e("dsadsadsadsa",addressList+"");
                                    Log.e("sdsdsadsadsd",addressList+"");


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);

                                        Geocoder geocoder = new Geocoder(context);
                                        try {
                                            List<Address> addressList = geocoder.getFromLocation(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude(),1);
                                            String str = addressList.get(0).getLocality();
                                            str += addressList.get(0).getPostalCode();
                                            str += addressList.get(0).getAdminArea();
                                            str += addressList.get(0).getLocale();
                                            str += addressList.get(0).getPremises();

                                            Log.e("dsadsadsadsa",addressList+"");


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(context, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return addressList.get(0).getFeatureName();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {



    }
}
