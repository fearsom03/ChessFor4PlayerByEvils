package kz.evilteamgenius.chessapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Game implements Serializable {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("player1")
    @Expose
    private String player1;

    @SerializedName("player2")
    @Expose
    private String player2;

    @SerializedName("player3")
    @Expose
    private String player3;

    @SerializedName("player4")
    @Expose
    private String player4;

    @SerializedName("FEN")
    @Expose
    private String FEN;

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("from_x")
    @Expose
    private int from_x;

    @SerializedName("from_y")
    @Expose
    private int from_y;

    @SerializedName("to_x")
    @Expose
    private int to_x;

    @SerializedName("to_y")
    @Expose
    private int to_y;

    @SerializedName("made_by")
    @Expose
    private String made_by;

    @SerializedName("type")
    @Expose
    private int type;

    public Game(Long id, String player1, String player2, String player3, String player4, String FEN, String result, int from_x, int from_y, int to_x, int to_y, String made_by, int type) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.FEN = FEN;
        this.result = result;
        this.from_x = from_x;
        this.from_y = from_y;
        this.to_x = to_x;
        this.to_y = to_y;
        this.made_by = made_by;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer3() {
        return player3;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public String getPlayer4() {
        return player4;
    }

    public void setPlayer4(String player4) {
        this.player4 = player4;
    }

    public String getFEN() {
        return FEN;
    }

    public void setFEN(String FEN) {
        this.FEN = FEN;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getFrom_x() {
        return from_x;
    }

    public void setFrom_x(int from_x) {
        this.from_x = from_x;
    }

    public int getFrom_y() {
        return from_y;
    }

    public void setFrom_y(int from_y) {
        this.from_y = from_y;
    }

    public int getTo_x() {
        return to_x;
    }

    public void setTo_x(int to_x) {
        this.to_x = to_x;
    }

    public int getTo_y() {
        return to_y;
    }

    public void setTo_y(int to_y) {
        this.to_y = to_y;
    }

    public String getMade_by() {
        return made_by;
    }

    public void setMade_by(String made_by) {
        this.made_by = made_by;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", player3='" + player3 + '\'' +
                ", player4='" + player4 + '\'' +
                ", FEN='" + FEN + '\'' +
                ", result='" + result + '\'' +
                ", from_x=" + from_x +
                ", from_y=" + from_y +
                ", to_x=" + to_x +
                ", to_y=" + to_y +
                ", made_by='" + made_by + '\'' +
                ", type=" + type +
                '}';
    }
}
