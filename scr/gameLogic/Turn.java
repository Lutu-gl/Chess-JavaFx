package gameLogic;


import chess.FieldLabel;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pieces.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Stefan Hasler
 * @version 3.2
 * A EventHandler to move pieces
 */

public class Turn implements EventHandler<MouseEvent> {

    /**
     * The Move that the user wishes to perform
     */
    private static Move move = null;

    /**
     * The Color which has to move
     */
    private static Color colorToMove = Color.WHITE;

    @Override
    public void handle(MouseEvent event) {

        FieldLabel evenSource = (FieldLabel) event.getSource();
        if (move == null) {
            move = new Move(evenSource, null);

            if (move.getSource().getPiece() != null && move.getSource().getPiece().getColor() == colorToMove) //Checks if theres a Piece on the Field and if its has the right color to move
            {
                move.setMovingPiece(move.getSource().getPiece());
                /*
                System.out.println("my moves: ");
                for (Move e :move.getMovingPiece().calculateValidMoves(Move.board))
                {
                    System.out.println(e);
                }
                 */
                highlightPiece(move.getSource());
            } else {
                move = null;
            }
        } else { //A valid piece has been selected

            move.setTarget((FieldLabel) event.getSource());
            King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();

            addEnpassantMoves(Move.board.getBoardAsFen());

            if (isValidMove(move) != null) {
                move = isValidMove(move);
                String s = Move.board.getBoardAsFen();
                if (tryMove(move)) {
                    //moving the piece
                    makeMove(move);
                    //undoMove();

                    //Checking for Promotion
                    checkPawnPromotion(move.getMovingPiece());
                    //Checking if castling rights were broken or not
                    checkCastleRights(move.getMovingPiece());
                    //Checking for Castle move
                    checkCastle(move);
                    //Check enpassant
                    checkEnPassant(move);

                    colorToMove = colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
                    long tStart = System.currentTimeMillis();

                    Move.board.setGamestate(updateGamestate());

                    long tStop = System.currentTimeMillis();
                    System.out.println("isGameOver()=" + (tStop - tStart));


                    //Ending Turn
                    unhighlightPiece(move.getSource());
                    move.getMovingPiece().postTurn(move);
                    Move.board.endTurn(move);
                    move = null;
                }else {
                    undoMove(s, move);
                    unhighlightPiece(move.getSource());
                    move = null;
                }
            } else {
                unhighlightPiece(move.getSource());
                move = null;
            }
        }
    }

    private void makeMove(Move m) {
        //Make sure Pieces are removed
        //try{move.getEatenPiece().getFieldLabel().removePiece(); Move.board.removePiece(move.getEatenPiece());}catch(NullPointerException ignored){};

        m.getSource().removePiece();
        //System.out.println("Hier jetzt!" + m.getEatenPiece());
        Move.board.removePiece(m.getEatenPiece());


        //Moving the Piece to the Targetfieldlabel
        m.getMovingPiece().setFieldLabel(m.getTarget());
        m.getTarget().setPiece(m.getMovingPiece());

    }

    public static Color getColorToMove() {
        return colorToMove;
    }

    public static void setColorToMove(Color colorToMove) {
        Turn.colorToMove = colorToMove;
    }

    private void undoMove(String fen, Move m) {

        Move.board.clearAll();
        //Move.board.setBoardByFen("8/8/8/8/8/8/8/8 w - - 0 1");
        Move.board.setBoardByFen(fen);

        //Can potentially be deleted, if we change getter of moving/eaten piece that it gets the piece from the label
        m.setMovingPiece(Move.board.getLabelByCoordinates(m.getSource().getX(), m.getSource().getY()).getPiece());
        m.setEatenPiece(Move.board.getLabelByCoordinates(m.getTarget().getX(), m.getTarget().getY()).getPiece());
        //m.setEatenPiece(m.getEatenPiece());
    }

    public static void highlightPiece(FieldLabel label) {
        label.setStyle(label.getStyle() + ';' + (label.getStyle().equals("-fx-background-color: #F0D9B5;") ? "-fx-background-color: #829769;" : "-fx-background-color: #646F40;"));
    }

    public static void unhighlightPiece(FieldLabel label) {
        label.setStyle((label.getStyle().contains("-fx-background-color: #829769;") ? "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;"));
    }

    private Move isValidMove(Move m) {
        for (Move e : m.getMovingPiece().calculateValidMoves(Move.board)) {
            //System.out.println(e.getTarget() + "----" + move.getTarget());
            if (e.getTarget().equals(m.getTarget())) {
                return e;
            }
        }
        return null;
    }

