package com.lgt.NeWay.activity.SubjectList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.lgt.NeWay.CustomeInterface.SubjectListClickRefresh;
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

public class SubjectList extends AppCompatActivity implements SubjectListClickRefresh {

    RecyclerView rv_SubjectList;
    AdapterSubjectList adapterSubjectList;
    List<ModelSubject> mLIst = new ArrayList<>();
    Context context;
    String selectedName = "";
    Spinner sp_Select_Subject;
    RelativeLayout rl_AddSubjectContainer;
    List<String> stringList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button bt_cancel, bt_submitsubject;
    private AlertDialog download_dialog;
    public static String Ouserid;
    private SubjectList actvity;
    ImageView ivBackFullDescription;
    private AdapterSubjectList.Cityholder cityholder;

    public static List<String> statusList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        context=actvity=this;
        statusList.clear();
        statusList.add("Active");
        statusList.add("Inactive");

        iniSharedprefrence();
        iniviews();
        onclicklistener();
        loadSubjectlist();


    }

    private void iniSharedprefrence() {
        SharedPreferences sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Ouserid = sharedPreferences.getString("tbl_coaching_id", "");
    }


    private void iniviews() {
        bt_cancel = findViewById(R.id.bt_cancel);
        rv_SubjectList = findViewById(R.id.rv_SubjectList);
        bt_submitsubject = findViewById(R.id.bt_submitsubject);
        ivBackFullDescription=findViewById(R.id.ivBackFullDescription);
        rl_AddSubjectContainer = findViewById(R.id.rl_AddSubjectContainer);
    }


    private void onclicklistener() {
        rl_AddSubjectContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sdfdfssfs", "rewrwerwerew");
                opendilough();

            }
        });
        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void opendilough() {
        View customView = LayoutInflater.from(this).inflate(R.layout.subjectadddilough, null);
        download_dialog = new AlertDialog.Builder(this).create();
        download_dialog.setView(customView);
        download_dialog.setCanceledOnTouchOutside(false);
        sp_Select_Subject = customView.findViewById(R.id.sp_Select_Subject);
        bt_submitsubject = customView.findViewById(R.id.bt_submitsubject);
        bt_cancel = customView.findViewById(R.id.bt_cancel);
        //
        loadsubjectNames();
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download_dialog.dismiss();
            }
        });

        bt_submitsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedName.equals("")) {
                    Log.d("setOnClickListener", "setOnClickListener Nothing");
                } else {
                    Log.d("setOnClickListener", "select Name is : " + selectedName);
                    addSubject();
                }
            }
        });

        download_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loadSubjectlist();
            }
        });
        download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        download_dialog.show();
        onitemselectListener();
    }

    private void addSubject() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Add_Subject_ListApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Toast.makeText(SubjectList.this, message + "", Toast.LENGTH_SHORT).show();
                        download_dialog.dismiss();
                    } else {
                        Toast.makeText(SubjectList.this, message + "", Toast.LENGTH_SHORT).show();
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
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("name", selectedName);
                parem.put("tbl_coaching_id", Ouserid);

                Log.e("chcbhdbchdhbcbc", parem + "");
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void loadsubjectNames() {
        stringList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, NeWayApi.Subject_ListApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checkreeeeeeeeeeeeee", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 1) {
                        for (int i = 0; jsonArray.length() > i; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String tbl_subject_admin_id = object.getString("tbl_subject_admin_id");
                            String name = object.getString("name");
                            stringList.add(name);
                            setSpinnerAdapter(stringList);
                            Toast.makeText(SubjectList.this, message + "", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    private void onitemselectListener() {
        sp_Select_Subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

    private void loadSubjectlist() {
        mLIst.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Added_Subject_ListApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("chddddddddddd", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; jsonArray.length() > i; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String tbl_subject_id = object.getString("tbl_subject_id");
                            String tbl_coaching_id = object.getString("tbl_coaching_id");
                            String name = object.getString("name");
                            String subject_status = object.getString("subject_status");


                            Toast.makeText(SubjectList.this, message + "", Toast.LENGTH_SHORT).show();
                            mLIst.add(new ModelSubject(name, "", subject_status, "", "", tbl_subject_id));
                        }
                        adapterSubjectList = new AdapterSubjectList(mLIst, getApplicationContext(), SubjectList.this);
                        rv_SubjectList.setAdapter(adapterSubjectList);
                        rv_SubjectList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        rv_SubjectList.setHasFixedSize(true);
                        adapterSubjectList.notifyDataSetChanged();
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
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_coaching_id", Ouserid);


                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    private void setSpinnerAdapter(List<String> stringList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Select_Subject.setAdapter(adapter);
    }


    @Override
    public void subjectListRefresh(String tblId, int positon, AdapterSubjectList.Cityholder cityhol) {
        cityholder=cityhol;
        deleteSubject(tblId,positon);
        //Toast.makeText(this, "check", Toast.LENGTH_LONG).show();
    }

    private void deleteSubject(String board_id,int positon) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Subject_List_DeleteApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (cityholder!=null){
                    cityholder.iv_delete.setEnabled(true);
                }
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String Status = jsonObject.getString("status");
                    if (Status.equals("1")) {

                        notifyAdapter(positon);
                        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (cityholder!=null){
                    cityholder.iv_delete.setEnabled(true);
                }
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_coaching_id", Ouserid);
                parem.put("tbl_subject_id", board_id);
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void notifyAdapter(int positon) {
        mLIst.remove(positon);
        adapterSubjectList.notifyItemRemoved(positon);
        adapterSubjectList.notifyItemRangeRemoved(positon, mLIst.size());
    }

}