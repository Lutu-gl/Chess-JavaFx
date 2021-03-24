package gameLogic;


public enum Color {
    WHITE,
    BLACK;

    public static String getCapitalizedName(Color e){
        return e.toString().substring(0, 1).toUpperCase() + e.toString().substring(1).toLowerCase();
    }
}
