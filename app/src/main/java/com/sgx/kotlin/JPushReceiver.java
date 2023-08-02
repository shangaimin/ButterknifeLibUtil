package com.sgx.kotlin;

import android.app.Service;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Vibrator;
import android.util.Log;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class JPushReceiver extends JPushMessageReceiver {
    private Vibrator vibrator;

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        ScreenUtils.wakeUpScreen(context);
        SgxPlayMP3Utils.getInstance(context.getApplicationContext()).asyncPlayMP3(0);
        Log.e("eeee", "zhixing-1");

        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
//        vibrator.vibrate(30000);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION) //key
                .build();
        vibrator.vibrate(new long[]{
                0,
                1000,
                2000,
                5000,
                3000,
                1000
        }, -1, audioAttributes);

    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        ScreenUtils.wakeUpScreen(context);
//        SgxPlayMP3Utils.getInstance(context).asyncPlayMP3(0);
        Log.e("eeee", notificationMessage.notificationContent);
        TTSUtils.getInstance(context).playText(notificationMessage.notificationContent);

        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
//        vibrator.vibrate(30000);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION) //key
                .build();
        vibrator.vibrate(new long[]{
                0,
                3000,
        }, -1, audioAttributes);

    }
}
