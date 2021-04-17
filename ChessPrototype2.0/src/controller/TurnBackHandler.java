package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Chessboard;
import model.Turn;

import java.util.ArrayList;

public class TurnBackHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
        Chessboard chessboard = Chessboard.getInstance();
        ArrayList<Turn> turns = chessboard.getTurns();
        chessboard.undoTurn(turns.get(turns.size()-1));
    }
}
