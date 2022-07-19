package com.diario.serviceUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Singleton;
import java.util.Base64;

@Singleton
public class CryptoService {
    private static final String PADDING = "AES/ECB/PKCS5Padding";
    private final String KEY = "PdSgVkYp3s6v9y/B";

    public String encrypt(final String text) {
        try {
            final SecretKey secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
            final Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedText = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(encryptedText);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot encrypt text", e);
        }
    }

    public String decrypt(final String encrypted) {
        try {
            final SecretKey secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
            final Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot decrypt text", e);
        }
    }

}