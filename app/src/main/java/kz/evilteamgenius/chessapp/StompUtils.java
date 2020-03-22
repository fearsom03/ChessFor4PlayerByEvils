package kz.evilteamgenius.chessapp;

import android.util.Log;

import ua.naiksoftware.stomp.StompClient;
import static kz.evilteamgenius.chessapp.Const.TAG;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.*;

public class StompUtils {
    @SuppressWarnings({"ResultOfMethodCallIgnored", "CheckResult"})
    public static void lifecycle(StompClient stompClient) {
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    break;

                case ERROR:
                    Log.e(TAG, "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.d(TAG, "Stomp connection closed");
                    break;
            }
        });
    }
}
