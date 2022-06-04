package com.lgt.also_food_court_userApp.extra;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Common {

    public static String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };





    private static String status;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //string keys
    public static String from_order_review="add_address_from_order_review";
    public static String address ="address";
    public static String address_id="address_id";
    public static String latitude="latitudeCode";
    public static String logitude="logitudecode";
    public static String tbl_restaurant_productId="tbl_restaurant_productId";
    public static String tbl_restaurant_id="tbl_restaurant_id";
    public static String UPDATE_CART="update";



//Production
    public static String MerchantID="6883292";
    public static String MerchantKEy="2ThvJJMb";
    public static String MerchantSALTKEy="YZlCb5lRQ3";

//TestKey
public static String MerchantIDTest="6883292";
    public static String MerchantKEyTest="2ThvJJMb";
    public static String MerchantSALTKEyTest="YZlCb5lRQ3";



    //

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.e("play_services", "This device is not supported google play services.");

            }
            return false;
        }
        return true;
    }


}
