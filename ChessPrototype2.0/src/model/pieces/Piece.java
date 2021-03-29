package model.pieces;

import model.Color;
import model.Field;
import model.Turn;

import java.util.ArrayList;

public abstract class Piece {
    private Color color;
    private int value;
    private String name;
    private Field field;

    public Piece(Color color, String name, Field field, int value) {
        this.color = color;
        this.name = name;
        this.field = field;
        this.value = value;
    }

    public abstract ArrayList<Field> getMoves(Turn t);

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getValue() {
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

}
