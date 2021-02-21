package Pieces;

import Chess.Chessboard;
import Chess.FieldLabel;
import javafx.scene.image.ImageView;
import Chess.Color;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Describes a Knight and its movements
 * @author Stefan Hasler
 * @version 1.0
 */

public class Knight extends Piece {
    public Knight(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    @Override
    public ArrayList<FieldLabel> calculateValidMoves(Chessboard board) {

        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        validMoves.removeAll(validMoves);
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();

        FieldLabel fieldLabel1;
        try
        {

            fieldLabel1 = labels[x+1][y-2];
            if(canMove(fieldLabel1))
                validMoves.add(fieldLabel1);
        } catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            fieldLabel1 = labels[x+2][y-1];
            if(canMove(fieldLabel1))
                validMoves.add(fieldLabel1);
        } catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            fieldLabel1 = labels[x+2][y+1];
            if(canMove(fieldLabel1))
                validMoves.add(fieldLabel1);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            fieldLabel1 = labels[x+1][y+2];
            if(canMove(fieldLabel1))
                validMoves.add(fieldLabel1);
        }catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            fieldLabel1 = labels[x-1][y+2];
            if(canMove(fieldLabel1))
                validMoves.add(fieldLabel1);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            fieldLabel1 = labels[x-2][y+1];
            if(canMove(fieldLabel1))
                validMoves.add(fieldLabel1);
        }catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            fieldLabel1 = labels[x-2][y-1];
            if(canMove(fieldLabel1))
                validMoves.add(fieldLabel1);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            fieldLabel1 = labels[x-1][y-2];
            if(canMove(fieldLabel1))
                validMoves.add(fieldLabel1);
        }catch (ArrayIndexOutOfBoundsException ignored){}

        return validMoves;
    }

    /**
     * A Method called after creating the Fieldlabel to determine if the Field can be accesed by the Knight
     * @param fieldLabel1 the Fieldlabel the Knight could theoretically move to
     * @return True if he can false if not
     */
    private boolean canMove(FieldLabel fieldLabel1){
        return (fieldLabel1.hasPiece() && fieldLabel1.getPiece().getColor() != this.color) || !fieldLabel1.hasPiece();
    }
}
//<>