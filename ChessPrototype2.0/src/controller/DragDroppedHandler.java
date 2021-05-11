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


        FieldLabel sourcePreMove = controller.getSourcePreMove();
        FieldLabel targetPreMove = controller.getTargetPreMove();
        if(chessboard.AIThinking){
            System.out.println("Jetzt drag dropped");
            if(targetPreMove == null) {
                controller.setTargetPreMove(targetLabel);
                if(controller.getSourcePreMove() == controller.getTargetPreMove()){
                    controller.unSelectLabelPremove();
                    return;
                }
                targetLabel.selectPremoveTarget();
                controller.setPremove(true);
            }

            return;
        }


        controller.setTarget(targetLabel);

        ArrayList<FieldLabel> highlighted = controller.getHighlighted();
        Turn turn = null;
        for (int j = 0; j < highlighted.size(); j++) {
            if (highlighted.get(j).getLine() == controller.getTarget().getLine() && highlighted.get(j).getColumn() == controller.getTarget().getColumn()) {

                turn = new Turn(Chessboard.getInstance().getFields()[controller.getSource().getLine()][controller.getSource().getColumn()], Chessboard.getInstance().getFields()[controller.getTarget().getLine()][controller.getTarget().getColumn()]);

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
