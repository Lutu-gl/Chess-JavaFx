package Pieces;

import Chess.FieldLabel;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import Chess.Color;

public class Pawn extends Piece{
    public Pawn(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }
}
