package com.lgt.Ace11;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Class.Validations;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.BeanDBTeam;
import com.lgt.Ace11.MyTabFragment.MyJoinedFixtureContestListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.SAVETEAM;
import static com.lgt.Ace11.Constants.SAVETEAMTYPE;

public class ChooseCandVCActivity extends AppCompatActivity implements ResponseManager {


    RecyclerView Rv_FinalPlayerList;
    ListView lv;
    ChooseCandVCActivity activity;
    Context context;

    AdapterFinalTeamList adapterFinalTeamList;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    SQLiteDatabase db;

    ArrayList<String> sectionNameList = new ArrayList<>();
    LinearLayout LL_PlayerList;
    String JoinTeamId = "";

    ImageView im_back;
    TextView tv_HeaderName, tv_CreateTeamTimer, tv_CreateTeamsName, tv_TeamNext, team1Name, team2Name;
    //  String IntentMatchId, IntentTime, IntenTeamsName,IntentTeamOneName,IntentTeamTwoName
    ;
    String CaptainId = "0", ViceCaptainId = "0";
    JSONArray PlayerListArray;
    public static SessionManager sessionManager;
    static ImageView team1Image, team2Image;
    int previousPosition = -1;
    int previousPositionVC = -1;
    // HospitalAdapter hospitalAdapter;
    String ActivityValue;

