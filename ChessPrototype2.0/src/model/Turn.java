package model;

import model.pieces.Piece;
import view.FieldLabel;

import java.util.Arrays;

public class Turn {
    private Field sourceField;
    private Field targetField;
    private Piece movingPiece;
    private Piece eatenPiece;
    private Color colorToMove;
    private boolean isCastleTurn;
    private Gamestate gamestate;
    private boolean[] castlePermissions;
    private int castleSide, turnNumber;

    public Turn(FieldLabel sourceField, FieldLabel targetField) {
        this(Chessboard.getInstance().getFields()[sourceField.getLine()][sourceField.getColumn()],
             Chessboard.getInstance().getFields()[targetField.getLine()][targetField.getColumn()]);

        /*Chessboard chessboard = Chessboard.getInstance();

        this.sourceField = chessboard.getFields()[sourceField.getLine()][sourceField.getColumn()];
        this.targetField = chessboard.getFields()[targetField.getLine()][targetField.getColumn()];
        this.isCastleTurn = false;


        // Sets movingPiece and eatenPiece
        if(chessboard.getFields()[sourceField.getLine()][sourceField.getColumn()].hasPiece()){
            movingPiece = chessboard.getFields()[sourceField.getLine()][sourceField.getColumn()].getPiece();
        }
        if(chessboard.getFields()[targetField.getLine()][targetField.getColumn()].hasPiece()){
            eatenPiece = chessboard.getFields()[targetField.getLine()][targetField.getColumn()].getPiece();
        }*/

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

    public void setCastleTurn(boolean[] castlePermissions, int side) {
        /*
            side
            0 => long castling
            1 => short castling
         */
        isCastleTurn = true;
        // deep copy the castling permissions
        System.out.println("HERE!");
        System.out.println(castlePermissions[0]);
        System.out.println(castlePermissions[1]);
        System.out.println(castlePermissions[2]);
        System.out.println(castlePermissions[3]);
        this.castlePermissions = new boolean[] {castlePermissions[0],castlePermissions[1],castlePermissions[2],castlePermissions[3]};
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
}
