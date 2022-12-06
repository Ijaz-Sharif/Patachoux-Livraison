package com.patach.patachoux.Notification;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.patach.patachoux.MainActivity;
import com.patach.patachoux.Model.NotificationUtils;
import com.patach.patachoux.Screens.SuplierMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String notification_ID = "example.notification.Services.test";
    private static final String CHANNEL_ID = "Bestmarts";
    private static final String CHANNEL_NAME = "Bestmarts";

    private NotificationUtils notificationUtils;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("myNewToken", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("remotemessage::: ", remoteMessage.toString());
        if (remoteMessage.getData().size() > 0) {
           // Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            createNotification(remoteMessage);
        }

    }

    private void createNotification(RemoteMessage remoteMessage) {
        JSONObject notificationmessage = null;
        Log.d("mynotificationdata = ", remoteMessage.getData().toString());
        try {
            Map<String, String> params = remoteMessage.getData();
            notificationmessage = new JSONObject(params);
        } catch (Exception e) {
           // Log.e(TAG, "Exception: " + e.getMessage());
        }
        try {
            String title = notificationmessage.getString("title");
            String body = notificationmessage.getString("body");
            String check=notificationmessage.getString("check");


            String date = String.valueOf(new Date().getTime());
            Intent resultIntent;
            if(check.equals("suplier")){
                resultIntent = new Intent(getApplicationContext(), SuplierMainActivity.class);
            }
            else {
                resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            }



            Intent pushNotification = new Intent("PUSH_NOTIFICATION");
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            showNotificationMessageWithBigImage(getApplicationContext(), title, body, date, resultIntent, "");




        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP) ;
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}
