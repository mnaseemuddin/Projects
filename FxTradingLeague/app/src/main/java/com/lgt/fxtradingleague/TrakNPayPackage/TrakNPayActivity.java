package com.lgt.fxtradingleague.TrakNPayPackage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lgt.fxtradingleague.APICallingPackage.Class.APIRequestManager;
import com.lgt.fxtradingleague.APICallingPackage.Interface.ResponseManager;
import com.lgt.fxtradingleague.AddCashActivity;
import com.lgt.fxtradingleague.ContestListActivity;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.JoinContestActivity;
import com.lgt.fxtradingleague.PaymentConfirmationActivity;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;

import static com.lgt.fxtradingleague.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.fxtradingleague.Config.ADDAMOUNT;
import static com.lgt.fxtradingleague.Config.JOINCONTEST;
import static com.lgt.fxtradingleague.Config.MYACCOUNT;
import static com.lgt.fxtradingleague.Constants.ADDAMOUNTTYPE;
import static com.lgt.fxtradingleague.Constants.JOINCONTESTTYPE;
import static com.lgt.fxtradingleague.Constants.MYACCOUNTTYPE;

public class TrakNPayActivity extends AppCompatActivity implements ResponseManager {

    TrakNPayActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;

    EditText et_CheckName, et_CheckEmail, et_CheckNumber, et_CheckCity, et_CheckState, et_CheckZipCode;
    TextView tv_totalamount, tv_Checkout;

    private String orderID = "";
    private String customerID = "";
    private String PayAmount = "0.0";
    private String PayCoin = "0.0";
    double TotalAmount, Deposited, Winnings, Bonus, FinaltoPayAmount;
    String TName, TEmail, TNumber, TCity, TState, TZipCode;

