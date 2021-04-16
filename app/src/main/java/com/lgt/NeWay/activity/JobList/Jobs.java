package com.lgt.NeWay.activity.JobList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.lgt.NeWay.activity.SubjectList.SubjectList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jobs extends AppCompatActivity implements UpdateJobInterface{
    AdapterJob adapterJob;
    List<ModelJob>mlist=new ArrayList<>();
    RelativeLayout rl_AddJobContainer;
    RecyclerView rv_JobList;
    ProgressBar progressbar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView ivBackFullDescription;
     String Mtableid;
     Context context;
    private Jobs actvity;
    public static   List<String> JstatusList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        iniView();
        LoadJobList();
        iniONCLICK();
        iniSharedp();


        context=actvity=this;
        JstatusList.clear();
        JstatusList.add("Active");
        JstatusList.add("Inactive");
    }

    private void iniSharedp() {
        sharedPreferences=getSharedPreferences(common.UserData,MODE_PRIVATE);
        Mtableid=sharedPreferences.getString("tbl_coaching_id","");
    }


    private void iniView() {
        rv_JobList=findViewById(R.id.rv_JobList);
        progressbar=findViewById(R.id.progressbar);
        rl_AddJobContainer=findViewById(R.id.rl_AddJobContainer);
        ivBackFullDescription=findViewById(R.id.ivBackFullDescription);

    }
    private void iniONCLICK() {
        rl_AddJobContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddJob.class));
            }
        });

        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    private void LoadJobList() {
        progressbar.setVisibility(View.VISIBLE);
        mlist.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Job_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checkrspp",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        for (int i=0;jsonArray.length()>i;i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String tbl_jobs_id=jsonObject1.getString("tbl_jobs_id");
                            String job_title=jsonObject1.getString("job_title");
                            String tbl_coaching_id=jsonObject1.getString("tbl_coaching_id");
                            String board_name=jsonObject1.getString("board_name");
                            String subject_name=jsonObject1.getString("subject_name");
                            String Jobstatus=jsonObject1.getString("status");
                            String image=jsonObject1.getString("image");


                            Toast.makeText(Jobs.this, message+"", Toast.LENGTH_SHORT).show();

                            ModelJob modelJob = new ModelJob();
                            modelJob.setTbl_jobs_id(tbl_jobs_id);
                            modelJob.setTv_JobBoard_Name(board_name);
                            modelJob.setTv_JobTitle(job_title);
                            modelJob.setTv_SubjectName(subject_name);
                            modelJob.setStatus(Jobstatus);
                            modelJob.setIvBatch_Iamge(image);
                            mlist.add(modelJob);

                        }
                        adapterJob=new AdapterJob(mlist,getApplicationContext(),Jobs.this);
                        rv_JobList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                        rv_JobList.setAdapter(adapterJob);
                        rv_JobList.setHasFixedSize(true);
                        adapterJob.notifyDataSetChanged();
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
                Map<String,String>param=new HashMap<>();
                param.put("tbl_coaching_id",Mtableid);



                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        progressbar.setVisibility(View.GONE);

        adapterJob=new AdapterJob(mlist,getApplicationContext(),Jobs.this);
        rv_JobList.setAdapter(adapterJob);
        rv_JobList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        rv_JobList.setHasFixedSize(true);
        adapterJob.notifyDataSetChanged();
    }


    @Override
    public void updatejobdelete(String id) {
        deleteJob(id);

    }
    private void deleteJob(String job_id) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Delete_Job_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checkrespppp",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");

                   if (status.equals("1")){

                      adapterJob.notifyDataSetChanged();


                       Toast.makeText(Jobs.this, message+"", Toast.LENGTH_SHORT).show();
                       LoadJobList();

                   }else {
                       Toast.makeText(Jobs.this, message+"", Toast.LENGTH_SHORT).show();
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
                parem.put("tbl_jobs_id",job_id);

                Log.e("checkparemsssssssss",parem+"");
                return parem;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


}