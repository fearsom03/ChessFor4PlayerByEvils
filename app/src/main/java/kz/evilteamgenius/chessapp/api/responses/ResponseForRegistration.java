package kz.evilteamgenius.chessapp.api.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseForRegistration {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("content")
    @Expose
    private List<String> arr;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getArr() {
        return arr;
    }

    public void setArr(List<String> arr) {
        this.arr = arr;
    }
}
