package kz.evilteamgenius.chessapp.engine.pieces;

import java.util.LinkedList;
import java.util.List;

import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;

public class Rook extends Piece {

    public Rook(final Coordinate p, final String o) {
        super(p, o);
    }

    @Override
    public List<Coordinate> getPossiblePositions(Piece[][] board) {
        return moveStraight(this, board);
    }

    /**
     * Get a list of possible positions, if the piece can only move straight from its current position
     *
     * @param p the piece
     * @return a list of possible positions
     */
    public static List<Coordinate> moveStraight(final Piece p, Piece[][] board) {
        List<Coordinate> re = new LinkedList<>();

        // move to top
        int x = p.position.x;
        int y = p.position.y + 1;
        Coordinate c = new Coordinate(x, y);
        while (c.isValid() && Board.getPiece(c, board) == null) {
            re.add(c);
            y++;
            c = new Coordinate(x, y);
        }
        if (c.isValid() && p.sameTeam(c, board)) {
            re.add(c);
        }

        // move to bottom
        y = p.position.y - 1;
        c = new Coordinate(x, y);
        while (c.isValid() && Board.getPiece(c, board) == null) {
            re.add(c);
            y--;
            c = new Coordinate(x, y);
        }
        if (c.isValid() && p.sameTeam(c, board)) {
            re.add(c);
        }

        // move right
        y = p.position.y;
        x = p.position.x + 1;
        c = new Coordinate(x, y);
        while (c.isValid() && Board.getPiece(c, board) == null) {
            re.add(c);
            x++;
            c = new Coordinate(x, y);
        }
        if (c.isValid() && p.sameTeam(c, board)) {
            re.add(c);
        }

        // move left
        x = p.position.x - 1;
        c = new Coordinate(x, y);
        while (c.isValid() && Board.getPiece(c, board) == null) {
            re.add(c);
            x--;
            c = new Coordinate(x, y);
        }
        if (c.isValid() && p.sameTeam(c, board)) {
            re.add(c);
        }

        return re;
    }

    @Override
    public String getString() {
        return "\u265C";
    }
}
