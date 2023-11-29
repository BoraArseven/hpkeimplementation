//package org.hpke;
//
//import javax.crypto.DecapsulateException;
//import java.security.*;
//import java.util.Arrays;
//import org.hpke.HKDF;
//import org.hpke.AES;
//// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
//// then press Enter. You can now see whitespace characters in your code.
//
//public class HPKE2 {
//    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, DecapsulateException {
//        //Receiver side
//        var kpg = KeyPairGenerator.getInstance("X25519");
//        KeyPair kp_receiver = kpg.generateKeyPair(); // Generate a key pair for the receiver
//
//        //Sender side
//        javax.crypto.KEM kem1 = javax.crypto.KEM.getInstance("DHKEM");
//        KeyPair kp_sender = kpg.generateKeyPair(); // Generate a key pair for the sender
//        var sender = kem1.newEncapsulator(kp_receiver.getPublic()); // Use the receiver's public key for encapsulation
//
//        // Define the modes as byte values
//        byte[] MODES = new byte[] {0, 1}; // 0 for basic mode, 1 for PSK mode
//
//        // Choose a mode randomly
//        SecureRandom random = new SecureRandom();
//        byte mode = MODES[random.nextInt(MODES.length)];
//
//        // Generate or agree on a PSK and a PSK ID if PSK mode is chosen
//        var kdf = HKDF.fromHmacSha256();
//        byte[] psk = null;
//        byte[] psk_id = null;
//        if (mode == 1) {
//            psk = new byte[kdf.getMacFactory().getMacLengthBytes()]; // Generate a random PSK of the same length as the hash output
//            random.nextBytes(psk);
//            psk_id = new byte[] {0x01, 0x02, 0x03}; // Use some unique PSK ID
//        }
//
//        // Create an HPKE instance with the chosen mode
//        javax.crypto.HPKE hpke = javax.crypto.HPKE.getInstance(mode, psk, psk_id);
//
//        // Encapsulate the key and encrypt the message using the chosen mode
//        var encapsulated = sender.encapsulate();
//        var k1 = encapsulated.key();
//        byte[] message = "Hello, world!".getBytes();
//        byte[] aad = new byte[] {0x04, 0x05, 0x06}; // Use some associated data
//        byte[] ciphertext = hpke.encrypt(k1, aad, message);
//
//        // Prepend the mode to the output
//        byte[] output = new byte[1 + encapsulated.encapsulation().length + ciphertext.length];
//        output[0] = mode;
//        System.arraycopy(encapsulated.encapsulation(), 0, output, 1, encapsulated.encapsulation().length);
//        System.arraycopy(ciphertext, 0, output, 1 + encapsulated.encapsulation().length, ciphertext.length);
//
//        // Receiver side
//        var kem2 = javax.crypto.KEM.getInstance("DHKEM");
//        var receiver = kem2.newDecapsulator(kp_receiver.getPrivate());
//
//        // Extract the mode from the input
//        byte mode2 = input[0];
//
//        // Extract the PSK and the PSK ID if PSK mode is chosen
//        byte[] psk2 = null;
//        byte[] psk_id2 = null;
//        if (mode2 == 1) {
//            psk2 = ...; // Get the PSK from some source
//            psk_id2 = ...; // Get the PSK ID from some source
//        }
//
//        // Create an HPKE instance with the same mode
//        javax.crypto.HPKE hpke2 = javax.crypto.HPKE.getInstance(mode2, psk2, psk_id2);
//
//        // Extract the encapsulated key and the ciphertext from the input
//        byte[] encapsulation = new byte[encapsulated.encapsulation().length];
//        byte[] ciphertext2 = new byte[input.length - 1 - encapsulation.length];
//        System.arraycopy(input, 1, encapsulation, 0, encapsulation.length);
//        System.arraycopy(input, 1 + encapsulation.length, ciphertext2, 0, ciphertext2.length);
//
//        // Decapsulate the key and decrypt the message using the same mode
//        var k2 = receiver.decapsulate(encapsulation);
//        byte[] plaintext = hpke2.decrypt(k2, aad, ciphertext2);
//
//        // Print the decrypted message
//        System.out.println(new String(plaintext));
//
//        assert Arrays.equals(k1.getEncoded(), k2.getEncoded());
//        // end of the KEM
//        // KDFN(hkdf)
//        var kdf = HKDF.fromHmacSha256();
//        byte[] salt = new byte[kdf.getMacFactory().getMacLengthBytes()]; // Generate a random salt of the same length as the hash output
//        SecureRandom random = new SecureRandom();
//        random.nextBytes(salt);
//        var randomkey =kdf.extract(salt,k1.getEncoded()); // Use the salt and the KEM key for the HKDF extract step
//        byte[] info = new byte[] {0x01, 0x02, 0x03}; // Use some info parameter for the HKDF expand step
//        var derivedkey = kdf.expand(randomkey,info,kdf.getMacFactory().getMacLengthBytes()); // Derive a key of the same length as the hash output
//    }
//}
