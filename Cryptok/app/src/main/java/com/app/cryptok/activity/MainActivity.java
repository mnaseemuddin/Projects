package com.app.cryptok.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.LiveShopping.Activities.LiveShoppingPreviewActivity;
import com.app.cryptok.LiveShopping.Model.LiveShoppingModel;
import com.app.cryptok.R;
import com.app.cryptok.adapter.MainViewPagerAdapter;
import com.app.cryptok.databinding.ActivityMainBinding;
import com.app.cryptok.databinding.BtnAddLytBinding;
import com.app.cryptok.go_live_module.GoLiveActivity;
import com.app.cryptok.go_live_module.LiveConst;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.utils.UpdateHelper;
import com.app.cryptok.view_live_module.HotLiveModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import am.appwise.components.ni.NoInternetDialog;


import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class MainActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener {
    // Permission request code of any integer value
    private static final int PERMISSION_REQ_CODE = 1 << 4;
    private String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    ProfilePOJO profilePOJO;
    private SessionManager sessionManager;
    private FirebaseFirestore firebaseFirestore;
    private ActivityMainBinding binding;
    private MainActivity activity;
    private Context context;
    //daily checking
    private AlertDialog daily_checkin_dialog;
    private Button bt_check_in;
    NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        context = activity = this;
        iniFirebase();
        setBottom();
        checkUser();
        updateVersionApp();
    }

    private void updateVersionApp() {
        if (activity != null) {
            UpdateHelper.with(context)
                    .onUpdateCheck(this)
                    .check();
        }

    }

    @Override
    public void onUpdateCheckListener(final String app_url, final String update_title, final String update_desc) {

        firebaseFirestore.collection(DBConstants.AppInfo)
                .document("1")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot == null) {
                        return;
                    }
                    if (documentSnapshot.exists()) {
                        if (documentSnapshot.getBoolean("enable_popup") != null) {
                            boolean isEnabledPopup = documentSnapshot.getBoolean("enable_popup");
                            if (isEnabledPopup) {
                                updatePopup(app_url, update_title, update_desc);
                            }
                        } else {
                            updatePopup(app_url, update_title, update_desc);
                        }
                    } else {
                        updatePopup(app_url, update_title, update_desc);
                    }
                });


    }

    private void updatePopup(String app_url, String update_title, String update_desc) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.app_update_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tv_whatsnew = dialog.findViewById(R.id.tv_whatsnew);
        TextView tv_update = dialog.findViewById(R.id.tv_update);
        tv_update.setText(update_title);
        tv_whatsnew.setText(update_desc);
        Button bt_update = dialog.findViewById(R.id.bt_update);
        TextView tv_ignore_update = dialog.findViewById(R.id.tv_ignore_update);
        tv_ignore_update.setOnClickListener(view -> {
            //dialog.dismiss();
            Commn.myToast(context, "This update is mandatory");
        });
        dialog.setOnDismissListener(dialogInterface -> {
            Commn.myToast(context, "this update is mandatory...");
            System.exit(1);
        });
        bt_update.setOnClickListener(view -> {
            try {
                Uri webpage = Uri.parse(app_url);
                Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Commn.myToast(context, "Try Again...");
                e.printStackTrace();
            }

        });
    }

    private void iniFirebase() {
        if (context != null) {
            noInternetDialog = new NoInternetDialog.Builder(context).build();
        }

        sessionManager = new SessionManager(activity);
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        firebaseFirestore = FirebaseFirestore.getInstance();

    }


    private void checkUser() {
        String user_id;

        if (profilePOJO != null) {
            if (profilePOJO.getUserId() == null) {
                user_id = "temp123";
            } else {
                user_id = profilePOJO.getUserId();
            }
        } else {
            user_id = "temp123";
        }

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(user_id)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        if (value.exists()) {

                            updateToken();
                            checkDailyCheckin();
                            openShareable();
                            authChatSdk();
                        } else {
                            logoutFromApp(MainActivity.this);
                        }
                    }
                });

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(user_id)
                .addSnapshotListener((value, error) -> {
                    if (value == null) {
                        return;
                    }
                    if (value.exists()) {
                        if (value.getString(DBConstants.auth_status) != null) {
                            String status = value.getString(DBConstants.auth_status);
                            if ("false".equalsIgnoreCase(status)) {
                                logoutFromApp(MainActivity.this);
                            }
                        }
                    }
                });
    }

    private void authChatSdk() {

    }

    private void openShareable() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            String link = deepLink.toString();
                            try {
                                String main_id = link.substring(link.lastIndexOf("=") + 1);
                                String user_id = main_id.substring(0, main_id.indexOf("-"));
                                String type = main_id.substring(main_id.indexOf("-") + 1);
                                Commn.showDebugLog("recieve link:" + link + ",user_id:" + user_id + ",type=" + type);
                                if (DBConstants.LiveShopping.equalsIgnoreCase(type)) {
                                    getLiveShopping(user_id);
                                }
                                if (Commn.LIVE_TYPE.equalsIgnoreCase(type)) {
                                    getLiveStream(user_id);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Commn.showErrorLog("getDynamicLink:onFailure" + e.getMessage() + "");
                    }
                });
    }

    private void getLiveStream(String user_id) {
        firebaseFirestore.collection(DBConstants.Single_Streams)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().exists()) {

                            HotLiveModel model = task.getResult().toObject(HotLiveModel.class);
                            Intent intent = new Intent(context, SingleLiveStreamPreview.class);
                            intent.putExtra(LiveConst.STREAM_MODEL, new Gson().toJson(model));
                            startActivity(intent);
                            finish();
                        } else {
                            Commn.myToast(context, "Broadcast Ended");

                        }
                    } else {
                        Commn.myToast(context, "Broadcast Ended");

                    }

                });
    }

    private void getLiveShopping(String user_id) {
        firebaseFirestore.collection(DBConstants.LiveShopping)
                .document(user_id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().exists()) {
                            LiveShoppingModel model = task.getResult().toObject(LiveShoppingModel.class);
                            Intent intent = new Intent(context, LiveShoppingPreviewActivity.class);
                            intent.putExtra(DBConstants.LiveShoppingModel, new Gson().toJson(model));
                            startActivity(intent);
                            finish();
                        } else {
                            Commn.myToast(context, "Broadcast Ended");

                        }
                    } else {
                        Commn.myToast(context, "Broadcast Ended");

                    }

                });
    }


    private void checkDailyCheckin() {
        firebaseFirestore.collection(DBConstants.Daily_Checkin)
                .document(profilePOJO.getUserId())
                .addSnapshotListener((value, error) -> {
                    if (value.exists()) {

                        //  Commn.showErrorLog("now user available to daily checkin...");
                        Timestamp serverTimestamp = value.getTimestamp(DBConstants.timestamp);
                        if (serverTimestamp != null) {
                            if (Commn.getTime(System.currentTimeMillis()).equalsIgnoreCase(getDate(serverTimestamp.getSeconds(),
                                    serverTimestamp.getNanoseconds()))) {
                                binding.dailyChecking.setVisibility(View.GONE);
                            } else {
                                binding.dailyChecking.setVisibility(View.VISIBLE);
                            }
                            Commn.showErrorLog("last dalily checkin timestamp:" + "server original timestamp :" + serverTimestamp.toDate()
                                    + "current_date:" + Commn.getTime(System.currentTimeMillis()) + "," +
                                    "server timestamp date:" + getDate(serverTimestamp.getSeconds(),
                                    serverTimestamp.getNanoseconds()) + "");
                        }


                    } else {
                        binding.dailyChecking.setVisibility(View.VISIBLE);
                    }
                });

    }

    public String getDate(long mili, int nano) {
        long milliseconds = mili * 1000 + nano / 1000000;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date netDate = new Date(milliseconds);
        String date = sdf.format(netDate).toString();
        return date;
    }

    private void updateDailyCheckin() {

        Map<String, Object> map = new HashMap<>();
        map.put(DBConstants.timestamp, FieldValue.serverTimestamp());
        map.put(DBConstants.user_id, profilePOJO.getUserId());
        firebaseFirestore.collection(DBConstants.Daily_Checkin)
                .document(profilePOJO.getUserId())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Commn.showDebugLog("daily checking updated...");
                        addDailyCheckinPoints();
                    }
                });
    }

    int checkin_points = 20;

    private void addDailyCheckinPoints() {
        if (sessionManager.getPoints() != null) {
            checkin_points = sessionManager.getPoints().getCheckin_points();
        } else {
            checkin_points = 20;
        }
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(DBConstants.point, Objects.requireNonNull(task.getResult().getLong(DBConstants.point)).intValue()
                                + checkin_points);
                        firebaseFirestore.collection(DBConstants.UserInfo)
                                .document(profilePOJO.getUserId())
                                .update(map)
                                .addOnCompleteListener(task1 -> {
                                    hideCheckinDialog();
                                    if (task1.isSuccessful()) {

                                        binding.dailyChecking.setVisibility(View.GONE);
                                        bt_check_in.setEnabled(false);
                                        Commn.hideDialog(activity);

                                        Commn.showDebugLog("daily checkin points  updated success:");

                                    } else {
                                        Commn.showDebugLog("daily checkin points not updated:");
                                    }

                                });

                    }

                });
        Commn.hideDialog(activity);

    }

    private void hideCheckinDialog() {

        if (daily_checkin_dialog != null) {
            if (daily_checkin_dialog.isShowing()) {
                daily_checkin_dialog.dismiss();
            }
        }
    }


    private void updateToken() {

        if (profilePOJO != null) {

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Commn.showDebugLog("Fetching FCM registration token failed:" + task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Commn.showDebugLog("success to get token:" + token);
                        Map<String, Object> map = new HashMap<>();
                        map.put(DBConstants.user_token, token);
                        firebaseFirestore.collection(DBConstants.UserInfo)
                                .document(profilePOJO.getUserId())
                                .collection(DBConstants.Tokens)
                                .document(profilePOJO.getUserId())
                                .set(map)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Commn.showDebugLog("token updated:....");
                                    } else {
                                        Commn.showDebugLog("token updation failure");
                                    }

                                });
                    });


        }

    }


    private void setBottom() {
        initTabLayout();
        for (int i = 0; i <= 4; i++) {
            switch (i) {
                case 0:
                    binding.tabLout.addTab(binding.tabLout.newTab().setIcon(R.drawable.home));
                    TabLayout.Tab tab = binding.tabLout.getTabAt(0);
                    if (tab != null && tab.getIcon() != null) {
                        tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                    }
                    break;
                case 1:
                    binding.tabLout.addTab(binding.tabLout.newTab().setIcon(R.drawable.search));
                    break;
                case 2:
                    BtnAddLytBinding addLytBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.btn_add_lyt, null, false);
                    binding.tabLout.addTab(binding.tabLout.newTab().setCustomView(addLytBinding.getRoot()));
                    break;
                case 3:
                    binding.tabLout.addTab(binding.tabLout.newTab().setIcon(R.drawable.camera));
                    break;
                case 4:
                    binding.tabLout.addTab(binding.tabLout.newTab().setIcon(R.drawable.profile_icon));
                    break;
            }
        }

        binding.tabLout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab != null) {
                    Commn.showDebugLog("onTabSelected:" + tab.getPosition());
                    switch (tab.getPosition()) {
                        case 0:
                            binding.viewPager.setCurrentItem(0);
                            tab.setIcon(R.drawable.home);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                            break;
                        case 1:

                            binding.viewPager.setCurrentItem(1);
                            tab.setIcon(R.drawable.search);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                            break;
                        case 2:
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                            startActivity(new Intent(context, GoLiveActivity.class));
                            break;
                        case 3:
                            binding.viewPager.setCurrentItem(2);
                            tab.setIcon(R.drawable.camera);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));

                            break;
                        case 4:
                            binding.viewPager.setCurrentItem(3);
                            tab.setIcon(R.drawable.profile_icon);
                            if (tab.getIcon() != null)
                                tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));


                            break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Commn.showDebugLog("onTabUnselected:" + tab.getPosition());
                switch (tab.getPosition()) {

                    case 0:
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.dark_gray));

                        break;
                    case 1:
                        tab.setIcon(R.drawable.search);
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.dark_gray));
                        break;
                    case 3:
                        tab.setIcon(R.drawable.camera);
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.dark_gray));
                        break;
                    case 4:
                        tab.setIcon(R.drawable.profile_icon);
                        if (tab.getIcon() != null)
                            tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color.dark_gray));
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    if (!isFirstClick) {
                        startActivity(new Intent(context, GoLiveActivity.class));
                        isFirstClick = true;
                    } else {
                        isFirstClick = false;
                    }
                }
            }
        });

        checkPermission();


        binding.dailyChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDailyCheckinDialog();
            }
        });


    }

    Boolean isFirstClick = false;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isFirstClick = false;
    }

    private void initTabLayout() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(4);
    }

    private void showDailyCheckinDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.daily_checking_layout, null, false);

        daily_checkin_dialog = new AlertDialog.Builder(this).create();
        daily_checkin_dialog.setView(view);
        daily_checkin_dialog.setCanceledOnTouchOutside(false);

        bt_check_in = view.findViewById(R.id.bt_check_in);
        bt_check_in.setOnClickListener(view1 -> {
            Commn.showDialog(activity);
            updateCheckinPoints();
        });

        daily_checkin_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        daily_checkin_dialog.show();
    }

    private void updateCheckinPoints() {

        updateDailyCheckin();

    }

    public static void logoutFromApp(Activity context) {

        Commn.myToast(context, "You are no longer with us...");
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.clearData();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finishAffinity();
        // Intent intent=new Intent(context,)

    }

    private void checkPermission() {
        boolean granted = true;
        for (String per : PERMISSIONS) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            resetLayoutAndForward();
        } else {
            requestPermissions();
        }
    }

    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                resetLayoutAndForward();
            } else {
                toastNeedPermissions();
            }
        }
    }

    private void resetLayoutAndForward() {

    }


    private void toastNeedPermissions() {
        Toast.makeText(this, R.string.need_necessary_permissions, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noInternetDialog != null) {
            noInternetDialog.onDestroy();
        }

    }

}