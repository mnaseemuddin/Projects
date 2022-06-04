package com.lgt.also_food_court_userApp.Models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.also_food_court_userApp.Activities.MainActivity;
import com.lgt.also_food_court_userApp.Adapters.AdapterTopBrandListProduct;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopBrandAllProductListActivity extends AppCompatActivity {
     private RecyclerView rv_BrandProductList;
     private AdapterTopBrandListProduct adapterTopBrandListProduct;
     private List<ModelTopBrandList>listTopBrandItem=new ArrayList<>();

     private TextView tv_brandName;
     private String BrandName,BrandID;
     private ImageView iv_backArrowTopBrand;

     private LinearLayout ll_RestaurantListNoData;
     private ProgressBar pbTopBrandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_brand_all_product_list);


        tv_brandName=findViewById(R.id.tv_brandName);


        Intent intent=getIntent();

        if (intent.hasExtra("BRAND_NAME")){
            BrandName=intent.getStringExtra("BRAND_NAME");
            BrandID=intent.getStringExtra("Brand_ID");
        }else{
            tv_brandName.setText("TopBrandProduct");
        }


        Initialize();


    }

    private void Initialize() {

        rv_BrandProductList=findViewById(R.id.rv_BrandProductList);
        iv_backArrowTopBrand=findViewById(R.id.iv_backArrowTopBrand);
        ll_RestaurantListNoData=findViewById(R.id.ll_RestaurantListNoData);
        pbTopBrandList=findViewById(R.id.pbTopBrandList);


        iv_backArrowTopBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        tv_brandName.setText(BrandName);




        LoadTopBrandListItemData();


    }

    private void LoadTopBrandListItemData() {
        listTopBrandItem.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AlsoFoodCourtApi.Top_BrandProduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.e("jdfkgk",response+"");
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");

                    if (status.equals("1")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            if (jsonArray.length()>0){
                                JSONObject jsonObjectData=jsonArray.getJSONObject(i);
                                String product_id=jsonObjectData.getString("product_id");
                                String name=jsonObjectData.getString("name");
                                String category=jsonObjectData.getString("category");
                                String product_type=jsonObjectData.getString("product_type");
                                String image=jsonObjectData.getString("image");

                                listTopBrandItem.add(new ModelTopBrandList(product_id,name,image));



                            }
                            adapterTopBrandListProduct=new AdapterTopBrandListProduct(listTopBrandItem,getApplicationContext());
                            GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
                            rv_BrandProductList.setHasFixedSize(true);
                            rv_BrandProductList.setLayoutManager(gridLayoutManager);
                            rv_BrandProductList.setAdapter(adapterTopBrandListProduct);
                            adapterTopBrandListProduct.notifyDataSetChanged();
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
                params.put("tbl_brand_id",BrandID);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);




    }
}
