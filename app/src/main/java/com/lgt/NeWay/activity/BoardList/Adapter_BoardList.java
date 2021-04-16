package com.lgt.NeWay.activity.BoardList;

import android.content.Context;
import android.content.Intent;
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
import com.lgt.NeWay.CustomeInterface.ClicktoRefresh;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.Extra.NeWayApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lgt.NeWay.Extra.common.UserData;

public class Adapter_BoardList extends RecyclerView.Adapter<Adapter_BoardList.Cityholder> {
    List<ModelBoardList> modelBoardList;
    List<String> mStatusList;
    Context context;
    ClicktoRefresh clicktoRefresh;
    String compareValue = "some value";
    Intent intent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Uboardid, Ucochingid, Utbl_board_id;
    String status = "";


    public Adapter_BoardList(List<ModelBoardList> modelBoardList, List<String> mList, Context context, ClicktoRefresh click) {
        this.modelBoardList = modelBoardList;
        this.context = context;
        this.mStatusList = mList;
        this.clicktoRefresh = click;
    }

    @NonNull
    @Override
    public Adapter_BoardList.Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_boardlist, parent, false);
        iniSharedprefrense();
        return new Cityholder(view);
    }

    private void iniSharedprefrense() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(UserData, Context.MODE_PRIVATE);
        Ucochingid = sharedPreferences.getString("tbl_coaching_id", "");


    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_BoardList.Cityholder holder, int position) {
        String tbl_id = modelBoardList.get(position).getTbl_board_id();
        holder.tv_Board_Name.setText(modelBoardList.get(position).getTv_Board_Name());
        setAdapterStatus(holder.sp_Statuspending, Integer.parseInt(modelBoardList.get(position).getStatus()));
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBoard(modelBoardList.get(position).getTbl_board_id());
            }
        });


        holder.sp_Statuspending.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changestatus(position, tbl_id);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void checkPosition(int position) {
        status = mStatusList.get(position);
    }

    private void changestatus(int status, String bord_id) {
        Log.d("changestatus", status + "  ?    " + bord_id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Edit_Board_Status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String board_status = jsonObject.getString("board_status");
                    String status = jsonObject.getString("status");
                    if (board_status.equals("1")) {
                        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
                        //clicktoRefresh.ListRefresh();
                    } else {
                        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse", "" + error);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_board_id", bord_id);
                parem.put("tbl_coaching_id", Ucochingid);
                parem.put("status", "" + status);
                Log.d("getParams", "" + parem);
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void deleteBoard(String board_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Delete_Board, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        clicktoRefresh.ListRefresh();

                        notifyDataSetChanged();
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

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_board_id", board_id);
                parem.put("tbl_coaching_id", Ucochingid);
                Log.e("mssssdfdfdfdfdf", parem + "");
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void setAdapterStatus(Spinner sp_statuspending, int pos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, BaordList.statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_statuspending.setAdapter(adapter);// set the selected value of the spinner
        sp_statuspending.setSelection(pos);
    }

    @Override
    public int getItemCount() {
        return modelBoardList.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        private TextView tv_Board_Name;
        Spinner sp_Statuspending;
        private ImageView iv_Edit, iv_delete, ivBackFullDescription;
        ;

        public Cityholder(@NonNull View itemView) {
            super(itemView);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            ivBackFullDescription = itemView.findViewById(R.id.ivBackFullDescription);
            tv_Board_Name = itemView.findViewById(R.id.tv_Board_Name);
            sp_Statuspending = itemView.findViewById(R.id.sp_Statuspending);
            iv_Edit = itemView.findViewById(R.id.iv_Edit);
        }
    }
}
