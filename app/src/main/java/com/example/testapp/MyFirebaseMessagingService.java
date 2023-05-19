package com.example.testapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.example.testapp.retrofit.RetrofitService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title = message.getNotification().getTitle();
        String body = message.getNotification().getBody();
        final String CHANNEL_ID = "HEADS_UP_NOTIFICATIONS";
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID, "MyNotification", NotificationManager.IMPORTANCE_HIGH);

        getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID).setContentTitle(title).setContentText(body).setSmallIcon(R.drawable.tm).setAutoCancel(true
        );
        NotificationManagerCompat.from(this).notify(1, notification.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply();

    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }



}
