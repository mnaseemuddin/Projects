package com.lgt.fxtradingleague.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.Extra.IndiCompanyTrade;
import com.lgt.fxtradingleague.IndiModel;
import com.lgt.fxtradingleague.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class IndiCompanyAdapter extends RecyclerView.Adapter<IndiCompanyAdapter.CompanyHolder> {

    Context mContext;
    List<IndiModel> mLeagueList;
    private Set<String> selected_Indi_League = new HashSet<>();
    Drawable.ConstantState constantStateDrawableA;
    Drawable.ConstantState constantStateDrawableB;
    IndiCompanyTrade mIndiCompanyTrade;

    public IndiCompanyAdapter(Context context, List<IndiModel> mLeagueList, IndiCompanyTrade mIndiCompany) {
        this.mContext = context;
        this.mLeagueList = mLeagueList;
        this.mIndiCompanyTrade = mIndiCompany;
        constantStateDrawableA = mContext.getResources().getDrawable(R.drawable.check).getConstantState();
        constantStateDrawableB = mContext.getResources().getDrawable(R.drawable.uncheck).getConstantState();
    }

    @NonNull
    @Override
    public CompanyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_indi_adapter, parent, false);
        return new CompanyHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyHolder holder, int position) {
        final IndiModel indiModel = mLeagueList.get(position);
        Validations.common_log("Status: " + indiModel.getCurrency_value());
        holder.tv_iv_IndiLeague_company_name.setText(indiModel.getCompany_name());
        Glide.with(mContext).load(indiModel.getImage()).into(holder.iv_IndiLeague_icon);

        if (indiModel.getCurrency_value().equalsIgnoreCase("high")) {
            holder.tv_price_iv_IndiLeague_difference.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upward, 0, 0, 0);
        } else {
            holder.tv_price_iv_IndiLeague_difference.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_downward, 0, 0, 0);
        }

        holder.tv_iv_IndiLeague_current_price.setText(indiModel.getOpen_price());
        holder.tv_price_iv_IndiLeague_difference.setText(indiModel.getClose_price());

        holder.tv_iv_IndiLeague_company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndiCompanyTrade.showDetailsAboutCompany(indiModel);
            }
        });

        if (selected_Indi_League.contains(indiModel.getTbl_indian_league_id())) {
            holder.iv_IndiLeague_CheckBoxCheck.setVisibility(View.VISIBLE);
            holder.iv_IndiLeague_CheckBoxUncheck.setVisibility(View.GONE);
        } else {
            holder.iv_IndiLeague_CheckBoxCheck.setVisibility(View.GONE);
            holder.iv_IndiLeague_CheckBoxUncheck.setVisibility(View.VISIBLE);
        }

        holder.rl_IndiLeague_select_trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_Indi_League.size() < 11) {
                    if (selected_Indi_League.contains(indiModel.getTbl_indian_league_id())) {
                        selected_Indi_League.remove(indiModel.getTbl_indian_league_id());
                    } else {
                        selected_Indi_League.add(indiModel.getTbl_indian_league_id());
                    }
                    mIndiCompanyTrade.selectedItemsCount(selected_Indi_League.size());
                    mIndiCompanyTrade.setDataOfSelectedList(indiModel.getSymbol_to_display(), indiModel.getOpen_price());
                } else {
                    if (selected_Indi_League.contains(indiModel.getTbl_indian_league_id())) {
                        if (selected_Indi_League.contains(indiModel.getTbl_indian_league_id())) {
                            selected_Indi_League.remove(indiModel.getTbl_indian_league_id());
                        } else {
                            selected_Indi_League.add(indiModel.getTbl_indian_league_id());
                        }
                        mIndiCompanyTrade.selectedItemsCount(selected_Indi_League.size());
                        mIndiCompanyTrade.setDataOfSelectedList(indiModel.getSymbol_to_display(), indiModel.getOpen_price());
                    }
                    Toast.makeText(mContext, "You can select Only 11 Stock", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLeagueList.size();
    }

    public class CompanyHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_IndiLeague_select_trade;
        ImageView iv_IndiLeague_CheckBoxUncheck, iv_IndiLeague_CheckBoxCheck;
        CircleImageView iv_IndiLeague_icon;
        TextView tv_price_iv_IndiLeague_difference, tv_iv_IndiLeague_company_name, tv_iv_IndiLeague_current_price;

        public CompanyHolder(@NonNull View itemView) {
            super(itemView);
            rl_IndiLeague_select_trade = itemView.findViewById(R.id.rl_IndiLeague_select_trade);
            iv_IndiLeague_CheckBoxUncheck = itemView.findViewById(R.id.iv_IndiLeague_CheckBoxUncheck);
            iv_IndiLeague_CheckBoxCheck = itemView.findViewById(R.id.iv_IndiLeague_CheckBoxCheck);
            iv_IndiLeague_icon = itemView.findViewById(R.id.iv_IndiLeague_icon);
            tv_price_iv_IndiLeague_difference = itemView.findViewById(R.id.tv_price_iv_IndiLeague_difference);
            tv_iv_IndiLeague_company_name = itemView.findViewById(R.id.tv_iv_IndiLeague_company_name);
            tv_iv_IndiLeague_current_price = itemView.findViewById(R.id.tv_iv_IndiLeague_current_price);
        }
    }
}
