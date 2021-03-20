package gameLogic;

import chess.Chessboard;

public class GamestateObserver extends Observer{
    private final Chessboard chessboard;

    public GamestateObserver(Chessboard chessboard){
        this.chessboard = chessboard;
    }

    @Override
    public void update() {
        Gamestate gamestate = chessboard.getGamestate();

        if(gamestate == Gamestate.PLAYING){
            return;
        }else if(gamestate == Gamestate.BLACKWINS){
            System.out.println("Schwarz hat gewonnen");
        }else if(gamestate == Gamestate.WHITEWINS){
            System.out.println("Weiß hat gewonnen");
        }else if(gamestate == Gamestate.STALEMATE){
            System.out.println("Es ist ein Stalemate");
        }else if(gamestate == Gamestate.PLAYERCANCLAIMDRAW){
            System.out.println("Ein Spieler kann ein Draw einfordern sofern er möchte");
        }
    }

}


