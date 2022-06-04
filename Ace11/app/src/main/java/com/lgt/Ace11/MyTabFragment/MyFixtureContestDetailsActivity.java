package com.lgt.Ace11.MyTabFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Class.Validations;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.BeanMyFixLeaderboard;
import com.lgt.Ace11.Config;
import com.lgt.Ace11.ContestListActivity;
import com.lgt.Ace11.CreateTeamActivity;
import com.lgt.Ace11.FragmentBottomMenu.BottomSheetMatchStatus;
import com.lgt.Ace11.Playing11;
import com.lgt.Ace11.R;
import com.lgt.Ace11.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.MYFIXTURELEADERBOARD;
import static com.lgt.Ace11.Config.MYFIXTURELEADERBOARDTEAM;
import static com.lgt.Ace11.Constants.MYFIXTURELEADERBORADTEAMTYPE;
import static com.lgt.Ace11.Constants.MYFIXTURELEADERBORADTYPE;

public class MyFixtureContestDetailsActivity extends AppCompatActivity implements ResponseManager {


    MyFixtureContestDetailsActivity activity;
    Context context;
    RecyclerView Rv_MyFixLeaderboard;

    AdapterLeaderboard adapterLeaderboard;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    ImageView im_back;
    TextView tv_HeaderName,tv_ContestTimer,tv_ContestTeamsName;

    TextView tv_TotalWinning,tv_EntryFess,tv_JoinedWithTeam,tv_TotalJoinedTeamCount;

    boolean Add_View=true;
    String UserTeamId;
    BottomSheetDialog dialogGroundView=null;
    String match_status,ApiUserId;
    SessionManager sessionManager;
    ImageView ivteam1Image,ivteam2Image;
    TextView team1Name,team2Name;
    TextView tv_CreateTeamTimer;

