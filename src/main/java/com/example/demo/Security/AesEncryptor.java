package com.example.demo.Security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
    public class AesEncryptor {

        @Value("${encryption.aes.key}")
        private String base64Key;

        private SecretKeySpec secretKey;

        @PostConstruct
        public void init() {
            byte[] decodedKey = Base64.getDecoder().decode(base64Key);
            if (decodedKey.length != 16 && decodedKey.length != 24 && decodedKey.length != 32) {
                throw new IllegalArgumentException("Invalid AES key length");
            }
            this.secretKey = new SecretKeySpec(decodedKey, "AES");
        }

        public String encrypt(String plainText) throws Exception {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        }

        public String decrypt(String cipherText) throws Exception {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            return new String(cipher.doFinal(decoded));
        }
    }

