package view;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Chess!");
        primaryStage.setScene(ChessboardView.init(8, 8));
        ChessboardView.setFEN("8/8/8/2k5/4K3/8/8/8");
        primaryStage.getScene().getStylesheets().add(getClass().getResource("stylesheet.css").toString());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
