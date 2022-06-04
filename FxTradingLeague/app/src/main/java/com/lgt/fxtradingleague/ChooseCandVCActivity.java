package com.lgt.fxtradingleague;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Bean.BeanDBTeam;
import com.lgt.fxtradingleague.Extra.BuySaleClick;
import com.lgt.fxtradingleague.ModelPTL.JoinTeamData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_TEAM_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_USER_ID;
import static com.lgt.fxtradingleague.Extra.ExtraData.KEY_ENTRY_FEE;
import static com.lgt.fxtradingleague.Extra.ExtraData.SELECTED_PLAYER_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.SELL_AND_BUY_UPDATE;

public class ChooseCandVCActivity extends AppCompatActivity implements BuySaleClick {


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
    ProgressBar pb_progressBarSave;
    ImageView im_back;
    TextView tv_HeaderName, tv_CreateTeamTimer, tv_CreateTeamsName, tv_TeamNext, team1Name, team2Name;
    // String IntentMatchId, IntentTime, IntenTeamsName,IntentTeamOneName,IntentTeamTwoName;
    String CaptainId = "0", ViceCaptainId = "0";
    JSONArray PlayerListArray;
    public static SessionManager sessionManager;
    static ImageView team1Image, team2Image;
    int previousPosition = -1;
    int previousPositionVC = -1;
    // HospitalAdapter hospitalAdapter;
    String ActivityValue, contest_id, team_id, user_id,EntryFee,kContestType;

