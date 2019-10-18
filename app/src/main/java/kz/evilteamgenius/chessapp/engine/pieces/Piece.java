package kz.evilteamgenius.chessapp.engine.pieces;


import java.util.Collection;

import kz.evilteamgenius.chessapp.engine.Alliance;
import kz.evilteamgenius.chessapp.engine.Move;
import kz.evilteamgenius.chessapp.engine.board.Board;

public abstract class Piece {

    final PieceType pieceType;
    final Alliance pieceAlliance;
    final int piecePosition;
    private final boolean isFirstMove;

    Piece(final PieceType type,
          final Alliance alliance,
          final int piecePosition,
          final boolean isFirstMove) {
        this.pieceType = type;
        this.piecePosition = piecePosition;
        this.pieceAlliance = alliance;
        this.isFirstMove = isFirstMove;
    }


    public PieceType getPieceType() {
        return this.pieceType;
    }

    public Alliance getPieceAllegiance() {
        return this.pieceAlliance;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

//    public int getPieceValue() {
//        return this.pieceType.getPieceValue();
//    }

    public abstract int locationBonus();

    public abstract Piece movePiece(Move move);

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public enum PieceType{
        PAWN(){},
        KNIGHT(){},
        KING(){},
        BISHOP(){},
        ROOK(){},
        QUEEN(){};

    }
}
