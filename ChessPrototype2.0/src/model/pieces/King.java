package model.pieces;

import model.Chessboard;
import model.Color;
import model.Field;

import java.util.*;

import static java.util.Map.entry;

public class King extends Piece{
    public King(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }

    private Chessboard chessboard = Chessboard.getInstance();
    private ArrayList<Field> availableMoves;
    private boolean illegal;

    public ArrayList<Field> getMoves(boolean castle) {
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

        // Check for castling
        if (castle) {
            boolean[] permissions = chessboard.getCastlePermissions();
            if (color.equals(Color.WHITE)) {
                if (permissions[0] && chessboard.isLegal(chessboard.getFields()[line][column-2], field) && !isInCheck())
                    availableMoves.add(chessboard.getFields()[line][column - 2]);
                if (permissions[1] && chessboard.isLegal(chessboard.getFields()[line][column+2], field) && !isInCheck())
                    availableMoves.add(chessboard.getFields()[line][column + 2]);
            } else {
                if (permissions[2] && chessboard.isLegal(chessboard.getFields()[line][column-2], field) && !isInCheck())
                    availableMoves.add(chessboard.getFields()[line][column - 2]);
                if (permissions[3] && chessboard.isLegal(chessboard.getFields()[line][column+2], field) && !isInCheck())
                    availableMoves.add(chessboard.getFields()[line][column + 2]);
            }
        }

        return availableMoves;
    }

