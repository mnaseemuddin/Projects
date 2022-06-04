package com.lgt.fxtradingleague.IndiWorldLeague;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lgt.fxtradingleague.Extra.IndimBuyOrSaleClick;
import com.lgt.fxtradingleague.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class IndiAdapterBuyOrSell extends RecyclerView.Adapter<IndiAdapterBuyOrSell.IndiHolder> {
    String BuyID,SaleID;
    IndimBuyOrSaleClick mIndimBuyOrSaleClick;
    Context mContext;
    ArrayList<IndiBuySellModel> mDataList;

    public IndiAdapterBuyOrSell(Context mContext, ArrayList<IndiBuySellModel> mDataList,IndimBuyOrSaleClick mIndimBuyOrSale) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.mIndimBuyOrSaleClick = mIndimBuyOrSale;
    }

    @NonNull
    @Override
    public IndiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.buy_or_sell_items, parent, false);
        return new IndiHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final IndiHolder holder, int position) {
        final IndiBuySellModel data = mDataList.get(position);

        Glide.with(mContext).load(data.getImages()).into(holder.cv_IndiCompanyIcon);

        holder.tv_PlayerIndiName.setText(data.getCompany_name());
        holder.tv_PlayerTeamIndiName.setText(data.getCompany_symbol());

        if (data.getCurrency_value().equalsIgnoreCase("high")) {
            holder.iv_display_price_indian_status.setImageResource(R.drawable.ic_upward);
        } else {
            holder.iv_display_price_indian_status.setImageResource(R.drawable.ic_downward);
        }

        holder.checkboxIndi.setTag("B");
        holder.checkboxIndiTwo.setTag("S");

        holder.checkboxIndi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                String selectValue = cb.getTag().toString();
                if (holder.checkboxIndi.isChecked()) {
                    holder.checkboxIndiTwo.setChecked(false);
                }
                String type_add = "Buy";
                BuyID = data.getTbl_indianleage_team_player_id();
                mIndimBuyOrSaleClick.getSaleOrBuyData(BuyID, type_add);
            }
        });

        holder.checkboxIndiTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                String selectValue = cb.getTag().toString();
                if (holder.checkboxIndi.isChecked()) {
                    holder.checkboxIndi.setChecked(false);
                }
                String type_add = "Sale";
                SaleID = data.getTbl_indianleage_team_player_id();
                mIndimBuyOrSaleClick.getSaleOrBuyData(SaleID, type_add);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class IndiHolder extends RecyclerView.ViewHolder {
        CircleImageView cv_IndiCompanyIcon;
        TextView tv_PlayerIndiName, tv_PlayerTeamIndiName;
        ImageView iv_display_price_indian_status;
        CheckBox checkboxIndi, checkboxIndiTwo;

        public IndiHolder(@NonNull View itemView) {
            super(itemView);
            cv_IndiCompanyIcon = itemView.findViewById(R.id.cv_IndiCompanyIcon);
            tv_PlayerIndiName = itemView.findViewById(R.id.tv_PlayerIndiName);
            tv_PlayerTeamIndiName = itemView.findViewById(R.id.tv_PlayerTeamIndiName);
            iv_display_price_indian_status = itemView.findViewById(R.id.iv_display_price_indian_status);
            checkboxIndi = itemView.findViewById(R.id.checkboxIndi);
            checkboxIndiTwo = itemView.findViewById(R.id.checkboxIndiTwo);
        }
    }
}
