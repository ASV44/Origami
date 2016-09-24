package com.koshka.origami.activity.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;

import com.koshka.origami.utils.NetworkUtil;

/**
 * Created by imuntean on 7/29/16.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {

    public ConnectivityChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);

        if (status.equals("1") || status.equals("2")){
            //Connected to the internet, send data to the server
        } else {
            //status.equals("0")
            //Not connected to the internet
            //Save statuses in the local db

        }
/*
            Toast.makeText(context, status, Toast.LENGTH_LONG).show();*/
    }

}
