package net.wapwag.wemp.ui;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static net.wapwag.wemp.WempUtil.encodeURL;
import static net.wapwag.wemp.WempUtil.loadProperties;
import static net.wapwag.wemp.WempUtil.readJVMProperties;

/**
 * Wemp constant
 * Created by Administrator on 2016/11/14 0014.
 */
@SuppressWarnings("Duplicates")
public enum WempConstant {

    WEMP_ID("wemp_id"),

    WEMP_SECRET("wemp_secret"),

    WEMP_RETURN_PATH("wemp_return_path"),

    WEMP_ERROR_PATH("wemp_error_path");

    WempConstant(String key) {
        this.key = key;
    }

    private static final Logger logger = LoggerFactory.getLogger(WempConstant.class);

    private static final String WEMP_CONFIG = "wemp.file";

    private static Map<String, String> WEMP_CONSTANT_MAP;

    private String key;

    public String value() {
        return WEMP_CONSTANT_MAP.get(key);
    }

    /**
     * The path for /authorize.
     */
    public static String AUTHORIZE_PATH;
    private static final String AUTHORIZE_PATH_SUBSTITUE = "/authn/authorize?response_type=%s&redirect_uri=%s&client_id=%s";

    static {

        WEMP_CONSTANT_MAP = loadProperties(readJVMProperties(WEMP_CONFIG));

        try {
            // Encode url parameter value
            AUTHORIZE_PATH = String.format(AUTHORIZE_PATH_SUBSTITUE,
                    encodeURL(OAuth.OAUTH_CODE),
                    encodeURL(WEMP_RETURN_PATH.value()),
                    encodeURL(WEMP_ID.value()));
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

}
