package model.pieces;

import model.Color;
import model.Field;

import java.util.ArrayList;

/**
 * Abstract class every Piece needs to inherit of
 */
public abstract class Piece {
    protected Color color;
    protected int value;
    protected String name;
    protected Field field;
    protected char shortName;
    protected int timesMoved=0;

    /**
     * Default Constructor implemented for each Piece
     * @param color color of Piece
     * @param name name of Piece("Black Queen, White Knight")
     * @param field Field Piece is on
     * @param value value of Piece
     *              Recommended for default Pieces
     *              Queen = 9
     *              Rook = 5
     *              Knight = Bishop = 3
     *              Pawn = 1
     *              King = 300
     * @param shortName single Char for Piece
     *                  lowercase = Black
     *                  uppercase = White
     *
     *                  'n' or 'N' = Knight
     *                  'k' or 'K' = King
     *                  'q' or 'Q' = Queen
     *                  'r' or 'R' = Rook
     *                  'b' or 'B'= Bishop
     *                  'p' or 'P'= Pawn
     */
    public Piece(Color color, String name, Field field, int value, char shortName) {
        this.color = color;
        this.name = name;
        this.field = field;
        this.value = value;
        this.shortName = shortName;
    }

    /**
     * every Piece needs to Implement this method to indicate which moves it can make
     * does not need to consider putting own King in check when calculating valid moves
     * @return ArrayList of Fields
     */
    public abstract ArrayList<Field> getMoves();

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public char getShortName() {
        return shortName;
    }

    public void setShortName(char shortName) {
        this.shortName = shortName;
    }

    public int getTimesMoved() {
        return timesMoved;
    }

    public void setTimesMoved(int timesMoved) {
        this.timesMoved = timesMoved;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", value=" + value +
                ", name='" + name + '\'' +
                ", timesMoved='" + timesMoved + '\'' +
                /*", field=" + field +*/
                ", shortName=" + shortName +
                '}';
    }
}
