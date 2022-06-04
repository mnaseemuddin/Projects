package com.lgt.also_food_court_userApp.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SmsReceiver extends BroadcastReceiver {
    public static String otp;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            Log.e("PDUSSSS", pdus.length + "");
            String sender = smsMessage.getDisplayOriginatingAddress();
//You must check here if the sender is your provider and not another one with same text.
            String messageBody = smsMessage.getMessageBody();
            otp = messageBody.replaceAll("[^0-9]", "");
//Check here sender is yours
            Intent smsIntent = new Intent("otp");
            smsIntent.putExtra("message", otp);
            LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);


        }
    }
}
