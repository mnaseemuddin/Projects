package com.lgt;


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
import com.lgt.fxtradingleague.Playing11;
import com.lgt.fxtradingleague.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.lgt.fxtradingleague.Adapter.AdapterPlaying11;
import Model.ModelPlaying11;

public class FragmentTeamB extends Fragment {

    private ProgressBar pbTeamB;
    private RecyclerView rvPlaying11TeamB;
    private List<ModelPlaying11> list;
    private AdapterPlaying11 adapterPlaying11;

    private String mUniqueMatchID;

    public FragmentTeamB() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_b, container, false);

        rvPlaying11TeamB = view.findViewById(R.id.rvPlaying11TeamB);
        pbTeamB = view.findViewById(R.id.pbTeamB);


        mUniqueMatchID = Playing11.mUniqueID;

        if(mUniqueMatchID!=null){
            loadTeamB();
        }

        else {
            Toast.makeText(getActivity(), "Sorry, Couldn't load playing 11", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void loadTeamB() {

        pbTeamB.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        list.clear();


        String playing11New = "https://rest.entitysport.com/v2/matches/"+mUniqueMatchID+"/point?token=efff469cfc49e4a109cc057bd4f58a6e";


        //String url = "https://rest.entitysport.com/v2/matches/"+mUniqueMatchID+"/squads?token=efff469cfc49e4a109cc057bd4f58a6e";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, playing11New, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbTeamB.setVisibility(View.GONE);

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
                            JSONObject object = jsonObject.getJSONObject("response").getJSONObject("points").getJSONObject("teamb");

                            JSONArray jsonArray = object.getJSONArray("playing11");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject data = jsonArray.getJSONObject(i);

                                String playerName = data.getString("name");
                                list.add(new ModelPlaying11(playerName));

                                adapterPlaying11 = new AdapterPlaying11(list,getActivity());

                                rvPlaying11TeamB.hasFixedSize();
                                rvPlaying11TeamB.setNestedScrollingEnabled(false);
                                rvPlaying11TeamB.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
                                rvPlaying11TeamB.setAdapter(adapterPlaying11);

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
                pbTeamB.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Network or connection error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

}
