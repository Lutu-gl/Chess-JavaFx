package model.pieces;

import model.Chessboard;
import model.Color;
import model.Field;

import java.util.ArrayList;

public class Knight extends Piece{
    public Knight(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }

    @Override
    public ArrayList<Field> getMoves() {
        Field[][] labels = Chessboard.getInstance().getFields();
        int x = this.field.getColumn(), y = this.field.getLine();
        ArrayList<Field> moves = new ArrayList<Field>();
        System.out.println(x +" " + y);
        /*if(isValid(labels[x+1][y-2]))
            moves.add(labels[x+1][y-2]);

        if(isValid(labels[x+2][y-1]))
            moves.add(labels[x+2][y-1]);

        if(isValid(labels[x+2][y+1]))
            moves.add(labels[x+2][y+1]);

        if(isValid(labels[x+1][y+2]))
            moves.add(labels[x+1][y+2]);

        if(isValid(labels[x-1][y+2]))
            moves.add(labels[x-1][y+2]);

        if(isValid(labels[x-2][y+1]))
            moves.add(labels[x-2][y+1]);

        if(isValid(labels[x-2][y-1]))
            moves.add(labels[x-2][y-1]);

        if(isValid(labels[x-1][y-2]))
            moves.add(labels[x-1][y-2]);

         */

        return moves;
    }

    private boolean isValid(Field field){
        return field.isExists()&&(field.hasPiece() && field.getPiece().getColor() != this.color) || !field.hasPiece();
    }
}