    String FPaymentId, FTransactionId, FPaymentMode, FTransactionStatus, FAmount, FUserName,
            FEmail, FPhone, FMessage;
    JSONObject TrakNPayResponse;
    String FinalToPayAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_npay);
        context = activity = this;
        initViews();
        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        Intent intent = getIntent();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.d("total_amt", " Status " + intent.hasExtra("FinalAmount"));
        if (intent.hasExtra("FinalAmount")) {
            PayAmount = intent.getStringExtra("FinalAmount");
            PayCoin = intent.getStringExtra("FinalCoin");
            Log.d("total_amt", PayAmount + " | " + PayCoin + " " + sessionManager.getUser(context).getMobile());
        } else {
            Log.d("total_amt", " No Data Found ");
        }

        customerID = sessionManager.getUser(context).getUser_id();
        orderID = "OrderID" + System.currentTimeMillis() + "-" + customerID + "-" + PayAmount;
        if (sessionManager.getUser(context).getCity() != null) {
            if (sessionManager.getUser(context).getCity().equalsIgnoreCase(" ")) {
                et_CheckCity.setText("Delhi");
            } else {
                et_CheckCity.setText(sessionManager.getUser(context).getCity());
            }
        }
        if (sessionManager.getUser(context).getPincode() != null) {
            if (sessionManager.getUser(context).getPincode().equalsIgnoreCase(" ")) {
                et_CheckZipCode.setText("110001");
            } else {
                et_CheckZipCode.setText(sessionManager.getUser(context).getPincode());
            }
        }
        if (sessionManager.getUser(context).getState() != null) {
            if (sessionManager.getUser(context).getState().equalsIgnoreCase(" ")) {
                et_CheckState.setText("Delhi");
            } else {
                et_CheckState.setText(sessionManager.getUser(context).getState());
            }
        }
        if (sessionManager.getUser(context).getName() != null) {
            if (sessionManager.getUser(context).getName().equalsIgnoreCase("null")) {
                et_CheckName.setText(sessionManager.getUser(context).getReferral_code());
            } else {
                et_CheckName.setText(sessionManager.getUser(context).getName() + "");
            }

        }
        if (sessionManager.getUser(context).getMobile() != null) {
            if (sessionManager.getUser(context).getMobile().equalsIgnoreCase("null")) {
                et_CheckNumber.setText("");
            } else {
                et_CheckNumber.setText(sessionManager.getUser(context).getMobile() + "");
                TNumber=sessionManager.getUser(context).getMobile();
            }
        }

        if (sessionManager.getUser(context).getEmail().equalsIgnoreCase("null")) {
            et_CheckEmail.setText("");
        } else {
            et_CheckEmail.setText(sessionManager.getUser(context).getEmail() + "");
        }

        //et_CheckNumber.setText(sessionManager.getUser(context).getMobile()+"");
        tv_totalamount.setText("₹ " + getIntent().getStringExtra("FinalAmount"));


        tv_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TName = et_CheckName.getText().toString().trim();
                TEmail = et_CheckEmail.getText().toString().trim();
                // TNumber = et_CheckNumber.getText().toString().trim();
                TCity = et_CheckCity.getText().toString().trim();
                TState = et_CheckState.getText().toString().trim();
                TZipCode = et_CheckZipCode.getText().toString().trim();
                /*if (TName.equals("")) {
                    ShowToast(context, "Enter Name");
                } else if (TEmail.equals("")) {
                    ShowToast(context, "Enter Email");
                } else if (!Validations.isValidEmail(TEmail)) {
                    ShowToast(context, "Enter Valid Email Id");
                } else if (TNumber.equals("")) {
                    ShowToast(context, "Enter Number");
                } else if (!TNumber.matches(Validations.MobilePattern)) {
                    ShowToast(context, "Enter Valid Mobile Number");
                } else if (TCity.equals("")) {
                    ShowToast(context, "Enter City");
                } else if (TState.equals("")) {
                    ShowToast(context, "Enter State");
                } else if (TZipCode.equals("")) {
                    ShowToast(context, "Enter Zipcode");
                } else {

                }*/
                initializePayment();
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

        et_CheckName = findViewById(R.id.et_CheckName);
        et_CheckEmail = findViewById(R.id.et_CheckEmail);
        et_CheckNumber = findViewById(R.id.et_CheckNumber);
        et_CheckCity = findViewById(R.id.et_CheckCity);
        et_CheckState = findViewById(R.id.et_CheckState);
        et_CheckZipCode = findViewById(R.id.et_CheckZipCode);
        tv_totalamount = findViewById(R.id.tv_totalamount);
        tv_Checkout = findViewById(R.id.tv_Checkout);
    }

    public void initializePayment() {
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        TrakConstant.PG_ORDER_ID = Integer.toString(n);
        PaymentParams pgPaymentParams = new PaymentParams();
        pgPaymentParams.setAPiKey(TrakConstant.PG_API_KEY);
        Log.d("final_pay", orderID + " | " + PayAmount + " | " + TNumber + " | " + TName + " | " + TEmail);
        pgPaymentParams.setAmount(PayAmount);
        pgPaymentParams.setEmail(TEmail);
        pgPaymentParams.setName(TName);
        pgPaymentParams.setPhone(TNumber);
        pgPaymentParams.setOrderId(orderID);
        pgPaymentParams.setCurrency(TrakConstant.PG_CURRENCY);
        pgPaymentParams.setDescription(TrakConstant.PG_DESCRIPTION);
        pgPaymentParams.setCity("city");
        pgPaymentParams.setState("state");
        pgPaymentParams.setAddressLine1(TrakConstant.PG_ADD_1);
        pgPaymentParams.setAddressLine2(TrakConstant.PG_ADD_2);
        pgPaymentParams.setZipCode("zip code");
        pgPaymentParams.setCountry(TrakConstant.PG_COUNTRY);
        pgPaymentParams.setReturnUrl(TrakConstant.PG_RETURN_URL);
        pgPaymentParams.setMode(TrakConstant.PG_MODE);
        pgPaymentParams.setUdf1(TrakConstant.PG_UDF1);
        pgPaymentParams.setUdf2(TrakConstant.PG_UDF2);
        pgPaymentParams.setUdf3(TrakConstant.PG_UDF3);
        pgPaymentParams.setUdf4(TrakConstant.PG_UDF4);
        pgPaymentParams.setUdf5(TrakConstant.PG_UDF5);

        PaymentGatewayPaymentInitializer pgPaymentInitialzer = new
                PaymentGatewayPaymentInitializer(pgPaymentParams, activity);
        pgPaymentInitialzer.initiatePaymentProcess();
    }

    private void callMyAccountDetails(boolean isShowLoader) {

        try {
            apiRequestManager.callAPI(MYACCOUNT,
                    createRequestJsonWin(), context, activity, MYACCOUNTTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    JSONObject createRequestJsonWin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", HomeActivity.sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void callAddAmount(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(ADDAMOUNT,
                    createRequestJson(), context, activity, ADDAMOUNTTYPE,
                    isShowLoader, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            jsonObject.put("amount", PayCoin);
            jsonObject.put("mode", "TrakNPay");
            jsonObject.put("rupees", PayAmount);
            jsonObject.put("transection_detail", TrakNPayResponse);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void callJoinContest(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(JOINCONTEST,
                    createRequestJsonJoin(), context, activity, JOINCONTESTTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJsonJoin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            jsonObject.put("match_id", ContestListActivity.IntentMatchId);
            jsonObject.put("my_team_id", ContestListActivity.JoinMyTeamId);
            jsonObject.put("contest_id", ContestListActivity.ContestId);
            jsonObject.put("contest_amount", FinalToPayAmount + "");
            jsonObject.put("entryFee", JoinContestActivity.EntryFee);
            //if contest is private then value is 1 else 0
            if (JoinContestActivity.MyContestCode.equals("")) {
                jsonObject.put("private", "0");
            } else {
                jsonObject.put("private", "1");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        if (type.equals(ADDAMOUNTTYPE)) {
            try {
                String Status = result.getString("transaction_status");
           /* FinalMessage = "Status - "+Status+"\nTransactionId - "+FTransactionId+
                    "\nPayment Id - "+FPaymentId
                    +"\nAmount - "+FAmount;*/
                callMyAccountDetails(true);
                Dialog(FTransactionStatus, FTransactionId, orderID, FAmount);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (type.equals(MYACCOUNTTYPE)) {

                try {
                    TotalAmount = Double.parseDouble((result.getString("total_amount")));
                    Deposited = Double.valueOf(result.getString("credit_amount"));
                    Winnings = Double.valueOf(result.getString("winning_amount"));
                    Bonus = Double.valueOf(result.getString("bonous_amount"));
                    if (Double.parseDouble(JoinContestActivity.EntryFee) == 0) {
                        FinaltoPayAmount = Double.parseDouble(JoinContestActivity.EntryFee);
                        DecimalFormat df = new DecimalFormat("#.##");
                        FinalToPayAmount = df.format(FinaltoPayAmount);
                    } else if (Double.parseDouble(JoinContestActivity.EntryFee) >= Bonus) {
                        FinaltoPayAmount = Double.parseDouble(JoinContestActivity.EntryFee) - Bonus;
                        DecimalFormat df = new DecimalFormat("#.##");
                        FinalToPayAmount = df.format(FinaltoPayAmount);

                    } else {
                        FinaltoPayAmount = Double.parseDouble(JoinContestActivity.EntryFee) - Bonus;
                        FinaltoPayAmount = 0.0;
                        FinalToPayAmount = String.valueOf(FinaltoPayAmount);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (type.equals(JOINCONTESTTYPE)) {
                if (JoinContestActivity.MyContestCode.equals("")) {
                    //ShowToast(context, message);
                    LayoutInflater li = getLayoutInflater();
                    //Getting the View object as defined in the customtoast.xml file
                    View layout = li.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
                    TextView textView = (TextView) layout.findViewById(R.id.custom_toast_message);
                    //Creating the Toast object
                    textView.setText(message);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);//setting the view of custom toast layout
                    toast.show();
                    Intent i = new Intent(activity, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }


    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        if (type.equals(ADDAMOUNTTYPE)) {
            PaymentFailedDialog(FTransactionStatus);
        } else {
            if (type.equals(JOINCONTESTTYPE)) {
                PaymentFailedDialog((message));
            } else {

                PaymentFailedDialog((message));
            }
        }

    }

    public void Dialog(String Status, String TxId, String OrderId, String TxAmount) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dialog);

        final TextView tv_DStatus = dialog.findViewById(R.id.tv_DStatus);
        final TextView tv_DTransactionId = dialog.findViewById(R.id.tv_DTransactionId);
        final TextView tv_DOrderId = dialog.findViewById(R.id.tv_DOrderId);
        final TextView tv_DTxAmount = dialog.findViewById(R.id.tv_DTxAmount);
        final TextView tv_TxDone = dialog.findViewById(R.id.tv_TxDone);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        tv_DStatus.setText(Status);
        tv_DTransactionId.setText(TxId);
        tv_DOrderId.setText(OrderId);
        tv_DTxAmount.setText("₹ " + TxAmount);


        tv_TxDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                if (TextUtils.isEmpty(AddCashActivity.Activity) && AddCashActivity.Activity.equals("")) {
                    Intent i = new Intent(activity, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {

                    if (TotalAmount >= FinaltoPayAmount) {
                        callJoinContest(true);
                    } else {
                        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                        ab.setMessage("Not enough balance in your account to join contest.");

                        ab.setPositiveButton("Add Amount", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(activity, AddCashActivity.class);
                                i.putExtra("Activity", "PaymentConfirmationActivity");
                                i.putExtra("EntryFee", JoinContestActivity.EntryFee);
                                startActivity(i);
                            }
                        });
                        ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = ab.create();
                        alert.show();
                    }
                }

            }

        });
    }

    public void PaymentFailedDialog(String message) {

        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setCancelable(false);
        ab.setMessage(message);

        ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (AddCashActivity.Activity.equals("")) {
                    Intent i = new Intent(activity, HomeActivity.class);
                    startActivity(i);
                    finish();

                } else {

                    Intent i = new Intent(activity, PaymentConfirmationActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        AlertDialog alert = ab.create();
        alert.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PGConstants.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String paymentResponse = data.getStringExtra(PGConstants.PAYMENT_RESPONSE);
                    System.out.println("paymentResponse: " + paymentResponse);
                    if (paymentResponse.equals("null")) {
                        System.out.println("Transaction Error!");
                        ShowToast(context, "Transaction Failed");
                        //transactionIdView.setText("Transaction ID: NIL");
                        //transactionStatusView.setText("Transaction Status: Transaction Error!");
                    } else {
                        JSONObject response = new JSONObject(paymentResponse);
                        TrakNPayResponse = response;
                        FPaymentId = response.getString("transaction_id");
                        FTransactionId = response.getString("transaction_id");
                        FPaymentMode = response.getString("payment_mode");
                        FTransactionStatus = response.getString("response_message");
                        FAmount = response.getString("amount");
                        FMessage = response.getString("response_message");
                        Log.e("resssppoo", FTransactionStatus);

                        Log.e("TrakNPay", "onActivityResult: " + response);
                        System.out.print(AddCashActivity.Activity);
                        callAddAmount(true);

                        //if(AddCashActivity.Activity.equals("PaymentConfirmationActivity")){
                        //  callMyAccountDetails(true);}

                        //transactionIdView.setText("Transaction ID: "+response.getString("transaction_id"));
                        // transactionStatusView.setText("Transaction Status: "+response.getString("response_message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                ShowToast(context, "Transaction Canceled");
            }

        }
    }
}
