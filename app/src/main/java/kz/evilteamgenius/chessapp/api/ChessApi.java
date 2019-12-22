package kz.evilteamgenius.chessapp.api;

import kz.evilteamgenius.chessapp.api.responses.ResponseForRegistration;
import kz.evilteamgenius.chessapp.models.Game;
import kz.evilteamgenius.chessapp.models.RegisterMyUser;
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

//        or instead of id we may use Object
//        I use USER class for both of it like as example
//        Actually here we need to use it like response and request
//        @POST("/posts")
//        public Call<User> postData(@Body User data);

}
