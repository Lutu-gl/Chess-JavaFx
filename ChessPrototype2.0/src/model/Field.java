package model;

import model.pieces.Piece;

public class Field {
    private Piece piece;
    private int column, line;
    private boolean exists = true;
    private String name = "";

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public Field(int column, int line) {
        this(column, line, null);
    }
    public Field(int column, int line, Piece piece) {
        this.column = column;
        this.line = line;
        this.piece = piece;

        this.name += (char) ('a' + column);
        this.name +=  Math.abs(line-8);
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

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Field{" +
                "piece=" + piece +
                ", column=" + column +
                ", line=" + line +
                '}';
    }
}
