package kz.evilteamgenius.chessapp.engine;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Pair;

import kz.evilteamgenius.chessapp.activity.MainActivity;
import kz.evilteamgenius.chessapp.engine.pieces.Bishop;
import kz.evilteamgenius.chessapp.engine.pieces.DownPawn;
import kz.evilteamgenius.chessapp.engine.pieces.King;
import kz.evilteamgenius.chessapp.engine.pieces.Knight;
import kz.evilteamgenius.chessapp.engine.pieces.LeftPawn;
import kz.evilteamgenius.chessapp.engine.pieces.Pawn;
import kz.evilteamgenius.chessapp.engine.pieces.Piece;
import kz.evilteamgenius.chessapp.engine.pieces.Queen;
import kz.evilteamgenius.chessapp.engine.pieces.RightPawn;
import kz.evilteamgenius.chessapp.engine.pieces.Rook;
import timber.log.Timber;

public class Board {

    private Board() {
    }

    public static Piece[][] BOARD;
    public static int rotations;

    public static boolean extendedBoard; // true, if 12x12 board, false if 8x8

    /**
     * Remove all pieces belonging to the given player
     *
     * @param playerId the player id who's pieces should be removed
     */
    static void removePlayer(final String playerId) {
        int len = extendedBoard ? 12 : 8;
        for (int x = 0; x < len; x++) {
            for (int y = 0; y < len; y++) {
                if (BOARD[x][y] != null && playerId.equals(BOARD[x][y].getPlayerId())) {
                    BOARD[x][y] = null;
                }
            }
        }
    }

    /**
     * Move a piece
     *
     * @param old_pos old position of the piece
     * @param new_pos new position
     * @return false, if that move is not legal
     */
    public static boolean move(final Coordinate old_pos, final Coordinate new_pos, Context context) {

        //最后的赢家发出消息， 他赢了。
        boolean ifOver = false;
        if (!GameEngine.myTurn()) return false;

        if (!new_pos.isValid()) return false; // not a valid new position

        Piece p = BOARD[old_pos.x][old_pos.y];
        if (!p.getPossiblePositions(BOARD).contains(new_pos))
            return false; // not possible to move there

        Player me = GameEngine.getPlayer(GameEngine.currentPlayer());
        Piece target = BOARD[new_pos.x][new_pos.y];

        if (ifWillBeChecked(me, old_pos, new_pos))
            return false;

        // move the piece
        BOARD[new_pos.x][new_pos.y] = BOARD[old_pos.x][old_pos.y];
        BOARD[old_pos.x][old_pos.y] = null;
        p.position = new_pos;
        if (target != null)
            GameEngine.getPlayer(target.getPlayerId()).pieces.remove(target);

        me.lastMove = new Pair<>(old_pos, new_pos);
        if (checkForPromotion(p)) {
            me.pieces.remove(p);
            p = new Queen(p.position, p.getPlayerId());
            BOARD[new_pos.x][new_pos.y] = p;
            me.pieces.add(p);
        }
        // at the end of the turn, check if next player is checkmated, if so remove the player
        Player nextPlayer = GameEngine.getNextPlayer();
        checkUpgradePawn(nextPlayer);
        if (isCheckmated(nextPlayer)) {
            ifOver = GameEngine.removePlayer(nextPlayer.id);
        }

        if (ifOver) {
            // game ended
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Game over")
                    .setTitle("Game over");
            AlertDialog dialog = builder.create();
            dialog.show();
            if (!GameEngine.match.isLocal && GameEngine.myTurn()) {
                MainActivity.sendMove(old_pos, new_pos, true);
            }
            //GameEngine.over();
        } else {
            if (!GameEngine.match.isLocal && GameEngine.myTurn()) {
                MainActivity.sendMove(old_pos, new_pos, false);
            }
            GameEngine.moved();
        }
        return true;
    }

