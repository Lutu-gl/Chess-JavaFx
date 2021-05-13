package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainLaxe extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage stage;

    public static Scene hostScene;

    public static void changeScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String fxmlResource = "menu.fxml";
        Parent panel, panel2;
        //panel = FXMLLoader.load(getClass().getResource(fxmlResource));
        panel = FXMLLoader.load(getClass().getResource(fxmlResource));
        panel2 = FXMLLoader.load(getClass().getResource("host.fxml"));
        Scene scene = new Scene(panel);
        hostScene = new Scene(panel2);
        stage = new Stage();
        stage.setTitle("Chess!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        stage.show();
    }
}