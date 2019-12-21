package kz.evilteamgenius.chessapp.engine;

import java.util.ArrayList;

public class Rook extends Piece{

    public Rook(boolean white) {
        super(white);
    }

    @Override
    public ArrayList<Coordinates> AllowedMoves(Coordinates coordinates , Position[][] board){

        ArrayList<Coordinates> allowedMoves = new ArrayList<>();
        allowedMoves.clear();
        Coordinates c ;

        for(int i=(coordinates.getX()+1) ; i<8 ;i++){
            if(board[i][coordinates.getY()].getPiece() == null){
                c = new Coordinates(i , coordinates.getY());
                allowedMoves.add(c);
            }else{
                if(board[i][coordinates.getY()].getPiece().isWhite() != board[coordinates.getX()][coordinates.getY()].getPiece().isWhite()){
                    c = new Coordinates(i , coordinates.getY());
                    allowedMoves.add(c);
                }
                break;
            }
        }

        for(int i=(coordinates.getX()-1) ; i>=0 ; i--){
            if(board[i][coordinates.getY()].getPiece() == null){
                c = new Coordinates(i , coordinates.getY());
                allowedMoves.add(c);
            }else{
                if(board[i][coordinates.getY()].getPiece().isWhite() != board[coordinates.getX()][coordinates.getY()].getPiece().isWhite()){
                    c = new Coordinates(i , coordinates.getY());
                    allowedMoves.add(c);
                }
                break;
            }
        }

        for(int i=(coordinates.getY()-1) ; i>=0 ; i--){
            if(board[coordinates.getX()][i].getPiece() == null){
                c = new Coordinates( coordinates.getX() , i);
                allowedMoves.add(c);
            }else{
                if(board[coordinates.getX()][i].getPiece().isWhite() != board[coordinates.getX()][coordinates.getY()].getPiece().isWhite()){
                    c = new Coordinates( coordinates.getX() , i);
                    allowedMoves.add(c);
                }
                break;
            }
        }

        for(int i=(coordinates.getY()+1) ; i<8 ;i++){
            if(board[coordinates.getX()][i].getPiece() == null){
                c = new Coordinates(coordinates.getX() , i);
                allowedMoves.add(c);
            }else{
                if(board[coordinates.getX()][i].getPiece().isWhite() != board[coordinates.getX()][coordinates.getY()].getPiece().isWhite()){
                    c = new Coordinates(coordinates.getX() , i);
                    allowedMoves.add(c);
                }
                break;
            }
        }
        //check locations at board
        //work on the coordinates and return the allowed moves
        return allowedMoves;
    }
}