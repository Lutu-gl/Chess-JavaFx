package chess;

import pieces.Piece;
import javafx.scene.control.Label;

/**
 * Describes a field on the Chessboard(a1, b4, h5 ect...)
 * @author Stefan Hasler
 * @version 1.3
 *
 */
public class FieldLabel extends Label {
    /***Coordinates of field on board*/
    private int x, y;

    /***The board which the Field is on top of*/
    private Chessboard board;

    /***Color of field*/
    private final chess.Color color;

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

    public FieldLabel(int x, int y, chess.Color color){
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

    public Chessboard getBoard() {
        return board;
    }

    public void setBoard(Chessboard board) {
        this.board = board;
    }

    public Color getColor() {
        return color;
    }

    public boolean hasPiece(){
        return piece != null;
    }

    @Override
    public String toString() {
        return "FieldLabel{" +
                "x=" + x +
                ", y=" + y +
                ", piece=" + piece +
                '}';
    }
}
