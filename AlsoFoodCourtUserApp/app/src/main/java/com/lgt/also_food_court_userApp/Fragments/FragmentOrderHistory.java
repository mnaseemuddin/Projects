package com.lgt.also_food_court_userApp.Fragments;


import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.lgt.also_food_court_userApp.Adapters.AdapterOrderHistory;
import com.lgt.also_food_court_userApp.Models.ModelOrderHistory;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.android.volley.VolleyLog.TAG;


public class FragmentOrderHistory extends Fragment {

    private ProgressBar pbOrderHistory;
    private RelativeLayout Rl_Toolbar;
    private TextView tvToolbar;
    private ImageView iv_backToolbar ;


    private RecyclerView rvOrderHistory;
    private List<ModelOrderHistory> listOrderHistory;
    private AdapterOrderHistory adapterOrderHistory;

    private String mUserID;
    private SharedPreferences sharedPreferences;

    public FragmentOrderHistory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("user_id")){
            mUserID = sharedPreferences.getString("user_id","");

        }
        Rl_Toolbar=view.findViewById(R.id.Rl_Toolbar);
        pbOrderHistory = view.findViewById(R.id.pbOrderHistory);
        rvOrderHistory = view.findViewById(R.id.rvOrderHistory);
        tvToolbar = view.findViewById(R.id.tvToolbar);
        iv_backToolbar = view.findViewById(R.id.iv_backToolbar);

        tvToolbar.setText("Order History");
        iv_backToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               FragmentManager fragmentManager = getFragmentManager();
               fragmentManager.popBackStack("FRAGMENT_PROFILE",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        /*Rl_Toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack("FRAGMENT_PROFILE",FragmentManager.POP_BACK_STACK_INCLUSIVE);*//*
                getActivity().finish();
            }
        });*/

        loadOrderData();

        return view;
    }

    private void loadOrderData() {

        pbOrderHistory.setVisibility(View.VISIBLE);

        listOrderHistory = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ORDER_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                listOrderHistory.clear();
                pbOrderHistory.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.e(TAG, "onResponseOrderHistory: "+response );

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if(status.equals("1")){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject data = jsonArray.getJSONObject(i);

                            String restaurant_name = data.getString("restaurant_name");
                            String tbl_orders_id=data.getString("tbl_orders_id");
                            String order_number = data.getString("order_number");
                            String quantity = data.getString("quantity");
                            String total_price = data.getString("total_price");
                          //  String products_name = data.getString("products_name");
                           // String Size = data.getString("Size");
                            String order_date = data.getString("order_date");
                            String order_status = data.getString("order_status");
                            String restaurant_image = data.getString("restaurant_image");

                            listOrderHistory.add(new ModelOrderHistory(tbl_orders_id,order_number,quantity,total_price,restaurant_name,"Size",order_date,order_status,restaurant_image));
                            Collections.reverse(listOrderHistory);



                        }

                        adapterOrderHistory = new AdapterOrderHistory(listOrderHistory,getActivity());
                        rvOrderHistory.hasFixedSize();
                        rvOrderHistory.setNestedScrollingEnabled(false);
                        rvOrderHistory.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                        rvOrderHistory.setAdapter(adapterOrderHistory);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbOrderHistory.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                Log.e(TAG, "getParamsOrderHistory: "+params);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);

    }

}
