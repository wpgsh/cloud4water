package net.wapwag.authn;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Authn util for some general method
 * Created by Administrator on 2016/11/24 0024.
 */
@SuppressWarnings("Duplicates")
public class AuthnUtil {

    /**
     * Use AES/CBC/PKCS5PADDING to encrypt specific text,
     * use secure randowm class for generating the aes key
     * @param originalText the text you want to be encrypted
     * @return return cryptograph
     */
    public static String encryptAES(String originalText) {
        try {
            KeyGenerator keyGen = null;
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128, new SecureRandom());
            SecretKey secretKey = keyGen.generateKey();

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(originalText.getBytes());

            return encodeBase64Byte(result);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | BadPaddingException e) {
            throw new IllegalArgumentException();
        }
    }

    public static String encodeBase64Byte(byte[] originalBytes) {
        if (originalBytes != null && originalBytes.length > 0) {
            return Base64.getEncoder().encodeToString(originalBytes);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String encodeBase64String(String originalText) {
        if (StringUtils.isNotBlank(originalText)) {
            return encodeBase64Byte(originalText.getBytes());
        } else {
            throw new IllegalArgumentException();
        }
    }

}
