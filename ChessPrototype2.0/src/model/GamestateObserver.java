package model;

import javafx.application.Platform;
import view.WinningScreen;

/**
 * Observer to notify when the game is over
 */
public class GamestateObserver extends Observer{
    private final Chessboard chessboard;
    private Gamestate lastGamestate = Gamestate.PLAYING;

    public GamestateObserver(Chessboard chessboard){
        this.chessboard = chessboard;
    }

    @Override
    public void update() {
        Gamestate gamestate = chessboard.getGamestate();

        if(gamestate == lastGamestate) return;

        if(gamestate == Gamestate.PLAYING || Chessboard.getInstance().debug) {
            lastGamestate = gamestate;
            return;
        }else if(gamestate == Gamestate.BLACK_WINS) {
             //System.out.println("Black wins");
        }else if(gamestate == Gamestate.WHITE_WINS) {
            //System.out.println("White wins");
        }else if(gamestate == Gamestate.STALEMATE) {
            //System.out.println("It is Stalemate");
        }else if(gamestate == Gamestate.PLAYER_CAN_CLAIM_DRAW){
            //System.out.println("A player can claim a draw if he wants");
        }else if(gamestate == Gamestate.DRAW){
            //System.out.println("It is a Draw");
        }else if(gamestate == Gamestate.WHITE_WINS_ON_TIME){
            //System.out.println("White wins on time");
        }else if(gamestate == Gamestate.BLACK_WINS_ON_TIME){
            //System.out.println("Black wins on time");
        }else if(gamestate == Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL){
            //System.out.println("It is a draw because of insufficient material");
        }
        lastGamestate = gamestate;
        Platform.runLater(new WinningScreen());

        chessboard.getTimer().cancel();
        //System.out.println(Chessboard.getInstance().getBoardAsFen());
    }
}
