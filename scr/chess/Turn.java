package chess;

import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pieces.Pawn;
import pieces.Piece;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import pieces.Queen;

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

    @Override
    public void handle(MouseEvent event) {
        if(move == null)
        {
            move = new Move((FieldLabel)event.getSource(), null);

            if(move.getSource().getPiece() != null && move.getSource().getPiece().getColor() == colorToMove) //Checks if theres a Piece on the Field and if its has the right color to move
            {
                move.setMovingPiece(move.getSource().getPiece());
                highlightPiece(move.getSource());
                move.setMovingPiece(move.getSource().getPiece());
                System.out.println("JAAAA!");
            }
            else{
                System.out.println("NEIN!");
                move = null;
            }
        }
        else{ //A valid piece has been selected
            System.out.println("jA?");
            move.setTarget((FieldLabel) event.getSource());
            System.out.println(move.getTarget());

            //Checks if Target and Source are the same and if the move is legal
            if(move.getTarget().hashCode() != move.getSource().hashCode() && isValidMove(move)){
                move.getMovingPiece().setFieldLabel(move.getTarget());
                unhighlightPiece(move.getSource());
                move.getTarget().setPiece(move.getMovingPiece());
                checkPawnPromotion(move.getMovingPiece());
                System.out.println("jetzt is der zug fertig");
                move.getMovingPiece().postTurn();
                move.getSource().removePiece();
                Move.board.endTurn();
                move = null;
                colorToMove = colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
            }
            else{
                unhighlightPiece(move.getSource());
                move = null;
            }
        }
    }

    private void highlightPiece(FieldLabel label){
        label.setStyle(label.getStyle() + ';' + (label.getStyle().equals("-fx-background-color: #F0D9B5;") ? "-fx-background-color: #829769;" : "-fx-background-color: #646F40;"));
    }
    private void unhighlightPiece(FieldLabel label){
        label.setStyle((label.getStyle().contains("-fx-background-color: #829769;") ?  "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;"));
    }
    private boolean isValidMove(Move move){
        //System.out.println("wanna move here " + move.toString());
        for (Move e : move.getMovingPiece().calculateValidMoves(Move.board))
        {
            System.out.println(e.getTarget().equals(move.getTarget()));
            if(e.getTarget().equals(move.getTarget()))
                return true;
        }
        return false;
    }

    private void checkPawnPromotion(Piece p){
        System.out.println(p.toString());
        if(p.getName().contains("Pawn") && (p.getFieldLabel().getY() == 0 || p.getFieldLabel().getY() == 7))
            new PromotionDialog((Pawn)p).show();
    }
    public void eatPiece(){

    }
}

/*

 */