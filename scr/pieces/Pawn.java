package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import chess.Color;
import chess.Move;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Describes a Pawn and its valid moves
 * @author Stefan Hasler
 * @version 3.1
 *
 */

public class Pawn extends Piece{

    public Pawn(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    boolean doubleMove = false;
    int moveDirection = this.color == Color.WHITE ? -1 : 1;
    @Override
    public ArrayList<Move> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();

        try
        {
            if((y == 6 || y == 1)&&!labels[x][y+moveDirection*2].hasPiece()){ //Allows pawn to move two fields when Pawn is in his first turn
                validMoves.add(new Move(this.fieldLabel, labels[x][y+moveDirection*2]));
                doubleMove = true;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            if(!labels[x][y+moveDirection].hasPiece())
                validMoves.add(new Move(this.fieldLabel, labels[x][y+moveDirection]));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            if(labels[x-1][y+moveDirection].hasPiece())
                if(labels[x-1][y+moveDirection].getPiece().getColor() != this.color)
                    validMoves.add(new Move(this.fieldLabel, labels[x-1][y+moveDirection]));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            if(labels[x+1][y+moveDirection].hasPiece())
                if(labels[x+1][y+moveDirection].getPiece().getColor() != this.color)
                    validMoves.add(new Move(this.fieldLabel, labels[x+1][y+moveDirection]));
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        //en passant
        if(canEnPassant(x-1, y, labels)){
            Move m = new Move(this.fieldLabel, labels[x-1][y+moveDirection]);
            m.setEatenPiece(labels[x-1][y-moveDirection].getPiece());
            validMoves.add(m);
        }else if( canEnPassant(x+1,y,labels)){
            Move m = new Move(this.fieldLabel, labels[x-1][y+moveDirection]);
            m.setEatenPiece(labels[x-1][y-moveDirection].getPiece());
            validMoves.add(m);
        }

//||
       return validMoves;
    }

    public boolean canMove(FieldLabel fieldLabel1){
        return (fieldLabel1.hasPiece() && fieldLabel1.getPiece().getColor() != this.color) || !fieldLabel1.hasPiece();
    }

    public boolean isDoubleMove() {
        return doubleMove;
    }

    @Override
    public void postTurn() {
        System.out.println(this.getName() + " " + doubleMove);
        validMoves.removeAll(validMoves);

    }
    private boolean canEnPassant(int x, int y, FieldLabel[][] labels){
        try
        {
            if(labels[x][y].getPiece().getName().contains("Pawn") && ((Pawn)labels[x][y].getPiece()).isDoubleMove()){
                System.out.println("EN PASSANT");
                ((Pawn) labels[x][y].getPiece()).setDoubleMove(false);
                return true;
            }
        } catch (Exception ignored){}

        return false;
    }

    public void setDoubleMove(boolean doubleMove) {
        this.doubleMove = doubleMove;
    }

}
/*
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();


        try
        {
            if((y == 6 || y == 1)&&!labels[x][y+moveDirection*2].hasPiece()){ //Allows pawn to move two fields when Pawn is in his first turn
                validMoves.add(labels[x][y+moveDirection*2]);
                doubleMove = true;
            }
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            if(!labels[x][y+moveDirection].hasPiece())
                validMoves.add(labels[x][y+moveDirection]);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            if(labels[x-1][y+moveDirection].hasPiece())
                if(labels[x-1][y+moveDirection].getPiece().getColor() != this.color)
                    validMoves.add(labels[x-1][y+moveDirection]);
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try
        {
            if(labels[x+1][y+moveDirection].hasPiece())
                if(labels[x+1][y+moveDirection].getPiece().getColor() != this.color)
                    validMoves.add(labels[x+1][y+moveDirection]);
        }
        catch (ArrayIndexOutOfBoundsException ignored){}

        //en passant
        if(canEnPassant(x-1, y, labels))
                validMoves.add(labels[x-1][y+moveDirection]);
        else if( canEnPassant(x+1,y,labels))
            validMoves.add(labels[x+1][y+moveDirection]);
 */
