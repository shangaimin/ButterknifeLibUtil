package com.sgx.kotlin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

public class ScreenUtils {
    /**
     * 唤醒屏幕
     *
     * @param context
     */

    public static void wakeUpScreen(Context context) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        boolean screenOn = pm.isScreenOn();

        if (!screenOn) {

            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

            wl.acquire(10 * 60 * 1000L /*10 minutes*/); // 点亮屏幕

            wl.release(); // 释放

        }

    }
}
