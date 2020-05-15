package kz.evilteamgenius.chessapp.activity;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import kz.evilteamgenius.chessapp.BuildConfig;
import timber.log.Timber;

public class ApplicationController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new CrashReportingTree());
    }

    private class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable throwable) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
            Bundle params = new Bundle();
            params.putString("message", message);
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics
                    .getInstance(getApplicationContext());
            mFirebaseAnalytics.logEvent(tag, params);
        }
    }
}
