package controller;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;

public class DragOverHandler implements EventHandler<DragEvent> {

    private DragOverHandler(){}

    private static DragOverHandler instance = null;

    public static DragOverHandler getInstance() {
        if (instance == null)
            instance = new DragOverHandler();
        return instance;
    }

    @Override
    public void handle(DragEvent dragEvent) {

    }
}
