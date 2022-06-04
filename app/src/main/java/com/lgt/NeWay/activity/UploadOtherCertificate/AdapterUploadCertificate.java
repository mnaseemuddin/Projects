package com.lgt.NeWay.activity.UploadOtherCertificate;

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
import com.lgt.NeWay.activity.ClassList.ClassList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterUploadCertificate extends RecyclerView.Adapter<AdapterUploadCertificate.Cityholder> {
    List<MOdelUploadCertificate> Ulist;
    Context context;
    DeleteCertificateInterface deleteCertificateInterface;
    SharedPreferences sharedPreferences;
    String Mtable_id;

    public AdapterUploadCertificate(List<MOdelUploadCertificate> Mlist, Context context,DeleteCertificateInterface deleteCertificateInterface) {
        this.Ulist = Mlist;
        this.context = context;
        this.deleteCertificateInterface = deleteCertificateInterface;
    }

    @NonNull
    @Override
    public Cityholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        iniSharedpref();
        View view = LayoutInflater.from(context).inflate(R.layout.adapteruploadcertificate, parent, false);
        return new Cityholder(view);
    }

    private void iniSharedpref() {
        sharedPreferences=context.getSharedPreferences(common.UserData,Context.MODE_PRIVATE);
        Mtable_id=sharedPreferences.getString("tbl_coaching_id","");
    }

    @Override
    public void onBindViewHolder(@NonNull Cityholder holder, int position) {
        String id=Ulist.get(position).getTbl_upload_other_certificate_id();

        holder.tv_TeacherName.setText(Ulist.get(position).getTv_TeacherName());
        holder.tv_tvContactNo.setText(Ulist.get(position).getTv_tvContactNo());
        holder.tv_Title_Name.setText(Ulist.get(position).getTv_Title_Name());
        Glide.with(context).load(Ulist.get(position).getIv_certificate()).into(holder.iv_certificate);

        setAdapterstatus(holder.sp_Statuspending,Integer.parseInt(Ulist.get(position).getCertificate_status()));

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCertificateInterface.deletecertificate(id);

            }
        });

        holder.sp_Statuspending.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long Uid) {
                changestatus(position,id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

   private void changestatus(int status, String id) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.Edit_Certificate_Status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ajjjjjjjjj",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("0")){
                        Toast.makeText(context, message+"", Toast.LENGTH_LONG).show();
                    }else {
                        if (status.equals("1")){
                            Toast.makeText(context, message+"", Toast.LENGTH_LONG).show();
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
                param.put("tbl_upload_other_certificate_id", id);
                param.put("status", "" + status);
                Log.d("getParams", "getParams: " + param);

                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private void setAdapterstatus(Spinner sp_statuspending, int pos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, UploadOtherCertificate.statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_statuspending.setAdapter(adapter);// set the selected value of the spinner
        sp_statuspending.setSelection(pos);
    }

    @Override
    public int getItemCount() {
        return Ulist.size();
    }

    public class Cityholder extends RecyclerView.ViewHolder {
        TextView tv_TeacherName, tv_Title_Name, tv_tvContactNo;
        Spinner sp_Statuspending;
        ImageView iv_delete, iv_certificate;

        public Cityholder(@NonNull View itemView) {
            super(itemView);

            tv_TeacherName = itemView.findViewById(R.id.tv_TeacherName);
            tv_Title_Name = itemView.findViewById(R.id.tv_Title_Name);
            tv_tvContactNo = itemView.findViewById(R.id.tv_tvContactNo);
            sp_Statuspending = itemView.findViewById(R.id.sp_Statuspending);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_certificate = itemView.findViewById(R.id.iv_certificate);
        }
    }
}
