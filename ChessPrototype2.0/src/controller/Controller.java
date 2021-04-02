package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.Chessboard;
import model.Field;
import model.Turn;
import view.FieldLabel;

public class Controller implements EventHandler<MouseEvent> {
    private FieldLabel source=null;
    private FieldLabel target=null;
    private static Controller instance=null;

    private Controller(){

    }

    public static Controller getInstance(){
        if(instance == null) instance = new Controller();
        return instance;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {

        if(source == null){
            FieldLabel fieldLabel = (FieldLabel) mouseEvent.getSource();
            Field clickedField = Chessboard.getInstance().getFields()[fieldLabel.getLine()][fieldLabel.getColumn()];
            if (!clickedField.hasPiece() || !clickedField.getPiece().getColor().equals(Chessboard.getInstance().getColorToMove())) return;
            fieldLabel.getStyleClass().add("selectedField");
            source =fieldLabel;
        }else{
            target = (FieldLabel) mouseEvent.getSource();
            Turn turn = new Turn(source, target);
            source.getStyleClass().remove("selectedField");
            source = null;
            System.out.println(turn);
            turn.getMovingPiece().getMoves().forEach(System.out::println);
        }
    }
}
