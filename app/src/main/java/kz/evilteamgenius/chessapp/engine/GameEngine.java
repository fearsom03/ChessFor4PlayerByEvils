package kz.evilteamgenius.chessapp.engine;

import android.content.Context;
import android.graphics.Color;

import java.util.LinkedList;
import java.util.List;

import kz.evilteamgenius.chessapp.Const;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.fragments.GameFragment;
import timber.log.Timber;

/*
 * Copyright 2014 Thomas Hoffmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class GameEngine {

    private final static int PROTOCOL_VERSION = 1;

    private final static int[] PLAYER_COLOR =
            {Color.parseColor("#FF8800"), Color.parseColor("#99CC00"), Color.parseColor("#33B5E5"),
                    Color.parseColor("#CC0000")};

    public final static int MODE_2_PLAYER_2_SIDES = 1;
    public final static int MODE_2_PLAYER_4_SIDES = 2;
    public final static int MODE_4_PLAYER_TEAMS = 3;
    public final static int MODE_4_PLAYER_NO_TEAMS = 4;

    public static String myPlayerId;
    public static String myPlayerUserame;
    public static Match match;
    public static Player[] players;
    public static int turns;

    public static kz.evilteamgenius.chessapp.models.Game game;
    public static String roomAdress;
    private static List<String> deadPlayers;

    public static GameFragment UI;

    public static String matchModeToName(final Context c, int mode) {
        switch (mode) {
            default:
            case MODE_2_PLAYER_2_SIDES:
                return c.getString(R.string.mode1);
            case MODE_2_PLAYER_4_SIDES:
                return c.getString(R.string.mode2);
            case MODE_4_PLAYER_TEAMS:
                return c.getString(R.string.mode3);
            case MODE_4_PLAYER_NO_TEAMS:
                return c.getString(R.string.mode4);
        }
    }

    /**
     * Should be called when a move is made
     */
    public static void moved() {

        turns++;
        String next = players[turns % players.length].id;
        while (deadPlayers.contains(next)) {

            turns++; // skip dead players
            next = players[turns % players.length].id;
        }

        if (next.startsWith("AutoMatch_")) next = null;
        if (!match.isLocal) {
            //TODO SEND MOVES TO WEBSCOKET
            //Games.TurnBasedMultiplayer.takeTurn(api, match.id, toBytes(), next);
        }
        if (UI != null) UI.updateTurn();
    }

    public static Player getNextPlayer(){
        int now = turns + 1;
        Player next = players[now % players.length];
        while (deadPlayers.contains(next)) {
            now++; // skip dead players
            next = players[now % players.length];
        }
        return next;
    }
    public static void save(final Context c) {
        if (match.isLocal) {
            c.getSharedPreferences("localMatches", Context.MODE_PRIVATE).edit()
                    .putString("match_" + match.id + "_" + match.mode, new String(toBytes()))
                    .apply();
        }
    }

    /**
     * Gets the ID of the winner team
     *
     * @return the team-id of the winner team
     */
    public static int getWinnerTeam() {
        return getWinner().team;
    }

    /**
     * Gets the player you has won the game
     *
     * @return the winner
     */
    private static Player getWinner() {
        for (Player p : players) {
            if (!deadPlayers.contains(p.id)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Game over
     */
    public static void over() {
        int winnerTeam = getWinnerTeam();
        if (!match.isLocal) {
            //TODO SEND OVER MESSAGE TO WEBSOCKET
//            System.out.println("Game.over state: " + match.getStatus());
//            if (match.getStatus() == TurnBasedMatch.MATCH_STATUS_ACTIVE) {
//                List<ParticipantResult> result = new ArrayList<ParticipantResult>(players.length);
//                for (Player p : players) {
//                    result.add(new ParticipantResult(p.id,
//                            p.team == winnerTeam ? ParticipantResult.MATCH_RESULT_WIN :
//                                    ParticipantResult.MATCH_RESULT_LOSS,
//                            ParticipantResult.PLACING_UNINITIALIZED));
//                    System.out.println(p.id + " " + (p.team == winnerTeam ? "win" : "loss"));
//                }
//                Games.TurnBasedMultiplayer.finishMatch(api, match.id, toBytes(), result);
//            } else {
//                Games.TurnBasedMultiplayer.finishMatch(api, match.id);
//            }
//            if (UI != null) UI.gameOver(winnerTeam == getPlayer(myPlayerId).team);
        } else {
            if (UI != null) UI.gameOverLocal(getWinner());
        }
    }

    /**
     * Checks if the game is over
     *
     * @return true, if the game is over
     */
    public static boolean isGameOver() {
        String someText = deadPlayers.size() + ((deadPlayers.size() == 2) ?
                " sameTeam: " + sameTeam(deadPlayers.get(0), deadPlayers.get(1)) :
                "-");
        Timber.d("Game.isGameOver: #Player: %s  #Dead:  %s "
                , String.valueOf(players.length)
                , someText);
        return (players.length - deadPlayers.size() <= 1) ||
                (deadPlayers.size() == 2 && sameTeam(deadPlayers.get(0), deadPlayers.get(1)));
    }

    /**
     * Removes a player from the game
     *
     * @param playerId the player to remove
     * @return true, if the game is now over
     */
    public static boolean removePlayer(final String playerId) {
        if (players.length > 2) Board.removePlayer(playerId);
        deadPlayers.add(playerId);
        return isGameOver();
    }

    public static boolean isPlayerAlive(final String playerId) {
        if(deadPlayers.contains(playerId))
            return false;
        return true;
    }

    /**
     * Check if this client should move its piece
     *
     * @return true, if it's this client's turn
     */
    public static boolean myTurn() {
        return match.isLocal || myPlayerId.equals(players[turns % players.length].id);
    }

    public static boolean isTurnRight(String id) {
        return match.isLocal || id.equals(players[turns % players.length].id);
    }

    /**
     * Gets the id for the currently active player.
     *
     * @return the id of the currently active player
     */
    public static String currentPlayer() {
        return players[turns % players.length].id;
    }


    /*
      Load game data

      @param data the data to load
     * @param m    the match
     * @param w    the ApiClient
     * @return false, if protocol version is too old and the app should be updated first
     */
//    public static boolean load(final byte[] data, final Match m, final WSHelper w) {
//        System.out.println("  load: " + (new String(data)));
//        String[] s = new String(data).split(":");
//        // newer protocol used for the match
//        if (s.length > 6 && s[6] != null && Integer.parseInt(s[6]) > PROTOCOL_VERSION) {
//            return false;
//        }
//        wsHelper = w;
//        match = m;
//        if (s.length > 5 && s[5] != null) {
//            match.mode = Integer.parseInt(s[5]);
//        }
//        turns = Integer.parseInt(s[0]);
//        deadPlayers = new LinkedList<String>();
//        if (s.length > 3 && s[3] != null) {
//            for (String dead : s[3].split(",")) {
//                System.out.println("  dead: " + dead);
//                if (dead != null && dead.length() > 0) deadPlayers.add(dead);
//            }
//        }
//        createPlayers();
//        System.out.println("Game.load myPlayerId: " + myPlayerId + " playersInData: " + s[1]);
//        if (!s[1].contains(players[1].id)) {
//            s[2] = s[2].replace("AutoMatch_2", players[1].id);
//        }
//        if (players.length > 2) {
//            if (!s[1].contains(players[2].id)) {
//                s[2] = s[2].replace("AutoMatch_3", players[2].id);
//            }
//            if (!s[1].contains(players[3].id)) {
//                s[2] = s[2].replace("AutoMatch_4", players[3].id);
//            }
//        }
//        Board.load(s[2], match.mode);
//        if (s.length > 4 && s[4] != null) {
//            String[] lastMoves = s[4].split(";");
//            String[] coords;
//            for (int i = 0; i < lastMoves.length; i++) {
//                System.out.println("  lastMove: " + lastMoves[i]);
//                if (lastMoves[i].equals("-")) continue;
//                coords = lastMoves[i].split(",");
//                players[i].lastMove = new Pair<Coordinate, Coordinate>(
//                        new Coordinate(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
//                                Board.getRotation()),
//                        new Coordinate(Integer.parseInt(coords[2]), Integer.parseInt(coords[3]),
//                                Board.getRotation()));
//            }
//        }
//        if (isGameOver()) over();
//        return true;
//    }

    /**
     * Returns the byte-array representation of the game
     *
     * @return the byte-array representation of the game
     */
    private static byte[] toBytes() {
        StringBuilder sb = new StringBuilder(turns + ":" + players[0].id + "," + players[1].id);
        if (players.length > 2)
            sb.append(",").append(players[2].id).append(",").append(players[3].id);
        sb.append(":").append(Board.getString()).append(":");
        for (String dead : deadPlayers)
            sb.append(dead).append(",");
        sb.append(":");
        Coordinate oldPos, newPos;
        for (Player player : players) {
            if (player.lastMove == null) {
                sb.append("-");
            } else {
                oldPos = new Coordinate(player.lastMove.first.x, player.lastMove.first.y,
                        (4 - Board.getRotation()) % 4);
                newPos = new Coordinate(player.lastMove.second.x, player.lastMove.second.y,
                        (4 - Board.getRotation()) % 4);
                sb.append(oldPos.toString()).append(",").append(newPos.toString());
            }
            sb.append(";");
        }
        sb.append(":").append(match.mode).append(":").append(PROTOCOL_VERSION);
        Timber.d("  save: %s", sb.toString());
        return sb.toString().getBytes();
    }

    /**
     * Initiate a new game
     *
     * @param m the match
     */
    public static void newGame(final Match m, String[] receivedPlayers, String myName, String room_id) {
//        stompClient = s;
        match = m;
        turns = 0;
        myPlayerId = "0";
        deadPlayers = new LinkedList<>();
        createPlayers(receivedPlayers, myName);
        Timber.d("Game.newGame, players: %s", players.length);
        Board.newGame(players);
        if (room_id != null)
            roomAdress = Const.makeMoveAddress.replace(Const.placeholder, room_id);
    }

    /**
     * Creates the player objects
     */
    private static void createPlayers(String[] receivedPlayers, String myName) {
        boolean ifNull = receivedPlayers == null;
        int num_players = match.getNumPlayers();
        players = new Player[num_players];
        for (int i = 0; i < num_players; i++) {
            players[i] =
                    new Player(String.valueOf(i), match.mode == MODE_4_PLAYER_TEAMS ? i % 2 : i,
                            PLAYER_COLOR[i], ifNull ? "Player " + (i + 1) : receivedPlayers[i]);
            if (!match.isLocal) {
                if (receivedPlayers[i].equals(myName)) {
                    myPlayerId = String.valueOf(i);
                    myPlayerUserame = myName;
                }
            }

        }
        String someText = players[1].id +
                ((players.length > 2) ? ", " + players[2].id + ", " + players[3].id : "");
        Timber.d("Game.createPlayers , %s , %s ", players[0].id, someText);
    }

    /**
     * Checks if id1 and id2 are on the same team
     *
     * @param id1 player1
     * @param id2 player2
     * @return true, if player1 and player2 are on the same team
     */
    public static boolean sameTeam(final String id1, final String id2) {
        return getPlayer(id1).team == getPlayer(id2).team;
    }

    /**
     * Gets the color of the player with the id 'id'
     *
     * @param id the player id
     * @return the player's color
     */
    public static int getPlayerColor(final String id) {
        return getPlayer(id).color;
    }

    /**
     * Gets the player object to the given id
     *
     * @param id the player id
     * @return the player or null, if no such player exists
     */
    public static Player getPlayer(final String id) {
        for (Player p : players) {
            if (p.id.equals(id)) return p;
        }
        return null;
    }
}
