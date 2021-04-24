package model;

import javafx.application.Platform;
import model.pieces.Piece;

import java.util.ArrayList;

public class AI implements Runnable {

    public void run() {

        Turn bestTurn = null;

        //try {
            Chessboard chessboard = Chessboard.getInstance();
            ArrayList<Piece> pieces = chessboard.getColorToMove().equals(Color.WHITE) ? chessboard.getWhitePieces() : chessboard.getBlackPieces();

            // Create the turns
            ArrayList<Turn> moves = new ArrayList<>();
            for (Piece piece : pieces) {
                for (Field move : piece.getMoves()) {
                    Turn turn = new Turn(piece.getField(), move);
                    if (chessboard.isLegal(turn.getTargetField(), turn.getSourceField(), turn.getColorToMove()))
                        moves.add(turn);
                }
            }
            //for (int i = 0; i < 2100000000;  i++)

            //    i = i + ((i%2==0) ? 1: -1);
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bestTurn = moves.get((int) (Math.random()*moves.size()));

        //} catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
        //}

        System.out.println(bestTurn);

        Chessboard.getInstance().handleTurn(bestTurn);
    }

}
