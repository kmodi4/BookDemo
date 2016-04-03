package com.example.karan.bookdemo.chat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.karan.bookdemo.ChatList;
import com.example.karan.bookdemo.R;

import java.util.Random;

import co.mobiwise.fastgcm.GCMListenerService;

public class CustomGCMService extends GCMListenerService {

    public static final int NOTIFICATION_ID = 1;
    //private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String Sender;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        //Here is called even app is not working.
        //create your notification here
        // .

        String msg = data.getString("message");
        if (msg != null) {
            int index = msg.indexOf(" ");
            if(index!=-1)
               Sender = msg.substring(0,index);
            msg = msg.substring(msg.indexOf(" ")+1,msg.length());
        }
        sendNotification(from, msg);


    }

    private void sendNotification(String sender,String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, ChatList.class)
                //.addFlags(PendingIntent.FLAG_UPDATE_CURRENT)
                .putExtra("data", msg)
                .putExtra("sender",Sender)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Intent pushmsg = new Intent("pushmsg")
        .putExtra("data", msg)
                .putExtra("sender",Sender);

        LocalBroadcastManager.getInstance(this).sendBroadcast(pushmsg);


        PendingIntent contentIntent = PendingIntent.getActivity(this, new Random().nextInt(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        // .setSmallIcon(R.drawable.ic_stat_gcm)
                        .setContentTitle("From:"+Sender)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
