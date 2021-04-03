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

        if(source == null)
        {
            FieldLabel fieldLabel = (FieldLabel) mouseEvent.getSource();
            Field clickedField = Chessboard.getInstance().getFields()[fieldLabel.getLine()][fieldLabel.getColumn()];
            if (!clickedField.hasPiece() || !clickedField.getPiece().getColor().equals(Chessboard.getInstance().getColorToMove())) return;
            fieldLabel.getStyleClass().add("selectedField");
            source = fieldLabel;

            //kannt man eventuell in a eigne Methode tian oder af ChessboardView a Methode mochn wos des tuat
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

        } else {
            target = (FieldLabel) mouseEvent.getSource();
            Turn turn = new Turn(source, target);
            source.getStyleClass().remove("selectedField");
            source = null;
            System.out.println(turn);

            highlighted.forEach(fieldLabel -> {
                if(fieldLabel.isMarked())
                    fieldLabel.unmark();
                else
                    fieldLabel.removeOutline();
            });
            turn.getMovingPiece().getMoves().forEach(System.out::println);
        }
    }
}
