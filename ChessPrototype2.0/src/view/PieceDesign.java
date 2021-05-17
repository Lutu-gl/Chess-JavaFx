package view;

public enum PieceDesign {
    STANDARD(""),
    BOOK("book_"),
    SPACE("space_"),
    ALPHA("Alpha_"),
    NEO("Neo_"),
    OLDSCHOOL("Oldschool_"),
    SKY("Sky_");

    private String design;

    PieceDesign(String design) {
        this.design = design;
    }

    public String getDesign() {
        return this.design;
    }

}
