//package com.tonly.walnut.gesturedemo;
//
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnCompletionListener;
//import android.media.MediaPlayer.OnErrorListener;
//import android.util.Log;
//
//
//import java.io.IOException;
//
//public class MediaManager {
//    private static String TAG = "MediaManager";
//
//    private static MediaPlayer mPlayer;
//    private static AudioManager mAudioManager;
//    private static boolean isPause;
//
//    public static void playSound(String filePathString,
//                                 OnCompletionListener onCompletionListener) {
//        //1 初始化AudioManager对象
//        mAudioManager = (AudioManager) TKidsApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
//        //2 申请焦点
//        mAudioManager.requestAudioFocus(mAudioFocusChange, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//
//        // TODO Auto-generated method stub
//        if (mPlayer == null) {
//            mPlayer = new MediaPlayer();
//            //保险起见，设置报错监听
//            mPlayer.setOnErrorListener(new OnErrorListener() {
//
//                @Override
//                public boolean onError(MediaPlayer mp, int what, int extra) {
//                    // TODO Auto-generated method stub
//                    mPlayer.reset();
//                    return false;
//                }
//            });
//        } else {
//            mPlayer.reset();//就恢复
//        }
//
//        try {
//            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mPlayer.setOnCompletionListener(onCompletionListener);
//            mPlayer.setDataSource(filePathString);
//            mPlayer.prepare();
//            mPlayer.start();
//        } catch (IllegalArgumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (SecurityException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//
//    private static AudioManager.OnAudioFocusChangeListener mAudioFocusChange = new AudioManager.OnAudioFocusChangeListener() {
//        @Override
//        public void onAudioFocusChange(int focusChange) {
//            switch (focusChange){
//                case AudioManager.AUDIOFOCUS_LOSS:
//                    //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，
//                    //会触发此回调事件，例如播放QQ音乐，网易云音乐等
//                    //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
//                    Log.d(TAG, "AUDIOFOCUS_LOSS");
//
//                    //释放焦点，该方法可根据需要来决定是否调用
//                    //若焦点释放掉之后，将不会再自动获得
//                    abandonAudioFocus();
//
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                    //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
//                    //会触发此回调事件，例如播放短视频，拨打电话等。
//                    //通常需要暂停音乐播放
//                    pause();
//                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                    //短暂性丢失焦点并作降音处理
//                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
//                    break;
//                case AudioManager.AUDIOFOCUS_GAIN:
//                    //当其他应用申请焦点之后又释放焦点会触发此回调
//                    //可重新播放音乐
//                    Log.d(TAG, "AUDIOFOCUS_GAIN");
//
//                    break;
//            }
//        }
//    };
//
//    public static void abandonAudioFocus() {
//        if(mAudioFocusChange != null && mAudioManager != null){
//            mAudioManager.abandonAudioFocus(mAudioFocusChange);
//        }
//
//    }
//
//
//    //停止函数
//    public static void pause() {
//        if (mPlayer != null && mPlayer.isPlaying()) {
//            mPlayer.pause();
//            isPause = true;
//            abandonAudioFocus();
//        }
//
//    }
//
//    public static Boolean isPlaying(){
//        if (mPlayer != null && mPlayer.isPlaying()) {
//            return mPlayer.isPlaying();
//        }
//        return false;
//    }
//
//    //继续
//    public static void resume() {
//        if (mPlayer != null && isPause) {
//            mPlayer.start();
//            isPause = false;
//        }
//    }
//
//
//    public static void release() {
//        if (mPlayer != null) {
//            mPlayer.release();
//            mPlayer = null;
//        }
//        abandonAudioFocus();
//    }
//}
