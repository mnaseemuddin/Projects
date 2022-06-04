package com.lgt.fxtradingleague.TradingAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lgt.fxtradingleague.ClickInterFace.ClickInterFace;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.TradingModel.TradingListModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TradeListAdapter extends RecyclerView.Adapter<TradeListAdapter.TradeHolder> {
    private String oneday,towdays,oneweek,onemnth,openingrate,closingrate;
    ArrayList<TradingListModel> mTradeData;
    Context mContext;
    long mTargetPrice;
    ClickInterFace clickInterFace;
    int checkboxCount = 0, progressCount = 0;
    String CaseAdd = "";
    DecimalFormat df = new DecimalFormat("#####.###");
    Float MasterPrice = 0.0f;
    String mPriceCurrent = "";
    private Set<String> selected_trade = new HashSet<>();
    Drawable.ConstantState constantStateDrawableA;
    Drawable.ConstantState constantStateDrawableB;
    boolean isChecked = false;

    private int curren_selecttion = -1;

    public TradeListAdapter(ArrayList<TradingListModel> mTradeData, Context mContext, long targetPrice, ClickInterFace mClick) {
        this.mTradeData = mTradeData;
        this.mContext = mContext;
        this.mTargetPrice = targetPrice;
        this.clickInterFace = mClick;
        constantStateDrawableA = mContext.getResources().getDrawable(R.drawable.check).getConstantState();
        constantStateDrawableB = mContext.getResources().getDrawable(R.drawable.uncheck).getConstantState();
    }

    @NonNull
    @Override
    public TradeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.trader_list_items, parent, false);
        return new TradeHolder(mView);
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
    public void onBindViewHolder(@NonNull final TradeHolder holder, final int position) {
        final TradeHolder mTradeHolder = holder;
        final TradingListModel model = mTradeData.get(position);
        holder.tv_company_name.setText(model.getName());
        holder.tv_current_price.setText(model.getCurrency_rate());
        holder.tv_price_difference.setText(model.getSymbol());
        Glide.with(mContext).load(model.getCurrency_image()).into(holder.iv_coin_icon);

        // up or low
        if (mTradeData.get(position).getCurrency_value().equalsIgnoreCase("high")) {
            holder.tv_price_difference.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upward, 0, 0, 0);
        } else {
            holder.tv_price_difference.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_downward, 0, 0, 0);
        }

        if (selected_trade.contains(model.getCurrency_id())) {
            holder.iv_CheckBoxCheck.setVisibility(View.VISIBLE);
            holder.iv_CheckBoxUncheck.setVisibility(View.GONE);
        } else {
            holder.iv_CheckBoxCheck.setVisibility(View.GONE);
            holder.iv_CheckBoxUncheck.setVisibility(View.VISIBLE);
        }
        if (holder != null) {
            clickInterFace.itemCountList(selected_trade.size(), CaseAdd, holder);
        }

        holder.rl_select_trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_trade.size() < 11) {
                    if (selected_trade.contains(model.getCurrency_id())) {
                        selected_trade.remove(model.getCurrency_id());
                    } else {
                        selected_trade.add(model.getCurrency_id());
                    }
                    clickInterFace.itemCountList(selected_trade.size(), CaseAdd, holder);
                    clickInterFace.savedSelectTradingDetails(model.getName(),
                            model.getCurrency_rate(), df.format(MasterPrice));

                } else {
                    if (selected_trade.contains(mTradeData.get(position).getCurrency_id())) {
                        if (selected_trade.contains(model.getCurrency_id())) {
                            selected_trade.remove(model.getCurrency_id());
                        } else {
                            selected_trade.add(model.getCurrency_id());
                        }
                        clickInterFace.itemCountList(selected_trade.size(), CaseAdd, holder);
                        clickInterFace.savedSelectTradingDetails(model.getName(),
                                model.getCurrency_rate(), df.format(MasterPrice));
                    }
                    Toast.makeText(mContext, "You can select Only 11 Stock", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });

        if (model.getCurrency_rate() != null && !TextUtils.isEmpty(model.getCurrency_rate())){
            openingrate = model.getCurrency_rate();
        }else{
            openingrate= "0";
        }

        if (model.getClosing_rate() != null && !TextUtils.isEmpty(model.getClosing_rate())){
            closingrate = model.getClosing_rate();
        }else{
            closingrate= "0";
        }

        // not in use
        /*
        if (model.getPrice_change_percentage_24h() != null && !TextUtils.isEmpty(model.getPrice_change_percentage_24h())){
            oneday = model.getPrice_change_percentage_24h();
        }else{
            oneday= "0";
        }

        if (model.getPrice_change_percentage_7d() != null && !TextUtils.isEmpty(model.getPrice_change_percentage_7d())){
            towdays = model.getPrice_change_percentage_7d();
        }else{
            towdays= "0";
        }

        if ( model.getPrice_change_percentage_14d() != null && !TextUtils.isEmpty( model.getPrice_change_percentage_14d())){
            oneweek = model.getPrice_change_percentage_14d();
        }else{
            oneweek= "0";
        }

        if (model.getPrice_change_percentage_30d() != null && !TextUtils.isEmpty(model.getPrice_change_percentage_30d())){
            onemnth = model.getPrice_change_percentage_30d();
        }else{
            onemnth= "0";
        }*/

        holder.tv_company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickInterFace.displayCoinDetails(
                        model.getName(),
                        model.getSymbol(),
                        openingrate,
                        closingrate,
                        model.getPrice_change_percentage_24h(),
                        model.getPrice_change_percentage_7d(),
                        model.getPrice_change_percentage_14d(),
                        model.getPrice_change_percentage_30d());
            }
        });
    }

    public static void setClickable(View view, boolean clickable) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    setClickable(viewGroup.getChildAt(i), clickable);
                }
            }
            view.setClickable(clickable);
        }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return mTradeData.size();
    }


    public class TradeHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_checkBox_Container;
        private RelativeLayout rl_select_trade;
        public CheckBox cb_selectCompanyBox;
        public TextView tv_price_difference, tv_current_price, tv_company_name, tv_actual_price, tv_change_price;
        public ImageView iv_coin_icon, iv_CheckBoxCheck, iv_CheckBoxUncheck;

        public TradeHolder(@NonNull View itemView) {
            super(itemView);
            cb_selectCompanyBox = itemView.findViewById(R.id.cb_selectCompanyBox);
            tv_price_difference = itemView.findViewById(R.id.tv_price_difference);
            tv_current_price = itemView.findViewById(R.id.tv_current_price);
            tv_company_name = itemView.findViewById(R.id.tv_company_name);
            iv_coin_icon = itemView.findViewById(R.id.iv_coin_icon);
            tv_actual_price = itemView.findViewById(R.id.tv_actual_price);
            tv_change_price = itemView.findViewById(R.id.tv_change_price);
            iv_CheckBoxCheck = itemView.findViewById(R.id.iv_CheckBoxCheck);
            iv_CheckBoxUncheck = itemView.findViewById(R.id.iv_CheckBoxUncheck);
            ll_checkBox_Container = itemView.findViewById(R.id.ll_checkBox_Container);
            rl_select_trade = itemView.findViewById(R.id.rl_select_trade);
        }
    }

}
