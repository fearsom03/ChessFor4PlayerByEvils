package kz.evilteamgenius.chessapp.api.loaders;

import kz.evilteamgenius.chessapp.api.ChessService;
import kz.evilteamgenius.chessapp.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginLoader {

    private LoginCallback loginCallback;

    public LoginLoader(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    public void loginUser(User user){
        ChessService.getInstance().getJSONApi().
                loginUser(user)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        loginCallback.onGetGoodsLoaded(response.message().toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        loginCallback.onResponseFailed(t.getMessage());
                    }
                });
    }

    public interface LoginCallback {
        void onGetGoodsLoaded(String responseForRegistration);
        void onResponseFailed(String errorMessage);
    }}
