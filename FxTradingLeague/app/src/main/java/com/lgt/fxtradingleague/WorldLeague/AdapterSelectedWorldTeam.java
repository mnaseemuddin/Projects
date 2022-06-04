package com.lgt.fxtradingleague.WorldLeague;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.fxtradingleague.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSelectedWorldTeam extends RecyclerView.Adapter<AdapterSelectedWorldTeam.TeamHolder> {

    ArrayList<DJWLeagueModel> mListData;
    Context mContext;
    DecimalFormat decimalFormat = new DecimalFormat("###.##");

    public AdapterSelectedWorldTeam(ArrayList<DJWLeagueModel> mListData, Context mContext) {
        this.mListData = mListData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdapterSelectedWorldTeam.TeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.view_single_player, parent, false);
        return new AdapterSelectedWorldTeam.TeamHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelectedWorldTeam.TeamHolder holder, int position) {
        holder.tv_CoinName.setText("Company : " + mListData.get(position).getCurrency());
        holder.tv_share_qnt.setText("Quantity : " + decimalFormat.format(Double.parseDouble(mListData.get(position).getShare_qnt())));
        holder.tv_profit_value.setText("Profit Value : " + mListData.get(position).getProfit_value());
        Glide.with(mContext).load(mListData.get(position).getCompany_image()).apply(new RequestOptions()
                .override(192, 192)).into(holder.cv_CoinIcon);
        if (mListData.get(position).getProfit_value().equalsIgnoreCase("0")) {
        } else {
            if (mListData.get(position).getProfit_value().contains("-")) {
                holder.cv_eventCardBackgroundType.setCardBackgroundColor(mContext.getResources().getColor(R.color.red_light));
                // holder.ll_eventCardBackgroundType.setBackground(getDrawableWithRedRadius());
            } else {
                holder.cv_eventCardBackgroundType.setCardBackgroundColor(mContext.getResources().getColor(R.color.green_light));
                // holder.ll_eventCardBackgroundType.setBackground(getDrawableWithGreenRadius());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class TeamHolder extends RecyclerView.ViewHolder {
        CircleImageView cv_CoinIcon;
        TextView tv_CoinName, tv_share_qnt, tv_profit_value;
        CardView cv_eventCardBackgroundType;
        LinearLayout ll_eventCardBackgroundType;

        public TeamHolder(@NonNull View itemView) {
            super(itemView);
            tv_CoinName = itemView.findViewById(R.id.tv_CoinName);
            cv_CoinIcon = itemView.findViewById(R.id.cv_CoinIcon);
            tv_share_qnt = itemView.findViewById(R.id.tv_share_qnt);
            tv_profit_value = itemView.findViewById(R.id.tv_profit_value);
            cv_eventCardBackgroundType = itemView.findViewById(R.id.cv_eventCardBackgroundType);
            ll_eventCardBackgroundType = itemView.findViewById(R.id.ll_eventCardBackgroundType);
        }
    }
}