package view;

public enum PieceDesign {
    STANDARD(""),
    BOOK("book_");

    private String design;

    PieceDesign(String design) {
        this.design = design;
    }

    public String getDesign() {
        return this.design;
    }

}
