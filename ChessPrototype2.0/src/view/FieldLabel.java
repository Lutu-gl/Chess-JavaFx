package view;


import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public void mark(){
        isMarked = true;
        ImageView img = new ImageView( new Image(Main.class.getResourceAsStream("/dot.png")));
        img.setFitWidth(20);
        img.setFitHeight(20);
        this.setGraphic(img);
    }
    public void unmark(){
        isMarked = false;
        this.setGraphic(null);
    }
    public void outline(){
        isOutlined = true;
        this.getStyleClass().add("potentialTake");
    }
    public void removeOutline(){
        isOutlined = false;
        this.getStyleClass().remove("potentialTake");
    }
    public void select() {
        isSelected = true;
        this.getStyleClass().add("selectedField");
    }
    public void unselect() {
        isSelected = false;
        this.getStyleClass().remove("selectedField");
    }
    public void markAsCheck(){
        isCheckLabel = true;
        this.getStyleClass().add("checkLabel");
    }
    public void unmarkAsCheck(){
        isCheckLabel = false;
        this.getStyleClass().remove("checkLabel");
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
