package net.wapwag.authn.ui;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Authn constant
 * Created by Administrator on 2016/11/14 0014.
 */
@SuppressWarnings("Duplicates")
enum AuthnConstant {

    AUTHN_ERROR_PATH("authn_error_path");

    AuthnConstant(String key) {
        this.key = key;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthnConstant.class);

    private static final String AUTHN_CONFIG = "authn.file";

    private static Map<String, String> AUTHN_CONSTANT_MAP;

    private String key;

    public String value() {
        return AUTHN_CONSTANT_MAP.get(key);
    }

    static {
        AUTHN_CONSTANT_MAP = loadProperties(readJVMProperties(AUTHN_CONFIG));
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
