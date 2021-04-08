package model.pieces;

import model.Chessboard;
import model.Color;
import model.Field;

import java.util.ArrayList;

public class King extends Piece{
    public King(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }

    private Chessboard chessboard = Chessboard.getInstance();
    private ArrayList<Field> availableMoves;

    @Override
    public ArrayList<Field> getMoves() {
        availableMoves = new ArrayList<>();
        chessboard = Chessboard.getInstance();
        int line = field.getLine(), column = field.getColumn();

        for (int i = -1; i < 2; i++) {
            evaluate(line+1, column+i);
            evaluate(line-1, column+i);
            if(i != 0){
                evaluate(line, column+i);
            }
        }

        return availableMoves;
    }

    private boolean evaluate(int line, int column){
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
    public boolean isInCheck(){

        //System.out.println("I am here:" + this.field);
        ArrayList<Piece> pieces = chessboard.getColorToMove() == Color.BLACK ? chessboard.getWhitePieces(): chessboard.getBlackPieces();

        for (Piece p : pieces)
        {
            //System.out.println(p.getMoves().size());
            for (Field f : p.getMoves())
            {
                //System.out.println((p == Chessboard.getInstance().getFields()[p.getField().getLine()][p.getField().getColumn()].getPiece()));
                //System.out.println("\t"+f);
                if (f == this.getField()){
                    System.err.println("IN SCHACH VON " + p.getField());
                    return true;
                }
            }

        }
        return false;
    }
}
