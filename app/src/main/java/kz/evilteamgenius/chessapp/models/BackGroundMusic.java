package kz.evilteamgenius.chessapp.models;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

import kz.evilteamgenius.chessapp.R;

public class BackGroundMusic extends Service {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    int volume;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("TRY to start music KUKA");
        mediaPlayer = MediaPlayer.create(this, R.raw.coin);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            System.out.println("TRY to start music KUKA 22");
            e.printStackTrace();
        }
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        System.out.println("Song is started");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public boolean stopService(Intent name) {
        System.out.println("Song is stopped");

        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

    }



}
