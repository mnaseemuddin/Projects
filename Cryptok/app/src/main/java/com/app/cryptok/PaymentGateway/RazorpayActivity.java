package com.app.cryptok.PaymentGateway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.activity.PurchaseCoinsActivity;
import com.app.cryptok.databinding.ActivityRazorpayBinding;
import com.app.cryptok.model.Packages.PackageModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {


    private ActivityRazorpayBinding binding;
    private RazorpayActivity activity;
    private Context context;

    private PackageModel packageModel;
    private double SELECTED_AMOUNT;
    private int SELECTED_COINS;
    private String selected_package_id = "", PAYMENT_RESPONSE = "", selected_package_name = "", ORDER_ID = "";
    private ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    private FirebaseFirestore firebaseFirestore;

    private String razorpay_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_razorpay);
        context = activity = this;

        getInfo();
        handleClick();

    }

    private void getInfo() {
        sessionManager = new SessionManager(context);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (getIntent().hasExtra(DBConstants.DIAMONDS_PACKAGE_JSON)) {
            String json = getIntent().getStringExtra(DBConstants.DIAMONDS_PACKAGE_JSON);
            Commn.showDebugLog("recieved:" + json);
            packageModel = new Gson().fromJson(json, PackageModel.class);
            SELECTED_AMOUNT = Double.parseDouble(packageModel.getAmount());
            SELECTED_COINS = Integer.parseInt(packageModel.getTotal_coins());
            selected_package_id = packageModel.getTbl_package_id();
            selected_package_name = packageModel.getPackage_name();

        }
        Checkout.preload(getApplicationContext());

        firebaseFirestore.collection(DBConstants.Payment_Info)
                .document(DBConstants.payment_info_key)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().exists()) {
                            if (task.getResult().getString(DBConstants.razorpay_key) != null) {
                                razorpay_key = task.getResult().getString(DBConstants.razorpay_key);
                                Commn.showDebugLog("payment_key:" + razorpay_key);
                            }

                        }
                    }
                });


    }

    private void handleClick() {

        binding.llGoogleWallet.setOnClickListener(view -> {

            if (razorpay_key != null || !razorpay_key.equals("")) {
                inializeGateway();
            }

        });
        binding.llCard.setOnClickListener(view -> {

            if (razorpay_key != null || !razorpay_key.equals("")) {
                inializeGateway();
            }
        });

        binding.llUpi.setOnClickListener(view -> {

            if (razorpay_key != null || !razorpay_key.equals("")) {
                inializeGateway();
            }
        });

        binding.llWallet.setOnClickListener(view -> {

            if (razorpay_key != null || !razorpay_key.equals("")) {
                inializeGateway();
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void inializeGateway() {
        Checkout checkout = new Checkout();
        checkout.setKeyID(razorpay_key);
        checkout.setImage(R.drawable.app_logo);
        try {
            JSONObject options = new JSONObject();
            options.put("name", context.getString(R.string.app_name));
            options.put("description", context.getString(R.string.app_name));
            options.put("currency", "INR");
            options.put("amount", SELECTED_AMOUNT * 100);//pass amount in currency subunits
            checkout.open(activity, options);
        } catch (Exception e) {
            Commn.showDebugLog("Error in starting Razorpay Checkout" + e.getMessage() + "");
        }
    }

    private void addCoins() {

        //check user exists
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        //getting current coins
                        firebaseFirestore.collection(DBConstants.UserInfo
                                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                                .document(profilePOJO.getUserId())
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.getResult().exists()) {
                                        int current_coins = task1.getResult().getLong(DBConstants.current_coins).intValue();
                                        Map<String, Object> current_coins_map = new HashMap<>();
                                        //adding coins in current coins
                                        int toAdd = getFinalCoins(current_coins, SELECTED_COINS);
                                        current_coins_map.put(DBConstants.current_coins, toAdd);
                                        firebaseFirestore.collection(DBConstants.UserInfo + "/" + profilePOJO.getUserId()
                                                + "/" + DBConstants.User_Coins_Info)
                                                .document(profilePOJO.getUserId())
                                                .update(current_coins_map)
                                                .addOnCompleteListener(task2 -> {
                                                    updateHistory();
                                                    Commn.showDebugLog("updated_purchased_coins:" + toAdd + "");
                                                });
                                    } else {

                                        Map<String, Object> current_coins_map = new HashMap<>();
                                        //adding coins in current coins
                                        current_coins_map.put(DBConstants.current_coins, SELECTED_COINS);
                                        current_coins_map.put(DBConstants.current_recieved_beans, 0);
                                        firebaseFirestore.collection(DBConstants.UserInfo + "/" + profilePOJO.getUserId()
                                                + "/" + DBConstants.User_Coins_Info)
                                                .document(profilePOJO.getUserId())
                                                .set(current_coins_map)
                                                .addOnCompleteListener(task2 -> {
                                                    updateHistory();
                                                    Commn.showDebugLog("added_purchased_coins:" + SELECTED_COINS + "");
                                                });
                                    }


                                });


                    }

                });


    }

    private void updateHistory() {

        Map<String, Object> coins_history_map = new HashMap<>();
        coins_history_map.put(DBConstants.coins_purchased, String.valueOf(SELECTED_COINS));
        coins_history_map.put(DBConstants.coins_purchased_time, FieldValue.serverTimestamp());
        coins_history_map.put(DBConstants.coins_purchased_amount, String.valueOf(SELECTED_AMOUNT));
        coins_history_map.put(DBConstants.package_id, selected_package_id);
        coins_history_map.put(DBConstants.payment_response, PAYMENT_RESPONSE);
        coins_history_map.put(DBConstants.package_name, selected_package_name);

        CollectionReference collectionReference = firebaseFirestore.collection(DBConstants.UserInfo +
                "/" + profilePOJO.getUserId() +
                "/" + DBConstants.User_Coins_Purchase_History);
        DocumentReference Id_Ref = collectionReference.document();
        String doc_id = Id_Ref.getId();
        coins_history_map.put(DBConstants.history_id, doc_id);
        collectionReference.document(doc_id)
                .set(coins_history_map)
                .addOnCompleteListener(task -> {
                    onCoinsAdded();
                    Commn.showDebugLog("history_added successfully in user_id:"
                            + profilePOJO.getUserId() + ",history_data=" + new Gson().toJson(coins_history_map) + "");
                });
        Commn.hideProgress();

    }

    private void onCoinsAdded() {
        final SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);

        dialog.setTitleText("Coins Added !")
                .setContentText("Coins Added Successfully ! Enjoy Service")
                .show();
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            dialog.dismiss();
            startActivity(new Intent(context, PurchaseCoinsActivity.class));
            finish();
        });
    }

    private int getFinalCoins(int current_coins, int ToAddCoins) {
        return current_coins + ToAddCoins;
    }

    private void updatePaymentError(int payment_code) {
        Map<String, Object> coins_history_map = new HashMap<>();
        coins_history_map.put(DBConstants.coins_purchased, String.valueOf(SELECTED_COINS) + "");
        coins_history_map.put(DBConstants.coins_purchased_time, FieldValue.serverTimestamp());
        coins_history_map.put(DBConstants.coins_purchased_amount, String.valueOf(SELECTED_AMOUNT) + "");
        coins_history_map.put(DBConstants.package_id, selected_package_id + "");
        coins_history_map.put(DBConstants.payment_response, PAYMENT_RESPONSE + "");
        coins_history_map.put(DBConstants.payment_code, String.valueOf(payment_code) + "");
        coins_history_map.put(DBConstants.package_name, selected_package_name + "");

        CollectionReference collectionReference = firebaseFirestore.collection(DBConstants.UserInfo +
                "/" + profilePOJO.getUserId() +
                "/" + DBConstants.User_Coins_Purchase_Errors);
        DocumentReference Id_Ref = collectionReference.document();
        String doc_id = Id_Ref.getId();
        coins_history_map.put(DBConstants.history_id, doc_id);
        collectionReference.document(doc_id)
                .set(coins_history_map)
                .addOnCompleteListener(task -> {
                    Commn.hideProgress();
                    Commn.showDebugLog("history_added successfully in user_id:"
                            + profilePOJO.getUserId() + ",history_data=" + new Gson().toJson(coins_history_map) + "");
                });
    }

    @Override
    public void onPaymentSuccess(String payment_response) {

        Commn.showDebugLog("onPaymentSuccess:" + payment_response + "");

        PAYMENT_RESPONSE = payment_response;
        Commn.showProgress(context);
        addCoins();
    }


    @Override
    public void onPaymentError(int payment_code, String payment_response) {
        Commn.showDebugLog("onPaymentError:" + payment_response + "");
        PAYMENT_RESPONSE = payment_response;
        updatePaymentError(payment_code);
        Commn.myToast(context, payment_response + "");
    }


}