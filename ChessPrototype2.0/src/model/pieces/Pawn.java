package model.pieces;

import controller.Controller;
import controller.PromotionDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import model.Chessboard;
import model.Color;
import model.Field;
import view.Main;

import java.util.ArrayList;

//TODO: enpassant
public class Pawn extends Piece{
    public Pawn(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }

    public Pawn(Color color, String name, Field field) {
        super(color, name, field, 1, color == Color.BLACK ? 'p' : 'P');
    }
    private ArrayList<Field> availableMoves;
    private static int promotionPieces;

    @Override
    public ArrayList<Field> getMoves() {

        int column = this.field.getColumn(), line = this.getField().getLine();
        Field[][] fields = Chessboard.getInstance().getFields();

        int defaultY;
        if(fields.length>=4){ //if the Board has less than 4 lines the pawn cant move two Fields
            defaultY = this.getColor() == Color.BLACK ?  1 : fields.length-2;
        }
        else
            defaultY = Integer.MAX_VALUE;

        int moveDirection = this.getColor() == Color.BLACK ? 1 : -1;
        availableMoves = new ArrayList<>();
        if(fieldExists(column, line+moveDirection) && !fields[line+moveDirection][column].hasPiece() && !fields[line+moveDirection][column].hasPiece()){
            availableMoves.add(fields[line+moveDirection][column]);
            if (line + moveDirection == 0 || line + moveDirection== Chessboard.getInstance().getFields().length) {
                for (int i = 0; i < 3; i++) {
                    availableMoves.add(fields[line+moveDirection][column]);
                }
            }
        }

        if(defaultY == line && fieldExists(column, line+moveDirection*2) && !fields[line+moveDirection*2][column].hasPiece() && !fields[line+moveDirection][column].hasPiece())
            availableMoves.add(fields[line+moveDirection*2][column]);

        evaluate(column+1, line+moveDirection);
        evaluate(column-1, line+moveDirection);

        //enPassant
        Pawn pawn = Chessboard.getInstance().getEnPassantable();
        if(pawn != null && (pawn.getField().getColumn() + 1 == getField().getColumn() || pawn.getField().getColumn() - 1 == getField().getColumn()) && pawn.getField().getLine() == getField().getLine()){
           Field f =  fields[pawn.getField().getLine() + (color == Color.WHITE ? -1 : +1)][pawn.getField().getColumn()];
           pawn.getField().setPiece(null);
           if(Chessboard.getInstance().isLegal(f, field, color))
                availableMoves.add(f);
           pawn.getField().setPiece(pawn);
        }
        //availableMoves.forEach(System.out::println);

        return availableMoves;
    }
    private void evaluate(int column, int line){
        Field[][] fields = Chessboard.getInstance().getFields();
        if(fieldExists(column, line) && fields[line][column].hasPiece() && fields[line][column].getPiece().getColor() != this.color) {
            availableMoves.add(fields[line][column]);
            for (int i = 0; i < 3; i++) {
                availableMoves.add(fields[line][column]);
            }
        }

    }
    private boolean fieldExists(int column, int line){
        Field[][] fields = Chessboard.getInstance().getFields();
        //System.out.println(column + " " + line + " = ");
        if(line>=fields.length || line<0 || column>=fields[0].length || column<0) return false;
        return fields[line][column].isExists();
    }
    public void promoteIfPossible(){
        Chessboard chessboard = Chessboard.getInstance();

        if(this.getField().getLine() == chessboard.getFields()[0].length-1 || this.getField().getLine() == 0){
            Piece p = null;
            if((color == Color.WHITE && chessboard.getPlaysAI()[0]) || (color == Color.BLACK && chessboard.getPlaysAI()[1])){
                String colorString = color.toString().charAt(0) + color.toString().substring(1).toLowerCase();
                switch (promotionPieces){
                    case 0 -> p = new Queen(color, colorString + " Queen", field);
                    case 1 -> p = new Rook(color, colorString + " Rook", field);
                    case 2 -> p = new Bishop(color, colorString + " Bishop", field);
                    case 3 -> p = new Knight(color, colorString + " Knight", field);
                }

                chessboard.changeTForPromotion(field);
                promotionPieces = (promotionPieces + 1) % 4;
            }else {
                p = Controller.getInstance().promotionDialog(this);
            }
            chessboard.removePiece(this);
            chessboard.addPiece(p);
            p.setField(this.getField());
            p.getField().setPiece(p);
        }
    }
}
