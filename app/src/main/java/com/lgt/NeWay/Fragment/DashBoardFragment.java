package com.lgt.NeWay.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.lgt.NeWay.Fragment.Adapter.AdapterDashBoard;
import com.lgt.NeWay.Fragment.Model.ModelDashBoard;
import com.lgt.NeWay.Neway.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DashBoardFragment extends Fragment {

    ImageView ivBackFullDescription;
    List<ModelDashBoard> modelDashBoardList = new ArrayList<>();
    AdapterDashBoard adapterDashBoard;
    RecyclerView rvDashboardTab;
    SharedPreferences sharedPreferences;
    String Mtable_id;
    TextView tv_countoflistitem, list;
    ImageView iv_dash_listimage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        loadDashboard();

        rvDashboardTab = view.findViewById(R.id.rvDashboardTab);
        ivBackFullDescription = view.findViewById(R.id.ivBackFullDescription);
        tv_countoflistitem = view.findViewById(R.id.tv_countoflistitem);
        list = view.findViewById(R.id.list);
        iv_dash_listimage = view.findViewById(R.id.iv_dash_listimage);

        ivBackFullDescription.setVisibility(View.GONE);

        SharedPreferences  sharedPreferences = getContext().getSharedPreferences(common.UserData, Context.MODE_PRIVATE);
        Mtable_id = sharedPreferences.getString("tbl_coaching_id", "");


        return view;
    }

    private void loadDashboard() {
        modelDashBoardList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.DashBoardAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("bdhshsgdhsgds", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; jsonArray.length() > i; i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String title = jsonObject1.getString("title");
                                String count = jsonObject1.getString("count");
                                String icon = jsonObject1.getString("icon");

                                modelDashBoardList.add(new ModelDashBoard(count, title, icon, id));

                                //Toast.makeText(getContext(), message + "", Toast.LENGTH_LONG).show();
                            }
                        }
                        adapterDashBoard = new AdapterDashBoard(modelDashBoardList, getContext());
                        rvDashboardTab.setLayoutManager(new GridLayoutManager(getContext(),2,RecyclerView.VERTICAL,false));
                        rvDashboardTab.setAdapter(adapterDashBoard);
                        rvDashboardTab.setHasFixedSize(true);
                        adapterDashBoard.notifyDataSetChanged();

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
                parem.put("tbl_coaching_id", Mtable_id);
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}