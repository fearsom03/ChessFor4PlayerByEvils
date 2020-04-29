/*
 * Copyright (c) 2020.  Kuanysh Salyk, All rights reserved.
 */

package kz.evilteamgenius.chessapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import kz.evilteamgenius.chessapp.BuildConfig;
import kz.evilteamgenius.chessapp.Constants;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitErrorUtil {
    public static ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter =
                getRetrofit()
                        .responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error = new ApiError();
        error.setStatus_code(010);

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            ApiError networkError = new ApiError();
            if (response.code() == 404) {
                networkError.setStatus_code(404);
                networkError.setMessage("PAGE NOT FOUND");
                return networkError;
            } else if (response.code() == 403) {
                networkError.setStatus_code(403);
                networkError.setMessage("Fetching problem");
                return networkError;
            } else if (e.getCause() instanceof SocketTimeoutException) {
                networkError.setStatus_code(000);
                networkError.setMessage("Poor connection");
                return networkError;
            } else if (e.getCause() instanceof UnknownHostException
                    || e.getCause() instanceof ConnectException) {
                networkError.setStatus_code(000);
                networkError.setMessage("NO Internet connection");
                return networkError;
            } else {
                networkError.setStatus_code(000);
                networkError.setMessage("Fetching problem");
                return networkError;
            }
        }
        return error;
    }

    private static Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client;

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG)
            builder.addInterceptor(interceptor);
        client = builder.build();


        Gson gson = new GsonBuilder().create();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constants.Url_BASE)
                .client(client)
                .build();
    }

}
