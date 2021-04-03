package controller;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Chessboard;
import model.Field;
import model.Turn;
import view.ChessboardView;
import view.FieldLabel;
import view.Main;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements EventHandler<MouseEvent> {
    private FieldLabel source=null;
    private FieldLabel target=null;
    private ArrayList<FieldLabel> highlighted = new ArrayList<>();
    private static Controller instance=null;

    private Controller(){

    }

    public static Controller getInstance(){
        if(instance == null) instance = new Controller();
        return instance;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {

        FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();
        Field clickedField = Chessboard.getInstance().getFields()[clickedFieldLabel.getLine()][clickedFieldLabel.getColumn()];
        if(source == null)
        {
            //FieldLabel fieldLabel = (FieldLabel) mouseEvent.getSource();
            if (!clickedField.hasPiece() || !clickedField.getPiece().getColor().equals(Chessboard.getInstance().getColorToMove())) return;
            clickedFieldLabel.select();
            source = clickedFieldLabel;

            markAvailableMoves();
        }
        else {
            if (clickedField.hasPiece() && clickedField.getPiece().getColor().equals(Chessboard.getInstance().getColorToMove())) {
                source.unselect();
                source = clickedFieldLabel;
                source.select();
                unmarkAvailableMoves();
                markAvailableMoves();
                return;
            }
            target = clickedFieldLabel;
            //if (!highlighted.contains(target))
            //    return;
            System.out.println(highlighted);
            for (int i = 0; i < highlighted.size(); i++) {
                if (highlighted.get(i).getLine() == target.getLine() && highlighted.get(i).getColumn() == target.getColumn()) {
                    Turn turn = new Turn(source, target);
                    source.unselect();
                    source = null;

                    unmarkAvailableMoves();
                    //turn.getMovingPiece().getMoves().forEach(System.out::println);
                    Chessboard.getInstance().handleTurn(turn);
                    break;
                }
            }


        }
    }

    private void markAvailableMoves() {
        for (Field d : Chessboard.getInstance().getFields()[source.getLine()][source.getColumn()].getPiece().getMoves())
        {
            if(!d.hasPiece()){
                ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()).mark();
            }
            else{
                ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()).outline();
            }
            highlighted.add(ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()));
        }
    }

    private void unmarkAvailableMoves() {
        highlighted.forEach(fieldLabel -> {
            if(fieldLabel.isMarked())
                fieldLabel.unmark();
            else
                fieldLabel.removeOutline();
        });
        highlighted.clear();
    }

}
