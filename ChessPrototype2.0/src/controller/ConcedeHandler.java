package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Chessboard;
import model.Color;
import model.Gamestate;
import model.Server;

import java.io.IOException;

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
        if (Chessboard.getInstance().isPlayerConnected()) {
            try {
                Server.getOutputStream().writeUTF("RESIGN");
            } catch (IOException e) {
                //System.out.println("Fehler beim Senden!");
                e.printStackTrace();
            }
        }
    }
}