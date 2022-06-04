package com.lgt.fxtradingleague.RazorPayments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.SessionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.lgt.fxtradingleague.Extra.ExtraData.Add_Amount_API_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.PAYMENT_KEY_ID;

public class AddPaymentActivity extends AppCompatActivity implements PaymentResultListener {
    ProgressDialog loading = null;
    AddPaymentActivity activity;
    Context context;
    private int mAmt = 0;
    ImageView im_back,iv_transaction_history;
    TextView tv_HeaderName,tv_Checkout;
    EditText et_AddAmt;
    SessionManager sessionManager;
    JSONObject RazorpayResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_layout);
        initView();
    }

    private void initView() {
        context = activity = this;
        Checkout.preload(getApplicationContext());
        sessionManager = new SessionManager();

        im_back = findViewById(R.id.im_back);
        et_AddAmt = findViewById(R.id.et_AddAmt);
        tv_Checkout = findViewById(R.id.tv_Checkout);
        iv_transaction_history = findViewById(R.id.iv_transaction_history);
        tv_HeaderName = findViewById(R.id.tv_HeaderName);

        loading = new ProgressDialog(this);
        loading.setCancelable(true);
        loading.setMessage("Please Wait....");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        clickEvent();
    }

    @SuppressLint("SetTextI18n")
    private void clickEvent() {
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        iv_transaction_history.setVisibility(View.VISIBLE);
        tv_HeaderName.setText("Add Payment");
        tv_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPayment();
            }
        });
    }

    private void addPayment() {
        if (et_AddAmt.getText().toString().trim().equals("")){
            Toast.makeText(this, "Enter Amount ", Toast.LENGTH_SHORT).show();
        }else{
            mAmt = Integer.parseInt(et_AddAmt.getText().toString().trim());
            if (mAmt > 1){
                addRozorPayPayment();
            }else{
                Toast.makeText(this, "Amount Should Be Grater then 5", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addRozorPayPayment() {
        loading.show();
        startPayment();
    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        // merchant id
        checkout.setKeyID(PAYMENT_KEY_ID);

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.rzp_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */

        try {
            JSONObject options = new JSONObject();
            options.put("name", "mName");
            options.put("description", "Shopping Charges");

            // options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("order_id", "s2s4ffa87JOWzybf0sJbb");//from response of step 3.
            // options.put("theme.color", "#3399cc");

            Double paybleAmount = Double.parseDouble(String.valueOf(mAmt));
            Log.d("Pay_able_Amount", paybleAmount + "");
            options.put("currency", "INR");
            options.put("amount", (paybleAmount.intValue() * 100) + "");

            //pass amount in currency subunits
            JSONObject preFill = new JSONObject();

            preFill.put("email", sessionManager.getUser(context).getEmail());
            preFill.put("contact", sessionManager.getUser(context).getMobile());
            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    private void callJoinContest(final String status ,final String resp) {
        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Add_Amount_API_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Log.d("joinContest", "Response : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("1")){
                        Toast.makeText(AddPaymentActivity.this, "", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(AddPaymentActivity.this, "", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.d("joinContest", "Error : " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", sessionManager.getUser(context).getUser_id());
                param.put("amount", ""+mAmt);
                param.put("getway_status", status);
                param.put("getway_response", resp);
                Log.d("joinContest", "Data : " + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onPaymentSuccess(String s) {
        loading.dismiss();
        Log.d("onPaymentSuccess",": "+s);
        callJoinContest("SUCCESS",s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        loading.dismiss();
        Log.d("onPaymentError",i+", "+s);
        callJoinContest("FAILED",s);
    }

}
