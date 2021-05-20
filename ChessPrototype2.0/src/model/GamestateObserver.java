package model;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GamestateObserver extends Observer{
    private final Chessboard chessboard;

    public GamestateObserver(Chessboard chessboard){
        this.chessboard = chessboard;
    }

    @Override
    public void update() {
        Gamestate gamestate = chessboard.getGamestate();

        if(gamestate == Gamestate.PLAYING || Chessboard.getInstance().debug) {
            return;
        }else if(gamestate == Gamestate.BLACK_WINS) {
             System.out.println("Black wins");
        }else if(gamestate == Gamestate.WHITE_WINS) {
            System.out.println("White wins");
        }else if(gamestate == Gamestate.STALEMATE) {
            System.out.println("It is Stalemate");
        }else if(gamestate == Gamestate.PLAYER_CAN_CLAIM_DRAW){
            System.out.println("A player can claim a draw if he wants");
        }else if(gamestate == Gamestate.DRAW){
            System.out.println("It is a Draw");
        }else if(gamestate == Gamestate.WHITE_WINS_ON_TIME){
            System.out.println("White wins on time");
        }else if(gamestate == Gamestate.BLACK_WINS_ON_TIME){
            System.out.println("Black wins on time");
        }else if(gamestate == Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL){
            System.out.println("It is a draw because of insufficient material");
        }


        chessboard.getTimer().cancel();
        //System.out.println(Chessboard.getInstance().getBoardAsFen());
    }
}
