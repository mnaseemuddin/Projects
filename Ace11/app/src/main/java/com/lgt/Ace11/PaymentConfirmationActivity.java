package com.lgt.Ace11;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.CreateContest.InviteInContestActivity;
import com.lgt.Ace11.TrakNPayPackage.TrakConstant;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.ADDAMOUNT;
import static com.lgt.Ace11.Config.JOINCONTEST;
import static com.lgt.Ace11.Config.MYACCOUNT;
import static com.lgt.Ace11.Constants.ADDAMOUNTTYPE;
import static com.lgt.Ace11.Constants.JOINCONTESTTYPE;
import static com.lgt.Ace11.Constants.MYACCOUNTTYPE;

public class PaymentConfirmationActivity extends AppCompatActivity implements ResponseManager {

    TextView tv_ConfirmationJoinContest, tv_ConfirmationToPay, tv_ConfirmationEntryFee,
            tv_WalletBalance, tv_ConfirmationBonus;
    PaymentConfirmationActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    TextView tv_HeaderName, tv_join_terms_condition;
    SessionManager sessionManager;
    String JoinMyTeamId;
    String MyContestCode;

    double TotalAmount, Deposited, Winnings, Bonus;
    double EntryFee;
    int FinaltoPayAmount;
    String FinalToPayAmount;

    //paytment gateway
    private String orderID = "";
    private String customerID = "";
    private String NeddedAmontToAdd;
    String FPaymentId, FTransactionId, FPaymentMode, FTransactionStatus, FAmount, FUserName,IntentType = "", IntentEmail = "",
            FEmail, FPhone, FMessage;
    JSONObject TrakNPayResponse;
    private boolean neededToAdd = false;
    private String city, state, pincode;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        context = activity = this;
        initViews();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        sessionManager = new SessionManager();

        if (sessionManager.getUser(context) != null) {
            IntentType = sessionManager.getUser(context).getType();
            IntentEmail = sessionManager.getUser(context).getEmail();
            Log.wtf("AddCashType", IntentEmail + " | " + IntentType);
        }

        final String Entryfees = JoinContestActivity.EntryFee;
        try {
            EntryFee = Double.valueOf(JoinContestActivity.EntryFee);
            JoinMyTeamId = ContestListActivity.JoinMyTeamId;
            MyContestCode = JoinContestActivity.MyContestCode;

            tv_ConfirmationEntryFee.setText("" + EntryFee);
            tv_ConfirmationToPay.setText("" + EntryFee);
        }catch (Exception e){e.printStackTrace();}finally { Log.d("mega_error","something big");}

        getDataForPayment();
        tv_ConfirmationJoinContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dsddddd", TotalAmount + " dasdfasd final amt " + FinaltoPayAmount);
                // TotalAmount = 15.0;
                if (TotalAmount >= FinaltoPayAmount) {
                    Log.e("finalAmounttoPay", FinaltoPayAmount + "");
                    callJoinContest(true);

                } else {

                    AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                    ab.setMessage("Not enough balance in your account to join contest.");

                    ab.setPositiveButton("Add Amount", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // not in use
                           /* NeddedAmontToAdd = String.valueOf(FinaltoPayAmount - TotalAmount);
                            Log.e("neddedAmount", NeddedAmontToAdd + "");
                            initializePayment(); */
                            // not in use
                           /* Intent i = new Intent(activity,AddCashActivity.class);
                            i.putExtra("Activity","PaymentConfirmationActivity");
                            i.putExtra("EntryFee",Entryfees);
                            startActivity(i);*/

                            try {
                                int addAmtFinal = (int) (FinaltoPayAmount - TotalAmount);
                                int TotalCoin = (int) (addAmtFinal * 0.75);
                                Log.d("total_amt",FinaltoPayAmount+" <|> "+TotalCoin);
                                Log.wtf("AddCashType", IntentEmail + " | " + IntentType);
                                if (IntentType.equalsIgnoreCase("Social")) {
                                    // if ( IntentEmail != null && "".equalsIgnoreCase(IntentEmail)) {
                                    Intent i = new Intent(activity, PaymentOptionActivity.class);
                                    startActivity(i);

                                }
                                if (IntentType.equalsIgnoreCase("Normal")) {
                                    if ( IntentEmail != null && !"".equalsIgnoreCase(IntentEmail)) {
                                        Intent i = new Intent(activity, PaymentOptionActivity.class);
                                        startActivity(i);
                                    }else {
                                        Toast.makeText(activity, "Please Add Email Address Or Phone.", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(activity, EditProfileActivity.class);
                                        startActivity(i);
                                    }

                                }
                                /*Intent i = new Intent(activity, PayUPaymentConfig.class);
                                i.putExtra("FinalAmount", ""+TotalCoin);
                                i.putExtra("FinalCoin", ""+FinaltoPayAmount);
                                startActivity(i);*/
                            }catch (
                                    Exception e){e.printStackTrace();
                            }
                            // not in use
                            /* Intent i = new Intent(activity, PaymetPackageActivity.class);
                            startActivity(i); */
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
        });
        callMyAccountDetails(true);
    }

