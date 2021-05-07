package view;

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


    public String getS1() {
        return s1;
    }

    public String getS2() {
        return s2;
    }
}
