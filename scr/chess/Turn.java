package chess;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.MoveTo;
import pieces.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Stefan Hasler
 * @version 3.2
 * A Eventhandler to move pieces
 */

public class Turn implements EventHandler<MouseEvent>{

    /**The Move that the user wishes to perform*/
    static Move move = null;
    /**The Color which has to move*/
    static Color colorToMove = Color.WHITE;

    /**Save of move incase of rollback*/
    private Move move_save = null;

    @Override
    public void handle(MouseEvent event) {

        FieldLabel evenSource = (FieldLabel) event.getSource();
        if(move == null)
        {
            move = new Move(evenSource, null);

            if(move.getSource().getPiece() != null && move.getSource().getPiece().getColor() == colorToMove) //Checks if theres a Piece on the Field and if its has the right color to move
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
            }
            else{
                move = null;
            }
        }
        else{ //A valid piece has been selected
            move.setTarget((FieldLabel) event.getSource());
            King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();

            //Checks if Target and Source are the same and if the move is legal
            if(isValidMove()!=null){
                move=isValidMove();
                move_save = new Move(move);
                if(testMove(move)){

                    //System.out.println("legaler move");
                    //moving the piece
                    makeMove(move);
                    //System.out.println(move.getMovingPiece().getFieldLabel());;
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
                    /*
                    for (int i = 0; i < 8; i++)
                    {
                        for (int j = 0; j < 8; j++)
                        {
                            System.out.println(Move.board.getLabels()[j][i]);

                        }
                        System.out.println("--------------------------------");
                    }
                    System.out.println("#########################################################");

                     */
                    colorToMove = colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
                }
                else{
                    //System.out.println("illegaler move");
                    //undoMove();
                    unhighlightPiece(move.getSource());
                    move = null;
                }
            }
            else{
                //System.out.println("den move gibs nicht");
                unhighlightPiece(move.getSource());
                move = null;
            }
        }
    }

    private void makeMove(Move m){
        //Make sure Pieces are removed
        //System.out.println(move);
        //try{System.out.println(move.getEatenPiece().getFieldLabel());}catch(NullPointerException ignored){}
        try{move.getEatenPiece().getFieldLabel().removePiece();}catch(NullPointerException ignored){};
        move.getSource().removePiece();

        //Moving the Piece to the Targetfieldlabel
        move.getMovingPiece().setFieldLabel(move.getTarget());
        move.getTarget().setPiece(move.getMovingPiece());
    }

    private void undoMove(){
        Chessboard b = Move.board;
        move_save.getMovingPiece().setFieldLabel(move_save.getSource());
        move_save.getSource().setPiece(move_save.getMovingPiece());
        try{
            move_save.getTarget().removePiece();
            move_save.getTarget().setPiece(move.getEatenPiece());

        }catch (NullPointerException ignored){}
    }

    public static void highlightPiece(FieldLabel label){
        label.setStyle(label.getStyle() + ';' + (label.getStyle().equals("-fx-background-color: #F0D9B5;") ? "-fx-background-color: #829769;" : "-fx-background-color: #646F40;"));
    }

    public static void unhighlightPiece(FieldLabel label){
        label.setStyle((label.getStyle().contains("-fx-background-color: #829769;") ?  "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;"));
    }

    private Move isValidMove(){
        for (Move e : move.getMovingPiece().calculateValidMoves(Move.board))
        {
            //System.out.println(e.getTarget() + "----" + move.getTarget());
            if(e.getTarget().equals(move.getTarget())){
                return e;
            }
        }
        return null;
    }

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

    private boolean testMove(Move m){

        King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();
        //String fen = Move.board.getBoardAsFen();
        makeMove(m);

        if(k.isInCheck()){
            undoMove();
            return false;
        }
        undoMove();
        return true;
    }
}

/*

 */