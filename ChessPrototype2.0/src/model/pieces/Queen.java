package model.pieces;

import model.Chessboard;
import model.Color;
import model.Field;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Queen extends Piece{
    public Queen(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }
    public Queen(Color color, String name, Field field) {
        super(color, name, field, 9, color == Color.BLACK ? 'q' : 'Q');
    }

    private Chessboard chessboard;
    private ArrayList<Field> availableMoves;

    @Override
    public ArrayList<Field> getMoves() {
        return (ArrayList<Field>) Stream.concat((new Rook(this.color, "ByQueenGenerated", this.field, 5, 'Q').getMoves().stream()),(new Bishop(this.color, "ByQueenGenerated", this.field, 3, 'Q').getMoves()).stream()).collect(Collectors.toList());
    }
}
