package model.pieces;

import model.Chessboard;
import model.Color;
import model.Field;

import java.util.ArrayList;
import java.util.Collections;

public class King extends Piece{
    public King(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }
    public King(Color color, String name, Field field) {
        super(color, name, field, Integer.MAX_VALUE, color == Color.BLACK ? 'k' : 'K');
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
    /*public boolean isInCheck(){

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
    }*/
    public boolean isInCheck() {

        // Search
        ArrayList<Character> search = new ArrayList<>();

        // Check for checks by knight
        search.add(color.equals(Color.WHITE)?'n':'N');
        if (checkHelper(new Knight(color, "ByKingGenerated", field, 3, 'G').getMoves(), search)) return true;
        search.clear();

        // Check for check by bishop or queen
        if (color.equals(Color.WHITE)) Collections.addAll(search, 'b','q');
        else Collections.addAll(search, 'B','Q');
        if (checkHelper(new Bishop(color, "ByKingGenerated", field, 3, 'G').getMoves(), search)) return true;
        search.clear();

        // Check for check by rook or queen
        if (color.equals(Color.WHITE)) Collections.addAll(search, 'r','q');
        else Collections.addAll(search, 'R','Q');
        if (checkHelper(new Rook(color, "ByKingGenerated", field, 5, 'G').getMoves(), search)) return true;
        search.clear();

        // Check for check by King
        if (color.equals(Color.WHITE)) Collections.addAll(search, 'k');
        else Collections.addAll(search, 'K');
        if (checkHelper(new King(color, "ByKingGenerated", field, 5, 'G').getMoves(), search)) return true;
        search.clear();

        // Check for check by Pawn
        Field pawnField;
        if (color.equals(Color.WHITE)) {
            // Check for piece left above
            if (field.getLine() >= 1 && field.getColumn() >= 1) {
                pawnField = Chessboard.getInstance().getFields()[field.getLine() - 1][field.getColumn() - 1];
                if (pawnField.hasPiece() && pawnField.getPiece().getShortName() == 'p') return true;
            }
            // Check for piece right above
            if (field.getLine() >= 1 && field.getColumn() < Chessboard.getInstance().getFields().length-1) {
                pawnField = Chessboard.getInstance().getFields()[field.getLine() - 1][field.getColumn() + 1];
                if (pawnField.hasPiece() && pawnField.getPiece().getShortName() == 'p') return true;
            }
        } else {
            // Check for piece left below
            if (field.getLine() < Chessboard.getInstance().getFields().length-1 && field.getColumn() >= 1) {
                pawnField = Chessboard.getInstance().getFields()[field.getLine() + 1][field.getColumn() - 1];
                if (pawnField.hasPiece() && pawnField.getPiece().getShortName() == 'P') return true;
            }
            // Check for piece right below
            if (field.getLine() < Chessboard.getInstance().getFields().length-1 && field.getColumn() < Chessboard.getInstance().getFields().length-1) {
                pawnField = Chessboard.getInstance().getFields()[field.getLine() + 1][field.getColumn() + 1];
                if (pawnField.hasPiece() && pawnField.getPiece().getShortName() == 'P') return true;
            }
        }
        return false;
    }

    private boolean checkHelper(ArrayList<Field> moves, ArrayList<Character> search) {

        for (Field move : moves) {
            if (move.hasPiece() && search.contains(move.getPiece().getShortName()))
                return true;
        }
        return false;
    }

}
