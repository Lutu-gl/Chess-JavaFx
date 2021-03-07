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

    private boolean canCastleKing = true;
    private boolean canCastleQueen = true;
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

        //Das Castlen funktioniert nur mit der default chess position. Also wenn der Rook in der ecke ist etc.
        //Kingside
        //System.out.println("moin " + labels[7][7].getPiece().getName());

        if(canCastleKing){
            if( ((this.color == Color.WHITE) && !labels[5][7].hasPiece() && !labels[6][7].hasPiece()) || ((this.color == Color.BLACK) && !labels[5][0].hasPiece() && !labels[6][0].hasPiece())){
                validMoves.add(new Move(this.fieldLabel, (this.color == Color.WHITE) ? labels[6][7] : labels[6][0]));
            }
        }

        if(canCastleQueen){
            if( ((this.color == Color.WHITE) && !labels[1][7].hasPiece() && !labels[2][7].hasPiece() && !labels[3][7].hasPiece()) || ((this.color == Color.BLACK) && !labels[1][0].hasPiece() && !labels[2][0].hasPiece() && !labels[3][0].hasPiece())){
                validMoves.add(new Move(this.fieldLabel, (this.color == Color.WHITE) ? labels[2][7] : labels[2][0]));
            }
        }


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

        for (Piece e:Move.board.getPiecesByColor(this.color))
        {
            //System.out.println("piece to check " + e);
            for (Move f:e.calculateValidMoves(Move.board)){
                if(this.fieldLabel == f.getTarget() && f.getSource().getPiece().getColor() != this.color){
                    System.out.println("ich bin im schach von " + f.getSource().getPiece());
                    return true;
                }
            }
        }
        return false;
    }
//<>

    public boolean isCanCastleKing() {
        return canCastleKing;
    }

    public void setCanCastleKing(boolean canCastleKing) {
        this.canCastleKing = canCastleKing;
    }

    public boolean isCanCastleQueen() {
        return canCastleQueen;
    }

    public void setCanCastleQueen(boolean canCastleQueen) {
        this.canCastleQueen = canCastleQueen;
    }
}
