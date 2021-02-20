package Pieces;

import Chess.Chessboard;
import Chess.FieldLabel;
import Chess.Color;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * @author Stefan Hasler
 * @version 2.0
 * Describes a Pawn
 */

public class Pawn extends Piece{
    public Pawn(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    @Override
    public ArrayList<FieldLabel> calculateValidMoves(Chessboard board) {
        int x = this.fieldLabel.getX(), y = this.fieldLabel.getY();
        validMoves.removeAll(validMoves);
        FieldLabel field1 = null;
        FieldLabel field3 = null;
        FieldLabel field4 = null;
        try {  field1 = board.getLabelByCoordinates(x,y+1); }
        catch (IndexOutOfBoundsException ignored){ }

        try {  field3 = board.getLabelByCoordinates(x+1,y+1); }
        catch (IndexOutOfBoundsException ignored){ }

        try {  field4 = board.getLabelByCoordinates(x-1,y+1); }
        catch (IndexOutOfBoundsException ignored){ }

//||
       if(color == Color.BLACK)
       {
           if(field1 != null && !field1.hasPiece())
               validMoves.add(field1);
           if(field3 != null && field3.hasPiece() && field3.getPiece().getColor() == Color.WHITE)
               validMoves.add(field3);
           if(field4 != null && field4.hasPiece() && field4.getPiece().getColor() == Color.WHITE)
               validMoves.add(field4);
       }

       return validMoves;
    }
}