    private void checkPawnPromotion(Piece p) {

        if (p.getName().contains("Pawn") && (p.getFieldLabel().getY() == 0 || p.getFieldLabel().getY() == 7))
            new PromotionDialog((Pawn) p).show();
    }

    private void checkEnPassant(Move m){
        if(m.getMovingPiece() instanceof Pawn && m.getEatenPiece() == null && m.getSource().getX() != m.getTarget().getX()){
            Move.board.getLabelByCoordinates(m.getTarget().getX(), m.getTarget().getY() + ((m.getMovingPiece().getColor() == Color.WHITE) ? +1 : -1)).removePiece();
        }
    }

    private void checkCastle(Move m) {
        if (m.getMovingPiece() instanceof King) {

            FieldLabel[][] labels = m.getTarget().getBoard().getLabels();

            if (m.getSource().getX() + 1 < m.getTarget().getX()) { //Kingside
                if (m.getMovingPiece().getColor() == Color.WHITE) { //White
                    labels[7][7].removePiece();
                    labels[5][7].setPiece(new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), labels[5][7], Color.WHITE, "White Rook"));
                } else if (m.getMovingPiece().getColor() == Color.BLACK) {//Black
                    labels[7][0].removePiece();
                    labels[5][0].setPiece(new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), labels[5][0], Color.BLACK, "Black Rook"));
                }
            }

            if (m.getSource().getX() - 1 > m.getTarget().getX()) { //Queenside
                if (m.getMovingPiece().getColor() == Color.WHITE) { //White
                    labels[0][7].removePiece();
                    labels[3][7].setPiece(new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), labels[3][7], Color.WHITE, "White Rook"));
                } else if (m.getMovingPiece().getColor() == Color.BLACK) {//Black
                    labels[0][0].removePiece();
                    labels[3][0].setPiece(new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), labels[3][0], Color.BLACK, "Black Rook"));
                }
            }


        }
    }

    private void checkCastleRights(Piece p) {

        if (!(p instanceof Rook || p instanceof King)) return;

        FieldLabel[][] labels = p.getFieldLabel().getBoard().getLabels();

        if (p instanceof King) {
            ((King) p).setCanCastleKing(false);
            ((King) p).setCanCastleQueen(false);
            return;
        }

        if (!labels[4][1].hasPiece() && !labels[4][7].hasPiece() && !(labels[4][1].getPiece() instanceof King) && !(labels[4][7].getPiece() instanceof King)) {
            return;
        }

        if (p.getColor() == Color.WHITE) {
            if (p.getFieldLabel().getX() > 4) Move.board.getW_King().setCanCastleKing(false);
            if (p.getFieldLabel().getX() < 4) Move.board.getW_King().setCanCastleQueen(false);
        }
        if (p.getColor() == Color.BLACK) {
            if (p.getFieldLabel().getX() > 4) Move.board.getB_King().setCanCastleKing(false);
            if (p.getFieldLabel().getX() < 4) Move.board.getB_King().setCanCastleQueen(false);
        }
    }

    private boolean tryCastle(Move m) {
        if (m.getMovingPiece() instanceof King) {

            String s = Move.board.getBoardAsFen();
            FieldLabel[][] labels = m.getTarget().getBoard().getLabels();
            King k = (King) m.getMovingPiece();
            if (m.getSource().getX() + 1 < m.getTarget().getX()) { //Kingside
                if (m.getMovingPiece().getColor() == Color.WHITE) { //White
                    if (k.isInCheck()) return false;

                    makeMove(new Move(labels[4][7], labels[5][7]));

                    if (k.isInCheck()) {

                        undoMove(s, m);
                        return false;
                    }
                    makeMove(new Move(labels[5][7], labels[6][7]));
                    if (k.isInCheck()) {
                        undoMove(s, m);
                        return false;
                    }
                } else if (m.getMovingPiece().getColor() == Color.BLACK) {//Black
                    if (((King) m.getMovingPiece()).isInCheck()) return false;
                    makeMove(new Move(labels[4][0], labels[5][0]));
                    if (((King) m.getMovingPiece()).isInCheck()) {
                        undoMove(s, m);
                        return false;
                    }
                    makeMove(new Move(labels[5][0], labels[6][0]));
                    if (((King) m.getMovingPiece()).isInCheck()) {
                        undoMove(s, m);
                        return false;
                    }
                }
            }

            if (m.getSource().getX() - 1 > m.getTarget().getX()) { //Queenside
                if (m.getMovingPiece().getColor() == Color.WHITE) { //White
                    if (((King) m.getMovingPiece()).isInCheck()) return false;
                    makeMove(new Move(labels[4][7], labels[3][7]));
                    if (((King) m.getMovingPiece()).isInCheck()) {
                        undoMove(s, m);
                        return false;
                    }
                    makeMove(new Move(labels[3][7], labels[2][7]));
                    if (((King) m.getMovingPiece()).isInCheck()) {
                        undoMove(s, m);
                        return false;
                    }
                } else if (m.getMovingPiece().getColor() == Color.BLACK) {//Black
                    if (((King) m.getMovingPiece()).isInCheck()) return false;
                    makeMove(new Move(labels[4][0], labels[3][0]));
                    if (((King) m.getMovingPiece()).isInCheck()) {
                        undoMove(s, m);
                        return false;
                    }
                    makeMove(new Move(labels[3][0], labels[2][0]));
                    if (((King) m.getMovingPiece()).isInCheck()) {
                        undoMove(s, m);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean tryMove(Move m) {
        //For castling
        String s = Move.board.getBoardAsFen();

        boolean trycastle = true;

        trycastle = tryCastle(m);

        undoMove(s, m);


        King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();

        //System.out.println("Fen nach castle mvoe: " + Move.board.getBoardAsFen());

        makeMove(m);
        //System.out.println("Fen nach Try mvoe: " + Move.board.getBoardAsFen());

        if (k.isInCheck()) {
            //System.out.println("der move geht nicht");
            //System.out.println("\t" + m);

            undoMove(s, m);
            return false;
        }
        //System.out.println(m + "#" + k.isInCheck() + "#" + k.toString());
        undoMove(s, m);
        return trycastle;
    }

    private void addEnpassantMoves(String fen){
        String fenInfo = fen.substring(fen.indexOf(' ')+1).replace("/","");//.replace(" ","");,,
        char c;
        for (int j = 0; j < fenInfo.length(); j++) {
            c = fenInfo.charAt(j);
            //en Passant
            if((c >= 97) && (c<=105) && fenInfo.charAt(j+1) != ' ') {
                int x = c - 97;
                int y = (Turn.getColorToMove() != Color.WHITE ? 5 : 2);
                int moveDirect = (Turn.getColorToMove() != Color.WHITE ? -1 : +1);

                //System.out.println("En Passant bei: " + c + fenInfo.charAt(j + 1)); //enPassant ist möglich bei dem feld
                FieldLabel label = Move.board.getLabelByCoordinates(x, y);

                //System.out.println("label das enpassant .." + label);

                FieldLabel label2 = Move.board.getLabelByCoordinates(x + 1, y + moveDirect);
                //System.out.println(label2);

                if (label2 != null && label2.hasPiece() && label2.getPiece() instanceof Pawn && label2.getPiece().getColor() == Turn.getColorToMove()) {
                    ((Pawn) label2.getPiece()).setEnpassantLabel(label);
                }
                label2 = Move.board.getLabelByCoordinates(x - 1, y + moveDirect);
                //System.out.println(label2);
                if (label2 != null && label2.hasPiece() && label2.getPiece() instanceof Pawn && label2.getPiece().getColor() == Turn.getColorToMove()) {
                    ((Pawn) label2.getPiece()).setEnpassantLabel(label);
                }
            }
        }
    }

    private Gamestate updateGamestate() {
        /* ATTENZIONE: NON FUNZIONA NEANCHE UN POCCETINO */

        for (int i = 0; i < Move.board.getPiecesByColor(colorToMove).size(); i++)
        {
            Piece p = Move.board.getPiecesByColor(colorToMove).get(i);
            for (Move m : p.calculateValidMoves(Move.board))
            {
                if(tryMove(m)){
                    return Gamestate.PLAYING;
                }
            }
        }


        if(Move.board.getKing(colorToMove).isInCheck()){
            return colorToMove == Color.WHITE ? Gamestate.BLACKWINS : Gamestate.WHITEWINS;
        }
        return Gamestate.STALEMATE;
    }
}
/*
  /*

         */


        /*
        FieldLabel[][] labels = Move.board.getLabels();
        boolean isStalemate = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (labels[j][i].hasPiece() && labels[j][i].getPiece().getColor() == colorToMove) {
                    for (Move f : labels[j][i].getPiece().calculateValidMoves(Move.board)) {
                        if (tryMove(f)) {
                            return Gamestate.PLAYING;
                        }
                    }
                }
            }
        }

         */