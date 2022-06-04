package com.lgt.also_food_court_userApp.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.lgt.also_food_court_userApp.Activities.ActivityOrderReview;
import com.lgt.also_food_court_userApp.Adapters.AdapterCart;
import com.lgt.also_food_court_userApp.Models.ModelCart;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.extra.SingletonVolley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;




public class FragmentCart extends Fragment {

    List<ModelCart> listCart;
    AdapterCart adapterCart;
    RecyclerView rv_cart;
    RelativeLayout RL_Procced, rlCart;

    ImageView iv_back_cart;

    public static TextView tvNoItems,tv_subtotal_amount,tv_total_amount,tv_delivery_charge,tv_textCharges;

    private SharedPreferences sharedPreferences;
    private String mUserID = "";

    private ProgressBar pbCart;

    private TextView tvToolbar;


    public FragmentCart() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fargment_cart, container, false);

        Initialization(view);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(UpdateCart,new IntentFilter("Update_Cart"));

        updateTotalBalance();

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("user_id")) {
            mUserID = sharedPreferences.getString("user_id", "");
            Log.e("nmnmnnmnmn", mUserID + "");
        }

        iv_back_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack("Fragment_Cart", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        RL_Procced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ActivityOrderReview.class));

                /*FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, new FragmentAddress()).addToBackStack("Fragment_Address").commit();*/
            }
        });

        tvToolbar.setText("My Cart");

        loadCartItems();

        return view;
    }

    private void updateTotalBalance() {

        Intent intent=new Intent("Update_Cart");
        intent.putExtra("update","update_now");
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).sendBroadcast(intent);
    }

    private void Initialization(View view) {

        rv_cart = view.findViewById(R.id.rv_cart);
        iv_back_cart = view.findViewById(R.id.iv_backToolbar);
        RL_Procced = view.findViewById(R.id.RL_Procced);
        tvToolbar = view.findViewById(R.id.tvToolbar);
        rlCart = view.findViewById(R.id.rlCart);
        tvNoItems = view.findViewById(R.id.tvNoItems);
        pbCart = view.findViewById(R.id.pbCart);
        tv_subtotal_amount=view.findViewById(R.id.tv_subtotal_amount);
        tv_total_amount=view.findViewById(R.id.tv_total_amount);
        tv_delivery_charge=view.findViewById(R.id.tv_delivery_charge);
        tv_textCharges=view.findViewById(R.id.tv_textCharges);
    }

    public void loadCartItems() {

        listCart = new ArrayList<>();
        showProgressBar();

        if (listCart.size() > 0) {
            listCart.clear();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.GET_CART_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                listCart.clear();
                hideProgressBar();

                Log.e("dsahuhuhuh", response + "");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("1")) {

                        hideProgressBar();
                        rlCart.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject data = jsonArray.getJSONObject(i);
                            String cart_id = data.getString("cart_id");
                            String products_name = data.getString("products_name");
                            String price = data.getString("price");
                            String quantity = data.getString("quantity");
                            String image = data.getString("image");
                            String products_price = data.getString("products_price");

                            listCart.add(new ModelCart(cart_id, products_name, price, quantity, image, products_price));



                        }
                        adapterCart = new AdapterCart(listCart, getActivity(), FragmentCart.this);
                        rv_cart.setNestedScrollingEnabled(false);
                        rv_cart.hasFixedSize();
                        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        rv_cart.setAdapter(adapterCart);
                        adapterCart.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "No items in Cart", Toast.LENGTH_SHORT).show();
                        rlCart.setVisibility(View.GONE);
                        tvNoItems.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("klklklklkkl", response + "");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideProgressBar();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                return params;
            }
        };

        if (getActivity()!=null){
            RequestQueue requestQueue = SingletonVolley.getInstance(getActivity()).getRequestQueue();
            requestQueue.add(stringRequest);

        }


    }

    public void hideProgressBar() {
        if (pbCart.getVisibility() == View.VISIBLE) {
            pbCart.setVisibility(View.GONE);
        }
    }

    public void showProgressBar() {
        pbCart.setVisibility(View.VISIBLE);
    }

    public BroadcastReceiver UpdateCart=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("update")){
                if (intent.getStringExtra("update").equalsIgnoreCase("update_now")){
                    Log.e("total_balance_updated","yesss");
                    getCart();
                }

            }
        }
    };

    private void getCart() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.REVIEW_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("saddsadjhksadhuisadh", response + "");
                Log.e("mhjshfsdjfhdfjfd", "methodcalalled: " );

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {

                       String total_cart_price = jsonObject.getString("total_cart_price");
                        String  Delivery_charge = jsonObject.getString("Delivery_charge");
                        String Total_bag_price=jsonObject.getString("Total_bag_price");
                        String Tax_Charges=jsonObject.getString("Tax_Charges");
                        tv_subtotal_amount.setText(getString(R.string.rs)+total_cart_price);
                        tv_total_amount.setText(getString(R.string.rs)+Total_bag_price);
                         tv_delivery_charge.setText(getString(R.string.rs)+Delivery_charge);
                        tv_textCharges.setText(getString(R.string.rs)+Tax_Charges);

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
                Log.e("klkjkljkljdjsalk", mUserID + "");
                return params;
            }
        };
        if (getActivity()!=null){
            RequestQueue requestQueue = SingletonVolley.getInstance(getActivity()).getRequestQueue();
            requestQueue.add(stringRequest);

        }
    }
}
