package org.hpke;
import javax.crypto.DecapsulateException;
import java.security.*;
import java.util.Arrays;

;
public class KEM {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, DecapsulateException {

//        //Receiver side
//        var kpg = KeyPairGenerator.getInstance("X25519");
//        KeyPair kp = kpg.generateKeyPair();
//
//
//        //Sender side
//        javax.crypto.KEM kem1 = javax.crypto.KEM.getInstance("DHKEM");
//        var sender = kem1.newEncapsulator(kp.getPublic());
//        var encapsulated = sender.encapsulate();
//        var k1 = encapsulated.key();
//
//        // Receiver side
//        var kem2 = javax.crypto.KEM.getInstance("DHKEM");
//        var receiver = kem2.newDecapsulator(kp.getPrivate());
//        var k2 = receiver.decapsulate(encapsulated.encapsulation());
//        // Bora's addition
//        for (int i = 0; i < k1.getEncoded().length ; i++) {
//            System.out.print(k1.getEncoded()[i]);
//        }
//        System.out.println("");
//        for (int i = 0; i < k2.getEncoded().length ; i++) {
//            System.out.print(k2.getEncoded()[i]);
//        }
//
//        assert Arrays.equals(k1.getEncoded(), k2.getEncoded());
 }
}