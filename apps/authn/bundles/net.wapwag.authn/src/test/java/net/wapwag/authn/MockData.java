package net.wapwag.authn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * mock data
 * Created by Administrator on 2016/11/7 0007.
 */
@SuppressWarnings("Duplicates")
class MockData {

    static final long clientId = 1L;
    static final long objId = 1L;
    static final long userId = 1L;
    static final long groupId = 1L;
    static final long orgId = 1L;
    static final long invalidId = 0L;

    static final int count = 5;
    static final int addCount = 1;
    static final int removeCount = 1;
    static final int updateCount = 1;

    static final String redirectURI = "http://wwww.baidu.com";
    static final String handle = "testToken";
    //Use base64 encoding handle:testToken
    static final String encodeHandle = "dGVzdFRva2Vu";
    static final String code = "testCode";
    static final String clientIdentity = "clientId";
    static final String clientSecret = "clientSecret";
    static final String scope = "user:name user:avatar";
    static final String nonAuthzedscope = "user:email";
    static final String action = "read";
    static final String invalidString = "invalid_string";
    static final String invalidEncodeHanlde = "aW52YWxpZF9zdHJpbmc=";

    
}
