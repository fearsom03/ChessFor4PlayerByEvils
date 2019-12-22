package kz.evilteamgenius.chessapp.api.loaders;

import kz.evilteamgenius.chessapp.api.ChessService;
import kz.evilteamgenius.chessapp.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastMoveLoader {
    LastMoveCallback lastMoveCallback;

    public LastMoveLoader(LastMoveCallback lastMoveCallback) {
        this.lastMoveCallback = lastMoveCallback;
    }

    public void getLastMove(String token){
        String authentication = "Bearer " + token;
        ChessService.getInstance().getJSONApi().
                getLastMove("no-cache",authentication).enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                lastMoveCallback.onMoveLoaded(response.body());
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                lastMoveCallback.onResponseFailed(t.getMessage());
            }
        });
    }

    public interface LastMoveCallback {
        void onMoveLoaded(Game game);
        void onResponseFailed(String errorMessage);
    }

}

