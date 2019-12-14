package kz.evilteamgenius.chessapp.api;

import kz.evilteamgenius.chessapp.api.responses.ResponseForRegistration;
import kz.evilteamgenius.chessapp.models.RegisterMyUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChessApi {

        @POST("registration/submit")
        public Call<ResponseForRegistration> registerUser(@Body RegisterMyUser user);

//        or instead of id we may use Object
//        I use USER class for both of it like as example
//        Actually here we need to use it like response and request
//        @POST("/posts")
//        public Call<User> postData(@Body User data);

}
