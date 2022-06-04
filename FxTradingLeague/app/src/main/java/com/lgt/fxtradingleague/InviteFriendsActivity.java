package com.lgt.fxtradingleague;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;

import org.json.JSONObject;

public class InviteFriendsActivity extends AppCompatActivity implements ResponseManager {


    InviteFriendsActivity  activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;
    TextView tv_InviteFriend,tv_UniqueCode,tv_MyFriendList;
    String ReferralCode,UserName;
    String finalRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        context = activity = this;
        initViews();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        sessionManager = new SessionManager();
        ReferralCode = sessionManager.getUser(context).getReferral_code();
        UserName = sessionManager.getUser(context).getName();

       // finalRef=getString(R.string.bbb)+ReferralCode+getString(R.string.ccc);
    //    Log.e("reffff",finalRef);


        http://uabsports.in/apk/UAB%20Sports.apk
        tv_UniqueCode.setText(ReferralCode);

        tv_InviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(getApplicationContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Your friend " + UserName +" invited you to play " +
                        "Paper Trading League " +
                        "Where you can earn daily cash prize. " +
                        "Download the app using " +
                        "link:- https://www.google.com and register with referral code " +"\""+ReferralCode+"\" to create your account.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share"));
            }
        });

        tv_MyFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(activity, InvitedFriendListActivity.class);
                startActivity(i);*/
            }
        });

    }

    public void initViews(){
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_InviteFriend = findViewById(R.id.tv_InviteFriend);
        tv_UniqueCode = findViewById(R.id.tv_UniqueCode);
        tv_MyFriendList = findViewById(R.id.tv_MyFriendList);

        tv_HeaderName.setText("INVITE FRIENDS");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {

    }
}
