package kz.evilteamgenius.chessapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterMyUser {
    @SerializedName("username")
    @Expose
    private String login;

    @SerializedName("password")
    @Expose
    private String pass;

    @SerializedName("passwordVerification")
    @Expose
    private String passCheck;



    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPassCheck() {
        return passCheck;
    }

    public void setPassCheck(String passCheck) {
        this.passCheck = passCheck;
    }
}
