package kz.evilteamgenius.chessapp.activity;

import android.app.Application;

import kz.evilteamgenius.chessapp.BuildConfig;
import timber.log.Timber;

public class ApplicationController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