    private static void checkUpgradePawn(Player nextPlayer) {
        //todo need to do this thing EDIL
        // Why do we use static methods instead of object???
        if (Board.extendedBoard) {
            //extended board game
            if (BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]
                    instanceof DownPawn && nextPlayer.lastMove.second.y == 6) {
                //upgrade
                updatePawn(BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]);
                Timber.d("Upgrade needed");
            } else if (BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]
                    instanceof Pawn && nextPlayer.lastMove.second.y == 6) {
                //upgrade
                updatePawn(BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]);
                Timber.d("Upgrade needed");

            } else if (BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]
                    instanceof LeftPawn && nextPlayer.lastMove.second.x == 7) {
                //upgrade
                updatePawn(BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]);
                Timber.d("Upgrade needed");

            } else if (BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]
                    instanceof RightPawn && nextPlayer.lastMove.second.x == 7) {
                //upgrade
                updatePawn(BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]);
                Timber.d("Upgrade needed");
            }
        } else {
            //normal game
            if (BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]
                    instanceof DownPawn && nextPlayer.lastMove.second.y == 7) {
                //upgrade
                updatePawn(BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]);
                Timber.d("Upgrade needed");
            } else if (BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]
                    instanceof Pawn && nextPlayer.lastMove.second.y == 0) {
                //upgrade
                updatePawn(BOARD[nextPlayer.lastMove.second.x][nextPlayer.lastMove.second.y]);
                Timber.d("Upgrade needed");
            }
        }
    }

    private static void updatePawn(Piece pawn) {
        //do update here!!
    }

    private static boolean ifWillBeChecked(Player player, final Coordinate old_pos, final Coordinate new_pos) {
        boolean flag = false;
        King myKing = player.king;
        Piece[][] newBoard = cloneTheBoard();
        Piece moved = newBoard[old_pos.x][old_pos.y];
        Piece target = newBoard[new_pos.x][new_pos.y];
        newBoard[new_pos.x][new_pos.y] = moved;
        newBoard[old_pos.x][old_pos.y] = null;
        if (target != null)
            GameEngine.getPlayer(target.getPlayerId()).pieces.remove(target);
        moved.position = new_pos;
        for (Player p : GameEngine.players) {
            if (p.team == player.team || !GameEngine.isPlayerAlive(p.id))
                continue;
            for (Piece piece1 : p.pieces) {
                if (piece1.getPossiblePositions(newBoard).contains(myKing.position)) {
                    flag = true;
                    break;
                }
            }
            if (flag)
                break;
        }
        if (target != null)
            GameEngine.getPlayer(target.getPlayerId()).pieces.add(target);
        moved.position = old_pos;
        return flag;
    }

    private static boolean isInCheckNow(Player player) {
        King king = player.king;
        for (Player p : GameEngine.players) {
            if (p.team == player.team || !GameEngine.isPlayerAlive(p.id))
                continue;
            for (Piece piece : p.pieces) {
                if (piece.getPossiblePositions(BOARD).contains(king.position)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isCheckmated(Player player) {
        if (!isInCheckNow(player)) {
            return false;
        }
        King king = player.king;
        for (Piece piece : player.pieces) {
            Coordinate cur = piece.position;
            for (Coordinate target : piece.getPossiblePositions(BOARD)) {
                if (!ifWillBeChecked(player, cur, target))
                    return false;
            }
        }
        return true;
    }

    public static void moveWhenReceived(final Coordinate old_pos, final Coordinate new_pos, Context context) {
        boolean ifOver = false;
        Piece p = BOARD[old_pos.x][old_pos.y];
        if (p == null)
            return;
        Player cur_player = GameEngine.getPlayer(GameEngine.currentPlayer());
        Piece target = BOARD[new_pos.x][new_pos.y];
        BOARD[new_pos.x][new_pos.y] = BOARD[old_pos.x][old_pos.y];
        BOARD[old_pos.x][old_pos.y] = null;
        p.position = new_pos;  //error: p is null, and write to null.pos
        cur_player.lastMove =
                new Pair<>(old_pos, new_pos);

        if (checkForPromotion(p)) {
            cur_player.pieces.remove(p);
            p = new Queen(p.position, p.getPlayerId());
            BOARD[new_pos.x][new_pos.y] = p;
            cur_player.pieces.add(p);
        }

        Player nextPlayer = GameEngine.getNextPlayer();
        if (isCheckmated(nextPlayer)) {
            ifOver = GameEngine.removePlayer(nextPlayer.id);
        }

        if (ifOver) {
            // game ended
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Game over")
                    .setTitle("Game over");
            AlertDialog dialog = builder.create();
            dialog.show();
            //GameEngine.over();
        } else {
            GameEngine.moved();
        }
    }

    /**
     * Gets a piece from the board
     *
     * @param c the coordinate of the piece to get
     * @return the piece or null, if there is none at the given coordinate
     */
    public static Piece getPiece(final Coordinate c, Piece[][] board) {
        return board[c.x][c.y];
    }

    /**
     * Loads the game board from the given data
     *
     * @param data containing information about the state of the game
     */
    public static void load(final String data, int match_mode) {
        String[] pieceData;
        Coordinate c;
        extendedBoard = match_mode != GameEngine.MODE_2_PLAYER_2_SIDES;
        BOARD = extendedBoard ? new Piece[12][12] : new Piece[8][8];
        for (String piece : data.split(";")) {
            pieceData = piece.split(",");
            c = new Coordinate(Integer.parseInt(pieceData[0]), Integer.parseInt(pieceData[1]),
                    getRotation());
            switch (pieceData[3]) {
                case "Bishop":
                    BOARD[c.x][c.y] = new Bishop(c, pieceData[2]);
                    break;
                case "King":
                    BOARD[c.x][c.y] = new King(c, pieceData[2]);
                    break;
                case "Knight":
                    BOARD[c.x][c.y] = new Knight(c, pieceData[2]);
                    break;
                case "Pawn":
                    BOARD[c.x][c.y] = new Pawn(c, pieceData[2]);
                    break;
                case "Queen":
                    BOARD[c.x][c.y] = new Queen(c, pieceData[2]);
                    break;
                case "Rook":
                    BOARD[c.x][c.y] = new Rook(c, pieceData[2]);
                    break;
                case "LeftPawn":
                    BOARD[c.x][c.y] = new LeftPawn(c, pieceData[2]);
                    break;
                case "RightPawn":
                    BOARD[c.x][c.y] = new RightPawn(c, pieceData[2]);
                    break;
                case "DownPawn":
                    BOARD[c.x][c.y] = new DownPawn(c, pieceData[2]);
                    break;
            }
        }
    }

    /**
     * Get a string representation of this board
     *
     * @return the board, represented as a string
     */
    public static String getString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < (extendedBoard ? 12 : 8); x++) {
            for (int y = 0; y < (extendedBoard ? 12 : 8); y++) {
                if (BOARD[x][y] != null) {
                    sb.append(BOARD[x][y].toString());
                    sb.append(";");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Sets the player at the top or the bottom up
     *
     * @param x_begin  x-coordinate of the left-most piece
     * @param y_pawns  y-coordinate of the rows of pawns
     * @param y_others y-coordinate of the rows of other pieces
     * @param owner    player.id who owns these pieces
     */
    private static void setupPlayerTopBottom(int x_begin, int y_pawns, int y_others, final String owner) {
        Player player = GameEngine.getPlayer(owner);
        Piece modelPawn = new Pawn(new Coordinate(2, y_pawns, rotations), owner);
        for (int x = x_begin; x < x_begin + 8; x++) {
            Piece pawn;
            if (modelPawn.position.y == 1) {
                pawn = new Pawn(new Coordinate(x, y_pawns, rotations), owner);
            } else if (modelPawn.position.y == 6 || modelPawn.position.y == 10) {
                pawn = new DownPawn(new Coordinate(x, y_pawns, rotations), owner);
            } else if (modelPawn.position.x == 1) {
                pawn = new RightPawn(new Coordinate(x, y_pawns, rotations), owner);
            } else {
                pawn = new LeftPawn(new Coordinate(x, y_pawns, rotations), owner);
            }
            BOARD[pawn.position.x][pawn.position.y] = pawn;
            player.pieces.add(pawn);
        }
//        for (int x = x_begin; x < x_begin + 8; x++) {
//            Piece pawn = GameEngine.match.isLocal && (y_pawns == 6 || y_pawns == 10) ?
//                    new DownPawn(new Coordinate(x, y_pawns, rotations), owner) :
//                    new Pawn(new Coordinate(x, y_pawns, rotations), owner);
//            BOARD[pawn.position.x][pawn.position.y] = pawn;
//            player.pieces.add(pawn);
//        }
        Piece rook1 = new Rook(new Coordinate(x_begin, y_others, rotations), owner);
        BOARD[rook1.position.x][rook1.position.y] = rook1;
        player.pieces.add(rook1);

        Piece knight1 = new Knight(new Coordinate(x_begin + 1, y_others, rotations), owner);
        BOARD[knight1.position.x][knight1.position.y] = knight1;
        player.pieces.add(knight1);

        Piece bishop1 = new Bishop(new Coordinate(x_begin + 2, y_others, rotations), owner);
        BOARD[bishop1.position.x][bishop1.position.y] = bishop1;
        player.pieces.add(bishop1);

        Piece queen = new Queen(new Coordinate(x_begin + 3, y_others, rotations), owner);
        BOARD[queen.position.x][queen.position.y] = queen;
        player.pieces.add(queen);

        King king = new King(new Coordinate(x_begin + 4, y_others, rotations), owner);
        BOARD[king.position.x][king.position.y] = king;
        player.pieces.add(king);
        player.king = king;

        Piece bishop2 = new Bishop(new Coordinate(x_begin + 5, y_others, rotations), owner);
        BOARD[bishop2.position.x][bishop2.position.y] = bishop2;
        player.pieces.add(bishop2);

        Piece knight2 = new Knight(new Coordinate(x_begin + 6, y_others, rotations), owner);
        BOARD[knight2.position.x][knight2.position.y] = knight2;
        player.pieces.add(knight2);

        Piece rook2 = new Rook(new Coordinate(x_begin + 7, y_others, rotations), owner);
        BOARD[rook2.position.x][rook2.position.y] = rook2;
        player.pieces.add(rook2);
    }

    /**
     * Sets the player at the left or the right up
     *
     * @param x_pawns  x-coordinate of the columns of pawns
     * @param x_others x-coordinate of the columns of other pieces
     * @param owner    player.id who owns these pieces
     */
    private static void setupPlayerLeftRight(int x_pawns, int x_others, final String owner) {
        Player player = GameEngine.getPlayer(owner);
        Piece modelPawn = new Pawn(new Coordinate(x_pawns, 2, rotations), owner);
        for (int y = 2; y < 10; y++) {
            Piece pawn;
            if (modelPawn.position.x == 10) {
                pawn = new LeftPawn(new Coordinate(x_pawns, y, rotations), owner);
            } else if (modelPawn.position.x == 1) {
                pawn = new RightPawn(new Coordinate(x_pawns, y, rotations), owner);
            } else if (modelPawn.position.y == 1) {
                pawn = new Pawn(new Coordinate(x_pawns, y, rotations), owner);
            } else {
                pawn = new DownPawn(new Coordinate(x_pawns, y, rotations), owner);
            }
//            if (GameEngine.match.isLocal) {
//                pawn =
//                        x_pawns == 1 ? new RightPawn(new Coordinate(x_pawns, y, rotations), owner) :
//                                new LeftPawn(new Coordinate(x_pawns, y, rotations), owner);
//            } else {
//                pawn = GameEngine.match.mode == GameEngine.MODE_2_PLAYER_4_SIDES ?
//                        new LeftPawn(new Coordinate(x_pawns, y, rotations), owner) :
//                        new Pawn(new Coordinate(x_pawns, y, rotations), owner);
//            }
            player.pieces.add(pawn);
            BOARD[pawn.position.x][pawn.position.y] = pawn;
        }
        Piece rook1 = new Rook(new Coordinate(x_others, 2, rotations), owner);
        BOARD[rook1.position.x][rook1.position.y] = rook1;
        player.pieces.add(rook1);

        Piece knight1 = new Knight(new Coordinate(x_others, 3, rotations), owner);
        BOARD[knight1.position.x][knight1.position.y] = knight1;
        player.pieces.add(knight1);

        Piece bishop1 = new Bishop(new Coordinate(x_others, 4, rotations), owner);
        BOARD[bishop1.position.x][bishop1.position.y] = bishop1;
        player.pieces.add(bishop1);

        King king = new King(new Coordinate(x_others, 5, rotations), owner);
        BOARD[king.position.x][king.position.y] = king;
        player.pieces.add(king);
        player.king = king;

        Piece queen = new Queen(new Coordinate(x_others, 6, rotations), owner);
        BOARD[queen.position.x][queen.position.y] = queen;
        player.pieces.add(queen);

        Piece bishop2 = new Bishop(new Coordinate(x_others, 7, rotations), owner);
        BOARD[bishop2.position.x][bishop2.position.y] = bishop2;
        player.pieces.add(bishop2);

        Piece knight2 = new Knight(new Coordinate(x_others, 8, rotations), owner);
        BOARD[knight2.position.x][knight2.position.y] = knight2;
        player.pieces.add(knight2);

        Piece rook2 = new Rook(new Coordinate(x_others, 9, rotations), owner);
        BOARD[rook2.position.x][rook2.position.y] = rook2;
        player.pieces.add(rook2);
    }

    /**
     * Initialize a new game
     *
     * @param players the players
     */
    public static void newGame(final Player[] players) {
        rotations = getRotation();
        int myId = Integer.parseInt(GameEngine.myPlayerId);
        if (GameEngine.match.mode == GameEngine.MODE_2_PLAYER_2_SIDES) {
            BOARD = new Piece[8][8];
            extendedBoard = false;

            // setup player 1 (bottom)
            setupPlayerTopBottom(0, 1, 0, players[0].id);

            // setup player 2 (top)
            setupPlayerTopBottom(0, 6, 7, players[1].id);
        } else {
            BOARD = new Piece[12][12];
            extendedBoard = true;

            // setup player 1 (bottom)
            setupPlayerTopBottom(2, 1, 0, players[0].id);

            // setup player 2 (right)
            setupPlayerLeftRight(10, 11,
                    GameEngine.match.mode == GameEngine.MODE_2_PLAYER_4_SIDES ? players[0].id : players[1].id);

            // setup player 3 (top)
            setupPlayerTopBottom(2, 10, 11,
                    GameEngine.match.mode == GameEngine.MODE_2_PLAYER_4_SIDES ? players[1].id : players[2].id);

            // setup player 4 (left)
            setupPlayerLeftRight(1, 0,
                    GameEngine.match.mode == GameEngine.MODE_2_PLAYER_4_SIDES ? players[1].id : players[3].id);
        }
    }

    /**
     * Gets the number of rotations the board should do, so this players start position is at the bottom of the board
     *
     * @return number of rotations necessary to have this players start position at the bottom
     */
    public static int getRotation() {
        if (GameEngine.match.isLocal) return 0;
        for (int i = 0; i < 4; i++) {
            if (GameEngine.players[i].id.equals(GameEngine.myPlayerId))
                return GameEngine.players.length > 2 ? i : i * 2;
        }
        return 0;
    }

    public static Piece[][] cloneTheBoard() {
        int len = extendedBoard ? 12 : 8;
        Piece[][] cloned = new Piece[len][len];
        for (int i = 0; i < len; i++) {
            cloned[i] = BOARD[i].clone();
        }
        return cloned;
    }

    private static boolean checkForPromotion(Piece p) {
        if (p instanceof DownPawn) {
            int max = extendedBoard ? 11 : 7;
            return p.position.y == max - 7;
        } else if (p instanceof Pawn) {
            return p.position.y == 7;
        } else if (p instanceof LeftPawn) {
            return p.position.x == 4;
        } else if (p instanceof RightPawn) {
            return p.position.x == 7;
        } else {
            return false;

        }
    }
}
