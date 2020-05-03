package kz.evilteamgenius.chessapp.api.loaders;

import kz.evilteamgenius.chessapp.api.ApiError;
import kz.evilteamgenius.chessapp.api.ChessService;
import kz.evilteamgenius.chessapp.api.RetrofitErrorUtil;
import kz.evilteamgenius.chessapp.api.responses.ResponseForRegistration;
import kz.evilteamgenius.chessapp.models.RegisterMyUser;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationLoader {
    private GetRegistrationLoaderCallback mCallback;

    public RegistrationLoader(GetRegistrationLoaderCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void loadRegistration(RegisterMyUser myUser){

        RequestBody formBody = new FormBody.Builder()
                .add("username", myUser.getLogin())
                .add("password", myUser.getPass())
                .build();

        ChessService.getInstance().getJSONApi().
                registerUser(formBody)
                .enqueue(new Callback<ResponseForRegistration>() {
                    @Override
                    public void onResponse(Call<ResponseForRegistration> call, Response<ResponseForRegistration> response) {
                        if (response.isSuccessful()) {
                            mCallback.onGetGoodsLoaded(response.body());
                        } else {
                            ApiError apiError = RetrofitErrorUtil.parseError(response);
                            mCallback.onResponseFailed(apiError.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseForRegistration> call, Throwable t) {
                        mCallback.onResponseFailed(t.getMessage());
                    }
                });

    }

    public interface GetRegistrationLoaderCallback {
        void onGetGoodsLoaded(ResponseForRegistration responseForRegistration);
        void onResponseFailed(String errorMessage);
    }
}
