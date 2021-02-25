package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import chess.Move;
import javafx.scene.image.ImageView;
import chess.Color;

import java.util.ArrayList;

/**
 * Describes a Queen and its moves
 * @author Lutu
 * @version 1.1
 */
public class Queen extends Piece{
    public Queen(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }

    @Override
    public ArrayList<Move> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        validMoves.removeAll(validMoves);
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
                if(labels[x][i].getPiece().getColor() != this.color) validMoves.add(new Move(this.fieldLabel, labels[x][i]));;
                break;
            }

            if(labels[x][i].hasPiece() && labels[x][i].getPiece().hashCode() == this.hashCode()) continue;
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
}
