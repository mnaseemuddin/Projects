package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.lgt.also_food_court_userApp.Adapters.AdapterProductReview;
import com.lgt.also_food_court_userApp.Adapters.AdapterSingleProductQuantity;
import com.lgt.also_food_court_userApp.Fragments.BottomSheetAddProduct;
import com.lgt.also_food_court_userApp.Models.ModelReviewProduct;
import com.lgt.also_food_court_userApp.Models.modelSingleProductDetailsQty;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleProductDetails extends AppCompatActivity {
    private ImageView iv_backArrowSingleProduct,iv_productImage;
    private TextView tv_ProductName,tv_productDiscounted,tv_PiecePrice,tv_noReviewProduct,
    tvFullPrice,tvHalfPrice,tvQuarterPrice;
    private Button btn_AddProduct;

    private LinearLayout llFull,llHalf,llQuarter,llSizeOption,ll_offer;

    private RecyclerView rv_ReviewProduct;
    private AdapterProductReview adapterProductReview;
    private List<ModelReviewProduct>listProductReview=new ArrayList<>();
    private int i,j;

    private SharedPreferences sharedPreferences;


   private RecyclerView rv_itemQuantity;
   private AdapterSingleProductQuantity adapterSingleProductQuantity;
   private List<modelSingleProductDetailsQty>qtyList=new ArrayList<>();



    private String ProductId,ProductOffer,mUserID,products_name,productImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_details);

        sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")) {
            mUserID = sharedPreferences.getString("user_id", "");
            Log.e("NNBVVB",mUserID+"");
        }

        Intent intent=getIntent();
        if (intent!=null){

            ProductId=intent.getStringExtra("Products_id");
            ProductOffer=intent.getStringExtra("Products_Offer");


            Log.e("AZSDXC",ProductId+"");
        }

        initilize();
        LoadApiDataSingleProduct();

    }


    private void initilize() {
        rv_itemQuantity=findViewById(R.id.rv_itemQuantity);
        ll_offer=findViewById(R.id.ll_offer);
        llFull=findViewById(R.id.llFull);
        llHalf=findViewById(R.id.llHalf);
        llQuarter=findViewById(R.id.llQuarter);
        rv_ReviewProduct=findViewById(R.id.rv_ReviewProduct);
        iv_backArrowSingleProduct=findViewById(R.id.iv_backArrowSingleProduct);
        iv_productImage=findViewById(R.id.iv_productImage);
        tv_ProductName=findViewById(R.id.tv_ProductName);
        tv_productDiscounted=findViewById(R.id.tv_productDiscounted);
        tv_PiecePrice=findViewById(R.id.tv_PiecePrice);
        btn_AddProduct=findViewById(R.id.btn_AddProduct);
        tv_noReviewProduct=findViewById(R.id.tv_noReviewProduct);
        tvFullPrice=findViewById(R.id.tvFullPrice);
        tvHalfPrice=findViewById(R.id.tvHalfPrice);
        tvQuarterPrice=findViewById(R.id.tvQuarterPrice);
        llSizeOption=findViewById(R.id.llSizeOption);





        iv_backArrowSingleProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


        btn_AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // AddToCardApiData();

                BottomSheetAddProduct bottomSheetAddProduct = new BottomSheetAddProduct();
                Bundle bundle = new Bundle();
                bundle.putString("KEY_PRODUCT_ID",ProductId);
                bundle.putString("KEY_PRODUCT_NAME",products_name);
                bundle.putString("Product_Image",productImage);
                Log.e("hbgvfcd",products_name+""+ProductId);
                bottomSheetAddProduct.setArguments(bundle);
                bottomSheetAddProduct.show(getSupportFragmentManager(), String.valueOf(getApplicationContext()));
            }
        });

    }




    private void LoadApiDataSingleProduct() {
        qtyList.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AlsoFoodCourtApi.SingleProductDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("HBGVFD",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (i=0;i<jsonArray.length();i++){
                            JSONObject dataObject=jsonArray.getJSONObject(i);
                            String tbl_restaurant_products_id=dataObject.getString("tbl_restaurant_products_id");
                            products_name=dataObject.getString("products_name");
                            String category=dataObject.getString("category");
                            String product_type=dataObject.getString("product_type");
                            productImage=dataObject.getString("image");

                            JSONArray jsonArrayPrice=dataObject.getJSONArray("price_details");
                            if (jsonArrayPrice.length()>0){
                                for (int l=0;l<jsonArrayPrice.length();l++){
                                    JSONObject jsonObjectDAta=jsonArrayPrice.getJSONObject(l);
                                    String tbl_products_attribute_id=jsonObjectDAta.getString("tbl_products_attribute_id");
                                    String price_type=jsonObjectDAta.getString("price_type");
                                    String main_price=jsonObjectDAta.getString("main_price");
                                    String sale_price=jsonObjectDAta.getString("sale_price");
                                    String discount=jsonObjectDAta.getString("discount");


                                    qtyList.add(new modelSingleProductDetailsQty(price_type,sale_price));
                                }


                                adapterSingleProductQuantity=new AdapterSingleProductQuantity(qtyList, getApplicationContext());
                                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                                rv_itemQuantity.setHasFixedSize(true);
                                rv_itemQuantity.setLayoutManager(linearLayoutManager);
                                rv_itemQuantity.setAdapter(adapterSingleProductQuantity);
                                adapterSingleProductQuantity.notifyDataSetChanged();
                            }





                            Glide.with(getApplicationContext()).load(productImage).into(iv_productImage);
                            tv_ProductName.setText(products_name);




                           JSONArray jsonArrayReview=dataObject.getJSONArray("product_reviews");
                            if (jsonArrayReview.length()>0){
                                 for (j=0;j<jsonArrayReview.length();j++){
                                      JSONObject jsonObjectReview=jsonArrayReview.getJSONObject(j);
                                        String User_name=jsonObjectReview.getString("user_nema");
                                        String review=jsonObjectReview.getString("review");
                                        String rating=jsonObjectReview.getString("rating");
                                        String Time=jsonObjectReview.getString("created_date");


                                        listProductReview.add(new ModelReviewProduct(User_name,Time,review,rating));
                                        adapterProductReview=new AdapterProductReview(listProductReview, getApplicationContext());
                                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                                        rv_ReviewProduct.setHasFixedSize(true);
                                        rv_ReviewProduct.setLayoutManager(linearLayoutManager);
                                        rv_ReviewProduct.setAdapter(adapterProductReview);
                                        adapterProductReview.notifyDataSetChanged();

                                }

                            }else{
                                tv_noReviewProduct.setVisibility(View.VISIBLE);
                                rv_ReviewProduct.setVisibility(View.GONE);
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
                params.put("products_id",ProductId);
                Log.e("HBGVFD",ProductId+"");
                return params;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
