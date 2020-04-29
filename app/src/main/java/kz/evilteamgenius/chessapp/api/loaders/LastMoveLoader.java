package kz.evilteamgenius.chessapp.api.loaders;

import kz.evilteamgenius.chessapp.api.ApiError;
import kz.evilteamgenius.chessapp.api.ChessService;
import kz.evilteamgenius.chessapp.api.RetrofitErrorUtil;
import kz.evilteamgenius.chessapp.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastMoveLoader {
    LastMoveCallback lastMoveCallback;

    public LastMoveLoader(LastMoveCallback lastMoveCallback) {
        this.lastMoveCallback = lastMoveCallback;
    }

    public void getLastMove(String token) {
        String authentication = "Bearer " + token;
        ChessService.getInstance().getJSONApi().
                getLastMove("no-cache", authentication).enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (response.isSuccessful()) {
                    lastMoveCallback.onMoveLoaded(response.body());
                } else {
                    ApiError apiError = RetrofitErrorUtil.parseError(response);
                    lastMoveCallback.onResponseFailed(apiError.getMessage());
                }
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

