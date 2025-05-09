package kem;

public class Kem {
    
    public static final int N = 1024;
    public static final int Q = 12289; // Modulus
    public static final int Q2 = Q / 2;
    public static final int Q4 = Q / 4;
    public static final int QINV = (1 << 24) / Q; // Precomputed value for Barrett reduction



    public static void keygen() {
        Poly a = new Poly();
        Rng.randomInt(a.getCoeffs());

        Poly r1, r2;
        r1 = new Poly();
        r2 = new Poly();
        Rng.sampleNoise(r1.getCoeffs());
        Rng.sampleNoise(r2.getCoeffs());

        // Poly p = Poly.sub(r1, a.mult(r2));
    }
    public static void encaps() {
        // Encapsulation logic
        System.out.println("Encapsulation logic");
    }
    public static void decaps() {
        // Decapsulation logic
        System.out.println("Decapsulation logic");
    }
}
