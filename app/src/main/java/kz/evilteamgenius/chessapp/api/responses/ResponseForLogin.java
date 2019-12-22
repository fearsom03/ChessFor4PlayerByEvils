package kz.evilteamgenius.chessapp.api.responses;

import com.google.gson.annotations.Expose;

public class ResponseForLogin {
    @Expose
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
