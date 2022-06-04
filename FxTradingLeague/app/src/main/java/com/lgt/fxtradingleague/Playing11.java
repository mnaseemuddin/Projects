package com.lgt.fxtradingleague;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lgt.fxtradingleague.Adapter.PagerAdapterPlaying11;

public class Playing11 extends AppCompatActivity {

    private TabLayout FragmentTabLayoutPlaying11;
    private TabItem tab_TeamA,tab_TeamB;
    private PagerAdapterPlaying11 adapterPlaying11;

    private ImageView ivBackToolbar;

    public static String mUniqueID;

    private ViewPager viewPagerPlaying11;

    private TextView tvToolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing11);

        FragmentTabLayoutPlaying11 = findViewById(R.id.FragmentTabLayoutPlaying11);
        tab_TeamA = findViewById(R.id.tab_TeamA);
        tab_TeamB = findViewById(R.id.tab_TeamB);
        viewPagerPlaying11 = findViewById(R.id.viewPagerPlaying11);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);

        ivBackToolbar = findViewById(R.id.ivBackToolbar);

        ivBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent getUniqueID = getIntent();

        if(getUniqueID!=null){
            if(getUniqueID.hasExtra("KEY_UNIQUE_ID")){
                mUniqueID = getUniqueID.getStringExtra("KEY_UNIQUE_ID");
                Log.e("rerwerwewerwer",mUniqueID);

                if(mUniqueID!=null){
                    //getTeamInformation(mUniqueID);

                    getTeamInformationNew(mUniqueID);
                }
                else {
                    Toast.makeText(Playing11.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                }


            }
        }



        adapterPlaying11 = new PagerAdapterPlaying11(getSupportFragmentManager(),FragmentTabLayoutPlaying11.getTabCount());
        viewPagerPlaying11.setAdapter(adapterPlaying11);


        tvToolbarTitle.setText("Playing 11");



        viewPagerPlaying11.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(FragmentTabLayoutPlaying11));

        FragmentTabLayoutPlaying11.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPagerPlaying11.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void getTeamInformationNew(String mUniqueID) {

        String playing11New = "https://rest.entitysport.com/v2/matches/"+mUniqueID+"/point?token=efff469cfc49e4a109cc057bd4f58a6e";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, playing11New, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if(status.equals("ok")){

                      /*  JSONObject objectTeamA = jsonObject.getJSONObject("response").getJSONObject("points").getJSONObject("teama");
                        JSONObject objectTeamB = jsonObject.getJSONObject("response").getJSONObject("points").getJSONObject("teamb");*/


                        JSONObject teamAID = jsonObject.getJSONObject("response").getJSONObject("teama");
                        JSONObject teamBID = jsonObject.getJSONObject("response").getJSONObject("teamb");

                        String teamAName = teamAID.getString("name");
                        String teamBName = teamBID.getString("name");

                        FragmentTabLayoutPlaying11.getTabAt(0).setText(teamAName);
                        FragmentTabLayoutPlaying11.getTabAt(1).setText(teamBName);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(Playing11.this);
        requestQueue.add(stringRequest);



    }

    private void getTeamInformation(String mUniqueID) {

        String urll = "https://rest.entitysport.com/v2/matches/"+mUniqueID+"/squads?token=efff469cfc49e4a109cc057bd4f58a6e";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    if(status.equals("ok")){

                        JSONObject objectTeamA = jsonObject.getJSONObject("response").getJSONObject("teama");
                        JSONObject objectTeamB = jsonObject.getJSONObject("response").getJSONObject("teamb");

                        String teamA_id = objectTeamA.getString("team_id");
                        String teamB_id = objectTeamB.getString("team_id");

                        Log.e("sdjsakldajsdkla",teamA_id+""+teamB_id+"");


                        JSONObject getArray = jsonObject.getJSONObject("response");
                        JSONArray jsonArray = getArray.getJSONArray("teams");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject data = jsonArray.getJSONObject(i);

                            String teamID = data.getString("tid");

                            if(teamID.equals(teamA_id)){
                                String title = data.getString("title");
                                FragmentTabLayoutPlaying11.getTabAt(0).setText(title);


                                Log.e("jkljkljkljjkl",title+"");
                            }
                           if(teamID.equals(teamB_id)){
                               String title = data.getString("title");
                               FragmentTabLayoutPlaying11.getTabAt(1).setText(title);
                               Log.e("jkljkljkljjkl",title+"");
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
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Playing11.this);
        requestQueue.add(stringRequest);
    }
}
