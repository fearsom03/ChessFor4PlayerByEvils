package kz.evilteamgenius.chessapp.api.loaders;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.activity.LoginActivity;
import kz.evilteamgenius.chessapp.activity.RegistrationActivity;
import kz.evilteamgenius.chessapp.api.ChessService;
import kz.evilteamgenius.chessapp.api.responses.ResponseForRegistration;
import kz.evilteamgenius.chessapp.models.RegisterMyUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationLoader {
    private GetRegistrationLoaderCallback mCallback;

    public RegistrationLoader(GetRegistrationLoaderCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void loadRegistration(RegisterMyUser myUser){
        ChessService.getInstance().getJSONApi().
                registerUser(myUser)
                .enqueue(new Callback<ResponseForRegistration>() {
                    @Override
                    public void onResponse(Call<ResponseForRegistration> call, Response<ResponseForRegistration> response) {
                        mCallback.onGetGoodsLoaded(response.body());
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
