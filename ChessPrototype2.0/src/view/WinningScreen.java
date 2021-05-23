package view;

import controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Chessboard;
import model.Gamestate;
import model.ai.Gamephase;

/**
 * This class is displaying the winning/draw screens
 */
public class WinningScreen implements Runnable{

    private static Timeline timeline;

    /**
     * Displays white winning screen
     */
    public static void whiteWins() {
        showWinningDialog("White wins!", "whiteWins.mp4", "W_K.png");
    }

    /**
     * Displays black winning screen
     */
    public static void blackWins() {
        showWinningDialog("Black wins!", "blackWins.mp4", "B_k.png");
    }

    /**
     * Helper function to create a winning screen
     * @param title Title of dialog
     * @param video Video which is played in the background
     * @param k Name of picture of king to use
     */
    private static void showWinningDialog(String title, String video, String k) {
        Dialog<ImageView> dialog = new Dialog<>();
        dialog.setTitle(title);
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(type);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Main.class.getResource("/icon.png").toString()));

        MediaView mediaView = new MediaView();
        mediaView.setFitWidth(490);
        mediaView.setFitHeight(576);
        dialog.getDialogPane().setPrefWidth(490);
        dialog.getDialogPane().setPrefHeight(576);

        dialog.getDialogPane().getChildren().add(mediaView);

        Media media = new Media(Main.class.getResource(video).toExternalForm());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(-1);
        mediaPlayer.setAutoPlay(true);


        ImageView king = new ImageView(new Image(Main.class.getResourceAsStream("/"+ MainLaxe.pieceDesign.getDesign()+k)));
        dialog.getDialogPane().getChildren().add(king);
        king.setFitHeight(100);
        king.setFitWidth(100);
        king.setLayoutX(200);
        king.setLayoutY(350);

        ImageView crown = new ImageView(new Image(Main.class.getResourceAsStream("/crown.png")));
        dialog.getDialogPane().getChildren().add(crown);
        crown.setFitWidth(100);
        crown.setFitHeight(100);
        crown.setLayoutX(200);
        crown.setLayoutY(100);

        dialog.setOnCloseRequest(dialogEvent -> {Platform.exit(); System.exit(0);});

        final double[] y = {100};
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.02), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                y[0] += 0.5;
                crown.setLayoutY(y[0]);
                if (y[0] == 250)
                    timeline.stop();
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();

        dialog.show();
        //mediaPlayer.play();
    }

    public static void draw() {
        Dialog<ImageView> dialog = new Dialog<>();
        dialog.setTitle("Draw");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(type);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Main.class.getResource("/icon.png").toString()));

        ImageView imageView = new ImageView();
        imageView.setImage(new Image(Main.class.getResource("/draw.jpeg").toString()));
        imageView.setFitWidth(490);
        imageView.setFitHeight(576);
        dialog.getDialogPane().setPrefWidth(490);
        dialog.getDialogPane().setPrefHeight(576);
        dialog.getDialogPane().getChildren().add(imageView);
        dialog.show();
    }

    @Override
    public void run() {
        Gamestate gamestate = Chessboard.getInstance().getGamestate();
        if (gamestate != Gamestate.PLAYING) {
            if (gamestate == Gamestate.WHITE_WINS || gamestate == Gamestate.WHITE_WINS_ON_TIME) {
                WinningScreen.whiteWins();
            } else if (gamestate == Gamestate.BLACK_WINS || gamestate == Gamestate.BLACK_WINS_ON_TIME) {
                WinningScreen.blackWins();
            } else {
                WinningScreen.draw();
            }
        }
    }
}
