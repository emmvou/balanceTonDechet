package com.moustache.professeur.balancetondechet;

import android.app.Application;
import android.app.NotificationChannel;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class NotificationManager extends Application
{

    public static final String CHANNEL_1 = "low";
    public static final String CHANNEL_2 = "default";
    public static final String CHANNEL_3 = "high";

    private void createNotificationsChannels()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel lowChannel = new NotificationChannel(CHANNEL_1, "low priority channel", android.app.NotificationManager.IMPORTANCE_LOW);
            NotificationChannel defaultChannel = new NotificationChannel(CHANNEL_2, "default priority channel", android.app.NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel highChannel = new NotificationChannel(CHANNEL_3, "high priority channel", android.app.NotificationManager.IMPORTANCE_HIGH);

            lowChannel.setDescription("A low priority channel for the app.");
            lowChannel.setDescription("A default priority channel for the app.");
            lowChannel.setDescription("A high priority channel for the app.");

            android.app.NotificationManager notificationManager = getSystemService(android.app.NotificationManager.class);

            Objects.requireNonNull(notificationManager).createNotificationChannel(lowChannel);
            Objects.requireNonNull(notificationManager).createNotificationChannel(defaultChannel);
            Objects.requireNonNull(notificationManager).createNotificationChannel(highChannel);
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        createNotificationsChannels();
    }
}
