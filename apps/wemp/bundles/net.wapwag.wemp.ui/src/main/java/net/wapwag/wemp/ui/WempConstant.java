package net.wapwag.wemp.ui;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.wapwag.wemp.WempUtil.encodeBase64String;
import static net.wapwag.wemp.WempUtil.encodeURL;

/**
 * Wemp constant
 * Created by Administrator on 2016/11/14 0014.
 */
final class WempConstant {

    private static final Logger logger = LoggerFactory.getLogger(WempConstant.class);

    private static final String WEMP_ID = "wemp";

    private static final String WEMP_SECRET = "wemp_secret";

    private static final String WEMP_BAISC_ORIGIN = String.format("%s:%s", WEMP_ID, WEMP_SECRET);

    private static final String WEMP_BAISC_ENCODED_ORIGIN = encodeBase64String(WEMP_BAISC_ORIGIN);

    static final String WEMP_BASIC_ENCODED_CREDENTIAL = String.format("Basic %s", WEMP_BAISC_ENCODED_ORIGIN);

    static final String WEMP_RETURN_PATH = "http://10.10.22.52:8181/wemp/return";

    static final String WEMP_ERROR_PATH = "http://10.10.22.52:8181/authn/error/503.html";

    /**
     * The path for /authorize.
     */
    static String AUTHORIZE_PATH;
    private static final String AUTHORIZE_PATH_SUBSTITUE = "/authn/authorize?response_type=%s&redirect_uri=%s&client_id=%s";

    static {
        try {
            // Encode url parameter value
            AUTHORIZE_PATH = String.format(AUTHORIZE_PATH_SUBSTITUE,
                    encodeURL(OAuth.OAUTH_CODE),
                    encodeURL(WEMP_RETURN_PATH),
                    encodeURL(WEMP_ID));
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

}
