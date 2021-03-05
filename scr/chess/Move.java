package chess;

import javafx.scene.control.CheckBox;
import pieces.Piece;

import java.lang.reflect.Field;

/**
 * Describes a Move in Chess
 * @author Stefan Hasler
 * @version 1.3
 */
public class Move {
    private FieldLabel source;
    private FieldLabel target;
    private Piece movingPiece;
    private Piece eatenPiece;
    public static Chessboard board;

    //Fieldlabel of movingPiece, Fieldlabel movingPiece can move to, the to be movedPiece, the potential Piece ontop of the target Fieldlabel
    public Move(FieldLabel source, FieldLabel target) {
        this.source = source;
        this.target = target;
        try{
            this.movingPiece = source.getPiece();
            this.eatenPiece = target.getPiece();
        }
        catch (NullPointerException ignored){}
    }
    public Move(Move move) {
        this.source = move.getSource();
        this.target = move.getTarget();
        this.movingPiece = move.getMovingPiece();
        this.eatenPiece = move.getEatenPiece();
    }


    public FieldLabel getSource() {
        return source;
    }

    public void setSource(FieldLabel source) {
        this.source = source;
    }

    public FieldLabel getTarget() {
        return target;
    }

    public void setTarget(FieldLabel target) {
        this.target = target;
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
        return "Move{" +
                "source=" + source +
                ", target=" + target +
                ", movingPiece=" + movingPiece +
                ", eatenPiece=" + eatenPiece +
                '}';
    }

}
