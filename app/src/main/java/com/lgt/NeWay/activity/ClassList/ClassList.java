package com.lgt.NeWay.activity.ClassList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.lgt.NeWay.CustomeInterface.ClassListRefresh;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassList extends AppCompatActivity implements ClassListRefresh {
    RecyclerView rv_ClassList;
    AdapterClass adapterClass;
    RelativeLayout rl_AddClassContainer;
    List<ModelClass>mlist=new ArrayList<>();
    ImageView ivBackFullDescription;
    public  AlertDialog download_dialog;
    Spinner sp_Select_Class;
    Button bt_cancel,bt_submitboard;
    String selectedName = "";
    ProgressBar progressbar;
    List<String> stringList = new ArrayList<>();
    public static List<String> statusList = new ArrayList<>();
    public static String Mtable_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        iniView();
        LoadClassAddedList();
        iniSharedprefrence();

        statusList.clear();
        statusList.add("Active");
        statusList.add("Inactive");
    }

    private void iniSharedprefrence() {
        sharedPreferences = getSharedPreferences(common.UserData,MODE_PRIVATE);
        editor=sharedPreferences.edit();
        Mtable_id=sharedPreferences.getString("tbl_coaching_id","");
    }



    private void iniView() {
        ivBackFullDescription=findViewById(R.id.ivBackFullDescription);
        rv_ClassList=findViewById(R.id.rv_ClassList);
        rl_AddClassContainer=findViewById(R.id.rl_AddClassContainer);
        progressbar=findViewById(R.id.progressbar);

        inionclicklistner();
    }

    private void inionclicklistner() {
        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rl_AddClassContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diloughopen();
                progressbar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void diloughopen() {
        progressbar.setVisibility(View.GONE);

        View customView = LayoutInflater.from(this).inflate(R.layout.classadddilough, null);
        download_dialog = new AlertDialog.Builder(this).create();
        download_dialog.setView(customView);
        download_dialog.setCanceledOnTouchOutside(false);

        sp_Select_Class = customView.findViewById(R.id.sp_Select_Class);
        bt_submitboard = customView.findViewById(R.id.bt_submitboard);
        bt_cancel = customView.findViewById(R.id.bt_cancel);
        ivBackFullDescription = customView.findViewById(R.id.ivBackFullDescription);

        loadclasses();

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download_dialog.dismiss();
            }
        });

        bt_submitboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedName.equals("")) {
                    Log.d("setOnClickListener", "setOnClickListener Nothing");
                } else {
                    Log.d("setOnClickListener", "select Name is : " + selectedName);
                    Addclass();
                }
            }
        });
        download_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LoadClassAddedList();
            }
        });

        // tv_current_size_progress = customView.findViewById(R.id.tv_current_size_progress);
        download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        download_dialog.show();
        progressbar.setVisibility(View.VISIBLE);

        sp_Select_Class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("gsgsgsgsgsg", position + "position ");
                checkPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void checkPosition(int position) {
        if (stringList.size() != 0) {
            selectedName = stringList.get(position);
            Log.d("selectedName", "" + selectedName);
        } else {
            Log.d("selectedName", "Noting found!");
        }
    }


    private void loadclasses() {
        progressbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, NeWayApi.Class_ListApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        for (int i=0;jsonArray.length()>i;i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            String tbl_admin_class_id=object.getString("tbl_admin_class_id");
                            String name=object.getString("name");


                            //Toast.makeText(getApplicationContext(), message+"", Toast.LENGTH_SHORT).show();
                            stringList.add(name);
                            setSpinnerAdapter(stringList);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setSpinnerAdapter(List<String> stringList) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_Select_Class.setAdapter(adapter);

    }

    private void Addclass() {
        stringList.clear();
        progressbar.setVisibility(View.VISIBLE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Add_Class_ListApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressbar.setVisibility(View.GONE);
                Log.e("cheklofcdd",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                      //  Toast.makeText(ClassList.this, message+"", Toast.LENGTH_SHORT).show();
                        download_dialog.dismiss();

                    }else {
                        if (status.equals("0")){
                            Toast.makeText(ClassList.this, message+"", Toast.LENGTH_SHORT).show();
                        }

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
                param.put("name",selectedName );
                param.put("tbl_coaching_id",Mtable_id);

                Log.e("chekcparemmmmm",param+"");
                return param;
            }
        };RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void LoadClassAddedList() {
        mlist.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Added_Class_ListApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("chekcparemmmmm",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if (jsonArray.length()>0){
                        for (int i=0;jsonArray.length()>i;i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            String tbl_class_id=object.getString("tbl_class_id");
                            String tbl_coaching_id=object.getString("tbl_coaching_id");
                            String name=object.getString("name");
                            String class_status=object.getString("class_status");
                           // Toast.makeText(ClassList.this, message+"", Toast.LENGTH_LONG).show();
                            ModelClass modelClass = new ModelClass();
                            modelClass.setTv_Class_Name(name);
                            modelClass.setClass_status(class_status);
                            modelClass.setTbl_class_id(tbl_class_id);
                            mlist.add(modelClass);
                        }
                        adapterClass=new AdapterClass(mlist,getApplicationContext(),ClassList.this);
                        rv_ClassList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                        rv_ClassList.setAdapter(adapterClass);
                        rv_ClassList.setHasFixedSize(true);
                        adapterClass.notifyDataSetChanged();
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
                param.put("tbl_coaching_id",Mtable_id);

                return param;
            }
        };RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    @Override
    public void ListItemDelete(String itemID) {
        deleteListItem(Integer.parseInt(itemID));
    }

    @Override
    public void ListEditItem(String itemID) {

    }


    public void deleteListItem(int itemID){
        Log.e("deleteListItem","this will be detele"+itemID);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Delete_Class_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("chahahaha",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){

                        adapterClass.notifyDataSetChanged();
                       // Toast.makeText(ClassList.this, message+"", Toast.LENGTH_SHORT).show();
                        LoadClassAddedList();
                    }else {
                        adapterClass.notifyDataSetChanged();
                        Toast.makeText(ClassList.this, message+"", Toast.LENGTH_SHORT).show();

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
                parem.put("tbl_class_id",""+itemID);
                parem.put("tbl_coaching_id",Mtable_id);

                Log.e("chahahaha",parem+"");

                return parem;
            }
        };RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);




    }
}