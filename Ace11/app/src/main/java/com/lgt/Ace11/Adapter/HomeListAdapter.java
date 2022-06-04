package com.lgt.Ace11.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.Ace11.Bean.BeanHomeFixtures;
import com.lgt.Ace11.Config;
import com.lgt.Ace11.ContestListActivity;
import com.lgt.Ace11.FragmentBottomMenu.BottomSheetMatchStatus;
import com.lgt.Ace11.MyTabFragment.MyFixtureContestDetailsActivity;
import com.lgt.Ace11.Playing11;
import com.lgt.Ace11.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeListholder> {

    Context mContext;
    List<BeanHomeFixtures> beanHomeFixtures;

    public HomeListAdapter(Context mContext, List<BeanHomeFixtures> beanHome) {
        this.mContext = mContext;
        this.beanHomeFixtures = beanHome;
    }

    @NonNull
    @Override
    public HomeListholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.home_all_list_items, parent, false);
        return new HomeListholder(mView);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeListholder holder, int position) {
        final String match_id = beanHomeFixtures.get(position).getMatch_id();
        String teamid1 = beanHomeFixtures.get(position).getTeamid1();
        String match_status = beanHomeFixtures.get(position).getMatch_status();
        String match_type = beanHomeFixtures.get(position).getMatch_type();
        String type = beanHomeFixtures.get(position).getType();
        String title = beanHomeFixtures.get(position).getTitle();
        final int time = beanHomeFixtures.get(position).getTime();
        String teamid2 = beanHomeFixtures.get(position).getTeamid2();
        final String team_name1 = beanHomeFixtures.get(position).getTeam_name1();
        final String team_image1 = beanHomeFixtures.get(position).getTeam_image1();
        final String team_short_name1 = beanHomeFixtures.get(position).getTeam_short_name1();
        final String team_name2 = beanHomeFixtures.get(position).getTeam_name2();
        final String team_image2 = beanHomeFixtures.get(position).getTeam_image2();
        final String team_short_name2 = beanHomeFixtures.get(position).getTeam_short_name2();
        final String unique_id = beanHomeFixtures.get(position).getUnique_id();
        String team1 = team_short_name1.replaceAll(" ", "");
        String team2 = team_short_name2.replaceAll(" ", "");
        // set data to view.
        if (team1.length() > 3) {
            holder.tv_Team1SortName.setText(team1.substring(0, 3));
        } else {
            holder.tv_Team1SortName.setText(team1);
        }

        if (team2.length() > 3) {
            holder.tv_Team2SortName.setText(team2.substring(0, 3));
        } else {
            holder.tv_Team2SortName.setText(team2);
        }
        holder.tv_TeamTitle.setText(type);
        holder.tv_countTeam.setText("Match Type: " + match_type);
        holder.tv_Team1.setText(team_name1);
        holder.tv_Team2.setText(team_name2);
        Glide.with(mContext).load(Config.TEAMFLAGIMAGE + team_image1)
                //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_TeamImage1);
        Glide.with(mContext).load(Config.TEAMFLAGIMAGE + team_image2)
                //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_TeamImage2);

        if (match_status.equals("Fixture")) {
            holder.tv_timerPendingTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.watch_icon, 0, 0, 0);
            holder.tv_timerPendingTime.setText(time + "");
            if (holder.countDownTimer != null) {
                holder.countDownTimer.cancel();
            }

            try {
                int FlashCount = time;
                long millisUntilFinished = FlashCount * 1000;
                /* long Days = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long Hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                long Minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long Seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)); */
                //String format = "%1$02d";
                //holder.tv_TimeRemained.setText(String.format(format, Days) + ":" + String.format(format, Hours) + ":" + String.format(format, Minutes) + ":" + String.format(format, Seconds));
                holder.countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
                    public void onTick(long millisUntilFinished) {
                        long Days = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                        long Hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                        long Minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                        long Seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                        String format = "%1$02d";
                        holder.tv_timerPendingTime.setText(String.format(format, Days) + ":" + String.format(format, Hours) + ":" + String.format(format, Minutes) + ":" + String.format(format, Seconds));

                        // Log.e("dsfdsfdsfdsfdsfsd",(String.format(format, Days) + ":" + String.format(format, Hours) + ":" + String.format(format, Minutes) + ":" + String.format(format, Seconds)+""));

                    }

                    public void onFinish() {
                        holder.tv_timerPendingTime.setText("Entry Over!");
                    }

                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ClickMe", "Click OutSide");
                if (holder.tv_timerPendingTime.getText().toString().equals("Entry Over!")) {
                    Log.d("ClickMe", "Click InSide 1");
                    ShowToast(mContext, "Entry Over");
                } else {
                    Log.d("ClickMe", "Click InSide 2");
                    Intent k = new Intent(mContext, ContestListActivity.class);
                    k.putExtra("MatchId", match_id);
                    k.putExtra("Time", time + "");
                    k.putExtra("UNIQUE_ID", unique_id + "");
                    //     k.putExtra("TeamsName", holder.tv_TeamsName.getText().toString());
                    k.putExtra("TeamsOneName", team_short_name1);
                    k.putExtra("TeamsTwoName", team_short_name2);
                    k.putExtra("team1Image", Config.TEAMFLAGIMAGE + team_image1);
                    k.putExtra("team2Image", Config.TEAMFLAGIMAGE + team_image2);
                    mContext.startActivity(k);
                }
            }
        });

        holder.tv_play11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMatchStatus(unique_id);
            }
        });


        holder.iv_AlertIcon.setVisibility(View.GONE);
        holder.iv_AlertIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "We Will Update You \n When Match Start!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanHomeFixtures.size();
    }

    public class HomeListholder extends RecyclerView.ViewHolder {
        ImageView iv_AlertIcon, iv_TeamImage1, iv_TeamImage2;
        TextView tv_TeamTitle, tv_Team1, tv_Team2, tv_Team1SortName, tv_timerPendingTime, tv_Team2SortName, tv_countTeam,tv_play11;
        CountDownTimer countDownTimer;

        public HomeListholder(@NonNull View itemView) {
            super(itemView);
            iv_AlertIcon = itemView.findViewById(R.id.iv_AlertIcon);
            iv_TeamImage1 = itemView.findViewById(R.id.iv_TeamImage1);
            iv_TeamImage2 = itemView.findViewById(R.id.iv_TeamImage2);
            tv_TeamTitle = itemView.findViewById(R.id.tv_TeamTitle);
            tv_Team1 = itemView.findViewById(R.id.tv_Team1);
            tv_Team2 = itemView.findViewById(R.id.tv_Team2);
            tv_Team1SortName = itemView.findViewById(R.id.tv_Team1SortName);
            tv_timerPendingTime = itemView.findViewById(R.id.tv_timerPendingTime);
            tv_Team2SortName = itemView.findViewById(R.id.tv_Team2SortName);
            tv_countTeam = itemView.findViewById(R.id.tv_countTeam);
            tv_play11=itemView.findViewById(R.id.tv_play11);
        }
    }

    private void checkMatchStatus(final String unique_id) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
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
                            bottomSheetMatchStatus.show(((AppCompatActivity)mContext).getSupportFragmentManager(),"BottomSheetMatchStatus");
                        }
                        else {
                            Intent intent = new Intent(mContext, Playing11.class);
                            intent.putExtra("KEY_UNIQUE_ID",unique_id);
                            Log.e("Dasrwerewrwer",unique_id+"");
                            mContext.startActivity(intent);
                        }
                    }

                    else {
                        Toast.makeText(mContext, "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Network or server error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }


}
