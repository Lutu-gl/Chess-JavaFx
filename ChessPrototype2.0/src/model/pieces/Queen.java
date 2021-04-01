package model.pieces;

import model.Color;
import model.Field;

import java.util.ArrayList;

public class Queen extends Piece{
    public Queen(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }

    @Override
    public ArrayList<Field> getMoves() {
        return null;
    }
}