    @Override
    public ArrayList<Field> getMoves() {
        return getMoves(true);
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

    public boolean isInCheck() {

        /*
        // Search
        ArrayList<Character> search = new ArrayList<>();

        // Check for checks by knight
        search.add(color.equals(Color.WHITE)?'n':'N');
        if (checkHelper(new Knight(color, "ByKingGenerated", field, 3, 'G').getMoves(), search))
            return true;
        search.clear();

        // Check for check by bishop or queen
        if (color.equals(Color.WHITE)) Collections.addAll(search, 'b','q');
        else Collections.addAll(search, 'B','Q');
        if (checkHelper(new Bishop(color, "ByKingGenerated", field, 3, 'G').getMoves(), search))
            return true;
        search.clear();

        // Check for check by rook or queen
        if (color.equals(Color.WHITE)) Collections.addAll(search, 'r','q');
        else Collections.addAll(search, 'R','Q');
        if (checkHelper(new Rook(color, "ByKingGenerated", field, 5, 'G').getMoves(), search))
            return true;
        search.clear();

        // Check for check by King
        if (color.equals(Color.WHITE)) Collections.addAll(search, 'k');
        else Collections.addAll(search, 'K');
        if (checkHelper(getMoves(false), search))
            return true;
        search.clear();

        // Check for check by Pawn
        Field pawnField;
        if (color.equals(Color.WHITE)) {
            // Check for piece left above
            if (field.getLine() >= 1 && field.getColumn() >= 1) {
                pawnField = Chessboard.getInstance().getFields()[field.getLine() - 1][field.getColumn() - 1];
                if (pawnField.hasPiece() && pawnField.getPiece().getShortName() == 'p')
                    return true;
            }
            // Check for piece right above
            if (field.getLine() >= 1 && field.getColumn() < Chessboard.getInstance().getFields().length-1) {
                pawnField = Chessboard.getInstance().getFields()[field.getLine() - 1][field.getColumn() + 1];
                if (pawnField.hasPiece() && pawnField.getPiece().getShortName() == 'p')
                    return true;
            }
        } else {
            // Check for piece left below
            if (field.getLine() < Chessboard.getInstance().getFields().length-1 && field.getColumn() >= 1) {
                pawnField = Chessboard.getInstance().getFields()[field.getLine() + 1][field.getColumn() - 1];
                if (pawnField.hasPiece() && pawnField.getPiece().getShortName() == 'P')
                    return true;
            }
            // Check for piece right below
            if (field.getLine() < Chessboard.getInstance().getFields().length-1 && field.getColumn() < Chessboard.getInstance().getFields().length-1) {
                pawnField = Chessboard.getInstance().getFields()[field.getLine() + 1][field.getColumn() + 1];
                if (pawnField.hasPiece() && pawnField.getPiece().getShortName() == 'P')
                    return true;
            }
        }
        return false;

         */
        // Check knight checks first
        char search = color.equals(Color.WHITE) ? 'n' : 'N';
        for (Field move : new Knight(color, "ByKingGenerated", field, 3, 'G').getMoves()) {
            if (move.hasPiece()) {
                if (move.getPiece().getShortName() == search)
                    return true;
            }
        }
        // Check pawn checks now
        ArrayList<Field> moves = new ArrayList<>();
        int line = field.getLine(), column = field.getColumn();
        if (color.equals(Color.WHITE)) {
            if (checkHelperPawn(line-1, column-1, 'p')) return true;
            if (checkHelperPawn(line-1, column+1, 'p')) return true;
        } else {
            if (checkHelperPawn(line+1, column-1, 'P')) return true;
            if (checkHelperPawn(line+1, column+1, 'P')) return true;
        }

        search = color.equals(Color.WHITE) ? 'k' : 'K';
        int size = chessboard.getFields().length;
        for (int i = -1; i < 2; i++) {
            for (int x = -1; x < 2; x++) {
                if (line+i < 0 || column+x < 0 || line+i >= size || column+x >= size) continue;
                Field move = chessboard.getFields()[line+i][column+x];
                if (move.hasPiece() && move.getPiece().getShortName() == search) return true;
            }
        }
        // Check checks for all other pieces now!!
        /*
        Indexes:
            0 => up
            1 => right up
            2 => right
            3 => right down
            4 => down
            5 => left down
            6 => left
            7 => left up
         */
        boolean[] continueSearch = new boolean[]{true, true, true, true, true, true, true, true};
        /*
        Indexes:
            0 => white straight search
            1 => white diagonal search
            2 => black straight search
            3 => black diagonal search
         */
        ArrayList<ArrayList<Character>> directions = new ArrayList<>();
        directions.add(new ArrayList<>(Arrays.asList('r', 'q')));
        directions.add(new ArrayList<>(Arrays.asList('b', 'q')));
        directions.add(new ArrayList<>(Arrays.asList('R', 'Q')));
        directions.add(new ArrayList<>(Arrays.asList('B', 'Q')));

        illegal = false;
        int offset = color.equals(Color.WHITE)?0:2;
        // Search up
        for (int i = 1; continueSearch[0]; i++ ) continueSearch[0] = checkHelper(line-i, column, directions.get(offset));
        if (illegal) return true;

        // Search right up
        for (int i = 1; continueSearch[1]; i++ ) continueSearch[1] = checkHelper(line-i, column+i, directions.get(offset+1));
        if (illegal) return true;

        // Search right
        for (int i = 1; continueSearch[2]; i++ ) continueSearch[2] = checkHelper(line, column+i, directions.get(offset));
        if (illegal) return true;

        // Search right down
        for (int i = 1; continueSearch[3]; i++ ) continueSearch[3] = checkHelper(line+i, column+i, directions.get(offset+1));
        if (illegal) return true;

        // Search down
        for (int i = 1; continueSearch[4]; i++ ) continueSearch[4] = checkHelper(line+i, column, directions.get(offset));
        if (illegal) return true;

        // Search left down
        for (int i = 1; continueSearch[5]; i++ ) continueSearch[5] = checkHelper(line+i, column-i, directions.get(offset+1));
        if (illegal) return true;

        // Search left
        for (int i = 1; continueSearch[6]; i++ ) continueSearch[6] = checkHelper(line, column-i, directions.get(offset));
        if (illegal) return true;

        // Search left up
        for (int i = 1; continueSearch[7]; i++ ) continueSearch[7] = checkHelper(line-i, column-i, directions.get(offset+1));
        if (illegal) return true;
        return false;
    }

    private boolean checkHelper(int line, int column, ArrayList<Character> search) {
        // Check if we don't leave the board
        if (line < 0 || column < 0 || line >= chessboard.getFields().length || column >= chessboard.getFields().length) return false;

        // Check if field exists
        Field f = chessboard.getFields()[line][column];
        if (!f.isExists()) return false;

        // Check if field has piece
        if (!f.hasPiece()) return true;

        // Check if there is a check
        if (search.contains(f.getPiece().getShortName())) {
            illegal = true;
        }
        return false;

    }

    private boolean checkHelperPawn(int line, int column, char search) {
        if (line < 0 || column < 0 || line >= chessboard.getFields().length || column >= chessboard.getFields().length) return false;
        Field f = chessboard.getFields()[line][column];
        return f.isExists() && f.hasPiece() && f.getPiece().getShortName() == search;
    }

}
