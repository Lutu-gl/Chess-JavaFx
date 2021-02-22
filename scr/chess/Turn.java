package chess;

import pieces.Piece;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * @author Stefan Hasler
 * @version 2.2
 * A Eventhandler to move pieces
 */

public class Turn implements EventHandler<MouseEvent> {
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
                System.out.println("im here:\n" + currentPiece.getFieldLabel());
                System.out.println("These Are my Moves:\n"+currentPiece.calculateValidMoves(eventSource.getBoard()));
            }
            else{
                System.out.println(":(");
                eventSource = null;
            }
        }
        else{ //A valid piece has been selected and now the
            eventTarget = (FieldLabel) event.getSource();

            //Checks if Target and Source are the same
            if(eventTarget.hashCode() != eventSource.hashCode() && isValidMove(eventSource, eventTarget, currentPiece)){
                currentPiece.setFieldLabel(eventTarget);
                unhighlightPiece(eventSource);
                eventTarget.setPiece(currentPiece);
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
        System.out.println(label.getStyle());
        label.setStyle(label.getStyle() + ';' + (label.getStyle().equals("-fx-background-color: #F0D9B5;") ? "-fx-background-color: #829769;" : "-fx-background-color: #646F40;"));
    }
    public void unhighlightPiece(FieldLabel label){
        label.setStyle((label.getStyle().contains("-fx-background-color: #829769;") ?  "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;"));
    }
    public boolean isValidMove(FieldLabel source, FieldLabel target, Piece currentPiece){
        return currentPiece.calculateValidMoves(source.getBoard()).contains(target);
    }
}
