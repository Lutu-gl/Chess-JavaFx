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
 * @version 2.3
 * A Eventhandler to move pieces
 */

public class Turn implements EventHandler<MouseEvent>{
    /**Selected Piece*/
    static Piece currentPiece = null;
    /**Source Field of the Piece*/
    static FieldLabel eventSource = null;
    /**Target Field for Piece*/
    static FieldLabel eventTarget = null;
    /**The Color which has to move*/
    static Color colorToMove = Color.WHITE;

    @Override
    public void handle(MouseEvent event) {
        if(eventSource == null)
        {
            eventSource = (FieldLabel) event.getSource();
            if(eventSource.getPiece() != null && eventSource.getPiece().getColor() == colorToMove) //Checks if theres a Piece on the Field and if its has the right color to move
            {
                highlightPiece(eventSource);
                currentPiece = eventSource.getPiece();
            }
            else
                eventSource = null;
        }
        else{ //A valid piece has been selected
            eventTarget = (FieldLabel) event.getSource();

            //Checks if Target and Source are the same
            if(eventTarget.hashCode() != eventSource.hashCode() && isValidMove(eventSource, eventTarget, currentPiece)){
                currentPiece.setFieldLabel(eventTarget);
                unhighlightPiece(eventSource);
                eventTarget.setPiece(currentPiece);
                checkPawnPromotion(currentPiece);
                eventSource.removePiece();
                eventSource.getBoard().endTurn();
                eventSource = null;
                eventTarget = null;
                currentPiece = null;
                colorToMove = colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
            }
            else{
                eventTarget = null;
                eventSource = null;
            }
        }
    }

    public void highlightPiece(FieldLabel label){
        label.setStyle(label.getStyle() + ';' + (label.getStyle().equals("-fx-background-color: #F0D9B5;") ? "-fx-background-color: #829769;" : "-fx-background-color: #646F40;"));
    }
    public void unhighlightPiece(FieldLabel label){
        label.setStyle((label.getStyle().contains("-fx-background-color: #829769;") ?  "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;"));
    }
    public boolean isValidMove(FieldLabel source, FieldLabel target, Piece currentPiece){
        return currentPiece.calculateValidMoves(source.getBoard()).contains(target);
    }
    public void checkPawnPromotion(Piece p){
        if(p.getName().contains("Pawn") && (p.getFieldLabel().getY() == 0 || p.getFieldLabel().getY() == 7))
            new PromotionDialog((Pawn)p).show();
    }
}
