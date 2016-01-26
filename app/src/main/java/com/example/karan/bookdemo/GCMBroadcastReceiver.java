package com.example.karan.bookdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by KARAN on 26-01-2016.
 */
public class GCMBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("com.google.android.c2dm.intent.REGISTRATION")){
            String registration_id   =  intent.getStringExtra("registration_id");
            String error             =  intent.getStringExtra("error");
            String unregistered      =  intent.getStringExtra("unregistered");
            Log.i("R_Id",registration_id);
        }
        else if(action.equals("com.google.android.c2dm.intent.RECEIVE")){
            String data1 = intent.getStringExtra("data1");
            String data2 = intent.getStringExtra("data2");
        }
    }
}
