package model.pieces;

import model.Chessboard;
import model.Color;
import model.Field;

import java.util.ArrayList;

public class Knight extends Piece{
    public Knight(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }

    private Chessboard chessboard;
    private ArrayList<Field> availableMoves;

    @Override
    public ArrayList<Field> getMoves() {
        chessboard = Chessboard.getInstance();
        int line = field.getLine(), column = field.getColumn();
        availableMoves = new ArrayList<>();

        evaluate(line+1, column+2);
        evaluate(line+2, column+1);
        evaluate(line+2, column-1);
        evaluate(line+1, column-2);
        evaluate(line-1, column-2);
        evaluate(line-2, column-1);
        evaluate(line-2, column+1);
        evaluate(line-1, column+2);

        return availableMoves;
    }

    private void evaluate(int line, int column) {
        if (line >= chessboard.getFields().length || column >= chessboard.getFields().length || line < 0 || column < 0) return;
        Field nextField = chessboard.getFields()[line][column];
        if (!nextField.isExists()) return;
        if (nextField.hasPiece() && nextField.getPiece().getColor().equals(this.color))return;
        availableMoves.add(nextField);
    }
}
