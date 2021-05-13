package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class HostController {
    @FXML
    public Label text;
    public ImageView backgroundImage;
    public AnchorPane pane;

    @FXML
    public void initialize() {
        int pic = (int) (Math.floor(Math.random()*5)) + 1;
        System.out.println(pic);
        Image image = new Image(Main.class.getResourceAsStream("/wallpaper"+pic+".jpg"));
        backgroundImage.setImage(image);
        backgroundImage.setFitHeight(800.0);
        backgroundImage.setFitWidth(image.getWidth());
        double width = (image.getWidth()*800)/image.getHeight()-1;
        pane.setPrefWidth(width);
        text.setLayoutX(width/3);
        System.out.println(image.getWidth());
        System.out.println(image.getHeight());
    }
}
