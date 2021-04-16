package com.lgt.NeWay.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lgt.NeWay.Fragment.Adapter.AdapterUserAppliedBatchlist;
import com.lgt.NeWay.Fragment.Model.ModelUserAppliedJob;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.Addbatches;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BatchListFragment extends Fragment {
    RelativeLayout rv_Addbatches;
    List<ModelUserAppliedJob> modelUserAppliedJobList = new ArrayList<>();
    AdapterUserAppliedBatchlist adapterUserAppliedBatchlist;
    RecyclerView rv_userbatchlist;
    SharedPreferences sharedPreferences;
    String Mtable_id;
    Fragment FragmentDashboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_batchlist, container, false);

        iniSharedpref();
        rv_userbatchlist = view.findViewById(R.id.rv_userbatchlist);
        rv_Addbatches = view.findViewById(R.id.rv_Addbatches);


        rv_Addbatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Addbatches.class);
                startActivity(intent);
            }
        });


        return view;
    }
    private void iniSharedpref() {
        sharedPreferences=getContext().getSharedPreferences(common.UserData, Context.MODE_PRIVATE);
        Mtable_id=sharedPreferences.getString("tbl_coaching_id","");
    }
    private void loadlist() {
        modelUserAppliedJobList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.BatchList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; jsonArray.length() > i; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String tbl_batches_id = object.getString("tbl_batches_id");
                                String batch_name = object.getString("batch_name");
                                String tbl_coaching_id = object.getString("tbl_coaching_id");
                                String subject_name = object.getString("subject_name");
                                String BatchStatus = object.getString("status");

                                modelUserAppliedJobList.add(new ModelUserAppliedJob(batch_name,BatchStatus));
                                Toast.makeText(getContext(), message+"", Toast.LENGTH_LONG).show();
                            }
                            adapterUserAppliedBatchlist = new AdapterUserAppliedBatchlist(modelUserAppliedJobList, getContext());
                            rv_userbatchlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            rv_userbatchlist.setHasFixedSize(true);
                            rv_userbatchlist.setAdapter(adapterUserAppliedBatchlist);
                            adapterUserAppliedBatchlist.notifyDataSetChanged();
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
                param.put("tbl_coaching_id",Mtable_id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onResume() {
        loadlist();
        super.onResume();
    }
}