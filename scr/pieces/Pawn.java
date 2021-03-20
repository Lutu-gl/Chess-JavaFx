package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import gameLogic.Color;
import gameLogic.Move;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Describes a Pawn and its valid moves
 * @author Stefan Hasler
 * @version 3.5
 *
 */

public class Pawn extends Piece{

    public Pawn(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    private boolean doubleMove = false;
    private final int moveDirection = this.color == Color.WHITE ? -1 : 1;
    private FieldLabel enpassantLabel;

    @Override
    public ArrayList<Move> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();
        validMoves.removeAll(validMoves);
        //Allows pawn to move two fields when Pawn is in his first turn
        try{
            if((y == 6 || y == 1) && !labels[x][y+moveDirection*2].hasPiece() && !labels[x][y+moveDirection].hasPiece()){
                validMoves.add(new Move(this.fieldLabel, labels[x][y+moveDirection*2]));
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        //Calculates if the Piece can move forward one Square
        try
        {

            if(!labels[x][y+moveDirection].hasPiece())
                validMoves.add(new Move(this.fieldLabel, labels[x][y+moveDirection]));
        }catch (ArrayIndexOutOfBoundsException ignored){}

        //checks if theres a enemy Piece at its diagonals
        try
        {
                if(labels[x-1][y+moveDirection].hasPiece()&&labels[x-1][y+moveDirection].getPiece().getColor() != this.color)
                    validMoves.add(new Move(this.fieldLabel, labels[x-1][y+moveDirection]));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
                if(labels[x+1][y+moveDirection].hasPiece()&&labels[x+1][y+moveDirection].getPiece().getColor() != this.color)
                    validMoves.add(new Move(this.fieldLabel, labels[x+1][y+moveDirection]));
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        //en passant
        if(enpassantLabel != null){
            Move m = new Move(this.fieldLabel, enpassantLabel);
            m.setEatenPiece(Move.board.getLabelByCoordinates(enpassantLabel.getX(),enpassantLabel.getY()-moveDirection).getPiece());
            //System.out.println("fesopfeshp: " + m);
            validMoves.add(m);
        }
        /*
        try
        {
            if(canEnPassant(labels[x-1][y])){ //checks for en passant left to the piece
                Move m = new Move(this.fieldLabel, labels[x-1][y+moveDirection]);
                m.setEatenPiece(labels[x-1][y].getPiece());
                System.out.println("AAAAAAAAAAAAH");
                validMoves.add(m);
            }else if(canEnPassant(labels[x+1][y])){ //checks for en passant right to the piece
                Move m = new Move(this.fieldLabel, labels[x+1][y+moveDirection]);
                m.setEatenPiece(labels[x+1][y].getPiece());
                System.out.println("AAAAAAAAAAAAH");
                validMoves.add(m);
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}
         */

//||
       return validMoves;
    }

    @Override
    public ArrayList<FieldLabel> calculateAttackingSquares() {
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        attackingSquares.add(labels[x-1][y+moveDirection]);
        attackingSquares.add(labels[x+1][y+moveDirection]);
        return attackingSquares;
    }

    /**
     * Getter for Boolean doubleMove
     * @return Boolean true if pawn moved two squares last halfmove
     */
    public boolean isDoubleMove() {
        return doubleMove;
    }

    /**
     * sets doubleMove to true or false
     * @param m The valid halfmove
     */
    @Override
    public void postTurn(Move m) {

        //Looks if the Pawn just moved two squares by taking its last position and its current position and subtracting 2 from it
        //doubleMove = m.getTarget().getY() - moveDirection * 2 == m.getSource().getY();
        if(m.getTarget().getY() - moveDirection * 2 == m.getSource().getY()){
            Move.board.setEnPassantable(this);
        }
    }

    /**
     * Calculates if the pawn can enpassant
     * @param l Fieldlabel that has a potential enpassantable pawn on it
     * @return boolean true or false
     */
    private boolean canEnPassant(FieldLabel l){
        try
        {
            //Checks if the piece ontop is a enemy pawn and if it has just moved two squares
            if(l.getPiece().getName().contains("Pawn") && ((Pawn)l.getPiece()).isDoubleMove() && l.getPiece().getColor() != this.getColor()){
                ((Pawn) l.getPiece()).setDoubleMove(false); //could potentially be deleted
                return true;
            }
        } catch (Exception ignored){}

        return false;
    }

    /**
     * Setter for boolean DoubleMove
     * @param doubleMove true if pawn just moved two squares
     */
    public void setDoubleMove(boolean doubleMove) {
        this.doubleMove = doubleMove;
    }

    public FieldLabel getEnpassantLabel() {
        return enpassantLabel;
    }

    public void setEnpassantLabel(FieldLabel enpassantLabel) {
        this.enpassantLabel = enpassantLabel;
    }
}
/*

 */
