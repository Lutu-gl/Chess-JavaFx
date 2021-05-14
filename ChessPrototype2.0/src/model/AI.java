package model;

import model.pieces.King;
import model.pieces.Piece;
import view.FieldLabel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class AI implements Callable<Turn> {

    private static Chessboard chessboard = Chessboard.getInstance();
    private static int iteration = 0;


    @Override
    public Turn call() throws Exception {
        chessboard.debug = true;
        chessboard.withTime = false;
        //System.out.println(chessboard.getColorToMove());
        chessboard.setCurrentAIMovingColor(Chessboard.getInstance().getColorToMove());
        ArrayList<Turn> moves = generateMoves();
        Turn bestMoveOverall = null, bestMove = null;

        System.out.println("Ai denkt die Position hat die evalutaion: " + evaluate());
        HashMap<String, String> openingBook = chessboard.getOpeningBook();
        String fen = chessboard.getBoardAsFen();
        fen = fen.substring(0, fen.indexOf(" ")+2);
        if (openingBook.containsKey(fen)) {
            System.out.println(openingBook.get(fen));
            chessboard.debug = false;
            chessboard.withTime = true;
            return convertNotation(openingBook.get(fen));
        }

        int depth = 0;
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Suche auf Tiefe "+ (depth+1));
            double bestEval = -10000;       //Eval auf ganz was niederes setzen

            iteration = 0;
            for (Turn move : moves) { //Jeden move probieren
                chessboard.handleTurn(move);
                double eval = -search(depth, -10000, 10000);    //In die Tiefe schauen und eval holen mit der depth
                if (eval > bestEval) {      //eval updaten
                    bestEval = eval;
                    bestMove = move;
                }
                chessboard.undoTurn(move);  //Zug rückgängig machen
                if (eval > 250) {               //Wenn er winning Sequenz findet
                    chessboard.debug = false;      //Debug=false dann macht es nichts grafisches
                    chessboard.withTime = true;     //mit zeit spielen. Also alles von Time handeln
                    return bestMove;                //bestmove zurückgeben
                }
                if (Thread.currentThread().isInterrupted() && depth > 2) { //Wenn AI keine Zeit mehr hat, dann soll sie doch bis tiefe 2 schauen
                    chessboard.debug = false;
                    chessboard.withTime = true;
                    return bestMoveOverall;
                }
            }
            bestMove.setMoveScoreGuess(1000);       //Die Wahrscheinlichkeit erhöhen das dieser Move getestet wird. Zum Beispiel bei Capture moves wird das erhöht
            bestMoveOverall = bestMove;             //BestMoveOverall updaten. BestMoveOverall ist der Move den es nach einer depth komplett durch rausgefunden hat. Wenn es während einer depth durchrechnen, abbricht dann nimmt es den bestMove von der letzen depth!
            depth++;                                //depth erhöhen.
        }

        chessboard.debug = false;
        chessboard.withTime = true;
        System.out.println(iteration);
        System.out.println("2: "+bestMoveOverall);
        return bestMoveOverall;
    }

    private static double search(int depth, double alpha, double beta) {
        if (Thread.currentThread().isInterrupted()) {   //Wenn keine Zeit mehr ist
            return -1e6;
        }

        if (depth == 0) {   //Wenn die depth komplett durchgerechnet wurde, dann evaluiert man die Position
            return evaluate();
            //return searchForCaptures(alpha, beta);
        }

        ArrayList<Turn> moves = generateMoves();    //Die moves für jedes piece holen
        if (moves.size() == 0) {    //Wenn es keine Moves gibt
            King k = chessboard.getColorToMove().equals(Color.WHITE)? chessboard.getW_king() : chessboard.getB_king();
            if (k.isInCheck()){
                //Wenn er Schach ist, dann ist Schachmatt.
                return -500;
            }

            //chessboard.printBoard();
            return 0;       //Sonst ist Stalemate
        }

        for (Turn move : moves) {   //Wieder jeden Move durchgehen
            chessboard.handleTurn(move);    //Den move spielen
            double eval = -search(depth-1, -beta, -alpha);  //Nochmal die Funktion aufrufen mit einer depth weniger
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

    private static ArrayList<Turn> generateMoves() {    //Da berechnen welche Moves zuerst angeschaut werden
        ArrayList<Piece> pieces = chessboard.getColorToMove().equals(Color.WHITE) ? chessboard.getWhitePieces() : chessboard.getBlackPieces();

        // Create the turns
        ArrayList<Turn> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            for (Field move : piece.getMoves()) {
                Turn turn = new Turn(piece.getField(), move);
                if (chessboard.isLegal(turn.getTargetField(), turn.getSourceField(), turn.getColorToMove())){
                    if (move.hasPiece()) //Wenn es ein Capture isch
                        turn.setMoveScoreGuess((int) (10 * move.getPiece().getValue() - piece.getValue()));
                    else if (turn.isPromotionTurn()) //Wenn es Promotion isch
                        turn.setMoveScoreGuess(9);
                    else if (isAttackedByPawn(turn)) //Wenn es von Pawn attacked ist
                        turn.setMoveScoreGuess(-10);
                    else if (!move.hasPiece())  //Wenn es kein Piece captured
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
        moves.sort((o1, o2) -> Integer.compare(o2.getMoveScoreGuess(), o1.getMoveScoreGuess())); //Die Züge sortieren

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

    private static boolean isAttackedByPawn(Turn t) { //Wenne Piece von Pawn attacked ist
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


    private static double evaluate() {              //Die Endposition evaluieren

        //System.out.println(Chessboard.getInstance().getBoardAsFen());
        /*
        King k = chessboard.getColorToMove().equals(Color.WHITE)? chessboard.getW_king() : chessboard.getB_king();
        if(k.getMoves().size() == 0){
            if (k.isInCheck()){
                //Wenn er Schach ist, dann ist Schachmatt.
                //System.out.println(Chessboard.getInstance().getBoardAsFen());
                return -500;
            }else{
                return 0;       //Sonst ist Stalemate
            }
        }

         */
        //chessboard.printBoard();
        ArrayList<Piece> myPieces, enemyPieces;

        double mySpace = 0;
        double enemySpace = 0;



        if (chessboard.getColorToMove().equals(Color.WHITE)) {
            myPieces = chessboard.getWhitePieces();
            enemyPieces = chessboard.getBlackPieces();
        } else {
            myPieces = chessboard.getBlackPieces();
            enemyPieces = chessboard.getWhitePieces();
        }
        double value = 0;
        for (Piece p : myPieces) {      //Jedes Piece durchgehen und Value adden
            value += p.getValue();
            value += PositionTables.getValue(p);

            mySpace += evaluateSpace(p);
        }
        double enemyValue = 0;           //EnemyValue berechnen
        for (Piece p : enemyPieces) {
            enemyValue += p.getValue();
            enemyValue += PositionTables.getValue(p);

            enemySpace += evaluateSpace(p);
        }
        double space = (mySpace - enemySpace)*0.2;

        return value-enemyValue + space;    //final value zurückgeben
    }

    private static double evaluateSpace(Piece p){
        int column = p.getField().getColumn();
        int line = p.getField().getLine();

        double space = column;

        return space;
    }

    private static double evaluateKingSavety(){
        double myKingEval;
        double enemyKingEval;



        return 0;
    }

    private static ArrayList<Piece> getPiecesNearKing(double length, King k){
        ArrayList<Piece> pieces = new ArrayList<>();
        Field kingField = k.getField();
        int kingColumn = kingField.getColumn();
        int kingLine = kingField.getLine();

        Field[][] fields = chessboard.getFields();

        for (int i = 0; i < length*2; i++) {
            for (int j = 0; j < length*2; j++) {
                Field field;
                if((i-length < 0) || (i-length) > chessboard.getSizeOfBoard() || j-length < 0 || j-length > chessboard.getSizeOfBoard()) continue;
                field = fields[(int) (i-length)][(int) (j-length)];

                if(field.hasPiece()){
                    pieces.add(field.getPiece());
                }
            }
        }


        return pieces;
    }

    private Turn convertNotation(String s) {
        int column1 = s.charAt(0)-97, line1 = Math.abs(Integer.parseInt(String.valueOf(s.charAt(1)))-8);
        int column2 = s.charAt(2)-97, line2 = Math.abs(Integer.parseInt(String.valueOf(s.charAt(3)))-8);

        //System.out.println(line1 + " " + column2);
        return new Turn(chessboard.getFields()[line1][column1], chessboard.getFields()[line2][column2]);
    }
}
