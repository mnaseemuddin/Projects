package com.lgt.fxtradingleague;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Model.ModelPlayerInformation;

public class ActivityPlayersInformation extends AppCompatActivity {

    //Same activity was opened from MyTeamListActivity earlier, code commented

    private TabLayout tabs;
    private ViewPager frameLayout;

    private ImageView ivBackToolbar;

    private ProgressBar pbPlayerInformation;
    private List<ModelPlayerInformation> list;

    PagerAdapterPlayerInformation adapter;

    public  static  int hhh=0;

    private String mUniqueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_information);

        frameLayout = findViewById(R.id.frameLayout);
        tabs = findViewById(R.id.tabs);
        pbPlayerInformation = findViewById(R.id.pbPlayerInformation);
        ivBackToolbar = findViewById(R.id.ivBackToolbar);
        ivBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent getUniqueID = getIntent();
        if (getUniqueID != null) {
            if (getUniqueID.hasExtra("KEY_UNIQUE_ID")) {
                mUniqueID = getUniqueID.getStringExtra("KEY_UNIQUE_ID");

                Log.e("Asdderfewr", mUniqueID + "");
                loadDataPlayer(mUniqueID);
            }

        }

    }

    private void loadDataPlayer(String uniqueID) {

        Log.e("Asrerwerwer", uniqueID + "");

       //uniqueID = "43762";

        list = new ArrayList<>();
        pbPlayerInformation.setVisibility(View.VISIBLE);

        String urlTest = "https://rest.entitysport.com/v2/matches/" + uniqueID + "/point?token=efff469cfc49e4a109cc057bd4f58a6e";

        Log.e("asdderewrwe", urlTest + "");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlTest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbPlayerInformation.setVisibility(View.GONE);
                Log.e("joijojioiojo", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {

                        JSONObject getStatus = jsonObject.getJSONObject("response");
                        String matchsttt = getStatus.getString("status_str");
                        Log.e("asdrewrwerwe",matchsttt+"");

                        if(matchsttt.equals("Scheduled")){
                            Toast.makeText(ActivityPlayersInformation.this, "Match not started yet, please try later", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            JSONObject object = jsonObject.getJSONObject("response").getJSONObject("points").getJSONObject("teama");
                            JSONArray jsonArray = object.getJSONArray("playing11");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject playerData = jsonArray.getJSONObject(i);

                                String name = playerData.getString("name");
                                String role = playerData.getString("role");
                                String rating = playerData.getString("rating");
                                String point = playerData.getString("point");
                                String starting11 = playerData.getString("starting11");
                                String run = playerData.getString("run");
                                String four = playerData.getString("four");
                                String six = playerData.getString("six");
                                String sr = playerData.getString("sr");
                                String fifty = playerData.getString("fifty");
                                String duck = playerData.getString("duck");
                                String wkts = playerData.getString("wkts");
                                String maidenover = playerData.getString("maidenover");
                                String er = playerData.getString("er");
                                String catches = playerData.getString("catch");
                                String thirty = playerData.getString("thirty");
                                String bonus = playerData.getString("bonus");

                                list.add(new ModelPlayerInformation(name, role, rating, point, starting11, run, four, six, sr, fifty, duck, wkts, maidenover, er, catches, thirty, bonus));

                            }

                            for (int k = 0; k < list.size(); k++) {
                                tabs.addTab(tabs.newTab().setText("Ranjan" + k));
                            }

                            adapter  = new PagerAdapterPlayerInformation
                                    (getSupportFragmentManager(), tabs.getTabCount(), list, 0);
                            frameLayout.setAdapter(adapter);


                            Log.e("dasdsarewrwer",tabs.getTabCount()+""+list.size()+"");
                            frameLayout.setClipToPadding(false);
                            frameLayout.setPadding(16, 0, 60, 0);


                            Log.e("asdrwerwerwer",list.size()+"");
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbPlayerInformation.setVisibility(View.GONE);
                Toast.makeText(ActivityPlayersInformation.this, "Some error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityPlayersInformation.this);
        requestQueue.add(stringRequest);


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
