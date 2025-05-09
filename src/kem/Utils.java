package kem;

public class Utils {
    

    public static void printPoly(Poly poly) {
        StringBuilder sb = new StringBuilder();
        for (int coeff : poly.getCoeffs()) {
            sb.append(coeff).append(" ");
        }
        System.out.println(sb.toString().trim());
    }


    public static int[] recon(Poly mhat) {
        int[] m = new int[Kem.N];
        for (int i = 0; i < Kem.N; i++) {
            m[i] = Math.abs(mhat.get(i) - Kem.Q2) <= Kem.Q4 ? 1 : 0;
        }
        return m;
    }


}
