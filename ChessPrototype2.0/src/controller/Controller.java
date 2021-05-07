package controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.*;
import model.pieces.Pawn;
import model.pieces.Piece;
import view.ChessboardView;
import view.FieldLabel;

import java.util.ArrayList;


/**
 * Controller to make model Classes and view Classes communicate
 * is singleton
 * implements EvenHandler<MouseEvent>
 * @since 1.0
 * @version 5.2
 */
public class Controller implements EventHandler<MouseEvent>{

    private FieldLabel source=null;
    private FieldLabel target=null;
    private ArrayList<FieldLabel> highlighted = new ArrayList<>();
    private FieldLabel selectedLabel=null;
    private FieldLabel checkLabel=null;
    private static Controller instance=null;

    private FieldLabel lastLabelSource;
    private FieldLabel lastLabelTarget;

    private Controller(){

    }

    public static Controller getInstance(){
        if(instance == null) instance = new Controller();
        return instance;
    }


    /**
     * gets called after clicking a FieldLabel on view.ChessboardView
     * creates Turn Object to send to model
     */
    @Override
    public void handle(MouseEvent mouseEvent) {
        System.out.println("Gamephase: " + Chessboard.getInstance().getGamephase());
        Chessboard chessboard = Chessboard.getInstance();
        // This is just testing to start the AI

        if(chessboard.AIThinking){
            return;
        }

        if (chessboard.getPlaysAI()[0]&&chessboard.getPlaysAI()[1])
           chessboard.endTurn();

        if (!chessboard.getGamestate().equals(Gamestate.PLAYING) ||
           (chessboard.getColorToMove().equals(Color.WHITE) && chessboard.getPlaysAI()[0]) ||
           (chessboard.getColorToMove().equals(Color.BLACK) && chessboard.getPlaysAI()[1])) return;

        FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();
        Field clickedField = Chessboard.getInstance().getFields()[clickedFieldLabel.getLine()][clickedFieldLabel.getColumn()];
        if(source == null)
        {
            if (!clickedField.hasPiece() || !clickedField.getPiece().getColor().equals(Chessboard.getInstance().getColorToMove())) return;
            source = clickedFieldLabel;
            selectLabel(source);
            markAvailableMoves(Chessboard.getInstance().getFields()[source.getLine()][source.getColumn()]);
        }
        else {
            if (clickedField.hasPiece() && clickedField.getPiece().getColor().equals(Chessboard.getInstance().getColorToMove())) {
                source.unselect();
                unmarkAvailableMoves();
                if (clickedFieldLabel != source) {
                    source = clickedFieldLabel;
                    selectLabel(source);
                    markAvailableMoves(Chessboard.getInstance().getFields()[source.getLine()][source.getColumn()]);
                } else { //Wenns das selbe Piece ist, dann wird source auf null gesetzt!
                    source = null;
                }
                return;
            }
            target = clickedFieldLabel;
            for (int i = 0; i < highlighted.size(); i++) {
                if (highlighted.get(i).getLine() == target.getLine() && highlighted.get(i).getColumn() == target.getColumn()) {
                    Turn turn = new Turn(Chessboard.getInstance().getFields()[source.getLine()][source.getColumn()], Chessboard.getInstance().getFields()[target.getLine()][target.getColumn()]);
                    unSelectLabel();


                    source = null;

                    unmarkAvailableMoves();
                    Chessboard.getInstance().handleTurn(turn);
                    break;
                }
            }
        }
    }

