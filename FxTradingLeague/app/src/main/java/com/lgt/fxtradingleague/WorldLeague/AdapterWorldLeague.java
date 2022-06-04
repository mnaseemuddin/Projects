package com.lgt.fxtradingleague.WorldLeague;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lgt.fxtradingleague.Extra.WorldCompanyTrade;
import com.lgt.fxtradingleague.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterWorldLeague extends RecyclerView.Adapter<AdapterWorldLeague.LeagueViewHolder> {

    char mFirst;
    String mSymbolText = "";
    ArrayList<WorldLeagueModel> mListData;
    Context mContext;
    WorldCompanyTrade mCompanyTrade;
    private Set<String> selected_trade = new HashSet<>();
    Drawable.ConstantState constantStateDrawableA;
    Drawable.ConstantState constantStateDrawableB;

    public AdapterWorldLeague(ArrayList<WorldLeagueModel> mListData, Context mContext, WorldCompanyTrade mTrade) {
        this.mListData = mListData;
        this.mContext = mContext;
        this.mCompanyTrade = mTrade;
        constantStateDrawableA = mContext.getResources().getDrawable(R.drawable.check).getConstantState();
        constantStateDrawableB = mContext.getResources().getDrawable(R.drawable.uncheck).getConstantState();
    }

    @NonNull
    @Override
    public LeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_world_trading_items, parent, false);
        return new LeagueViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueViewHolder holder, int position) {
        final int selected_position = position + 1;
        final WorldLeagueModel dataModel = mListData.get(position);

        if (dataModel.getCurrency_value().equalsIgnoreCase("high")) {
            holder.tv_price_world_cmp_difference.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upward, 0, 0, 0);
        } else {
            holder.tv_price_world_cmp_difference.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_downward, 0, 0, 0);
        }
        mSymbolText = dataModel.getSymbol();
        mFirst = mSymbolText.charAt(0);
        holder.tv_company_symbol.setText("" + mFirst);
        holder.tv_world_cmp_company_name.setText(dataModel.getCompany_name());
        holder.tv_world_cmp_current_price.setText(dataModel.getOpen_price());
        holder.tv_price_world_cmp_difference.setText(dataModel.getPrevious_open_price());
        // click event
        holder.tv_world_cmp_company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCompanyTrade.showDetailsAboutCompany(dataModel.getCompany_name(),dataModel.getSymbol(),dataModel.getOpen_price(),dataModel.getDayHigh(),dataModel.getDayLow(),dataModel.getExchange(),dataModel.getPrevious_open_price(),dataModel.getPreviousClose(),dataModel.getChangesPercentage(),dataModel.getDaily_changes());
            }
        });
        // check box with data
        if (selected_trade.contains(dataModel.getTbl_world_league_id())) {
            holder.iv_world_cmp_CheckBoxCheck.setVisibility(View.VISIBLE);
            holder.iv_world_cmp_CheckBoxUncheck.setVisibility(View.GONE);
        } else {
            holder.iv_world_cmp_CheckBoxCheck.setVisibility(View.GONE);
            holder.iv_world_cmp_CheckBoxUncheck.setVisibility(View.VISIBLE);
        }
        // load images
        Glide.with(mContext).load(dataModel.getImages()).into(holder.iv_world_cmp_icon);
        // set click listener
        holder.rl_world_cmp_select_trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_trade.size() < 11) {
                    if (selected_trade.contains(dataModel.getTbl_world_league_id())) {
                        selected_trade.remove(dataModel.getTbl_world_league_id());
                    } else {
                        selected_trade.add(dataModel.getTbl_world_league_id());
                    }
                    mCompanyTrade.selectedItemsCount(selected_trade.size());
                    mCompanyTrade.setDataOfSelectedList(dataModel.getSymbol(), dataModel.getOpen_price());
                } else {
                    if (selected_trade.contains(dataModel.getTbl_world_league_id())) {
                        if (selected_trade.contains(dataModel.getTbl_world_league_id())) {
                            selected_trade.remove(dataModel.getTbl_world_league_id());
                        } else {
                            selected_trade.add(dataModel.getTbl_world_league_id());
                        }
                        mCompanyTrade.selectedItemsCount(selected_trade.size());
                        mCompanyTrade.setDataOfSelectedList(dataModel.getSymbol(), dataModel.getOpen_price());
                    }
                    Toast.makeText(mContext, "You can select Only 11 Stock", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class LeagueViewHolder extends RecyclerView.ViewHolder {
        TextView tv_world_cmp_current_price, tv_world_cmp_company_name, tv_world_cmp_change_price, tv_world_cmp_actual_price, tv_price_world_cmp_difference, tv_company_symbol;
        LinearLayout ll_world_cmp_checkBox_Container;
        ImageView iv_world_cmp_CheckBoxUncheck, iv_world_cmp_CheckBoxCheck;
        RelativeLayout rl_world_cmp_select_trade;
        CircleImageView iv_world_cmp_icon;
        public LeagueViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_world_cmp_current_price = itemView.findViewById(R.id.tv_world_cmp_current_price);
            tv_world_cmp_company_name = itemView.findViewById(R.id.tv_world_cmp_company_name);
            tv_world_cmp_change_price = itemView.findViewById(R.id.tv_world_cmp_change_price);
            tv_world_cmp_actual_price = itemView.findViewById(R.id.tv_world_cmp_actual_price);
            tv_price_world_cmp_difference = itemView.findViewById(R.id.tv_price_world_cmp_difference);
            ll_world_cmp_checkBox_Container = itemView.findViewById(R.id.ll_world_cmp_checkBox_Container);
            rl_world_cmp_select_trade = itemView.findViewById(R.id.rl_world_cmp_select_trade);
            iv_world_cmp_icon = itemView.findViewById(R.id.iv_world_cmp_icon);
            tv_company_symbol = itemView.findViewById(R.id.tv_company_symbol);
            iv_world_cmp_CheckBoxUncheck = itemView.findViewById(R.id.iv_world_cmp_CheckBoxUncheck);
            iv_world_cmp_CheckBoxCheck = itemView.findViewById(R.id.iv_world_cmp_CheckBoxCheck);
        }
    }
}
