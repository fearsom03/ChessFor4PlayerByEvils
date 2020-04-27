package kz.evilteamgenius.chessapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Game implements Serializable {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("white")
    @Expose
    private String white;

    @SerializedName("black")
    @Expose
    private String black;

    @SerializedName("fen")
    @Expose
    private String fen;

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

    @SerializedName("next_move")
    @Expose
    private String next_move;

    public Game(Long id, String white, String black, String fen, String result, int from_x, int from_y, int to_x, int to_y, String next_move) {
        this.id = id;
        this.white = white;
        this.black = black;
        this.fen = fen;
        this.result = result;
        this.from_x = from_x;
        this.from_y = from_y;
        this.to_x = to_x;
        this.to_y = to_y;
        this.next_move = next_move;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
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

    public String getNext_move() {
        return next_move;
    }

    public void setNext_move(String next_move) {
        this.next_move = next_move;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", white='" + white + '\'' +
                ", black='" + black + '\'' +
                ", fen='" + fen + '\'' +
                ", result='" + result + '\'' +
                ", from_x=" + from_x +
                ", from_y=" + from_y +
                ", to_x=" + to_x +
                ", to_y=" + to_y +
                ", next_move='" + next_move + '\'' +
                '}';
    }
}
