package Chess;

import Pieces.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Main Class which contains the Main Method
 * @author Stefan Hasler
 * @version 1.3
 *
 */
public class Main extends Application {
    /**
     * Sets up the Chessboard
     * @param primaryStage stage given on startup by java
     */
    @Override
    public void start(Stage primaryStage){
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Floppa Chess");
        Chessboard board = new Chessboard();
        Scene sc = new Scene(board, 700, 700);


        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };

        primaryStage.setScene(sc);
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:scr/Chess/Download.jpg"));
        Pawn p = new Pawn(new ImageView(new Image("Imgs\\B_Pawn.png")), board.getLabels()[2][0], Color.BLACK, "Black Pawn");
        Pawn b = new Pawn(new ImageView(new Image("Imgs\\W_Pawn.png")), board.getLabels()[1][5], Color.WHITE, "White Pawn");
        Rook r = new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), board.getLabels()[7][4], Color.BLACK, "Black Rook");
        board.getLabels()[2][0].setPiece(p);
        board.getLabels()[1][5].setPiece(b);
        board.getLabels()[7][4].setPiece(r);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
