package kz.evilteamgenius.chessapp.api.loaders;

import kz.evilteamgenius.chessapp.api.ApiError;
import kz.evilteamgenius.chessapp.api.ChessService;
import kz.evilteamgenius.chessapp.api.RetrofitErrorUtil;
import kz.evilteamgenius.chessapp.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeNewGameLoader {
    private GetMakeNewGameLoaderCallback getMakeNewGameLoaderCallback;

    public MakeNewGameLoader(GetMakeNewGameLoaderCallback getMakeNewGameLoaderCallback) {
        this.getMakeNewGameLoaderCallback = getMakeNewGameLoaderCallback;
    }

    public void loadMakeNewGame(String token){
        ChessService.getInstance().getJSONApi().makeNewGame("Bearer "+token).enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (response.isSuccessful()) {
                    getMakeNewGameLoaderCallback.onGetGoodsLoaded(response.body());
                } else {
                    ApiError apiError = RetrofitErrorUtil.parseError(response);
                    getMakeNewGameLoaderCallback.onResponseFailed(apiError.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {
                getMakeNewGameLoaderCallback.onResponseFailed(t.getMessage());
            }
        });
    }

    public interface GetMakeNewGameLoaderCallback {
        void onGetGoodsLoaded(Game game);
        void onResponseFailed(String errorMessage);
    }
}
