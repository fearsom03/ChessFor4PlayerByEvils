package kz.evilteamgenius.chessapp.engine.pieces;

import java.util.Collection;

import kz.evilteamgenius.chessapp.engine.Alliance;
import kz.evilteamgenius.chessapp.engine.Move;
import kz.evilteamgenius.chessapp.engine.board.Board;

public final class Knight extends Piece {
    Knight(PieceType type, Alliance alliance, int piecePosition, boolean isFirstMove) {
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
    public Collection<Move> calculateLegalMoves(Board board) {
        return null;
    }
}
