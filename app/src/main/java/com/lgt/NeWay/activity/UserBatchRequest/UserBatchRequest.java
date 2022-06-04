package com.lgt.NeWay.activity.UserBatchRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Neway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserBatchRequest extends AppCompatActivity {
    RecyclerView rv_Userbatchreqlist;
    List<ModelUserBatchRequest>mlist=new ArrayList<>();
    AdapterUserbatchRequest adapterUserbatchRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_batch_request);

        iniViews();
        loaduserrequestbatch();
    }


    private void iniViews() {
        rv_Userbatchreqlist=findViewById(R.id.rv_Userbatchreqlist);
    }
    private void loaduserrequestbatch() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.User_Batch_Request_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                   // String

                    mlist.add(new ModelUserBatchRequest("","",""));

                    adapterUserbatchRequest=new AdapterUserbatchRequest(mlist,getApplicationContext());
                    rv_Userbatchreqlist.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                    rv_Userbatchreqlist.setAdapter(adapterUserbatchRequest);
                    rv_Userbatchreqlist.setHasFixedSize(true);
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

}