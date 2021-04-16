package controller;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import model.Chessboard;
import model.Turn;
import view.FieldLabel;

import java.util.ArrayList;

public class DragDroppedHandler implements EventHandler<DragEvent> {
    @Override
    public void handle(DragEvent dragEvent) {
        Controller controller = Controller.getInstance();
        FieldLabel targetLabel = (FieldLabel) dragEvent.getGestureTarget();
        Chessboard chessboard = Chessboard.getInstance();

        controller.setTarget(targetLabel);

        ArrayList<FieldLabel> highlighted = controller.getHighlighted();
        Turn turn = null;
        for (int j = 0; j < highlighted.size(); j++) {
            if (highlighted.get(j).getLine() == controller.getTarget().getLine() && highlighted.get(j).getColumn() == controller.getTarget().getColumn()) {
                turn = new Turn(controller.getSource(), controller.getTarget());
                break;
            }
        }

        controller.unmarkAvailableMoves();
        controller.unSelectLabel();
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
        if (turn == null) return;
        chessboard.handleTurn(turn);
    }
}