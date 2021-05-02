package model;

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
        }
        chessboard.getTimer().cancel();
        //System.out.println(Chessboard.getInstance().getBoardAsFen());
    }
}
