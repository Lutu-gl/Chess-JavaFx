package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import chess.Color;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Describes a Pawn and its valid moves
 * @author Stefan Hasler
 * @version 3.1
 *
 */

public class Pawn extends Piece{
    public Pawn(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }
    int moveDirection = this.color == Color.WHITE ? -1 : 1;
    @Override
    public ArrayList<FieldLabel> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        validMoves.removeAll(validMoves);
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();


        try
        {
            if((y == 6 || y == 1)&&!labels[x][y+moveDirection*2].hasPiece()) //Allows pawn to move two fields when Pawn is in his first turn
                validMoves.add(labels[x][y+moveDirection*2]);
        }catch (ArrayIndexOutOfBoundsException ignored){}

        try
        {
            if(!labels[x][y+moveDirection].hasPiece())
                validMoves.add(labels[x][y+moveDirection]);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            if(labels[x-1][y+moveDirection].hasPiece())
                if(labels[x-1][y+moveDirection].getPiece().getColor() != this.color)
                    validMoves.add(labels[x-1][y+moveDirection]);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            if(labels[x+1][y+moveDirection].hasPiece())
                if(labels[x+1][y+moveDirection].getPiece().getColor() != this.color)
                    validMoves.add(labels[x+1][y+moveDirection]);
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

//||
       return validMoves;
    }

    public boolean canMove(FieldLabel fieldLabel1){
        return (fieldLabel1.hasPiece() && fieldLabel1.getPiece().getColor() != this.color) || !fieldLabel1.hasPiece();
    }
}
