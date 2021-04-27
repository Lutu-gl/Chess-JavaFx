package model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import model.pieces.Piece;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.*;

public class AI implements Callable<Turn> {

    private static Chessboard chessboard = Chessboard.getInstance();

    @Override
    public Turn call() throws Exception {
        chessboard.debug = true;
        chessboard.withTime = false;
        ArrayList<Turn> moves = generateMoves();
        Turn bestMove = null;
        double bestEval = Double.MIN_VALUE;
        for (Turn move : moves) {
            chessboard.handleTurn(move);
            double eval = search(1);
            if (eval > bestEval) {
                bestEval = eval;
                bestMove = move;
            }
            chessboard.undoTurn(move);
        }
        System.out.println("BEST MOVE:" + bestMove);
        //search(2);
        chessboard.debug = false;
        chessboard.withTime = true;
        return bestMove;

        /*Turn bestTurn = null;

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
        return null; */
    }

    private static double search(int depth) {
        if (depth == 0) {
            return evaluate();
        }

        ArrayList<Turn> moves = generateMoves();
        double bestEvaluation = Double.MIN_VALUE;
        for (Turn move : moves) {
            chessboard.handleTurn(move);
            double eval = -search(depth-1);
            bestEvaluation = Math.max(eval, bestEvaluation);
            chessboard.undoTurn(move);
        }
        return bestEvaluation;
    }

    private static ArrayList<Turn> generateMoves() {
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
        return moves;
    }

    private static double evaluate() {
        ArrayList<Piece> myPieces, enemyPieces;
        if (chessboard.getColorToMove().equals(Color.WHITE)) {
            myPieces = chessboard.getWhitePieces();
            enemyPieces = chessboard.getBlackPieces();
        } else {
            myPieces = chessboard.getBlackPieces();
            enemyPieces = chessboard.getBlackPieces();
        }
        double value = 0;
        for (Piece p : myPieces) {
            value += p.getValue() + PositionTables.getValue(p);
        }
        for (Piece p : enemyPieces) {
            value -= p.getValue() - PositionTables.getValue(p);
        }
        return value;
    }

}
