package model.pieces;

import model.Chessboard;
import model.Color;
import model.Field;

import java.util.ArrayList;

//TODO: enpassant
public class Pawn extends Piece{
    public Pawn(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }
    private ArrayList<Field> availableMoves;
    @Override
    public ArrayList<Field> getMoves() {

        int column = this.field.getColumn(), line = this.getField().getLine();
        Field[][] fields = Chessboard.getInstance().getFields();

        int defaultY;
        if(fields.length>=4){ //if the Board has less than 4 lines the pawn cant move two Fields
            defaultY = this.getColor() == Color.BLACK ?  1 : fields.length-2;
        }
        else
            defaultY = Integer.MAX_VALUE;

        int moveDirection = this.getColor() == Color.BLACK ? 1 : -1;
        availableMoves = new ArrayList<>();
        if(fieldExists(column, line+moveDirection) && !fields[line+moveDirection][column].hasPiece() && !fields[line+moveDirection][column].hasPiece())
            availableMoves.add(fields[line+moveDirection][column]);
        if(defaultY == line && fieldExists(column, line+moveDirection*2) && !fields[line+moveDirection*2][column].hasPiece() && !fields[line+moveDirection][column].hasPiece())
            availableMoves.add(fields[line+moveDirection*2][column]);

        evaluate(column+1, line+moveDirection);
        evaluate(column-1, line+moveDirection);

        return availableMoves;
    }
    private void evaluate(int column, int line){
        Field[][] fields = Chessboard.getInstance().getFields();
        if(fieldExists(column, line) && fields[line][column].hasPiece() && fields[line][column].getPiece().getColor() != this.color)
            availableMoves.add(fields[line][column]);
    }
    private boolean fieldExists(int column, int line){
        Field[][] fields = Chessboard.getInstance().getFields();
        //System.out.println(column + " " + line + " = ");
        if(line>=fields.length || line<0 || column>=fields[0].length || column<0) return false;
        return fields[line][column].isExists();

    }
}
