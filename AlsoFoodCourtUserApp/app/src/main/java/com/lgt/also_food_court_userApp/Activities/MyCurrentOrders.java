package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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
import com.lgt.also_food_court_userApp.Adapters.AdapterMyCurrentOrder;
import com.lgt.also_food_court_userApp.Models.ModelMyCurrentOrder;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCurrentOrders extends AppCompatActivity {
    private ImageView iv_backArrow;
    private TextView tv_no_currentOrder;
    private RecyclerView rv_myCurrentOrder;
    private AdapterMyCurrentOrder adapterMyCurrentOrder;
    private List<ModelMyCurrentOrder>currentOrderList=new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private String User_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_current_orders);

        sharedPreferences=getSharedPreferences("USER_DATA",MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")){
            User_Id=sharedPreferences.getString("user_id","");
            Log.e("hgjsdg",User_Id+"");
        }

        initialize();
    }

    private void initialize() {
        rv_myCurrentOrder=findViewById(R.id.rv_myCurrentOrder);
        iv_backArrow=findViewById(R.id.iv_backArrow);
        tv_no_currentOrder=findViewById(R.id.tv_no_currentOrder);
        iv_backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        currentOrderApiData();
    }

    private void currentOrderApiData() {
        currentOrderList.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AlsoFoodCourtApi.CurrentOrderStatus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hjfksa",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String Status=jsonObject.getString("status");

                    if (Status.equalsIgnoreCase("1")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        if (jsonArray.length()>0){
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String tbl_orders_id=jsonObject1.getString("tbl_orders_id");
                                String order_number=jsonObject1.getString("order_number");
                                String total_price=jsonObject1.getString("total_price");
                                String quantity=jsonObject1.getString("quantity");
                                String image=jsonObject1.getString("image");
                                currentOrderList.add(new ModelMyCurrentOrder(tbl_orders_id,image,order_number,quantity,total_price));

                            }
                            adapterMyCurrentOrder=new AdapterMyCurrentOrder(currentOrderList,getApplicationContext());
                            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                            rv_myCurrentOrder.setHasFixedSize(true);
                            rv_myCurrentOrder.setLayoutManager(linearLayoutManager);
                            rv_myCurrentOrder.setAdapter(adapterMyCurrentOrder);

                            adapterMyCurrentOrder.notifyDataSetChanged();
                        }else{
                            tv_no_currentOrder.setVisibility(View.VISIBLE);
                            rv_myCurrentOrder.setVisibility(View.GONE);
                        }
                    }else{
                        tv_no_currentOrder.setVisibility(View.VISIBLE);
                        rv_myCurrentOrder.setVisibility(View.GONE);
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
                params.put("user_id",User_Id);
                Log.e("jhdf",params+"");
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(MyCurrentOrders.this);
        requestQueue.add(stringRequest);
    }
}
