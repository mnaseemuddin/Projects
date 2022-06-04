package com.lgt.NeWay.activity.Amity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Amitys extends AppCompatActivity implements AmityRefreshInterface{
    List<ModelAmitys>mlist=new ArrayList<>();
    AdapterAmitys adapterAmitys;
    RecyclerView rv_Amitylist;
    RelativeLayout rl_AddAmityContainer;
    public AlertDialog download_dialog;
    SharedPreferences sharedPreferences;
    EditText et_Select_Amity;
    Button bt_submitboard,bt_cancel;
    String Mtableid;
    ImageView ivBackFullDescription;
    public static List<String> statusList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amitys);

        iniSharedpref();
        iniview();
        LoadAmitylist();
        onclick();

        statusList.clear();
        statusList.add("Active");
        statusList.add("Inactive");
    }

    private void iniSharedpref() {
        sharedPreferences=getSharedPreferences(common.UserData,MODE_PRIVATE);
        Mtableid=sharedPreferences.getString("tbl_coaching_id","");
    }


    private void iniview() {
        rv_Amitylist=findViewById(R.id.rv_Amitylist);
        rl_AddAmityContainer=findViewById(R.id.rl_AddAmityContainer);
        ivBackFullDescription=findViewById(R.id.ivBackFullDescription);
    }

    private void onclick() {
        rl_AddAmityContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDilough();
            }
        });

        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void openDilough() {

        //progressbar.setVisibility(View.GONE);

        View customView = LayoutInflater.from(this).inflate(R.layout.addamitylistdilough, null);
        download_dialog = new AlertDialog.Builder(this).create();
        download_dialog.setView(customView);
        download_dialog.setCanceledOnTouchOutside(false);

        et_Select_Amity = customView.findViewById(R.id.et_Select_Amity);
        bt_submitboard = customView.findViewById(R.id.bt_submitboard);
        bt_cancel = customView.findViewById(R.id.bt_cancel);

        download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        download_dialog.show();

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download_dialog.dismiss();
            }
        });

        bt_submitboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submitamity();
            }
        });

    }

    private void Submitamity() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Add_Amitys, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("name&id",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        //Toast.makeText(Amitys.this, message+"", Toast.LENGTH_SHORT).show();
                        download_dialog.dismiss();
                        LoadAmitylist();
                    }else {
                        Toast.makeText(Amitys.this, message+"", Toast.LENGTH_SHORT).show();
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
                parem.put("name",et_Select_Amity.getText().toString());
                parem.put("tbl_coaching_id",Mtableid);

                Log.e("name&id",parem+"");
                return parem;
            }
        };RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void LoadAmitylist() {
        mlist.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Added_AmitysList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        if (jsonArray.length()>0){
                            for (int i=0;jsonArray.length()>i;i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                String tbl_amenity_id=object.getString("tbl_amenity_id");
                                String name=object.getString("name");
                                String tbl_coaching_id=object.getString("tbl_coaching_id");
                                String Amitystatus=object.getString("status");

                                mlist.add(new ModelAmitys(name,Amitystatus,tbl_amenity_id));
                                //Toast.makeText(Amitys.this, message+"", Toast.LENGTH_SHORT).show();
                            }
                            adapterAmitys=new AdapterAmitys(mlist,getApplicationContext(),Amitys.this);
                            rv_Amitylist.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                            rv_Amitylist.setAdapter(adapterAmitys);
                            rv_Amitylist.setHasFixedSize(true);
                        }
                    }else {
                        Toast.makeText(Amitys.this, message+"", Toast.LENGTH_SHORT).show();
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
                return parem;
            }
        };RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    @Override
    public void amityRefreshInterface(String id) {
        deleteAmitys(id);

    }

    private void deleteAmitys(String Aminity_id) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Delete_AmitysList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checkresppppppp",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        LoadAmitylist();

                       // Toast.makeText(Amitys.this, message+"", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Amitys.this, message+"", Toast.LENGTH_SHORT).show();
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
                parem.put("tbl_amenity_id",Aminity_id);
                parem.put("tbl_coaching_id",Mtableid);
                return parem;
            }
        };RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}