package com.sgx.kotlin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * 开机自启
 */
public class AutoStartBroadCastReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("eeeeeee", "onReceive: 重启广播");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent ootStartIntent = new Intent(context, MainActivity.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }
    }
}
