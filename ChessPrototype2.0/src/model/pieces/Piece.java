package model.pieces;

import model.Color;
import model.Field;

import java.util.ArrayList;

public abstract class Piece {
    protected Color color;
    protected int value;
    protected String name;
    protected Field field;
    protected char shortName;

    public Piece(Color color, String name, Field field, int value, char shortName) {
        this.color = color;
        this.name = name;
        this.field = field;
        this.value = value;
        this.shortName = shortName;
    }

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

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", value=" + value +
                ", name='" + name + '\'' +
                /*", field=" + field +*/
                ", shortName=" + shortName +
                '}';
    }
}
