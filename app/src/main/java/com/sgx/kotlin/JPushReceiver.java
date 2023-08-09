package com.sgx.kotlin;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Vibrator;
import android.util.Log;

import java.util.Date;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class JPushReceiver extends JPushMessageReceiver {
    private Vibrator vibrator;

    @SuppressLint("MissingPermission")
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        ScreenUtils.wakeUpScreen(context);
//        SgxPlayMP3Utils.getInstance(context).asyncPlayMP3(0);
        Log.e("eeee", customMessage.toString());
        TTSUtils.getInstance(context).playText(customMessage.message, true);

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
        JPushMessages jPushMessages = new JPushMessages(customMessage.title, customMessage.messageId + "", customMessage.message, TTSUtils.sdf.format(new Date()).toString(), 0);
        Log.e("eeee", jPushMessages.toString());
        MyApplication.mDaoSession.getJPushMessagesDao().insertOrReplace(jPushMessages);


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        ScreenUtils.wakeUpScreen(context);
//        SgxPlayMP3Utils.getInstance(context).asyncPlayMP3(0);
        Log.e("eeee", notificationMessage.toString());
        TTSUtils.getInstance(context).playText(notificationMessage.notificationContent, true);

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
        JPushMessages jPushMessages = new JPushMessages(notificationMessage.notificationTitle, notificationMessage.notificationId + "", notificationMessage.notificationContent, TTSUtils.sdf.format(new Date()).toString(), 0);
        Log.e("eeee", jPushMessages.toString());
        MyApplication.mDaoSession.getJPushMessagesDao().insertOrReplace(jPushMessages);

    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        super.onMultiActionClicked(context, intent);
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
