package com.lgt.Ace11.FragmentBottomMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Config;
import com.lgt.Ace11.GlobalRankActivity;
import com.lgt.Ace11.HomeActivity;
import com.lgt.Ace11.InviteFriendsActivity;
import com.lgt.Ace11.InvitedFriendListActivity;
import com.lgt.Ace11.R;
import com.lgt.Ace11.WebviewAcitivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.lgt.Ace11.Config.MYPLAYINGHISTORY;
import static com.lgt.Ace11.Constants.MYPLAYINGHISTORYTYPE;


public class MoreFragment extends Fragment implements ResponseManager {

    RelativeLayout RL_MoreInviteFriend,RL_FantasyPointSystem,
            RL_MoreHowToPlay,RL_MoreAboutUs,RL_MoreHelpDesk;

    HomeActivity activity;
    Context context;
    APIRequestManager apiRequestManager;
    TextView tv_InviteFriends,tv_MyFriendsList;
    TextView tv_JoinedContest,tv_JoinedMatches;
    LinearLayout LL_GlobalRanking;
    ResponseManager responseManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_more, container, false);

        context = activity = (HomeActivity)getActivity();

        initViews(v);
        
        responseManager = this;
        apiRequestManager = new APIRequestManager(getActivity());

        RL_MoreInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, InviteFriendsActivity.class);
                startActivity(i);
            }
        });

        RL_FantasyPointSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","FANTASY POINT SYSTEM");
                i.putExtra("URL", Config.FANTASYPOINTSYSTEMURL);
                startActivity(i);
            }
        });
        RL_MoreHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","HOW TO PLAY");
                i.putExtra("URL",Config.HOWTOPLAYURL);
                startActivity(i);
            }
        });
        RL_MoreAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","ABOUT US");
                i.putExtra("URL",Config.ABOUTUSURL);
                startActivity(i);
            }
        });
        RL_MoreHelpDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","HELP DESK");
                i.putExtra("URL",Config.HELPDESKURL);
                startActivity(i);
            }
        });
        callMyHistory(true);
        return v;
    }

    public void initViews(View v){
        RL_MoreInviteFriend = v.findViewById(R.id.RL_MoreInviteFriend);
        RL_FantasyPointSystem = v.findViewById(R.id.RL_FantasyPointSystem);
        RL_MoreHowToPlay = v.findViewById(R.id.RL_MoreHowToPlay);
        RL_MoreAboutUs = v.findViewById(R.id.RL_MoreAboutUs);
        RL_MoreHelpDesk = v.findViewById(R.id.RL_MoreHelpDesk);

        tv_InviteFriends= v.findViewById(R.id.tv_InviteFriends);
        tv_MyFriendsList= v.findViewById(R.id.tv_MyFriendsList);
        LL_GlobalRanking= v.findViewById(R.id.LL_GlobalRanking);
        tv_JoinedContest= v.findViewById(R.id.tv_JoinedContest);
        tv_JoinedMatches= v.findViewById(R.id.tv_JoinedMatches);
        tv_InviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), InviteFriendsActivity.class);
                startActivity(i);
            }
        });
        tv_MyFriendsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), InvitedFriendListActivity.class);
                startActivity(i);
            }
        });
        LL_GlobalRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), GlobalRankActivity.class);
                startActivity(i);
            }
        });

    }

    private void callMyHistory(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(MYPLAYINGHISTORY,
                    createRequestJsonHistory(), context, activity, MYPLAYINGHISTORYTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    JSONObject createRequestJsonHistory() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", HomeActivity.sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        Log.e("shhhhss33",type.toString()+"");

        if (type.equals(MYPLAYINGHISTORYTYPE)){
            try {
                String contest = result.getString("contest");
                String matchs = result.getString("matchs");
                String Referal_Bonus = result.getString("Referal_Bonus");
                tv_InviteFriends.setText("INVITE FRIENDS & GET ₹"+Referal_Bonus);
                Log.e("shhhhss",contest+"");
                tv_JoinedContest.setText(contest);
                tv_JoinedMatches.setText(matchs);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        Log.e("shhhhss33error",type.toString()+","+message);
    }
}
