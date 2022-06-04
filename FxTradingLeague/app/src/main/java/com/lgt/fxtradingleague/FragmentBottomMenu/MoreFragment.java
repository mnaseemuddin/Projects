package com.lgt.fxtradingleague.FragmentBottomMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.GlobalRankActivity;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.InviteFriendsActivity;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.WebviewAcitivity;
import com.lgt.fxtradingleague.databinding.FragmentMoreBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.lgt.fxtradingleague.Config.MYPLAYINGHISTORY;
import static com.lgt.fxtradingleague.Constants.MYPLAYINGHISTORYTYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.PTL_DISCLAIMER_LINK;
import static com.lgt.fxtradingleague.Extra.ExtraData.PTL_POINT_SYSTEM_LINK;


public class MoreFragment extends Fragment implements ResponseManager {

    HomeActivity activity;
    Context context;
    APIRequestManager apiRequestManager;
    ResponseManager responseManager;
    private FragmentMoreBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        context = activity = (HomeActivity) getActivity();
        // callMyHistory(true);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        handleClick();

        responseManager = this;
        apiRequestManager = new APIRequestManager(getActivity());


    }

    private void handleClick() {
        binding.RLMoreInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, InviteFriendsActivity.class);
                startActivity(i);
            }
        });

        binding.RLFantasyPointSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading", "FANTASY POINT SYSTEM");
                i.putExtra("URL", PTL_POINT_SYSTEM_LINK);
                startActivity(i);
            }
        });

        binding.RLMoreHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading", "DISCLAIMER");
                i.putExtra("URL", PTL_DISCLAIMER_LINK);
                startActivity(i);
            }
        });


        binding.RLMoreHelpDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","HELP DESK");
                i.putExtra("URL",Config.HELPDESKURL);
                startActivity(i);*/
            }
        });

    }

    public void initViews( ) {

        binding.tvMyFriendsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(getActivity(), InvitedFriendListActivity.class);
                startActivity(i);*/
            }
        });

    }

    private void callMyHistory(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(MYPLAYINGHISTORY,
                    createRequestJsonHistory(), context, activity, MYPLAYINGHISTORYTYPE,
                    isShowLoader, responseManager);

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

        Log.e("shhhhss33", type.toString() + "");

        if (type.equals(MYPLAYINGHISTORYTYPE)) {
            try {

                String contest = result.getString("contest");
                String matchs = result.getString("matchs");
                String Referal_Bonus = result.getString("Referal_Bonus");

                Log.e("shhhhss", contest + "");
                binding.tvJoinedContest.setText(contest);
                binding.tvJoinedMatches.setText(matchs);

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
        Log.e("shhhhss33error", type.toString() + "," + message);

    }
}
