package com.lgt.fxtradingleague.TradingAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.Extra.ClickBottomPlayerInfo;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.TradingModel.TeamModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.HolderContest> {

    ArrayList<TeamModel> mTeamList;
    Context mContext;
    String registerID,EventType;
    ClickBottomPlayerInfo clickBottomPlayerInfo;

    public ContestAdapter(ArrayList<TeamModel> mTeamList, Context mContext, String sessionUserId,String mEvent,ClickBottomPlayerInfo PlayerInfo) {
        this.mTeamList = mTeamList;
        this.mContext = mContext;
        this.clickBottomPlayerInfo = PlayerInfo;
        this.registerID = sessionUserId;
        this.EventType = mEvent;
    }

    @NonNull
    @Override
    public HolderContest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.adapter_leaderboard_list, parent, false);
        return new HolderContest(mView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HolderContest holder, final int position) {
        int my_rank = position+1;
        holder.tv_LeaderboardPlayerRank.setText(""+my_rank);
        holder.tv_LeaderboardPlayerTeamName.setText(mTeamList.get(position).getName());

        if (registerID.equalsIgnoreCase(mTeamList.get(position).getUser_id())) {
            // holder.RL_LeaderboardMain.setBackgroundResource(R.color.ptl_color);
            holder.RL_LeaderboardMain.setBackgroundResource(R.drawable.flicker_background);
            holder.tv_LeaderboardPlayerTeamName.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.tv_LeaderboardPlayerRank.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        Validations.common_log("TypeEvent: "+EventType);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerID.equalsIgnoreCase(mTeamList.get(position).getUser_id())) {
                    clickBottomPlayerInfo.clickToViewPlayer(mTeamList.get(position).getTeam_id(),mTeamList.get(position).getName(),mTeamList.get(position).getOverall_profit());
                }else if(EventType.equalsIgnoreCase("1")){
                    clickBottomPlayerInfo.clickToViewPlayer(mTeamList.get(position).getTeam_id(),mTeamList.get(position).getName(),mTeamList.get(position).getOverall_profit());
                }else if(EventType.equalsIgnoreCase("2")){
                    clickBottomPlayerInfo.clickToViewPlayer(mTeamList.get(position).getTeam_id(),mTeamList.get(position).getName(),mTeamList.get(position).getOverall_profit());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTeamList.size();
    }

    public class HolderContest extends RecyclerView.ViewHolder {

        RelativeLayout RL_LeaderboardMain;
        CircleImageView im_LeaderboardPlayerAvtar;
        TextView tv_LeaderboardPlayerTeamName, tv_LeaderboardPlayerRank;
        CardView cv_select_user_view;
        public HolderContest(@NonNull View itemView) {
            super(itemView);
            cv_select_user_view = itemView.findViewById(R.id.cv_select_user_view);
            RL_LeaderboardMain = itemView.findViewById(R.id.RL_LeaderboardMain);
            tv_LeaderboardPlayerRank = itemView.findViewById(R.id.tv_LeaderboardPlayerRank);
            im_LeaderboardPlayerAvtar = itemView.findViewById(R.id.im_LeaderboardPlayerAvtar);
            tv_LeaderboardPlayerTeamName = itemView.findViewById(R.id.tv_LeaderboardPlayerTeamName);
        }
    }
}
