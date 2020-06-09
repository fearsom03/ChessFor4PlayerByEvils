package kz.evilteamgenius.chessapp.engine.pieces;

import java.util.List;

import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;
import kz.evilteamgenius.chessapp.engine.GameEngine;

public abstract class Piece {

    /**
     * The current position
     */
    public Coordinate position;

    /**
     * The player ID who owns this piece
     */
    private final String ownerId;

    /**
     * Construct a new piece
     *
     * @param p the coordinate where this piece is located
     * @param o the ID of the player who owns this piece
     */
    Piece(final Coordinate p, final String o) {
        position = p;
        ownerId = o;
    }

    /**
     * Gets possible new positions for the current piece
     *
     * @return a list of possible new coordinates
     */
    public abstract List<Coordinate> getPossiblePositions(Piece[][] board);

    /**
     * Gets the player ID, to whom this piece belongs
     *
     * @return the owner id of this piece
     */
    public String getPlayerId() {
        return ownerId;
    }

    /**
     * Checks if this piece belongs to the same team as the destination piece
     *
     * @param destination the piece to check against
     * @return true, if both pieces belong to the same team or same player
     */
    boolean sameTeam(final Coordinate destination, Piece[][] board) {
        Piece p = Board.getPiece(destination, board);
        return p == null || !GameEngine.sameTeam(p.ownerId, ownerId);
    }

    /**
     * @return the unicode representation of this piece
     */
    public abstract String getString();

    @Override
    public String toString() {
        Coordinate c = new Coordinate(position.x, position.y, (4 - Board.getRotation()) % 4);
        return c.toString() + "," + ownerId + "," + getClass().getSimpleName();
    }

    public abstract String getType();
}