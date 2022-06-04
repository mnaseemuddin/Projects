package com.lgt.fxtradingleague.MyTabFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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


public class FragmentMyResults extends Fragment {

    String UserID = "", MatchStatus = "0", mEventType = "Daily";
    ActivityResultTypeContainer activity;
    Context context;
    RecyclerView Rv_MyResult;
    APIRequestManager apiRequestManager;
    SessionManager sessionManager;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_NoDataAvailable;
    JoinListViewAdapter ResultListViewAdapter;
    ArrayList<MyJoinTeamModel> myResultTeamModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_results, container, false);
        context = activity = (ActivityResultTypeContainer) getActivity();
        sessionManager = new SessionManager();
        initViews(v);
        return v;
    }

    public void initViews(View v) {
        if (sessionManager.getUser(activity).getUser_id() != null) {
            UserID = sessionManager.getUser(activity).getUser_id();
        }
        // load data from server
        Log.d("DataForFixtures", "fragment title " + FragmentMyFixtures.class.getSimpleName());
        if (FragmentMyResults.class.getSimpleName().equalsIgnoreCase("FragmentMyFixtures")) {
            MatchStatus = "0";
        } else if (FragmentMyResults.class.getSimpleName().equalsIgnoreCase("FragmentMyLive")) {
            MatchStatus = "1";
        } else if (FragmentMyResults.class.getSimpleName().equalsIgnoreCase("FragmentMyResults")) {
            MatchStatus = "2";
        }
        Rv_MyResult = v.findViewById(R.id.Rv_MyResult);
        tv_NoDataAvailable = v.findViewById(R.id.tv_NoDataAvailable);
        mEventType = Context_Type;
        if (ActivityResultTypeContainer.Result_Type.equalsIgnoreCase(KEY_WORLD_LEAGUE)) {
            Validations.common_log("Recived: World League," + UserID + "," + MatchStatus + "," + mEventType);
            if (mEventType.equalsIgnoreCase("")) {
                mEventType = ActivityResultTypeContainer.Context_Type;
                getDataForResult(JOIN_WORLD_CONTEST_LEAGUE_API, mEventType);
            } else {
                getDataForResult(JOIN_WORLD_CONTEST_LEAGUE_API, mEventType);
            }
        } else if (ActivityResultTypeContainer.Result_Type.equalsIgnoreCase(KEY_INDI_LEAGUE)) {
            Validations.common_log("Recived: NASDAQ 200," + UserID + "," + MatchStatus + "," + mEventType);
            if (mEventType.equalsIgnoreCase("")) {
                mEventType = ActivityResultTypeContainer.Context_Type;
                getDataForResult(INDIAN_JOIN_CONTEST_LIST_API, mEventType);
            } else {
                getDataForResult(INDIAN_JOIN_CONTEST_LIST_API, mEventType);
            }
        } else if (ActivityResultTypeContainer.Result_Type.equalsIgnoreCase(KEY_CRYPTO_LEAGUE)) {
            Validations.common_log("Recived: CRYPTO 100," + UserID + "," + MatchStatus + "," + mEventType);
            if (mEventType.equalsIgnoreCase("")) {
                mEventType = ActivityResultTypeContainer.Context_Type;
                getDataForResult(JOIN_CONTEST_LIST_API, mEventType);
            } else {
                getDataForResult(JOIN_CONTEST_LIST_API, mEventType);
            }
        }
    }


    private void getDataForResult(String URL_RECIEVED, final String mEvent) {
        Validations.showProgress(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RECIEVED, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DataForResult", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray innerData = jsonObject.getJSONArray("data");
                        for (int i = 0; i < innerData.length(); i++) {
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
                            myResultTeamModels.add(myJoinTeamModel);
                        }
                        setAdapterToRv();
                    } else if (status.equalsIgnoreCase("0")) {
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
                Log.d("DataForResult", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", UserID);
                param.put("match_status", MatchStatus);
                param.put("main_contest_name", mEvent);
                Log.d("DataForResult", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void setAdapterToRv() {
        Rv_MyResult.setHasFixedSize(true);
        ResultListViewAdapter = new JoinListViewAdapter(getActivity(), myResultTeamModels, UserID, MatchStatus);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        Rv_MyResult.setLayoutManager(linearLayoutManager);
        Rv_MyResult.setAdapter(ResultListViewAdapter);
    }

}
