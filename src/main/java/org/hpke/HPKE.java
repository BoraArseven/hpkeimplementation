    package org.hpke;

    import javax.crypto.DecapsulateException;
    import java.security.*;
    import java.util.Arrays;
    import org.hpke.HKDF;
    import org.hpke.AES;
    // Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
    // then press Enter. You can now see whitespace characters in your code.
    public class HPKE {
        public static void main(String[] args) throws Exception {
            //Receiver side
            var kpg = KeyPairGenerator.getInstance("X25519");
            KeyPair kp_receiver = kpg.generateKeyPair(); // Generate a key pair for the receiver


            //Sender side
            javax.crypto.KEM kem1 = javax.crypto.KEM.getInstance("DHKEM");
            KeyPair kp_sender = kpg.generateKeyPair(); // Generate a key pair for the sender
            var sender = kem1.newEncapsulator(kp_receiver.getPublic()); // Use the receiver's public key for encapsulation
            var encapsulated = sender.encapsulate();
            var k1 = encapsulated.key();

            // Receiver side
            var kem2 = javax.crypto.KEM.getInstance("DHKEM");
            var receiver = kem2.newDecapsulator(kp_receiver.getPrivate());
            var k2 = receiver.decapsulate(encapsulated.encapsulation());
            // Bora's addition
            for (int i = 0; i < k1.getEncoded().length; i++) {
                System.out.print(k1.getEncoded()[i]);
            }
            System.out.println("");
            for (int i = 0; i < k2.getEncoded().length; i++) {
                System.out.print(k2.getEncoded()[i]);
            }

            assert Arrays.equals(k1.getEncoded(), k2.getEncoded());
            // end of the KEM
            // KDFN(hkdf)
            var kdf = HKDF.fromHmacSha256();
            byte[] salt = new byte[kdf.getMacFactory().getMacLengthBytes()]; // Generate a random salt of the same length as the hash output
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            var randomkey =kdf.extract(salt,k1.getEncoded()); // Use the salt and the KEM key for the HKDF extract step
            byte[] info = new byte[] {0x01, 0x02, 0x03}; // Use some info parameter for the HKDF expand step
            var derivedkey = kdf.expand(randomkey,info,kdf.getMacFactory().getMacLengthBytes()); // Derive a key of the same length as the hash output
            //AEAD
            // The derivedkey should be 16 bytes (128 bits) for AES-128


            // The message to be encrypted
            byte[] message = "Hello, world!".getBytes();

            // Encrypt the message using AEAD
            byte[] ciphertext = AES.encrypt(derivedkey, message);

            // Decrypt the ciphertext using AEAD
            byte[] plaintext = AES.decrypt(derivedkey, ciphertext);

            // Print the decrypted message
            System.out.println(new String(plaintext));
        }

        }



