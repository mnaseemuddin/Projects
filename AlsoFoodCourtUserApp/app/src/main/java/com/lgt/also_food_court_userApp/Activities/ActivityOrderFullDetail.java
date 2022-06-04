package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lgt.also_food_court_userApp.Adapters.AdapterFullHistory;
import com.lgt.also_food_court_userApp.Adapters.AdapterOrderHistory;
import com.lgt.also_food_court_userApp.Models.ModelOrderHistory;
import com.lgt.also_food_court_userApp.beans.OrderFullDetail.SingleProductDetails;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.beans.OrderFullDetail.ModelOrderFullDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityOrderFullDetail extends AppCompatActivity {

    private ImageView ivBackFullDetail, ivFoodType,ivProductDetail;
    private TextView tvToolbarOrderFull, tvPurchasedFrom, tvPurchasedFromRestaurantAddress,
            tvDeliverAtOrderDetail, tvDeliveredAddressOrderDetail, tvOrderDeliveryDate, tvFoodName,
            tvFoodSize, tvFoodQty, tvProductPriceFullDetail, tvTotal, tvGST, tvDeliveryCharges,
            tvPaidUsing, tvPaidAmount, tvRepeatOrder;


    private AlertDialog dialog;
    private EditText et_EnterReview;
    private RatingBar ratingBar;

    private RecyclerView rv_orderProductList;
    private AdapterFullHistory adapterFullHistory;
    private List<ModelOrderHistory> singleProductDetails=new ArrayList<>();


    private static String mOrderID = "";
    private static final String TAG = "ActivityOrderFullDetail";

    private ProgressBar pbFullDetails;
    private RelativeLayout rlOrderFullDetails,rv_addReviewProduct;

     private String userId,ProductId,UserReview,productNumber;
     private SharedPreferences sharedPreferences;
     Float Rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_full_detail);



        sharedPreferences = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);
         Log.e("KLIJUHY",userId+"");







        ivBackFullDetail = findViewById(R.id.ivBackFullDetail);
        ivFoodType = findViewById(R.id.ivFoodType);
        tvToolbarOrderFull = findViewById(R.id.tvToolbarOrderFull);
        tvPurchasedFrom = findViewById(R.id.tvPurchasedFrom);
        tvPurchasedFromRestaurantAddress = findViewById(R.id.tvPurchasedFromRestaurantAddress);
        tvDeliverAtOrderDetail = findViewById(R.id.tvDeliverAtOrderDetail);
        tvDeliveredAddressOrderDetail = findViewById(R.id.tvDeliveredAddressOrderDetail);
        tvOrderDeliveryDate = findViewById(R.id.tvOrderDeliveryDate);
        tvFoodName = findViewById(R.id.tvFoodName);
        tvFoodSize = findViewById(R.id.tvFoodSize);
        tvFoodQty = findViewById(R.id.tvFoodQty);
        tvProductPriceFullDetail = findViewById(R.id.tvProductPriceFullDetail);
        tvTotal = findViewById(R.id.tvTotal);
        tvGST = findViewById(R.id.tvGST);
        tvDeliveryCharges = findViewById(R.id.tvDeliveryCharges);
        tvPaidUsing = findViewById(R.id.tvPaidUsing);
        tvPaidAmount = findViewById(R.id.tvPaidAmount);
        tvRepeatOrder = findViewById(R.id.tvRepeatOrder);
        pbFullDetails = findViewById(R.id.pbFullDetails);
        rlOrderFullDetails = findViewById(R.id.rlOrderFullDetails);
        ivProductDetail = findViewById(R.id.ivProductDetail);
        rv_addReviewProduct=findViewById(R.id.rv_addReviewProduct);
        rv_orderProductList=findViewById(R.id.rv_orderProductList);

        rv_addReviewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(ActivityOrderFullDetail.this, "Please Add review", Toast.LENGTH_SHORT).show();
                View ViewDialog= LayoutInflater.from(ActivityOrderFullDetail.this).inflate(R.layout.popupmenu_layout,null);

                Button btn_SubmitReview=(Button)ViewDialog.findViewById(R.id.btn_SubmitReview);
                Button btn_cancelReview=(Button)ViewDialog.findViewById(R.id.btn_cancelReview);
                et_EnterReview=ViewDialog.findViewById(R.id.et_EnterReview);
                 ratingBar=(RatingBar)ViewDialog.findViewById(R.id.ratingBar);



                dialog=new AlertDialog.Builder(ActivityOrderFullDetail.this).create();
                dialog.setView(ViewDialog);

                btn_SubmitReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inputValidation();

                       // Toast.makeText(ActivityOrderFullDetail.this, "Thanks For Review", Toast.LENGTH_SHORT).show();

                    }
                });

                btn_cancelReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });


        rlOrderFullDetails.setVisibility(View.GONE);
        ivProductDetail.setVisibility(View.GONE);


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("KEY_ORDER_ID")) {
                mOrderID = intent.getStringExtra("KEY_ORDER_ID");
                Log.e(TAG, "onCreate: " + mOrderID);
            }
        }

        tvRepeatOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityOrderFullDetail.this, "To be implemented...", Toast.LENGTH_SHORT).show();
            }
        });
        ivBackFullDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        apiGetDetails();

    }

    private void inputValidation() {
        UserReview=et_EnterReview.getText().toString().trim();
        Rating=ratingBar.getRating();
        if (Rating>0.0){
            CallApiAddReview();
            dialog.dismiss();
        }else {
            Toast.makeText(this, "Please Given A Review", Toast.LENGTH_SHORT).show();
            dialog.show();
        }
    }

    private void CallApiAddReview() {
        final StringRequest stringRequest=new StringRequest(Request.Method.POST, AlsoFoodCourtApi.AddReviewForProduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");


                    Toast.makeText(ActivityOrderFullDetail.this, message+"", Toast.LENGTH_SHORT).show();
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
                params.put("user_id",userId);
                Log.e("HBGFVF",userId+"");
                params.put("tbl_restaurant_products_id",ProductId);
                params.put("review",UserReview);
                Log.e("AAAAAAAAAAA",UserReview+"");
                params.put("rating", String.valueOf(Rating));
                Log.e("KLMKL1258",Rating+"");
                Log.e("p123456",params+"");
                return params;

            }

        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void apiGetDetails() {

        pbFullDetails.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.SINGLE_ORDER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbFullDetails.setVisibility(View.GONE);

                Log.e(TAG, "onResponsdsadasde: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Gson gson = new Gson();

                    ModelOrderFullDetail modelOrderFullDetail = gson.fromJson(response, ModelOrderFullDetail.class);

                    if(modelOrderFullDetail.getStatus().equalsIgnoreCase("1")){

                        rlOrderFullDetails.setVisibility(View.VISIBLE);
if (modelOrderFullDetail.getData().size()>0){
    Log.e("dsadjjjkiiojoijo", new Gson().toJson(modelOrderFullDetail.getData().get(0).getSingleProduct()) + "");

    singleProductDetails=modelOrderFullDetail.getData().get(0).getSingleProduct();
    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
    rv_orderProductList.setHasFixedSize(true);
    adapterFullHistory=new AdapterFullHistory(singleProductDetails,getApplicationContext());
    rv_orderProductList.setLayoutManager(linearLayoutManager);
    rv_orderProductList.setAdapter(adapterFullHistory);
      int i=0;
                        //for (int i = 0; i < modelOrderFullDetail.getData().size(); i++) {

                            tvToolbarOrderFull.setText("# " + modelOrderFullDetail.getData().get(i).getOrderNumber() + "");
                             ProductId=modelOrderFullDetail.getData().get(i).getProductId();
                            tvPurchasedFrom.setText(modelOrderFullDetail.getData().get(i).getRestaurantName());
                            tvPurchasedFromRestaurantAddress.setText(modelOrderFullDetail.getData().get(i).getBlock() +
                                    ", " + modelOrderFullDetail.getData().get(i).getAddress() + " ," +
                                    modelOrderFullDetail.getData().get(i).getPincode() + "");

                            tvDeliveredAddressOrderDetail.setText(modelOrderFullDetail.getData().get(i).getAddress() + ", "
                                    + modelOrderFullDetail.getData().get(i).getCity() + ", "
                                    + modelOrderFullDetail.getData().get(i).getUserPinCode() + " ,"
                                    + modelOrderFullDetail.getData().get(i).getState() + "");


                            Log.e("jdfg",tvDeliveredAddressOrderDetail.toString()+"");

                            tvFoodName.setText(modelOrderFullDetail.getData().get(i).getProductsName());
                            tvFoodSize.setText(modelOrderFullDetail.getData().get(i).getSize());
                            tvFoodQty.setText(modelOrderFullDetail.getData().get(i).getQuantity());

                            tvProductPriceFullDetail.setText(modelOrderFullDetail.getData().get(i).getPrice());
                            tvTotal.setText(modelOrderFullDetail.getData().get(i).getPrice());
                            tvGST.setText(modelOrderFullDetail.getData().get(i).getgST());
                            tvDeliveryCharges.setText(modelOrderFullDetail.getData().get(i).getDeliveryCharges());
                            tvPaidUsing.setText("Paid using - "+modelOrderFullDetail.getData().get(i).getPaymentType());
                            tvPaidAmount.setText(modelOrderFullDetail.getData().get(i).getTotalPrice());

                            Glide.with(ActivityOrderFullDetail.this).load(modelOrderFullDetail.getData().get(i).getImage()).into(ivProductDetail);

                           /* if(modelOrderFullDetail.getData().get(i).getProductsCategory().equalsIgnoreCase("NonVeg")){
                                ivFoodType.setImageDrawable(getDrawable(R.drawable.non_veg_icon));
                            }
*/
                        }

                    }

                    else {
                        Toast.makeText(ActivityOrderFullDetail.this, "No data found", Toast.LENGTH_SHORT).show();
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
                params.put("order_number", mOrderID);
                Log.e("HYUTGRFED",mOrderID+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityOrderFullDetail.this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
