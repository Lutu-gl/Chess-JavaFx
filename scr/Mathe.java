public class Mathe {
    public static void main(String[] args) {
        int x=0;
        int y=0;
        int n=0;
        long erg=0;

        while(n<=31) {
            y=-1000;
            while (y < 1000) {
                x = -1000;
                while (x < 1000) {
                    erg = 2 * (x * x) + (4 * x * y) + (7 * y * y) - (12 * x) - (2 * y) + n;
                    //System.out.println("erg: " + erg + " bei " + x + " " + y);
                    if (erg == 0) {
                        ///System.out.println("Lösung bei: " + x + " " + y + " und bei n=" + n);
                    }
                    x++;
                }
                y++;
            }
            n++;
        }

    }
}