    static String ivteam1Image, ivteam2Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cand_vc);
        context = activity = this;
        sessionManager = new SessionManager();

        initViews();


        ActivityValue = getIntent().getStringExtra("Activity");
        tv_CreateTeamsName.setText(ContestListActivity.IntenTeamsName);
        tv_CreateTeamTimer.setText(ContestListActivity.IntentTime);
        team1Image = findViewById(R.id.team1Image);
        team2Image = findViewById(R.id.team2Image);
        team1Name = findViewById(R.id.team1Name);

        team2Name = findViewById(R.id.team2Name);


        String team1 = ContestListActivity.IntentTeamOneName.replaceAll(" ", "");
        String team2 = ContestListActivity.IntentTeamTwoName.replaceAll(" ", "");


        ivteam1Image = ContestListActivity.team1Image;
        ivteam2Image = ContestListActivity.team2Image;


        if (team1.length() > 3) {
            team1Name.setText(team1.substring(0, 3));
        } else {
            team1Name.setText(team1);
        }
        if (team2.length() > 3) {
            team2Name.setText(team2.substring(0, 3));
        } else {
            team2Name.setText(team2);
        }
        Glide.with(context).load(ivteam1Image).into(team1Image);
        Glide.with(context).load(ivteam2Image).into(team2Image);

        sectionNameList.add("Wicket Keeper");
        sectionNameList.add("Batsman");
        sectionNameList.add("All Rounder");
        sectionNameList.add("Bowler");

        Validations.CountDownTimer(ContestListActivity.IntentTime, tv_CreateTeamTimer);

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        Rv_FinalPlayerList.setHasFixedSize(true);
        Rv_FinalPlayerList.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        Rv_FinalPlayerList.setLayoutManager(mLayoutManager);
        Rv_FinalPlayerList.setItemAnimator(new DefaultItemAnimator());
        GetTeamData();


        tv_TeamNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CaptainId.equals("0")) {
                    ShowToast(context, "Please Select Captain");
                } else if (ViceCaptainId.equals("0")) {
                    ShowToast(context, "Please Select Vice Captain");
                } else {
                    callSaveTeam(true);
                }
            }
        });
    }


    @SuppressLint("WrongConstant")
    public void initViews() {

        lv = (ListView) findViewById(R.id.listView);

        Rv_FinalPlayerList = findViewById(R.id.Rv_FinalPlayerList);
        LL_PlayerList = findViewById(R.id.LL_PlayerList);
        tv_TeamNext = findViewById(R.id.tv_TeamNext);

        db = this.openOrCreateDatabase("TeamData.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        tv_CreateTeamsName = findViewById(R.id.tv_CreateTeamsName);
        tv_CreateTeamTimer = findViewById(R.id.tv_CreateTeamTimer);

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        tv_HeaderName.setText("CHOOSE C & VC");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


    }


    public void GetTeamData() {
        try {
            String qry = "select * from TeamListTable"; // where Role="+RoleDes;
            Cursor cur = db.rawQuery(qry, null);

            ArrayList<BeanDBTeam> arr_bean = new ArrayList<>();
            PlayerListArray = new JSONArray();
            if (cur == null) {
                ShowToast(context, "No Data Found");
            } else {
                while (cur.moveToNext()) {

                    String PlayerId = cur.getString(cur.getColumnIndex("PlayerId"));
                    String MatchId = cur.getString(cur.getColumnIndex("MatchId"));
                    String IsSelected = cur.getString(cur.getColumnIndex("IsSelected"));
                    String Role = cur.getString(cur.getColumnIndex("Role"));
                    String PlayerData = cur.getString(cur.getColumnIndex("PlayerData"));

                    if (IsSelected.equals("1")) {
                        BeanDBTeam ABean = new BeanDBTeam();
                        ABean.setPlayerId(PlayerId);
                        ABean.setMatchId(MatchId);
                        ABean.setIsSelected(IsSelected);
                        ABean.setRole(Role);
                        ABean.setPlayerData(PlayerData);
                        arr_bean.add(ABean);

                        adapterFinalTeamList = new AdapterFinalTeamList(arr_bean, activity);
                        Rv_FinalPlayerList.setAdapter(adapterFinalTeamList);

                        /*hospitalAdapter = new HospitalAdapter(arr_bean,activity);
                        lv.setAdapter(hospitalAdapter);*/

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("PlayerId", PlayerId);
                        PlayerListArray.put(jsonObject);
                    }


                }

            }
            adapterFinalTeamList.notifyDataSetChanged();
            //hospitalAdapter.notifyDataSetChanged();

            cur.close();

        } catch (Exception ex) {

            ShowToast(context, "No Data Found.");
        }
    }

    private void callSaveTeam(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(SAVETEAM,
                    createRequestJson(), context, activity, SAVETEAMTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (ActivityValue.equals("MyFixtureContestDetailActivity")) {
                jsonObject.put("matchid", MyJoinedFixtureContestListActivity.Matchid);
            } else {
                jsonObject.put("matchid", ContestListActivity.IntentMatchId);
            }

            jsonObject.put("userid", sessionManager.getUser(context).getUser_id());
            jsonObject.put("CaptainId", CaptainId);
            jsonObject.put("ViceCaptainId", ViceCaptainId);
            jsonObject.put("PlayerList", PlayerListArray);

            if (ContestListActivity.MyTeamEditorSave.equals("Edit")) {
                jsonObject.put("edit", "1");
                jsonObject.put("my_team_id", ContestListActivity.JoinMyTeamId);
            } else {
                jsonObject.put("edit", "0");
            }


            Log.e("Choose C & VC>>", jsonObject + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        try {
            ContestListActivity.JoinMyTeamId = result.getString("id");
            Log.e("JoinTeamId", JoinTeamId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ShowToast(context, message);
        if (ActivityValue.equals("ContestListActivity") && ContestListActivity.MyTeamEditorSave.equals("Save")) {
            Intent i = new Intent(activity, PaymentConfirmationActivity.class);
            i.putExtra("EntryFee", ContestListActivity.EntryFee);
            i.putExtra("MyTeamId", ContestListActivity.JoinMyTeamId);
            i.putExtra("MyContestCode", ContestListActivity.MyContestCode);
            startActivity(i);
        } else {

            if (ActivityValue.equals("MyFixtureContestDetailActivity")) {
                Intent i = new Intent(activity, HomeActivity.class);
                startActivity(i);
            } else {
                Intent i = new Intent(activity, ContestListActivity.class);
                i.putExtra("MatchId", ContestListActivity.IntentMatchId);
                i.putExtra("Time", ContestListActivity.IntentTime);
                i.putExtra("TeamsName", ContestListActivity.IntenTeamsName);
                i.putExtra("TeamsOneName", ContestListActivity.IntentTeamOneName);
                i.putExtra("TeamsTwoName", ContestListActivity.IntentTeamTwoName);
                startActivity(i);
                finish();
            }
        }

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        ShowToast(context, message);

    }


    public class AdapterFinalTeamList extends RecyclerView.Adapter<AdapterFinalTeamList.MyViewHolder> {
        private List<BeanDBTeam> mListenerList;
        Context mContext;
        private CheckBox lastChecked = null;
        private int lastCheckedPos = 0;

        private CheckBox lastChecked2 = null;
        private int lastCheckedPos2 = 0;

        private RadioButton lastCheckedRB = null;
        private RadioButton lastCheckedRB1 = null;

        TextView PreviousCaptain = null;
        TextView PreviousVC = null;


        public AdapterFinalTeamList(List<BeanDBTeam> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_PlayerName, tv_SelectCaptain, tv_SelectViceCaptain, tv_PlayerTeamName, tv_PlayerPoints, tv_TeamNumber;
            ImageView im_PlayerImage, im_onetwox;
            CheckBox checkbox, checkbox2;

            RadioGroup radiogroup;
            RadioButton radio, radio2;


            public MyViewHolder(View view) {
                super(view);

                tv_PlayerName = view.findViewById(R.id.tv_PlayerName);
                tv_PlayerTeamName = view.findViewById(R.id.tv_PlayerTeamName);
                tv_PlayerPoints = view.findViewById(R.id.tv_PlayerPoints);

                im_PlayerImage = view.findViewById(R.id.im_PlayerImage);
                im_onetwox = view.findViewById(R.id.im_onetwox);


                tv_TeamNumber = view.findViewById(R.id.tv_TeamNumber);
                tv_SelectViceCaptain = view.findViewById(R.id.tv_SelectViceCaptain);
                tv_SelectCaptain = view.findViewById(R.id.tv_SelectCaptain);
                checkbox = view.findViewById(R.id.checkbox);
                checkbox2 = view.findViewById(R.id.checkbox2);
                radiogroup = view.findViewById(R.id.radiogroup);
                radio = view.findViewById(R.id.radio);
                radio2 = view.findViewById(R.id.radio2);


            }

        }

        @Override
        public int getItemCount() {
            return mListenerList.size();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_final_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


            String id = mListenerList.get(position).getMatchId();

            final String arrayList = (mListenerList.get(position).getPlayerData());
            try {
                JSONObject job = new JSONObject(arrayList);

                String PlayerName = job.getString("name");
                String PlayerImage = job.getString("image");
                String PlayerPoints = job.getString("player_points");
                String PlayerCredit = job.getString("credit_points");
                String TeamShortName = job.getString("team_short_name");

                String team_number = job.getString("team_number");
                String player_shortname = job.getString("player_shortname");
                holder.tv_TeamNumber.setText(team_number);
                // PlayerTeam= job.getString("short_name");

                holder.tv_PlayerName.setText(PlayerName);

                holder.tv_PlayerPoints.setText(PlayerPoints);
                holder.tv_PlayerTeamName.setText(TeamShortName);


                Glide.with(activity).load(Config.PLAYERIMAGE + PlayerImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.im_PlayerImage);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            holder.checkbox.setTag(new Integer(position));
            holder.checkbox2.setTag(new Integer(position));


            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v;

                    int clickedPos = ((Integer) cb.getTag()).intValue();
                    if (holder.checkbox2.isChecked()) {
                        ViceCaptainId = "0";
                    }
                    holder.checkbox2.setChecked(false);

                    if (cb.isChecked()) {
                        if (lastChecked != null) {
                            mListenerList.get(lastCheckedPos).setSelected(false);
                            lastChecked.setChecked(false);
                            //ViceCaptainId="0";
                            CaptainId = mListenerList.get(position).getPlayerId();
                        } else if (clickedPos == position) {
                           // holder.checkbox.setTextColor(getResources().getColor(R.color.white));

                            lastCheckedPos = clickedPos;
                            lastChecked = cb;
                            lastChecked.setChecked(true);
                            CaptainId = mListenerList.get(position).getPlayerId();
                        }
                        lastCheckedPos = clickedPos;
                        lastChecked = cb;
                        // CaptainId = mListenerList.get(position).getPlayerId();
                    } else {
                       // holder.checkbox.setTextColor(getResources().getColor(R.color.black));

                        lastChecked = null;
                        CaptainId = "0";
                    }
                    try {
                        lastChecked.setChecked(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mListenerList.get(clickedPos).setSelected(cb.isChecked());
                    // CaptainId = mListenerList.get(position).getPlayerId();

                }
            });
            holder.checkbox2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;

                    int clickedPos = ((Integer) cb.getTag()).intValue();
                    if (holder.checkbox.isChecked()) {
                        CaptainId = "0";
                    }
                    holder.checkbox.setChecked(false);


                    if (cb.isChecked()) {
                        if (lastChecked2 != null) {
                            lastChecked2.setChecked(false);
                            mListenerList.get(lastCheckedPos2).setSelected(false);
                            // CaptainId="0";
                            ViceCaptainId = mListenerList.get(position).getPlayerId();
                        } else if (clickedPos == position) {
                            //holder.checkbox2.setTextColor(getResources().getColor(R.color.white));
                            lastChecked2 = cb;
                            lastCheckedPos2 = clickedPos;
                            lastChecked2.setChecked(true);
                            ViceCaptainId = mListenerList.get(position).getPlayerId();
                        }

                        lastChecked2 = cb;
                        lastCheckedPos2 = clickedPos;
                        //ViceCaptainId = mListenerList.get(position).getPlayerId();
                    } else {
                        lastChecked2 = null;
                        ViceCaptainId = "0";
                    }
                    try {
                        lastChecked2.setChecked(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mListenerList.get(clickedPos).setSelected2(cb.isChecked());
                    //ViceCaptainId = mListenerList.get(position).getPlayerId();

                }

            });
        }
    }
}
