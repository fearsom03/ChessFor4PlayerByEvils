package kz.evilteamgenius.chessapp.api.loaders;

import kz.evilteamgenius.chessapp.api.ApiError;
import kz.evilteamgenius.chessapp.api.ChessService;
import kz.evilteamgenius.chessapp.api.RetrofitErrorUtil;
import kz.evilteamgenius.chessapp.models.Game2P;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeNew2PGameLoader {
    private GetMakeNewGameLoaderCallback getMakeNewGameLoaderCallback;

    public MakeNew2PGameLoader(GetMakeNewGameLoaderCallback getMakeNewGameLoaderCallback) {
        this.getMakeNewGameLoaderCallback = getMakeNewGameLoaderCallback;
    }

    public void loadMakeNew2PGame(String token, int mode){
        ChessService.getInstance().getJSONApi().makeNewGame2P("Bearer "+token, mode).enqueue(new Callback<Game2P>() {
            @Override
            public void onResponse(Call<Game2P> call, Response<Game2P> response) {
                if (response.isSuccessful()) {
                    getMakeNewGameLoaderCallback.onGetGoodsLoaded(response.body());
                } else {
                    ApiError apiError = RetrofitErrorUtil.parseError(response);
                    getMakeNewGameLoaderCallback.onResponseFailed(apiError.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Game2P> call, Throwable t) {
                getMakeNewGameLoaderCallback.onResponseFailed(t.getMessage());
            }
        });
    }

    public interface GetMakeNewGameLoaderCallback {
        void onGetGoodsLoaded(Game2P game2P);
        void onResponseFailed(String errorMessage);
    }
}
