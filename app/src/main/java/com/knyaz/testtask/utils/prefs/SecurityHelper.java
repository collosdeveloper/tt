package com.knyaz.testtask.utils.prefs;

import android.os.Build;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityHelper {
    private static final boolean HAS_JELLY_BEAN = Build.VERSION.SDK_INT >= Build.VERSION.SDK_INT;

    /* Security */
    private static final byte[] SEED_BYTES = "thisisseed".getBytes();
    private static final char[] SEED_CHARS = "thisisseed".toCharArray();
    private static final int KEY_SIZE = 32;
    private static final byte[] SALT = new byte[KEY_SIZE];

    private static final String CRYPTO_ALGORITHM = "SHA1PRNG";
    private static final String CRYPTO_PROVIDER = "Crypto";

    private static final int INTERACTION_COUNT = 100;
    private static final int KEY_SIZE_INT_BITS = 8;

    private static final int RANDOM_KEY_SIZE = 128;

    static {
        Arrays.fill(SALT, (byte) 1);
    }

    private static final String ALG = "AES";

    @Deprecated
    private static SecretKey getRawKeyDeprecated() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(ALG);

        SecureRandom sr = HAS_JELLY_BEAN
                ? SecureRandom.getInstance(CRYPTO_ALGORITHM, CRYPTO_PROVIDER) // getProvider(): AndroidOpenSSL by default
                // in JB, so force to use "Crypto"
                : SecureRandom.getInstance(CRYPTO_ALGORITHM);
        sr.setSeed(SEED_BYTES);
        kgen.init(RANDOM_KEY_SIZE, sr); // 192 and 256 bits may not be available
        return kgen.generateKey();
    }

    private static SecretKey getRawKeyNew() throws Exception {
        KeySpec keySpec = new PBEKeySpec(SEED_CHARS, SALT,
                INTERACTION_COUNT, KEY_SIZE * KEY_SIZE_INT_BITS);
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            return new SecretKeySpec(keyBytes, ALG);
        } catch (Exception e) {
            throw new RuntimeException("Deal with exceptions properly!", e);
        }
    }

    public static String encodeString(String str) {
        if (str == null)
            return null;

        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALG);
            cipher.init(Cipher.ENCRYPT_MODE, getRawKeyNew());

            result = cipher.doFinal(str.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeBytes(result);
    }

    public static String decodeString(String encrypted, boolean oldStyle) {
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALG);
            cipher.init(Cipher.DECRYPT_MODE, oldStyle ? getRawKeyDeprecated() : getRawKeyNew());

            byte[] encryptedBytes = Base64.decode(encrypted);
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            result = new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}