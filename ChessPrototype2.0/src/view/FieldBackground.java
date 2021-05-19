package view;

/**
 * Enum to provide some constants for the design of the fields
 */
public enum FieldBackground {
    STANDARD(new String[]{"creamwhiteField", "lightbrownField"}),
    SKY(new String[]{"whiteField", "lightblueField"}),
    CHEWING_GUM(new String[]{"whiteField", "pinkField"}),
    EIGHT_BIT(new String[]{"whiteField", "greenField"}),
    TRANSPARENT(new String[]{"whiteField", "lightgreyField"}),
    LUMBERJACK(new String[]{"beigeField", "brownField"}),
    METALLIC(new String[]{"lightmetallicField", "darkmetallicField"});

    private String s1, s2;
    FieldBackground(String[] array) {
        this.s1 = array[0];
        this.s2 = array[1];
    }

    /**
     * Get color of light field
     * @return
     */
    public String getS1() {
        return s1;
    }

    /**
     * Get color of dark field
     * @return
     */
    public String getS2() {
        return s2;
    }
}
