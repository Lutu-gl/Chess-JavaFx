package pieces;

import chess.*;
import gameLogic.Color;
import gameLogic.Move;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Describes a Rook and a movement
 * @author Stefan Hasler
 * version 1.3
 */

public class Rook extends Piece {
    public Rook(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }
    @Override
    public ArrayList<Move> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        validMoves.removeAll(validMoves);
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();


        for(int i = y; i != 8;i++){

            if(labels[x][i].hasPiece()  && labels[x][i].getPiece().hashCode() != this.hashCode()){
                if(labels[x][i].getPiece().getColor() != this.color) validMoves.add(new Move(this.fieldLabel, labels[x][i]));;
                break;
            }

            if(labels[x][i].hasPiece() && labels[x][i].getPiece().hashCode() == this.hashCode()) continue;
            validMoves.add(new Move(this.fieldLabel, labels[x][i]));
        }
        for(int i = x; i != 8;i++){
            if(labels[i][y].hasPiece()  && labels[i][y].getPiece().hashCode() != this.hashCode()){
                if(labels[i][y].getPiece().getColor() != this.color) validMoves.add(new Move(this.fieldLabel, labels[i][y]));;
                break;
            }

            if(labels[i][y].hasPiece() && labels[i][y].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(new Move(this.fieldLabel, labels[i][y]));
        }


        for(int i = x; i != -1;i--){
            if(labels[i][y].hasPiece()  && labels[i][y].getPiece().hashCode() != this.hashCode()){
                if(labels[i][y].getPiece().getColor() != this.color) validMoves.add(new Move(this.fieldLabel, labels[i][y]));;
                break;
            }
            if(labels[i][y].hasPiece() && labels[i][y].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(new Move(this.fieldLabel, labels[i][y]));
        }
        for(int i = y; i != -1;i--){

            if(labels[x][i].hasPiece()  && labels[x][i].getPiece().hashCode() != this.hashCode()){
                if(labels[x][i].getPiece().getColor() != this.color) validMoves.add(new Move(this.fieldLabel, labels[x][i]));;
                break;
            }

            if(labels[x][i].hasPiece() && labels[x][i].getPiece().hashCode() == this.hashCode()) continue;
            validMoves.add(new Move(this.fieldLabel, labels[x][i]));

        }

        return validMoves;
    }
    public ArrayList<FieldLabel> calculateAttackingSquares(){
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();
        for(int i = y; i != 8;i++)
            attackingSquares.add(labels[x][i]);

        for(int i = x; i != 8;i++)
            attackingSquares.add(labels[i][y]);

        for(int i = x; i != -1;i--)
            attackingSquares.add(labels[i][y]);

        for(int i = y; i != -1;i--)
            attackingSquares.add(labels[x][i]);

        return attackingSquares;
    }

    @Override
    public void postTurn(Move m) {
    }
}
/*

 */