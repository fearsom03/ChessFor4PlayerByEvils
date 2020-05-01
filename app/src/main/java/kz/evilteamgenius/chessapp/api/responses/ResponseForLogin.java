package kz.evilteamgenius.chessapp.api.responses;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ResponseForLogin implements Serializable {
    @Expose
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
