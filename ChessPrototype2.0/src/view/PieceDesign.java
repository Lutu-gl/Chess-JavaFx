package view;

/**
 * Enum to provide some constants for the design of the pieces
 */
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

    /**
     * Get design of pieces
     * @return
     */
    public String getDesign() {
        return this.design;
    }

}
