package com.app.cryptok.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.Interface.Packages.ExchangeDiamondsListener;
import com.app.cryptok.R;
import com.app.cryptok.adapter.Packages.ExchangeDiamondsAdapter;
import com.app.cryptok.databinding.ActivityExchangeDiamondsBinding;
import com.app.cryptok.model.Packages.ExchangeDiamondModel;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.razorpay.Checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExchangeDiamondsActivity extends AppCompatActivity implements ExchangeDiamondsListener {

    private ActivityExchangeDiamondsBinding binding;
    ProfilePOJO profilePOJO;
    SessionManager sessionManager;
    private FirebaseFirestore firebaseFirestore;
    private Context context;
    private ExchangeDiamondsActivity activity;
    private List<ExchangeDiamondModel> mList = new ArrayList<>();
    ExchangeDiamondsAdapter adapter;
    private ExchangeDiamondsListener diamondsListener;
    private int current_recieved_beans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exchange_diamonds);
        context = activity = this;

        diamondsListener = this;
        getInfo();
        getExchangeDiamondsList();
        handleClick();

    }

    private void handleClick() {

        binding.ivBack.setOnClickListener(view -> onBackPressed());

    }

    private void getInfo() {
        sessionManager = new SessionManager(context);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getCurrentCoins();
        Checkout.preload(getApplicationContext());
    }

    private void getCurrentCoins() {
        firebaseFirestore.collection(DBConstants.UserInfo
                + "/" + profilePOJO.getUserId() + "/" + DBConstants.User_Coins_Info)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {
                    if (value.exists()) {

                        current_recieved_beans = value.getLong(DBConstants.current_recieved_beans).intValue();
                        Commn.showDebugLog("current_beans:" + current_recieved_beans + "");
                        binding.tvCurrentBeans.setText(String.valueOf(current_recieved_beans));
                    } else {
                        Commn.showDebugLog("current_beans:" + "0" + "");
                        binding.tvCurrentBeans.setText("0");
                    }
                });
    }

    private void getExchangeDiamondsList() {

        mList.clear();
        iniAdapter();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MyApi.exchange_diamond_list_api, response -> {

            binding.progressBar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(response);
                Commn.showDebugLog("packages_list_api:" + response.toString() + "");
                String status = jsonObject.getString("status");
                if ("1".equalsIgnoreCase(status)) {

                    JSONArray jsonArray = jsonObject.getJSONArray("package_data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String tbl_exchange_diamond_id = jsonObject1.getString("tbl_exchange_diamond_id");
                            String beans = jsonObject1.getString("beans");
                            String diamonds = jsonObject1.getString("diamonds");
                            mList.add(new ExchangeDiamondModel(tbl_exchange_diamond_id, beans, diamonds));

                        }
                        adapter.notifyDataSetChanged();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            binding.progressBar.setVisibility(View.GONE);

        });
        Commn.requestQueue(getApplicationContext(), stringRequest);

    }

    private void iniAdapter() {
        adapter = new ExchangeDiamondsAdapter(mList, context, this);
        binding.rvExchangeDiamonds.setAdapter(adapter);

    }

    @Override
    public void onExchangeClick(ExchangeDiamondModel model) {
        Commn.showDebugLog("selected_exchange_offers:" + new Gson().toJson(model) + "");

        if (Integer.parseInt(model.getBeans()) <= current_recieved_beans) {

            Commn.showProgress(context);
            exchangeDiamonds(model);
        } else {
            lowBeans();

        }


    }

    private void lowBeans() {
        final SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);

        dialog.setTitleText("Low Beans !")
                .setContentText("you have less beans to exchange this offer")
                .show();
        dialog.setConfirmClickListener(sweetAlertDialog -> dialog.dismiss());
    }

    private void exchangeDiamonds(ExchangeDiamondModel model) {
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
                                        int current_coins = task1.getResult().getLong(DBConstants.current_coins).intValue();//current diamonds

                                        Map<String, Object> current_coins_map = new HashMap<>();
                                        //deducting coins from current coins
                                        int finalDiamonds = getFinalDiamonds(current_coins, Integer.parseInt(model.getDiamonds()));
                                        current_coins_map.put(DBConstants.current_coins, finalDiamonds);
                                        firebaseFirestore.collection(DBConstants.UserInfo + "/" + profilePOJO.getUserId()
                                                + "/" + DBConstants.User_Coins_Info)
                                                .document(profilePOJO.getUserId())
                                                .update(current_coins_map)
                                                .addOnCompleteListener(task2 -> {

                                                    deductBeans(model.getBeans());
                                                    Commn.showDebugLog("exchages_Diamonds_successfully:itne add krne they=>" +
                                                            finalDiamonds + ",itne m=>" + current_coins + "");
                                                });
                                    }

                                });
                    }

                });
    }

    private void deductBeans(String beans) {
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
                                        int current_recieved_beans = task1.getResult().getLong(DBConstants.current_recieved_beans).intValue();//current diamonds
                                        if (current_recieved_beans > 0) {
                                            Map<String, Object> current_coins_map = new HashMap<>();
                                            //deducting coins from current coins
                                            int finalBeans = getFinalBeans(current_recieved_beans, Integer.parseInt(beans));
                                            current_coins_map.put(DBConstants.current_recieved_beans, finalBeans);
                                            firebaseFirestore.collection(DBConstants.UserInfo + "/" + profilePOJO.getUserId()
                                                    + "/" + DBConstants.User_Coins_Info)
                                                    .document(profilePOJO.getUserId())
                                                    .update(current_coins_map)
                                                    .addOnCompleteListener(task2 -> {
                                                        Commn.hideProgress();
                                                        showExchangeDialog();
                                                        Commn.showDebugLog("beans deduction successfully:itne deduct krne they=>" +
                                                                beans + ",itne m se=>" + current_recieved_beans + "");
                                                    });
                                        }

                                    }

                                });
                    }

                });
    }

    private void showExchangeDialog() {

        final SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);

        dialog.setTitleText("Diamond Exchanged !")
                .setContentText("Diamond Exchanged ! Enjoy Service")
                .show();
        dialog.setConfirmClickListener(sweetAlertDialog -> dialog.dismiss());
    }

    private int getFinalBeans(int current_coins, int deduction_beans) {
        return current_coins - deduction_beans;
    }

    private int getFinalDiamonds(int current_diamonds, int toAddDiamonds) {

        return current_diamonds + toAddDiamonds;

    }
}