package com.lgt.NeWay.activity.Amity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterAmitys extends RecyclerView.Adapter<AdapterAmitys.Cityholder> {
    List<ModelAmitys>mlist;
    Context context;
    SharedPreferences sharedPreferences;
    String Mtable_id;
    AmityRefreshInterface amityRefreshInterface;
    public AdapterAmitys(List<ModelAmitys> mlist, Context context,AmityRefreshInterface amityRefreshInterface) {
        this.mlist = mlist;
        this.context = context;
        this.amityRefreshInterface = amityRefreshInterface;
    }

    @NonNull
    @Override
    public AdapterAmitys.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        inisharedpref();
        View view= LayoutInflater.from(context).inflate(R.layout.adapteramitys,parent,false);
        return new Cityholder(view);
    }

    private void inisharedpref() {
        sharedPreferences=context.getSharedPreferences(common.UserData,Context.MODE_PRIVATE);
        Mtable_id=sharedPreferences.getString("tbl_coaching_id","");
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAmitys.Cityholder holder, int position) {

        String amity_id=mlist.get(position).getTbl_amenity_id();
        holder.tv_Aminity_Name.setText(mlist.get(position).getTv_Aminity_Name());

       setAdapter(holder.sp_Statuspending,Integer.parseInt(mlist.get(position).getAmitystatus()));

        holder.sp_Statuspending.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changestatus(position,amity_id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amityRefreshInterface.amityRefreshInterface(amity_id);
            }
        });
    }

    private void changestatus(int status, String Amity_id) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Edit_AmitysList_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("chechhhhe",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String amenity_status=jsonObject.getString("amenity_status");
                    String status=jsonObject.getString("status");
                    if (amenity_status.equals("1")){
                        //Toast.makeText(context, message+"", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, message+"", Toast.LENGTH_SHORT).show();
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
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>parem=new HashMap<>();
                parem.put("tbl_amenity_id",Amity_id);
                parem.put("status",""+status);
                parem.put("tbl_coaching_id",Mtable_id);

                Log.e("chechhhhe",parem+"");
                return parem;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void setAdapter(Spinner sp_statuspending, int pos) {
        ArrayAdapter<String>adapter=new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,Amitys.statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_statuspending.setAdapter(adapter);
        sp_statuspending.setSelection(pos);

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        TextView tv_Aminity_Name;
        ImageView iv_delete;
        Spinner sp_Statuspending;
        public Cityholder(@NonNull View itemView) {
            super(itemView);


            tv_Aminity_Name=itemView.findViewById(R.id.tv_Aminity_Name);
            iv_delete=itemView.findViewById(R.id.iv_delete);
            sp_Statuspending=itemView.findViewById(R.id.sp_Statuspending);

        }
    }
}
