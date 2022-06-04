package com.lgt.Ace11;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.PayUPayment.AppEnvironment;
import com.lgt.Ace11.PayUPayment.BaseApplication;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.lgt.Ace11.Config.ADDAMOUNT;
import static com.lgt.Ace11.Constants.ADDAMOUNTTYPE;

public class PaymentOptionActivity extends AppCompatActivity implements ResponseManager {

    private boolean isDisableExitConfirmation = false;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    PaymentOptionActivity activity;
    Context context;
    ProgressBar showprogress;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;
    private String PaymentStatus = "";
private int FINALAMOUNT;
    String IntentFinalAmount = "", IntentFinalCoin = "",PayAmount,phone,productName,firstName,email,surl,furl,txnId,enter_merchant_key,enter_merchant_ID,generate_header;

    EditText et_PaymentFinalAmount;
    RelativeLayout RL_TrakNPayPayment;


    private JSONObject resultObject = new JSONObject();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JSONObject jsonObject, result = new JSONObject();

        // Result Code is -1 send from Payumoney activity
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {

                    String payuResponse = transactionResponse.getPayuResponse();
                    try {
                        Log.d("Paymeny", payuResponse);
                        jsonObject = new JSONObject(payuResponse);
                        result = jsonObject.getJSONObject("result");
                        resultObject = result;
                        PaymentStatus = "SUCCESS";
                        Log.d("Paymeny", ""+resultObject.toString());
                        callAddAmount(true);
                        /*transactionSuccess(result.getString("txnid"), result.getString("amount"),
                                result.getString("status"), result.getString("hash"), result.getString("firstname"),
                                result.getString("key"), result.getString("productinfo"), result.getString("email"));*/
                        // Toast.makeText(getApplicationContext(),result.getString("status")+result.getString("field9")+result.getString("bankcode"),Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("Paymeny", "payuResponse"+new Gson().toJson(e));
                    }

                    // Response from SURl and FURL
                    String merchantResponse = transactionResponse.getTransactionDetails();

                    try {
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setTitle("Transaction Status")
                                .setMessage("Congratulations ! Your premium wallet received INR "+FINALAMOUNT)
                                .setPositiveButton("Go To Home", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                        startActivity(new Intent(PaymentOptionActivity


                                                .this, HomeActivity.class));
                                        finish();
                                    }
                                }).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Success Transaction
                } else {
                    PaymentStatus = "FAILED";
                    callAddAmount(true);
                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setTitle("Transaction Status")
                            .setMessage("Transaction Failed")
                            .setPositiveButton("Go To Home", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    startActivity(new Intent(PaymentOptionActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }).show();
                }
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);

        context = activity = this;
        initViews();

        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        if (sessionManager.getUser(this) != null) {

            firstName = sessionManager.getUser(this).getName();
            email = sessionManager.getUser(this).getEmail();
            phone = sessionManager.getUser(this).getMobile();

            productName = "GamePayment";
            surl = "https://google.com";
            furl = "https://google.com";
            txnId = System.currentTimeMillis() + "";
            enter_merchant_key = "UroZi1Vw";
            enter_merchant_ID = "7344937";
            generate_header = "Rl7ptod6dUhnJAyNLgEZCdhE9d6DOJLxYJQ4xbw1Ipc=";
        }
        if (getIntent() != null) {
            if (!getIntent().hasExtra("FinalAmount")) {

            }
        }

        RL_TrakNPayPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFinalAmount = et_PaymentFinalAmount.getText().toString().trim();
                Log.e("IntentFinalAmount",IntentFinalAmount+"");



                if (TextUtils.isEmpty(IntentFinalAmount)){
                    et_PaymentFinalAmount.setError("Enter Amount");
                    et_PaymentFinalAmount.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    Intent intent=new Intent(getApplicationContext(),EditProfileActivity.class);
                    startActivity(intent);

                }else {

                }
                FINALAMOUNT=Integer.parseInt(IntentFinalAmount);

                if (FINALAMOUNT >9){

                   // Toast.makeText(getApplicationContext(),"Minimum Amount 5",Toast.LENGTH_LONG);
                    launchPayUMoneyFlow(FINALAMOUNT);

                   /* Intent i = new Intent(activity, PayUPaymentConfig.class);
                    i.putExtra("FinalAmount", IntentFinalAmount);
                    i.putExtra("FinalCoin", IntentFinalCoin);
                    Log.d("WeHave", IntentFinalAmount + "  Amt  " + IntentFinalCoin);
                    startActivity(i);*/
                }else{
                    Toast.makeText(getApplicationContext(),"Minimum Amount 10",Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    private void callAddAmount(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(ADDAMOUNT,
                    createRequestJson(), this, this, ADDAMOUNTTYPE,
                    isShowLoader, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(this).getUser_id());
            jsonObject.put("amount", FINALAMOUNT);
            jsonObject.put("mode", "PayUMoney");
            jsonObject.put("rupees", FINALAMOUNT);
            jsonObject.put("status", PaymentStatus);
            jsonObject.put("transection_detail", ""+resultObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public void initViews() {
        showprogress=findViewById(R.id.showprogress);
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
        et_PaymentFinalAmount = findViewById(R.id.et_PaymentFinalAmount);

    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }



    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
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
    private void launchPayUMoneyFlow(int amt) {
        showprogress.setVisibility(View.VISIBLE);
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(String.valueOf(amt));

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        String phone = this.phone;
        String productName = this.productName;
        String firstName = this.firstName;
        String email = this.email;
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        // setProgressDialog();
        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();
            showprogress.setVisibility(View.GONE);
            /*
             * Hash should always be generated from your server side.
             * */
            /**   generateHashFromServer(mPaymentParams);
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentOptionActivity.this, R.style.AppTheme_default, false);
        } catch (Exception e) {
            // some exception occurred
            showprogress.setVisibility(View.GONE);

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

            Log.e("fkjojo",e.getMessage()+"");

        }
    }

    @Override
    protected void onPostResume() {
        if (sessionManager.getUser(this) != null) {
            firstName = sessionManager.getUser(this).getName();
            email = sessionManager.getUser(this).getEmail();
            phone = sessionManager.getUser(this).getMobile();
            productName = "GamePayment";
            surl = "https://google.com";
            furl = "https://google.com";
            txnId = System.currentTimeMillis() + "";
            enter_merchant_key = "UroZi1Vw";
            enter_merchant_ID = "7344937";
            generate_header = "Rl7ptod6dUhnJAyNLgEZCdhE9d6DOJLxYJQ4xbw1Ipc=";
        }
        super.onPostResume();
    }
}
