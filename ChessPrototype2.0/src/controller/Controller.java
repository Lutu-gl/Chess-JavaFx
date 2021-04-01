package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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
            fieldLabel.getStyleClass().add("selectedField");
            source =fieldLabel;
        }else{
            target = (FieldLabel) mouseEvent.getSource();
            Turn turn = new Turn(source, target);
            source.getStyleClass().remove("selectedField");
            source = null;
            System.out.println(turn);
        }
    }
}
