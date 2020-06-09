package kz.evilteamgenius.chessapp.engine.pieces;

import java.util.LinkedList;
import java.util.List;

import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;

public class Bishop extends Piece {

    public Bishop(final Coordinate p, final String o) {
        super(p, o);
    }

    @Override
    public List<Coordinate> getPossiblePositions(Piece[][] board) {
        return moveDiagonal(this, board);
    }

    /**
     * Get a list of possible positions, if the piece can only move diagonal from its current position
     *
     * @param p the piece
     * @return a list of possible positions
     */
    public static List<Coordinate> moveDiagonal(final Piece p, Piece[][] board) {
        List<Coordinate> re = new LinkedList<>();
        int x = p.position.x + 1;
        int y = p.position.y + 1;
        Coordinate c = new Coordinate(x, y);

        // move to top right
        while (c.isValid() && Board.getPiece(c, board) == null) {
            re.add(c);
            y++;
            x++;
            c = new Coordinate(x, y);
        }
        if (c.isValid() && p.sameTeam(c, board)) {
            re.add(c);
        }

        // move to bottom right
        x = p.position.x + 1;
        y = p.position.y - 1;
        c = new Coordinate(x, y);
        while (c.isValid() && Board.getPiece(c, board) == null) {
            re.add(c);
            y--;
            x++;
            c = new Coordinate(x, y);
        }
        if (c.isValid() && p.sameTeam(c, board)) {
            re.add(c);
        }

        // move top left
        x = p.position.x - 1;
        y = p.position.y + 1;
        c = new Coordinate(x, y);
        while (c.isValid() && Board.getPiece(c, board) == null) {
            re.add(c);
            x--;
            y++;
            c = new Coordinate(x, y);
        }
        if (c.isValid() && p.sameTeam(c, board)) {
            re.add(c);
        }

        // move bottom left
        x = p.position.x - 1;
        y = p.position.y - 1;
        c = new Coordinate(x, y);
        while (c.isValid() && Board.getPiece(c, board) == null) {
            re.add(c);
            x--;
            y--;
            c = new Coordinate(x, y);
        }
        if (c.isValid() && p.sameTeam(c, board)) {
            re.add(c);
        }

        return re;
    }

    @Override
    public String getType() {
        return "bishop";
    }

    @Override
    public String getString() {
        return "\u265D";
    }
}
