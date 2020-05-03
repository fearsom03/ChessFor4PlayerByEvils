package kz.evilteamgenius.chessapp.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.widget.MediaController;

import java.util.ArrayList;

import kz.evilteamgenius.chessapp.R;
import timber.log.Timber;

//---------------------------------------------------------------------------------------
public class MusicService extends Service
        implements
        OnErrorListener,
        MediaController.MediaPlayerControl,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener {

    private final IBinder mBinder = new ServiceBinder();
    private MediaPlayer mPlayer;
    private int mBuffer = 0;
    private int currentPosition = 0;
    private ArrayList sourceArray = new ArrayList<Integer>();

    @Override
    public IBinder onBind(Intent context) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        create();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Timber.e("Music player failed");
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }

    public void create() {
        mPlayer = MediaPlayer.create(this, R.raw.kobyz1);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnCompletionListener(this);
        sourceArray.add(R.raw.kobyz1);
        sourceArray.add(R.raw.sybyzgy);
        sourceArray.add(R.raw.kobyz2);
        sourceArray.add(R.raw.alkissa);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        currentPosition++;
        if (currentPosition < sourceArray.size()) {
            mPlayer.reset();
            int resId = (int) sourceArray.get(currentPosition);
            mPlayer = MediaPlayer.create(this, resId);
            mPlayer.setOnCompletionListener(this);
            mPlayer.start();
        } else {
            currentPosition = 0;
            mPlayer.reset();
            int resId = (int) sourceArray.get(currentPosition);
            mPlayer = MediaPlayer.create(this, resId);
            mPlayer.setOnCompletionListener(this);
            mPlayer.start();
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
        return mBuffer;
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void pause() {
        if (mPlayer != null && isPlaying()) {
            mPlayer.pause();
        }
    }

    @Override
    public void seekTo(int pos) {
        mPlayer.seekTo(pos);
    }

    @Override
    public void start() {
        if (mPlayer == null) create();
        Thread th = new Thread(() -> mPlayer.start());
        th.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mBuffer = percent;
    }

    // Called by the interface ServiceConnected when calling the service
    public class ServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
