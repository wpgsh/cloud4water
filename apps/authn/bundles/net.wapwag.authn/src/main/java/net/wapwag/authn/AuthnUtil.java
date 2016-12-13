package net.wapwag.authn;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * Authn util for some general method
 * Created by Administrator on 2016/11/24 0024.
 */
@SuppressWarnings("Duplicates")
public final class AuthnUtil {

    private static final Logger logger = LoggerFactory.getLogger(AuthnUtil.class);

    private static final String UTF8 = "UTF-8";
    private static final String AUTHN_CONFIG = "authn.file";
    private static byte[] AUTHN_SECRET_KEY_BYTES;
    private static final Base64.Encoder base64Encoder = Base64.getEncoder().withoutPadding();

    static {
        try {
            Map<String, String> constantMap = loadProperties(readJVMProperties(AUTHN_CONFIG));
            String AUTHN_SECRET_KEY = constantMap.get("authn_secret_key");
            AUTHN_SECRET_KEY_BYTES = AUTHN_SECRET_KEY.getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Use AES/CBC/PKCS5PADDING to encrypt specific text,
     * use secure randowm class for generating the aes key
     * @param originalText the text you want to be encrypted
     * @return return cryptograph
     */
    static String encryptAES(String originalText) {
        try {
            KeyGenerator keyGen;
            keyGen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(AUTHN_SECRET_KEY_BYTES);
            keyGen.init(128, secureRandom);
            SecretKey secretKey = keyGen.generateKey();
            SecretKeySpec resultKey = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, resultKey);
            byte[] result = cipher.doFinal(originalText.getBytes(UTF8));

            return encodeBase64Byte(result);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException |
                InvalidKeyException | NoSuchPaddingException |
                BadPaddingException | UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static String encodeBase64Byte(byte[] originalBytes) {
        if (originalBytes != null && originalBytes.length > 0) {
            return base64Encoder.encodeToString(originalBytes);
        } else {
            throw new IllegalArgumentException();
        }
    }

    static String encodeBase64String(String originalText) {
        if (StringUtils.isNotBlank(originalText)) {
            return encodeBase64Byte(originalText.getBytes());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Set<String> readJVMProperties(String... paramNames) {
        Set<String> preloadSet = new HashSet<>();
        if (ArrayUtils.isNotEmpty(paramNames)) {

            for (String paramName : paramNames) {
                String configPath = System.getProperty(paramName);

                if (StringUtils.isNotBlank(configPath)) {
                    preloadSet.add(configPath);
                }
            }

        } else {
            throw new IllegalArgumentException("jvm param name couldn't be empty");
        }

        if (logger.isInfoEnabled()) {
            logger.info("read {} params,\n{}", paramNames.length, preloadSet);
        }

        return preloadSet;
    }

    public static Map<String, String> loadProperties(Collection<String> paths) {
        return loadProperties(paths.toArray(new String[paths.size()]));
    }

    public static Map<String, String> loadProperties(String... paths) {
        Map<String, String> preloadMap = new HashMap<>();

        if (ArrayUtils.isNotEmpty(paths)) {

            Properties properties;
            for (String path : paths) {
                if (StringUtils.isNotBlank(path)) {

                    properties = new Properties();
                    try {
                        properties.load(Files.newBufferedReader(Paths.get(path)));

                        Set<String> keySet = properties.stringPropertyNames();
                        if (!keySet.isEmpty()) {
                            for (String key : properties.stringPropertyNames()) {
                                preloadMap.put(key, properties.getProperty(key));
                            }
                        }

                    } catch (IOException e) {
                        throw new IllegalArgumentException("couldn't load the config file");
                    } finally {
                        properties.clear();
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("the path of the config file couldn't be empty");
        }

        if (logger.isInfoEnabled()) {
            logger.info("Load {} config,\n{}", paths.length, preloadMap);
        }

        return preloadMap;
    }

}
