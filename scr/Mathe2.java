public class Mathe2 {
    public static void main(String[] args) {

        double x=0.00001d,y=0.0000001d;
        double g1=0.001d;
        double g2=0.001d;
        double g3=0.001d;
        double g4=0.001d;

        double komplettesMax=0.0d;

        while(x < 100){
            y=0.000001d;
            while(y < 100){
                g1 = x + 1/x;
                g2 = x + 1/y;
                g3 = y + 1/x;
                g4 = y + 1/y;

                double max1 = Math.max(g1,g2);
                double max2 = Math.max(g3,g4);
                double max3 = Math.max(max1,max2);

                if(max3 > komplettesMax){
                    komplettesMax = max3;
                   // System.out.println("Bei x="+x + " y="+y);
                    //System.out.println(g1 + " " + g2+ " " + g3 + " " +g4);
                }

                y=y+000.1;
            }
            x=x+000.1;
        }






    }
}