    private void getDataForPayment() {
        customerID = sessionManager.getUser(context).getUser_id();
        orderID = "OrderID" + System.currentTimeMillis() + "-" + customerID + "-" + FinaltoPayAmount + "";
        if (sessionManager.getUser(PaymentConfirmationActivity.this).getCity() == null
                || sessionManager.getUser(PaymentConfirmationActivity.this).getCity().equals(" ")
                || sessionManager.getUser(PaymentConfirmationActivity.this).getCity().equals("")) {
            city = "City";
        } else {
            city = sessionManager.getUser(PaymentConfirmationActivity.this).getCity();
        }
        if (sessionManager.getUser(PaymentConfirmationActivity.this).getState() == null
                || sessionManager.getUser(PaymentConfirmationActivity.this).getState().equals(" ")
                || sessionManager.getUser(PaymentConfirmationActivity.this).getState().equals("")) {
            state = "State";

        } else {
            state = sessionManager.getUser(PaymentConfirmationActivity.this).getState();
        }
        if (sessionManager.getUser(PaymentConfirmationActivity.this).getPincode() == null
                || sessionManager.getUser(PaymentConfirmationActivity.this).getPincode().equals(" ")
                || sessionManager.getUser(PaymentConfirmationActivity.this).getPincode().equals("")) {
            pincode = "Pincode";
        } else {
            pincode = sessionManager.getUser(PaymentConfirmationActivity.this).getPincode();
        }
    }

