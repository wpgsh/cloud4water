package net.wapwag.wemp.ui;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

/**
 * Wemp constant
 * Created by Administrator on 2016/11/14 0014.
 */
final class WempConstant {

    static final String WEMP_ID = "wemp";

    static final String WEMP_STATE = "wpg/wemp";

    static final String SWM_STATE = "wpg/swm";

    private static final String WEMP_SECRET = "wemp_secret";

    private static final String WEMP_BAISC_ORIGIN = String.format("%s:%s", WEMP_ID, WEMP_SECRET);

    private static final String WEMP_BAISC_ENCODED_ORIGIN = Base64.encodeBase64String(WEMP_BAISC_ORIGIN.getBytes());

    static final String WEMP_BASIC_ENCODED_CREDENTIAL = String.format("Basic %s", WEMP_BAISC_ENCODED_ORIGIN);

    static final String WEMP_RETURN_PATH = "http://10.10.22.52:8181/wemp/return";

    static final String WEMP_ERROR_PATH = "http://www.baidu.com";

}
