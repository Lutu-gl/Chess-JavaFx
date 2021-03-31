package view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Chess!");
        primaryStage.setScene(ChessboardView.init(8, 8));
        ChessboardView.setFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        primaryStage.getScene().getStylesheets().add(getClass().getResource("stylesheet.css").toString());
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
