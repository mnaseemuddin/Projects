package com.lgt.fxtradingleague;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.lgt.fxtradingleague.Adapter.AdapterPlaying11;
import Model.ModelPlaying11;


public class FragmentTeamA extends Fragment {

    private ProgressBar pbPlayingTeamA;
    private RecyclerView rvPlaying11TeamA;

    private List<ModelPlaying11> list;
    private AdapterPlaying11 adapterPlaying11;

    private String mUniqueMatchID;

    public FragmentTeamA() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        pbPlayingTeamA = view.findViewById(R.id.pbPlayingTeamA);
        rvPlaying11TeamA = view.findViewById(R.id.rvPlaying11TeamA);

        mUniqueMatchID = Playing11.mUniqueID;
        if(mUniqueMatchID!=null){
            loadRecyclerView();
        }
        else {
            Toast.makeText(getActivity(), "Sorry, Couldn't load playing 11", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadRecyclerView() {

        pbPlayingTeamA.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        list.clear();


        String playing11New = "https://rest.entitysport.com/v2/matches/"+mUniqueMatchID+"/point?token=efff469cfc49e4a109cc057bd4f58a6e";


      //  String url = "https://rest.entitysport.com/v2/matches/"+mUniqueMatchID+"/squads?token=efff469cfc49e4a109cc057bd4f58a6e";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, playing11New, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbPlayingTeamA.setVisibility(View.GONE);
                Log.e("sdjkljkljklj",response+"");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if(status.equals("ok")){

                        JSONObject check = jsonObject.getJSONObject("response");
                        String status_str = check.getString("status_str");

                        if(status_str.equals("Scheduled")){
                            Toast.makeText(getActivity(), "Match not started yet, please wait", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            JSONObject object = jsonObject.getJSONObject("response").getJSONObject("points").getJSONObject("teama");

                            Log.e("sadrferewrwer",object+"");

                            JSONArray jsonArray = object.getJSONArray("playing11");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);

                                String playerName = data.getString("name");
                                list.add(new ModelPlaying11(playerName));

                                adapterPlaying11 = new AdapterPlaying11(list,getActivity());

                                rvPlaying11TeamA.hasFixedSize();
                                rvPlaying11TeamA.setNestedScrollingEnabled(false);
                                rvPlaying11TeamA.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                                rvPlaying11TeamA.setAdapter(adapterPlaying11);

                            }
                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("dsrewtreerterrt",response+"");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_SHORT).show();
                pbPlayingTeamA.setVisibility(View.GONE);

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

}
