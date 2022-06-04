package com.app.cryptok.Notifications;

import com.google.gson.Gson;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.utils.Commn;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class InializeNotification {
    private CompositeDisposable disposable = new CompositeDisposable();

    public void sendNotification(Sender sender) {
        disposable.add(MyApi.initPushNotification().pushNotification(sender)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((dates, throwable) -> {
                    if (dates != null) {

                        Commn.showDebugLog("pushNotificationRES:" + new Gson().toJson(dates));
                    }
                }));
    }



}
