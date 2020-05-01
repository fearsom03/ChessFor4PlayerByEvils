package kz.evilteamgenius.chessapp.api.requests

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequestModel {
    // maybe later I will init it in loginActivity loginFunction
    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    constructor()

    constructor(username: String?, password: String?) {
        this.username = username
        this.password = password
    }

}