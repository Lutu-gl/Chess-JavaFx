package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HostController {
    @FXML
    public Label text;
    public ImageView backgroundImage;
    public Button imageView1, imageView2, imageView3;
    public AnchorPane pane;

    @FXML
    public void initialize() {
        int pic = (int) (Math.floor(Math.random()*5)) + 1;
        ArrayList<Color> colors = new ArrayList<>(Arrays.asList(
           Color.WHITE,
           Color.WHITE,
           Color.BLUE,
           Color.WHITE,
           Color.WHITE
        ));
        pic = 3;
        System.out.println(pic);
        Image image = new Image(Main.class.getResourceAsStream("/wallpaper"+pic+".jpg"));
        backgroundImage.setImage(image);
        backgroundImage.setFitHeight(800.0);
        backgroundImage.setFitWidth(image.getWidth());
        double width = (image.getWidth()*800)/image.getHeight()-1;
        pane.setPrefWidth(width);
        text.setLayoutX(width/3);
        text.setTextFill(colors.get(pic-1));
        imageView1.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/book_W_K.png"))));
        imageView2.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/book_B_k.png"))));
        imageView3.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/book_W_B_K.png"))));
        imageView1.setLayoutX(Math.floor(width/5));
        imageView2.setLayoutX(Math.floor(width/5*2));
        imageView3.setLayoutX(Math.floor(width/5*3));

        String localip = "";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            localip = socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        final String ip = localip;

        imageView1.setOnAction(e -> {
            imageView1.setStyle("-fx-background-color: #829769");
            imageView2.setStyle("-fx-background-color: white");
            imageView3.setStyle("-fx-background-color: white");
            text.setText("You are hosting on:"+"\n   "+ip);
        });
        imageView2.setOnAction(e -> {
            imageView2.setStyle("-fx-background-color: #829769");
            imageView1.setStyle("-fx-background-color: white");
            imageView3.setStyle("-fx-background-color: white");
            text.setText("You are hosting on:"+"\n   "+ip);
        });
        imageView3.setOnAction(e -> {
            imageView3.setStyle("-fx-background-color: #829769");
            imageView1.setStyle("-fx-background-color: white");
            imageView2.setStyle("-fx-background-color: white");
            text.setText("You are hosting on:"+"\n   "+ip);
        });
    }

    public void mouseEnteredHandler(MouseEvent e) {
        Button clickedButton = (Button) e.getSource();
        if (!clickedButton.getStyle().contains("#829769"))
            clickedButton.setStyle("-fx-background-color: #e1e1e1");
    }

    public void mouseExitedHandler(MouseEvent e) {
        Button clickedButton = (Button) e.getSource();
        if (!clickedButton.getStyle().contains("#829769"))
            clickedButton.setStyle("-fx-background-color: white");
    }
}
