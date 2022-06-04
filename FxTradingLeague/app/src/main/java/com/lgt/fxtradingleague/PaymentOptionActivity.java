package com.lgt.fxtradingleague;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.TrakNPayPackage.TrakNPayActivity;

import org.json.JSONObject;

import static com.lgt.fxtradingleague.APICallingPackage.Class.Validations.ShowToast;

public class PaymentOptionActivity extends AppCompatActivity implements ResponseManager {


    PaymentOptionActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;

    String IntentFinalAmount,IntentFinalCoin;

    TextView tv_PaymentFinalAmount;
    RelativeLayout RL_TrakNPayPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        context = activity = this;
        initViews();
        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);

        IntentFinalAmount = getIntent().getStringExtra("FinalAmount");
        IntentFinalCoin = getIntent().getStringExtra("FinalCoin");
        // IntentFinalAmount = "50";
        tv_PaymentFinalAmount.setText("₹ " + IntentFinalAmount);

        RL_TrakNPayPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IntentFinalAmount.equals("") && IntentFinalAmount == null) {
                    ShowToast(context, "Please Select Correct Amount");
                } else {
                    Intent i = new Intent(activity, TrakNPayActivity.class);
                    i.putExtra("FinalAmount", IntentFinalAmount);
                    i.putExtra("FinalCoin", IntentFinalCoin);
                    startActivity(i);
                }

            }
        });


    }

    public void initViews() {

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_HeaderName.setText("PAYMENT OPTION");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        RL_TrakNPayPayment = findViewById(R.id.RL_TrakNPayPayment);
        tv_PaymentFinalAmount = findViewById(R.id.tv_PaymentFinalAmount);

    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {

    }

}
