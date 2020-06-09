package kz.evilteamgenius.chessapp.engine.pieces;

import java.util.List;

import kz.evilteamgenius.chessapp.engine.Coordinate;

public class Queen extends Piece {

    public Queen(final Coordinate p, final String o) {
        super(p, o);
    }

    @Override
    public List<Coordinate> getPossiblePositions(Piece[][] board) {
        List<Coordinate> re = Rook.moveStraight(this, board);
        re.addAll(Bishop.moveDiagonal(this, board));
        return re;
    }

    public String getType() {
        return "queen";
    }

    @Override
    public String getString() {
        return "\u265A";
    }
}
