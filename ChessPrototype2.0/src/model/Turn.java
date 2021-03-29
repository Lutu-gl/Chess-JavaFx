package model;

import model.pieces.Piece;

public class Turn {
    private Field sourceField;
    private Field TargetField;
    private Piece movingPiece;

    public Field getSourceField() {
        return sourceField;
    }

    public void setSourceField(Field sourceField) {
        this.sourceField = sourceField;
    }

    public Field getTargetField() {
        return TargetField;
    }

    public void setTargetField(Field targetField) {
        TargetField = targetField;
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

    private Piece eatenPiece;
}
