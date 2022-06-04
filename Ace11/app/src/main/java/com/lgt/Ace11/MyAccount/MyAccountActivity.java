package com.lgt.Ace11.MyAccount;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Adapter.CustomAdapter;
import com.lgt.Ace11.Bean.WithdrawModel;
import com.lgt.Ace11.EditProfileActivity;
import com.lgt.Ace11.HomeActivity;
import com.lgt.Ace11.PaymentOptionActivity;
import com.lgt.Ace11.R;
import com.lgt.Ace11.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.MYACCOUNT;
import static com.lgt.Ace11.Config.WITHDRAWLREQUEST;
import static com.lgt.Ace11.Constants.MYACCOUNTTYPE;
import static com.lgt.Ace11.Constants.SUBMITWITHDRAWLREQUESTTYPE;

public class MyAccountActivity extends AppCompatActivity implements ResponseManager {

    MyAccountActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back;
    TextView tv_HeaderName;
    SessionManager sessionManager;

    TextView tv_TotalBalance, tv_AddBalance, tv_DepositedAmount, tv_WinningAmount, tv_Withdraw,
            tv_BonusAmount;
    ImageView im_DepositeInfo, im_WinningInfo, im_BonusInfo;
    RelativeLayout RL_MyRecentTransactions, RL_ManagePayments;

    String TotalBalance, Deposited, Winnings, Bonus, PanStatus, AadhaarStatus;
    CustomAdapter customAdapter;
    ArrayList<WithdrawModel> spinnerArray = new ArrayList<WithdrawModel>();
    //Upload Document
    TextView tv_UploadPan, tv_UploadAadhaar;
    ImageView im_PanVerified, im_AadhaarVerified;


    //withdraw document
    public static AlertDialog dialog;
    String Amount, Name, AccountNumber, withdraw_type, withdraw_id, IntentType = "", IntentEmail = "",
            IFSCCode, bankName;
    private TextView tv_SubmitWithdrawRequest;
    EditText et_WithdrawEnterAmount, et_WithdrawName,
            et_WithdrawAccountNumber, et_WithdrawIFSCCode,
            et_WithdrawBankName;
    Spinner et_WithdrawType;
    TextInputLayout input_pick_type_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        context = activity = this;
        initViews();
        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        getUserDetailsData();

        Log.wtf("AddCashType", IntentEmail + " | " + IntentType);

        tv_AddBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        callMyAccount(true);

