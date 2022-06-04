package com.lgt.NeWay.activity.ContactList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.telephony.PhoneNumberUtils;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.JobList.ModelJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.validation.Validator;

public class AddContactNumber extends AppCompatActivity implements DeleteContactInterface {
    List<ModelContact> mlist = new ArrayList<>();
    Adaptercontact adaptercontact;
    RecyclerView rv_contactlist;
    SharedPreferences sharedPreferences;
    Button bt_submit;
    EditText et_AddName, et_Addcontact;
    String Mtable_id;
    Boolean isValidPhoneNumber;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_number);


        inisharedpref();
        iniview();
        LoadContactlist();
    }


    private void inisharedpref() {
        sharedPreferences = getSharedPreferences(common.UserData, MODE_PRIVATE);
        Mtable_id = sharedPreferences.getString("tbl_coaching_id", "");

    }


    private void iniview() {
        rv_contactlist = findViewById(R.id.rv_contactlist);
        et_AddName = findViewById(R.id.et_AddName);
        et_Addcontact = findViewById(R.id.et_Addcontact);

        bt_submit = findViewById(R.id.bt_submit);
        iniOnclick();

    }

    private void LoadContactlist() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Contact_ListAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("chechechehceh", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String tbl_coaching_user_contact_number_id = object.getString("tbl_coaching_user_contact_number_id");
                                String mobile = object.getString("mobile");

                                String name = object.getString("name");


                                mlist.add(new ModelContact(name, mobile, tbl_coaching_user_contact_number_id));

                            }
                            adaptercontact = new Adaptercontact(mlist, getApplicationContext(), AddContactNumber.this);

                            rv_contactlist.setAdapter(adaptercontact);
                            //Toast.makeText(AddContactNumber.this, message + "", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(AddContactNumber.this, message + "", Toast.LENGTH_LONG).show();
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
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parem = new HashMap<>();
                parem.put("tbl_coaching_id", Mtable_id);
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void iniOnclick() {

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invalidate();
            }
        });

    }

    private void Invalidate() {
        if (TextUtils.isEmpty(et_AddName.getText())) {
            et_AddName.setError("Please Enter Name");
            et_AddName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(et_Addcontact.getText())) {
            et_Addcontact.setError("Please Enter Number");
            et_Addcontact.requestFocus();
            return;

        }
        if (et_Addcontact.length() < 10) {
            et_Addcontact.setError("Incorrect Number");
            et_Addcontact.requestFocus();
            return;
        }
        Addcontact();
    }

    private void Addcontact() {
        mlist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.AddContact_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("bdshdhbsbdb",response+"");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                      //  Toast.makeText(AddContactNumber.this, message + "", Toast.LENGTH_LONG).show();
                        LoadContactlist();
                        et_AddName.setText("");
                        et_Addcontact.setText("");

                    } else {
                        Toast.makeText(AddContactNumber.this, message + "", Toast.LENGTH_LONG).show();
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
                parem.put("name", et_AddName.getText().toString());
                parem.put("mobile", et_Addcontact.getText().toString());
                return parem;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public void deleteContact(String id) {

        Dcontact(id);

    }

    private void Dcontact(String id) {
        mlist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NeWayApi.Delete_Contact_ListAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ergyegryegryegrg",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){

                       // Toast.makeText(AddContactNumber.this, message+"", Toast.LENGTH_SHORT).show();
                        adaptercontact.notifyDataSetChanged();
                        LoadContactlist();
                    }else {
                        Toast.makeText(AddContactNumber.this, message+"", Toast.LENGTH_SHORT).show();
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
                param.put("tbl_coaching_user_contact_number_id",id);
                Log.e("checjrrererer",param+"");
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}