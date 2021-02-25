package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import chess.Move;
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
    public ArrayList<Move> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        validMoves.removeAll(validMoves);
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();

        for(int i = x-1; i != x+2;i++){
            try { addIfValid(labels[i][y-1]); }catch (ArrayIndexOutOfBoundsException ignored){}
            try { addIfValid(labels[i][y+1]); }catch (ArrayIndexOutOfBoundsException ignored){}
        }
        try { addIfValid(labels[x-1][y]); } catch (ArrayIndexOutOfBoundsException ignored){}
        try { addIfValid(labels[x+1][y]); } catch (ArrayIndexOutOfBoundsException ignored){}

        return validMoves;
    }
    private void addIfValid(FieldLabel l){
        if((l.hasPiece() && l.getPiece().getColor() != this.color) || !l.hasPiece())
            validMoves.add(new Move(this.fieldLabel, l));
    }
//<>
}
