package com.lgt.also_food_court_userApp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.also_food_court_userApp.Adapters.AdapterRestaurantList;
import com.lgt.also_food_court_userApp.Models.ModelRestaurantList;

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


public class FragmentRestaurantList extends Fragment {


    private RecyclerView rvBannerData;
    private AdapterRestaurantList adapterRestaurantList;
    private List<ModelRestaurantList> list;

    private TextView tvToolbar,tvTotalCount;
    private ImageView iv_backToolbar;
    private ProgressBar pbRestaurantList;
    private FrameLayout frameRestaurantList;

    private static String mBannerPercent = "";


    private LinearLayout llTotalRestaurant,ll_RestaurantListNoData;

    public FragmentRestaurantList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        rvBannerData = view.findViewById(R.id.rvBannerData);
        tvToolbar = view.findViewById(R.id.tvToolbar);
        iv_backToolbar = view.findViewById(R.id.iv_backToolbar);
        tvTotalCount = view.findViewById(R.id.tvTotalCount);
        llTotalRestaurant = view.findViewById(R.id.llTotalRestaurant);
        pbRestaurantList = view.findViewById(R.id.pbRestaurantList);
        ll_RestaurantListNoData = view.findViewById(R.id.ll_RestaurantListNoData);
        frameRestaurantList = view.findViewById(R.id.frameRestaurantList);

        iv_backToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    fragmentManager.popBackStack("Fragment_Banner",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });

        Bundle bundle = getArguments();
        if(bundle!=null){
            if(bundle.containsKey("KEY_OFFER_PERCENT")){
                mBannerPercent = bundle.getString("KEY_OFFER_PERCENT");
                Log.e("jkljkljkljklj",mBannerPercent+"");
                loadRecyclerView(mBannerPercent);
            }
        }



        return view;

    }

    private void loadRecyclerView(final String mBannerPercent) {

        list = new ArrayList<>();

        pbRestaurantList.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.GET_RESTAURANT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("restusadtusadtasudsa",response+"");

                pbRestaurantList.setVisibility(View.GONE);

                list.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");



                    if (status.equals("1")) {

                        tvToolbar.setText(message);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject data = jsonArray.getJSONObject(i);

                            String tbl_restaurant_id = data.getString("tbl_restaurant_id");
                            String shop_name = data.getString("shop_name");
                            String category_name = data.getString("category_name");
                            String description = data.getString("description");
                            String address = data.getString("address");
                            String restaurant_image = data.getString("restaurant_image");


                           // list.add(new ModelRestaurantList(tbl_restaurant_id, shop_name, address, restaurant_image, "4.5", category_name, description));



                            if(restaurant_image!=null){
                                list.add(new ModelRestaurantList(tbl_restaurant_id, shop_name, address, restaurant_image, "4.5", category_name, description));

                            }

                        }
                        tvTotalCount.setText(String.valueOf(jsonArray.length()));
                        llTotalRestaurant.setVisibility(View.VISIBLE);
                        adapterRestaurantList = new AdapterRestaurantList(list, getActivity());
                        rvBannerData.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        rvBannerData.hasFixedSize();
                        rvBannerData.setNestedScrollingEnabled(false);
                        rvBannerData.setAdapter(adapterRestaurantList);
                        adapterRestaurantList.notifyDataSetChanged();

                    }
                    else {
                        tvToolbar.setText("No restaurant found");
                        frameRestaurantList.setVisibility(View.GONE);
                        ll_RestaurantListNoData.setVisibility(View.VISIBLE);
                        pbRestaurantList.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbRestaurantList.setVisibility(View.GONE);
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("offer",mBannerPercent);
                Log.e("kfdlghj",mBannerPercent+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);

    }

}
