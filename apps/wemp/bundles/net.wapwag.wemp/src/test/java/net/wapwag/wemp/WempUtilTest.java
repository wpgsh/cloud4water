package net.wapwag.wemp;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import static org.junit.Assert.*;

public class WempUtilTest {

    @Test
    public void encryptAES() {
        String orginalText = "12345678901234567890123456789012";
        String cryptograph = WempUtil.encryptAES(orginalText);

        assertEquals("zpAGvV1+3mcDYDZsT7vFQoUkA3DXHbzTjHB4nB9xFd45ntbT5464nPB2ts/xd6lv", cryptograph);
    }

    @Test
    public void encodeBase64String_withoutPadding() {
        String str = "abcd";
        String encodedStr = WempUtil.encodeBase64String(str);

        assertEquals("YWJjZA", encodedStr);

        String decoderStr = new String(Base64.getDecoder().decode("YWJjZA"));

        assertEquals(str, decoderStr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeBase64String_illegalArgument() {
        WempUtil.encodeBase64String("");
    }

    @Test
    public void encodeBase64String() {
        String str = "abc";
        String encodedStr = WempUtil.encodeBase64String(str);

        assertEquals("YWJj", encodedStr);
    }

    @Test
    public void encodeBase64Byte_withoutPadding() {
        String str = "abcd";
        String encodedStr = WempUtil.encodeBase64Byte(str.getBytes());

        assertEquals("YWJjZA", encodedStr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeBase64Byte_illegalArgument() {
        WempUtil.encodeBase64Byte("".getBytes());
    }

    @Test
    public void encodeBase64Byte() {
        String str = "abc";
        String encodedStr = WempUtil.encodeBase64Byte(str.getBytes());

        assertEquals("YWJj", encodedStr);
    }

    @Test
    public void encodeURL() {
        String orginalText = "state=wpg/swm";

        assertEquals("state%3Dwpg%2Fswm", WempUtil.encodeURL(orginalText));
    }

    @Test
    public void encodeURL_emptyValue() throws UnsupportedEncodingException {
        assertEquals("", WempUtil.encodeURL(null));
        assertEquals("", WempUtil.encodeURL(""));
    }

}
