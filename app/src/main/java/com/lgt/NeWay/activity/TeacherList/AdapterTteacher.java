package com.lgt.NeWay.activity.TeacherList;

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
import com.bumptech.glide.Glide;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.JobList.Jobs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterTteacher extends RecyclerView.Adapter<AdapterTteacher.Cityholder> {
    List<ModelTeacher>mlist;
    Context context;
    SharedPreferences sharedPreferences;
    String Mtableid;
    UpdateTeacherListInterFace updateTeacherListInterFace;

    public AdapterTteacher(List<ModelTeacher> mlist, Context context, UpdateTeacherListInterFace updateTeacherListInterFace) {
        this.mlist = mlist;
        this.context = context;
        this.updateTeacherListInterFace = updateTeacherListInterFace;
    }

    @NonNull
    @Override
    public Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        iniSharedP();
        View view= LayoutInflater.from(context).inflate(R.layout.adapterteacher,parent,false);
        return new Cityholder(view);

    }

    private void iniSharedP() {
        sharedPreferences=context.getSharedPreferences(common.UserData,Context.MODE_PRIVATE);
        Mtableid=sharedPreferences.getString("tbl_coaching_id","");
    }

    @Override
    public void onBindViewHolder(@NonNull Cityholder holder, int position) {
        String tbl_teacher_id=mlist.get(position).getTbl_teacher_id();
        holder.tv_TeacherName.setText(mlist.get(position).getTv_TeacherName());
        holder.tv_Qualification_Name.setText(mlist.get(position).getTv_Qualification_Name());
        holder.tv_tvContactNo.setText(mlist.get(position).getTv_tvContactNo());
        holder.tv_Email_id.setText(mlist.get(position).getTv_Email_id());
        Glide.with(context).load(mlist.get(position).getIvTeacher_Iamge()).into(holder.ivTeacher_Iamge);

        setAdapterStatus(holder.sp_Statuspending, Integer.parseInt(mlist.get(position).getTeacherStatus()));

        holder.sp_Statuspending.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changestatus(position,tbl_teacher_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTeacherListInterFace.updateList(mlist.get(position).getTbl_teacher_id());

            }
        });


    }

    private void setAdapterStatus(Spinner sp_statuspending, int pos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, TeacherList.JstatusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_statuspending.setAdapter(adapter); // set the selected value of the spinner
        sp_statuspending.setSelection(pos);

    }

    private void changestatus(int status, String teacherid) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Edit_TeacherList_Status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checkkkkkkkk",response+"");


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String teacher_status=jsonObject.getString("teacher_status");
                    String status=jsonObject.getString("status");

                    if (teacher_status.equals("1")){
                        Toast.makeText(context, message+"", Toast.LENGTH_SHORT).show();
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
                parem.put("tbl_coaching_id",Mtableid);
                parem.put("tbl_teacher_id",teacherid);
                parem.put("status", "" + status);
                return parem;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
       TextView  tv_TeacherName,tv_Qualification_Name,tv_tvContactNo,tv_Email_id;
       Spinner   sp_Statuspending;
       ImageView iv_delete,ivTeacher_Iamge;


        public Cityholder(@NonNull View itemView) {
            super(itemView);

            sp_Statuspending=itemView.findViewById(R.id.sp_Statuspending);
            tv_TeacherName=itemView.findViewById(R.id.tv_TeacherName);
            tv_Qualification_Name=itemView.findViewById(R.id.tv_Qualification_Name);
            tv_tvContactNo=itemView.findViewById(R.id.tv_tvContactNo);
            tv_Email_id=itemView.findViewById(R.id.tv_Email_id);
            iv_delete=itemView.findViewById(R.id.iv_delete);
            ivTeacher_Iamge=itemView.findViewById(R.id.ivTeacher_Iamge);
        }
    }
}
