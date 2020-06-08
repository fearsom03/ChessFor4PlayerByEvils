package kz.evilteamgenius.chessapp.engine.pieces;

import java.util.LinkedList;
import java.util.List;

import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;

public class Pawn extends Piece {
    public Pawn(final Coordinate p, final String o) {
        super(p, o);
    }

    @Override
    public List<Coordinate> getPossiblePositions(Piece[][] board) {
        List<Coordinate> re = new LinkedList<>();
        Coordinate c;
        int x = position.x;
        int y = position.y;
        c = new Coordinate(x, y + 1);
        if (c.isValid() && Board.getPiece(c, board) == null) {
            re.add(c);
        }
        // can move two squares at the beginning
        // (only if no other piece stands 1 before us)
        if (y == 1 && Board.getPiece(c, board) == null) {
            c = new Coordinate(x, y + 2);
            if (c.isValid() && Board.getPiece(c, board) == null) {
                re.add(c);
            }
        }

        // check if we can attack another piece
        c = new Coordinate(x + 1, y + 1);
        if (c.isValid() && Board.getPiece(c, board) != null && sameTeam(c, board)) {
            re.add(c);
        }
        c = new Coordinate(x - 1, y + 1);
        if (c.isValid() && Board.getPiece(c, board) != null && sameTeam(c, board)) {
            re.add(c);
        }
        return re;
    }

    @Override
    public String getString() {
        return "\u265F";
    }
}
