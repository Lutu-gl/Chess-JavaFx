package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.Server;

import java.io.IOException;

/**
 * Controller only for join menu
 */
public class JoinController {
    @FXML
    public ImageView backgroundImage;
    public AnchorPane pane;
    public Label text, errorLabel;
    public Button connectButton;
    public TextField ipField;

    /**
     * Initialize method, which sets a random background picture and places the buttons, labels and the TextField on the correct spot.
     * The entered IP address is checked with a regular expression and the connection to the host is getting established.
     * Possible errors are displayed.
     */
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
        connectButton.setLayoutX(width/3);
        ipField.setLayoutX(width/3);
        errorLabel.setLayoutX(width/3);

        connectButton.setOnAction(e -> {
            if (!ipField.getText().matches("^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$")) // Sick regex to check if IP is valid
                return;
            System.out.println("Versuche zu verbinden!");
            if (!Server.startConnection(ipField.getText())) {
                System.out.println("Der host konnte nicht erreicht werden!");
                errorLabel.setText("Unable to reach host!\nPlease try again ...");
            }
            try {
                if (Server.getInputStream().readUTF().equals("white")) {
                    MainLaxe.whiteAi = true;
                    //controller.Controller.getInstance().flipTimers();
                    MainLaxe.blackAi = false;
                    MainLaxe.invertBoard = true;
                } else {
                    MainLaxe.whiteAi = false;
                    MainLaxe.blackAi = true;
                    MainLaxe.invertBoard = false;
                }
                MainLaxe.timeWhite = Integer.parseInt(Server.getInputStream().readUTF());
                MainLaxe.timeBlack = Integer.parseInt(Server.getInputStream().readUTF());
                MainLaxe.inkrementWhite = Integer.parseInt(Server.getInputStream().readUTF());
                MainLaxe.inkrementBlack = Integer.parseInt(Server.getInputStream().readUTF());

                MainLaxe.startGame();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.out.println("Es gab Probleme beim Lesen des Servers!");
            }

        });

    }
}
