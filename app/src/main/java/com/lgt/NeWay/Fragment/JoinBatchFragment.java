package com.lgt.NeWay.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.NeWay.BatchListFragment.AdapterUserAppliedBatchlist;
import com.lgt.NeWay.BatchListFragment.BatchListFragment;
import com.lgt.NeWay.BatchListFragment.ModelUserAppliedJob;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.Fragment.Adapter.AdapterJoinBatch;
import com.lgt.NeWay.Fragment.Adapter.ModelJoinBatch;
import com.lgt.NeWay.Neway.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class JoinBatchFragment extends Fragment {

    RecyclerView rv_join_batch;

    AdapterJoinBatch adapterJoinBatch;
    private SharedPreferences sharedPreferences;
    String Mtable_id;
    List<ModelJoinBatch> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_batch, container, false);

        rv_join_batch = view.findViewById(R.id.rv_join_batch);
        iniSharedpref();
        loadApiData();
        return view;
    }
    private void iniSharedpref() {
        sharedPreferences = requireContext().getSharedPreferences(common.UserData, Context.MODE_PRIVATE);
        Mtable_id = sharedPreferences.getString("tbl_coaching_id", "");
    }

    private void loadApiData() {

        list.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, NeWayApi.User_Join_Request, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("checknow",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                       if (jsonArray.length()>0){
                           for (int i=0;jsonArray.length()>i;i++){

                           }
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
        }); RequestQueue requestQueue=Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

    }

}






