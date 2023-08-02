package com.sgx.kotlin;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.SparseIntArray;


import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SgxPlayMP3
 * @Author jianglei
 * @CreateDate 2021/1/8 16:15
 * @Copyright 江苏高速公路信息工程有限公司
 */
public class SgxPlayMP3Utils {

    private MediaPlayer mediaPlayer;
    private AudioManager audiomanager;

    private volatile static SgxPlayMP3Utils mSgxPlayMP3 = null;
    private Context mContext;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private final int TIMEINTERVAL = 1;//ms

    private SoundPool mSoundPool;
    private SoundPool.Builder mSoundPoolBuilder = null;

    private final String REMINDERTYPE_GETIN = "1";
    private final String REMINDERTYPE_TIMEOUT = "2";
    private final String REMINDERTYPE_GETOUT = "3";
    private final String REMINDERTYPE_INPACKING = "4";
    private final String REMINDERTYPE_UNPACKING = "5";

    private SgxPlayMP3Utils(Context context) {
        this.mContext = context;
        audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        initSoundPool();
        executor.submit(new VoicePlaySchedule());
    }

    public static SgxPlayMP3Utils getInstance(Context context) {
        if (null == mSgxPlayMP3) {
            synchronized (SgxPlayMP3.class) {
                if (null == mSgxPlayMP3) {
                    mSgxPlayMP3 = new SgxPlayMP3Utils(context);
                }
            }
        }
        return mSgxPlayMP3;
    }

    private void initSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPoolBuilder = new SoundPool.Builder();
            mSoundPoolBuilder.setMaxStreams(1);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            mSoundPoolBuilder.setAudioAttributes(attrBuilder.build());
            mSoundPool = mSoundPoolBuilder.build();
        } else {
            mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
//            mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        }
        loadMP3();
    }

    private SparseIntArray musicId = new SparseIntArray();

    private Map<String, String> extra = null;

    private volatile BlockingQueue<Integer> soundQueue = new LinkedBlockingQueue<>();

    private String reminderType = "";


    public void asyncPlayMP3(int type) {
//        mSoundPool.autoPause();
        Log.e("eeee","zhixing");
        if (type == 0) {//登录方式
            soundQueue.clear();
            Log.e("eeee","zhixing122");
            soundQueue.add(R.raw.specail_things);
//            play();
            playmp3mp3();
        }

    }

    public void stopPlayMp3() {
        if (null != mSoundPool) {
            mSoundPool.autoPause();
        }
    }

    private void loadMP3() {
        musicId.put(R.raw.specail_things, mSoundPool.load(mContext, R.raw.specail_things, 1));
//        musicId.put(R.raw.idcard_hint, mSoundPool.load(mContext, R.raw.idcard_hint, 1));
//        musicId.put(R.raw.phone_hint, mSoundPool.load(mContext, R.raw.phone_hint, 1));
//        musicId.put(R.raw.vehicle_select_hint, mSoundPool.load(mContext, R.raw.vehicle_select_hint, 1));
//        musicId.put(R.raw.info_edit_hint, mSoundPool.load(mContext, R.raw.info_edit_hint, 1));
//        musicId.put(R.raw.back_idcard_hint, mSoundPool.load(mContext, R.raw.back_idcard_hint, 1));

    }

    private void play() {
        synchronized (SgxPlayMP3Utils.class) {
            SgxPlayMP3Utils.class.notifyAll();
        }
    }

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private boolean isBreakOff = true;

    public boolean isBreakOff() {
        return isBreakOff;
    }

    public void setBreakOff(boolean breakOff) {
        isBreakOff = breakOff;
    }

    private void playmp3mp3() {
        if (!isBreakOff()) {
            return;
        }
        setBreakOff(false);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                    audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                if(soundQueue.size()>0){
                    mSoundPool.play(musicId.get(soundQueue.poll()), 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        }, 0, TIMEINTERVAL, TimeUnit.MILLISECONDS);
    }

    private class VoicePlaySchedule implements Runnable {
        @Override
        public void run() {
            synchronized (SgxPlayMP3.class) {
                while (true) {
                    while (soundQueue.size() > 0) {
                        timer.start();
                        mFlag = false;
                        while (true) {
                            if (mFlag == true) {
                                break;
                            }
                        }
                    }

                    try {
                        SgxPlayMP3.class.wait();
                    } catch (InterruptedException e) {
                        Log.e("ee","ee");
                    }

                }
            }
        }
    }

    private boolean mFlag = false;

    /**
     * 播放倒计时
     */
    private CountDownTimer timer = new CountDownTimer(TIMEINTERVAL, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("CountDownTimer", "" + millisUntilFinished);
        }

        @Override
        public void onFinish() {
            if (soundQueue.size() > 0) {
                mSoundPool.play(musicId.get(soundQueue.poll()), 1.0f, 1.0f, 0, 0, 1.0f);
            } else {
                mFlag = false;
            }
        }
    };

}