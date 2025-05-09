import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Arrays;

import kem.*;
import sign.*;

public class App {
    public static void main(String[] args) throws Exception {
        test_kem();
        test_sig();
        test_both();
        System.out.println("All tests passed");  
    }


    public static void test_both() throws Exception {
        byte[] seed = new byte[32];
        (new SecureRandom()).nextBytes(seed);

        KeyPair KemKeyPair = Kem.keygen();
        KeyPair SignKeyPair = Dilithium.keygen(seed);

        int msg[] = new int[Kem.N];
        Rng.sampleNoise(msg);

        CipherText enc = Kem.encapsulate((KemPublicKey) KemKeyPair.getPublic(), msg);
        byte[] sig = Dilithium.sign((DilithiumPrivateKey) SignKeyPair.getPrivate(), enc.getC1().toString().getBytes());


        int[] dec = Kem.decapsulate((KemPrivateKey) KemKeyPair.getPrivate(), enc);
        boolean valid = Dilithium.verify((DilithiumPublicKey) SignKeyPair.getPublic(), sig, enc.getC1().toString().getBytes());

        if (dec.length != msg.length) {
            throw new Exception("Decryption failed");
        }
        for (int i = 0; i < msg.length; i++) {
            if (dec[i] != msg[i]) {
                throw new Exception("Decryption failed");
            }
        }

        if (!valid) {
            throw new Exception("Signature verification failed");
        }
    }

    public static void test_kem() throws Exception {

        int fails = 0;

        for (int i = 0; i < 1000; i++) {
            byte[] seed = new byte[32];
            (new SecureRandom()).nextBytes(seed);

            KeyPair KemKeyPair = Kem.keygen();
            KeyPair alteredKeyPair = Kem.keygen();

            int msg[] = new int[Kem.N];
            Rng.sampleNoise(msg);

            CipherText enc = Kem.encapsulate((KemPublicKey) KemKeyPair.getPublic(), msg);
            
            int[] dec = Kem.decapsulate((KemPrivateKey) KemKeyPair.getPrivate(), enc);
            int[] alteredDec = Kem.decapsulate((KemPrivateKey) alteredKeyPair.getPrivate(), enc);

            boolean shouldEquals = Arrays.equals(msg, dec);
            boolean shouldNotEquals = !Arrays.equals(msg, alteredDec);
            if (!shouldEquals || !shouldNotEquals) {
                fails++;
            }
        }

        if (fails > 0) {
            throw new Exception("Decryption failed " + fails + " times");
        } else {
            System.out.println("Decryption passed");
        }

    }

    public static void test_sig() throws Exception {

        int fails = 0;

        for (int i = 0; i < 1000; i++) {
                byte[] seed = new byte[32];
                (new SecureRandom()).nextBytes(seed);
        
                KeyPair kp = Dilithium.keygen(seed);
                KeyPair alteredKeyPair = Dilithium.keygen(null);
        
                byte[] msg = new byte[Kem.N];
                (new SecureRandom()).nextBytes(msg);
        
                byte[] sig = Dilithium.sign((DilithiumPrivateKey) kp.getPrivate(), msg);
                boolean shouldBeValid = Dilithium.verify((DilithiumPublicKey) kp.getPublic(), sig, msg);
                boolean shouldBeInValid = Dilithium.verify((DilithiumPublicKey) alteredKeyPair.getPublic(), sig, msg);
                
                if (!shouldBeValid || shouldBeInValid) {
                    fails++;
                }
            
        }
        if (fails > 0) {
            throw new Exception("Signature verification failed " + fails + " times");
        } else {
            System.out.println("Signature verification passed");
        }

    }

}
