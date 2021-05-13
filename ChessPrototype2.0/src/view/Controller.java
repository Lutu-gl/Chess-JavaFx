package view;


import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;


public class Controller{
    @FXML
    //private MediaView mv = new MediaView();
    public ImageView myImageView;
    public CheckBox checkBox1, checkBox2, checkBox3;
    public TextField time1, time2;


    Image myImage2 = new Image(getClass().getResourceAsStream("Ebene6.jpg"));
    Image myImage = new Image(getClass().getResourceAsStream("Ebene7.jpg"));
    //String VUrl= "alphago-animation.mp4";
    //Media media =new Media(VUrl);
    //MediaPlayer mediaPlayer= new MediaPlayer(media);

   // public void initialize() {
    //    mv.setMediaPlayer(mediaPlayer);
    //    mediaPlayer.play();
   // }




    private boolean ai = true;

    public void displayImage(ActionEvent e){
        CheckBox clickedBox = (CheckBox) e.getSource();
        if (clickedBox.getText().equals("Spieler gegen Computer")) {
            myImageView.setImage(myImage);
            checkBox2.setSelected(false);
            checkBox3.setSelected(false);
            checkBox1.setSelected(true);
        } else if (clickedBox.getText().equals("Spieler gegen Spieler")) {
            myImageView.setImage(myImage2);
            checkBox1.setSelected(false);
            checkBox3.setSelected(false);
            checkBox2.setSelected(true);
        } else {
            myImageView.setImage(myImage);
            checkBox2.setSelected(false);
            checkBox1.setSelected(false);
            checkBox3.setSelected(true);
        }
        /*if (ai) {
            ai = false;
            myImageView.setImage(myImage);
        } else {
            ai = true;
            myImageView.setImage(myImage2);
        }*/

    }
    //public void video(Stage stage) throws Exception{

            //goes to user Directory
            //File f = new File(Dir, "C:\\Users\\unter\\IdeaProjects\\CHessNEw\\src\\sample\\alphago-animation.mp4");

            //Converts media to string URL
            //Media media = new Media(f.toURI().toURL().toString());
            //javafx.scene.media.MediaPlayer player = new   javafx.scene.media.MediaPlayer(media);
            //MediaView viewer = new MediaView(player);

            //change width and height to fit video
            //DoubleProperty width = viewer.fitWidthProperty();
            //DoubleProperty height = viewer.fitHeightProperty();
            //width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
            //height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
            //viewer.setPreserveRatio(true);

            //StackPane root = new StackPane();
            //root.getChildren().add(viewer);
    //}
    public void startGame() {
        // Falls kein Spielmodus ausgewählt ist
        if (!checkBox1.isSelected() && !checkBox2.isSelected() && !checkBox3.isSelected()) {
            System.out.println("Kein Spielmodus ausgewählt!");
            return;
        }

        // Falls keine Zeit eingegeben wurde
        if (time1.getText().length() <= 0 || time2.getText().length() <= 0) {
            System.out.println("Keine Zeit eingegeben!");
            return;
        }

        try {
            int intTime1 = Integer.parseInt(time1.getText());
            int intTime2 = Integer.parseInt(time2.getText());
        } catch(NumberFormatException e) {
            System.out.println("String statt Zahl eingegeben!");
            return;
        }

        System.out.println("Los geats!!!");
        Main.main(new String[]{});
    }
}