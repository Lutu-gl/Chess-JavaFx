package controller;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import view.FieldLabel;

public class DragStartHandler implements EventHandler<MouseEvent> {


    private DragStartHandler(){}

    public static DragStartHandler instance = null;
    public static DragStartHandler getInstance() {
        if (instance == null)
            instance = new DragStartHandler();
        return instance;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        System.out.println("You are dragging right now!");
        FieldLabel clickedField = (FieldLabel) mouseEvent.getSource();
        Dragboard db = clickedField.startDragAndDrop(TransferMode.ANY);
        mouseEvent.consume();
    }
}
