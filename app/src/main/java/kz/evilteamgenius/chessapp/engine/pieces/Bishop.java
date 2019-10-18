package kz.evilteamgenius.chessapp.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kz.evilteamgenius.chessapp.engine.Alliance;
import kz.evilteamgenius.chessapp.engine.Move;
import kz.evilteamgenius.chessapp.engine.board.Board;

public final class Bishop extends Piece {

    Bishop(PieceType type, Alliance alliance, int piecePosition, boolean isFirstMove) {
        super(type, alliance, piecePosition, isFirstMove);
    }

    @Override
    public int locationBonus() {
        return 0;
    }

    @Override
    public Piece movePiece(Move move) {
        return null;
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        return null;
    }

}
