package com.lgt.NeWay.activity.ClassList;

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
import com.lgt.NeWay.CustomeInterface.ClassListRefresh;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.Cityholder> {
    List<ModelClass>list;
    Context context;
    String Mtable_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ClassListRefresh mClassListRefresh;
    String class_id;

    public AdapterClass(List<ModelClass> list, Context context, ClassListRefresh mRefresh) {
        this.list = list;
        this.context = context;
        this.mClassListRefresh = mRefresh;
    }

    @NonNull
    @Override
    public AdapterClass.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapterclass,parent,false);

        inisharep();
        return new Cityholder(view);
    }

    private void inisharep() {
        sharedPreferences=context.getSharedPreferences(common.UserData,Context.MODE_PRIVATE);

        Mtable_id=sharedPreferences.getString("tbl_coaching_id","");

    }

    @Override
    public void onBindViewHolder(@NonNull Cityholder holder, int position) {
        // String tbl_id = list.get(position).getTbl_board_id();
        String class_id=list.get(position).getTbl_class_id();
        holder.tv_Class_Name.setText(list.get(position).getTv_Class_Name());

        setAdapterStatus(holder.sp_Statuspending, Integer.parseInt(list.get(position).getClass_status()));

        holder.sp_Statuspending.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changestatus(position, class_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.iv_delete.setEnabled(false);
                mClassListRefresh.ListItemDelete(class_id);
            }
        });
    }

    private void changestatus(int status, String class_id) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Edit_Class_ListStatus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ajjjjjjjjj",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String subject_status=jsonObject.getString("subject_status");
                    String status=jsonObject.getString("status");
                    if (subject_status.equals("0")){
                      //  Toast.makeText(context, message+"", Toast.LENGTH_SHORT).show();
                    }else {
                        if (subject_status.equals("1")){
                            Toast.makeText(context, message+"", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse", "onErrorResponse: " + error);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
             Map<String,String>param=new HashMap<>();
                param.put("tbl_coaching_id", Mtable_id);
                param.put("tbl_class_id", class_id);
                param.put("status", "" + status);
                Log.d("getParams", "getParams: " + param);

             return param;
            }
        };RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void setAdapterStatus(Spinner sp_statuspending, int pos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, ClassList.statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_statuspending.setAdapter(adapter);// set the selected value of the spinner
        sp_statuspending.setSelection(pos);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        ImageView iv_delete;
        TextView tv_Class_Name;
        Spinner sp_Statuspending;

        public Cityholder(@NonNull View itemView) {
            super(itemView);
            sp_Statuspending=itemView.findViewById(R.id.sp_Statuspending);
            tv_Class_Name=itemView.findViewById(R.id.tv_Class_Name);
            iv_delete=itemView.findViewById(R.id.iv_delete);
        }
    }
}
