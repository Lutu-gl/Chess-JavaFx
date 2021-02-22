package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import chess.Color;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Describes a Pawn and its valid moves
 * @author Stefan Hasler
 * @version 3.0
 *
 */

public class Pawn extends Piece{
    public Pawn(ImageView img, FieldLabel l, Color color, String name, int startX, int startY) {
        super(img, l, color, name,startX, startY );
    }
    int moveDirection = this.color == Color.WHITE ? -1 : 1;
    @Override
    public ArrayList<FieldLabel> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        validMoves.removeAll(validMoves);
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();

        System.out.println(startY +" Y "+y);
        System.out.println(startX +" X "+x);

        try
        {
            if(!labels[x][y+moveDirection*2].hasPiece() && (y == 6 || y == 1))
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
