package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ClickHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent mouseEvent) {
        System.out.println("Klicked!");
    }
}
