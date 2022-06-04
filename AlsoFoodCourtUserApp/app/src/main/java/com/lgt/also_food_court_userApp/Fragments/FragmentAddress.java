package com.lgt.also_food_court_userApp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.lgt.also_food_court_userApp.Activities.Add_AddressActivity;
import com.lgt.also_food_court_userApp.Models.ModelAddress;
import com.lgt.also_food_court_userApp.Activities.ActivityOrderReview;
import com.lgt.also_food_court_userApp.Adapters.AdapterAddress;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class FragmentAddress extends Fragment implements AdapterAddress.interfaceAddID {

    private List<ModelAddress> listAddress;
    private AdapterAddress adapterAddress;
    private RecyclerView rv_delivery_address;

    private ImageView iv_back_ordering;
    private RelativeLayout rl_checkOut;

    private SharedPreferences sharedPreferences;
    private static String mUserID = "";

    private ProgressBar pbAddress;

    private FloatingActionButton fab_addNewAddress;
    private TextView tv_cartAmount;

    private static final String TAG = "FragmentAddress";
    public static String mAddID;

    ActivityOrderReview orderReview;

    public FragmentAddress() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fargment_address, container, false);

        rv_delivery_address = view.findViewById(R.id.rv_delivery_address);
        iv_back_ordering = view.findViewById(R.id.iv_back_ordering);
        pbAddress = view.findViewById(R.id.pbAddress);
        fab_addNewAddress = view.findViewById(R.id.fab_addNewAddress);
        tv_cartAmount = view.findViewById(R.id.tv_cartAmount);

        mAddID = null;

        orderReview = new ActivityOrderReview();

        if(ActivityOrderReview.Total_bag_price !=null){
            if(!ActivityOrderReview.Total_bag_price.equals("")){
                tv_cartAmount.setText(ActivityOrderReview.Total_bag_price);
            }
        }

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("user_id")) {
            mUserID = sharedPreferences.getString("user_id", "");
            Log.e("dsadjjuseridddd", mUserID);
        }



        iv_back_ordering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager=getFragmentManager();
                fragmentManager.popBackStack("FRAGMENT_PROFILE",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        fab_addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameOrderReview, new FragmentAddAddress()).addToBackStack("FragmentAddAddress").commit();
      */

                Intent intent=new Intent(getActivity(), Add_AddressActivity.class);
                startActivity(intent);



            }
        });

        loadAddress();

        return view;
    }

    public void loadAddress() {

        listAddress = new ArrayList<>();

        if (listAddress.size() > 0) {
            listAddress.clear();
        }
        pbAddress.setVisibility(View.VISIBLE);

        listAddress = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ADDRESS_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                listAddress.clear();

                pbAddress.setVisibility(View.GONE);

                Log.e(TAG, "onResponse: "+response );

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            String address_id = object.getString("address_id");
                            String title = object.getString("title");
                            String name = object.getString("name");
                            String houseNo = object.getString("houseNo");
                            String mobileNo = object.getString("mobileNo");
                            String email = object.getString("email");
                            String nearBy = object.getString("nearBy");
                            String district = object.getString("district");
                            String city = object.getString("city");
                            String location = object.getString("location");
                            String pin_code = object.getString("pin_code");
                            String state = object.getString("state");
                            String country = object.getString("country");
                            String address = object.getString("address");
                            String longitute = object.getString("longitute");
                            String latitude = object.getString("latitude");

                            listAddress.add(new ModelAddress(address_id,title, address,latitude,longitute));

                            adapterAddress = new AdapterAddress(listAddress, getActivity(),FragmentAddress.this,FragmentAddress.this);

                            rv_delivery_address.hasFixedSize();
                            rv_delivery_address.setNestedScrollingEnabled(false);
                            rv_delivery_address.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

                            rv_delivery_address.setAdapter(adapterAddress);
                            adapterAddress.notifyDataSetChanged();

                        }

                    }
                    else {
                        Toast.makeText(getActivity(), "No address found...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pbAddress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);

    }

    @Override
    public void getAddID(String addressID) {
        Log.e("adddididididididid",addressID+"");
        if(addressID!=null){
            if(!addressID.equals("")){
                mAddID = addressID;
            }
        }
    }
}
