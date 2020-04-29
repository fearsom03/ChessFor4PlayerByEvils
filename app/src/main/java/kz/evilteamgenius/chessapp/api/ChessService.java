package kz.evilteamgenius.chessapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static kz.evilteamgenius.chessapp.Constants.UrlDefault;

public class ChessService {
    private static final String EndPoint = UrlDefault + "api/";
    private static ChessService chessService;

    private Retrofit mRetrofit;

    private ChessService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(EndPoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();
    }

    public static ChessService getInstance() {
        if (chessService == null) {
            chessService = new ChessService();
        }
        return chessService;
    }

    public ChessApi getJSONApi() {
        return mRetrofit.create(ChessApi.class);
    }
}
