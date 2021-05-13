package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainLaxe extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String fxmlResource = "sample.fxml";
        Parent panel;
        //panel = FXMLLoader.load(getClass().getResource(fxmlResource));
        panel = FXMLLoader.load(getClass().getResource(fxmlResource));
        Scene scene = new Scene(panel);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}