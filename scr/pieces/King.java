package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import chess.Move;
import javafx.scene.image.ImageView;
import chess.Color;

import java.util.ArrayList;

/**
 * Describes a King and his Movements
 * @author Stefan Hasler
 * @version 1.2
 */

public class King extends Piece{
    public King(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    boolean canCastleKing = true;
    boolean canCastleQueen = true;
    @Override
    public ArrayList<Move> calculateValidMoves(Chessboard board) {
        validMoves.removeAll(validMoves);
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();

        for(int i = x-1; i != x+2;i++){
            try { addIfValid(labels[i][y-1]); }catch (ArrayIndexOutOfBoundsException ignored){}
            try { addIfValid(labels[i][y+1]); }catch (ArrayIndexOutOfBoundsException ignored){}
        }
        try { addIfValid(labels[x-1][y]); } catch (ArrayIndexOutOfBoundsException ignored){}
        try { addIfValid(labels[x+1][y]); } catch (ArrayIndexOutOfBoundsException ignored){}

        return validMoves;
    }

    @Override
    public ArrayList<FieldLabel> calculateAttackingSquares() {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();
        for(int i = x-1; i != x+2;i++){
            try { attackingSquares.add(labels[i][y-1]); }catch (ArrayIndexOutOfBoundsException ignored){}
            try { attackingSquares.add(labels[i][y+1]); }catch (ArrayIndexOutOfBoundsException ignored){}
        }
        try { attackingSquares.add(labels[x-1][y]); } catch (ArrayIndexOutOfBoundsException ignored){}
        try { attackingSquares.add(labels[x+1][y]); } catch (ArrayIndexOutOfBoundsException ignored){}

        return attackingSquares;
    }

    private void addIfValid(FieldLabel l){
        if((l.hasPiece() && l.getPiece().getColor() != this.color) || !l.hasPiece())
            validMoves.add(new Move(this.fieldLabel, l));
    }
    public boolean isInCheck(){

        for (Piece e:Move.board.getPieces())
        {
            System.out.println("piece to check " + e);
            for (Move f:e.calculateValidMoves(Move.board)){
                if(this.fieldLabel == f.getTarget() && f.getSource().getPiece().getColor() != this.color){
                    System.out.println("ich bin im schach von" + f.getSource().getPiece());
                    return true;
                }
            }
        }
        return false;
    }
//<>
}
