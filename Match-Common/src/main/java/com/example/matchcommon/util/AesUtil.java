package com.example.matchcommon.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AesUtil {


    @Value("${private.aes.key}")
    private String privateKey;


    public String aesCBCEncode(String plainText)  {
        SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec IV = new IvParameterSpec(privateKey.substring(0,16).getBytes());

        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, secretKey, IV);

            byte[] encrpytionByte = c.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return Hex.encodeHexString(encrpytionByte);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }


    public String aesCBCDecode(String encodeText)  {

        SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec IV = new IvParameterSpec(privateKey.substring(0,16).getBytes());

        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

            c.init(Cipher.DECRYPT_MODE, secretKey, IV);

            byte[] decodeByte = Hex.decodeHex(encodeText.toCharArray());

            return new String(c.doFinal(decodeByte), StandardCharsets.UTF_8);
        }catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}