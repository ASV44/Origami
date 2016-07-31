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

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }

}