    /**
     * @param f marks/outlines every move for Piece on Field f, adds field to ArrayList highlighted
     */
    public void markAvailableMoves(Field f) {
        for (Field d : f.getPiece().getMoves())
        {

            if (!Chessboard.getInstance().isLegal(d, Chessboard.getInstance().getFields()[source.getLine()][source.getColumn()], Chessboard.getInstance().getColorToMove())) continue;


            if(!d.hasPiece()){
                ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()).mark();
            }
            else{
                ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()).outline();
            }
            highlighted.add(ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()));
        }
    }

    public void markLastPlayedMove(){
        if (Chessboard.getInstance().getTurns().size() == 0)
            return;

        if(lastLabelSource != null) lastLabelSource.getStyleClass().remove("lastPlayedFieldWhite");
        if(lastLabelSource != null) lastLabelSource.getStyleClass().remove("lastPlayedFieldBlack");
        if(lastLabelTarget != null) lastLabelTarget.getStyleClass().remove("lastPlayedFieldWhite");
        if(lastLabelTarget != null) lastLabelTarget.getStyleClass().remove("lastPlayedFieldBlack");

        Chessboard chessboard = Chessboard.getInstance();
        ArrayList<Turn> turns = chessboard.getTurns();
        Turn lastTurn = turns.get(turns.size()-1);

        lastLabelSource = fieldToFieldLabel(lastTurn.getSourceField());
        lastLabelTarget = fieldToFieldLabel(lastTurn.getTargetField());

        if((lastLabelSource.getLine() + lastLabelSource.getColumn()) % 2 == 0){
            lastLabelSource.getStyleClass().add("lastPlayedFieldWhite");
        }else{
            lastLabelSource.getStyleClass().add("lastPlayedFieldBlack");
        }
        if((lastLabelTarget.getLine() + lastLabelTarget.getColumn()) % 2 == 0){
            lastLabelTarget.getStyleClass().add("lastPlayedFieldWhite");
        }else{
            lastLabelTarget.getStyleClass().add("lastPlayedFieldBlack");
        }
    }

    /**
     * Removes every outline/mark for each FieldLabel in ArrayList highlighted, clears it afterwards
     */
    public void unmarkAvailableMoves() {
        if (highlighted == null) return;
        highlighted.forEach(fieldLabel -> {
            if(fieldLabel.isMarked())
                fieldLabel.unmark();
            else
                fieldLabel.removeOutline();
        });
        highlighted.clear();
    }

    /**
     * @param f tells view.ChessboardView which field to mark as selected, sets selectedLabel to f
     */
    public void selectLabel(FieldLabel f) {
        f.select();
        selectedLabel = f;
    }

    /**
     * tells view.ChessboardView to unmark selectedLabel as selected, sets selectedLabel to null
     */
    public void unSelectLabel() {
        if (selectedLabel == null) return;
        selectedLabel.unselect();
        selectedLabel = null;
    }

    /**
     * converts model.Field to its corresponding view.FieldLabel
     * @param f Field to convert
     * @return can return null if there is no matching FieldLabel
     */
    public FieldLabel fieldToFieldLabel(Field f){
        return ChessboardView.getBoard().get(f.getLine()).get(f.getColumn());
    }

    /**
     * @param f tells view.ChessboardView which FieldLabel to mark, sets checkLabel to f
     */
    public void markCheck(FieldLabel f){
        checkLabel = f;
        f.markAsCheck();
    }

    /**
     * tells view.ChessboardView to unmark checkLabel, sets checkLabel to null
     */
    public void unmarkCheck(){
        if(checkLabel==null) return;
        checkLabel.unmarkAsCheck();
        checkLabel = null;
    }

    /**
     * Updates the time on view.ChessboardView for color c
     * @param time sets the Time to this value using Platform.runLater()
     * @param c color of Player to set the time for
     */
    public void updateTime(double time, Color c){
        //System.out.println("Time "+ time + c);
        switch(c){
            case BLACK -> Platform.runLater(() -> ((Label)ChessboardView.getTimerVBox().getChildren().get(0)).setText(Double.toString(time))); // [0] == black Label [1] == scrollPane [2] == white Label
            case WHITE -> Platform.runLater(() -> ((Label)ChessboardView.getTimerVBox().getChildren().get(2)).setText(Double.toString(time)));
        }

    }

    /**
     * Used for writing into the Textarea next to the Chessboard.
     * should be used to write the move that has just been played in PGN notation into the TextArea
     * makes a newLine in TextArea after a full turn
     * @param s String to write into TextArea
     */
    public void addMoveToDisplay(String s){
        VBox vb = ChessboardView.getMovesVBox();
        TextArea t = (TextArea) vb.getChildren().get(0);
        if(Chessboard.getInstance().getColorToMove() == Color.WHITE){
            t.appendText(s + "\n");
        }
        else
            t.appendText(s);
            //Platform.runLater(() -> (TextField)(ChessboardView.getMovesVBox().getChildren().get(ChessboardView.getMovesVBox().getChildren().size()-1)));

        //Platform.runLater(() -> ChessboardView.getMovesVBox().getChildren().add(new Text(s)));
    }

    public void setSource(FieldLabel source) {
        this.source = source;
    }

    public void setTarget(FieldLabel target) {
        this.target = target;
    }

    public ArrayList<FieldLabel> getHighlighted() {
        return highlighted;
    }

    public FieldLabel getSource() {
        return source;
    }

    public FieldLabel getTarget() {
        return target;
    }

    /**
     * Displays Dialog for promoting Pawn to a different Piece
     * only works for Human players
     * @param pawn pawn to be Promoted
     * @return Piece player selected for the Pawn to be Promoted to
     */
    public Piece promotionDialog(Pawn pawn) {
        return new PromotionDialog(pawn).getResult();
    }
}
