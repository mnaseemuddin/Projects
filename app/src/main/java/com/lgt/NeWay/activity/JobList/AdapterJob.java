package com.lgt.NeWay.activity.JobList;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.SubjectList.SubjectList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lgt.NeWay.activity.ClassList.ClassList.Mtable_id;

public class AdapterJob extends RecyclerView.Adapter<AdapterJob.Cityholder> {
    List<ModelJob>mlist;
    Context context;
    String Utbl_id;
    SharedPreferences sharedPreferences;
    UpdateJobInterface refreshJobListFterDelte;


    public AdapterJob(List<ModelJob> mlist, Context context , UpdateJobInterface refreshJobListFterDelte) {
        this.mlist = mlist;
        this.context = context;
        this.refreshJobListFterDelte=refreshJobListFterDelte;
    }

    @NonNull
    @Override
    public AdapterJob.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapterjob,parent,false);

        iniSharedprefrence();
        return new Cityholder(view);


    }

    private void iniSharedprefrence() {
        sharedPreferences=context.getSharedPreferences(common.UserData,Context.MODE_PRIVATE);
        Utbl_id=sharedPreferences.getString("tbl_coaching_id","");
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterJob.Cityholder holder, int position) {
        String job_id=mlist.get(position).getTbl_jobs_id();

        holder.tv_JobTitle.setText(mlist.get(position).getTv_JobTitle());
        holder.tv_JobBoard_Name.setText(mlist.get(position).getTv_JobBoard_Name());
        holder.tv_SubjectName.setText(mlist.get(position).getTv_SubjectName());
        Glide.with(context).load(mlist.get(position).getIvBatch_Iamge()).into(holder.ivBatch_Iamge);


        setAdapterStatus(holder.sp_Statuspending, Integer.parseInt(mlist.get(position).getStatus()));
        Log.d("setAdapterStatus", job_id + "  setAdapterStatus: " + mlist.get(position).getStatus());

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshJobListFterDelte.updatejobdelete(mlist.get(position).getTbl_jobs_id());

            }
        });

        holder.sp_Statuspending.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changestatus(position,job_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void changestatus(int status, String job_id) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Edit_Job_List_Status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checkkkkkkkk",response+"");


                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String job_status=jsonObject.getString("job_status");
                    String status=jsonObject.getString("status");

                    if (job_status.equals("1")){
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
                parem.put("tbl_coaching_id",Utbl_id);
                parem.put("tbl_jobs_id",job_id);
                parem.put("status", "" + status);
                return parem;
            }
        };RequestQueue requestQueue=Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void setAdapterStatus(Spinner sp_statuspending, int pos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, Jobs.JstatusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_statuspending.setAdapter(adapter); // set the selected value of the spinner
        sp_statuspending.setSelection(pos);
    }



    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        TextView tv_JobTitle,tv_JobBoard_Name,tv_SubjectName;
        ImageView iv_delete,ivBatch_Iamge;
        Spinner sp_Statuspending;
        public Cityholder(@NonNull View itemView) {
            super(itemView);

            tv_SubjectName=itemView.findViewById(R.id.tv_SubjectName);
            tv_JobBoard_Name=itemView.findViewById(R.id.tv_JobBoard_Name);
            tv_JobTitle=itemView.findViewById(R.id.tv_JobTitle);
            iv_delete=itemView.findViewById(R.id.iv_delete);
            sp_Statuspending=itemView.findViewById(R.id.sp_Statuspending);
            ivBatch_Iamge=itemView.findViewById(R.id.ivBatch_Iamge);
        }
    }
}
