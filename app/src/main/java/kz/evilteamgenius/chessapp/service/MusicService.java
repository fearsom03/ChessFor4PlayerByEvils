package kz.evilteamgenius.chessapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.widget.MediaController;

import java.util.ArrayList;

import kz.evilteamgenius.chessapp.Const;
import kz.evilteamgenius.chessapp.R;
import timber.log.Timber;

public class MusicService extends Service
        implements
        OnErrorListener,
        MediaController.MediaPlayerControl,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener {

    private final IBinder serviceBinder = new ServiceBinder();
    private MediaPlayer mediaPlayer;
    private int pausedSecond = 0;
    private int currentPosition = 0;
    private ArrayList sourceArray = new ArrayList<Integer>();
    private boolean isPlay;
    private SharedPreferences preferences;

    @Override
    public IBinder onBind(Intent context) {
        return serviceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        create();
        isPlay = preferences.getBoolean(Const.PLAY_MUSIC, true);
        if (!isPlay) {
            pause();
        } else {
            start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } finally {
                mediaPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Timber.e("Music player failed");
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } finally {
                mediaPlayer = null;
            }
        }
        return false;
    }

    public void create() {
        mediaPlayer = MediaPlayer.create(this, R.raw.kobyz1);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        sourceArray.add(R.raw.kobyz1);
        sourceArray.add(R.raw.sybyzgy);
        sourceArray.add(R.raw.kobyz2);
        sourceArray.add(R.raw.alkissa);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        currentPosition++;
        if (currentPosition < sourceArray.size()) {
            mediaPlayer.reset();
            int resId = (int) sourceArray.get(currentPosition);
            mediaPlayer = MediaPlayer.create(this, resId);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        } else {
            currentPosition = 0;
            mediaPlayer.reset();
            int resId = (int) sourceArray.get(currentPosition);
            mediaPlayer = MediaPlayer.create(this, resId);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        }
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return pausedSecond;
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public void start() {
        isPlay = preferences.getBoolean(Const.PLAY_MUSIC, true);
        if (isPlay) {
            if (mediaPlayer == null) {
                create();
            }
            Thread th = new Thread(() -> mediaPlayer.start());
            th.start();
        }

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        pausedSecond = percent;
    }

    // Called by the interface ServiceConnected when calling the service
    public class ServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
