package Pieces;

import Chess.Chessboard;
import Chess.FieldLabel;
import javafx.scene.image.ImageView;
import Chess.Color;

import java.util.ArrayList;

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

        FieldLabel fieldLabel1 = labels[x-1][y-2];
        FieldLabel fieldLabel2 = labels[x-1][y-2];

        return validMoves;
    }
}
