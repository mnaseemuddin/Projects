package com.lgt.Ace11;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Class.SingletonRequestQueue;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Bean.UserDetails;
import com.lgt.Ace11.TrakNPayPackage.TrakConstant;
import com.test.pg.secure.pgsdkv4.PGConstants;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.ADDAMOUNT;
import static com.lgt.Ace11.Config.BIT_COIN_URL;
import static com.lgt.Ace11.Config.BIT_KEY;
import static com.lgt.Ace11.Config.EDITPROFILE;
import static com.lgt.Ace11.Config.check_coinaddress;
import static com.lgt.Ace11.Config.check_ioaddress;
import static com.lgt.Ace11.Constants.ADDAMOUNTTYPE;
import static com.lgt.Ace11.Constants.CheckAmountType;

public class AddCashActivity extends AppCompatActivity implements ResponseManager {
    RecyclerView rv_Transactions_history;
    AddCashActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    ImageView im_back, iv_barcode_scanner, iv_copy_barcode, iv_transaction_history;
    TextView tv_HeaderName, tv_barcode_address, tv_check_balance;
    SessionManager sessionManager;
    String address = "",USER_ID;
    EditText et_AddCashEnterAmount;
    TextView tv_AddCash, tv_OneHundred, tv_TwoHundred, tv_FiveHundred, tv_sixHundred;
    String FinalAmountToAdd;
    String EntryFee, BITCOIN_USERID, BITCOIN_ADDRESS, BITCOIN_LABEL;
    public static String Activity = "";
    private String orderID = "";
    private String customerID = "";
    StringBuilder stringBuilder;
    double TotalAmount, Deposited, Winnings, Bonus, FinaltoPayAmount;
    String FPaymentId, FTransactionId, FPaymentMode, FTransactionStatus, FAmount, FUserName,
            FEmail, FPhone, FMessage;
    JSONObject TrakNPayResponse;
    boolean isGenerated = false;
    //payment
    private String city, state, pincode;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cash);
        sessionManager = new SessionManager();
        context = activity = this;
        initViews();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);

        Intent brightnessIntent = this.getIntent();
        float brightness = brightnessIntent.getFloatExtra("brightness value", 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1;
        getWindow().setAttributes(lp);

        try {
            customerID = sessionManager.getUser(context).getUser_id();
            getUserPaymentData();
            Log.d("customer_id_found",""+customerID);
        }catch (Exception e){e.printStackTrace();}

        try {
            EntryFee = getIntent().getStringExtra("EntryFee");

            if (EntryFee != null) {
                et_AddCashEnterAmount.setText(String.valueOf(EntryFee));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // getUserPaymentData();

        try {
            Activity = getIntent().getStringExtra("Activity");
            System.out.print(Activity);
            if (Activity == null) {
                Activity = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // getDataForPayment();
        // checkAccountStatus(true);
        tv_AddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalAmountToAdd = et_AddCashEnterAmount.getText().toString();

                if (FinalAmountToAdd.equals("")) {
                    ShowToast(context, "Enter Valid Amount");
                    FinalAmountToAdd = "0";
                } else if (Integer.parseInt(FinalAmountToAdd) < 10) {
                    ShowToast(context, "Enter minimum Amount is 10");

                } else {
                    initializePayment();
                   /* Intent i = new Intent(activity,PaymentOptionActivity.class);
                    i.putExtra("FinalAmount",FinalAmountToAdd);

                    startActivity(i);*/
                }
            }
        });

        tv_OneHundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_AddCashEnterAmount.setText("10");
                tv_OneHundred.setBackgroundResource(R.drawable.add_cash_black);

                tv_FiveHundred.setBackgroundResource(R.drawable.addcash);
                tv_sixHundred.setBackgroundResource(R.drawable.addcash);
                tv_TwoHundred.setBackgroundResource(R.drawable.addcash);
            }
        });
        tv_TwoHundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_TwoHundred.setBackgroundResource(R.drawable.add_cash_black);
                tv_OneHundred.setBackgroundResource(R.drawable.addcash);
                tv_FiveHundred.setBackgroundResource(R.drawable.addcash);
                tv_sixHundred.setBackgroundResource(R.drawable.addcash);
                et_AddCashEnterAmount.setText("20");
            }
        });
        tv_FiveHundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_TwoHundred.setBackgroundResource(R.drawable.addcash);
                tv_OneHundred.setBackgroundResource(R.drawable.addcash);
                tv_FiveHundred.setBackgroundResource(R.drawable.add_cash_black);
                tv_sixHundred.setBackgroundResource(R.drawable.addcash);
                et_AddCashEnterAmount.setText("50");
            }
        });

        tv_sixHundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_TwoHundred.setBackgroundResource(R.drawable.addcash);
                tv_OneHundred.setBackgroundResource(R.drawable.addcash);
                tv_FiveHundred.setBackgroundResource(R.drawable.addcash);
                tv_sixHundred.setBackgroundResource(R.drawable.add_cash_black);
                et_AddCashEnterAmount.setText("60");
            }
        });
        // setUpPaymentWallet();
    }

    private void getUserPaymentData() {
        JsonObjectRequest stringRequest = new JsonObjectRequest(check_coinaddress, createAccountJson(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response + "");
                try {
                    JSONObject jsonObject = response;//new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("0")) {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        BITCOIN_ADDRESS="";
                        BITCOIN_ADDRESS = getSaltString();
                        setUpPaymentWallet();
                    } else if (status.equalsIgnoreCase("1")) {
                        isGenerated = true;
                        BITCOIN_ADDRESS="";
                        // Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        BITCOIN_ADDRESS = jsonObject.getString("data");
                        stringBuilder.append(BITCOIN_ADDRESS);
                        generatebarCode(stringBuilder, iv_barcode_scanner);
                        tv_barcode_address.setText(BITCOIN_ADDRESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.getMessage() + "");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /*private void getUserPaymentData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, check_coinaddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("0")) {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                        updateUserPaymentData();
                    } else if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", sessionManager.getUser(context).getUser_id());
                Log.d("data_sending",""+param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }*/
    private void updateUserPaymentData() {
        JsonObjectRequest stringRequest = new JsonObjectRequest(EDITPROFILE, createRequestJson(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response + "");
                try {
                    JSONObject jsonObject = response; //new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("0")) {

                    } else if (status.equalsIgnoreCase("success")) {
                        stringBuilder.append(address);
                        generatebarCode(stringBuilder, iv_barcode_scanner);
                        tv_barcode_address.setText(address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.getMessage() + "");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    /*    private void callEditProfile(boolean isShowLoader) {
            try {

                apiRequestManager.callAPI(EDITPROFILE,
                        createEditProfileJson(), context, activity, EDITPROFILETYPE,
                        isShowLoader,responseManager);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject createEditProfileJson() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
                jsonObject.put("coin_address", BITCOIN_ADDRESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }*/

    /*private void getDataForPayment() {
        orderID = "OrderID" + System.currentTimeMillis() + "-" + customerID + "-" + FinalAmountToAdd;
        if (sessionManager.getUser(AddCashActivity.this).getCity() == null ||
                sessionManager.getUser(AddCashActivity.this).getCity().equals(" ")
                || sessionManager.getUser(AddCashActivity.this).getCity().equals("")) {

            city = "City";

        } else {
            city = sessionManager.getUser(AddCashActivity.this).getCity();

        }
        if (sessionManager.getUser(AddCashActivity.this).getState() == null ||
                sessionManager.getUser(AddCashActivity.this).getState().equals(" ")
                || sessionManager.getUser(AddCashActivity.this).getState().equals("")) {
            state = "State";

        } else {
            state = sessionManager.getUser(AddCashActivity.this).getState();
        }
        if (sessionManager.getUser(AddCashActivity.this).getPincode() == null
                || sessionManager.getUser(AddCashActivity.this).getPincode().equals(" ")
                || sessionManager.getUser(AddCashActivity.this).getPincode().equals("")) {
            pincode = "Pincode";
        } else {
            pincode = sessionManager.getUser(AddCashActivity.this).getPincode();
        }
    }*/

    private void initializePayment() {


        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        TrakConstant.PG_ORDER_ID = Integer.toString(n);
        PaymentParams pgPaymentParams = new PaymentParams();
        pgPaymentParams.setAPiKey(TrakConstant.PG_API_KEY);
        pgPaymentParams.setAmount(FinalAmountToAdd);
        pgPaymentParams.setEmail(sessionManager.getUser(AddCashActivity.this).getEmail());
        pgPaymentParams.setName(sessionManager.getUser(AddCashActivity.this).getName());
        pgPaymentParams.setPhone(sessionManager.getUser(AddCashActivity.this).getMobile());
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

    public void initViews() {
        stringBuilder = new StringBuilder();
        im_back = findViewById(R.id.im_back);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);
        tv_check_balance = findViewById(R.id.tv_check_balance);
        iv_transaction_history = findViewById(R.id.iv_transaction_history);
        et_AddCashEnterAmount = findViewById(R.id.et_AddCashEnterAmount);
        tv_AddCash = findViewById(R.id.tv_AddCash);
        rv_Transactions_history = findViewById(R.id.rv_Transactions_history);
        tv_sixHundred = findViewById(R.id.tv_sixHundred);
        tv_OneHundred = findViewById(R.id.tv_OneHundred);
        tv_TwoHundred = findViewById(R.id.tv_TwoHundred);
        tv_FiveHundred = findViewById(R.id.tv_FiveHundred);
        tv_HeaderName.setText("ADD CASH");
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // setup payment code not in use
        /*if (sessionManager.getUser(context) != null && sessionManager.getUser(context).getBITCOIN_ADDRESS() == null) {
            Toast.makeText(activity, "NO VALID WALLET DETAILS", Toast.LENGTH_SHORT).show();
        } else {
            // BITCOIN_USERID = sessionManager.getUser(context).getBITCOIN_USERID();
            // BITCOIN_LABEL = sessionManager.getUser(context).getBITCOIN_LABEL();
            // BITCOIN_ADDRESS = sessionManager.getUser(context).getBITCOIN_ADDRESS();
            // Log.d("loading", BITCOIN_LABEL + " | " + BITCOIN_ADDRESS + " | " + BITCOIN_USERID);
        }*/
        // set barcode
        iv_barcode_scanner = findViewById(R.id.iv_barcode_scanner);
        iv_copy_barcode = findViewById(R.id.iv_copy_barcode);
        tv_barcode_address = findViewById(R.id.tv_barcode_address);

        iv_copy_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGenerated) {
                    copyGenerateBarCodeADD(BITCOIN_ADDRESS);
                } else {
                    Toast.makeText(activity, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_transaction_history.setVisibility(View.VISIBLE);
        iv_transaction_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Payment History", Toast.LENGTH_SHORT).show();
            }
        });

        tv_check_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Check Balance", Toast.LENGTH_SHORT).show();
                checkBalance();
            }
        });
    }

    private void checkBalance() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, check_ioaddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("check_Balance_Request", ""+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("check_Balance_Request", ""+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("user_id", customerID);
                if (!address.equalsIgnoreCase("")){
                    param.put("addresses", address);
                }else if(!BITCOIN_ADDRESS.equalsIgnoreCase("")){
                    param.put("addresses", BITCOIN_ADDRESS);
                }
                Log.d("check_Balance_Request", ""+param);
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void copyGenerateBarCodeADD(String generatecode) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(generatecode);
            Toast.makeText(activity, "Copy Pay Address", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label", generatecode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(activity, "Copy Pay Address", Toast.LENGTH_SHORT).show();
        }
    }

    private void generatebarCode(StringBuilder generatecode, ImageView imageView) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(generatecode.toString(), BarcodeFormat.QR_CODE, 1200, 1200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        Toast.makeText(mContext, "FFFFFF   " + type, Toast.LENGTH_SHORT).show();
        if (type.equals(ADDAMOUNTTYPE)) {
            try {
                String Status = result.getString("transaction_status");

                Log.e("add_cash_amount", Status + "");
                Dialog(FTransactionStatus, FTransactionId, orderID, FAmount);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equals(CheckAmountType)) {
            Log.e("add_cash_amount", result.toString() + "");
        }

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {
        Toast.makeText(mContext, "FFFFFF   " + type + result.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Context mContext, String type, String message) {

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

                        if (response.getString("response_code").equals("1043")) {
                            ShowToast(context, "Transaction Canceled");
                        }
                        Log.e("resssppoo", FTransactionStatus);

                        Log.e("TrakNPay", "onActivityResult: " + response);
                        System.out.print(AddCashActivity.Activity);

                        // callAddAmount(true);

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

    private void checkAccountStatus(Boolean isShowLoader) {
        try {
            Toast.makeText(this, "FFFFFF   " + CheckAmountType, Toast.LENGTH_SHORT).show();
            apiRequestManager.callAPI(check_coinaddress, createAccountJson(), context, activity, CheckAmountType, isShowLoader, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createAccountJson() {
        JSONObject getting_data = new JSONObject();
        try {
            getting_data.put("user_id", customerID);
            Log.d("getting_data", ""+getting_data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getting_data;
    }

    JSONObject checkBalanceRequestJson() {
        JSONObject sending_data = new JSONObject();
        try {
            sending_data.put("user_id", customerID);
            if (!address.equalsIgnoreCase("")){
                sending_data.put("addresses", address);
            }else if(!BITCOIN_ADDRESS.equalsIgnoreCase("")){
                sending_data.put("addresses", BITCOIN_ADDRESS);
            }
            Log.d("check_Balance_Request", ""+sending_data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sending_data;
    }

    JSONObject createRequestJson() {
        JSONObject sending_data = new JSONObject();
        try {
            sending_data.put("user_id", customerID);
            sending_data.put("coin_address", address);
            Log.d("sending_data", ""+sending_data.toString()+"  uyhguy"+ customerID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sending_data;
    }

    private void setUpPaymentWallet() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BIT_COIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("bit_response", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String network = data.getString("network");
                        String user_id = data.getString("user_id");
                        String Pay_Address = data.getString("address");
                        String label = data.getString("label");
                        UserDetails userDetails = sessionManager.getUser(activity);
                        userDetails.setBITCOIN_ADDRESS(Pay_Address);
                        userDetails.setBITCOIN_LABEL(label);
                        userDetails.setBITCOIN_USERID(user_id);
                        sessionManager.setUser(getApplicationContext(), userDetails);
                        address = Pay_Address;
                        updateUserPaymentData();
                        Log.d("get_data", network + " | " + user_id + " | " + address + " | " + label);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 404) {
                    Toast.makeText(activity, "Setup could not configure on this ...", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("api_key", BIT_KEY);
                param.put("label", BITCOIN_ADDRESS);
                Log.d("param_wallet", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(AddCashActivity.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

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
        tv_DTxAmount.setText(" " + TxAmount);


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
                        // callJoinContest(true);
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

}
