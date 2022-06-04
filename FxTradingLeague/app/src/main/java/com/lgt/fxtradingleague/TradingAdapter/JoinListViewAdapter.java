package com.lgt.fxtradingleague.TradingAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.MyTabFragment.MyFixtureContestDetailsActivity;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.TradingModel.MyJoinTeamModel;

import java.util.ArrayList;

public class JoinListViewAdapter extends RecyclerView.Adapter<JoinListViewAdapter.JoinHolderView> {

    Context mContext;
    ArrayList<MyJoinTeamModel> mListData;
    String user_ID, Match_Status;

    public JoinListViewAdapter(Context mContext, ArrayList<MyJoinTeamModel> mListData, String userId, String matchStatus) {
        this.mContext = mContext;
        this.mListData = mListData;
        this.user_ID = userId;
        this.Match_Status = matchStatus;
    }

    @NonNull
    @Override
    public JoinHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.join_list_item_view, parent, false);
        return new JoinHolderView(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinHolderView holder, final int position) {
        int teamLeft = Integer.parseInt(mListData.get(position).getTotal_team()) - Integer.parseInt(mListData.get(position).getJoin_team());
        holder.tv_ViewMyTeamName.setText(mListData.get(position).getContest_name());
        holder.tv_View_EntryFees.setText("Entry Token " + mListData.get(position).getEntry_fee());
        holder.tv_View_LiveContestDesc.setText(mListData.get(position).getMain_contest_name());
        holder.tv_TotalPrice.setText(mListData.get(position).getPrize_pool());
        holder.tv_View_WinnersCount.setText(mListData.get(position).getWinner());
        holder.PB_EntryProgress.setMax(Integer.parseInt(mListData.get(position).getTotal_team()));
        holder.PB_EntryProgress.setProgress(Integer.parseInt(mListData.get(position).getJoin_team()));
        holder.tv_View_TeamLeftCount.setText("Teams Left " + teamLeft);
        holder.tv_View_TotalTeamCount.setText(mListData.get(position).getJoin_team() + " Teams");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inData = new Intent(mContext, MyFixtureContestDetailsActivity.class);
                inData.putExtra("user_id", user_ID);
                inData.putExtra("match_status", Match_Status);
                inData.putExtra("contest_id", mListData.get(position).getTbl_contest_id());
                inData.putExtra("joing_date", mListData.get(position).getJoing_date());
                Log.d("BindViewHolder", ">>   " + mListData.get(position).getTbl_contest_id());
                mContext.startActivity(inData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class JoinHolderView extends RecyclerView.ViewHolder {
        TextView tv_TotalPrice,tv_View_JoinContestTeam, tv_View_EntryFees, tv_View_LiveContestDesc, tv_View_WinnersCount, tv_ViewMyTeamName, tv_View_TeamLeftCount, tv_View_TotalTeamCount;
        ProgressBar PB_EntryProgress;

        public JoinHolderView(@NonNull View itemView) {
            super(itemView);
            tv_TotalPrice = itemView.findViewById(R.id.tv_TotalPrice);
            tv_View_JoinContestTeam = itemView.findViewById(R.id.tv_View_JoinContestTeam);
            tv_View_EntryFees = itemView.findViewById(R.id.tv_View_EntryFees);
            tv_View_LiveContestDesc = itemView.findViewById(R.id.tv_View_LiveContestDesc);
            tv_View_WinnersCount = itemView.findViewById(R.id.tv_View_WinnersCount);
            tv_ViewMyTeamName = itemView.findViewById(R.id.tv_ViewMyTeamName);
            PB_EntryProgress = itemView.findViewById(R.id.PB_EntryProgress);
            tv_View_TeamLeftCount = itemView.findViewById(R.id.tv_View_TeamLeftCount);
            tv_View_TotalTeamCount = itemView.findViewById(R.id.tv_View_TotalTeamCount);
        }
    }
}
