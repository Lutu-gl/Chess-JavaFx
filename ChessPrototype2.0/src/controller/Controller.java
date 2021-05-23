
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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


    private FieldLabel sourcePreMove = null;
    private FieldLabel targetPreMove = null;
    private boolean isPremove = false;
    private boolean flippedTimer = false;

    Chessboard chessboard = Chessboard.getInstance();
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
        //System.out.println("Gamephase: " + chessboard.getGamephase());

        // This is just testing to start the AI
//        if (chessboard.getPlaysAI()[0]&&chessboard.getTurns().size() == 0){
//            chessboard.endTurn();
//        }

        if (chessboard.getPlaysAI()[0]&&chessboard.getTurns().size() == 0)
            chessboard.endTurn();

        if (chessboard.getPlaysAI()[0]&&chessboard.getPlaysAI()[1]){
            return;
        }


        if(chessboard.AIThinking){
            FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();

            if(sourcePreMove != null && targetPreMove != null){
                if(targetPreMove == clickedFieldLabel){
                    unSelectLabelPremove();
                    return;
                }
                unSelectLabelPremove();
            }

            if(sourcePreMove == null){
                //Schauen ob Piece oben ist.
                if(clickedFieldLabel.getGraphic() == null) return;
                sourcePreMove = clickedFieldLabel;
                clickedFieldLabel.select();
            } else {
                if(targetPreMove != null){
                    unSelectLabelPremove();
                    return;
                }
                targetPreMove = clickedFieldLabel;

                if(targetPreMove == sourcePreMove){
                    unSelectLabelPremove();
                    return;
                }

                targetPreMove.selectPremoveTarget();
                sourcePreMove.selectPremoveSource();
                isPremove = true;
            }

            return;
        }else{
            if(sourcePreMove != null || targetPreMove != null){
                unSelectLabel();
            }
        }

        if (!chessboard.getGamestate().equals(Gamestate.PLAYING) ||
                (chessboard.getColorToMove().equals(Color.WHITE) && chessboard.getPlaysAI()[0]) ||
                (chessboard.getColorToMove().equals(Color.BLACK) && chessboard.getPlaysAI()[1])) return;

        FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();
        Field clickedField = chessboard.getFields()[clickedFieldLabel.getLine()][clickedFieldLabel.getColumn()];
        if(source == null)
        {
            if (!clickedField.hasPiece() || !clickedField.getPiece().getColor().equals(chessboard.getColorToMove())) return;
            source = clickedFieldLabel;
            selectLabel(source);
            markAvailableMoves(chessboard.getFields()[source.getLine()][source.getColumn()], source);
        }
        else {
            if (clickedField.hasPiece() && clickedField.getPiece().getColor().equals(chessboard.getColorToMove())) {
                source.unselect();
                unmarkAvailableMoves();
                if (clickedFieldLabel != source) {
                    source = clickedFieldLabel;
                    selectLabel(source);
                    markAvailableMoves(chessboard.getFields()[source.getLine()][source.getColumn()], source);
                } else { //Wenns das selbe Piece ist, dann wird source auf null gesetzt!
                    source = null;
                }
                return;
            }
            target = clickedFieldLabel;
            for (int i = 0; i < highlighted.size(); i++) {
                if (highlighted.get(i).getLine() == target.getLine() && highlighted.get(i).getColumn() == target.getColumn()) {
                    Turn turn = new Turn(chessboard.getFields()[source.getLine()][source.getColumn()], chessboard.getFields()[target.getLine()][target.getColumn()]);
                    unSelectLabel();
                    turn.setBlackTime(chessboard.getBlackTime());
                    turn.setWhiteTime(chessboard.getWhiteTime());
                    System.out.println("setting white time");
                    source = null;

                    unmarkAvailableMoves();
                    chessboard.handleTurn(turn);
                    break;
                }
            }
        }
    }
    public void handlePremove(){    //wird von endTurn aufgerufen wenn es einen Premove gibt!

        //if(!isPremove) return;
        if(sourcePreMove == null || targetPreMove== null){
            unSelectLabelPremove();
            return;
        }
        Field clickedFieldSource = chessboard.getFields()[sourcePreMove.getLine()][sourcePreMove.getColumn()];
        Field clickedFieldTarget = chessboard.getFields()[targetPreMove.getLine()][targetPreMove.getColumn()];

        if (!clickedFieldSource.hasPiece() || !clickedFieldSource.getPiece().getColor().equals(chessboard.getColorToMove())){
            unSelectLabelPremove();
            return;
        }
        if(clickedFieldTarget.hasPiece() && clickedFieldTarget.getPiece().getColor() == chessboard.getColorToMove()){
            unSelectLabelPremove();
            return;
        }

        if(!chessboard.isLegal(clickedFieldTarget, clickedFieldSource, chessboard.getColorToMove())){
            unSelectLabelPremove();
            return;
        }
        for(Field f : clickedFieldSource.getPiece().getMoves()){
            if(f == clickedFieldTarget){
                Turn turn = new Turn(chessboard.getFields()[sourcePreMove.getLine()][sourcePreMove.getColumn()], chessboard.getFields()[targetPreMove.getLine()][targetPreMove.getColumn()]);
                turn.setWhiteTime(chessboard.getWhiteTime());
                turn.setBlackTime(chessboard.getBlackTime());
                chessboard.handleTurn(turn);
            }
        }
        unSelectLabelPremove();
    }

    public void markAvailableMoves(Field f, FieldLabel source) {
        for (Field d : f.getPiece().getMoves())
        {

            if (!chessboard.isLegal(d, chessboard.getFields()[source.getLine()][source.getColumn()], chessboard.getColorToMove())) continue;


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
        if (chessboard.getTurns().size() == 0)
            return;

        if(lastLabelSource != null) lastLabelSource.getStyleClass().remove("lastPlayedFieldWhite");
        if(lastLabelSource != null) lastLabelSource.getStyleClass().remove("lastPlayedFieldBlack");
        if(lastLabelTarget != null) lastLabelTarget.getStyleClass().remove("lastPlayedFieldWhite");
        if(lastLabelTarget != null) lastLabelTarget.getStyleClass().remove("lastPlayedFieldBlack");


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

    public void selectLabelPremove(FieldLabel f) {
        f.select();
        selectedLabel = f;
    }

    public void unSelectLastPlayedMove(){
        if(lastLabelSource != null) lastLabelSource.getStyleClass().remove("lastPlayedFieldWhite");
        if(lastLabelSource != null) lastLabelSource.getStyleClass().remove("lastPlayedFieldBlack");
        if(lastLabelTarget != null) lastLabelTarget.getStyleClass().remove("lastPlayedFieldWhite");
        if(lastLabelTarget != null) lastLabelTarget.getStyleClass().remove("lastPlayedFieldBlack");
    }

    public void unSelectLabelPremove() {
        if (sourcePreMove == null) return;
        sourcePreMove.unselect();
        sourcePreMove = null;
        if (targetPreMove == null) return;
        targetPreMove.unselect();
        targetPreMove = null;
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
        //Date date = new Date((long)(time*1000));
        //String formattedDate = new SimpleDateFormat("HH:mm:ss.SSS").format(date);
//        System.out.println(ChessboardView.getInformationsGridpane().getChildren().get(!flippedTimer ? 1:1));
        switch(c) {
            case BLACK -> Platform.runLater(() -> ((Label)ChessboardView.getInformationsGridpane().getChildren().get(!flippedTimer ? 0:2)).setText(secondsToFormattedString(time))); // [0] == black Label [1] == scrollPane [2] == white Label //Double.toString((int)time/60) + "%.2d:" +Double.toString(time%60))
            case WHITE -> Platform.runLater(() -> ((Label)ChessboardView.getInformationsGridpane().getChildren().get(!flippedTimer ? 2:1)).setText(secondsToFormattedString(time)));
        }
    }

    public String secondsToFormattedString(double time){
        int hours = (int)time / 3600;
        int minutes = (int)(time % 3600) / 60;
        String timeString;
        if(hours != 0)
            timeString = String.format("%02d:%02d:%02d", hours, minutes, (int)time % 60);
        else if(minutes != 0)
            timeString = String.format("%02d:%02d", minutes, (int)(time % 60));
        else
            timeString = String.format("%.3f", time % 60);

        return timeString;
    }

    /**
     * Used for writing into the Textarea next to the Chessboard.
     * should be used to write the move that has just been played in PGN notation into the TextArea
     * makes a newLine in TextArea after a full turn
     * @param s String to write into TextArea
     */
    public void addPgnToDisplay(String s){
        VBox vb = ChessboardView.getMovesVBox();
        TextArea t = (TextArea) vb.getChildren().get(1);
        if(chessboard.getColorToMove() == Color.WHITE){
            t.appendText(s + "\n");
        }
        else
            t.appendText(s);
        //Platform.runLater(() -> (TextField)(ChessboardView.getMovesVBox().getChildren().get(ChessboardView.getMovesVBox().getChildren().size()-1)));

        //Platform.runLater(() -> ChessboardView.getMovesVBox().getChildren().add(new Text(s)));
        //t.setScrollTop(Double.MIN_VALUE);
    }
    /**
     * Removes num entries from the Textarea
     */
    public void removePGNFromTextArea(int num) throws ArrayIndexOutOfBoundsException{
        VBox vb = ChessboardView.getMovesVBox();
        TextArea t = (TextArea) vb.getChildren().get(1);
        String text = t.getText();
        //System.out.println("L:"+text.length());
        //System.out.println("TEXT BEFORE:\"" + text + "\"");
        for (int i = text.length()-1; num+1 != 0; i--)
        {
            //System.out.println("i:"+i);
            if(text.charAt(i) == '\n' || text.charAt(i) == ' ' || i == 0){
                //System.out.println("in if with:"+i);
                num--;
                if(i != 0)
                    text = text.substring(0, i+1);
                else
                    text = text.substring(0, i);

            }
        }
        //System.out.println("TEXT AFTER:\""+text+"\"");
        t.clear();
        t.appendText(text); //using append instead of set to trigger Listener
    }

    public void flipTimers(){
        // [0] == black Label [1] == scrollPane [2] == white Label
        flippedTimer=true;
        Label l  = (Label) ChessboardView.getInformationsGridpane().getChildren().get(0);
        Label l2 = (Label) ChessboardView.getInformationsGridpane().getChildren().get(2);
        ChessboardView.getInformationsGridpane().getChildren().remove(ChessboardView.getInformationsGridpane().getChildren().get(2));
        ChessboardView.getInformationsGridpane().getChildren().remove(ChessboardView.getInformationsGridpane().getChildren().get(0));
//        ChessboardView.getInformationsGridpane().getChildren().set(0, l2);
        ChessboardView.getInformationsGridpane().add(l2, 0, 0);
        ChessboardView.getInformationsGridpane().add(l,0,2);
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

    public boolean isPremove() {
        return isPremove;
    }

    public void setPremove(boolean premove) {
        isPremove = premove;
    }
    public FieldLabel getSourcePreMove() {
        return sourcePreMove;
    }

    public void setSourcePreMove(FieldLabel sourcePreMove) {
        this.sourcePreMove = sourcePreMove;
    }

    public FieldLabel getTargetPreMove() {
        return targetPreMove;
    }

    public void setTargetPreMove(FieldLabel targetPreMove) {
        this.targetPreMove = targetPreMove;
    }

    public boolean isFlippedTimer() {
        return flippedTimer;
    }

    public void setFlippedTimer(boolean flippedTimer) {
        this.flippedTimer = flippedTimer;
    }
}