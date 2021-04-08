package controller;

import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import model.Chessboard;
import model.Color;
import model.Field;
import view.ChessboardView;
import view.FieldLabel;
import view.Main;

public class DragDetectedHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent mouseEvent) {
        FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();
        Chessboard chessboard = Chessboard.getInstance();
        Field clickedField = chessboard.getFields()[clickedFieldLabel.getLine()][clickedFieldLabel.getColumn()];
        if (clickedField.hasPiece() && clickedField.getPiece().getColor().equals(chessboard.getColorToMove())) {
            //ChessboardView.changeCursor(clickedField.getPiece());

            System.out.println("Dragged");
            Controller.getInstance().selectLabel(clickedFieldLabel);
            Controller.getInstance().markAvailableMoves(clickedField);
            Controller.getInstance().setSource(clickedFieldLabel);
            Dragboard db = ((FieldLabel) mouseEvent.getSource()).startDragAndDrop(TransferMode.ANY);

            Image image = new Image(Main.class.getResourceAsStream("/"+(clickedField.getPiece().getColor().equals(Color.WHITE)?"W":"B")+"_"+clickedField.getPiece().getShortName()+".png"));
            
            db.setDragView(image, image.getHeight()/2, image.getHeight()/2);

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString("Drag event");
            db.setContent(content);
        }
        mouseEvent.consume();
    }
}
