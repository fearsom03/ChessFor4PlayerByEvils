package kz.evilteamgenius.chessapp.api.loaders;

import kz.evilteamgenius.chessapp.api.ApiError;
import kz.evilteamgenius.chessapp.api.ChessService;
import kz.evilteamgenius.chessapp.api.RetrofitErrorUtil;
import kz.evilteamgenius.chessapp.models.Game2P;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastMove2PLoader {
    LastMoveCallback lastMoveCallback;

    public LastMove2PLoader(LastMoveCallback lastMoveCallback) {
        this.lastMoveCallback = lastMoveCallback;
    }

    public void getLastMove(String token) {
        String authentication = "Bearer " + token;
        ChessService.getInstance().getJSONApi().
                getLastMove2P("no-cache", authentication).enqueue(new Callback<Game2P>() {
            @Override
            public void onResponse(Call<Game2P> call, Response<Game2P> response) {
                if (response.isSuccessful()) {
                    lastMoveCallback.onMoveLoaded(response.body());
                } else {
                    ApiError apiError = RetrofitErrorUtil.parseError(response);
                    lastMoveCallback.onResponseFailed(apiError.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Game2P> call, Throwable t) {
                lastMoveCallback.onResponseFailed(t.getMessage());
            }
        });
    }

    public interface LastMoveCallback {
        void onMoveLoaded(Game2P game2P);
        void onResponseFailed(String errorMessage);
    }

}

