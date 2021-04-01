package model;

import model.pieces.Piece;
import view.FieldLabel;

public class Turn {
    private FieldLabel sourceField;
    private FieldLabel targetField;
    private Piece movingPiece;
    private Piece eatenPiece;

    public Turn(FieldLabel sourceField, FieldLabel targetField) {
        this.sourceField = sourceField;
        this.targetField = targetField;

        Chessboard chessboard = Chessboard.getInstance();

        // Sets movingPiece and eatenPiece
        if(chessboard.getFields()[sourceField.getLine()][sourceField.getColumn()].hasPiece()){
            movingPiece = chessboard.getFields()[sourceField.getLine()][sourceField.getColumn()].getPiece();
        }
        if(chessboard.getFields()[targetField.getLine()][targetField.getColumn()].hasPiece()){
            eatenPiece = chessboard.getFields()[targetField.getLine()][targetField.getColumn()].getPiece();
        }

    }

    public FieldLabel getSourceField() {
        return sourceField;
    }

    public void setSourceField(FieldLabel sourceField) {
        this.sourceField = sourceField;
    }

    public FieldLabel getTargetField() {
        return targetField;
    }

    public void setTargetField(FieldLabel targetField) {
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
