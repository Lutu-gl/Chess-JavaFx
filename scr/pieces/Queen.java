package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import gameLogic.Move;
import javafx.scene.image.ImageView;
import gameLogic.Color;

import java.util.ArrayList;

/**
 * Describes a Queen and its moves
 * @author Lutu
 * @version 1.2
 */
public class Queen extends Piece{
    public Queen(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    @Override
    public ArrayList<Move> calculateValidMoves(Chessboard board) {
        validMoves.removeAll(validMoves);
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();

        //Rook moves
        for(int i = x; i != 8;i++){
            if(labels[i][y].hasPiece()  && labels[i][y].getPiece().hashCode() != this.hashCode()){
                if(labels[i][y].getPiece().getColor() != this.color) validMoves.add(new Move(this.fieldLabel, labels[i][y]));;
                break;
            }

            if(labels[i][y].hasPiece() && labels[i][y].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(new Move(this.fieldLabel, labels[i][y]));;
        }
        for(int i = y; i != 8;i++){
            if(labels[x][i].hasPiece()  && labels[x][i].getPiece().hashCode() != this.hashCode()){
                if(labels[x][i].getPiece().getColor() != this.color) validMoves.add(new Move(this.fieldLabel, labels[x][i]));
                break;
            }

            if(labels[x][i].hasPiece() && labels[x][i].getPiece().hashCode() == this.hashCode()){
                continue;
            }
            validMoves.add(new Move(this.fieldLabel, labels[x][i]));
        }


        for(int i = x; i != -1;i--){
            if(labels[i][y].hasPiece()  && labels[i][y].getPiece().hashCode() != this.hashCode()){
                if(labels[i][y].getPiece().getColor() != this.color) validMoves.add(new Move(this.fieldLabel, labels[i][y]));;
                break;
            }
            if(labels[i][y].hasPiece() && labels[i][y].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(new Move(this.fieldLabel, labels[i][y]));;
        }
        for(int i = y; i != -1;i--){

            if(labels[x][i].hasPiece()  && labels[x][i].getPiece().hashCode() != this.hashCode()){
                if(labels[x][i].getPiece().getColor() != this.color) validMoves.add(new Move(this.fieldLabel, labels[x][i]));;
                break;
            }

            if(labels[x][i].hasPiece() && labels[x][i].getPiece().hashCode() == this.hashCode()) continue;
            validMoves.add(new Move(this.fieldLabel, labels[x][i]));;

        }

        //Bishop moves
        for (int i = x, i2 = y; i != 8 && i2 != -1; i++, i2--) {
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() != this.hashCode()) {
                if(labels[i][i2].getPiece().getColor()!=this.color) validMoves.add(new Move(this.fieldLabel, labels[i][i2]));;
                break;
            }

            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(new Move(this.fieldLabel, labels[i][i2]));;

        }
        for (int i = x, i2 = y; i != 8 && i2 != 8; i++, i2++) {
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() != this.hashCode()) {
                if(labels[i][i2].getPiece().getColor()!=this.color) validMoves.add(new Move(this.fieldLabel, labels[i][i2]));;
                break;
            }
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(new Move(this.fieldLabel, labels[i][i2]));;

        }
        for (int i = x,i2 = y; i != -1 && i2 != 8; i--, i2++){
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() != this.hashCode()) {
                if(labels[i][i2].getPiece().getColor()!=this.color) validMoves.add(new Move(this.fieldLabel, labels[i][i2]));;
                break;
            }
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(new Move(this.fieldLabel, labels[i][i2]));;
        }
        for (int i = x, i2 = y; i != -1 && i2 != -1; i--, i2--){
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() != this.hashCode()) {
                if(labels[i][i2].getPiece().getColor()!=this.color) validMoves.add(new Move(this.fieldLabel, labels[i][i2]));;
                break;
            }
            if(labels[i][i2].hasPiece() && labels[i][i2].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(new Move(this.fieldLabel, labels[i][i2]));;
        }



        return validMoves;
    }

    @Override
    public ArrayList<FieldLabel> calculateAttackingSquares() {
        int x = this.fieldLabel.getX();
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        int y = this.fieldLabel.getY();
        //new Rook().calculateAttackingSquares();
        //Rook
        for(int i = y; i != 8;i++)
            attackingSquares.add(labels[x][i]);
        for(int i = x; i != 8;i++)
            attackingSquares.add(labels[i][y]);
        for(int i = x; i != -1;i--)
            attackingSquares.add(labels[i][y]);
        for(int i = y; i != -1;i--)
            attackingSquares.add(labels[x][i]);

        //Bishop
        for (int i = x, i2 = y; i != 8 && i2 != -1; i++, i2--)
            attackingSquares.add(labels[i][i2]);
        for (int i = x, i2 = y; i != 8 && i2 != 8; i++, i2++)
            attackingSquares.add(labels[i][i2]);
        for (int i = x,i2 = y; i != -1 && i2 != 8; i--, i2++)
            attackingSquares.add(labels[i][i2]);
        for (int i = x, i2 = y; i != -1 && i2 != -1; i--, i2--)
            attackingSquares.add(labels[i][i2]);

        return attackingSquares;
    }
}
