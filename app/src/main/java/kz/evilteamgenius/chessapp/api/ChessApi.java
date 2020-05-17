package kz.evilteamgenius.chessapp.api;

import kz.evilteamgenius.chessapp.api.responses.ResponseForRegistration;
import kz.evilteamgenius.chessapp.models.Game;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChessApi {

        @POST("registration/submit")
        Call<ResponseForRegistration> registerUser(@Body RequestBody user);

        @Headers({ "Content-Type: application/json;charset=UTF-8"})
        @GET("game/new/{type}")
        Call<Game> makeNewGame2P(@Header("Authorization") String authHeader,
                                 @Path("type") int type);

        @POST("auth/token")
        Call<String> loginUser(@Body RequestBody body);

        @GET("game/get/move/{id}")
        Call<Game> getLastMove(@Header("cache-control") String header,
                               @Header("Authorization") String Authorization,
                               @Path("id") long id);

}
