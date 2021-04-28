package model;

import model.pieces.Piece;

import java.util.ArrayList;
import java.util.concurrent.*;

public class AI implements Callable<Turn> {

    private static Chessboard chessboard = Chessboard.getInstance();
    private static int iteration = 0;

    @Override
    public Turn call() throws Exception {
        chessboard.debug = true;
        chessboard.withTime = false;
        ArrayList<Turn> moves = generateMoves();
        Turn bestMoveOverall = null;

        int depth = 0;
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Suche auf Tiefe "+ (depth+1));
            double bestEval = -100000;
            Turn bestMove = null;
            for (Turn move : moves) {
                chessboard.handleTurn(move);
                double eval = -search(depth, -10000, 10000);
                if (eval > bestEval) {
                    bestEval = eval;
                    bestMove = move;
                }
                chessboard.undoTurn(move);
                if (eval > 250) {
                    chessboard.debug = false;
                    chessboard.withTime = true;
                    return bestMove;
                }
                if (Thread.currentThread().isInterrupted() && depth > 2) {
                    System.out.println("DIOCAN");
                    chessboard.debug = false;
                    chessboard.withTime = true;
                    System.out.println("1: "+bestMove);
                    System.out.println("Interrupted :"+Thread.currentThread().isInterrupted());
                    return bestMoveOverall;
                }
            }
            bestMoveOverall = bestMove;

            depth++;
        }
        chessboard.debug = false;
        chessboard.withTime = true;
        System.out.println(iteration);
        System.out.println("2: "+bestMoveOverall);
        return bestMoveOverall;
    }

    private static double search(int depth, double alpha, double beta) {
        iteration++;

        if (Thread.currentThread().isInterrupted()) {
            return -1e6;
        }

        if (depth == 0) {
            return evaluate();
        }

        ArrayList<Turn> moves = generateMoves();
        /*if (moves.size() == 0) {
            chessboard.printBoard();
            return 0;
        }*/

        for (Turn move : moves) {
            chessboard.handleTurn(move);
            double eval = -search(depth-1, -beta, -alpha);
            chessboard.undoTurn(move);
            if (eval > beta)
                return beta;
            alpha = Math.max(alpha, eval);
        }
        return alpha;
    }

    private static ArrayList<Turn> generateMoves() {
        ArrayList<Piece> pieces = chessboard.getColorToMove().equals(Color.WHITE) ? chessboard.getWhitePieces() : chessboard.getBlackPieces();

        // Create the turns
        ArrayList<Turn> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            for (Field move : piece.getMoves()) {
                Turn turn = new Turn(piece.getField(), move);
                if (chessboard.isLegal(turn.getTargetField(), turn.getSourceField(), turn.getColorToMove())){
                    if (move.hasPiece() && move.getPiece().getValue() > piece.getValue())
                        turn.setMoveScoreGuess((int) (10 * move.getPiece().getValue() - piece.getValue()));
                    if (turn.isPromotionTurn())
                        turn.setMoveScoreGuess(9);
                    moves.add(turn);
                }
            }
        }

        // Order the moves
        moves.sort((o1, o2) -> o1.getMoveScoreGuess() > o2.getMoveScoreGuess() ? 1 : 0);

        return moves;
    }

    private static double evaluate() {
        ArrayList<Piece> myPieces, enemyPieces;
        if (chessboard.getColorToMove().equals(Color.WHITE)) {
            myPieces = chessboard.getWhitePieces();
            enemyPieces = chessboard.getBlackPieces();
        } else {
            myPieces = chessboard.getBlackPieces();
            enemyPieces = chessboard.getWhitePieces();
        }
        double value = 0;
        for (Piece p : myPieces) {
            value += p.getValue();
            value += PositionTables.getValue(p);
        }
        double enemyValue = 0;
        for (Piece p : enemyPieces) {
            enemyValue += p.getValue();
            enemyValue += PositionTables.getValue(p);
        }
        return value-enemyValue;
    }

}
