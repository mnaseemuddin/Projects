package com.lgt.fxtradingleague.MyTabFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
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
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_CRYPTO_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_INDI_LEAGUE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_WORLD_LEAGUE;
import static com.lgt.fxtradingleague.TradingPackage.ActivityResultTypeContainer.Context_Type;


public class FragmentMyLive extends Fragment {

    ActivityResultTypeContainer activity;
    Context context;
    RecyclerView Rv_MyLive;
    String UserID="",MatchStatus="0",mEventType="Daily";
    APIRequestManager apiRequestManager;
    SessionManager sessionManager;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_NoDataAvailable,tv_Score_refresh;
    JoinListViewAdapter LiveListViewAdapter;
    ArrayList<MyJoinTeamModel> myLiveTeamModels=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_live, container, false);
        context = activity = (ActivityResultTypeContainer)getActivity();
        sessionManager = new SessionManager();
        initViews(v);
        Rv_MyLive.setHasFixedSize(true);
        Rv_MyLive.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        Rv_MyLive.setLayoutManager(mLayoutManager);
        Rv_MyLive.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(true);
                                    // callMyLive(false);
                                }
                            }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // callMyLive(false);
            }
        });
        tv_Score_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // callMyLive(true);

            }
        });

        return v;
    }

    public void initViews(View v){
        if (sessionManager.getUser(activity).getUser_id() != null){
            UserID=sessionManager.getUser(activity).getUser_id();
        }
        // load data from server
        Log.d("DataForFixtures","fragment title "+FragmentMyFixtures.class.getSimpleName());
        if (FragmentMyLive.class.getSimpleName().equalsIgnoreCase("FragmentMyFixtures")){
            MatchStatus="0";
        }else if(FragmentMyLive.class.getSimpleName().equalsIgnoreCase("FragmentMyLive")){
            MatchStatus="1";
        }else if(FragmentMyLive.class.getSimpleName().equalsIgnoreCase("FragmentMyResult")){
            MatchStatus="2";
        }
        Rv_MyLive = v.findViewById(R.id.Rv_MyLive);
        tv_NoDataAvailable = v.findViewById(R.id.tv_NoDataAvailable);
        tv_Score_refresh=v.findViewById(R.id.tv_Score_refresh);
        mEventType = Context_Type;
        if (ActivityResultTypeContainer.Result_Type.equalsIgnoreCase(KEY_WORLD_LEAGUE)){
            Validations.common_log("Recived: World League,"+UserID+","+MatchStatus+","+mEventType);
            if (mEventType.equalsIgnoreCase("")){
                mEventType =ActivityResultTypeContainer.Context_Type;
                getDataForLive(JOIN_WORLD_CONTEST_LEAGUE_API,mEventType);
            }else {getDataForLive(JOIN_WORLD_CONTEST_LEAGUE_API,mEventType);}
        }else if(ActivityResultTypeContainer.Result_Type.equalsIgnoreCase(KEY_INDI_LEAGUE)){
            Validations.common_log("Recived: NASDAQ 200,"+UserID+","+MatchStatus+","+mEventType);
            if (mEventType.equalsIgnoreCase("")){
                mEventType =ActivityResultTypeContainer.Context_Type;
                getDataForLive(INDIAN_JOIN_CONTEST_LIST_API,mEventType);
            }else {
                getDataForLive(INDIAN_JOIN_CONTEST_LIST_API,mEventType);
            }
        }else if(ActivityResultTypeContainer.Result_Type.equalsIgnoreCase(KEY_CRYPTO_LEAGUE)){
            Validations.common_log("Recived: CRYPTO 100,"+UserID+","+MatchStatus+","+mEventType);
            if (mEventType.equalsIgnoreCase("")){
                mEventType =ActivityResultTypeContainer.Context_Type;
                getDataForLive(JOIN_CONTEST_LIST_API,mEventType);
            }else {getDataForLive(JOIN_CONTEST_LIST_API,mEventType);}
        }
    }

    private void getDataForLive(String URL_RECIEVED, final String mEvent) {
        Validations.showProgress(getActivity());
        Log.d("getDataForLive",""+URL_RECIEVED);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RECIEVED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DataForLive",""+response);
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
                            myLiveTeamModels.add(myJoinTeamModel);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        setAdapterToRv();
                    }else if(status.equalsIgnoreCase("0")){
                        swipeRefreshLayout.setRefreshing(false);
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
                Log.d("DataForLive",""+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("user_id",UserID);
                param.put("match_status",MatchStatus);
                param.put("main_contest_name",mEvent);
                Log.d("DataForLive",""+param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void setAdapterToRv() {
        Rv_MyLive.setHasFixedSize(true);
        LiveListViewAdapter = new JoinListViewAdapter(getActivity(),myLiveTeamModels,UserID,MatchStatus);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        Rv_MyLive.setLayoutManager(linearLayoutManager);
        Rv_MyLive.setAdapter(LiveListViewAdapter);
    }

}
