package com.lgt.fxtradingleague.MyTabFragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;
import com.lgt.fxtradingleague.TradingAdapter.JoinListViewAdapter;
import com.lgt.fxtradingleague.TradingModel.MyJoinTeamModel;
import com.lgt.fxtradingleague.TradingPackage.ActivityResultTypeContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.lgt.fxtradingleague.Extra.ExtraData.INDIAN_JOIN_CONTEST_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.JOIN_CONTEST_LIST_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.JOIN_WORLD_CONTEST_LEAGUE_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_CHART_TITLE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_CRYPTO_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_INDI_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_WORLD_LEAGUE;
import static com.lgt.fxtradingleague.TradingPackage.ActivityResultTypeContainer.Context_Type;


public class FragmentMyFixtures extends Fragment {
    String UserID="",MatchStatus="0",mEventType="Daily";
    // ActivityResultTypeContainer activity;
    RecyclerView rv_fixtures_event_list;
    // Context context;
    SessionManager sessionManager;
    TextView tv_NoDataAvailable;
    String title="";
    ArrayList<MyJoinTeamModel> myJoinTeamModels=new ArrayList<>();
    JoinListViewAdapter joinListViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_fixtures, container, false);
        // context = activity = (ActivityResultTypeContainer)getActivity();
        sessionManager=new SessionManager();
        initViews(v);
        return v;
    }

    public void initViews(View v){
        if (sessionManager.getUser(getActivity()).getUser_id() != null){
            UserID=sessionManager.getUser(getActivity()).getUser_id();
        }
        rv_fixtures_event_list = v.findViewById(R.id.rv_fixtures_event_list);
        tv_NoDataAvailable = v.findViewById(R.id.tv_NoDataAvailable);
        rv_fixtures_event_list.setHasFixedSize(true);
        rv_fixtures_event_list.setNestedScrollingEnabled(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_fixtures_event_list.setLayoutManager(mLayoutManager);
        rv_fixtures_event_list.setItemAnimator(new DefaultItemAnimator());
        // load data from server
        Log.d("DataForFixtures","fragment title "+FragmentMyFixtures.class.getSimpleName());
        if (FragmentMyFixtures.class.getSimpleName().equalsIgnoreCase("FragmentMyFixtures")){
            MatchStatus="0";
        }else if(FragmentMyFixtures.class.getSimpleName().equalsIgnoreCase("FragmentMyLive")){
            MatchStatus="1";
        }else if(FragmentMyFixtures.class.getSimpleName().equalsIgnoreCase("FragmentMyResult")){
            MatchStatus="2";
        }
        mEventType = Context_Type;
        Validations.common_log("EventType: "+mEventType);
        if (ActivityResultTypeContainer.Result_Type.equalsIgnoreCase(KEY_WORLD_LEAGUE)){
            Validations.common_log("Recived: World League,"+UserID+","+MatchStatus+","+mEventType);
            if (mEventType.equalsIgnoreCase("")){
                mEventType =ActivityResultTypeContainer.Context_Type;
                getDataForFixtures(JOIN_WORLD_CONTEST_LEAGUE_API,mEventType);
            }else {getDataForFixtures(JOIN_WORLD_CONTEST_LEAGUE_API,mEventType);}
        }else if(ActivityResultTypeContainer.Result_Type.equalsIgnoreCase(KEY_INDI_LEAGUE)){
            Validations.common_log("Recived: NASDAQ 200,"+UserID+","+MatchStatus+","+mEventType);
            if (mEventType.equalsIgnoreCase("")){
                mEventType =ActivityResultTypeContainer.Context_Type;
                getDataForFixtures(INDIAN_JOIN_CONTEST_LIST_API,mEventType);
            }else {
               getDataForFixtures(INDIAN_JOIN_CONTEST_LIST_API,mEventType);
            }
        }else if(ActivityResultTypeContainer.Result_Type.equalsIgnoreCase(KEY_CRYPTO_LEAGUE)){
            Validations.common_log("Recived: CRYPTO 100,"+UserID+","+MatchStatus+","+mEventType);
            if (mEventType.equalsIgnoreCase("")){
                mEventType =ActivityResultTypeContainer.Context_Type;
                getDataForFixtures(JOIN_CONTEST_LIST_API,mEventType);
            }else {getDataForFixtures(JOIN_CONTEST_LIST_API,mEventType);}
        }
    }

    public String getTitle() {
        if( title == null ) {
            title = getArguments().getString(KEY_CHART_TITLE);
        }
        return title;
    }

    private void getDataForFixtures(String URL_RECIEVED, final String mEvent) {
        Log.d("DataForFixtures",""+URL_RECIEVED +", "+ mEvent);
        Validations.showProgress(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RECIEVED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DataForFixtures",""+response);
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        JSONArray innerData = jsonObject.getJSONArray("data");
                        for (int i=0;i<innerData.length();i++){
                            tv_NoDataAvailable.setVisibility(View.GONE);
                            Validations.hideProgress();
                            JSONObject data = innerData.getJSONObject(i);
                            String main_contest_name = data.getString("main_contest_name");
                            String tbl_contest_id = data.getString("tbl_contest_id");
                            String contest_name = data.getString("contest_name");
                            String winner = data.getString("winner");
                            String prize_pool = data.getString("prize_pool");
                            String total_team = data.getString("total_team");
                            String join_team = data.getString("join_team");
                            String entry_fee = data.getString("entry_fee");
                            String cont_time = data.getString("cont_time");
                            String joing_date = data.getString("joing_date");
                            MyJoinTeamModel myJoinTeamModel = new MyJoinTeamModel();
                            myJoinTeamModel.setTbl_contest_id(tbl_contest_id);
                            myJoinTeamModel.setMain_contest_name(main_contest_name);
                            myJoinTeamModel.setCont_time(cont_time);
                            myJoinTeamModel.setEntry_fee(entry_fee);
                            myJoinTeamModel.setJoin_team(join_team);
                            myJoinTeamModel.setTotal_team(total_team);
                            myJoinTeamModel.setContest_name(contest_name);
                            myJoinTeamModel.setWinner(winner);
                            myJoinTeamModel.setPrize_pool(prize_pool);
                            myJoinTeamModel.setJoing_date(joing_date);
                            myJoinTeamModels.add(myJoinTeamModel);
                        }
                        setAdapterToRv();
                    }else if(status.equalsIgnoreCase("0")){
                        Validations.hideProgress();
                        tv_NoDataAvailable.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Validations.hideProgress();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.hideProgress();
                Log.d("DataForFixtures",""+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("user_id",UserID);
                param.put("match_status",MatchStatus);
                param.put("main_contest_name",mEvent);
                Log.d("DataForFixtures",""+param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void setAdapterToRv() {
        rv_fixtures_event_list.setHasFixedSize(true);
        joinListViewAdapter = new JoinListViewAdapter(getActivity(),myJoinTeamModels,UserID,MatchStatus);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rv_fixtures_event_list.setLayoutManager(linearLayoutManager);
        rv_fixtures_event_list.setAdapter(joinListViewAdapter);
    }

}
