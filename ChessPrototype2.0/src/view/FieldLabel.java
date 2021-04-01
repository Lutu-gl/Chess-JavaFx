package view;


import javafx.scene.control.Label;

public class FieldLabel extends Label {

    private int line, column;


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


    @Override
    public String toString() {
        return "FieldLabel{" +
                "line=" + line +
                ", column=" + column +
                '}';
    }
}
