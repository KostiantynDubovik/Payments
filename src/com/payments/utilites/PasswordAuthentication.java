package com.payments.utilites;

import org.apache.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * @author Kostiantyn Dubovik
 */
public class PasswordAuthentication {
    private static final Logger LOG = Logger.getLogger(PasswordAuthentication.class);
    private static final String SHA_1_PRNG = "SHA1PRNG";
    private static final String PBKDF_2_WITH_HMAC_SHA_512 = "PBKDF2WithHmacSHA512";

//    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
//        System.out.println(generateStrongPasswordHash("user"));
//    }

    public static String generateStrongPasswordHash(String password) {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = new byte[0];
        try {
            salt = getSalt();
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);

        byte[] hash = new byte[0];
        hash = getBytes(spec, hash);

        return String.valueOf(iterations) + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance(SHA_1_PRNG);
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public static boolean validatePassword(String originalPassword, String storedPassword) {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);

        byte[] testHash = new byte[0];
        testHash = getBytes(spec, testHash);


        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] getBytes(PBEKeySpec spec, byte[] testHash) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_512);
            testHash = skf.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
        }
        return testHash;
    }


    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
