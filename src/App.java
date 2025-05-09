import java.util.Arrays;

import kem.*;


public class App {
    public static void main(String[] args) throws Exception {

        int fails = 0;
        for (int i = 0; i < 1000; i++) {
            try {
                test();
            } catch (Exception e) {
                fails++;
                System.out.println("Test failed: " + e.getMessage());
            }
        }

        System.out.println("Total failures: " + fails);
        

    }

    public static void test() throws Exception {
        // Alice keygen
        Poly a = new Poly();
        Rng.randomInt(a.getCoeffs());

        Poly r1, r2;
        r1 = new Poly();
        r2 = new Poly();
        Rng.sampleNoise(r1.getCoeffs());
        Rng.sampleNoise(r2.getCoeffs());

        Poly p = Poly.sub(r1, a.mult(r2));

        // Bob select and encrypt m
        int[] m = new int[Kem.N];
        Rng.sampleNoise(m);

        Poly mhat = new Poly(m);
        Poly e1, e2, e3;
        e1 = new Poly();
        e2 = new Poly();
        e3 = new Poly();
        Rng.sampleNoise(e1.getCoeffs());
        Rng.sampleNoise(e2.getCoeffs());
        Rng.sampleNoise(e3.getCoeffs());

        Poly c1 = Poly.add(Poly.mult(a, e1), e2);
        Poly c2 = Poly.add(Poly.mult(p, e1), e3.add(mhat));


        // Alice decrypts
        Poly mhat1 = Poly.mult(c1, r2).add(c2);

        int[] m2 = Utils.recon(mhat1);
        if (!Arrays.equals(m, m2)) {
            throw new Exception("Decryption failed: " + Arrays.toString(m) + " != " + Arrays.toString(m2));
        }
    }



}
