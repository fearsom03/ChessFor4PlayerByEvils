package kz.evilteamgenius.chessapp.api.loaders;

import kz.evilteamgenius.chessapp.api.ApiError;
import kz.evilteamgenius.chessapp.api.ChessService;
import kz.evilteamgenius.chessapp.api.RetrofitErrorUtil;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginLoader {

    private LoginCallback loginCallback;

    public LoginLoader(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    public void loginUser(String name, String pass) {
        RequestBody formBody = new FormBody.Builder()
                .add("username", name)
                .add("password", pass)
                .build();

        ChessService.getInstance().getJSONApi().loginUser(formBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            loginCallback.onGetGoodsLoaded(response.body(), name);
                        } else {
                            ApiError apiError = RetrofitErrorUtil.parseError(response);
                            loginCallback.onResponseFailed(apiError.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        loginCallback.onResponseFailed(t.getMessage());
                    }
                });
    }

    public interface LoginCallback {
        void onGetGoodsLoaded(String responseForRegistration, String username);

        void onResponseFailed(String errorMessage);
    }
}
