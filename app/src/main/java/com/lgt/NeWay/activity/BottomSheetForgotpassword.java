package com.lgt.NeWay.activity;

import androidx.annotation.Nullable;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Neway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BottomSheetForgotpassword extends BottomSheetDialogFragment {
    private Button btnForgotPassword;
    private EditText etforgotMobile;
    private TextView tvToolbar;
    Context context;

    String UbtnForgotPassword,UetforgotMobile;

    public BottomSheetForgotpassword() {
    }

    public static void show() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View view = inflater.inflate(R.layout.activity_bottom_sheet_forgotpassword, container, false);

        tvToolbar = view.findViewById(R.id.tvToolbar);
        tvToolbar.setText("Forgot Password");
        etforgotMobile = view.findViewById(R.id.etforgotMobile);
        btnForgotPassword = view.findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });


        return view;


    }

    private void Validation() {
        UetforgotMobile=etforgotMobile.getText().toString();

        if (TextUtils.isEmpty(UetforgotMobile)){
            etforgotMobile.setError("Please Enter Mobile NUmber");
            etforgotMobile.requestFocus();
            return;
        }else {
            Forgotpassword();
        }

    }

    private void Forgotpassword() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, NeWayApi.RESET_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        JSONObject object=jsonObject.getJSONObject("data");
                        String tbl_coaching_id=object.getString("tbl_coaching_id");
                        String password=object.getString("password");
                        String contact_no=object.getString("contact_no");

                        Toast.makeText(getActivity(), message+"", Toast.LENGTH_LONG).show();
                        dismiss();
                    }else {
                        if (status.equals("0")){
                            Toast.makeText(getActivity(), message+"", Toast.LENGTH_LONG).show();
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
                Map<String,String>parem=new HashMap<>();
                parem.put("contact_no",UetforgotMobile);

                Log.e("checkparamhhhhh",parem+"");

                return parem;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}