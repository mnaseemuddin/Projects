package com.app.cryptok.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.text.HtmlCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;
import com.app.cryptok.activity.SplashActivity;
import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.app.cryptok.utils.Commn.TYPE;


public class MyFirebaseMessenging extends FirebaseMessagingService {

    private SessionManager sessionManager;
    Map<String, String> params;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference all_notification_ref;
    private ProfilePOJO profilePOJO;
    String myUser_id = "", reciever_user_id = "", alert_type = "", message = "", super_live_id = "", context_message = "",
            notification_type = "", notification_data = "";

    @Override
    public void onCreate() {
        super.onCreate();
        iniConfig();


    }

    private void iniConfig() {
        sessionManager = new SessionManager(getApplicationContext());
        profilePOJO = new Gson().fromJson(sessionManager.getString(ConstantsKey.PROFILE_KEY), ProfilePOJO.class);
        if (profilePOJO != null) {
            if (profilePOJO.getUserId() != null) {
                myUser_id = profilePOJO.getUserId();
            } else {
                myUser_id = "temp123";
            }
        } else {
            myUser_id = "temp123";
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        all_notification_ref = firebaseFirestore
                .collection(DBConstants.UserInfo)
                .document(myUser_id)
                .collection(DBConstants.all_notifications);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Commn.showDebugLog("onNewToken:" + token);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
                String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put(DBConstants.user_token, token);
                FirebaseFirestore.getInstance().collection(DBConstants.UserInfo)
                        .document(uuid)
                        .collection(DBConstants.Tokens)
                        .document(uuid)
                        .set(map)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Commn.showDebugLog("token updated:....");
                            } else {
                                Commn.showDebugLog("token updation failure");
                            }

                        });
            }
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Commn.showDebugLog("notification_data" + new Gson().toJson(remoteMessage.getData()) + ",from:" + remoteMessage.getFrom() + "");

        if (sessionManager.getBoolean(ConstantsKey.IS_LOGIN) || !sessionManager.getString(ConstantsKey.PROFILE_KEY).equals("")) {

            if (remoteMessage.getData() == null) {
                return;
            }
            params = remoteMessage.getData();
            reciever_user_id = params.get(Notification_Const.user_id);
            alert_type = params.get(Notification_Const.alert_type);
            message = params.get(Notification_Const.message);
            super_live_id = params.get(Notification_Const.super_live_id);
            context_message = params.get(Notification_Const.context_message);
            notification_type = params.get(Notification_Const.notification_type);
            if (params.containsKey(Notification_Const.notification_data)) {
                notification_data = params.get(Notification_Const.notification_data);
            }
            if (remoteMessage.getFrom().toString().contains(Notification_Const.global_notification_name)) {
                filterNotification();
            } else {
                recieveNotification();
            }

        }
    }

    private void filterNotification() {
        if (DBConstants.LiveShopping.equalsIgnoreCase(alert_type) || Commn.LIVE_TYPE.equalsIgnoreCase(alert_type))
            firebaseFirestore
                    .collection(DBConstants.UserInfo)
                    .document(reciever_user_id)
                    .collection(DBConstants.User_Followers)
                    .document(myUser_id)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.getResult() != null) {
                            if (task.getResult().exists()) {
                                Commn.showDebugLog("you are  eligible to this notification because you  follow him/her");
                                recieveNotification();

                            } else {
                                Commn.showDebugLog("you are not eligible to this notification because you did'nt follow him/her");
                            }
                        } else {
                            Commn.showDebugLog("you are not eligible to this notification because you did'nt follow him/her");
                        }
                    });
    }

    private void addNotificationOnFirebase() {

        Map<String, Object> params = new HashMap<>();
        params.put(DBConstants.user_id, reciever_user_id + "");
        params.put(DBConstants.my_user_id, myUser_id + "");
        params.put(DBConstants.message, message + "");
        params.put(DBConstants.super_live_id, super_live_id + "");
        params.put(DBConstants.timestamp, FieldValue.serverTimestamp());
        params.put(DBConstants.seen, false);
        params.put(Notification_Const.context_message, context_message + "");
        params.put(Notification_Const.alert_type, alert_type + "");
        params.put(Notification_Const.notification_type, notification_type + "");
        params.put(Notification_Const.notification_data, notification_data + "");
        DocumentReference docRef = all_notification_ref.document();
        String notification_id = docRef.getId();
        params.put(DBConstants.notification_id, notification_id + "");
        all_notification_ref
                .document(notification_id)
                .set(params)
                .addOnSuccessListener(aVoid -> Commn.showDebugLog("notification_added_in_list:" + new Gson().toJson(params))).

                addOnFailureListener(e -> Commn.showDebugLog("notification_added_onFailure:"));
    }

    String user_image;

    private void recieveNotification() {
        if (params == null) {
            return;
        }
        int requestID = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putString(DBConstants.user_id, reciever_user_id);

        bundle.putString(TYPE, alert_type);
        if (!notification_data.isEmpty()) {
            bundle.putString(Notification_Const.notification_data, notification_data);
        }
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT
                | PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "01";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(super_live_id != null || !super_live_id.isEmpty() ? super_live_id : getResources().getString(R.string.app_name))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.app_logo);
        if (context_message != null) {
            if (!context_message.isEmpty()) {

                notificationBuilder.setContentText(HtmlCompat.fromHtml(context_message, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        }
        if (!Commn.CHAT_TYPE.equalsIgnoreCase(alert_type)) {
            addNotificationOnFirebase();
        }

        if (Notification_Const.IMAGE_NOTIFICATION_TYPE.equalsIgnoreCase(notification_type)) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getImage(message)));
        }
        if (Notification_Const.NORMAL_NOTIFICATION_TYPE.equalsIgnoreCase(notification_type)) {
            if (message != null) {
                if (!message.isEmpty()) {
                    notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY)));
                }
            }
        }
        assert reciever_user_id != null;
        FirebaseFirestore.getInstance().collection(DBConstants.UserInfo)
                .document(reciever_user_id)
                .get()
                .addOnCompleteListener(task -> {


                    if (task.getResult().exists()) {
                        if (task.getResult().getString(DBConstants.image) != null) {
                            user_image = task.getResult().getString(DBConstants.image);

                            Commn.showDebugLog("user_image:" + user_image);


                        }


                    }

                });

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            String name = "Channel_001";
            String description = "Channel Description";
            //String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            //NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Random random = new Random();
        int ran = random.nextInt(100);
        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(ran, notificationBuilder.build());
        }
    }

    private Bitmap getImage(String url_recived) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bitmap bitmap = null;
        try {
            URL url = new URL(url_recived);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);


        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }
}
