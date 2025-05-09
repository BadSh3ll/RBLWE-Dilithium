package kem;

public class Utils {
    

    public static void printPoly(Poly poly) {
        System.out.println(poly.toString());
    }


    public static int[] recon(Poly mhat) {
        int[] m = new int[Kem.N];
        for (int i = 0; i < Kem.N; i++) {
            m[i] = Math.abs(mhat.get(i) - Kem.Q2) <= Kem.Q4 ? 1 : 0;
        }
        return m;
    }


}
