package Chess;

import Pieces.Piece;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class LabelEvent implements EventHandler<MouseEvent> {
    static Piece p = null;
    static FieldLabel eventSource = null;
    static FieldLabel eventTarget = null;
    @Override
    public void handle(MouseEvent event) {
        if(eventSource == null)
        {
            eventSource = (FieldLabel) event.getSource();
            if(eventSource.getPiece() != null)
            {
                p = eventSource.getPiece();
                System.out.println(p.toString());
            }
            else{
                System.out.println("is empty");
                eventSource = null;
            }
        }
        else{
            eventTarget = (FieldLabel) event.getSource();
            if(eventTarget.hashCode() != eventSource.hashCode()){
                eventTarget.setPiece(p);
                eventSource.removePiece();
                eventSource = null;
                eventTarget = null;
                p = null;
            }
            else{
                eventTarget = null;
                eventSource = null;
            }
        }

    }
}
