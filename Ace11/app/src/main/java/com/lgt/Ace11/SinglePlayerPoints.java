package com.lgt.Ace11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.ModelTeamA;
import Model.ModelTeamB;

public class SinglePlayerPoints extends AppCompatActivity {


    private TextView tvSelectedBy, tvPointsPlayerInfo, tvCredits,
            tvActualStarting, tvPointsStarting, tvRunsActual,
            tvRunPoints, tvFoursActual, tvFoursPoints, tvCatchesActual,tvCatchesPoints,tvSixACtual, tvSixPoints, tvSrActuals, tvSrPoints, tvFiftyActual, tvFiftyPoints, tvDuckActual,
            tvDuckPoints, tvWktsActual, tvWktsPoints,
            tvMaidenActual, tvMaidenPoints,tvToolbarTitle;

    private TextView tvPlayerNameInfo;

    private ProgressBar pbSinglePlayerPoint;
    private ImageView ivBackToolbar;

    private String newPID,mMatchUniqueID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_points);


        tvPlayerNameInfo =findViewById(R.id.tvPlayerNameInfo);

        tvSelectedBy = findViewById(R.id.tvSelectedBy);
        tvPointsPlayerInfo = findViewById(R.id.tvPointsPlayerInfo);
        tvCredits = findViewById(R.id.tvCredits);
        tvActualStarting = findViewById(R.id.tvActualStarting);
        tvPointsStarting = findViewById(R.id.tvPointsStarting);
        tvRunsActual = findViewById(R.id.tvRunsActual);
        tvRunPoints = findViewById(R.id.tvRunPoints);
        tvFoursActual = findViewById(R.id.tvFoursActual);
        tvFoursPoints =findViewById(R.id.tvFoursPoints);
        tvSixACtual = findViewById(R.id.tvSixACtual);
        tvSixPoints = findViewById(R.id.tvSixPoints);
        tvSrActuals = findViewById(R.id.tvSrActuals);
        tvSrPoints = findViewById(R.id.tvSrPoints);
        tvFiftyActual =findViewById(R.id.tvFiftyActual);
        tvFiftyPoints = findViewById(R.id.tvFiftyPoints);
        tvDuckActual = findViewById(R.id.tvDuckActual);
        tvDuckPoints = findViewById(R.id.tvDuckPoints);
        tvWktsActual = findViewById(R.id.tvWktsActual);
        tvCatchesActual = findViewById(R.id.tvCatchesActual);
        tvWktsPoints = findViewById(R.id.tvWktsPoints);
        tvMaidenActual = findViewById(R.id.tvMaidenActual);
        tvMaidenPoints = findViewById(R.id.tvMaidenPoints);
        pbSinglePlayerPoint = findViewById(R.id.pbSinglePlayerPoint);

        ivBackToolbar = findViewById(R.id.ivBackToolbar);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);


        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("KEY_NEW_PID")){
                newPID = intent.getStringExtra("KEY_NEW_PID");
                mMatchUniqueID = intent.getStringExtra("KEY_MATCH_UNIQUE_ID");
                Log.e("asdasdrere",newPID+""+mMatchUniqueID);
                loadTeamPoints(newPID,mMatchUniqueID);
            }
        }

        ivBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void loadTeamPoints(final String newPID, String mMatchUniqueID) {
        Log.e("asdasdrere",newPID+""+mMatchUniqueID);
        pbSinglePlayerPoint.setVisibility(View.VISIBLE);

        String urlTeam = "https://rest.entitysport.com/v2/matches/"+mMatchUniqueID+"/point?token=efff469cfc49e4a109cc057bd4f58a6e";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlTeam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbSinglePlayerPoint.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equalsIgnoreCase("ok")){

                        JSONObject responseObjectA = jsonObject.getJSONObject("response").getJSONObject("points").getJSONObject("teama");
                        JSONArray jsonArrayA = responseObjectA.getJSONArray("playing11");


                        JSONObject responseObjectB = jsonObject.getJSONObject("response").getJSONObject("points").getJSONObject("teamb");
                        JSONArray jsonArrayB = responseObjectB.getJSONArray("playing11");

                        JSONObject getTitle = jsonObject.getJSONObject("response");
                        String matchTitle = getTitle.getString("title");
                        tvToolbarTitle.setText(matchTitle);

                        for(int i=0;i<jsonArrayA.length();i++){
                            JSONObject teamA = jsonArrayA.getJSONObject(i);

                            Gson gsonA = new Gson();
                            ModelTeamA modelTeamA = gsonA.fromJson(String.valueOf(teamA),ModelTeamA.class);

                            if(modelTeamA.getPid().equalsIgnoreCase(newPID)){
                                Log.e("dsadsadasdassdd","fouidudias");
                                Log.e("ssss",modelTeamA.getPid()+"");

                                tvPlayerNameInfo.setText(modelTeamA.getName());
                                tvPointsPlayerInfo.setText(modelTeamA.getPoint());

                                tvActualStarting.setText(modelTeamA.getStarting11());
                                tvRunsActual.setText(modelTeamA.getRun());
                                tvFoursActual.setText(modelTeamA.getFour());
                                tvSixACtual.setText(modelTeamA.getSix());
                                tvCatchesActual.setText(modelTeamA.getCatch());
                                tvSrActuals.setText(modelTeamA.getSr());
                                tvFiftyActual.setText(modelTeamA.getFifty());
                                tvDuckActual.setText(modelTeamA.getDuck());
                                tvWktsActual.setText(modelTeamA.getWkts());
                                tvMaidenActual.setText(modelTeamA.getMaidenover());
                            }
                        }


                        for(int i=0;i<jsonArrayB.length();i++){
                            JSONObject teamB = jsonArrayB.getJSONObject(i);

                            Gson gsonB = new Gson();
                            ModelTeamB modelTeamB = gsonB.fromJson(String.valueOf(teamB),ModelTeamB.class);

                            if(modelTeamB.getPid().equalsIgnoreCase(newPID)){
                                Log.e("foundindb","dddddddd");
                                Log.e("dddddsss",modelTeamB.getPid()+"");

                                tvPointsPlayerInfo.setText(modelTeamB.getPoint());
                                tvPlayerNameInfo.setText(modelTeamB.getName());
                                tvActualStarting.setText(modelTeamB.getStarting11());
                                tvRunsActual.setText(modelTeamB.getRun());
                                tvFoursActual.setText(modelTeamB.getFour());
                                tvSixACtual.setText(modelTeamB.getSix());
                                tvCatchesActual.setText(modelTeamB.getCatch());
                                tvSrActuals.setText(modelTeamB.getSr());
                                tvFiftyActual.setText(modelTeamB.getFifty());
                                tvDuckActual.setText(modelTeamB.getDuck());
                                tvWktsActual.setText(modelTeamB.getWkts());
                                tvMaidenActual.setText(modelTeamB.getMaidenover());


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
                pbSinglePlayerPoint.setVisibility(View.GONE);
                Toast.makeText(SinglePlayerPoints.this, "Server or network error", Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(SinglePlayerPoints.this);
        requestQueue.add(stringRequest);

    }
}
