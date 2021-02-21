package chess;

import pieces.Piece;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * @author Stefan Hasler
 * @version 1.3
 * A Eventhandler to move pieces
 */

public class LabelEvent implements EventHandler<MouseEvent> {
    /**Piece to be moved*/
    static Piece currentPiece = null;

    /**Source Field of the Piece*/
    static FieldLabel eventSource = null;

    /**Target Field for Piece*/
    static FieldLabel eventTarget = null;

    @Override
    public void handle(MouseEvent event) {
        if(eventSource == null)
        {
            eventSource = (FieldLabel) event.getSource();
            if(eventSource.getPiece() != null) //Checks if theres a Piece on the Field
            {
                currentPiece = eventSource.getPiece();
                System.out.println("im here:\n" + currentPiece.getFieldLabel());
                System.out.println("These Are my Moves:\n"+currentPiece.calculateValidMoves(eventSource.getBoard()));
            }
            else{
                System.out.println("is empty");
                eventSource = null;
            }
        }
        else{
            eventTarget = (FieldLabel) event.getSource();

            //Checks if Target and Source are the same
            if(eventTarget.hashCode() != eventSource.hashCode() && isValidMove(eventSource, eventTarget, currentPiece)){
                currentPiece.setFieldLabel(eventTarget);
                eventTarget.setPiece(currentPiece);
                eventSource.removePiece();
                eventSource = null;
                eventTarget = null;
                currentPiece = null;
            }
            else{
                eventTarget = null;
                eventSource = null;
            }
        }
    }
    public boolean isValidMove(FieldLabel source, FieldLabel target, Piece currentPiece){
        return currentPiece.calculateValidMoves(source.getBoard()).contains(target);
    }
}
