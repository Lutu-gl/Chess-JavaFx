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
    private int x, y;
    private Chess.Color color;
    private Piece figure = null;

    public Piece getFigure() {
        return figure;
    }

    public void setFigure(Piece figure) {
        this.figure = figure;
        this.setGraphic(figure.getImg());
    }

    public FieldLabel(int x, int y, Chess.Color color){
        this.x = x;
        this.y = y;
        this.color = color;
        this.setPrefHeight(116.0);
        this.setPrefWidth(98.0);
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
