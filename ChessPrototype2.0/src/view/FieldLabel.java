package view;


import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** Describes Graphic Field for view.ChessboardView, inherits from javafx.scene.control.Label;
 * @version 3.2
 * @since 1.0
 */
public class FieldLabel extends Label {

    private int line, column;
    private boolean isMarked=false, isOutlined=false, isSelected=false, isCheckLabel = false;


    public FieldLabel(){
        super();
    }
    public FieldLabel(String s) {
        super(s);
    }
    public FieldLabel(int x, int y){
        this.line = x;
        this.column = y;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int x) {
        this.line = x;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int y) {
        this.column = y;
    }

    /**
     * Marks Label with a green dot to indicate that it is a valid Field to move to, sets isMarked to true
     */
    public void mark(){
        isMarked = true;
        ImageView img = new ImageView( new Image(Main.class.getResourceAsStream("/dot.png")));
        img.setFitWidth(20);
        img.setFitHeight(20);
        this.setGraphic(img);
    }

    /**
     * Removes green dot from Label, sets isMarked to false
     */
    public void unmark(){
        isMarked = false;
        this.setGraphic(null);
    }

    /**
     * Outlines the Label with a red outline to indicate that there is a Piece on the Label that can be taken, sets isOutlined to true
     */
    public void outline(){
        isOutlined = true;
        this.getStyleClass().add("potentialTake");
    }

    /**
     * Removes red Outline from Label, sets isOutlined to false
     */
    public void removeOutline(){
        isOutlined = false;
        this.getStyleClass().remove("potentialTake");
    }

    /**
     * Changes the Labels color to green to show which Label the player clicked on, sets isSelected to true
     */
    public void select() {
        isSelected = true;
        this.getStyleClass().add("selectedField");
    }
    public void selectPremoveSource() {
        isSelected = true;
        this.getStyleClass().add("selectedFieldPremoveSource");
    }
    public void selectPremoveTarget() {
        isSelected = true;
        this.getStyleClass().add("selectedFieldPremoveTarget");
    }


    /**
     * Removes the green color from Label, sets isSelected to false
     */
    public void unselect() {
        isSelected = false;
        this.getStyleClass().remove("selectedField");
        this.getStyleClass().remove("selectedFieldPremoveSource");
        this.getStyleClass().remove("selectedFieldPremoveTarget");
    }

    /**
     * Marks Label with a red outline similar to Field.outline() to indicate that the King on this Label is in Check, sets isCheckLabel to true
     */
    public void markAsCheck(){
        isCheckLabel = true;
        this.getStyleClass().add("checkLabel");
    }

    /**
     * Removes the red outline from the Label, sets isCheckLabel to false
     */
    public void unmarkAsCheck(){
        isCheckLabel = false;
        this.getStyleClass().removeAll("checkLabel");
    }
    public boolean isMarked() {
        return isMarked;
    }
    public boolean isOutlined() {
        return isOutlined;
    }
    public boolean isCheckLabel() {
        return isCheckLabel;
    }
    @Override
    public String toString() {
        return "FieldLabel{" +
                "line=" + line +
                ", column=" + column +
                '}';
    }
}
