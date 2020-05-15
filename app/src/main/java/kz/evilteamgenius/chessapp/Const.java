package kz.evilteamgenius.chessapp;

public class Const {
	public static final String placeholder = "placeholder";
    public static final String address = "ws://192.168.0.101:8080/kchess/websocket";
	public static final String makeMatchAddress = "/app/makeMatch";
	public static final String makeMatchResponse = "/queue/" + placeholder;
	public static final String makeMoveAddress = "/topic/"+placeholder;
	public static final String makeMoveResponse = "/topic/"+placeholder;


    public static final String NOTIFICATION = "NOTIFICATION";
    public static final String PLAY_MUSIC = "MUSIC";
}
