package kz.evilteamgenius.chessapp.models;

import kz.evilteamgenius.chessapp.models.enums.MatchMakingMessageType;

public class MatchMakingMessage {

    private MatchMakingMessageType messageType;
    private int gameType;
    private String[] players;
    private String room_id;

    public MatchMakingMessage(MatchMakingMessageType messageType, int gameType, String[] players, String room_id) {
        this.messageType = messageType;
        this.gameType = gameType;
        this.players = players;
        this.room_id = room_id;
    }

    public MatchMakingMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MatchMakingMessageType messageType) {
        this.messageType = messageType;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
}
