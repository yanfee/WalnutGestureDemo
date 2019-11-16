package com.tonly.walnut.gesturedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private ImageView iv;
    private int mIndex = 0;
    private MediaPlayer mediaPlayer;
    private List<Bitmap> mList;
    private List<String> mp3List = new ArrayList<>();
    private String mp3Path;
    public static final int KEY_RIGHT = 0X04;
    public static final int KEY_LEFT = 0X08;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUi();
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void setUi() {
        //for new api versions.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void initViews() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        boolean isGrantRead = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean isGrantWrite = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        String path = "";
        if (isGrantRead && isGrantWrite) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Walnut/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, 0x01);
        }
        iv = findViewById(R.id.iv);
        if (!TextUtils.isEmpty(path)) {
            mList = getFileName(path);
        }
        if (null != mp3List && mp3List.size() > 0) {
            mp3Path = mp3List.get(0);
            playFlieMp3(mp3Path);
        } else {
            playSoundFromA();
        }
    }

    public List<Bitmap> getFileName(String fileAbsolutePath) {
        List<Bitmap> vecFile = new ArrayList<>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if (null == subFile) return vecFile;
        for (File value : subFile) {
            // 判断是否为文件夹
            if (!value.isDirectory()) {
                String filename = value.getName();
                if (filename.toLowerCase().contains("png") || filename.toLowerCase().contains("jpg")) {
                    Bitmap bitmap = getLoacalBitmap(value.getPath());
                    vecFile.add(bitmap);
                } else if (filename.toLowerCase().contains("mp3")||filename.toLowerCase().contains("wav")) {
                    mp3List.add(value.getPath());
                }
                Log.e("eee", "文件名 ： " + filename);
            }
        }
        return vecFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x01 && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Walnut/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            Log.e(TAG, "权限申请失败");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(mp3Path)) {
            playSoundFromA();
        } else {
            playFlieMp3(mp3Path);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEY_RIGHT) {
            Toast.makeText(this, "RIGHT", Toast.LENGTH_LONG).show();
            nextPic();
        } else if (keyCode == KEY_LEFT) {
            Toast.makeText(this, "LEFT", Toast.LENGTH_LONG).show();
            prePic();
        }
        iv.setImageBitmap(mList.get(mIndex));
        return super.onKeyDown(keyCode, event);
    }

    private void nextPic() {
        if (null != mList && mList.size() > 0) {
//            if (mIndex >= mList.size() - 1) {
//                mIndex = 0;
//            } else {
//                mIndex++;
//            }
            int size = mList.size();
            mIndex = (mIndex++) % size;
        }
    }

    private void prePic() {
        if (null != mList && mList.size() > 0) {
//            if (mIndex == 0) {
//                mIndex = mList.size() - 1;
//            } else {
//                mIndex--;
//            }

            int size = mList.size();
            if (mIndex <= 0)
                mIndex = size;
            mIndex = (mIndex--) % size;
        }
    }

    /**
     * 从assets资源文件夹中播放
     */
    private void playSoundFromA() {
        if (null != mediaPlayer) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            AssetFileDescriptor afd;
            try {
                afd = getAssets().openFd("left.mp3");
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true);
                    }
                });
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void playFlieMp3(String path) {
        if (null != mediaPlayer) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true);
                    }
                });
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(url);
            bitmap = BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
