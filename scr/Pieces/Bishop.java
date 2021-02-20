package Pieces;

import Chess.Chessboard;
import Chess.FieldLabel;
import javafx.scene.image.ImageView;
import Chess.Color;

import java.util.ArrayList;

public class Bishop extends Piece{

    public Bishop(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    @Override
    public ArrayList<FieldLabel> calculateValidMoves(Chessboard board) {
        return null;
    }
}
