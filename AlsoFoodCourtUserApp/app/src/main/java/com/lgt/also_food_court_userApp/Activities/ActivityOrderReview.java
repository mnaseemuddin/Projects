package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lgt.also_food_court_userApp.Adapters.AdapterReviewOrder;
import com.lgt.also_food_court_userApp.Adapters.BottomAddressList;
import com.lgt.also_food_court_userApp.Fragments.FragmentAddAddress;
import com.lgt.also_food_court_userApp.Models.ModelAddress;
import com.lgt.also_food_court_userApp.Models.ModelReviewOrder;
import com.lgt.also_food_court_userApp.extra.Common;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.SingletonVolley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityOrderReview extends AppCompatActivity {

    private TextView tvToolbar,tvTotalOrderAmount;

    private RecyclerView rvOrderReview;
    private List<ModelReviewOrder> listReview= new ArrayList<>();;
    private AdapterReviewOrder adapterReviewOrder;

    private SharedPreferences sharedPreferences;
    private static String mUserID = "";

    private ImageView iv_backToolbar;

    public  static String LatitudeCode,LogitudeCode,products_name;


    public static String Total_bag_price,name,email,mobileNo;
    public  TextView tv_subtotal_amount,tv_total_amount,tv_delivery_charge,tv_to_pay,tv_change_address;
    private LinearLayout llOrderReview,llPlaceOrder;
    public static ProgressBar pbOrder_review;

    //change address and select and add address
    private BottomSheetDialog change_address_dialog;
    private ProgressBar pb_address;
    private BottomAddressList addressBottomAddress;
    private RecyclerView rv_address_list;
    private LinearLayout LL_Add_new_address,LL_Address,LL_Bottom,LL_Add_Address_to_Proceed;
    private String check_address_list_size;
    private List<ModelAddress>addressList=new ArrayList<>();
    private Button bt_add_address,bt_select_address,bt_add_addressToProceed;
    private Bundle bundle;
    private String address,Tax_Charges;
    public static String address_id;
    private TextView tv_address,tv_textCharges;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);

        sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")) {
            mUserID = sharedPreferences.getString("user_id", "");
        }


        iniaViews();

        loadOrderReviewData();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(UpdateCart,new IntentFilter("Update_Cart"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(Update_Address,new IntentFilter("Update_Address"));

        iv_backToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        llPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), ActivityCheakOut.class);
                intent.putExtra(Common.latitude,LatitudeCode);
                intent.putExtra(Common.logitude,LogitudeCode);
                startActivity(intent);


            }
        });

    }

    private void iniaViews() {
        tv_textCharges=findViewById(R.id.tv_textCharges);
        iv_backToolbar = findViewById(R.id.iv_backToolbar);
        bundle=new Bundle();
        rvOrderReview = findViewById(R.id.rvOrderReview);
        tvToolbar = findViewById(R.id.tvToolbar);
        llOrderReview = findViewById(R.id.llOrderReview);
        tvTotalOrderAmount = findViewById(R.id.tvTotalOrderAmount);
        llPlaceOrder = findViewById(R.id.llPlaceOrder);
        pbOrder_review= findViewById(R.id.pbOrder_review);
        tv_subtotal_amount=findViewById(R.id.tv_subtotal_amount);
        tv_total_amount=findViewById(R.id.tv_total_amount);
        tv_delivery_charge=findViewById(R.id.tv_delivery_charge);
        tv_to_pay=findViewById(R.id.tv_to_pay);
        tv_change_address=findViewById(R.id.tv_change_address);
        bt_add_address=findViewById(R.id.bt_add_address);
        bt_select_address=findViewById(R.id.bt_select_address);
        tv_address=findViewById(R.id.tv_address);
        LL_Address=findViewById(R.id.LL_Address);
        LL_Bottom=findViewById(R.id.LL_Bottom);
        LL_Add_Address_to_Proceed=findViewById(R.id.LL_Add_Address_to_Proceed);
        bt_add_addressToProceed=findViewById(R.id.bt_add_addressToProceed);
        tvToolbar.setText("Review order");

        tv_change_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeAddressDialog();
            }
        });

        bt_select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAddressDialog();
            }
        });

        bt_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ActivityOrderReview.this,Add_AddressActivity.class);
                intent.putExtra(Common.from_order_review,Common.from_order_review);
                startActivity(intent);


               /* bundle.putString(Common.from_order_review,Common.from_order_review);
                FragmentAddAddress addAddress=new FragmentAddAddress();
                addAddress.setArguments(bundle);
                replaceFragment(addAddress);*/

            }
        });
        bt_add_addressToProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString(Common.from_order_review,Common.from_order_review);
                FragmentAddAddress addAddress=new FragmentAddAddress();
                addAddress.setArguments(bundle);
                replaceFragment(addAddress);
            }
        });

        if (getIntent().hasExtra(Common.address)){
            LL_Address.setVisibility(View.GONE);
            LL_Bottom.setVisibility(View.VISIBLE);
            address=getIntent().getStringExtra(Common.address);
            tv_address.setText(address);
        }else {
            checkAddressSize();

        }




    }

    private void checkAddressSize() {
        StringRequest request = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ADDRESS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("check_Address",response+"" );

                try {
                    JSONObject jsonObject = new JSONObject(response);

                  String  status = jsonObject.getString("status");

                  if (status.equalsIgnoreCase("1")){
                      LL_Address.setVisibility(View.VISIBLE);
                      LL_Add_Address_to_Proceed.setVisibility(View.GONE);
                          LL_Bottom.setVisibility(View.GONE);

                  }else {
                      LL_Add_Address_to_Proceed.setVisibility(View.VISIBLE);
                      LL_Bottom.setVisibility(View.GONE);
                      LL_Address.setVisibility(View.GONE);

                  }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LL_Add_Address_to_Proceed.setVisibility(View.VISIBLE);
                LL_Bottom.setVisibility(View.GONE);
                LL_Address.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                Log.e("HBGVFR",mUserID+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameOrderReview,fragment).addToBackStack("FragmentAddAddress").commit();
    }

    private void changeAddressDialog() {
        change_address_dialog=new BottomSheetDialog(this);
        change_address_dialog.setContentView(R.layout.address_list_bottom);
        change_address_dialog.show();
        LL_Add_new_address=change_address_dialog.findViewById(R.id.LL_Add_new_address);
        rv_address_list=change_address_dialog.findViewById(R.id.rv_address_list);
        pb_address=change_address_dialog.findViewById(R.id.pb_address);
        loadAddressList();

        LL_Add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* bundle.putString(Common.from_order_review,Common.from_order_review);
                FragmentAddAddress addAddress=new FragmentAddAddress();
                addAddress.setArguments(bundle);
                replaceFragment(addAddress);
                if (change_address_dialog!=null){
                    if (change_address_dialog.isShowing()){
                        change_address_dialog.dismiss();
                    }
                }*/

               Intent intent=new Intent(getApplicationContext(),Add_AddressActivity.class);
               intent.putExtra(Common.from_order_review,Common.from_order_review);
               startActivity(intent);



            }
        });


    }

    private void loadAddressList() {

        addressList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ADDRESS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pb_address.setVisibility(View.GONE);

                Log.e("onResponse", response+"" );

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            String address_id = object.getString("address_id");
                            String title = object.getString("title");
                             name = object.getString("name");

                            String houseNo = object.getString("houseNo");
                             mobileNo = object.getString("mobileNo");
                            Log.e("lmkdfkj",mobileNo+"");
                             email = object.getString("email");
                            String nearBy = object.getString("nearBy");
                            String district = object.getString("district");
                            String city = object.getString("city");
                            String location = object.getString("location");
                            String pin_code = object.getString("pin_code");
                            String state = object.getString("state");
                            String country = object.getString("country");
                            String address = object.getString("address");
                            String latitude = object.getString("latitude");
                            String longitute = object.getString("longitute");

                            addressList.add(new ModelAddress(address_id, title,address,latitude,longitute));

                            addressBottomAddress = new BottomAddressList(ActivityOrderReview.this,addressList );

                            rv_address_list.hasFixedSize();

                            rv_address_list.setLayoutManager(new LinearLayoutManager(ActivityOrderReview.this, RecyclerView.VERTICAL, false));

                            rv_address_list.setAdapter(addressBottomAddress);
                            addressBottomAddress.notifyDataSetChanged();

                        }

                    }
                    else {
                        Toast.makeText(ActivityOrderReview.this, "No address found...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_address.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                Log.e("ponse123", mUserID+"" );
                return params;
            }
        };

        RequestQueue requestQueue = SingletonVolley.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    public void loadOrderReviewData() {

        listReview.clear();
       StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.REVIEW_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbOrder_review.setVisibility(View.GONE);


                Log.e("saddsadjhksadhuisadh", response + "");

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    String total_cart_price = jsonObject.getString("total_cart_price");
                    String Delivery_charge = jsonObject.getString("Delivery_charge");
                    Total_bag_price = jsonObject.getString("Total_bag_price");
                    Tax_Charges=jsonObject.getString("Tax_Charges");
                    tv_subtotal_amount.setText(getString(R.string.rs)+total_cart_price);
                    tv_delivery_charge.setText(getString(R.string.rs)+Delivery_charge);
                    tv_to_pay.setText(getString(R.string.rs)+Total_bag_price);
                    tvTotalOrderAmount.setText(getString(R.string.rs)+Total_bag_price);
                    tv_textCharges.setText(getString(R.string.rs)+Tax_Charges);


                    if (status.equals("1")) {
                        rvOrderReview.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject data = jsonArray.getJSONObject(i);
                            String cart_id = data.getString("cart_id");
                            products_name = data.getString("products_name");
                            String price = data.getString("price");
                            String quantity = data.getString("quantity");
                            String image = data.getString("image");

                            listReview.add(new ModelReviewOrder(cart_id, products_name, price, quantity, image));


                        }
                        adapterReviewOrder = new AdapterReviewOrder(listReview, ActivityOrderReview.this);
                        rvOrderReview.hasFixedSize();
                        rvOrderReview.setNestedScrollingEnabled(false);
                        rvOrderReview.setLayoutManager(new LinearLayoutManager(ActivityOrderReview.this, RecyclerView.VERTICAL, false));
                        rvOrderReview.setAdapter(adapterReviewOrder);
                        adapterReviewOrder.notifyDataSetChanged();

                    } else {
                        rvOrderReview.setVisibility(View.GONE);
                        Toast.makeText(ActivityOrderReview.this, "No data", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbOrder_review.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                Log.e("klkjkljkljdjsalk", mUserID + "");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityOrderReview.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public BroadcastReceiver UpdateCart=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("Update_Cart")){
                if (intent.hasExtra("update")){
                    if (intent.getStringExtra("update").equalsIgnoreCase("update_now")){
                        Log.e("total_balance_updated","yesss");
                        loadOrderReviewData();
                    }

                }


            }


        }
    };


    public BroadcastReceiver Update_Address=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("Update_Address")){
                if (intent.hasExtra(Common.address)){

                    LatitudeCode= intent.getStringExtra(Common.latitude);
                    LogitudeCode= intent.getStringExtra(Common.logitude);

                    Log.e("NHBGG",LatitudeCode+"");
                    Log.e("NHBGG456",LogitudeCode+"");
                    Log.e("total_balance_updated","addresscalled");
                    if (change_address_dialog!=null){
                        if (change_address_dialog.isShowing()){
                            change_address_dialog.dismiss();
                        }
                    }
                    LL_Address.setVisibility(View.GONE);
                    LL_Bottom.setVisibility(View.VISIBLE);
                    address=intent.getStringExtra(Common.address);
                    address_id=intent.getStringExtra(Common.address_id);
                    tv_address.setText(address);
                }else{
                    Log.e("total_balance_updated","addresscalled34");

                    LL_Bottom.setVisibility(View.GONE);
                    LL_Address.setVisibility(View.VISIBLE);
                }
            }

        }
    };
    @Override
    protected void onResume() {
        super.onResume();

    }
}
