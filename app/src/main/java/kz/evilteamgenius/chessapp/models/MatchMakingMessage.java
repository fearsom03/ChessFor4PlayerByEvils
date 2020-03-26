package kz.evilteamgenius.chessapp.models;

import java.util.ArrayList;

import kz.evilteamgenius.chessapp.models.enums.MatchMakingMessageType;

public class MatchMakingMessage {

    private MatchMakingMessageType messageType;
    private int gameType;
    private ArrayList<String> players;

    public MatchMakingMessage(MatchMakingMessageType messageType, int gameType, ArrayList<String> players) {
        this.messageType = messageType;
        this.gameType = gameType;
        this.players = players;
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

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }


}
