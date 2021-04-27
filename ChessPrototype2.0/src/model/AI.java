package model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import model.pieces.Piece;

import java.util.ArrayList;
import java.util.concurrent.*;

public class AI implements Callable<Turn> {

    @Override
    public Turn call() throws Exception {
        Turn bestTurn = null;

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

        //int r = (int) (Math.round(Math.random()));
        //if (r == 1 || r == 0) {
        //    bestTurn = moves.get((int) (Math.random() * moves.size()));
        //    return bestTurn;
        //}

        try {
            while (!Thread.currentThread().isInterrupted())
                Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Ich wurde abgebrochen!");
            bestTurn = moves.get((int) (Math.random()*moves.size()));
            return bestTurn;
        }
        return null;

    }


}
