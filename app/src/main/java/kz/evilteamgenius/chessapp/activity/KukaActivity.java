package kz.evilteamgenius.chessapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import java.util.Objects;

import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.fragments.FragmentOptionsFragment;
import kz.evilteamgenius.chessapp.fragments.NavigationPageFragment;

public class KukaActivity extends AppCompatActivity implements NavigationPageFragment.OnFragmentInteractionListener {

    private Fragment fragment;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuka);
        initMusic();
        fragment =new  NavigationPageFragment();
        replaceFragment(fragment);
    }

    private void initMusic() {
        mPlayer = MediaPlayer.create(this, R.raw.nina);
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction tr = this.getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.frame, fragment);
        tr.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        mPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mPlayer.stop();
        mPlayer.release();
        super.onStop();
    }

    @Override
    protected void onStart() {
        mPlayer.start();
        super.onStart();
    }

    @Override
    protected void onResume() {
        mPlayer.reset();
        super.onResume();
    }
}