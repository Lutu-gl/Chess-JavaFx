package gameLogic;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

public class WinDialog extends Dialog<Boolean> {

    public WinDialog(String s) {
        GridPane gridPane = new GridPane();
        gridPane.setId("windialog");
        gridPane.getStylesheets().add(getClass().getResource("windialog.css").toExternalForm());

        Label label = new Label(s);
        label.setId("winlabel");
        Button quit = new Button("Quit");
        quit.setId("quitBtn");

        label.prefWidth(500);
        label.prefHeight(500);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);

        quit.setOnMouseClicked((mouseEvent -> {
            System.exit(0);
            this.setResult(true);
        }));

        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);

        gridPane.addRow(0);
        gridPane.addRow(1);
        gridPane.addColumn(0);
        gridPane.addColumn(1);
        gridPane.addColumn(2);
        gridPane.add(new Label(""),0,0);
        gridPane.add(label, 1, 0);
        gridPane.add(quit, 1, 1);
        gridPane.add(new Label(""),2,1);


        getDialogPane().setContent(gridPane);
    }

}
