package model;

import model.pieces.Piece;

public class Field {
    private Piece piece;
    private int x, y;
    private boolean exists = true;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public Field(int x, int y) {
        this(x, y, null);
    }
    public Field(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public boolean hasPiece(){
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Field{" +
                "piece=" + piece +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
