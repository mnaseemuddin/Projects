package com.lgt.fxtradingleague.TradingAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.IndiWorldLeague.ActivityIndiWorldLeague;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.TradingModel.Datum;
import com.lgt.fxtradingleague.TradingPackage.SelectTradingMaster;
import com.lgt.fxtradingleague.WorldLeague.ActivityWorldLeague;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.lgt.fxtradingleague.Extra.ExtraData.KEYS_CONTEST_TYPE;

public class JoinAdapter extends RecyclerView.Adapter<JoinAdapter.JoinHolder> {

    String contest_type, currency_type;
    Context mContext;
    List<Datum> mJoinListData;
    //Dialog dialog;
    BottomSheetDialog dialog;

    String givenDateString = "Tue Apr 23 16:08:28 GMT+05:30 2013";

    public JoinAdapter(Context mContext, List<Datum> mListData, String mType, String mCurrency) {
        this.mContext = mContext;
        this.mJoinListData = mListData;
        this.contest_type = mType;
        this.currency_type = mCurrency;
    }

    @NonNull
    @Override
    public JoinHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.join_contest_list_items, parent, false);
        return new JoinHolder(mView);
    }


    @Override
    public void onBindViewHolder(@NonNull JoinHolder holder, final int position) {
        Validations.common_log("types_of_event: " + currency_type);

        int leftTeam = Integer.parseInt(mJoinListData.get(position).getTotalTeam()) - Integer.parseInt(mJoinListData.get(position).getJoinTeam());
        holder.tv_MyTeamName.setText(mJoinListData.get(position).getContestName());
        holder.tv_EntryFees.setText("EntryFee : " + mJoinListData.get(position).getEntryFee());
        holder.tv_TeamLeftCount.setText("Teams Left " + leftTeam);
        holder.tv_TotalPrice.setText(mJoinListData.get(position).getPrizePool());
        holder.tv_TotalTeamCount.setText(mJoinListData.get(position).getTotalTeam() + " Teams");
        holder.tv_LiveContestDesc.setText(mJoinListData.get(position).getContestTag());
        holder.tv_WinnersCount.setText(mJoinListData.get(position).getWinner());
        holder.PB_EntryProgress.setMax(Integer.parseInt(mJoinListData.get(position).getTotalTeam()));
        holder.PB_EntryProgress.setProgress(Integer.parseInt(mJoinListData.get(position).getJoinTeam()));

        holder.tv_WinnersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialog = new BottomSheetDialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_winning_breakups);
                    final TextView tv_DClose = dialog.findViewById(R.id.tv_DClose);
                    final TextView tv_DTotalWinning = dialog.findViewById(R.id.tv_DTotalWinning);
                    final TextView tv_DBottomNote = dialog.findViewById(R.id.tv_DBottomNote);
                    final LinearLayout LL_WinningBreackupList = dialog.findViewById(R.id.LL_WinningBreackupList);
                    dialog.show();
                    tv_DTotalWinning.setText("" + mJoinListData.get(position).getPrizePool());
                    tv_DBottomNote.setText("Note: " + mJoinListData.get(position).getContestTag());
                    tv_DClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    for (int i = 0; i < mJoinListData.get(position).getWinningInformation().size(); i++) {
                        View to_add = LayoutInflater.from(mContext).inflate(R.layout.item_winning_breakup,
                                LL_WinningBreackupList, false);
                        TextView tv_Rank = to_add.findViewById(R.id.tv_Rank);
                        TextView tv_Price = to_add.findViewById(R.id.tv_Price);
                        RelativeLayout rankRL = to_add.findViewById(R.id.rankRL);
                        if (i % 2 == 1) {
                            rankRL.setBackgroundColor(mContext.getResources().getColor(R.color.bg_gray));
                        }
                        tv_Rank.setText(mJoinListData.get(position).getWinningInformation().get(i).getFromRank());
                        tv_Price.setText("" + mJoinListData.get(position).getWinningInformation().get(i).getPrice());
                        LL_WinningBreackupList.addView(to_add);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // 09:00:00 -- 09:00
        // 19:00:00 -- 07:00
        // 13:30:00 -- 01:30
        getMilliSecondbytime("02:30:00", holder);


        holder.tv_JoinContestTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currency_type.equalsIgnoreCase("CRYPTO")) {
                    Intent joinContast = new Intent(mContext, SelectTradingMaster.class);
                    joinContast.putExtra("KEY_JOIN_MATCH", "YES");
                    joinContast.putExtra("KEY_JOIN_ID", mJoinListData.get(position).getTblContestId());
                    joinContast.putExtra("EntryFee", mJoinListData.get(position).getEntryFee());
                    joinContast.putExtra("ContestType", contest_type);
                    joinContast.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    joinContast.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(joinContast);
                } else if (currency_type.equalsIgnoreCase("WorldLeague")) {
                    Intent joinContast = new Intent(mContext, ActivityWorldLeague.class);
                    joinContast.putExtra("KEY_JOIN_MATCH", "YES");
                    joinContast.putExtra("KEY_JOIN_ID", mJoinListData.get(position).getTblContestId());
                    joinContast.putExtra("EntryFee", mJoinListData.get(position).getEntryFee());
                    joinContast.putExtra(KEYS_CONTEST_TYPE, contest_type);
                    joinContast.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    joinContast.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(joinContast);
                } else if (currency_type.equalsIgnoreCase("NASDAQ")) {
                    Intent joinContast = new Intent(mContext, ActivityIndiWorldLeague.class);
                    joinContast.putExtra("KEY_JOIN_MATCH", "YES");
                    joinContast.putExtra("KEY_JOIN_ID", mJoinListData.get(position).getTblContestId());
                    joinContast.putExtra("EntryFee", mJoinListData.get(position).getEntryFee());
                    joinContast.putExtra(KEYS_CONTEST_TYPE, contest_type);
                    joinContast.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    joinContast.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(joinContast);
                }
            }
        });
    }

    long milliseconds, startTime;


    public void getMilliSecondbytime(String time, final JoinHolder mHolder) {
        ; // Results in "2-5-2012 20:43"
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss"); // I assume d-M, you may refer to M-d for month-day instead.
        Date date = null; // You will need try/catch around this
        try {
            date = formatter.parse(time);
            startTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        milliseconds = System.currentTimeMillis();

       /* CountDownTimer cdt = new CountDownTimer(milliseconds, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                startTime = startTime - 1;
                Long serverUptimeSeconds = (millisUntilFinished - startTime) / 1000;
                @SuppressLint("DefaultLocale") String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                @SuppressLint("DefaultLocale") String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                @SuppressLint("DefaultLocale") String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                mHolder.tv_ContestLiveTime.setText("Start in : " + hoursLeft + "h " + minutesLeft + "m " + secondsLeft + "s");
            }

            @Override
            public void onFinish() {
                mHolder.tv_ContestLiveTime.setText("Event Time Out.");
            }
        };
        cdt.start();*/
    }

    @Override
    public int getItemCount() {
        return mJoinListData.size();
    }

    public class JoinHolder extends RecyclerView.ViewHolder {
        TextView tv_ContestLiveTime, tv_JoinContestTeam, tv_MyTeamName, tv_TotalPrice, tv_TeamLeftCount, tv_TotalTeamCount, tv_WinnersCount, tv_EntryFees, tv_LiveContestDesc;
        ProgressBar PB_EntryProgress;

        public JoinHolder(@NonNull View itemView) {
            super(itemView);
            tv_MyTeamName = itemView.findViewById(R.id.tv_MyTeamName);
            tv_TotalPrice = itemView.findViewById(R.id.tv_TotalPrice);
            tv_ContestLiveTime = itemView.findViewById(R.id.tv_ContestLiveTime);
            tv_JoinContestTeam = itemView.findViewById(R.id.tv_JoinContestTeam);
            tv_TeamLeftCount = itemView.findViewById(R.id.tv_TeamLeftCount);
            tv_TotalTeamCount = itemView.findViewById(R.id.tv_TotalTeamCount);
            tv_WinnersCount = itemView.findViewById(R.id.tv_WinnersCount);
            tv_EntryFees = itemView.findViewById(R.id.tv_EntryFees);
            PB_EntryProgress = itemView.findViewById(R.id.PB_EntryProgress);
            tv_LiveContestDesc = itemView.findViewById(R.id.tv_LiveContestDesc);
        }
    }
}
