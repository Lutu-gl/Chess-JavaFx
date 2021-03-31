package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.Chessboard;

public class ClickHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent mouseEvent) {
        System.out.println("Klicked!");
        Chessboard chessboard = Chessboard.getInstance();
        System.out.println(chessboard);
    }
}
