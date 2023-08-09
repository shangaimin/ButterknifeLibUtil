package com.sgx.kotlin;

import android.app.Service;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TTSUtils extends UtteranceProgressListener {

    private Context mContext;
    private static TTSUtils singleton;
    private TextToSpeech textToSpeech; // 系统语音播报类
    private boolean isSuccess = true;
    private String text;
    private Vibrator vibrator;
    private int count=0;
    public static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    public static TTSUtils getInstance(Context context) {
        if (singleton == null) {
            synchronized (TTSUtils.class) {
                if (singleton == null) {
                    singleton = new TTSUtils(context);
                }
            }
        }
        return singleton;
    }

    private TTSUtils(Context context) {
        this.mContext = context.getApplicationContext();
        textToSpeech = new TextToSpeech(mContext, i -> {
            Log.e("语音初始化结果", i + "");
            //系统语音初始化成功
            if (i == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.CHINA);
                textToSpeech.setPitch(2.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                textToSpeech.setSpeechRate(1.0f);
                textToSpeech.setOnUtteranceProgressListener(TTSUtils.this);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    //系统不支持中文播报
                    isSuccess = false;
                }
            }

        }, "com.iflytek.speechcloud");//百度的播放引擎 "com.baidu.duersdk.opensdk"

    }

    public void playText(String playText,boolean isNewMessage) {

        if (!isSuccess) {
            Toast.makeText(mContext, "系统不支持中文播报", Toast.LENGTH_SHORT).show();
            return;
        }
        if(isNewMessage){
            count=0;
        }
        this.text=playText;
        count++;
//        if (ismServiceConnectionUsable(textToSpeech)) {//没有被回收
//            textToSpeech.speak(playText, TextToSpeech.QUEUE_ADD, null, "jiance");
//        } else {//被回收了不可用状态
        textToSpeech = new TextToSpeech(MyApplication.getmContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.CHINA);
                if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                        && result != TextToSpeech.LANG_AVAILABLE) {
                }
                textToSpeech.speak(playText, TextToSpeech.QUEUE_ADD, null, "jiance");
            } else {
                Log.e("eeee", "TTS初始化失败！");
            }
        }, "com.iflytek.speechcloud");//科大讯飞语音引擎
//        }
        textToSpeech.setOnUtteranceProgressListener(this);

        vibrator = (Vibrator) MyApplication.getmContext().getSystemService(Service.VIBRATOR_SERVICE);
//        vibrator.vibrate(30000);


    }

    public void stopSpeak() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();

        }
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {
        Log.e("eeeee",count+"");
        if(count>=3){
            return;
        }
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION) //key
                .build();
        vibrator.vibrate(new long[]{
                0,
                3000,
        }, -1, audioAttributes);
        playText(text,false);
    }

    @Override
    public void onError(String utteranceId) {

    }

    /**
     * TTS初始化之后有时会无法播放语音。
     * 从打印日志看failed: not bound to TTS engine
     * 找到源代码打印处
     * if (mServiceConnection == null) {
     * Log.w(TAG, method + " failed: not bound to TTS engine");
     * return errorResult;
     * }
     * 通过反射判断mServiceConnection是否为空
     *
     * @param tts
     * @return true 可用
     */
    public static boolean ismServiceConnectionUsable(TextToSpeech tts) {

        boolean isBindConnection = true;
        if (tts == null) {
            return false;
        }
        Field[] fields = tts.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);

            if (TextUtils.equals("mConnectingServiceConnection", fields[j].getName()) && TextUtils.equals("android.speech.tts.TextToSpeech$Connection", fields[j].getType().getName())) {
                try {
                    if (fields[j].get(tts) == null) {
                        isBindConnection = false;
                        Log.e("eeee", "*******反射判断 TTS -> mServiceConnection == null*******");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return isBindConnection;
    }

}
