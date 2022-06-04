package com.app.cryptok.activity;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.BuildConfig;
import com.app.cryptok.Notifications.Notification_Const;
import com.app.cryptok.model.Points.PointsModel;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.SessionManager;
import com.app.cryptok.utils.UpdateHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OfflineFun extends Application {
    private static OfflineFun mInstance;
    SessionManager sessionManager;
    public static SimpleCache simpleCache = null;
    public static LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = null;
    public static ExoDatabaseProvider exoDatabaseProvider = null;
    public static Long exoPlayerCacheSize = (long) (200 * 1024 * 1024);

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        sessionManager = new SessionManager(getApplicationContext());

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);


        setupRemoteConfig();
        if (sessionManager != null) {
            savePoints();
        }
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);
        }

        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = new ExoDatabaseProvider(this);
        }

        if (simpleCache == null) {
            simpleCache = new SimpleCache(getCacheDir(), leastRecentlyUsedCacheEvictor, exoDatabaseProvider);
            if (simpleCache.getCacheSpace() >= exoPlayerCacheSize) {
                freeMemory();
            }
            Commn.showDebugLog("onCreate: " + simpleCache.getCacheSpace());
        }
        subscribeTopic();

    }


    private void subscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(Notification_Const.global_notification_name)
                .addOnCompleteListener(task -> Commn.showDebugLog("subscribeToTopic:" + task.isSuccessful()));
    }

    public void freeMemory() {

        try {
            File dir = getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void setupRemoteConfig() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        String MobileVName = BuildConfig.VERSION_NAME;
        Log.e("mobileversionname", MobileVName + "");
        //default value
        Map<String, Object> defaultMap = new HashMap<>();
        defaultMap.put(UpdateHelper.KEY_UPDATE_ENABLE, true);
        defaultMap.put(UpdateHelper.KEY_UPDATE_VERSION, MobileVName);

        remoteConfig.setDefaultsAsync(defaultMap);
        remoteConfig.fetch(5)// fetch notificationModel from firebase every 5 seconds ,in real world,you need make it to 1 min , min etc
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Commn.showDebugLog("Fetched_remote:" + "success");
                            remoteConfig.activate();
                        } else {
                            Commn.showDebugLog("Fetched_remote:" + "failure" + task.getException().getMessage() + "");
                        }
                    }
                });
    }

    private void savePoints() {

        FirebaseFirestore.getInstance().collection(DBConstants.points_table)
                .document(DBConstants.points_table_key)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            Commn.showDebugLog("get points:");
                            PointsModel model = task.getResult().toObject(PointsModel.class);
                            if (model != null) {
                                sessionManager.setPoints(model);
                            }
                        }
                    } else {
                        Commn.showDebugLog("get not points:");
                    }


                }).addOnFailureListener(e -> Commn.showDebugLog("get not points:" + new Gson().toJson(e)));

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
