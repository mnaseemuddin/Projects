package com.lgt.Ace11.TrakNPayPackage;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.Ace11.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymetPackageActivity extends AppCompatActivity {
    ArrayList<PackModel> mList = new ArrayList<>();
    RecyclerView rv_package_list_items;
    PackageAdapter packageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_package_list);
        Log.d("package", "package_list");
        Toast.makeText(this, "PaymetPackageActivity", Toast.LENGTH_SHORT).show();
        initView();
        callPackageList();
    }

    private void initView() {
        rv_package_list_items = findViewById(R.id.rv_package_list_items);
        // not in use
        /*rv_package_list_items.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_package_list_items.setHasFixedSize(true);
        packageAdapter = new PackageAdapter(this, mList);
        rv_package_list_items.setAdapter(packageAdapter);
        Log.d("package", "package_list_end");*/
    }

    private void callPackageList() {
        mList.clear();
        StringRequest request = new StringRequest(Request.Method.GET, "http://dreamcricketers.com/myrest/user/package_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("callSaveTeam", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    String responsecode = jsonObject.getString("responsecode");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (status.equalsIgnoreCase("success")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jb = jsonArray.getJSONObject(i);
                            String tbl_coin_package_id = jb.getString("tbl_coin_package_id");
                            String package_name = jb.getString("package_name");
                            String package_value = jb.getString("package_value");
                            String package_coins = jb.getString("package_coins");
                            PackModel packModel = new PackModel(tbl_coin_package_id, package_name, package_value, package_coins);
                            mList.add(packModel);
                        }
                        setAdapterPack();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("callSaveTeam", "" + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void setAdapterPack() {
        rv_package_list_items.setLayoutManager(new GridLayoutManager(this,2));
        rv_package_list_items.setHasFixedSize(true);
        packageAdapter = new PackageAdapter(this, mList);
        rv_package_list_items.setAdapter(packageAdapter);
    }
}
