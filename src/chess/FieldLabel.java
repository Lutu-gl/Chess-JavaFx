package chess;

import gameLogic.Color;
import gameLogic.Turn;
import javafx.application.Platform;
import pieces.Piece;
import javafx.scene.control.Label;

/**
 * Describes a square on the Chessboard(a1, b4, h5 ect...) extends javafx.scene.control.Label
 *
 */
public class FieldLabel extends Label {
    /***Coordinates of field on board*/
    private int x, y;

    /***The board which the Field is on top of*/
    private Chessboard board;

    /***Color of field*/
    private final Color color;

    /***the Piece that may stand on top of the Field*/
    private Piece piece;


    /**
     * Constructor for FieldLabel
     * Initializes x, y and color, sets itself to the correct size and adds a onMouseClicked Listener
     * @param x x of FieldLabel
     * @param y y of FielLabel
     * @param color enum Color of FieldLabel
     */
    public FieldLabel(int x, int y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
        this.setPrefHeight(116.0);
        this.setPrefWidth(98.0);
        this.setOnMouseClicked(new Turn());
    }

    /**
     * Getter for Piece can return null
     * @return Piece Object or null
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Setter for Piece
     * Displays the Piece and overwrites the FieldLabel of piece
     * @param piece Piece Object that is moved on top of this Label
     */
    public void setPiece(Piece piece) {
        //board.removePiece(this.piece);
        this.piece = piece;
        //to ensure that the piece has the correct FieldLabel
        //piece.setFieldLabel(this);
        //piece.getImg().setFitHeight(this.getHeight());
        //piece.getImg().setFitWidth(this.getWidth());

        //Platform.runLater(() -> this.setGraphic(piece.getImg())); //Maybe needed for multithreading
        this.setGraphic(piece.getImg());
    }

    /**
     * Remove/Kill Piece of this FieldLabel
     */
    public void removePiece(){
        piece = null;
        //Platform.runLater(() -> this.setGraphic(null)); //Maybe needed for multithreading
        this.setGraphic(null);
    }

    /**
     * Getter for x Coordinate
     * @return int x
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y Coordinate
     * @return int y
     */
    public int getY() {
        return y;
    }

    /**
     * Getter for Chessboard of current game
     * @return Chessboard object
     * @deprecated
     */
    @Deprecated
    public Chessboard getBoard() {
        return board;
    }

    /**
     * Setter for board
     * @param board Chessboard object of current game
     */
    public void setBoard(Chessboard board) {
        this.board = board;
    }

    /**
     * Getter for enum Color
     * @return enum Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Checks if piece != null
     * @return boolean true or false
     */
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

    @Override
    public boolean equals(Object obj) {
        FieldLabel l = (FieldLabel) obj;
        return this.x == l.getX() && this.y == l.getY();
    }
}
