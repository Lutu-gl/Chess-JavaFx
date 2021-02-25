package chess;


import pieces.Pawn;
import pieces.Piece;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

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

            //Checks if Target and Source are the same and if the move is legal
            if(move.getTarget().hashCode() != move.getSource().hashCode() && isValidMove(move) != null){
                move=isValidMove(move);

                //Make sure Pieces are removed
                try{move.getEatenPiece().getFieldLabel().removePiece();}catch(NullPointerException ignored){};
                move.getSource().removePiece();

                //Moving the Piece to the Targetfieldlabel
                move.getMovingPiece().setFieldLabel(move.getTarget());
                move.getTarget().setPiece(move.getMovingPiece());

                //Checking for Promotion
                checkPawnPromotion(move.getMovingPiece());




                //Ending Turn
                unhighlightPiece(move.getSource());
                move.getMovingPiece().postTurn();
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
    private Move isValidMove(Move move){
        for (Move e : move.getMovingPiece().calculateValidMoves(Move.board))
        {
            if(e.getTarget().equals(move.getTarget()))
                return e;
        }
        return null;
    }

    private void checkPawnPromotion(Piece p){
        if(p.getName().contains("Pawn") && (p.getFieldLabel().getY() == 0 || p.getFieldLabel().getY() == 7))
            new PromotionDialog((Pawn)p).show();
    }

    private void movePiece(){

    }
}

/*

 */