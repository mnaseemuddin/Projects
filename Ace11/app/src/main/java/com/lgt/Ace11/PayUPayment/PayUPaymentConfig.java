package com.lgt.Ace11.PayUPayment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.HomeActivity;
import com.lgt.Ace11.R;
import com.lgt.Ace11.SessionManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lgt.Ace11.Config.ADDAMOUNT;
import static com.lgt.Ace11.Constants.ADDAMOUNTTYPE;

public class PayUPaymentConfig extends AppCompatActivity implements ResponseManager {

    private boolean isDisableExitConfirmation = false;
    String amount, txnId, phone, productName, firstName, email, udf1, udf2, udf3, udf4, udf5, udf6, udf7, udf8, udf9, udf10, enter_merchant_key, enter_merchant_ID, surl, furl, PayAmount, PayCoin, serverCalculatedHash, generate_header;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    EditText et_CheckName, et_CheckEmail;
    TextView tv_HeaderName, tv_totalamount, tv_Checkout;
    SessionManager sessionManager;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    private String TrakNPayResponse="";
    private String PaymentStatus = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_npay);
        initViews();
        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(this);
        Intent intent = getIntent();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.d("total_amt", " Status " + intent.hasExtra("FinalAmount"));
        if (intent.hasExtra("FinalAmount")) {
            PayAmount = intent.getStringExtra("FinalAmount");
            PayCoin = intent.getStringExtra("FinalCoin");
            Log.d("total_amt", PayAmount + " | " + sessionManager.getUser(this).getEmail() + " " + sessionManager.getUser(this).getMobile());
        } else {
            Log.d("total_amt", " No Data Found ");
        }
        if (sessionManager.getUser(this) != null) {
            tv_totalamount.setText(PayAmount);
            et_CheckName.setText(sessionManager.getUser(this).getName());
            et_CheckEmail.setText(sessionManager.getUser(this).getEmail());
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
        launchPayUMoneyFlow();
    }

    private void initViews() {
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_totalamount = findViewById(R.id.tv_totalamount);
        tv_Checkout = findViewById(R.id.tv_Checkout);
        et_CheckEmail = findViewById(R.id.et_CheckEmail);
        et_CheckName = findViewById(R.id.et_CheckName);
        tv_HeaderName.setText("PAYMENT OPTION");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tv_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
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

    // not in use
    public void setUpPayment() throws Exception {
        // create hash
        String hashSequence = enter_merchant_key + "|" + txnId + "|" + PayAmount + "|" + productName + "|" + firstName + "|" + email + "|||||||||||" + generate_header;
        serverCalculatedHash = hashCal(hashSequence);
        Log.d("generatedHash", "Hash: " + serverCalculatedHash);
        Log.d("PaymentCall", "Payment Click , " + hashSequence);
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setAmount(PayAmount)                                // Payment amount
                .setTxnId(txnId)                                    // Transaction ID
                .setPhone(phone)                                    // User Phone number
                .setProductName(productName)                        // Product Name or description
                .setFirstName(firstName)                            // User First name
                .setEmail(email)                                    // User Email ID
                .setsUrl(surl)                                      // Success URL (surl)
                .setfUrl(furl)                                      //Failure URL (furl)
                .setIsDebug(true)                                   // Integration environment - true (Debug)/ false(Production)
                .setKey(enter_merchant_key)                         // Merchant key
                .setMerchantId(enter_merchant_ID);                  // Merchant ID
        // declare paymentParam object
        PayUmoneySdkInitializer.PaymentParam paymentParam = builder.build();
        // set the hash
        paymentParam.setMerchantHash(serverCalculatedHash);
        // Invoke the following function to open the checkout page.
        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam,
                this, R.style.AppTheme_default, true);
    }


    /**
     * Thus function calculates the hash for transaction
     *
     * @param paymentParam payment params of transaction
     * @return payment params along with calculated merchant hash
     */
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

    private void launchPayUMoneyFlow() {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(PayAmount);

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

            /*
             * Hash should always be generated from your server side.
             * */
            /**   generateHashFromServer(mPaymentParams);
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayUPaymentConfig.this, R.style.AppTheme_default, false);
        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

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
                                .setMessage("Congratulations ! Your premium wallet received INR "+PayAmount)
                                .setPositiveButton("Go To Home", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                        startActivity(new Intent(PayUPaymentConfig.this, HomeActivity.class));
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
                                    startActivity(new Intent(PayUPaymentConfig.this, HomeActivity.class));
                                    finish();
                                }
                            }).show();
                }
            }


        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(this).getUser_id());
            jsonObject.put("amount", PayAmount);
            jsonObject.put("mode", "PayUMoney");
            jsonObject.put("rupees", PayAmount);
            jsonObject.put("status", PaymentStatus);
            jsonObject.put("transection_detail", ""+resultObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        if (type.equals(ADDAMOUNTTYPE)) {
            try {
                String Status = result.getString("transaction_status");
                Log.d("StatusPay","PaymentResp: "+new Gson().toJson(result));
                /* FinalMessage = "Status - "+Status+"\nTransactionId - "+FTransactionId+
                    "\nPayment Id - "+FPaymentId
                    +"\nAmount - "+FAmount; 8604111232*/
                // callMyAccountDetails(true);
                // Dialog(FTransactionStatus, FTransactionId, orderID, FAmount);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        Log.d("PayResponse", "Error: ," + type + ", " + message);
    }


    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }


    @Override
    protected void onPostResume() {
        if (sessionManager.getUser(this) != null) {
            tv_totalamount.setText(PayAmount);
            et_CheckName.setText(sessionManager.getUser(this).getName());
            et_CheckEmail.setText(sessionManager.getUser(this).getEmail());
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
