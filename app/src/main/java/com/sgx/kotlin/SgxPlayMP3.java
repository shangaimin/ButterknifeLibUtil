package com.sgx.kotlin;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
import android.os.CountDownTimer;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SgxPlayMP3 {
    private MediaPlayer mediaPlayer;
    private AudioManager audiomanager;
    private static volatile SgxPlayMP3 mSgxPlayMP3 = null;
    private Context mContext;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private final int TIMEINTERVAL = 3000;
    private SoundPool mSoundPool;
    private Builder mSoundPoolBuilder = null;
    private final String REMINDERTYPE_GETIN = "1";
    private final String REMINDERTYPE_TIMEOUT = "2";
    private final String REMINDERTYPE_GETOUT = "3";
    private final String REMINDERTYPE_INPACKING = "4";
    private final String REMINDERTYPE_UNPACKING = "5";
    private SparseIntArray musicId = new SparseIntArray();
    private Map<String, String> extra = null;
    private volatile BlockingQueue<Integer> soundQueue = new LinkedBlockingQueue();
    private String reminderType = "";
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private boolean isBreakOff = true;
    private boolean mFlag = false;
    private CountDownTimer timer = new CountDownTimer(3000L, 1000L) {
        public void onTick(long millisUntilFinished) {
            Log.e("CountDownTimer", "" + millisUntilFinished);
        }

        public void onFinish() {
            if (SgxPlayMP3.this.soundQueue.size() > 0) {
                SgxPlayMP3.this.mSoundPool.play(SgxPlayMP3.this.musicId.get((Integer)SgxPlayMP3.this.soundQueue.poll()), 1.0F, 1.0F, 0, 0, 1.0F);
            } else {
                SgxPlayMP3.this.mFlag = false;
            }

        }
    };

    private SgxPlayMP3(Context context) {
        this.mContext = context;
        this.audiomanager = (AudioManager)context.getSystemService("audio");
        this.initSoundPool();
        this.executor.submit(new SgxPlayMP3.VoicePlaySchedule());
    }

    public static SgxPlayMP3 getInstance(Context context) {
        if (null == mSgxPlayMP3) {
            Class var1 = SgxPlayMP3.class;
            synchronized(SgxPlayMP3.class) {
                if (null == mSgxPlayMP3) {
                    mSgxPlayMP3 = new SgxPlayMP3(context);
                }
            }
        }

        return mSgxPlayMP3;
    }

    private void initSoundPool() {
        if (VERSION.SDK_INT >= 21) {
            this.mSoundPoolBuilder = new Builder();
            this.mSoundPoolBuilder.setMaxStreams(3);
            android.media.AudioAttributes.Builder attrBuilder = new android.media.AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(3);
            this.mSoundPoolBuilder.setAudioAttributes(attrBuilder.build());
            this.mSoundPool = this.mSoundPoolBuilder.build();
        } else {
            this.mSoundPool = new SoundPool(3, 3, 0);
        }

        this.loadMP3();
    }


    private void loadMP3() {
        this.musicId.put(R.raw.specail_things, this.mSoundPool.load(this.mContext,R.raw.specail_things, 1));
    }

    private void play() {
        Class var1 = SgxPlayMP3.class;
        synchronized(SgxPlayMP3.class) {
            SgxPlayMP3.class.notify();
        }
    }

    public boolean isBreakOff() {
        return this.isBreakOff;
    }

    public void setBreakOff(boolean breakOff) {
        this.isBreakOff = breakOff;
    }

    private void playmp3mp3() {
        if (this.isBreakOff()) {
            this.setBreakOff(false);
            this.executorService.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if (SgxPlayMP3.this.soundQueue.size() > 0) {
                        SgxPlayMP3.this.mSoundPool.play(SgxPlayMP3.this.musicId.get((Integer)SgxPlayMP3.this.soundQueue.poll()), 1.0F, 1.0F, 0, 0, 1.0F);
                    }

                }
            }, 0L, 3000L, TimeUnit.MILLISECONDS);
        }
    }

    private class VoicePlaySchedule implements Runnable {
        private VoicePlaySchedule() {
        }

        public void run() {
            Class var1 = SgxPlayMP3.class;
            synchronized(SgxPlayMP3.class){}

            try {
                while(true) {
                    while(SgxPlayMP3.this.soundQueue.size() <= 0) {
                        try {
                            SgxPlayMP3.class.wait();
                        } catch (InterruptedException var6) {
                            var6.printStackTrace();
                        }
                    }

                    SgxPlayMP3.this.timer.start();
                    SgxPlayMP3.this.mFlag = false;

                    while(!SgxPlayMP3.this.mFlag) {
                    }
                }
            } finally {
                ;
            }
        }
    }
}
