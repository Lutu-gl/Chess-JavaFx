package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import javafx.scene.image.ImageView;
import chess.Color;

import java.util.ArrayList;

/**
 * Describes a King and his Movements
 * @author Stefan Hasler
 * @version 1.1
 */

public class King extends Piece{
    public King(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    boolean canCastleKing = true;
    boolean canCastleQueen = true;
    @Override
    public ArrayList<FieldLabel> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        validMoves.removeAll(validMoves);
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();

        for(int i = x-1; i != x+2;i++){
            try {
                if(canMove(labels[i][y-1]))
                    validMoves.add(labels[i][y-1]);
            }catch (ArrayIndexOutOfBoundsException ignored){}
            try {
                if(canMove(labels[i][y+1]))
                    validMoves.add(labels[i][y+1]);
            }catch (ArrayIndexOutOfBoundsException ignored){}
        }
        try {
           if(canMove(labels[x-1][y]))
               validMoves.add(labels[x-1][y]);
        } catch (ArrayIndexOutOfBoundsException ignored){}
        try {
            if(canMove(labels[x+1][y]))
                validMoves.add(labels[x+1][y]);
        } catch (ArrayIndexOutOfBoundsException ignored){}

        return validMoves;
    }
    private boolean canMove(FieldLabel l){
        return (l.hasPiece() && l.getPiece().getColor() != this.color) || !l.hasPiece();

    }
//<>
}
