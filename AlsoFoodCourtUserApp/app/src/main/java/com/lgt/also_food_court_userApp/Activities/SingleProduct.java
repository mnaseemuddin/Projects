package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import com.bumptech.glide.Glide;
import com.lgt.also_food_court_userApp.Fragments.FragmentCart;

import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SingleProduct extends AppCompatActivity {

    private TextView tvToolbar, tvVegOrNonVeg, tvSinglePrice, tvFullPrice, tvHalfPrice, tvQuarterPrice, tvSize, tvAmountToPay, tvCartCount, tvNoItems;
    private static String mTblProductID = "";
    private static String mProductName = "";

    private LinearLayout llSizeOption, llAddToCart, llSelectedSize, llFull, llHalf, llQuarter, llShowIfPiece, llCart;
    private ImageView ivSingleVegOrNon, ivSingleProduct, iv_backToolbar;

    private String mPrice = "";
    private String mUserID;
    private String selectedItem = "";

    private SharedPreferences sharedPreferences;

    private String selectedProducts = "";


    private String tbl_restaurant_products_id, tbl_restaurant_id, price_type, piece_sprice;

    private ProgressBar pbSingleProduct;
    private FrameLayout frameSingleProduct;


    @Override
    protected void onResume() {
        super.onResume();
        getCartItems();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")) {
            mUserID = sharedPreferences.getString("user_id", "");
        }

        tvToolbar = findViewById(R.id.tvToolbar);
        tvVegOrNonVeg = findViewById(R.id.tvVegOrNonVeg);
        tvSinglePrice = findViewById(R.id.tvSinglePrice);
        llSizeOption = findViewById(R.id.llSizeOption);
        llAddToCart = findViewById(R.id.llAddToCart);
        llSelectedSize = findViewById(R.id.llSelectedSize);
        tvSize = findViewById(R.id.tvSize);
        tvAmountToPay = findViewById(R.id.tvAmountToPay);
        tvCartCount = findViewById(R.id.tvCartCount);
        tvNoItems = findViewById(R.id.tvNoItems);
        iv_backToolbar = findViewById(R.id.iv_backToolbar);

        pbSingleProduct = findViewById(R.id.pbSingleProduct);
        frameSingleProduct = findViewById(R.id.frameSingleProduct);

        final Drawable bgSelected = getDrawable(R.drawable.bg_selected_size);


        llFull = findViewById(R.id.llFull);
        llHalf = findViewById(R.id.llHalf);
        llQuarter = findViewById(R.id.llQuarter);
        llShowIfPiece = findViewById(R.id.llShowIfPiece);
        llCart = findViewById(R.id.llCart);

        ivSingleVegOrNon = findViewById(R.id.ivSingleVegOrNon);
        ivSingleProduct = findViewById(R.id.ivSingleProduct);

        tvFullPrice = findViewById(R.id.tvFullPrice);
        tvHalfPrice = findViewById(R.id.tvHalfPrice);
        tvQuarterPrice = findViewById(R.id.tvQuarterPrice);

        iv_backToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        llFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelectedSize.setVisibility(View.VISIBLE);
                llFull.setBackground(bgSelected);
                llHalf.setBackgroundResource(0);
                llQuarter.setBackgroundResource(0);

                selectedProducts = "Full selected";
                selectedItem = "full_sprice";
                mPrice = tvFullPrice.getText().toString().trim();
                tvAmountToPay.setText(mPrice);
                tvSize.setText(selectedProducts);
            }
        });

        llHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelectedSize.setVisibility(View.VISIBLE);
                selectedProducts = "Half selected";
                llHalf.setBackground(bgSelected);
                llFull.setBackgroundResource(0);
                llQuarter.setBackgroundResource(0);
                mPrice = tvHalfPrice.getText().toString().trim();
                tvAmountToPay.setText(mPrice);
                selectedItem = "half_sprice";
                tvSize.setText(selectedProducts);
            }
        });

        llQuarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSelectedSize.setVisibility(View.VISIBLE);
                selectedProducts = "Quarter selected";
                llQuarter.setBackground(bgSelected);
                llFull.setBackgroundResource(0);
                llHalf.setBackgroundResource(0);
                tvAmountToPay.setText(mPrice);
                selectedItem = "quarter_sprice";
                mPrice = tvQuarterPrice.getText().toString().trim();
                tvSize.setText(selectedProducts);
            }
        });

        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameSingleProduct, new FragmentCart()).addToBackStack("").commit();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("KEY_PRODUCT_ID")) {
                mTblProductID = intent.getStringExtra("KEY_PRODUCT_ID");
                mProductName = intent.getStringExtra("KEY_PRODUCT_NAME");

                tvToolbar.setText(mProductName);
                Log.e("tblidddddd", mTblProductID + "");

                apiSingleProduct();

            }
        }

        getCartItems();

        llAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check for selected food, then call api

                if (price_type.equalsIgnoreCase("Size")) {


                    if (selectedProducts.equals("")) {
                        Toast.makeText(SingleProduct.this, "Please select one of products", Toast.LENGTH_SHORT).show();
                    } else {
                        mPrice = tvAmountToPay.getText().toString().trim();
                        addToCartAPI();
                        Toast.makeText(SingleProduct.this, "API CALL", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mPrice = piece_sprice;
                    selectedItem = "piece_sprice";


                    addToCartAPI();
                }


            }
        });
    }

    private void getCartItems() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.GET_CART_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbSingleProduct.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {

                        llCart.setVisibility(View.VISIBLE);


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            tvCartCount.setText(String.valueOf(jsonArray.length()));

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SingleProduct.this);
        requestQueue.add(stringRequest);
    }

    private void addToCartAPI() {

        pbSingleProduct.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("SADsadsasauiuiuiu", response + "");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {
                        getCartItems();
                        Toast.makeText(SingleProduct.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        pbSingleProduct.setVisibility(View.GONE);
                        Toast.makeText(SingleProduct.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("dsadarewwerwer", e.getMessage() + "");
                }

                Log.e("dsadsadsahjhkkj", response + "");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbSingleProduct.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                params.put("tbl_restaurant_products_id", tbl_restaurant_products_id);
                params.put("tbl_restaurant_id", tbl_restaurant_id);
                params.put("price", mPrice);
                params.put("quantity", "1");
                params.put("price_type", selectedItem);

                Log.e("dsadsadsad", params + "");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SingleProduct.this);
        requestQueue.add(stringRequest);

    }

    private void apiSingleProduct() {

        pbSingleProduct.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.SINGLE_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbSingleProduct.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {

                        frameSingleProduct.setVisibility(View.VISIBLE);


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject data = jsonArray.getJSONObject(i);
                            tbl_restaurant_products_id = data.getString("tbl_restaurant_products_id");
                            tbl_restaurant_id = data.getString("tbl_restaurant_id");
                            String name = data.getString("name");
                            String category = data.getString("category");
                            String product_type = data.getString("product_type");
                            String image = data.getString("image");
                            price_type = data.getString("price_type");
                            String quarter_price = data.getString("quarter_price");
                            String quarter_sprice = data.getString("quarter_sprice");
                            String quarter_discount = data.getString("quarter_discount");
                            String half_price = data.getString("half_price");
                            String half_sprice = data.getString("half_sprice");
                            String half_discount = data.getString("half_discount");
                            String full_price = data.getString("full_price");
                            String full_sprice = data.getString("full_sprice");
                            String full_discount = data.getString("full_discount");
                            String piece_price = data.getString("piece_price");
                            piece_sprice = data.getString("piece_sprice");
                            String piece_discount = data.getString("piece_discount");

                            tvSinglePrice.setText(piece_sprice);


                            if(!full_sprice.equals("")){
                                if(!half_sprice.equals("")){
                                    if(!quarter_sprice.equals("")){
                                        tvFullPrice.setText(full_sprice);
                                        tvHalfPrice.setText(half_sprice);
                                        tvQuarterPrice.setText(quarter_sprice);
                                        llSizeOption.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                            if(!full_sprice.equals("")){
                                if(!half_sprice.equals("")){
                                    if(quarter_sprice.equals("")){
                                        tvFullPrice.setText(full_sprice);
                                        tvHalfPrice.setText(half_sprice);
                                        llSizeOption.setVisibility(View.VISIBLE);
                                        llQuarter.setVisibility(View.GONE);
                                    }
                                }
                            }

                            if(!full_sprice.equals("")){
                                if(half_sprice.equals("")){
                                    if(!quarter_sprice.equals("")){
                                        tvFullPrice.setText(full_sprice);
                                        llSizeOption.setVisibility(View.VISIBLE);
                                        llHalf.setVisibility(View.GONE);
                                        llQuarter.setVisibility(View.GONE);
                                    }
                                }
                            }

                            if(!full_sprice.equals("")){
                                if(half_sprice.equals("")){
                                    if(quarter_sprice.equals("")){
                                        tvFullPrice.setText(full_sprice);

                                        llSizeOption.setVisibility(View.VISIBLE);
                                        llHalf.setVisibility(View.GONE);
                                        llQuarter.setVisibility(View.GONE);
                                    }
                                }
                            }

                            if(full_sprice.equals("")){
                                if(!half_sprice.equals("")){
                                    if(!quarter_sprice.equals("")){
                                        tvHalfPrice.setText(half_sprice);
                                        tvQuarterPrice.setText(quarter_sprice);
                                        llSizeOption.setVisibility(View.VISIBLE);
                                        llFull.setVisibility(View.GONE);
                                    }
                                }
                            }

                            if(full_sprice.equals("")){
                                if(!half_sprice.equals("")){
                                    if(quarter_sprice.equals("")){
                                        tvHalfPrice.setText(half_sprice);
                                        llSizeOption.setVisibility(View.VISIBLE);
                                        llFull.setVisibility(View.GONE);
                                        llQuarter.setVisibility(View.GONE);
                                    }
                                }
                            }

                            if(full_sprice.equals("")){
                                if(half_sprice.equals("")){
                                    if(!quarter_sprice.equals("")){
                                        tvQuarterPrice.setText(quarter_sprice);
                                        llSizeOption.setVisibility(View.VISIBLE);
                                        llFull.setVisibility(View.GONE);
                                        llHalf.setVisibility(View.GONE);
                                    }
                                }
                            }



                            if (price_type.equalsIgnoreCase("Size")) {

                                tvFullPrice.setText(full_sprice);
                                tvHalfPrice.setText(half_sprice);
                                tvQuarterPrice.setText(quarter_sprice);

                                llSizeOption.setVisibility(View.VISIBLE);
                            } else {
                                llShowIfPiece.setVisibility(View.VISIBLE);
                            }

                            if (product_type.equalsIgnoreCase("Veg")) {
                                ivSingleVegOrNon.setImageDrawable(getDrawable(R.drawable.veg));
                                tvVegOrNonVeg.setText(product_type);
                            }

                            if (!image.equals("")) {
                                Glide.with(SingleProduct.this).load(image).into(ivSingleProduct);
                            }


                        }
                    } else {
                        tvNoItems.setVisibility(View.VISIBLE);
                        frameSingleProduct.setVisibility(View.GONE);
                        Toast.makeText(SingleProduct.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbSingleProduct.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tbl_restaurant_products_id", mTblProductID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SingleProduct.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
