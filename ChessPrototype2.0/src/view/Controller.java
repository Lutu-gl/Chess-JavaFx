package view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Chessboard;

/**
 * Controller only for handling the main menu
 * @since 1.0
 * @version 4.6
 */
public class Controller{
    @FXML
    public ImageView myImageView;
    public CheckBox checkBox1, checkBox2, checkBox3;
    public TextField time1, time2;
    public ChoiceBox<String> dropdown1, dropdown2;

    Image myImage2 = new Image(getClass().getResourceAsStream("Ebene6.jpg"));
    Image myImage = new Image(getClass().getResourceAsStream("Ebene7.jpg"));

    private ObservableList<String> dropdownList1 = FXCollections.observableArrayList("Weiß", "Schwarz", "ZFufall");
    private ObservableList<String> dropdownList2 = FXCollections.observableArrayList("Local", "Host", "Join");

    /**
     * Initialize method which sets the default selected item in the dropdown lists
     */
    public void initialize() {
        dropdown1.setItems(dropdownList1);
        dropdown2.setItems(dropdownList2);
        dropdown1.getSelectionModel().select("Weiß");
        dropdown2.getSelectionModel().select("Local");
    }


    /**
     * CheckBox handler. Only one checkbox can be marked at the same time
     * @param e ActionEvent of clicked CheckBox
     */
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
    }

    /**
     * Handler for start button. Checks if everything was selected correctly!
     */
    public void startGame() {
        // Falls kein Spielmodus ausgewählt ist
        if (!checkBox1.isSelected() && !checkBox2.isSelected() && !checkBox3.isSelected()) {
            System.out.println("Kein Spielmodus ausgewählt!");
            return;
        }
        // Wenn man einem Spiel joint, muss die Zeiteingabe nicht korrekt sein
        int intTime1 = 0, intTime2 = 0;
        if (!(checkBox2.isSelected() && dropdown2.getSelectionModel().getSelectedItem().equals("Join"))) {
            // Falls keine Zeit eingegeben wurde
            if (time1.getText().length() <= 0 || time2.getText().length() <= 0) {
                System.out.println("Keine Zeit eingegeben!");
                return;
            }

            try {
                intTime1 = Integer.parseInt(time1.getText());
                intTime2 = Integer.parseInt(time2.getText());
            } catch (NumberFormatException e) {
                System.out.println("String statt Zahl eingegeben!");
                return;
            }
        }
        System.out.println("Los geats!!!");
        int size = 8;
        // Set the size and the FEN of the logic chessboard
        Chessboard board = Chessboard.getInstance();

        // Check which gamemode has to start
        boolean whiteAi = true, blackAi = true;
        if (checkBox1.isSelected()) {
            System.out.println(dropdown1.getSelectionModel().getSelectedItem());
            if (dropdown1.getSelectionModel().getSelectedItem().equals("Weiß")) whiteAi = false;
            else if (dropdown1.getSelectionModel().getSelectedItem().equals("Schwarz")) blackAi = false;
            else {
                if (Math.round(Math.random()) == 1) whiteAi = false;
                else blackAi = false;
            }
        } else if (checkBox2.isSelected()) {
            if (dropdown2.getSelectionModel().getSelectedItem().equals("Local")) {
                whiteAi = false;
                blackAi = false;
            } else if (dropdown2.getSelectionModel().getSelectedItem().equals("Host")) {
                hostDialog();
            } else {
                joinDialog();
            }
        }

        //MainLaxe.saveParametersForGame(size, whiteAi, blackAi, intTime1, intTime2, 0, 0);
        MainLaxe.size = size;
        MainLaxe.whiteAi = whiteAi;
        MainLaxe.blackAi = blackAi;
        MainLaxe.timeWhite = intTime1;
        MainLaxe.timeBlack = intTime2;
        MainLaxe.inkrementWhite = 0;
        MainLaxe.inkrementBlack = 0;
        MainLaxe.invertBoard = !blackAi&&!checkBox2.isSelected();
        if (!checkBox2.isSelected() || (checkBox2.isSelected() && dropdown2.getSelectionModel().getSelectedItem().equals("Local")))
            MainLaxe.startGame();
    }

    /**
     * Changes to the host menu
     */
    private void hostDialog() {
        MainLaxe.changeScene(MainLaxe.hostScene);
    }

    /**
     * Changes to the join menu
     */
    private void joinDialog() {
        MainLaxe.changeScene(MainLaxe.joinScene);
    }
}