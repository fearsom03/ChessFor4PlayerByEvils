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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuka);
        fragment =new  NavigationPageFragment();
        replaceFragment(fragment);
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
}