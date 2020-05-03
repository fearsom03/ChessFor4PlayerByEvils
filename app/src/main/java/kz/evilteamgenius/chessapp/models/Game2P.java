package kz.evilteamgenius.chessapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Game2P implements Serializable {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("player1")
    @Expose
    private String player1;

    @SerializedName("player2")
    @Expose
    private String player2;

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

    @SerializedName("game_type")
    @Expose
    private int game_type;

    public Game2P(Long id, String player1, String player2, String FEN, String result, int from_x, int from_y, int to_x, int to_y, String made_by, int game_type) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.FEN = FEN;
        this.result = result;
        this.from_x = from_x;
        this.from_y = from_y;
        this.to_x = to_x;
        this.to_y = to_y;
        this.made_by = made_by;
        this.game_type = game_type;
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

    public int getGame_type() {
        return game_type;
    }

    public void setGame_type(int game_type) {
        this.game_type = game_type;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", white='" + player1 + '\'' +
                ", black='" + player2 + '\'' +
                ", fen='" + FEN + '\'' +
                ", result='" + result + '\'' +
                ", from_x=" + from_x +
                ", from_y=" + from_y +
                ", to_x=" + to_x +
                ", to_y=" + to_y +
                ", next_move='" + made_by + '\'' +
                '}';
    }
}