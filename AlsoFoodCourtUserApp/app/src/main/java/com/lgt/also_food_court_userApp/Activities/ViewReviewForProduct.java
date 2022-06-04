package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.also_food_court_userApp.Adapters.AdapterProductReview;
import com.lgt.also_food_court_userApp.Models.ModelReviewProduct;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.extra.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewReviewForProduct extends AppCompatActivity {

    private RecyclerView rv_reviewList;
    private ImageView iv_backArrowReview;
    private AdapterProductReview adapterProductReview;
    private List<ModelReviewProduct>productReviewList=new ArrayList<>();
    private String productId;
    private TextView tv_noReviewProduct;

    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review_for_product);

        Intent intent=getIntent();
        if (intent!=null){
            if (intent.hasExtra(Common.tbl_restaurant_productId)){
                productId=intent.getStringExtra(Common.tbl_restaurant_productId);

                ReviewListApiData();

            }
        }

        initialize();
    }
    private void initialize() {
        tv_noReviewProduct=findViewById(R.id.tv_noReviewProduct);
        iv_backArrowReview=findViewById(R.id.iv_backArrowReview);
        rv_reviewList=findViewById(R.id.rv_reviewList);
        iv_backArrowReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void ReviewListApiData() {
        productReviewList.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ViewReviewForProduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("HBGVF",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                            for (i=0;i<jsonArray.length();i++){
                              JSONObject object=jsonArray.getJSONObject(i);
                                String User_name=object.getString("user_nema");
                                String review=object.getString("review");
                                String rating=object.getString("rating");


                                productReviewList.add(new ModelReviewProduct(User_name,"15-jun-2020 10:40 Am",review,rating));
                                 adapterProductReview=new AdapterProductReview(productReviewList, getApplicationContext());
                                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                                rv_reviewList.setHasFixedSize(true);
                                rv_reviewList.setLayoutManager(linearLayoutManager);
                                rv_reviewList.setAdapter(adapterProductReview);
                                adapterProductReview.notifyDataSetChanged();
                            }


                    }else{
                        rv_reviewList.setVisibility(View.GONE);
                        tv_noReviewProduct.setVisibility(View.VISIBLE);
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
                params.put("tbl_restaurant_products_id",productId);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}
