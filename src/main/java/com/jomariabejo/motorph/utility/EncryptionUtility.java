package com.jomariabejo.motorph.utility;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtility {

    // AES encryption algorithm
    private static final String ALGORITHM = "AES";

    // Method to generate a new AES key
    public static SecretKey generateKey(int n) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(n); // 128, 192, or 256 bits
        return keyGenerator.generateKey();
    }

    // Method to encrypt a string
    public static String encrypt(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Method to decrypt a string
    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, "UTF-8");
    }

    // Method to convert a SecretKey to a Base64-encoded string
    public static String keyToBase64(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // Method to convert a Base64-encoded string to a SecretKey
    public static SecretKey base64ToKey(String keyBase64) {
        byte[] decodedKey = Base64.getDecoder().decode(keyBase64);
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }

    // Example usage
    public static void main(String[] args) {
        try {
            // Generate a new key
            SecretKey key = generateKey(128);

            // Convert key to Base64 for storage or transmission
            String keyBase64 = keyToBase64(key);
            System.out.println("Key (Base64): " + keyBase64);

            // Encrypt a message
            String originalMessage = "Hello, World!";
            String encryptedMessage = encrypt(originalMessage, key);
            System.out.println("Encrypted Message: " + encryptedMessage);

            // Decrypt the message using the same key
            SecretKey keyFromBase64 = base64ToKey(keyBase64);
            String decryptedMessage = decrypt(encryptedMessage, keyFromBase64);
            System.out.println("Decrypted Message: " + decryptedMessage);


            //
            System.out.println("This was my encrypted message");

            SecretKey secretKey = generateKey(128);
            String keyBase = keyToBase64(secretKey);
            System.out.println("Key base: " + keyBase);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