        RL_MyRecentTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, MyTransactionActivity.class);
                startActivity(i);
            }
        });

        tv_Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double Amount = Double.parseDouble(activity.getString(R.string.MinimumWithdrawAmountLimit));
                if (PanStatus.equals("2") && AadhaarStatus.equals("2")) {
                    if (Double.parseDouble(Winnings) >= 1) {
                        // openWithdrawDialog();
                        Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT).show();

                        /*Intent i = new Intent(activity, WithdrawAmountActivity.class);
                        i.putExtra("AvailableBalance", Winnings);
                        startActivity(i);*/
                    } else {
                        ShowToast(context, "Not Enough Amount for Withdraw Request.");
                    }
                } else {
                    ShowToast(context, "Update your KYC document for withdraw amount.");
                }
            }
        });

        tv_UploadAadhaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, UploadKYCActivity.class);
                i.putExtra("DocType", "Aadhaar");
                startActivity(i);
            }
        });
        tv_UploadPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, UploadKYCActivity.class);
                i.putExtra("DocType", "Pan");
                startActivity(i);
            }
        });
    }

    private void getUserDetailsData() {
        if (sessionManager.getUser(context) != null) {
            IntentType = sessionManager.getUser(context).getType();
            IntentEmail = sessionManager.getUser(context).getEmail();
            Log.wtf("AddCashType", IntentEmail + " | " + IntentType);
        }
    }

    public void showAlertDialog() {
        /*androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);
        LinearLayout llcoin = dialogView.findViewById(R.id.ll_bit_coin);
        ImageView cancel_btn = dialogView.findViewById(R.id.iv_cancel_btn);
        LinearLayout lltrackpay = dialogView.findViewById(R.id.ll_track_and_pay_cash);
        builder1.setView(dialogView);
        builder1.setCancelable(false);
        final androidx.appcompat.app.AlertDialog alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        lltrackpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(activity, PaymentOptionActivity.class);
                startActivity(in);
                alert11.dismiss();
            }
        });
        llcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AddCashActivity.class);
                startActivity(i);
                alert11.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert11.dismiss();
            }
        });
        alert11.show();*/
    }

    private void openWithdrawDialog() {
        View customView = LayoutInflater.from(this).inflate(R.layout.withdrawl_layout, null);
        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(customView);
        dialog.setCanceledOnTouchOutside(false);
        et_WithdrawType = customView.findViewById(R.id.et_WithdrawType);
        et_WithdrawEnterAmount = customView.findViewById(R.id.et_WithdrawEnterAmount);
        input_pick_type_id = customView.findViewById(R.id.input_pick_type_id);
        et_WithdrawEnterAmount = customView.findViewById(R.id.et_WithdrawEnterAmount);
        et_WithdrawEnterAmount = customView.findViewById(R.id.et_WithdrawEnterAmount);
        et_WithdrawName = customView.findViewById(R.id.et_WithdrawName);
        et_WithdrawAccountNumber = customView.findViewById(R.id.et_WithdrawAccountNumber);
        et_WithdrawIFSCCode = customView.findViewById(R.id.et_WithdrawIFSCCode);
        et_WithdrawBankName = customView.findViewById(R.id.et_WithdrawBankName);
        tv_SubmitWithdrawRequest = customView.findViewById(R.id.tv_SubmitWithdrawRequest);

        // start add items
        spinnerArray.clear();
        spinnerArray.add(new WithdrawModel(R.drawable.withdraw, "Select Withdraw Option"));
        spinnerArray.add(new WithdrawModel(R.drawable.bit_coin, "BTC Address"));
        spinnerArray.add(new WithdrawModel(R.drawable.gpay, "Google Pay"));
        spinnerArray.add(new WithdrawModel(R.drawable.paypal, "PayPal"));
        spinnerArray.add(new WithdrawModel(R.drawable.poyneer, "Payoneer"));
        customAdapter = new CustomAdapter(getApplicationContext(), spinnerArray);
        et_WithdrawType.setAdapter(customAdapter);
        // end

        et_WithdrawType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    withdraw_type = "";
                    if (i == 1) {
                        withdraw_type = "BTC";
                        et_WithdrawName.setHint("Enter BTC Address");
                        input_pick_type_id.setVisibility(View.VISIBLE);
                    } else if (i == 2) {
                        withdraw_type = "GooglePay";
                        et_WithdrawName.setHint("Enter Google Pay ID Or Number");
                        input_pick_type_id.setVisibility(View.VISIBLE);
                    } else if (i == 3) {
                        withdraw_type = "PayPal";
                        et_WithdrawName.setHint("Enter PayPal ID");
                        input_pick_type_id.setVisibility(View.VISIBLE);
                    } else if (i == 4) {
                        withdraw_type = "Payoneer";
                        et_WithdrawName.setHint("Enter Payoneer ID");
                        input_pick_type_id.setVisibility(View.VISIBLE);
                    }
                } else {
                    input_pick_type_id.setVisibility(View.GONE);
                    Toast.makeText(activity, "Select Withdraw Option", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(activity, "Please Select Withdraw Method!", Toast.LENGTH_SHORT).show();
            }
        });

        tv_SubmitWithdrawRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amount = et_WithdrawEnterAmount.getText().toString();
                // AccountNumber = sessionManager.getUser(context).getName();
                Name = et_WithdrawName.getText().toString();
                IFSCCode = et_WithdrawIFSCCode.getText().toString();
                bankName = et_WithdrawBankName.getText().toString();


                if (TextUtils.isEmpty(Amount)) {
                    et_WithdrawEnterAmount.setError("Enter Amount");
                    et_WithdrawEnterAmount.requestFocus();
                    return;
                }

                if (et_WithdrawName.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(Name)) {
                        et_WithdrawEnterAmount.setError("Enter Valid Details");
                        et_WithdrawEnterAmount.requestFocus();
                        return;
                    }
                }


                /*if (TextUtils.isEmpty(Name)){
                    et_WithdrawName.setError("Enter Name");
                    et_WithdrawName.requestFocus();
                    return;
                }   if (TextUtils.isEmpty(AccountNumber)){
                    et_WithdrawAccountNumber.setError("Enter Account Number");
                    et_WithdrawAccountNumber.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(IFSCCode)){
                    et_WithdrawIFSCCode.setError("Enter IFSC");
                    et_WithdrawIFSCCode.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(bankName)){
                    et_WithdrawBankName.setError("Enter Branch");
                    et_WithdrawBankName.requestFocus();
                    return;
                }*/

                if (Double.valueOf(Amount) >= 500) {
                    dialog.dismiss();
                    ConfirmationDialog(Amount);
                } else {
                    ShowToast(activity, "You cannot withdraw less than 500");
                }
            }
        });


        dialog.show();
    }

    public void ConfirmationDialog(String Amount) {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setMessage("Confirm your withdrawl request of ₹" + Amount + " ?");
        ab.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                callSubmitWithdrawlRequest(true);

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

    private void callSubmitWithdrawlRequest(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(WITHDRAWLREQUEST,
                    createWithdrawlRequestJson(), context, activity, SUBMITWITHDRAWLREQUESTTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createWithdrawlRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            jsonObject.put("amount", Amount);
            // jsonObject.put("user_name", AccountNumber);
            jsonObject.put("withdraw_type", withdraw_type);
            jsonObject.put("withdraw_id", Name);
            ShowToast(context, "Withdraw successfully requested...");
            dialog.dismiss();
            Log.e("ifsccc", jsonObject.toString());

            callMyAccount(true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void initViews() {

        tv_TotalBalance = findViewById(R.id.tv_TotalBalance);
        tv_AddBalance = findViewById(R.id.tv_AddBalance);
        tv_DepositedAmount = findViewById(R.id.tv_DepositedAmount);
        im_DepositeInfo = findViewById(R.id.im_DepositeInfo);
        tv_WinningAmount = findViewById(R.id.tv_WinningAmount);
        tv_Withdraw = findViewById(R.id.tv_Withdraw);
        im_WinningInfo = findViewById(R.id.im_WinningInfo);
        tv_BonusAmount = findViewById(R.id.tv_BonusAmount);
        im_BonusInfo = findViewById(R.id.im_BonusInfo);
        RL_MyRecentTransactions = findViewById(R.id.RL_MyRecentTransactions);
        RL_ManagePayments = findViewById(R.id.RL_ManagePayments);

        //Upload Document
        im_AadhaarVerified = findViewById(R.id.im_AadhaarVerified);
        tv_UploadAadhaar = findViewById(R.id.tv_UploadAadhaar);
        im_PanVerified = findViewById(R.id.im_PanVerified);
        tv_UploadPan = findViewById(R.id.tv_UploadPan);


        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        tv_HeaderName.setText("MY ACCOUNT");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void callMyAccount(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(MYACCOUNT,
                    createRequestJson(), context, activity, MYACCOUNTTYPE,
                    isShowLoader, responseManager);
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

        Log.e("checkkkWith", result + "");


        try {
            TotalBalance = result.getString("total_amount");
            Deposited = result.getString("credit_amount");
            Winnings = result.getString("winning_amount");
            Bonus = result.getString("bonous_amount");
            AadhaarStatus = result.getString("aadhar_status");
            PanStatus = result.getString("pan_status");


            tv_TotalBalance.setText(" " + TotalBalance);
            tv_DepositedAmount.setText(" " + Deposited);
            tv_WinningAmount.setText(" " + Winnings);
            tv_BonusAmount.setText(" " + Bonus);

            if (PanStatus.equals("0")) {
                im_PanVerified.setVisibility(View.INVISIBLE);
                tv_UploadPan.setEnabled(true);
            } else if (PanStatus.equals("1")) {
                im_PanVerified.setVisibility(View.VISIBLE);
                im_PanVerified.setImageResource(R.drawable.pending_icon);
                tv_UploadPan.setText("Pending");
                tv_UploadPan.setEnabled(false);
            } else if (PanStatus.equals("2")) {
                im_PanVerified.setVisibility(View.VISIBLE);
                im_PanVerified.setImageResource(R.drawable.verify_icon);
                tv_UploadPan.setText("Verified");
                tv_UploadPan.setEnabled(false);
            } else {
                im_PanVerified.setVisibility(View.INVISIBLE);
                tv_UploadPan.setText("Upload");
                tv_UploadPan.setEnabled(true);
            }

            if (AadhaarStatus.equals("0")) {
                im_AadhaarVerified.setVisibility(View.INVISIBLE);
                tv_UploadAadhaar.setEnabled(true);
            } else if (AadhaarStatus.equals("1")) {
                im_AadhaarVerified.setVisibility(View.VISIBLE);
                im_AadhaarVerified.setImageResource(R.drawable.pending_icon);
                tv_UploadAadhaar.setText("Pending");
                tv_UploadAadhaar.setEnabled(false);

            } else if (AadhaarStatus.equals("2")) {
                im_AadhaarVerified.setVisibility(View.VISIBLE);
                im_AadhaarVerified.setImageResource(R.drawable.verify_icon);
                tv_UploadAadhaar.setText("Verified");
                tv_UploadAadhaar.setEnabled(false);
            } else {
                im_AadhaarVerified.setVisibility(View.INVISIBLE);
                tv_UploadAadhaar.setText("Upload");
                tv_UploadAadhaar.setEnabled(true);
            }


            if (type.equals(SUBMITWITHDRAWLREQUESTTYPE)) {


                Intent intent = new Intent(activity, HomeActivity.class);
                startActivity(intent);
                finishAffinity();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {

    }


    @Override
    protected void onResume() {
        super.onResume();

        getUserDetailsData();
    }
}
