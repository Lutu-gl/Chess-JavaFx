package model.pieces;

import model.Chessboard;
import model.Color;
import model.Field;

import java.util.ArrayList;

public class Rook extends Piece{
    public Rook(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }
    private Chessboard chessboard;
    private ArrayList<Field> availableMoves;

    @Override
    public ArrayList<Field> getMoves() {
        availableMoves = new ArrayList<>();
        chessboard = Chessboard.getInstance();
        int line = field.getLine(), column = field.getColumn();

        boolean[] continueSearches = new boolean[]{true, true, true, true};
        for(int gap = 1; ;gap++){
            if(continueSearches[0])
                continueSearches[0] = evaluate(line+gap, column);
            if(continueSearches[1])
                continueSearches[1] = evaluate(line-gap, column);
            if(continueSearches[2])
                continueSearches[2] = evaluate(line, column+gap);
            if(continueSearches[3])
                continueSearches[3] = evaluate(line, column-gap);
            if(!continueSearches[0] && !continueSearches[1] && !continueSearches[2] && !continueSearches[3]){
                break;
            }
        }

        return availableMoves;
    }

    private boolean evaluate(int line, int column) {
        if(line >= chessboard.getFields().length || column >= chessboard.getFields().length || line < 0 || column < 0) return false;
        Field nextField = chessboard.getFields()[line][column];

        if(nextField.isExists()){
            if(nextField.hasPiece()){
                if(!nextField.getPiece().getColor().equals(this.color)){ //Wenns nit dieselbe Farbe hat kann man schlagen
                    availableMoves.add(nextField);
                }
                return false;
            }else{
                availableMoves.add(nextField);
                return true;
            }
        }else{ //Wenns nit existiert
            return false;
        }
    }
}
