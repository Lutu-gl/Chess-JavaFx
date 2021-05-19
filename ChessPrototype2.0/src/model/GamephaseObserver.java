package model;

import model.pieces.King;
import model.pieces.Pawn;
import model.pieces.Piece;
import model.pieces.Queen;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GamephaseObserver extends Observer{
    private final Chessboard chessboard;

    public GamephaseObserver(Chessboard chessboard){
        this.chessboard = chessboard;
    }

    @Override
    public void update() {
        ArrayList<Piece> pieces = (ArrayList<Piece>) Stream.concat(chessboard.getBlackPieces().stream(), chessboard.getWhitePieces().stream()).collect(Collectors.toList());
        int value=0;
        int notPawns=0;

        for (Piece p : pieces) {      //Gamephase ändern
            if(!(p instanceof King)) value += p.getValue();
            if(!(p instanceof Pawn)) notPawns++;
        }

        //IMPORTANT!!!
        if(value >= 70){
            chessboard.setGamephase(Gamephase.OPENING);
        }else if(value > 31){
            chessboard.setGamephase(Gamephase.MIDGAME);
        }else{
            chessboard.setGamephase(Gamephase.ENDGAME);
        }

    }
}
