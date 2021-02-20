package Chess;

import Pieces.Queen;
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
 * @version 1.0
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
        primaryStage.setTitle("Chess");
        Chessboard board = new Chessboard();
        Scene sc = new Scene(board, 700, 700);

        Label l = new Label();
        //l.addMouseListener();
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
        board.getLabels().get(0).setPiece(new Queen(new ImageView(new Image("Imgs\\B_Queen.png")), board.getLabels().get(0), Color.BLACK, "Black Queen"));
        //board.getLabels().get(0).setPiece(null);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
