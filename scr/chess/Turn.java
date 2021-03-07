package chess;


import pieces.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * @author Stefan Hasler
 * @version 3.2
 * A Eventhandler to move pieces
 */

public class Turn implements EventHandler<MouseEvent>{

    /**The Move that the user wishes to perform*/
    private static Move move = null;

    /**The Color which has to move*/
    private static Color colorToMove = Color.WHITE;

    @Override
    public void handle(MouseEvent event) {
        //King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();


        FieldLabel evenSource = (FieldLabel) event.getSource();
        if(move == null)
        {
            move = new Move(evenSource, null);

            if(move.getSource().getPiece() != null && move.getSource().getPiece().getColor() == colorToMove) //Checks if theres a Piece on the Field and if its has the right color to move
            {
                move.setMovingPiece(move.getSource().getPiece());

                highlightPiece(move.getSource());
            }
            else{
                move = null;
            }
        }
        else{ //A valid piece has been selected
            move.setTarget((FieldLabel) event.getSource());
            King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();

            //Checks if Target and Source are the same and if the move is legal
            if(isValidMove(move)!=null){
                move=isValidMove(move);
                String s = Move.board.getBoardAsFen();
                if(tryMove(move)){

                    //moving the piece
                    makeMove(move);
                    //undoMove();

                    //Checking for Promotion
                    checkPawnPromotion(move.getMovingPiece());
                    //Checking if castling rights were broken or not
                    checkCastleRights(move.getMovingPiece());

                    //Ending Turn
                    unhighlightPiece(move.getSource());
                    move.getMovingPiece().postTurn(move);
                    Move.board.endTurn();
                    move = null;


                    colorToMove = colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
                }
                else{
                    undoMove(s);
                    unhighlightPiece(move.getSource());
                    move = null;
                }
            }
            else{
                unhighlightPiece(move.getSource());
                move = null;
            }
        }
    }

    private void makeMove(Move m){
        //Make sure Pieces are removed
        //try{move.getEatenPiece().getFieldLabel().removePiece(); Move.board.removePiece(move.getEatenPiece());}catch(NullPointerException ignored){};
        m.getSource().removePiece();

        //Moving the Piece to the Targetfieldlabel
        m.getMovingPiece().setFieldLabel(m.getTarget());
        m.getTarget().setPiece(m.getMovingPiece());
    }

    public static Color getColorToMove() {
        return colorToMove;
    }

    private void undoMove(String fen){
        Move.board.clearAll();
        //Move.board.setBoardByFen("8/8/8/8/8/8/8/8 w - - 0 1");
        Move.board.setBoardByFen(fen);

    }

    public static void highlightPiece(FieldLabel label){
        label.setStyle(label.getStyle() + ';' + (label.getStyle().equals("-fx-background-color: #F0D9B5;") ? "-fx-background-color: #829769;" : "-fx-background-color: #646F40;"));
    }

    public static void unhighlightPiece(FieldLabel label){
        label.setStyle((label.getStyle().contains("-fx-background-color: #829769;") ?  "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;"));
    }

    private Move isValidMove(Move m){
        for (Move e : m.getMovingPiece().calculateValidMoves(Move.board))
        {
            //System.out.println(e.getTarget() + "----" + move.getTarget());
            if(e.getTarget().equals(m.getTarget())){
                return e;
            }
        }
        return null;
    }
/*
        m_undo.getMovingPiece().setFieldLabel(m_undo.getSource());
        m_undo.getSource().setPiece(m_undo.getMovingPiece());
        try{
            m_undo.getTarget().removePiece();
            m_undo.getTarget().setPiece(m_done.getEatenPiece());

        }catch (NullPointerException ignored){}
 */
    private void checkPawnPromotion(Piece p){

        if(p.getName().contains("Pawn") && (p.getFieldLabel().getY() == 0 || p.getFieldLabel().getY() == 7))
            new PromotionDialog((Pawn)p).show();
    }

    private void checkCastleRights(Piece p){

        if(!(p instanceof Rook || p instanceof King)) return;

        FieldLabel[][] labels  = p.getFieldLabel().getBoard().getLabels();

        if(!labels[4][1].hasPiece() && !labels[4][7].hasPiece() && !(labels[4][1].getPiece() instanceof King) && !(labels[4][7].getPiece() instanceof King)){
            return;
        }


        if(p instanceof King){
            ((King) p).setCanCastleKing(false);
            ((King) p).setCanCastleQueen(false);
            return;
        }

        if(p.getColor() == Color.WHITE){
            if(p.getFieldLabel().getX() > 4) ((King)labels[4][7].getPiece()).setCanCastleKing(false);
            if(p.getFieldLabel().getX() < 4) ((King)labels[4][7].getPiece()).setCanCastleQueen(false);
        }
        if(p.getColor() == Color.BLACK){
            if(p.getFieldLabel().getX() > 4) ((King)labels[4][1].getPiece()).setCanCastleKing(false);
            if(p.getFieldLabel().getX() < 4) ((King)labels[4][1].getPiece()).setCanCastleQueen(false);
        }
    }


    protected boolean tryMove(Move m){

        King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();
        String s = Move.board.getBoardAsFen();
        makeMove(m);

        if(k.isInCheck()){
            System.out.println("der move geht nicht");
            System.out.println("\t" + m);

            undoMove(s);
            return false;
        }
        //System.out.println(m + "#" + k.isInCheck() + "#" + k.toString());
        undoMove(s);
        return true;
    }
}

/*

 */