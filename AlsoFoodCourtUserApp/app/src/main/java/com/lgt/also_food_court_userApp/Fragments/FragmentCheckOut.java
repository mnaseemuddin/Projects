package com.lgt.also_food_court_userApp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.lgt.also_food_court_userApp.Activities.ActivityOrderReview;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.Common;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.extra.SingletonVolley;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.lgt.also_food_court_userApp.Activities.ActivityOrderReview.Total_bag_price;
import static com.lgt.also_food_court_userApp.Activities.ActivityOrderReview.mobileNo;
import static com.lgt.also_food_court_userApp.Activities.ActivityOrderReview.products_name;


public class FragmentCheckOut extends Fragment {

    private static final String ALLOWED_CHARACTERS ="0123456789JHDSGSMWDPOUMMMSMNHVKFGKZZZKHqwertyuiopasdfghjklzxcvbnm";


public static String txnid;

   // PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;

  



    private Button btnPay;
    private RadioGroup radioGroup_Purchase;

    private ImageView iv_back_checkout;

    private RelativeLayout rl_checkOut;
    private TextView tv_cartAmount;

    private String mUserID, LatitudeCode, LogitudeCode,UName;
    private SharedPreferences sharedPreferences;



    public FragmentCheckOut() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frgment_check_out, container, false);
        

        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("user_id")) {
            mUserID = sharedPreferences.getString("user_id", "");
        }

        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("name")) {
            UName = sharedPreferences.getString("name", "");
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(Common.latitude)) {
                LatitudeCode = bundle.getString(Common.latitude);
                LogitudeCode = bundle.getString(Common.logitude);


                Log.e("qwerds", LatitudeCode + "");
                Log.e("mjnhyui", LogitudeCode + "");
            }
        }


        btnPay = view.findViewById(R.id.btnPay);
        radioGroup_Purchase = view.findViewById(R.id.radioGroup_Purchase);
        iv_back_checkout = view.findViewById(R.id.iv_back_checkout);
        tv_cartAmount = view.findViewById(R.id.tv_cartAmount);
        rl_checkOut = view.findViewById(R.id.rl_checkOut);


        iv_back_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack("Fragment_CheckOut", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        if (Total_bag_price != null) {
            if (!Total_bag_price.equals("")) {
                tv_cartAmount.setText(Total_bag_price);
            }
        }


        rl_checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
                Log.e("fjgh","OnPressed");

            }
        });


        Log.e("kikjkjoiuiouo", Total_bag_price + "");

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });


        //Random generator = new Random();
       // String x = String.valueOf (generator.nextInt(9645662) + 3244578);

        //Log.e("mfgklfhgj",x+"");

        getRandomString(8);
        return view;
    }


    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
         txnid=sb.toString();
        Log.e("dkjfjb",txnid+"");
        return sb.toString();



    }




    private void apiForCheckOut() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.CONFIRM_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("dsadsahdjkhdjkhdjkahd", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("1")) {

                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();

                        JSONObject object = jsonObject.getJSONObject("data");
                        String order_number = object.getString("order_number");
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                       /* Fragment fragmentPaymentConfirmation = new FragmentPaymentConfirmation();
                        Bundle bundle = new Bundle();
                        bundle.putString("ORDER_ID", order_number);
                        fragmentPaymentConfirmation.setArguments(bundle);
                        fragmentTransaction.replace(R.id.frameOrderReview, fragmentPaymentConfirmation);
                        fragmentTransaction.addToBackStack("Fragment_Pay");
                        fragmentTransaction.commit();*/


                    } else {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_addressbook_id", ActivityOrderReview.address_id);
                params.put("user_id", mUserID);
                params.put("payment_type", "Testing");
                Log.e("gtfrdesw", ActivityOrderReview.address_id + "");
                Log.e("kouiouiouiouiou", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = SingletonVolley.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void validation() {

        switch (radioGroup_Purchase.getCheckedRadioButtonId()) {


            case R.id.radio_CashOnDelivery: {
                apiForCheckOut();

            }
            break;
            case R.id.radio_Online: {
                startpay();
                Log.e("mdkfn","RAza");
            }
            break;
        }
    }

    public void startpay(){

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        builder.setAmount(Total_bag_price)                          // Payment amount
                .setTxnId(txnid)                     // Transaction ID
                .setPhone(mobileNo)                   // User Phone number
                .setProductName(products_name)                   // Product Name or description
                .setFirstName(UName)                       // User First name
                .setEmail("aqw@gmail.com")              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(Common.MerchantKEy)                        // Merchant key
                .setMerchantId(Common.MerchantID);

        Log.e("kghlfml","WERRRRF");
        try {
            paymentParam = builder.build();
            paymentParam = calculateServerSideHashAndInitiatePayment1(paymentParam);

            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, getActivity(), R.style.AppTheme_default, true);

            // generateHashFromServer(paymentParam );
          //  getHashkey();
            Log.e("djfk","GSDHfh");

        } catch (Exception e) {
            Log.e(TAG, " error s "+e.toString());
        }

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

        // AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        stringBuilder.append(Common.MerchantSALTKEy);

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
// Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {

                   // String payuResponse = transactionResponse.getPayuResponse();

                    Log.e("kdfghg","qasim");
                    try {
                        //CallSuccessApi
                        apiForCheckOut();
                        Log.e("jfgklrh","qasim");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Success Transaction
                } else {
                   // OrderFailedApi();

                }





                //payuMoneyresponse

                String merchantResponse = transactionResponse.getTransactionDetails();


            }


        }else{
          //  OrderFailedApi();
            //Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        }










    }



    /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // PayUMoneySdk: Success -- payuResponse{"id":225642,"mode":"CC","status":"success","unmappedstatus":"captured","key":"9yrcMzso","txnid":"223013","transaction_fee":"20.00","amount":"20.00","cardCategory":"domestic","discount":"0.00","addedon":"2018-12-31 09:09:43","productinfo":"a2z shop","firstname":"kamal","email":"kamal.bunkar07@gmail.com","phone":"9144040888","hash":"b22172fcc0ab6dbc0a52925ebbd0297cca6793328a8dd1e61ef510b9545d9c851600fdbdc985960f803412c49e4faa56968b3e70c67fe62eaed7cecacdfdb5b3","field1":"562178","field2":"823386","field3":"2061","field4":"MC","field5":"167227964249","field6":"00","field7":"0","field8":"3DS","field9":" Verification of Secure Hash Failed: E700 -- Approved -- Transaction Successful -- Unable to be determined--E000","payment_source":"payu","PG_TYPE":"AXISPG","bank_ref_no":"562178","ibibo_code":"VISA","error_code":"E000","Error_Message":"No Error","name_on_card":"payu","card_no":"401200XXXXXX1112","is_seamless":1,"surl":"https://www.payumoney.com/sandbox/payment/postBackParam.do","furl":"https://www.payumoney.com/sandbox/payment/postBackParam.do"}
        Log.e("mgfkldjgkf","qasim");
        // Result Code is -1 send from Payumoney activity
        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra( PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if(transactionResponse.getTransactionStatus().equals( TransactionResponse.TransactionStatus.SUCCESSFUL )){
                    //Success Transaction
                    apiForCheckOut();
                    Log.e("jfgkhfgj","Order Success");
                } else{
                    //Failure Transaction
                    Toast.makeText(getActivity(), "Your Payment is Failed Try Again Payment", Toast.LENGTH_SHORT).show();
                    Log.e("itorhihklsk","Order Failed");
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Log.e(TAG, "tran "+payuResponse+"---"+ merchantResponse);
            } *//* else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*//*
        }
    }*/
}


