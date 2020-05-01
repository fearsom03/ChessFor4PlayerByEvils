package kz.evilteamgenius.chessapp.api;

import kz.evilteamgenius.chessapp.api.responses.ResponseForRegistration;
import kz.evilteamgenius.chessapp.models.Game;
import kz.evilteamgenius.chessapp.models.RegisterMyUser;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChessApi {

        @POST("registration/submit")
        Call<ResponseForRegistration> registerUser(@Body RegisterMyUser user);

        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        @GET("game/new")
        Call<Game> makeNewGame(@Header("Authorization") String authHeader);

        @POST("auth/token")
        Call<String> loginUser(@Body RequestBody body);

        @GET("game/get/move")
//        @Headers({
//                "cache-control: application/vnd.yourapi.v1.full+json",
//                "Authorization: Bearer"
//        })
        Call<Game> getLastMove(@Header("cache-control") String header, @Header("Authorization") String Authorization);

}
