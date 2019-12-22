package kz.evilteamgenius.chessapp.api;

import kz.evilteamgenius.chessapp.api.responses.ResponseForRegistration;
import kz.evilteamgenius.chessapp.models.Game;
import kz.evilteamgenius.chessapp.models.RegisterMyUser;
import kz.evilteamgenius.chessapp.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChessApi {

        @POST("registration/submit")
        public Call<ResponseForRegistration> registerUser(@Body RegisterMyUser user);

        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        @GET("game/new")
        public Call<Game> makeNewGame(@Header("Authorization") String authHeader);

        @POST("auth/token")
        public Call<String> loginUser(@Body User user);

        @GET("game/get/move")
//        @Headers({
//                "cache-control: application/vnd.yourapi.v1.full+json",
//                "Authorization: Bearer"
//        })
        public Call<Game> getLastMove(@Header("cache-control") String header, @Header("Authorization") String Authorization);

}
