package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Chessboard;
import model.Color;
import model.Gamestate;

/**
 * Handles Conceding
 */
public class ConcedeHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        if(Chessboard.getInstance().getColorToMove() == Color.BLACK)
            Chessboard.getInstance().setGamestate(Gamestate.WHITE_WINS);
        else
            Chessboard.getInstance().setGamestate(Gamestate.BLACK_WINS);
    }
}