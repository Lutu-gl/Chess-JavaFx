package Chess;

import Pieces.Piece;
import javafx.scene.control.Label;

/**
 * Describes a field on the Chessboard(a1, b4, h5 ect...)
 * @author Stefan Hasler
 * @version 1.0
 *
 */
public class FieldLabel extends Label {
    /***Coordinates of field on board*/
    private int x, y;

    /***Color of field*/
    private Chess.Color color;

    /***the Piece that may stand on top of the Field*/
    private Piece piece;

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        this.setGraphic(piece.getImg());
    }
    public void removePiece(){
        piece = null;
        this.setGraphic(null);
    }

    public FieldLabel(int x, int y, Chess.Color color){
        this.x = x;
        this.y = y;
        this.color = color;
        this.setPrefHeight(116.0);
        this.setPrefWidth(98.0);
        this.setOnMouseClicked(new LabelEvent());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }


}
