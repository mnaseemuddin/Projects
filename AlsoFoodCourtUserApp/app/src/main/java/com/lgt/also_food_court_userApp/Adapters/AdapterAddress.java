package com.lgt.also_food_court_userApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.also_food_court_userApp.Activities.Add_AddressActivity;
import com.lgt.also_food_court_userApp.Fragments.FragmentAddress;
import com.lgt.also_food_court_userApp.Models.ModelAddress;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.extra.Common;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterAddress extends RecyclerView.Adapter<AdapterAddress.HolderAddress> {

    private List<ModelAddress> list;
    private Context context;
    private int checkedPosition = -1;

    public interfaceAddID addressID;

    public interface interfaceAddID{
       void getAddID(String addressID);
    }

    FragmentAddress fragmentAddress;


    public AdapterAddress(List<ModelAddress> list, Context context, FragmentAddress fragmentAddress,interfaceAddID addressID) {
        this.list = list;
        this.context = context;
        this.fragmentAddress = fragmentAddress;
        this.addressID = addressID;
    }

    @NonNull
    @Override
    public HolderAddress onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_address, parent, false);
        return new HolderAddress(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderAddress holder, final int position) {

        holder.tv_address_type.setText(list.get(position).getTv_address_type());
        holder.tv_full_address.setText(list.get(position).getTv_full_address());


     /*   holder.ll_AddressItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkedPosition = position;
                notifyDataSetChanged();
            }
        });

        if (checkedPosition == position) {

            holder.iv_select_address.setImageDrawable(context.getResources().getDrawable(R.drawable.tick));
            holder.ll_AddressItems.setBackground(context.getDrawable(R.drawable.bg_selected_size));
            Log.e("SELCETEDDDDADDDD", checkedPosition + "");

            addressID.getAddID(list.get(position).getAddress_id());

        } else {
            holder.iv_select_address.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));

        }*/

        holder.ivDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.DELETE_ADDRESS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {
                                fragmentAddress.loadAddress();
                                Toast.makeText(context, "Address deleted", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("deletedresponse", response + "");


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_addressbook_id", list.get(position).getAddress_id());
                        Log.e("adddressdsid", params + "");
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }
        });


        holder.ivEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAddress(list.get(position).getAddress_id(), list.get(position).getTv_address_type(), list.get(position).getTv_full_address(),list.get(position).getLatitude(),list.get(position).getLogitude());

               /* Intent intent=new Intent(context,Add_AddressActivity.class);
                context.startActivity(intent);
*/


            }
        });
    }

    private void editAddress(String address_id, String addressType, String fullAddress,String latitude,String logitude) {

       /* Fragment fragmentAddress = new FragmentAddAddress();
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameOrderReview,fragmentAddress);
*/

        Intent intent=new Intent(context, Add_AddressActivity.class);
        intent.putExtra("KEY_ADD_ID",address_id);
        intent.putExtra("KEY_ADD_Type",addressType);
        intent.putExtra("KEY_ADD_Full",fullAddress);
        intent.putExtra(Common.latitude,latitude);
        intent.putExtra(Common.logitude,logitude);

        context.startActivity(intent);


        /*bundle.putString("KEY_ADD_ID",address_id);
        bundle.putString("KEY_ADD_NAME",name);
        bundle.putString("KEY_ADD_MOBILE_NUMBER",mobileNo);
       */
       /* fragmentAddress.setArguments(bundle);
        fragmentTransaction.commit();
*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HolderAddress extends RecyclerView.ViewHolder {
        private ImageView iv_select_address, ivEditAddress, ivDeleteAddress;
        private TextView  tv_address_type, tv_full_address;
        private LinearLayout ll_AddressItems;


        public HolderAddress(@NonNull View itemView) {
            super(itemView);

            ivEditAddress = itemView.findViewById(R.id.ivEditAddress);
            ivDeleteAddress = itemView.findViewById(R.id.ivDeleteAddress);

            tv_address_type = itemView.findViewById(R.id.tv_address_type);
            tv_full_address = itemView.findViewById(R.id.tv_full_address);

            iv_select_address = itemView.findViewById(R.id.iv_select_address);
            ll_AddressItems = itemView.findViewById(R.id.ll_AddressItems);

        }


    }
}
