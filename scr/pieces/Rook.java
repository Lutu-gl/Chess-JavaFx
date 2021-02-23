package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import javafx.scene.image.ImageView;
import chess.Color;

import java.util.ArrayList;

/**
 * Describes a Rook and a movement
 * @author Stefan Hasler
 * version 1.1
 */

public class Rook extends Piece {
    public Rook(ImageView img, FieldLabel l, Color color, String name) {
        super(img, l, color, name);
    }
    @Override
    public ArrayList<FieldLabel> calculateValidMoves(Chessboard board) {
        FieldLabel[][] labels = this.fieldLabel.getBoard().getLabels();
        validMoves.removeAll(validMoves);
        int x = this.fieldLabel.getX();
        int y = this.fieldLabel.getY();

        for(int i = x; i != 8;i++){
            if(labels[i][y].hasPiece()  && labels[i][y].getPiece().hashCode() != this.hashCode()){
                if(labels[i][y].getPiece().getColor() != this.color) validMoves.add(labels[i][y]);
                break;
            }

            if(labels[i][y].hasPiece() && labels[i][y].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(labels[i][y]);
        }
        for(int i = y; i != 8;i++){

            if(labels[x][i].hasPiece()  && labels[x][i].getPiece().hashCode() != this.hashCode()){
                if(labels[x][i].getPiece().getColor() != this.color) validMoves.add(labels[x][i]);
                break;
            }

            if(labels[x][i].hasPiece() && labels[x][i].getPiece().hashCode() == this.hashCode()) continue;
            validMoves.add(labels[x][i]);
        }


        for(int i = x; i != -1;i--){
            if(labels[i][y].hasPiece()  && labels[i][y].getPiece().hashCode() != this.hashCode()){
                if(labels[i][y].getPiece().getColor() != this.color) validMoves.add(labels[i][y]);
                break;
            }
            if(labels[i][y].hasPiece() && labels[i][y].getPiece().hashCode() == this.hashCode()) continue;

            validMoves.add(labels[i][y]);
        }
        for(int i = y; i != -1;i--){

            if(labels[x][i].hasPiece()  && labels[x][i].getPiece().hashCode() != this.hashCode()){
                if(labels[x][i].getPiece().getColor() != this.color) validMoves.add(labels[x][i]);
                break;
            }

            if(labels[x][i].hasPiece() && labels[x][i].getPiece().hashCode() == this.hashCode()) continue;
            validMoves.add(labels[x][i]);

        }

        return validMoves;
    }
}
/*
        for (int i = y; i != 8; i++)
        {
            if(!labels[x][i].hasPiece()){
                validMoves.add(labels[x][i]);
                System.out.println(labels[i][y].toString());
            }
        }

 */