    private String unique_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fixture_contest_details);
        context = activity = this;
        initViews();
        sessionManager = new SessionManager();

        tv_ContestTeamsName.setText(MyJoinedFixtureContestListActivity.IntenTeamsName);
        tv_ContestTimer.setText(MyJoinedFixtureContestListActivity.IntentTime);

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        Rv_MyFixLeaderboard.setHasFixedSize(true);
        Rv_MyFixLeaderboard.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        Rv_MyFixLeaderboard.setLayoutManager(mLayoutManager);
        Rv_MyFixLeaderboard.setItemAnimator(new DefaultItemAnimator());

        callLeaderboardList(true);

        Validations.CountDownTimer(MyJoinedFixtureContestListActivity.IntentTime,tv_ContestTimer);




    }

    public void initViews(){
        Rv_MyFixLeaderboard =  findViewById(R.id.Rv_MyFixLeaderboard);
        tv_ContestTimer =  findViewById(R.id.tv_ContestTimer);
        tv_ContestTeamsName =  findViewById(R.id.tv_ContestTeamsName);

        tv_TotalJoinedTeamCount =  findViewById(R.id.tv_TotalJoinedTeamCount);
        tv_JoinedWithTeam =  findViewById(R.id.tv_JoinedWithTeam);
        tv_EntryFess =  findViewById(R.id.tv_EntryFess);
        tv_TotalWinning =  findViewById(R.id.tv_TotalWinning);


        tv_CreateTeamTimer=findViewById(R.id.tv_CreateTeamTimer);
        ivteam1Image=findViewById(R.id.team1Image);
        ivteam2Image=findViewById(R.id.team2Image);
        team1Name=findViewById(R.id.team1Name);
        team2Name=findViewById(R.id.team2Name);

        Validations.CountDownTimer(MyJoinedFixtureContestListActivity.IntentTime,tv_CreateTeamTimer);
        Glide.with(context).load(MyJoinedFixtureContestListActivity.team1Image).into(ivteam1Image);
        Glide.with(context).load(MyJoinedFixtureContestListActivity.team2Image).into(ivteam2Image);
        team1Name.setText(MyJoinedFixtureContestListActivity.IntentTeamOneName);
        team2Name.setText(MyJoinedFixtureContestListActivity.IntentTeamTwoName);

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        tv_HeaderName.setText("CONTESTS");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              onBackPressed();
            }
        });

    }



    private void callLeaderboardList(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(MYFIXTURELEADERBOARD,
                    createRequestJson(), context, activity, MYFIXTURELEADERBORADTYPE,
                    isShowLoader,responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contest_id", MyJoinedFixtureContestListActivity.ContestId);
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());

            Log.e("contest_iddsdrer",MyJoinedFixtureContestListActivity.ContestId+",user_id="+sessionManager.getUser(context).getUser_id());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void callLeaderboardTeam(boolean isShowLoader) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("team_id", UserTeamId);
            jsonObject.put("match_id", MyJoinedFixtureContestListActivity.Matchid);

            Log.e("teamIddd",UserTeamId+",matchiddd="+MyJoinedFixtureContestListActivity.Matchid);

            apiRequestManager.callAPI(MYFIXTURELEADERBOARDTEAM,
                   jsonObject, context, activity, MYFIXTURELEADERBORADTEAMTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {


        Log.e("allResult",result+"");
        if (type.equals(MYFIXTURELEADERBORADTEAMTYPE)){

            DialogGroundView(result);
        }
        else {

            try {
                String prize_pool = result.getString("prize_pool");
                String entry = result.getString("entry");
                String all_team_count = result.getString("all_team_count");
                String user_team_count = result.getString("user_team_count");
                 match_status = result.getString("match_status");

                 Log.e("kljf",match_status+"");

                tv_EntryFess.setText("₹ " + entry);
                tv_TotalWinning.setText("₹ " + prize_pool);
                tv_TotalJoinedTeamCount.setText(all_team_count + " Teams");
                tv_JoinedWithTeam.setText(user_team_count + " Teams");


                JSONArray jsonArray = result.getJSONArray("leaderboard");
                List<BeanMyFixLeaderboard> beanContestLists = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<List<BeanMyFixLeaderboard>>() {
                        }.getType());

                Log.e("asdarwerwer",beanContestLists+"");

                adapterLeaderboard = new AdapterLeaderboard(beanContestLists, activity);
                Rv_MyFixLeaderboard.setAdapter(adapterLeaderboard);

            } catch (Exception e) {
                e.printStackTrace();
            }
            adapterLeaderboard.notifyDataSetChanged();
        }
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        //ShowToast(context,message);

    }

    public class AdapterLeaderboard extends RecyclerView.Adapter<AdapterLeaderboard.MyViewHolder> {
        private List<BeanMyFixLeaderboard> mListenerList;
        Context mContext;


        public AdapterLeaderboard(List<BeanMyFixLeaderboard> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_LeaderboardPlayerTeamName,tv_LeaderboardPlayerRank;
            ImageView im_LeaderboardPlayerAvtar;
            RelativeLayout RL_LeaderboardMain;

            public MyViewHolder(View view) {
                super(view);

                im_LeaderboardPlayerAvtar = view.findViewById(R.id.im_LeaderboardPlayerAvtar);
                tv_LeaderboardPlayerTeamName = view.findViewById(R.id.tv_LeaderboardPlayerTeamName);
                tv_LeaderboardPlayerRank = view.findViewById(R.id.tv_LeaderboardPlayerRank);
                RL_LeaderboardMain = view.findViewById(R.id.RL_LeaderboardMain);

            }

        }
        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_leaderboard_list, parent, false);

            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            ApiUserId = mListenerList.get(position).getUser_id();
            String name= mListenerList.get(position).getName();
            String rank= mListenerList.get(position).getRank();
            String id= mListenerList.get(position).getId();
            String Image= mListenerList.get(position).getImage();
            String Team= mListenerList.get(position).getTeam();



            holder.tv_LeaderboardPlayerTeamName.setText(name+"(T"+Team+")");
            holder.tv_LeaderboardPlayerRank.setText(rank);

            Glide.with(activity).load(Config.LEADERBOARDPLAYERIMAGE+Image)

                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_LeaderboardPlayerAvtar);

            Log.e("userImageee",Config.LEADERBOARDPLAYERIMAGE+Image+"");

            if (position%2==1){
                holder.RL_LeaderboardMain.setBackgroundColor(getResources().getColor(R.color.bg_gray));
            }


            if (ApiUserId.equals(sessionManager.getUser(activity).getUser_id())){

                holder.RL_LeaderboardMain.setBackgroundResource(R.drawable.leaderboard_dark_rectangle);
                holder.tv_LeaderboardPlayerTeamName.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_LeaderboardPlayerRank.setTextColor(context.getResources().getColor(R.color.white));
            }
            else {

                holder.RL_LeaderboardMain.setBackgroundResource(R.drawable.white_rectangle);
                holder.tv_LeaderboardPlayerTeamName.setTextColor(context.getResources().getColor(R.color.black));
                holder.tv_LeaderboardPlayerRank.setTextColor(context.getResources().getColor(R.color.black));
            }



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserTeamId = mListenerList.get(position).getTeamid();
                    ApiUserId = mListenerList.get(position).getUser_id();
                    Log.e("rankuserId",ApiUserId+"");
                    if (ApiUserId.equals(sessionManager.getUser(activity).getUser_id())){

                        Log.e("dfjkhgk",sessionManager.getUser(activity).getUser_id()+"");

                        callLeaderboardTeam(true);
                    } else {
                        if (match_status.equals("1")){

                            ShowToast(context,"Please Wait! Match has not started yet.");
                        }
                        else {
                            callLeaderboardTeam(true);
                        }
                    }
                }
            });
        }

    }
    public void DialogGroundView(JSONObject result){
        Log.e("dialogView",result.toString());
       /* dialogGroundView = new BottomSheetDialog(activity);
        dialogGroundView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGroundView.setContentView(R.layout.dialog_ground_view);

        final LinearLayout LL_GroundWK = dialogGroundView.findViewById(R.id.LL_GroundWK);
        final LinearLayout LL_GroundBAT = dialogGroundView.findViewById(R.id.LL_GroundBAT);
        final LinearLayout LL_GroundAR = dialogGroundView.findViewById(R.id.LL_GroundAR);
        final LinearLayout LL_GroundBOWL = dialogGroundView.findViewById(R.id.LL_GroundBOWL);
        ImageView im_CloseIcon = dialogGroundView.findViewById(R.id.im_CloseIcon);
        dialogGroundView.show();
        im_CloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGroundView.dismiss();
            }
        });*/
        if (dialogGroundView == null){
            dialogGroundView = new BottomSheetDialog(activity);
            dialogGroundView.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogGroundView.setContentView(R.layout.dialog_ground_view); // initiate it the way you need
            dialogGroundView.show();

        }
        else if (!dialogGroundView.isShowing()){
            dialogGroundView.show();
        }
           else {
            dialogGroundView.dismiss();
            dialogGroundView=null;
            Add_View=true;
        }
        dialogGroundView.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                // TODO Auto-generated method stub

                dialog.dismiss();
                dialogGroundView=null;
                Add_View=true;

            }
        });
        final LinearLayout LL_GroundWK = dialogGroundView.findViewById(R.id.LL_GroundWK);
        final LinearLayout LL_GroundBAT = dialogGroundView.findViewById(R.id.LL_GroundBAT);
        final LinearLayout LL_GroundAR = dialogGroundView.findViewById(R.id.LL_GroundAR);
        final LinearLayout LL_GroundBOWL = dialogGroundView.findViewById(R.id.LL_GroundBOWL);

       TextView tvPlaying11 = dialogGroundView.findViewById(R.id.tvPlaying11);

        tvPlaying11.setVisibility(View.VISIBLE);

        tvPlaying11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MyFixtureContestDetailsActivity.this, ActivityPlayersInformation.class);
                //Check if match is start or not , if start send to Playing 11 else in case of scheduled show popup

                checkMatchStatus(unique_id);


            }
        });

        ImageView im_CloseIcon = dialogGroundView.findViewById(R.id.im_CloseIcon);
        ImageView im_Editteam = dialogGroundView.findViewById(R.id.im_Editteam);

        if (ApiUserId.equals(sessionManager.getUser(context).getUser_id())){
            im_Editteam.setVisibility(View.VISIBLE);
        }else {
            im_Editteam.setVisibility(View.GONE);

        }

        ImageView im_Refresh=dialogGroundView.findViewById(R.id.im_Refresh);
        im_Refresh.setVisibility(View.GONE);

        im_CloseIcon.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGroundView.dismiss();
                dialogGroundView=null;
                Add_View=true;

            }
        });
        im_Editteam.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    ContestListActivity.JoinMyTeamId=UserTeamId;
                    ContestListActivity.MyTeamEditorSave = "Edit";
                    ContestListActivity.IntentMatchId= MyJoinedFixtureContestListActivity.Matchid;
                    Intent i = new Intent(activity, CreateTeamActivity.class);
                    i.putExtra("Activity", "MyFixtureContestDetailActivity");
                    startActivity(i);

                    }
                }
        );

        try {
            JSONArray jsonArray = result.getJSONArray("data");
            Log.e("jsonArray",jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userData = jsonArray.getJSONObject(i);

                final String PlayerId = userData.getString("player_id");
                String IsSelected = userData.getString("is_select");
                String Role = userData.getString("short_term");
                final String player_shortname = userData.getString("player_shortname");
                String PlayerImage = userData.getString("image");
                String PlayerCredit = userData.getString("credit_points");
                String is_captain = userData.getString("is_captain");
                String is_vicecaptain = userData.getString("is_vicecaptain");

                 unique_id = userData.getString("unique_id");

                final String newPid = userData.getString("pid");

                 Log.e("asdrewrwerwer", userData.getString("unique_id"));


                if (is_captain==null){
                    is_captain = "0";
                }
                if (is_vicecaptain==null){
                    is_vicecaptain = "0";
                }

                if (IsSelected.equals("1")) {
                    if (Role.equals("WK")) {
                        View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                LL_GroundWK, false);



                        ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                        TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                        TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                        TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);

                        Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)

                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(im_GroundPlayerImage);
                        tv_GroundPlayerName.setText(player_shortname);
                        tv_GroundPlayerCredit.setText(PlayerCredit + " Cr");

                        if (is_captain.equals("1")){
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText(" C ");
                        }
                        if (is_vicecaptain.equals("1")){
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText("VC");
                        }
                       // LL_GroundWK.addView(to_add);
                        if (Add_View) {
                            LL_GroundWK.addView(to_add);
                        }

                        RelativeLayout LL_GroundPlayer = to_add.findViewById(R.id.LL_GroundPlayer);
                        LL_GroundPlayer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                              /*  Intent intent = new Intent(MyFixtureContestDetailsActivity.this,ActivityPlayersInformation.class);
                                intent.putExtra("KEY_UNIQUE_ID",unique_id);
                                startActivity(intent);

                                Toast.makeText(activity, "Clicked"+PlayerId+player_shortname, Toast.LENGTH_SHORT).show();*/

                            }
                        });

                    } else if (Role.equals("BAT")) {
                        View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                LL_GroundBAT, false);
                        ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                        TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                        TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                        TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);

                        Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)

                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(im_GroundPlayerImage);

                        tv_GroundPlayerName.setText(player_shortname);
                        tv_GroundPlayerCredit.setText(PlayerCredit + " Cr");

                        if (is_captain.equals("1")){
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText(" C ");
                        }
                        if (is_vicecaptain.equals("1")){
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText("VC");
                        }
                        //LL_GroundBAT.addView(to_add);
                        if (Add_View) {
                            LL_GroundBAT.addView(to_add);
                        }


                      /*  RelativeLayout LL_GroundPlayer = to_add.findViewById(R.id.LL_GroundPlayer);
                        LL_GroundPlayer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(activity, "Clicked"+PlayerId+player_shortname, Toast.LENGTH_SHORT).show();
                            }
                        });*/


                    } else if (Role.equals("AR")) {
                        View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                LL_GroundAR, false);
                        ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                        TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                        TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                        TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);

                        Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)

                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(im_GroundPlayerImage);
                        tv_GroundPlayerName.setText(player_shortname);
                        tv_GroundPlayerCredit.setText(PlayerCredit + " Cr");

                        if (is_captain.equals("1")){
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText(" C ");
                        }
                        if (is_vicecaptain.equals("1")){
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText("VC");
                        }
                        //LL_GroundAR.addView(to_add);
                        if (Add_View) {
                            LL_GroundAR.addView(to_add);
                        }


                       /* RelativeLayout LL_GroundPlayer = to_add.findViewById(R.id.LL_GroundPlayer);
                        LL_GroundPlayer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(activity, "Clicked"+PlayerId+player_shortname, Toast.LENGTH_SHORT).show();
                            }
                        });
*/

                    } else if (Role.equals("BOWL")) {
                        View to_add = LayoutInflater.from(context).inflate(R.layout.item_ground_player,
                                LL_GroundBOWL, false);
                        ImageView im_GroundPlayerImage = to_add.findViewById(R.id.im_GroundPlayerImage);
                        TextView tv_GroundPlayerName = to_add.findViewById(R.id.tv_GroundPlayerName);
                        TextView tv_GroundPlayerCredit = to_add.findViewById(R.id.tv_GroundPlayerCredit);
                        TextView tv_CorVC = to_add.findViewById(R.id.tv_CorVC);

                        Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)

                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(im_GroundPlayerImage);
                        tv_GroundPlayerName.setText(player_shortname);
                        tv_GroundPlayerCredit.setText(PlayerCredit + " Cr");

                        if (is_captain.equals("1")){
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText(" C ");
                        }
                        if (is_vicecaptain.equals("1")){
                            tv_CorVC.setVisibility(View.VISIBLE);
                            tv_CorVC.setText("VC");
                        }
                       // LL_GroundBOWL.addView(to_add);
                        if (Add_View) {
                            LL_GroundBOWL.addView(to_add);

                        }

                      /*  RelativeLayout LL_GroundPlayer = to_add.findViewById(R.id.LL_GroundPlayer);
                        LL_GroundPlayer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(activity, "Clicked"+PlayerId+player_shortname, Toast.LENGTH_SHORT).show();
                            }
                        });*/
                    }
                }

            }
            Add_View=false;

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void checkMatchStatus(final String unique_id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MyFixtureContestDetailsActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        Log.e("dsarewrwerwe",unique_id+"");

       String checkURL = "https://rest.entitysport.com/v2/matches/"+unique_id+"/point?token=93de634d3c0f09c0ba1f7f6d71257497";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, checkURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("ldfjgk",response+"");

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if(status.equalsIgnoreCase("ok")){
                        JSONObject object = jsonObject.getJSONObject("response");
                        String status_str = object.getString("status_str");

                        if(status_str.equalsIgnoreCase("Scheduled")){
                            BottomSheetMatchStatus bottomSheetMatchStatus = new BottomSheetMatchStatus();
                            bottomSheetMatchStatus.show(getSupportFragmentManager(),"BottomSheetMatchStatus");
                        }

                        else {
                            Intent intent = new Intent(MyFixtureContestDetailsActivity.this, Playing11.class);
                            intent.putExtra("KEY_UNIQUE_ID",unique_id);
                            Log.e("Dasrwerewrwer",unique_id+"");
                            startActivity(intent);
                        }

                    }
                    
                    else {
                        Toast.makeText(MyFixtureContestDetailsActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MyFixtureContestDetailsActivity.this, "Network or server error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MyFixtureContestDetailsActivity.this);
        requestQueue.add(stringRequest);

    }


}
