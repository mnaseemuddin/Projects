package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.also_food_court_userApp.Activities.ActivityOrderReview;
import com.lgt.also_food_court_userApp.Models.ModelReviewOrder;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterReviewOrder extends RecyclerView.Adapter<AdapterReviewOrder.HolderReview> {

    private List<ModelReviewOrder> list;
    private Context context;

    public AdapterReviewOrder(List<ModelReviewOrder> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderReview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_review_layout,parent,false);
        return new HolderReview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderReview holder, final int position) {

        Glide.with(context).load(list.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(R.drawable.image_not_available_new).error(R.drawable.image_not_available_new)).into(holder.iv_cart_image);
        holder.tv_cart_name.setText(list.get(position).getProducts_name());
        holder.tv_total_amount.setText(context.getString(R.string.rs)+list.get(position).getPrice());
        holder.tv_quantity.setText(list.get(position).getQuantity());


        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qty = Integer.parseInt(holder.tv_quantity.getText().toString());
                qty = qty + 1;
                holder.tv_quantity.setText(String.valueOf(qty));
                updateCartItems(list.get(position).getCart_id(), holder.tv_quantity.getText().toString());

            }
        });

        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int total = Integer.parseInt(holder.tv_quantity.getText().toString());
                total = total - 1;
                Log.e("check", "" + total);

                if (total == 0) {
                    holder.iv_minus.setEnabled(false);
                    holder.tv_quantity.setText("" + total);
                    deleteProduct(list.get(position).getCart_id());
                    Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();

                } else {
                    holder.tv_quantity.setText("" + total);
                    updateCartItems(list.get(position).getCart_id(), holder.tv_quantity.getText().toString().trim());
                }


            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(list.get(position).getCart_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderReview extends RecyclerView.ViewHolder {

       private ImageView iv_cart_image, iv_delete, iv_minus, iv_plus;
        private TextView tv_cart_name, tv_total_amount, tv_quantity;


        public HolderReview(@NonNull View itemView) {
            super(itemView);

            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_cart_image = itemView.findViewById(R.id.iv_cart_image);

            tv_cart_name = itemView.findViewById(R.id.tv_cart_name);

            tv_total_amount = itemView.findViewById(R.id.tv_total_amount);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);

            iv_plus = itemView.findViewById(R.id.iv_plus);
            iv_minus = itemView.findViewById(R.id.iv_minus);




        }
    }


    private void deleteProduct(final String cart_id) {

        ActivityOrderReview.pbOrder_review.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.DELETE_CART_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ActivityOrderReview.pbOrder_review.setVisibility(View.GONE);
                Log.e("dsadsayuuiyuiui", response + "");
                Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {

                        updateCart();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ActivityOrderReview.pbOrder_review.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cart_id", cart_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    private void updateCartItems(final String cart_id, final String qty) {
        ActivityOrderReview.pbOrder_review.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.UPDATE_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        Log.e("dsadsadsadsa", response + "");
                        updateCart();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ActivityOrderReview.pbOrder_review.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cart_id", cart_id);
                params.put("quantity", qty);
                Log.e("lololoololoo", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }
    private void updateCart() {

        Intent intent=new Intent("Update_Cart");
        intent.putExtra("update","update_now");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
