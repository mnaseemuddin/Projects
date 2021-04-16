package com.lgt.NeWay.activity.SubjectList;

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
import com.lgt.NeWay.CustomeInterface.SubjectListClickRefresh;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.Extra.NeWayApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lgt.NeWay.Extra.common.UserData;

public class AdapterSubjectList extends RecyclerView.Adapter<AdapterSubjectList.Cityholder> {
    List<ModelSubject> list;
    Context context;
    List<String> mStatusList;
    String status = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Uboardid, Ucochingid;
    SubjectListClickRefresh subjectclicktoRefresh;


    public AdapterSubjectList(List<ModelSubject> list, Context context, SubjectListClickRefresh subjectclick) {
        this.list = list;
        this.context = context;
        this.mStatusList = mStatusList;
        this.subjectclicktoRefresh = subjectclick;
    }


    @NonNull
    @Override
    public Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adaptersubjectlist, parent, false);

        iniSharedprefrence();

        return new Cityholder(view);
    }

    private void iniSharedprefrence() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(UserData, Context.MODE_PRIVATE);
        Ucochingid = sharedPreferences.getString("tbl_coaching_id", "");
    }

    @Override
    public void onBindViewHolder(@NonNull Cityholder holder, int position) {
        String tbl_id = list.get(position).getTbl_board_id();
        holder.tv_Subject_Name.setText(list.get(position).getTv_Subject_Name());

         setAdapterStatus(holder.sp_Statuspending, Integer.parseInt(list.get(position).getStatus()));
         Log.d("setAdapterStatus", tbl_id + "  setAdapterStatus: " + list.get(position).getStatus());

        holder.sp_Statuspending.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changestatus(position, tbl_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.iv_delete.setEnabled(false);
                subjectclicktoRefresh.subjectListRefresh(list.get(position).getTbl_board_id(),holder.getAdapterPosition(),holder);
            }
        });

    }


    private void changestatus(int status, String subject_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Edit_Subject_ListStatus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String subject_status = jsonObject.getString("subject_status");
                    if (subject_status.equals("1")) {
                        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_coaching_id", Ucochingid);
                parem.put("tbl_subject_id", subject_id);
                parem.put("status", "" + status);
                Log.d("getParams", "getParams: " + parem);
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private void setAdapterStatus(Spinner sp_statuspending, int pos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, SubjectList.statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_statuspending.setAdapter(adapter);// set the selected value of the spinner
        sp_statuspending.setSelection(pos);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        private TextView tv_Subject_Name;
        Spinner sp_Statuspending;
        public ImageView iv_delete;


        public Cityholder(@NonNull View itemView) {
            super(itemView);

            sp_Statuspending = itemView.findViewById(R.id.sp_Statuspending);
            tv_Subject_Name = itemView.findViewById(R.id.tv_Subject_Name);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
