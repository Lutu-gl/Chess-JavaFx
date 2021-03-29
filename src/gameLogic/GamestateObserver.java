package gameLogic;

import chess.Chessboard;

import javax.swing.*;

public class GamestateObserver extends Observer{
    private final Chessboard chessboard;

    public GamestateObserver(Chessboard chessboard){
        this.chessboard = chessboard;
    }

    @Override
    public void update() {
        Gamestate gamestate = chessboard.getGamestate();

        if(gamestate == Gamestate.PLAYING) {
            return;
        }else if(gamestate == Gamestate.BLACKWINS) {
            new WinDialog("Black Wins!").show();
           // System.out.println("Schwarz hat gewonnen");
        }else if(gamestate == Gamestate.WHITEWINS) {
            new WinDialog("White Wins!").show();
            //System.out.println("Weiß hat gewonnen");
        }else if(gamestate == Gamestate.STALEMATE) {
            new WinDialog("Stalemate").show();
            //System.out.println("Es ist ein Stalemate");
        }else if(gamestate == Gamestate.PLAYERCANCLAIMDRAW){
            String s = "";
            s = readString("Threefold repetition: If you want to Draw, then type draw. If not, then type no");
            if(s != null && s.equalsIgnoreCase("draw")){
                chessboard.setGamestate(Gamestate.DRAW);
                new WinDialog("Draw").show();
            }else{
                chessboard.setGamestate(Gamestate.PLAYING);
            }
            //System.out.println("Ein Spieler kann ein Draw einfordern sofern er möchte");

        }else if(gamestate == Gamestate.DRAW){
            new WinDialog("Draw").show();
        }
    }

    private String readString(String msg){
        String s = JOptionPane.showInputDialog(msg);
        return s;
    }

}


