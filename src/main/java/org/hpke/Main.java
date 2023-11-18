package org.hpke;
import javax.crypto.DecapsulateException;
import javax.crypto.KEM;
import javax.crypto.Cipher;
import java.security.*;
import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, DecapsulateException {

        //Receiver side
        var kpg = KeyPairGenerator.getInstance("X25519");
        KeyPair kp = kpg.generateKeyPair();


        //Sender side
        var kem1 = KEM.getInstance("DHKEM");
        var sender = kem1.newEncapsulator(kp.getPublic());
        var encapsulated = sender.encapsulate();
        var k1 = encapsulated.key();

        // Receiver side
        var kem2 = KEM.getInstance("DHKEM");
        var receiver = kem2.newDecapsulator(kp.getPrivate());
        var k2 = receiver.decapsulate(encapsulated.encapsulation());
        for (int i = 0; i < k1.getEncoded().length ; i++) {
            System.out.print(k1.getEncoded()[i]);
        }
        System.out.println("");
        for (int i = 0; i < k2.getEncoded().length ; i++) {
            System.out.print(k2.getEncoded()[i]);
        }
        assert Arrays.equals(k1.getEncoded(), k2.getEncoded());
    }
}