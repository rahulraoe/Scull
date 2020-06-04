package com.err.scullapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rahul Rao on 14-05-2020.
 */
public class FirebaseMessagingService extends  com.google.firebase.messaging.FirebaseMessagingService {

    Bitmap bitmap;
    RemoteViews View;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().size()>0){



            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            String url = remoteMessage.getData().get("url");
            bitmap = getBitmapfromUrl(url);


//View.setImageViewBitmap(R.id.image_app,bitmap);
//    View.setTextViewText(R.id.title,title);
//    View.setTextViewText(R.id.text,body);
            int notificationid=(int)System.currentTimeMillis();
            // Create an Intent for the activity you want to start
            Intent resultIntent = new Intent(this,Home_Activity.class);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            // Get the PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager mnotify=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "RAO_CH_02";
                @SuppressLint("ResourceAsColor") Notification notification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText("hello")
                        .setLargeIcon(bitmap)
                        .setColor(R.color.colorPrimaryDark)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap)
                                .bigLargeIcon(null))
                        .build();



//        Notification.Builder notificationBuilder =
//                new Notification.Builder(this, channelId)
//                        .setContentIntent(resultPendingIntent)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setLargeIcon(bitmap)
//
//
//
//                        .setColor(Color.parseColor("#039BE5"))
//                        .setContentTitle(title)
//                        .setContentText(body)
//                        .setAutoCancel(true);

                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);

                channel.enableLights(true);
                channel.enableVibration(true);

                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();
                Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                channel.setSound(uri,att);
//            channel.setVibrationPattern(new long[]{0, 1000, 500});
                channel.enableLights(true);
                channel.setLightColor(Color.RED);

                mnotify.createNotificationChannel(channel);
                mnotify.notify(notificationid,notification);



            }
            else
            {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(resultPendingIntent)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setVibrate(new long[]{1000})
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                mnotify.notify(notificationid,mBuilder.build());

            }

        }
        else
        {
            String title= remoteMessage.getNotification().getTitle();
            String body=remoteMessage.getNotification().getBody();


            int notificationid=(int)System.currentTimeMillis();
            // Create an Intent for the activity you want to start
            Intent resultIntent = new Intent(this,Home_Activity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager mnotify=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            String abc="yes";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "RAO_CH_02";
                Notification.Builder notificationBuilder =
                        new Notification.Builder(this, channelId)
                                .setContentIntent(resultPendingIntent)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setColor(Color.parseColor("#039BE5"))
                                .setContentTitle(title)
                                .setContentText(body)
                                .setAutoCancel(true);

                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);

                channel.enableLights(true);
                channel.enableVibration(true);

                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();
                Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                channel.setSound(uri,att);
//            channel.setVibrationPattern(new long[]{0, 1000, 500});
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                mnotify.createNotificationChannel(channel);
                mnotify.notify(notificationid, notificationBuilder.build());



            }
            else
            {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(resultPendingIntent)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setVibrate(new long[]{1000})
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                mnotify.notify(notificationid,mBuilder.build());

            }


        }






    }
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
