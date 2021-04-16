package com.lgt.NeWay.activity.BoardList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import com.lgt.NeWay.CustomeInterface.ClicktoRefresh;
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

public class BaordList extends AppCompatActivity implements ClicktoRefresh {

    RecyclerView rv_boardList;
    List<ModelBoardList> list = new ArrayList<>();
    Adapter_BoardList adapter_boardList;
    Context context;
    RelativeLayout rl_AddBoardContainer;
    public  AlertDialog download_dialog, unknown_sources_dialog;
    public static String Oname, Ouserid;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView ivBackFullDescription;
    Dialog dialog;
    String selectedName = "";
    Spinner sp_Select_Board;
    Button bt_submitboard, bt_cancel;

    ProgressBar progressbar;
    List<String> stringList = new ArrayList<>();
    public static List<String> statusList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baord_list);

        rv_boardList = findViewById(R.id.rv_boardList);
        rl_AddBoardContainer = findViewById(R.id.rl_AddBoardContainer);
        ivBackFullDescription = findViewById(R.id.ivBackFullDescription);
        progressbar = findViewById(R.id.progressbar);

        statusList.clear();
        statusList.add("Active");
        statusList.add("Inactive");

        iniShareprefrence();
        Loadboardlistdata();

        ivBackFullDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rl_AddBoardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendilough();
                progressbar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void Loadboardlistdata() {
        progressbar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Add_Board_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("chhhhhhhhhh", response + "");
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    JSONArray array = jsonObject.getJSONArray("data");
                    if (array.length() > 0) {
                        for (int j = 0; array.length() > j; j++) {
                            JSONObject object = array.getJSONObject(j);
                            String tbl_board_id = object.getString("tbl_board_id");
                            String tbl_coaching_id = object.getString("tbl_coaching_id");
                            String Board_name = object.getString("name");
                            String BoardStatus = object.getString("status");
                            Log.e("lllllllllllll",BoardStatus+"");
                            ModelBoardList modelBoardList = new ModelBoardList();
                            modelBoardList.setTbl_board_id(tbl_board_id);
                            modelBoardList.setTbl_coaching_id(tbl_coaching_id);
                            modelBoardList.setTv_Board_Name(Board_name);
                            modelBoardList.setStatus(BoardStatus);
                            list.add(modelBoardList);
                            setDataAdapter(list, stringList);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("checlllllllll", error + "");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_coaching_id", Ouserid);
                Log.e("checlllllllll", parem + "");
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setDataAdapter(List<ModelBoardList> klist, List<String> kstringList) {
        rv_boardList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_boardList.setHasFixedSize(true);
        adapter_boardList = new Adapter_BoardList(klist, kstringList, getApplicationContext(),this);
        rv_boardList.setAdapter(adapter_boardList);
        adapter_boardList.notifyDataSetChanged();
    }

    private void iniShareprefrence() {
        SharedPreferences sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Ouserid = sharedPreferences.getString("tbl_coaching_id", "");
        Oname = sharedPreferences.getString("owner_name", "");
    }

    private void opendilough() {

        View customView = LayoutInflater.from(this).inflate(R.layout.diloughaddboard, null);
        download_dialog = new AlertDialog.Builder(this).create();
        download_dialog.setView(customView);
        download_dialog.setCanceledOnTouchOutside(false);

        sp_Select_Board = customView.findViewById(R.id.sp_Select_Board);
        bt_submitboard = customView.findViewById(R.id.bt_submitboard);
        bt_cancel = customView.findViewById(R.id.bt_cancel);
        ivBackFullDescription = customView.findViewById(R.id.ivBackFullDescription);

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
                    addboard();
                }
            }
        });
        download_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Loadboardlistdata();
            }
        });

        // tv_current_size_progress = customView.findViewById(R.id.tv_current_size_progress);
        download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        download_dialog.show();
        progressbar.setVisibility(View.GONE);


        loadApiBoardList();

        sp_Select_Board.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void addboard() { progressbar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Add_Board, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressbar.setVisibility(View.GONE);
                Log.e("gsgsgsgsgsg", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        String tbl_board_id = object.getString("tbl_board_id");
                        String name = object.getString("name");
                        String tbl_coaching_id = object.getString("tbl_coaching_id");
                        Log.e("checcffsfsfskkc", message);
                        download_dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), message + "", Toast.LENGTH_LONG).show();
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
                Map<String, String> param = new HashMap<>();
                param.put("name", selectedName);
                param.put("tbl_coaching_id", Ouserid);
                Log.e("checkloggg", param.toString() + "");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void loadApiBoardList() {
        progressbar.setVisibility(View.VISIBLE);
        stringList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, NeWayApi.Board_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; jsonArray.length() > i; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String tbl_admin_board_id = object.getString("tbl_admin_board_id");
                            String name = object.getString("name");
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setSpinnerAdapter(List<String> stringList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Select_Board.setAdapter(adapter);
    }

    @Override
    public void ListRefresh() {
        Loadboardlistdata();
    }
}