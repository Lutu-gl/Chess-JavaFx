package model;

import model.pieces.Pawn;
import model.pieces.Piece;
import view.FieldLabel;

public class Turn {
    private Field sourceField;
    private Field targetField;
    private Piece movingPiece;
    private Piece eatenPiece;
    private Color colorToMove;
    private boolean isCastleTurn, isEnpassantTurn;
    private Gamestate gamestate;
    private boolean[] castlePermissions;
    private int castleSide, turnNumber, ruleCounter, turnsPlayed;
    private Pawn enpassantable;

    public Turn(FieldLabel sourceField, FieldLabel targetField) {
        this(Chessboard.getInstance().getFields()[sourceField.getLine()][sourceField.getColumn()],
             Chessboard.getInstance().getFields()[targetField.getLine()][targetField.getColumn()]);
    }
    public Turn(Field sourceField, Field targetField) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.isCastleTurn = false;
        this.colorToMove = sourceField.getPiece().getColor();
        this.turnNumber = Chessboard.getInstance().getTurns().size();
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

    public void setCastleTurn(int side) {
        /*
            side
            0 => long castling
            1 => short castling
         */
        isCastleTurn = true;
        // deep copy the castling permissions
        //this.castlePermissions = new boolean[] {castlePermissions[0],castlePermissions[1],castlePermissions[2],castlePermissions[3]};
        this.castleSide = side;
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

    public Color getColorToMove() {
        return colorToMove;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    public boolean isCastleTurn() {
        return isCastleTurn;
    }

    public boolean[] getCastlePermissions() {
        return castlePermissions;
    }

    public int getCastleSide() {
        return castleSide;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getRuleCounter() {
        return ruleCounter;
    }

    public void setRuleCounter(int ruleCounter) {
        this.ruleCounter = ruleCounter;
    }

    public void setEnpassantTurn(boolean t) {
        isEnpassantTurn = t;
    }

    public void setEnpassantable(Pawn pawn) {
        enpassantable = pawn;
    }

    public Pawn getEnpassantable() {
        return enpassantable;
    }

    public boolean isEnpassantTurn() {
        return isEnpassantTurn;
    }


    public int getTurnsPlayed() {
        return turnsPlayed;
    }

    public void setTurnsPlayed(int turnsPlayed) {
        this.turnsPlayed = turnsPlayed;
    }

    public void setCastlePermissions(boolean[] castlePermissions) {
        // deep copy the castling permissions
        this.castlePermissions = new boolean[] {castlePermissions[0],castlePermissions[1],castlePermissions[2],castlePermissions[3]};
    }
}
