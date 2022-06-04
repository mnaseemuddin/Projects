package com.lgt.fxtradingleague.WorldLeague;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.Extra.WorldBuyOrSaleClick;
import com.lgt.fxtradingleague.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuyOrSaleAdapter extends RecyclerView.Adapter<BuyOrSaleAdapter.BuySaleHolder> {
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    WorldBuyOrSaleClick mBuyOrSaleClick;
    String mSymbolText = "";
    char mFirst = '#';
    ArrayList<BuyOrSaleModel> mData;
    Context mContext;
    String BuyID,SaleID;
    public BuyOrSaleAdapter(ArrayList<BuyOrSaleModel> mData, Context mContext,WorldBuyOrSaleClick mBuyOrSale) {
        this.mData = mData;
        this.mContext = mContext;
        this.mBuyOrSaleClick = mBuyOrSale;
    }

    @NonNull
    @Override
    public BuySaleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.world_item_buy_or_sale, parent, false);
        return new BuySaleHolder(mView);
    }

    @Override
    public void onBindViewHolder(final @NonNull BuySaleHolder holder, final int position) {
        final BuyOrSaleModel mModel = mData.get(position);
        mSymbolText = mModel.getCompany_symbol();
        // set image
        Glide.with(mContext).load(mModel.getImages()).into(holder.cv_CompanyIcon);
        getFirstLater(holder,mModel);
        holder.tv_PlayerTeamWorldName.setText(decimalFormat.format(Double.parseDouble(mModel.getShare_qnt())));
        holder.tv_PlayerWorldName.setText(mModel.getCompany_symbol());
        // set arrow high or low
        if (mModel.getCurrency_value().equalsIgnoreCase("high")) {
            holder.iv_display_price_world_status.setImageResource(R.drawable.ic_upward);
        } else {
            holder.iv_display_price_world_status.setImageResource(R.drawable.ic_downward);
        }
        // select buy or sale
        holder.checkboxWorld.setTag("B");
        holder.checkboxWorldTwo.setTag("S");
        // click listener
        holder.checkboxWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                String selectValue = cb.getTag().toString();
                if (holder.checkboxWorld.isChecked()) {
                    holder.checkboxWorldTwo.setChecked(false);
                }
                String type_add = "Buy";
                BuyID = mModel.getTbl_worldleage_team_player_id();
                mBuyOrSaleClick.getSaleOrBuyData(BuyID, type_add);
            }
        });

        holder.checkboxWorldTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                String selectValue = cb.getTag().toString();
                if (holder.checkboxWorld.isChecked()) {
                    holder.checkboxWorld.setChecked(false);
                }
                String type_add = "Sale";
                SaleID = mModel.getTbl_worldleage_team_player_id();
                mBuyOrSaleClick.getSaleOrBuyData(SaleID, type_add);
            }
        });
    }

    private void getFirstLater(BuySaleHolder holder, BuyOrSaleModel model) {
        if (model.getCompany_symbol()!=null&&
                !TextUtils.isEmpty(model.getCompany_symbol()) &&model.getCompany_symbol().length()>1){
            String getFirstltr=model.getCompany_symbol().substring(0,1);
            Validations.common_log("getFirstltr:"+getFirstltr+"");
            holder.tv_World_symbol.setText(getFirstltr);
        }else {
            holder.tv_World_symbol.setText("");
        }
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
    public int getItemCount() {
        return mData.size();
    }

    public class BuySaleHolder extends RecyclerView.ViewHolder {
        TextView tv_PlayerWorldName, tv_PlayerTeamWorldName, tv_World_symbol;
        ImageView iv_display_price_world_status;
        CheckBox checkboxWorld, checkboxWorldTwo;
        CircleImageView cv_CompanyIcon;
        public BuySaleHolder(@NonNull View itemView) {
            super(itemView);
            tv_PlayerWorldName = itemView.findViewById(R.id.tv_PlayerWorldName);
            checkboxWorld = itemView.findViewById(R.id.checkboxWorld);
            cv_CompanyIcon = itemView.findViewById(R.id.cv_CompanyIcon);
            tv_World_symbol = itemView.findViewById(R.id.tv_World_symbol);
            checkboxWorldTwo = itemView.findViewById(R.id.checkboxWorldTwo);
            tv_PlayerTeamWorldName = itemView.findViewById(R.id.tv_PlayerTeamWorldName);
            iv_display_price_world_status = itemView.findViewById(R.id.iv_display_price_world_status);
        }
    }
}