    static String ivteam1Image, ivteam2Image;
    List<JoinTeamData> mJoinTeamData = new ArrayList<>();
    Set<String> mBuy = new HashSet<>();
    Set<String> mSale = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cand_vc);
        context = activity = this;
        sessionManager = new SessionManager();
        initViews();
        if (getIntent().hasExtra(KEYS_CONTEST_ID)) {
            contest_id = getIntent().getStringExtra(KEYS_CONTEST_ID);
            team_id = getIntent().getStringExtra(KEYS_TEAM_ID);
            user_id = getIntent().getStringExtra(KEYS_USER_ID);
            EntryFee = getIntent().getStringExtra(KEY_ENTRY_FEE);
            kContestType = getIntent().getStringExtra(KEYS_CONTEST_TYPE);
            Log.d("event_type",""+kContestType);
        }
        ActivityValue = getIntent().getStringExtra("Activity");
        tv_CreateTeamsName.setText(ContestListActivity.IntenTeamsName);
        tv_CreateTeamTimer.setText(ContestListActivity.IntentTime);
        team1Image = findViewById(R.id.team1Image);
        pb_progressBarSave = findViewById(R.id.pb_progressBarSave);
        team2Image = findViewById(R.id.team2Image);
        team1Name = findViewById(R.id.team1Name);
        team2Name = findViewById(R.id.team2Name);
        ivteam1Image = ContestListActivity.team1Image;
        ivteam2Image = ContestListActivity.team2Image;
        Glide.with(context).load(ivteam1Image).into(team1Image);
        Glide.with(context).load(ivteam2Image).into(team2Image);
        sectionNameList.add("Wicket Keeper");
        sectionNameList.add("Batsman");
        sectionNameList.add("All Rounder");
        sectionNameList.add("Bowler");
        // Validations.CountDownTimer(ContestListActivity.IntentTime, tv_CreateTeamTimer);
        // apiRequestManager = new APIRequestManager(activity);
        tv_TeamNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("total_size","Buy : "+mBuy.size() +",   Sale : "+ mSale.size());
                int totalSize =mBuy.size() + mSale.size();
                Log.d("total_size",""+totalSize);
                if (totalSize == 11){
                    pb_progressBarSave.setVisibility(View.VISIBLE);
                    setContestDetails();
                    Log.d("total_size",""+totalSize);
                }else{
                    Toast.makeText(activity, "Select Total 11 Stocks", Toast.LENGTH_SHORT).show();
                }
                // not use anymore two way validation 20-11-2020
                /*if (CaptainId.equals("0")) {
                    ShowToast(context, "Please Select Buy");
                } else if (ViceCaptainId.equals("0")) {
                    ShowToast(context, "Please Select Sale");
                } else {
                    int totalSize =mBuy.size() + mSale.size();

                    if (totalSize == 11){
                        pb_progressBarSave.setVisibility(View.VISIBLE);
                        setContestDetails(CaptainId, ViceCaptainId);
                        Log.d("total_size",""+totalSize);
                    }else{
                        Toast.makeText(activity, "Select Total 11 Stocks", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }
        });
    }

    private void setContestDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SELL_AND_BUY_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setContestDetails", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        pb_progressBarSave.setVisibility(View.GONE);
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        Intent comformation = new Intent(ChooseCandVCActivity.this,PaymentConfirmationActivity.class);
                        comformation.putExtra(KEYS_CONTEST_ID,contest_id);
                        comformation.putExtra(KEYS_TEAM_ID,team_id);
                        comformation.putExtra(KEYS_USER_ID,user_id);
                        comformation.putExtra(KEY_ENTRY_FEE,EntryFee);
                        comformation.putExtra(KEYS_CONTEST_TYPE,kContestType);
                        startActivity(comformation);
                        finish();
                    } else {
                        pb_progressBarSave.setVisibility(View.GONE);
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pb_progressBarSave.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("setContestDetails", "" + error);
                pb_progressBarSave.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", HomeActivity.sessionManager.getUser(getApplicationContext()).getUser_id());
                param.put("team_id", team_id);
                if (mSale.size()!=0){
                    param.put("s_player_id", getStringDataToList(mSale.toString()));
                }else{
                    param.put("s_player_id", "0");
                }
                if (mBuy.size()!=0){
                    param.put("b_player_id", getStringDataToList(mBuy.toString()));
                }else{
                    param.put("b_player_id", "0");
                }
                param.put("contest_id", contest_id);
                Log.d("setContestDetails", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
        tv_HeaderName.setText(R.string.header_title_text);
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getJoinContentDetails();
    }

    /*public void GetTeamData() {
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

                        */
    /*hospitalAdapter = new HospitalAdapter(arr_bean,activity);
                        lv.setAdapter(hospitalAdapter);*/
    /*

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
    }*/
    /*    private void callSaveTeam(boolean isShowLoader) {
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
    }*/
    /*@Override
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
    }*/

    private void getJoinContentDetails() {
        Validations.showProgress(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SELECTED_PLAYER_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JoinContentDetails", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        Validations.hideProgress();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject innerData = jsonArray.getJSONObject(i);
                            String tbl_my_team_player_id = innerData.getString("tbl_my_team_player_id");
                            String currency_image = innerData.getString("currency_image");
                            String currency = innerData.getString("currency");
                            String share_qnt = innerData.getString("share_qnt");
                            String currency_rate = innerData.getString("currency_rate");
                            String team_id = innerData.getString("team_id");
                            String position = innerData.getString("position");
                            String contest_id = innerData.getString("contest_id");
                            String currency_value = innerData.getString("currency_value");
                            JoinTeamData joinTeamData = new JoinTeamData();
                            joinTeamData.setCurrency_value(currency_value);
                            joinTeamData.setCurrency_image(currency_image);
                            joinTeamData.setTblMyTeamPlayerId(tbl_my_team_player_id);
                            joinTeamData.setCurrencyRate(currency_rate);
                            joinTeamData.setContestId(contest_id);
                            joinTeamData.setShareQnt(share_qnt);
                            joinTeamData.setCurrency(currency);
                            joinTeamData.setPosition(position);
                            joinTeamData.setTeamId(team_id);
                            mJoinTeamData.add(joinTeamData);
                            Validations.hideProgress();
                        }
                        adapterFinalTeamList = new AdapterFinalTeamList(mJoinTeamData, activity,ChooseCandVCActivity.this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                        Rv_FinalPlayerList.setHasFixedSize(true);
                        Rv_FinalPlayerList.setLayoutManager(linearLayoutManager);
                        Rv_FinalPlayerList.setAdapter(adapterFinalTeamList);
                        adapterFinalTeamList.notifyDataSetChanged();

                    } else {
                        Validations.hideProgress();
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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
                Log.d("JoinContentDetails", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", HomeActivity.sessionManager.getUser(getApplicationContext()).getUser_id());
                param.put("team_id", team_id);
                param.put("contest_id", contest_id);
                Log.d("JoinContentDetails", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void notifyAdapterAfterData() {
        // Rv_FinalPlayerList.setItemAnimator(new DefaultItemAnimator());
        // Rv_FinalPlayerList.setNestedScrollingEnabled(false);
    }

    private String getStringDataToList(String id) {
        return id.replaceAll("\\[", "").replaceAll("\\]", "");
    }

    @Override
    public void setDataToBuy(String Buy,String type) {
        Log.d("SelectSB", "final Data : " + Buy +", Final_type:  "+type);
        if (type.equalsIgnoreCase("buy")){
            if (!Buy.equalsIgnoreCase("")) {
                if (!mSale.contains(Buy)) {
                    if (!mBuy.contains(Buy)) {
                        mBuy.add(Buy);
                        Log.d("SelectSB", "Buy Add : " + mBuy.toString());
                    } else {
                        mBuy.remove(Buy);
                        Log.d("SelectSB", "Buy Remove : " + mBuy.toString());
                    }
                }else{
                    mSale.remove(Buy);
                    mBuy.add(Buy);
                }
            }
        }else if(type.equalsIgnoreCase("sale")){
            if (!Buy.equalsIgnoreCase("")) {
                if (!mBuy.contains(Buy)) {
                    if (!mSale.contains(Buy)) {
                        mSale.add(Buy);
                        Log.d("SelectSB", "Sale Add : " + mSale.toString());
                    } else {
                        mSale.remove(Buy);
                        Log.d("SelectSB", "Sale Remove : " + mSale.toString());
                    }
                }else{
                    mBuy.remove(Buy);
                    mSale.add(Buy);
                }
            }
        }
    }

    @Override
    public void setDataToSale(String Sale,String type) {
        if (!Sale.equalsIgnoreCase("")) {
            if (!mSale.contains(Sale)) {
                mSale.add(Sale);
                Log.d("SelectSB", "Add : " + mSale.toString());
            } else {
                mSale.remove(Sale);
                Log.d("SelectSB", "Remove : " + mSale.toString());
            }
        }
    }

    public class AdapterFinalTeamList extends RecyclerView.Adapter<AdapterFinalTeamList.MyViewHolder> {
        private List<BeanDBTeam> mListenerList;
        Context mContext;
        private CheckBox lastChecked = null;
        private int lastCheckedPos = 0;
        BuySaleClick buySaleClick;
        private CheckBox lastChecked2 = null;
        private int lastCheckedPos2 = 0;

        private RadioButton lastCheckedRB = null;
        private RadioButton lastCheckedRB1 = null;
        TextView PreviousCaptain = null;
        TextView PreviousVC = null;
        List<JoinTeamData> mJoinTeam;


        public AdapterFinalTeamList(List<JoinTeamData> mJoin, Context context,BuySaleClick buySale) {
            mContext = context;
            this.mJoinTeam = mJoin;
            this.buySaleClick = buySale;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_PlayerName, tv_SelectCaptain, tv_SelectViceCaptain, tv_PlayerTeamName, tv_PlayerPoints, tv_TeamNumber;
            ImageView im_PlayerImage, im_onetwox,iv_display_price_status;
            CheckBox checkbox, checkbox2;
            RadioGroup radiogroup;
            RadioButton radio, radio2;
            public MyViewHolder(View view) {
                super(view);
                tv_PlayerName = view.findViewById(R.id.tv_PlayerName);
                tv_PlayerTeamName = view.findViewById(R.id.tv_PlayerTeamName);
                tv_PlayerPoints = view.findViewById(R.id.tv_PlayerPoints);
                im_PlayerImage = view.findViewById(R.id.im_PlayerImage);
                iv_display_price_status = view.findViewById(R.id.iv_display_price_status);
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

        //mJoinTeam.size();
        @Override
        public int getItemCount() {
            return mJoinTeam.size();
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
            /*String id = mListenerList.get(position).getMatchId();

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
            }*/
            /*holder.checkbox.setOnClickListener(new View.OnClickListener() {
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
            });*/
            Glide.with(mContext).load(mJoinTeam.get(position).getCurrency_image()).into(holder.im_PlayerImage);
            holder.tv_PlayerName.setText(mJoinTeam.get(position).getCurrency());
            holder.tv_PlayerTeamName.setText(mJoinTeam.get(position).getCurrencyRate());
            Log.d("adapterData", "C_ID : " + mJoinTeam.get(position).getContestId());

            /*holder.checkbox.setTag(new Integer(position));
            holder.checkbox2.setTag(new Integer(position));*/

            holder.checkbox.setTag("B");
            holder.checkbox2.setTag("S");
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox cb = (CheckBox) view;
                    String selectValue = cb.getTag().toString();
                    if (holder.checkbox2.isChecked()){
                        holder.checkbox2.setChecked(false);
                    }
                    String type_add = "buy";
                    CaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                    Log.d("Found_position","  >   "+CaptainId);
                    buySaleClick.setDataToBuy(CaptainId,type_add);
                }
            });

            holder.checkbox2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox cb = (CheckBox) view;
                    String selectValue = cb.getTag().toString();
                    if (holder.checkbox.isChecked()){
                        holder.checkbox.setChecked(false);
                    }
                    String type_add = "sale";
                    ViceCaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                    Log.d("Found_position","  >   "+ViceCaptainId);
                    buySaleClick.setDataToBuy(ViceCaptainId,type_add);
                }
            });

            if (mJoinTeam.get(position).isCurrency_value().equalsIgnoreCase("high")){
                holder.iv_display_price_status.setImageResource(R.drawable.ic_upward);
            }else if(mJoinTeam.get(position).isCurrency_value().equalsIgnoreCase("low")){
                holder.iv_display_price_status.setImageResource(R.drawable.ic_downward);
            }

            // not in use 11-11-2020
            /*holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox cb = (CheckBox) view;
                    int clickedPos = ((Integer) cb.getTag()).intValue();
                    if (holder.checkbox2.isChecked()) {
                        ViceCaptainId = "0";
                    }
                    holder.checkbox2.setChecked(false);
                    if (cb.isChecked()) {
                        if (lastChecked != null) {
                            mJoinTeam.get(lastCheckedPos).setSelected(false);
                            lastChecked.setChecked(false);
                            ViceCaptainId = "0";
                            CaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                        } else if (clickedPos == position) {
                            // holder.checkbox.setTextColor(getResources().getColor(R.color.white));
                            lastCheckedPos = clickedPos;
                            lastChecked = cb;
                            lastChecked.setChecked(true);
                            CaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                        }
                        lastCheckedPos = clickedPos;
                        lastChecked = cb;
                        CaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                        // holder.checkbox.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        // holder.checkbox.setTextColor(getResources().getColor(R.color.ptl_color));
                        lastChecked = null;
                        CaptainId = "0";
                    }
                    try {
                        lastChecked.setChecked(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mJoinTeam.get(clickedPos).setSelected(cb.isChecked());
                    CaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                    Log.d("check_position", "Position CaptainId : " + CaptainId);
                }
            });

            holder.checkbox2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox cb = (CheckBox) view;
                    int clickedPos = ((Integer) cb.getTag()).intValue();
                    if (holder.checkbox.isChecked()) {
                        CaptainId = "0";
                    }
                    holder.checkbox.setChecked(false);
                    if (cb.isChecked()) {
                        if (lastChecked2 != null) {
                            lastChecked2.setChecked(false);
                            mJoinTeam.get(lastCheckedPos2).setSelected(false);
                            // CaptainId="0";
                            ViceCaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                        } else if (clickedPos == position) {
                            //holder.checkbox2.setTextColor(getResources().getColor(R.color.white));
                            lastChecked2 = cb;
                            lastCheckedPos2 = clickedPos;
                            lastChecked2.setChecked(true);
                            ViceCaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                        }
                        lastChecked2 = cb;
                        lastCheckedPos2 = clickedPos;
                        ViceCaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                    } else {
                        lastChecked2 = null;
                        ViceCaptainId = "0";
                    }
                    mJoinTeam.get(clickedPos).setSelected2(cb.isChecked());
                    ViceCaptainId = mJoinTeam.get(position).getTblMyTeamPlayerId();
                    Log.d("check_position", "Position ViceCaptainId : " + ViceCaptainId);
                }
            });*/
            /*holder.checkbox2.setOnClickListener(new View.OnClickListener() {
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

            });*/
        }
    }
}
