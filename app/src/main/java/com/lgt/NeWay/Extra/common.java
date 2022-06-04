package com.lgt.NeWay.Extra;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class common {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String UserData= "USER_DATA";
    public static String UseriD= "tbl_coaching_id";
    public static String USERSTATUS= "USER_STATUS";
    public static String Owner_name= "owner_name";
    public static String Full_address= "full_address";
    public static String Latitude= "latitude";
    public static String Longitude= "longitude";
    public static String Tbl_board_id= "tbl_board_id";
    public static String UsersChoice= "userChoiceSpinner";
    public static String Upassword= "password";

    public Context mContext;

    public common(Context context) {
        mContext = context;
    }


    /*public static boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
*/
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
