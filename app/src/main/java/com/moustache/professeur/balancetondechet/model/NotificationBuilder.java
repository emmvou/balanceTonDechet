package com.moustache.professeur.balancetondechet.model;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBuilder
{

    public void sendNotificationOnChannel(String title, String message, String channelId, int imageResource, int priority, Context context)
    {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(imageResource)
                .setPriority(priority);

        NotificationManagerCompat.from(context).notify(0, notification.build());
    }

}