    public void initViews() {

        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_ConfirmationJoinContest = findViewById(R.id.tv_ConfirmationJoinContest);
        tv_ConfirmationToPay = findViewById(R.id.tv_ConfirmationToPay);
        tv_ConfirmationEntryFee = findViewById(R.id.tv_ConfirmationEntryFee);
        tv_WalletBalance = findViewById(R.id.tv_WalletBalance);
        tv_ConfirmationBonus = findViewById(R.id.tv_ConfirmationBonus);
        tv_join_terms_condition = findViewById(R.id.tv_join_terms_condition);
        tv_join_terms_condition.setText("By Joining this contest, you accept " + getString(R.string.app_name) + "'s T & C and confirm that you are not a resident of Assam, Odisha, Telangana, Nagaland or Sikkim.");
        tv_join_terms_condition.setVisibility(View.GONE);
        tv_HeaderName.setText("CONFIRMATION");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initializePayment() {


        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        TrakConstant.PG_ORDER_ID = Integer.toString(n);
        PaymentParams pgPaymentParams = new PaymentParams();
        pgPaymentParams.setAPiKey(TrakConstant.PG_API_KEY);
        pgPaymentParams.setAmount(String.valueOf(NeddedAmontToAdd));
        pgPaymentParams.setEmail(sessionManager.getUser(PaymentConfirmationActivity.this).getEmail());
        pgPaymentParams.setName(sessionManager.getUser(PaymentConfirmationActivity.this).getName());
        pgPaymentParams.setPhone(sessionManager.getUser(PaymentConfirmationActivity.this).getMobile());
        pgPaymentParams.setOrderId(orderID);
        pgPaymentParams.setCurrency(TrakConstant.PG_CURRENCY);
        pgPaymentParams.setDescription(TrakConstant.PG_DESCRIPTION);
        pgPaymentParams.setCity(city);
        pgPaymentParams.setState(state);
        pgPaymentParams.setAddressLine1(TrakConstant.PG_ADD_1);
        pgPaymentParams.setAddressLine2(TrakConstant.PG_ADD_2);
        pgPaymentParams.setZipCode(pincode);
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
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());

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
            jsonObject.put("entryFee", EntryFee);
            //if contest is private then value is 1 else 0
            Log.e("finaldatatojoin", sessionManager.getUser(context).getUser_id()
                    + ",match_id=" + ContestListActivity.IntentMatchId
                    + ",my_team_id=" + ContestListActivity.JoinMyTeamId + ",contest_id="
                    + ContestListActivity.ContestId + ",contest_amount=" + FinalToPayAmount + ",entryFee=" + EntryFee + "");
            if (JoinContestActivity.MyContestCode.equals("")) {
                jsonObject.put("private", "0");
            } else {
                jsonObject.put("private", "1");
            }
            Log.e("finaldatatojoin", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        if (type.equals(JOINCONTESTTYPE)) {
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
                finishAffinity();


            } else {
                ShowToast(context, message);
                Log.e("checkckck", "calleddd");
                Intent i = new Intent(activity, InviteInContestActivity.class);
                i.putExtra("ContestCode", JoinContestActivity.MyContestCode);
                startActivity(i);
                finish();
            }
            Log.e("resultAfterJoined", result.toString());

        }

        if (type.equals(ADDAMOUNTTYPE)) {
            try {
                String Status = result.getString("transaction_status");

                Log.e("add_amount_sir_respose", result.toString() + "");

                if (neededToAdd) {
                    Dialog(FTransactionStatus, FTransactionId, orderID, FAmount);

                }

                callMyAccountDetails(true);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (type.equals(MYACCOUNTTYPE)) {
            try {
                Log.e("bonnessss", result + "");
                String total = String.valueOf(Double.parseDouble((result.getString("total_amount"))));
                String finalAmt = result.getString("total_amount");
                TotalAmount = Double.valueOf(total);
                Log.e("ttttttt", total);
                Log.e("dgdd", TotalAmount + "");
                Deposited = Double.valueOf(result.getString("credit_amount"));
                Winnings = Double.valueOf(result.getString("winning_amount"));
                double bonus = Double.valueOf(result.getString("bonous_amount"));
                double bonus_dedection_value = Double.valueOf(result.getString("bonus_dedection_value"));

                Log.e("bonusss", bonus + "");

                if (bonus >= ((EntryFee * bonus_dedection_value) / 100)) {

                    Bonus = (EntryFee * bonus_dedection_value) / 100;
                    Log.e("this2", "" + (EntryFee * bonus_dedection_value) / 100);
                    //jab bonus 20% se jyda ho ya equal ho tab deduct honge
                }
                if (bonus < ((EntryFee * bonus_dedection_value) / 100)) {
                    Bonus = 0;
                    Log.e("thi3", "3");
                    //jab bonus 20% se kam h
                    Log.e("this3", "" + (EntryFee * bonus_dedection_value) / 100);

                }

                tv_WalletBalance.setText("Unutilized Balance + Winnings = " + finalAmt);

                Log.e("boooooo", Bonus + "");
                tv_ConfirmationBonus.setText("- " + Bonus);
                if (EntryFee == 0) {
                    FinaltoPayAmount = (int) EntryFee;
                    DecimalFormat df = new DecimalFormat("#.##");
                    FinalToPayAmount = df.format(FinaltoPayAmount);
                    Log.e("EntryFee", EntryFee+" > "+Bonus+" EntryFee 1 > "+FinalToPayAmount+"  Final_amt >"+finalAmt);
                } else if (EntryFee >= Bonus) {
                    FinaltoPayAmount = (int) (EntryFee - Bonus);
                    DecimalFormat df = new DecimalFormat("#.##");
                    FinalToPayAmount = df.format(FinaltoPayAmount);
                    /* if (Integer.parseInt(finalAmt) >= 0){
                        FinaltoPayAmount = (int) (EntryFee - Integer.parseInt(finalAmt) - Bonus);
                        FinalToPayAmount = df.format(FinaltoPayAmount);
                    } */
                    Log.e("EntryFee", EntryFee+" > "+Bonus+" EntryFee 2 > "+FinalToPayAmount+"  Final_amt > "+finalAmt);
                } else {
                    FinaltoPayAmount = (int) (EntryFee - Bonus);
                    FinaltoPayAmount = (int) 0.0;
                    FinalToPayAmount = String.valueOf(FinaltoPayAmount);
                    Log.e("EntryFee", EntryFee+" > "+Bonus+" EntryFee 3 > "+FinalToPayAmount+"  Final_amt > "+finalAmt);
                }
                tv_ConfirmationToPay.setText("" + FinalToPayAmount);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        Log.e("resultAfterJoined", message);

        ShowToast(context, message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PGConstants.REQUEST_CODE) {
            if (resultCode == android.app.Activity.RESULT_OK) {
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
                        neededToAdd = true;   //important
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
            if (resultCode == android.app.Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                ShowToast(context, "Transaction Canceled");
            }

        }
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
            jsonObject.put("amount", NeddedAmontToAdd + "");
            jsonObject.put("mode", "TrakNPay");
            jsonObject.put("transection_detail", TrakNPayResponse);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
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

                callMyAccountDetails(true);


            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.getUser(context) != null) {
            IntentType = sessionManager.getUser(context).getType();
            IntentEmail = sessionManager.getUser(context).getEmail();
            Log.wtf("AddCashType", IntentEmail + " | " + IntentType);
        }
    }
}
