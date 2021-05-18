package controller;

import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import model.Chessboard;
import model.Field;
import model.Gamestate;
import view.FieldLabel;
import view.Main;

public class DragDetectedHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent mouseEvent) {
        Chessboard chessboard = Chessboard.getInstance();
        Controller controller = Controller.getInstance();

        FieldLabel sourcePreMove = controller.getSourcePreMove();
        FieldLabel targetPreMove = controller.getTargetPreMove();

        if (chessboard.getPlaysAI()[0]&&chessboard.getPlaysAI()[1]){
            return;
        }

        if(chessboard.AIThinking) {
            FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();
            Field clickedField = chessboard.getFields()[clickedFieldLabel.getLine()][clickedFieldLabel.getColumn()];

            controller.unSelectLabelPremove();


            if(clickedFieldLabel.getGraphic() == null) return;
            controller.setSourcePreMove(clickedFieldLabel);
            clickedFieldLabel.select();
            Dragboard db = ((FieldLabel) mouseEvent.getSource()).startDragAndDrop(TransferMode.ANY);

            // Set the drag view to the image of the piece dragged

            Image image = ((ImageView)clickedFieldLabel.getGraphic()).getImage();
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            ImageView imageView= new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            db.setDragView(imageView.snapshot(parameters, null), imageView.getFitHeight()/2, imageView.getFitHeight()/2);

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString("Drag event");
            db.setContent(content);
            mouseEvent.consume();

            return;
        }
/*
        if (!chessboard.getGamestate().equals(Gamestate.PLAYING) ||
           (chessboard.getColorToMove().equals(Color.WHITE) && chessboard.getPlaysAI()[0]) ||
           (chessboard.getColorToMove().equals(Color.BLACK) && chessboard.getPlaysAI()[1])) return;
 */
        FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();
        Field clickedField = chessboard.getFields()[clickedFieldLabel.getLine()][clickedFieldLabel.getColumn()];

        // Check if dragged piece belongs to the player
        if (clickedFieldLabel.getGraphic() != null && clickedField.getPiece().getColor().equals(chessboard.getColorToMove())) {
            // Unselect moves if a other piece was CLICKED (not dragged) before
            Controller.getInstance().unSelectLabel();
            Controller.getInstance().unmarkAvailableMoves();

            // Select the dragged field and mark available moves for it
            Controller.getInstance().setSource(clickedFieldLabel);
            Controller.getInstance().selectLabel(clickedFieldLabel);
            Controller.getInstance().markAvailableMoves(clickedField, clickedFieldLabel);

            // Create a Dragboard
            Dragboard db = ((FieldLabel) mouseEvent.getSource()).startDragAndDrop(TransferMode.ANY);

            // Set the drag view to the image of the piece dragged
            //Image image = new Image(Main.class.getResourceAsStream("/"+(clickedField.getPiece().getColor().equals(Color.WHITE)?"W":"B")+"_"+clickedField.getPiece().getShortName()+".png"));
            Image image = ((ImageView)clickedFieldLabel.getGraphic()).getImage();
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            ImageView imageView= new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            db.setDragView(imageView.snapshot(parameters, null), imageView.getFitHeight()/2, imageView.getFitHeight()/2);

            // Put a string on a dragboard
            ClipboardContent content = new ClipboardContent();
            content.putString("Drag event");
            db.setContent(content);
        }
        mouseEvent.consume();
    }
}
