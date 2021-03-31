package view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Chessboard;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Set Title of window
        primaryStage.setTitle("Chess!");
        // Set graphic view of the chess board. Normally it is 8x8
        primaryStage.setScene(ChessboardView.init(8, 8));
        // Set the figures with the FEN Code
        ChessboardView.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        // Set the stylesheet
        primaryStage.getScene().getStylesheets().add(getClass().getResource("stylesheet.css").toString());
        // Set the icon of the window
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        // Display the window
        primaryStage.show();
        // Initialize the virtual chessboard
        Chessboard.init("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
