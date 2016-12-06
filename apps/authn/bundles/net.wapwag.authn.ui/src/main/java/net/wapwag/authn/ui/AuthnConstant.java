package net.wapwag.authn.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static net.wapwag.authn.AuthnUtil.loadProperties;
import static net.wapwag.authn.AuthnUtil.readJVMProperties;

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
}
