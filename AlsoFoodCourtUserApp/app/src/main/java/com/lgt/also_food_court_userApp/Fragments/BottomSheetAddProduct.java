package com.lgt.also_food_court_userApp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.also_food_court_userApp.Activities.RestaurantDescription;
import com.lgt.also_food_court_userApp.Adapters.AdapterBottomSheetAddProduct;
import com.lgt.also_food_court_userApp.Models.modelBottomSheetAddProduct;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.Common;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lgt.also_food_court_userApp.Adapters.AdapterBottomSheetAddProduct.ProductAmount;
import static com.lgt.also_food_court_userApp.Adapters.AdapterBottomSheetAddProduct.ProductQuantityAmount;


public class BottomSheetAddProduct extends BottomSheetDialogFragment {

    RestaurantDescription restaurantDescription;

    private RecyclerView rv_ProductQuantityData;
    private AdapterBottomSheetAddProduct adapterBottomSheetAddProduct;
    private List<modelBottomSheetAddProduct>ListBottomSheetAddProduct=new ArrayList<>();




    private String mProductID, mProductName,TotalPrice;

    private String tbl_restaurant_products_id, tbl_restaurant_id, price_type, piece_sprice, mUserID,mProduct_Image;
    public static TextView tvPerPiecePriceBottomSheet;
    private TextView tvFullPrice, tvHalfPrice, tvQuarterPrice, tvNameBtmSheet, tvAddToCartBottomSheet;
    private LinearLayout llFullBtmSheet, llHalfBtmSheet, llQuarterBtmSheet, llShowIfSizeBtmSheet, llShowIfPieceBtmSheet;

    private static final String TAG = "BottomSheetAddProduct";
    public static String selectedItem = "";
    private ImageView ivVegNonVegBottomSheet,iv_productImageImage;

    private CheckBox chkFull, chkHalf, chkQuarter;
    private String mPrice = "";

    private ProgressBar pbBottomSheetAddToCart;

    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.bottom_sheet_add_product, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);

        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);



        rv_ProductQuantityData=view.findViewById(R.id.rv_ProductQuantityData);

        tvNameBtmSheet = view.findViewById(R.id.tvNameBtmSheet);
        tvPerPiecePriceBottomSheet = view.findViewById(R.id.tvPerPiecePriceBottomSheet);
        TotalPrice=tvPerPiecePriceBottomSheet.toString();

        Log.e("kdfkhhg",TotalPrice+"");
        pbBottomSheetAddToCart = view.findViewById(R.id.pbBottomSheetAddToCart);
        iv_productImageImage=view.findViewById(R.id.iv_productImageImage);


        ivVegNonVegBottomSheet = view.findViewById(R.id.ivVegNonVegBottomSheet);
        tvAddToCartBottomSheet = view.findViewById(R.id.tvAddToCartBottomSheet);


        if (sharedPreferences.contains("user_id")) {
            mUserID = sharedPreferences.getString("user_id", "");
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("KEY_PRODUCT_ID")) {
                mProductID = bundle.getString("KEY_PRODUCT_ID");
                Log.e("dlfkkkk",mProductID+"");
                if (bundle.containsKey("KEY_PRODUCT_NAME")) {
                    mProductName = bundle.getString("KEY_PRODUCT_NAME");
                }
                if (bundle.containsKey("Product_Image")){
                    mProduct_Image=bundle.getString("Product_Image");
                }

                apiGetProductData();

            }
        }

        tvAddToCartBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                apiAddToCart();

            }
        });

        return view;
    }

    private void apiAddToCart() {

        pbBottomSheetAddToCart.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ADD_TO_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbBottomSheetAddToCart.setVisibility(View.GONE);
                Log.e("fdgfdggttrgtrgdesadsa", response + "");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {


                         dismiss();

                         updateCart();
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
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
                pbBottomSheetAddToCart.setVisibility(View.GONE);

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                params.put("tbl_restaurant_products_id", tbl_restaurant_products_id);
                params.put("tbl_restaurant_id", tbl_restaurant_id);
                params.put("price", ProductAmount);
                params.put("quantity", "1");
                params.put("price_type", selectedItem);
                Log.e("1254dlfkj", ProductAmount + "");
                Log.e("mymyparammkmkmk", params + "");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void updateCart() {
        Intent intent=new Intent(Common.UPDATE_CART);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).sendBroadcast(intent);
    }

    private void apiGetProductData() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.SINGLE_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("dnfjkghj",response+"");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {

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

                            String brand_name = data.getString("brand_name");
                            String Status = data.getString("status");

                            JSONArray jsonArrayPrice=data.getJSONArray("product_price");
                            if (jsonArrayPrice.length()>0){
                                for (int k=0;k<jsonArrayPrice.length();k++){
                                    JSONObject jsonObjectData=jsonArrayPrice.getJSONObject(k);
                                    String tbl_products_attribute_id=jsonObjectData.getString("tbl_products_attribute_id");
                                    String price_type=jsonObjectData.getString("price_type");
                                    String main_price=jsonObjectData.getString("main_price");
                                    String sale_price=jsonObjectData.getString("sale_price");
                                    String discount=jsonObjectData.getString("tbl_products_attribute_id");


                                    ListBottomSheetAddProduct.add(new modelBottomSheetAddProduct(tbl_products_attribute_id,price_type,sale_price,main_price,false));
                                }

                                adapterBottomSheetAddProduct =new AdapterBottomSheetAddProduct(ListBottomSheetAddProduct,getActivity());

                                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                                rv_ProductQuantityData.setLayoutManager(linearLayoutManager);
                                rv_ProductQuantityData.setHasFixedSize(true);
                                rv_ProductQuantityData.setAdapter(adapterBottomSheetAddProduct);



                            }

                            tvNameBtmSheet.setText(name);
                            /*tvPerPiecePriceBottomSheet.setText(ProductQuantityAmount);
                            Log.e("fhdvf",tvPerPiecePriceBottomSheet.toString()+"");*/
                            Glide.with(getActivity()).load(image).into(iv_productImageImage);
                            Log.e(TAG, "onResponse:priceijceijci " + price_type);

                            if (!product_type.equalsIgnoreCase("Veg")) {
                                ivVegNonVegBottomSheet.setImageDrawable(getActivity().getDrawable(R.drawable.non_veg_icon));
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tbl_restaurant_products_id", mProductID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


}
