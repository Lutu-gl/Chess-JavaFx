package controller;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Color;
import model.pieces.*;
import view.ChessboardView;
import view.FieldLabel;
import view.Main;



/**
 * Used for Promoting a Pawn
 */
public class PromotionDialog extends Pane {

    private Piece retPiece = null;

    /**
     * constructor sets up Dialog for pawn p
     * moves it on top of pawn
     * @param p pawn to be Promoted
     */
    public PromotionDialog(Pawn p)
    {
        ImageView[] imgs = new ImageView[4];
        char[] chars = {'q','r','b','n'};
        int offsetY; //so dialog moves up when at the bottom of the Board
        Label qLabel = new Label();
        Label rLabel = new Label();
        Label nLabel = new Label();
        Label bLabel = new Label();
        if(p.getColor() == Color.BLACK) {
            for (int i = 0; i < 4; i++)
            {
                imgs[i] = new ImageView(new Image(Main.class.getResourceAsStream("/B_" + chars[i] + ".png")));
                imgs[i].setFitWidth(100);
                imgs[i].setFitHeight(100);
            }
            offsetY = 300;
        }
        else {
            for (int i = 0; i < 4; i++)
            {
                imgs[i] = new ImageView(new Image(Main.class.getResourceAsStream("/W_" + Character.toUpperCase(chars[i]) + ".png")));
                imgs[i].setFitWidth(100);
                imgs[i].setFitHeight(100);
            }
            offsetY = 0;
        }
        qLabel.setGraphic(imgs[0]);
        rLabel.setGraphic(imgs[1]);
        nLabel.setGraphic(imgs[3]);
        bLabel.setGraphic(imgs[2]);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(qLabel, rLabel, nLabel, bLabel);
        Stage stage = new Stage();
        stage.setScene(new Scene(vBox, 100,400));
        stage.setOpacity(0.6);

        //Removes the Minimize, Maximize and Close button from the Window
        stage.initStyle(StageStyle.UNDECORATED);

        //Gets the Position of the Pawn in relation to the Screen and moves the Window to there
        FieldLabel f = Controller.getInstance().fieldToFieldLabel(p.getField());
        Bounds boundsInScene = f.localToScreen(f.getBoundsInLocal());
        stage.setX(boundsInScene.getMaxX()-100);
        stage.setY(boundsInScene.getMinY()-offsetY);

        String colorString = p.getColor().toString().charAt(0) + p.getColor().toString().substring(1).toLowerCase();

        //making the labels set the Return string and then closing the stage when clicked
        qLabel.setOnMouseClicked(mouseEvent -> {retPiece = new Queen(p.getColor(), colorString + " Queen", p.getField()); stage.close();});
        rLabel.setOnMouseClicked(mouseEvent -> {retPiece = new Rook(p.getColor(), colorString + " Rook", p.getField()); stage.close();});
        nLabel.setOnMouseClicked(mouseEvent -> {retPiece = new Knight(p.getColor(), colorString + "Knight", p.getField()); stage.close();});
        bLabel.setOnMouseClicked(mouseEvent -> {retPiece = new Bishop(p.getColor(), colorString + "Bishop", p.getField()); stage.close();});

        //Forcing the stage to be on top and removing the ability to interact with the other JavaFx Windows
        stage.setAlwaysOnTop(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    public Piece getResult() {
        return retPiece;
    }

}

//Focus listener Code
/*
TextField yourTextField = new TextField();
yourTextField.focusedProperty().addListener(new ChangeListener<Boolean>()
{
    @Override
    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
    {
        if (newPropertyValue)
        {
            System.out.println("Textfield on focus");
        }
        else
        {
            System.out.println("Textfield out focus");
        }
    }
});
 */
/*
public PromotionDialog(Pawn pawn) {
        String colorName = pawn.getColor().toString().charAt(0) + pawn.getColor().toString().substring(1).toLowerCase();
        ImageView qImg = null,rImg = null,kImg = null,bImg = null;
        Label qLabel = new Label(), rLabel = new Label(), kLabel = new Label(), bLabel = new Label();
        if(pawn.getColor() == Color.BLACK){
            qLabel.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/B_q.png"))));
            rLabel.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/B_r.png"))));
            kLabel.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/B_n.png"))));
            bLabel.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/B_b.png"))));

            qLabel.setText("White Queen");
                    qLabel.setText("White Queen");
                    qLabel.setText("White Queen");
                    qLabel.setText("White Queen");

                    }
                    else{
                    qLabel.setText("queen");
            qLabel.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/W_Q.png"))));
            rLabel.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/W_R.png"))));
            kLabel.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/W_N.png"))));
            bLabel.setGraphic(new ImageView(new Image(Main.class.getResourceAsStream("/W_B.png"))));

                    }
                    //Queen q = new Queen(pawn.getColor(), colorName + " Queen", pawn.getField(), 9, pawn.getColor() == Color.BLACK ? 'q' : 'Q');
                    //Bishop b = new Bishop(pawn.getColor(), colorName + " Bishop", pawn.getField(), 3, pawn.getColor() == Color.BLACK ? 'b' : 'B');
                    //Knight k = new Knight(pawn.getColor(), colorName + " Knight", pawn.getField(), 3, pawn.getColor() == Color.BLACK ? 'n' : 'N');
                    //Rook r = new Rook(pawn.getColor(), colorName + " Rook", pawn.getField(), 5, pawn.getColor() == Color.BLACK ? 'r' : 'R');

                    this.setHeaderText("Promotion");
                    this.setContentText("Promote your Pawn");
                    //this.setSelectedItem(qImg);
                    this.setGraphic(null);
                    //this.getItems().addAll(qLabel, rLabel, bLabel, kLabel);
                    }
 */


//Keypress listener Code(Funkt nt)
        /*
        qLabel.setOnKeyPressed(keyEvent -> {
            System.out.println(keyEvent.getCode());
            System.out.println("hi");
            if(keyEvent.getCode().equals(KeyCode.ENTER)){
                new Queen(p.getColor(), colorString + " Queen", p.getField());
                stage.close();
            }
        });
         */