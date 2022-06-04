package com.app.cryptok.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.LiveShopping.Activities.LiveShoppingPreviewActivity;
import com.app.cryptok.LiveShopping.Model.LiveShoppingModel;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.R;
import com.app.cryptok.go_live_module.LiveConst;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.view_live_module.HotLiveModel;

import static com.app.cryptok.utils.Commn.CHAT_TYPE;
import static com.app.cryptok.utils.Commn.FOLLOW_TYPE;
import static com.app.cryptok.utils.Commn.TYPE;

public class SplashActivity extends AppCompatActivity {
    SessionManager sessionManager;
    private FirebaseFirestore firebaseFirestore;
    private Context context;
    private SplashActivity activity;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = activity = this;
        sessionManager = new SessionManager(context);
        firebaseFirestore = FirebaseFirestore.getInstance();
        runOnUiThread(this::launchApp);
    }

    private void launchApp() {
        if (sessionManager.getBoolean(ConstantsKey.IS_LOGIN) || !sessionManager.getString(ConstantsKey.PROFILE_KEY).equals("")) {
            if (getIntent().hasExtra(TYPE)) {
                user_id = getIntent().getStringExtra(DBConstants.user_id);
                String type = getIntent().getStringExtra(TYPE);
                if (CHAT_TYPE.equalsIgnoreCase(type)) {
                    openChat();
                }
                if (FOLLOW_TYPE.equalsIgnoreCase(type)) {
                    openProfile();
                }
                if (DBConstants.LiveShopping.equalsIgnoreCase(type)) {
                    getLiveShopping();
                }
                if (Commn.LIVE_TYPE.equalsIgnoreCase(type)) {
                    getLiveStream();
                }
                if (Commn.POST_LIKE_TYPE.equalsIgnoreCase(type) || Commn.POST_COMMENT_TYPE.equalsIgnoreCase(type)) {
                    openPost();
                }
            } else {
                launchMain();

            }
            sessionManager.setBoolean(ConstantsKey.IS_LOGIN, true);
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finishAffinity();
        }
    }

    private void openPost() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(Notification_Const.notification_data)) {
                String notificaton_data = getIntent().getStringExtra(Notification_Const.notification_data);
                Intent intent = new Intent(context, SinglePostActivity.class);
                intent.putExtra(DBConstants.post_id, notificaton_data);
                startActivity(intent);
                finish();
            } else {
                launchMain();
            }
        } else {
            launchMain();
        }

    }

    private void launchMain() {
        new Handler(Looper.getMainLooper())
                .postDelayed(() -> {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finishAffinity();

                }, 1500);

    }

    private void getLiveStream() {
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
                            launchMain();
                        }
                    } else {
                        Commn.myToast(context, "Broadcast Ended");
                        launchMain();
                    }

                });
    }

    private void getLiveShopping() {

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
                            launchMain();
                        }
                    } else {
                        Commn.myToast(context, "Broadcast Ended");
                        launchMain();
                    }

                });

    }

    private void openProfile() {
        new Handler(Looper.getMainLooper())
                .postDelayed(() -> {
                    Intent intent = new Intent(SplashActivity.this, AnotherUserProfile.class);
                    if (user_id != null) {
                        intent.putExtra(DBConstants.user_id, user_id + "");
                    }
                    startActivity(intent);
                    finish();
                }, 1500);

    }

    private void openChat() {
        new Handler(Looper.getMainLooper())
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
                        if (user_id != null) {
                            intent.putExtra(DBConstants.user_id, user_id + "");
                        }
                        intent.putExtra(TYPE, CHAT_TYPE);
                        startActivity(intent);
                        finish();
                    }
                }, 1500);

    }
}