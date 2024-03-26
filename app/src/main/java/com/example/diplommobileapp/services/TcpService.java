package com.example.diplommobileapp.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.diplommobileapp.R;
import com.example.diplommobileapp.data.models.chat.MessageViewModel;
import com.example.diplommobileapp.data.viewModels.ChatsViewModel;
import com.google.gson.Gson;

import java.io.IOException;

public class TcpService extends Service {
    public static final String CHANNEL_ID = "22";
    public static final String GROUP_KEY = "PrograMathChat";
    private int notificationId = 1;
    TcpClient client;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        client = new TcpClient(getResources().getString(R.string.tcp_id), 3333);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect();
                    client.authentification("HVCEU7OZ2KIQ5AFVTZBJX45OYPRJZV3E");
                    while (true) {
                        String message = client.receiveData();
                        Gson gson = new Gson();
                        MessageViewModel messageObject = gson.fromJson(message,MessageViewModel.class);
                        if (ChatsViewModel.instance!=null)
                            addMessageAndNotify(messageObject);
                        showNotification(messageObject.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_STICKY;
    }
    private Handler handler = new Handler(Looper.getMainLooper());

    private void addMessageAndNotify(MessageViewModel message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ChatsViewModel.instance.addMessage(message);
            }
        });
    }
    private void createNotificationChannel() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "ff",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            channel.setDescription("desc");
            channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String message) {
        notificationId++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat_50dp)
                .setContentTitle("Новое сообщение")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(message)
                .setGroup(GROUP_KEY)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}