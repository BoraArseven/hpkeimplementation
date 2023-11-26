package org.hpke;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;

public class AES {

    // The tag length should be 16 bytes (128 bits)
    private static final int tagLength = 16 * 8;

    public static void main(String[] args) throws Exception {
        // The derivedkey should be 16 bytes (128 bits) for AES-128
        byte[] derivedkey = new byte[] {0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18,
                0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20};

        // The message to be encrypted
        byte[] message = "Hello, world!".getBytes();

        // Encrypt the message using AEAD
        byte[] ciphertext = encrypt(derivedkey, message);

        // Decrypt the ciphertext using AEAD
        byte[] plaintext = decrypt(derivedkey, ciphertext);

        // Print the decrypted message
        System.out.println(new String(plaintext));
    }

    // Encrypts a message using AEAD with AES/GCM/NoPadding
    public static byte[] encrypt(byte[] key, byte[] message) throws Exception {
        // Create a secret key from the derived key
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        // Create a cipher instance for AES/GCM/NoPadding
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Generate a random nonce of 12 bytes
        SecureRandom random = new SecureRandom();
        byte[] nonce = new byte[12];
        random.nextBytes(nonce);

        // Initialize the cipher with the secret key and the GCM parameters
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(tagLength, nonce));

        // Generate some associated data to be authenticated
        byte[] associatedData = new byte[] {0x0d, 0x0e, 0x0f, 0x10};

        // Update the cipher with the associated data
        cipher.updateAAD(associatedData);

        // Encrypt the message and return the ciphertext
        byte[] ciphertext = cipher.doFinal(message);

        // Create an output stream to concatenate the nonce, the associated data, and the ciphertext
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Write the nonce to the output stream
        output.write(nonce);

        // Write the associated data to the output stream
        output.write(associatedData);

        // Write the ciphertext to the output stream
        output.write(ciphertext);

        // Return the output as a byte array
        return output.toByteArray();
    }

    // Decrypts a ciphertext using AEAD with AES/GCM/NoPadding
    public static byte[] decrypt(byte[] key, byte[] ciphertext) throws Exception {
        // Create a secret key from the derived key
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        // Create a cipher instance for AES/GCM/NoPadding
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Extract the nonce from the first 12 bytes of the ciphertext
        byte[] nonce = new byte[12];
        System.arraycopy(ciphertext, 0, nonce, 0, 12);

        // Initialize the cipher with the secret key and the GCM parameters
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(tagLength, nonce));

        // Extract the associated data from the next 4 bytes of the ciphertext
        byte[] associatedData = new byte[4];
        System.arraycopy(ciphertext, 12, associatedData, 0, 4);

        // Update the cipher with the associated data
        cipher.updateAAD(associatedData);

        // Extract the actual ciphertext from the remaining bytes of the ciphertext
        byte[] actualCiphertext = new byte[ciphertext.length - 16];
        System.arraycopy(ciphertext, 16, actualCiphertext, 0, actualCiphertext.length);

        // Decrypt the ciphertext and return the plaintext
        return cipher.doFinal(actualCiphertext);
    }
}