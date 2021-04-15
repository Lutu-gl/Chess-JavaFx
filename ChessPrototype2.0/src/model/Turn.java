package model;

import model.pieces.Piece;
import view.FieldLabel;

public class Turn {
    private Field sourceField;
    private Field targetField;
    private Piece movingPiece;
    private Piece eatenPiece;

    public Turn(FieldLabel sourceField, FieldLabel targetField) {
        Chessboard chessboard = Chessboard.getInstance();

        this.sourceField = chessboard.getFields()[sourceField.getLine()][sourceField.getColumn()];
        this.targetField = chessboard.getFields()[targetField.getLine()][targetField.getColumn()];



        // Sets movingPiece and eatenPiece
        if(chessboard.getFields()[sourceField.getLine()][sourceField.getColumn()].hasPiece()){
            movingPiece = chessboard.getFields()[sourceField.getLine()][sourceField.getColumn()].getPiece();
        }
        if(chessboard.getFields()[targetField.getLine()][targetField.getColumn()].hasPiece()){
            eatenPiece = chessboard.getFields()[targetField.getLine()][targetField.getColumn()].getPiece();
        }

    }
    public Turn(Field sourceField, Field targetField) {
        this.sourceField = sourceField;
        this.targetField = targetField;

        if (sourceField.hasPiece())
            movingPiece = sourceField.getPiece();
        if (targetField.hasPiece())
            eatenPiece = targetField.getPiece();
    }

    public Field getSourceField() {
        return sourceField;
    }

    public void setSourceField(Field sourceField) {
        this.sourceField = sourceField;
    }

    public Field getTargetField() {
        return targetField;
    }

    public void setTargetField(Field targetField) {
        this.targetField = targetField;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public void setMovingPiece(Piece movingPiece) {
        this.movingPiece = movingPiece;
    }

    public Piece getEatenPiece() {
        return eatenPiece;
    }

    public void setEatenPiece(Piece eatenPiece) {
        this.eatenPiece = eatenPiece;
    }



    @Override
    public String toString() {
        return "Turn{" +
                "sourceField=" + sourceField +
                ", targetField=" + targetField +
                ", movingPiece=" + movingPiece +
                ", eatenPiece=" + eatenPiece +
                '}';
    }
}
