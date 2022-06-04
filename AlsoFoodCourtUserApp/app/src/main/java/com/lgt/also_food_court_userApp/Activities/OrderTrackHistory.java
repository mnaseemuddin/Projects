package com.lgt.also_food_court_userApp.Activities;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.directionhelpers.FetchURL;
import com.lgt.also_food_court_userApp.directionhelpers.TaskLoadedCallback;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderTrackHistory extends AppCompatActivity implements OnMapReadyCallback,TaskLoadedCallback {
    private LinearLayout iv_BackArrowOrderTrace,ll_Call;
    private TextView tv_deliveryCoast,tv_totalAmount,tv_order_id,tv_date,tv_mobile,tv_driverName,tv_total_item;
    private  double Driver_latitude,Driver_logitude,User_Latitude,User_Logitude;
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    private Polyline polyline;
    private View mapView;

    private MapView mapView_trackOrder;
    public ArrayList<LatLng> my_current_points=new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private String User_Id,user_latitude,user_longitute,driver_latitude,driver_longitute,orderNo,driver_contact_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track_history);

        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")){
            User_Id=sharedPreferences.getString("user_id","");
            Log.e("hgjsdg",User_Id+"");
        }


        Intent intent=getIntent();
        if (intent!=null){
            if (intent.hasExtra("product_No")){
                orderNo=intent.getStringExtra("product_No");
                Log.e("mkfh",orderNo+"");
            }
        }


        currentOrderApiData();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(OrderTrackHistory.this);

      // new FetchURL(OrderTrackHistory.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");


        Initialize();





    }

    private void currentOrderApiData() {

            StringRequest stringRequest=new StringRequest(Request.Method.POST, AlsoFoodCourtApi.OrderTackApi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("hjfksa",response+"");
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String message=jsonObject.getString("message");
                        String Status=jsonObject.getString("status");

                        if (Status.equalsIgnoreCase("1")){
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            if (jsonArray.length()>0){
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    String order_number=jsonObject1.getString("order_number");
                                    String total_price=jsonObject1.getString("total_amount");
                                   // String products_name=jsonObject1.getString("products_name");
                                    String quantity=jsonObject1.getString("total_qty");
                                    String driver_name=jsonObject1.getString("driver_name");
                                    String created_date=jsonObject1.getString("date");
                                    user_latitude=jsonObject1.getString("user_latitude");
                                     user_longitute=jsonObject1.getString("user_longitute");
                                     driver_latitude=jsonObject1.getString("driver_latitude");
                                     driver_longitute=jsonObject1.getString("driver_longitute");
                                     Log.e("dklfmbh",driver_latitude+"");
                                     driver_contact_number=jsonObject1.getString("driver_mobile");
                                     Log.e("ldkmfhj","dnfevbf");
                                    if (!TextUtils.isEmpty(user_latitude)&&!TextUtils.isEmpty(user_longitute)&&
                                            !TextUtils.isEmpty(driver_latitude)&&!TextUtils.isEmpty(driver_longitute)) {

                                        Driver_latitude = Double.parseDouble(driver_latitude);
                                        Driver_logitude = Double.parseDouble(driver_longitute);
                                        User_Latitude = Double.parseDouble(user_latitude);
                                        User_Logitude = Double.parseDouble(user_longitute);

                                        Log.e("aewwd", Driver_latitude + "" + user_latitude);
                                        Log.e("jfkj455", User_Latitude + "");


                                       // PolylineOptions routes = new PolylineOptions().width(10).color(Color.RED);
                                      //  polyline = mMap.addPolyline(routes);
                                        my_current_points.add(new LatLng(User_Latitude, User_Logitude));
                                        my_current_points.add(new LatLng(Driver_latitude, Driver_logitude));
                                        Log.e("hjd", my_current_points.toString() + "");
                                      //  polyline.setPoints(my_current_points);
                                        final LatLng currentMark = new LatLng(Driver_latitude, Driver_logitude);

                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(currentMark)
                                                .zoom(15)
                                                .build();
                                        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                        mMap.animateCamera(camUpd3);


                                        Log.e("bhfjs", currentMark + "");

                                        place1 = new MarkerOptions().position(new LatLng(Driver_latitude, Driver_logitude)).title("Driver Position").icon(BitmapDescriptorFactory.fromBitmap(CustomMarker(OrderTrackHistory.this, R.drawable.deliverybike, null)));
                                        place2 = new MarkerOptions().position(new LatLng(User_Latitude, User_Logitude)).title("Delivery Address").icon(BitmapDescriptorFactory.fromBitmap(CustomMarker(OrderTrackHistory.this, R.drawable.deliveryadress, null)));


                                        mMap.addMarker(place1);
                                        mMap.addMarker(place2);

                                        new FetchURL(OrderTrackHistory.this,OrderTrackHistory.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");




                                        tv_deliveryCoast.setText(getString(R.string.rs) + "" + "0");
                                        tv_totalAmount.setText(getString(R.string.rs) + " " + total_price);
                                        tv_order_id.setText(order_number);
                                        tv_date.setText(created_date);
                                        tv_mobile.setText(driver_contact_number);
                                        tv_driverName.setText(driver_name);
                                        tv_total_item.setText(quantity);
                                    }
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
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    params.put("order_number",orderNo);
                    Log.e("jhdf",params+"");
                    return params;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(OrderTrackHistory.this);
            requestQueue.add(stringRequest);
    }

    private void Initialize() {
        ll_Call=findViewById(R.id.ll_Call);
        tv_deliveryCoast=findViewById(R.id.tv_deliveryCoast);
        tv_totalAmount=findViewById(R.id.tv_totalAmount);
        iv_BackArrowOrderTrace=findViewById(R.id.iv_BackArrowOrderTrace);
        iv_BackArrowOrderTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_order_id=findViewById(R.id.tv_order_id);
        tv_date=findViewById(R.id.tv_date);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_driverName=findViewById(R.id.tv_driverName);
        tv_total_item=findViewById(R.id.tv_total_item);

        ll_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+driver_contact_number));
                startActivity(intent);
            }
        });

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);



    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        //  String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" +getString(R.string.google_maps_key);
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyAklDnpyaj_7zCd_OiIDig29wfw1sysdeE";
        return url;
    }
   @Override
    public void onTaskDone(Object... values) {
        if (polyline != null)
            polyline.remove();
        polyline = mMap.addPolyline((PolylineOptions) values[0]);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //    for (Marker marker : markers) {

        for (LatLng latLng : polyline.getPoints()) {
            builder.include(latLng);
            //  builder.include(marker_drop.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 40; // offset from edges of the map in pixels
        CameraUpdate camUpd3 = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(camUpd3);
        try {
            float bearing=0;
            if (polyline.getPoints().size()>2){
                bearing=(float) bearingBetweenLocations(polyline.getPoints().get(0),polyline.getPoints().get(1));
            }
            //    marker.setRotation(bearing-90);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;

    }




    public static Bitmap CustomMarker(Context context, @DrawableRes int resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_layout,null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.iv_vecheleImage);
        markerImage.setImageResource(resource);
       // TextView txt_name = (TextView)marker.findViewById(R.id.driver_name);
       // txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }


}
