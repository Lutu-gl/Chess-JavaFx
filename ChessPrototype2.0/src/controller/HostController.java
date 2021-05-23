package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Chessboard;
import model.Server;
import view.Main;
import view.MainGame;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * StartController only for the host menu.
 * It lets the player choose the playing color and hosts the game on the local IPv4 address and port 50000
 */
public class HostController {
    @FXML
    public Label text;
    public ImageView backgroundImage;
    public Button imageView1, imageView2, imageView3;
    public AnchorPane pane;

    /**
     * Initialize method, which sets a random background picture and places the buttons and labels on the correct spot.
     * A web socket gets created and Google DNS (8.8.8.8) is getting pinged to find out the local IP address.
     * Only one (image)button can be selected at the same moment.
     */
    @FXML
    public void initialize() {
        int pic = (int) (Math.floor(Math.random()*5)) + 1;
        //System.out.println(pic);
        Image image = new Image(Main.class.getResourceAsStream("/wallpaper"+pic+".jpg"));
        backgroundImage.setImage(image);
        backgroundImage.setFitHeight(800.0);
        backgroundImage.setFitWidth(image.getWidth());
        double width = (image.getWidth()*800)/image.getHeight()-1;
        pane.setPrefWidth(width);
        text.setLayoutX(width/3);
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
            text.setText("You are hosting on:"+"\n     "+ip);
            startHosting(0);
        });
        imageView2.setOnAction(e -> {
            imageView2.setStyle("-fx-background-color: #829769");
            imageView1.setStyle("-fx-background-color: white");
            imageView3.setStyle("-fx-background-color: white");
            text.setText("You are hosting on:"+"\n     "+ip);
            startHosting(1);
        });
        imageView3.setOnAction(e -> {
            imageView3.setStyle("-fx-background-color: #829769");
            imageView1.setStyle("-fx-background-color: white");
            imageView2.setStyle("-fx-background-color: white");
            text.setText("You are hosting on:"+"\n     "+ip);
            startHosting((int)(Math.floor(Math.random()*2)));
        });
    }

    /**
     * Changes the background color of a button if you hover over it to highlight it, except when it is already selected.
     * @param e onHover MouseEvent
     */
    public void mouseEnteredHandler(MouseEvent e) {
        Button clickedButton = (Button) e.getSource();
        if (!clickedButton.getStyle().contains("#829769"))
            clickedButton.setStyle("-fx-background-color: #e1e1e1");
    }

    /**
     * Changes the background color of a button if you stop hovering over it to unhighlight it, except when it is already selected.
     * @param e
     */
    public void mouseExitedHandler(MouseEvent e) {
        Button clickedButton = (Button) e.getSource();
        if (!clickedButton.getStyle().contains("#829769"))
            clickedButton.setStyle("-fx-background-color: white");
    }

    private Timeline timeline;

    /**
     * Starts hosting the game on local IPv4 address and port 5000.
     * A Timeline thread is started for observing if a player has connected.
     * Frequency of observation is one second.
     * After a player connected, it sets game parameters for example time and color and sends it to the connected player
     * @param color
     */
    private static int color;
    private boolean alreadyHosting = false;
    private void startHosting(int color) {

        HostController.color = color;
        if (alreadyHosting) return;
        alreadyHosting = true;
        Server.startHosting();
        // Observer
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (Chessboard.getInstance().isPlayerConnected()) {
                    //System.out.println("Change Scene!");
                    if (HostController.color == 0) {
                        MainGame.invertBoard = false;
                        MainGame.whiteAi = false;
                        MainGame.blackAi = true;
                    } else {
                        MainGame.invertBoard = true;
                        MainGame.whiteAi = true;
                        MainGame.blackAi = false;
                    }
                    try {
                        Server.getOutputStream().writeUTF(HostController.color==0?"white":"black");
                        Server.getOutputStream().writeUTF(""+ MainGame.timeWhite);
                        Server.getOutputStream().writeUTF(""+ MainGame.timeBlack);
                        Server.getOutputStream().writeUTF(""+ MainGame.inkrementWhite);
                        Server.getOutputStream().writeUTF(""+ MainGame.inkrementBlack);
                    } catch (IOException e) {
                        e.printStackTrace();
                        //System.out.println("Es gab Probleme beim schicken an den Server!");
                    }
                    MainGame.startGame();
                    timeline.stop();
                } else {
                    //System.out.println("Waiting for player to connect!");
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
