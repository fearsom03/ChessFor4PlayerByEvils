package kz.evilteamgenius.chessapp;

public class Const {
	public static final String placeholder = "placeholder";
	public static final String address = "ws://192.168.0.106:8080/kchess/websocket";
	public static final String makeMatchAddress = "/app/makeMatch";
	public static final String makeMatchResponse = "/queue/makeMatch-" + placeholder;
	public static final String makeMoveAddress = "/app/makeMove";
	public static final String makeMoveResponse = "/queue/msg-" + placeholder;
}
