package com.lgt.also_food_court_userApp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.lgt.also_food_court_userApp.Activities.EditProfile;
import com.lgt.also_food_court_userApp.Activities.RegisterActivity;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfile extends Fragment {

    private ProgressBar pbProfile;
   private CircleImageView iv_profile_image;
    private TextView tv_user_name,tv_user_email;
   private SharedPreferences sharedPreferences;
   private SharedPreferences.Editor editor;
   private String user_id;

   private RelativeLayout RL_share,RL_Order_History,RL_Edit_profile,RL_Help,RL_Privacy_policy,RL_Logout,RL_Manage_Address;

    public FragmentProfile() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);

        Initialization(view);

        ApiProfileDataGet();

        return view;

    }

    private void Initialization(View view) {
        RL_share=view.findViewById(R.id.RL_share);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_user_email = view.findViewById(R.id.tv_user_email);
        iv_profile_image = view.findViewById(R.id.iv_profile_image);
        pbProfile= view.findViewById(R.id.pbProfile);
        RL_Order_History= view.findViewById(R.id.RL_Order_History);

        RL_Manage_Address=view.findViewById(R.id.RL_Manage_Address);



        RL_Edit_profile= view.findViewById(R.id.RL_Edit_profile);
        RL_Help= view.findViewById(R.id.RL_Help);
        RL_Privacy_policy= view.findViewById(R.id.RL_Privacy_policy);
        RL_Logout= view.findViewById(R.id.RL_Logout);


        RL_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyToShare();
            }
        });



        RL_Order_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //replaceFragment(new FragmentOrderHistory());
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutProfile,new FragmentOrderHistory()).addToBackStack("FRAGMENT_PROFILE").commit();




            }
        });
        RL_Edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });
        RL_Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        RL_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent in = new Intent(getActivity(), RegisterActivity.class);

                startActivity(in);
                Objects.requireNonNull(getActivity()).finishAffinity();
            }
        });

        RL_Manage_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutProfile,new FragmentAddress()).addToBackStack("FRAGMENT_PROFILE").commit();
            }
        });



    }

    private void readyToShare() {

        String contentToShare="Download "+getString(R.string.app_name)+" app at "+AlsoFoodCourtApi.play_store_url
                +getActivity().getPackageName();

        if (Patterns.WEB_URL.matcher(AlsoFoodCourtApi.play_store_url+getActivity().getPackageName()).matches()){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, contentToShare);

            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share"));
        }else {
            //Commn.myToast(context,"Coming soon...");
            Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
        }

    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, fragment).addToBackStack("my_account").commit();
    }


    private void ApiProfileDataGet() {

        pbProfile.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.GetProfileData, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {

                pbProfile.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        String User_name = object.getString("name");
                        String User_mobile = object.getString("mobile");
                        String User_email = object.getString("email");
                        String User_gender = object.getString("gender");
                        String User_birthday = object.getString("birthday");
                        String total_wallet_amount = object.getString("total_wallet_amount");
                        String USer_image = object.getString("user_image");

                        Log.e("DDDDDSSSS", response + "");

                        tv_user_name.setText(User_name);

                        tv_user_email.setText(User_email);





                        Glide.with(Objects.requireNonNull(getActivity()))
                                .load(USer_image).diskCacheStrategy(DiskCacheStrategy.RESOURCE).
                                apply(new RequestOptions().placeholderOf(R.drawable.user).error(R.drawable.user)
                                        .override(192, 192)).into(iv_profile_image);




                    } else {
                        Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                        pbProfile.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    pbProfile.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbProfile.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                Log.e("HYTGTTG", params + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
