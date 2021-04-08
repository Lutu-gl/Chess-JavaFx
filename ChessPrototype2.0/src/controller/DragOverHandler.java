package controller;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class DragOverHandler implements EventHandler<DragEvent> {

    @Override
    public void handle(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.MOVE);
        dragEvent.consume();
    }
}
