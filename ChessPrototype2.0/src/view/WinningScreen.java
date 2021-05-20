package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

public class WinningScreen {

    private static Timeline timeline;

    public static void whiteWins() {
        Dialog<ImageView> dialog = new Dialog<>();
        dialog.setTitle("White wins!");
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

        Media media = new Media(Main.class.getResource("whiteWins.mp4").toExternalForm());

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setCycleCount(-1);
        mediaPlayer.setAutoPlay(true);


        ImageView king = new ImageView(new Image(Main.class.getResourceAsStream("/"+ MainLaxe.pieceDesign.getDesign()+"W_K.png")));
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
}
