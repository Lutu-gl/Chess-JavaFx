package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import javafx.scene.image.ImageView;
import chess.Color;

import java.util.ArrayList;
/**
 * Descripes a Bishop and its valid moves
 * @author Stefan Hasler
 * version 1.1
 */


public class Bishop extends Piece{

    public Bishop(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    @Override
    public ArrayList<FieldLabel> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        validMoves.removeAll(validMoves);
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();


        for (int i = x, i2 = y; i != 8 && i2 != -1; i++, i2--) {
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() != this.hashCode()) {
                if(labels[i][i2].getPiece().getColor()!=this.color) validMoves.add(labels[i][i2]);
                break;
            }

            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(labels[i][i2]);

        }
        for (int i = x, i2 = y; i != 8 && i2 != 8; i++, i2++) {
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() != this.hashCode()) {
                if(labels[i][i2].getPiece().getColor()!=this.color) validMoves.add(labels[i][i2]);
                break;
            }
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(labels[i][i2]);

        }
        for (int i = x,i2 = y; i != -1 && i2 != 8; i--, i2++){
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() != this.hashCode()) {
                if(labels[i][i2].getPiece().getColor()!=this.color) validMoves.add(labels[i][i2]);
                break;
            }
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(labels[i][i2]);
        }
        for (int i = x, i2 = y; i != -1 && i2 != -1; i--, i2--){
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() != this.hashCode()) {
                if(labels[i][i2].getPiece().getColor()!=this.color) validMoves.add(labels[i][i2]);
                break;
            }
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(labels[i][i2]);
        }

        return validMoves;
    }
}
