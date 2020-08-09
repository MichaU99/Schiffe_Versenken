package Werft;

public class Test {
    public static void main(String[] args) {
        int k1, k2;
        boolean[][] testmeer;
        int[] standartSpiel=new int[]{2,2,2,2,3,3,3,3,4,4,5,5};
        int[] kurzesSpiel=new int[]{1,1,1,1,2,2,2,2,3,3,4,4};

        Schiffsverteilung test = new Schiffsverteilung();
        testmeer = test.verteile_schiffe(standartSpiel);
        for (k1 = 0; k1 < testmeer[0].length; k1++) {   //for Schleife über die beiden Dimensionen
            System.out.println();
            for (k2 = 0; k2 < testmeer[1].length; k2++) {
                if(testmeer[k1][k2]==true) System.out.print("0 ");
                else System.out.print("1 ");
            }
        }
    }
}