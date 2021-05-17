package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Chessboard;
import model.Color;
import model.Turn;
import view.ChessboardView;

import java.util.ArrayList;

public class TurnBackHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
        Chessboard chessboard = Chessboard.getInstance();
        ArrayList<Turn> turns = chessboard.getTurns();
        long tB = turns.get(turns.size()-1).getBlackTime();
        long tW = turns.get(turns.size()-1).getWhiteTime();

        if(chessboard.AIThinking){
            System.out.println("not allowed to turn back move");
            return;
        }
        if(Chessboard.getInstance().getPlaysAI()[1] || Chessboard.getInstance().getPlaysAI()[0]){
            System.out.println(turns.size());
            chessboard.setAllowedToMakeMove(false);
            Controller.getInstance().removePgnFromDisplay(2);
            chessboard.undoTurn(turns.get(turns.size()-2));
            chessboard.setAllowedToMakeMove(true);
            ChessboardView.display();
            //chessboard.printBoard();
        }
        else
        {
            Controller.getInstance().removePgnFromDisplay(1);
            chessboard.undoTurn(turns.get(turns.size()-1));
        }



        chessboard.setBlackTime(tB);
        chessboard.setWhiteTime(tW);
        System.out.println("white time, reset to:" + tW/1000D);
        System.out.println("black time, reset to:" + tB/1000D);
        Controller.getInstance().updateTime(tW/1000D, Color.WHITE);
        Controller.getInstance().updateTime(tB/1000D, Color.BLACK);



    }
}
