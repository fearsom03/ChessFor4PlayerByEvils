package kz.evilteamgenius.chessapp;

import timber.log.Timber;
import ua.naiksoftware.stomp.StompClient;

public class StompUtils {
    @SuppressWarnings({"ResultOfMethodCallIgnored", "CheckResult"})
    public static void lifecycle(StompClient stompClient) {
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Timber.i("Stomp connection opened");
                    break;

                case ERROR:
                    Timber.e("Error%s", lifecycleEvent.getException().toString());
                    break;

                case CLOSED:
                    Timber.i("Stomp connection closed");
                    break;
            }
        });
    }
}
