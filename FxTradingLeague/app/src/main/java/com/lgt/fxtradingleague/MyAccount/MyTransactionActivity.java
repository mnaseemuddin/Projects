package com.lgt.fxtradingleague.MyAccount;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.Bean.BeanMyTransList;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.lgt.fxtradingleague.Config.MYTRANSACTIONLIST;
import static com.lgt.fxtradingleague.Constants.MYTRANSACTIONTYPE;

public class MyTransactionActivity extends AppCompatActivity implements ResponseManager {

    RecyclerView RV_MyTransactions;
    MyTransactionActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;
    AdapterTransactionList adapterTransactionList;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView tv_NoDataAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transaction);

        context = activity = this;
        initViews();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        sessionManager = new SessionManager();

        RV_MyTransactions.setHasFixedSize(true);
        RV_MyTransactions.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        RV_MyTransactions.setLayoutManager(mLayoutManager);
        RV_MyTransactions.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        callTransactionList(false);
                                    }
                                }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callTransactionList(false);
            }
        });

    }

    public void initViews(){
        RV_MyTransactions =  findViewById(R.id.RV_MyTransactions);

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_NoDataAvailable = findViewById(R.id.tv_NoDataAvailable);

        tv_HeaderName.setText("RECENT TRANSACTIONS");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

    }

    private void callTransactionList(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(MYTRANSACTIONLIST,
                    createRequestJson(), context, activity, MYTRANSACTIONTYPE,
                    isShowLoader,responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        tv_NoDataAvailable.setVisibility(View.GONE);
        RV_MyTransactions.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        Log.e("ressulltttranstype",result+"");

        try {
            JSONArray jsonArray = result.getJSONArray("data");
            List<BeanMyTransList> beanContestLists = new Gson().fromJson(jsonArray.toString(),
                    new TypeToken<List<BeanMyTransList>>() {
                    }.getType());
            adapterTransactionList = new AdapterTransactionList(beanContestLists, activity);
            RV_MyTransactions.setAdapter(adapterTransactionList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapterTransactionList.notifyDataSetChanged();


    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {

        //ShowToast(context,message);
        tv_NoDataAvailable.setVisibility(View.VISIBLE);
        RV_MyTransactions.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }


    public class AdapterTransactionList extends RecyclerView.Adapter<AdapterTransactionList.MyViewHolder> {
        private List<BeanMyTransList> mListenerList;
        Context mContext;

        public AdapterTransactionList(List<BeanMyTransList> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_TransactionAmount,tv_TransactionInfo,tv_TransactionStatus;

            public MyViewHolder(View view) {
                super(view);

                tv_TransactionAmount = view.findViewById(R.id.tv_TransactionAmount);
                tv_TransactionInfo = view.findViewById(R.id.tv_TransactionInfo);
                tv_TransactionStatus = view.findViewById(R.id.tv_TransactionStatus);

            }

        }
        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_my_transaction_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final String TransAmount = mListenerList.get(position).getAmount();
            String TransType= mListenerList.get(position).getType();
            String TransStatus= mListenerList.get(position).getTransaction_status();
            String TransNote= mListenerList.get(position).getTransection_mode();

            if (TransType.equals("credit")){
                holder.tv_TransactionAmount.setText("+ ₹ "+TransAmount);
            }
            else if (TransType.equals("debit")){
                holder.tv_TransactionAmount.setText("- ₹ "+TransAmount);
            }
            else if (TransType.equals("bonus_debit")){
                holder.tv_TransactionAmount.setText("- ₹ "+TransAmount);
            }
            else if (TransType.equals("bonus")){
                holder.tv_TransactionAmount.setText("+ ₹ "+TransAmount);
            }
            else if (TransType.equals("winning")){
                holder.tv_TransactionAmount.setText("+ ₹ "+TransAmount);
            }
            else if (TransType.equals("winning_debit")){
                holder.tv_TransactionAmount.setText("- ₹ "+TransAmount);
            }
            if (TransNote==null){
                holder.tv_TransactionInfo.setText(TransType);
            }
            else {
                holder.tv_TransactionInfo.setText(TransNote);
            }
            if (TransStatus.equalsIgnoreCase("SUCCESS")||TransStatus.equalsIgnoreCase("TXN_SUCCESS")){
                Drawable img = getApplicationContext().getResources().getDrawable( R.drawable.suctick );

                Bitmap bitmap=((BitmapDrawable)img).getBitmap();
                Drawable drawable=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(bitmap,26,26,true));

                holder.tv_TransactionStatus.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
            }

            holder.tv_TransactionStatus.setText("Status -"+TransStatus);


        }

    }

}
