package com.example.yavor.nbuproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String message = bundle.getString("message");
        if (message == null || message.length() < 1){
          message = "Default";
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }
}