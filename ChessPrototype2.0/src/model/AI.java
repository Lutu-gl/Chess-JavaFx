package model;

import model.pieces.King;
import model.pieces.Piece;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.*;

public class AI implements Callable<Turn> {

    private static Chessboard chessboard = Chessboard.getInstance();
    private static int iteration = 0;

    @Override
    public Turn call() throws Exception {
        chessboard.debug = true;
        chessboard.withTime = false;
        ArrayList<Turn> moves = generateMoves();
        Turn bestMoveOverall = null, bestMove = null;

        int depth = 0;
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Suche auf Tiefe "+ (depth+1));
            double bestEval = -10000;
            //Turn bestMove = null;
            iteration = 0;
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
                    chessboard.debug = false;
                    chessboard.withTime = true;
                    System.out.println("1: "+bestMove);
                    System.out.println("Interrupted :"+Thread.currentThread().isInterrupted());
                    return bestMoveOverall;
                }
            }
            bestMove.setMoveScoreGuess(1000);
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

        if (Thread.currentThread().isInterrupted()) {
            return -1e6;
        }

        if (depth == 0) {
            return evaluate();
            //return searchForCaptures(alpha, beta);
        }

        ArrayList<Turn> moves = generateMoves();
        if (moves.size() == 0) {
            King k = chessboard.getColorToMove().equals(Color.WHITE)? chessboard.getW_king() : chessboard.getB_king();
            if (k.isInCheck())
                return -500;
            //chessboard.printBoard();
            return 0;
        }

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

    private static double searchForCaptures(double alpha, double beta) {
        double eval = evaluate();
        if (eval >= beta)
            return beta;
        alpha = Math.max(alpha, eval);

        ArrayList<Turn> captureMoves = generateCaptureMoves();

        for (Turn captureMove : captureMoves) {
            chessboard.handleTurn(captureMove);
            eval = -searchForCaptures(-beta, -alpha);
            chessboard.undoTurn(captureMove);

            if (eval >= beta)
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
                    if (move.hasPiece())
                        turn.setMoveScoreGuess((int) (10 * move.getPiece().getValue() - piece.getValue()));
                    else if (turn.isPromotionTurn())
                        turn.setMoveScoreGuess(9);
                    else if (isAttackedByPawn(turn))
                        turn.setMoveScoreGuess(-10);
                    else if (!move.hasPiece())
                        turn.setMoveScoreGuess(-5);

                    moves.add(turn);
                }
            }
        }

        /*System.out.println("\n\n");
        moves.forEach(move -> System.out.println(move.getMoveScoreGuess() + " " + move.getTargetField()));
        System.out.println("\n\n");
        */

        // Order the moves
        moves.sort(new Comparator<Turn>() {
            @Override
            public int compare(Turn o1, Turn o2) {
                return Integer.compare(o2.getMoveScoreGuess(), o1.getMoveScoreGuess());
            }
        });

        /*System.out.println("\n\n");
        moves.forEach(System.out::println);
        System.out.println("\n\n");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        return moves;
    }

    private static boolean isAttackedByPawn(Turn t) {
        int size = chessboard.getFields().length;
        int line = t.getTargetField().getLine(), column = t.getTargetField().getColumn();
        Field f = null;
        if (t.getColorToMove().equals(Color.WHITE)) {
            if (line-1 >= 0 && column-1 >= 0) {
                f = chessboard.getFields()[line - 1][column - 1];
                if (f != null && f.hasPiece() && f.getPiece().getShortName() == 'p')
                    return true;
            }
            if (line-1 >= 0 && column+1 < size){
                f = chessboard.getFields()[line-1][column+1];
                return f != null && f.hasPiece() && f.getPiece().getShortName() == 'p';
            }
        } else {
            if (line+1 < size && column-1 >= 0) {
                f = chessboard.getFields()[line + 1][column - 1];
                if (f != null && f.hasPiece() && f.getPiece().getShortName() == 'P')
                    return true;
            }
            if (line+1 < size && column+1 < size) {
                f = chessboard.getFields()[line+1][column+1];
                return f != null && f.hasPiece() && f.getPiece().getShortName() == 'P';
            }
        }
        return false;
    }

    private static ArrayList<Turn> generateCaptureMoves() {
        ArrayList<Piece> pieces = chessboard.getColorToMove().equals(Color.WHITE) ? chessboard.getWhitePieces() : chessboard.getBlackPieces();
        ArrayList<Turn> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            for (Field move : piece.getMoves()) {
                Turn turn = new Turn(piece.getField(), move);
                if (move.hasPiece() && chessboard.isLegal(turn.getTargetField(), turn.getSourceField(), turn.getColorToMove())){
                    moves.add(turn);
                }
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
