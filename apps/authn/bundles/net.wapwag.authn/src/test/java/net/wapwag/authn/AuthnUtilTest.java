package net.wapwag.authn;

import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class AuthnUtilTest {

    @Test
    public void encryptAES() {
        String orginalText = "12345678901234567890123456789012";
        String cryptograph = AuthnUtil.encryptAES(orginalText);

        assertEquals("ciYpL8Vu5LUhuXpeh7uUKumsRbe9ratZfEs4STD/fhsC/dJQLtPC2vJtbN6AhT8I", cryptograph);
    }

    @Test
    public void encodeBase64String_withoutPadding() {
        String str = "abcd";
        String encodedStr = AuthnUtil.encodeBase64String(str);

        assertEquals("YWJjZA", encodedStr);

        String decoderStr = new String(Base64.getDecoder().decode("YWJjZA"));

        assertEquals(str, decoderStr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeBase64String_illegalArgument() {
        AuthnUtil.encodeBase64String("");
    }

    @Test
    public void encodeBase64String() {
        String str = "abc";
        String encodedStr = AuthnUtil.encodeBase64String(str);

        assertEquals("YWJj", encodedStr);
    }

    @Test
    public void encodeBase64Byte_withoutPadding() {
        String str = "abcd";
        String encodedStr = AuthnUtil.encodeBase64Byte(str.getBytes());

        assertEquals("YWJjZA", encodedStr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeBase64Byte_illegalArgument() {
        AuthnUtil.encodeBase64Byte("".getBytes());
    }

    @Test
    public void encodeBase64Byte() {
        String str = "abc";
        String encodedStr = AuthnUtil.encodeBase64Byte(str.getBytes());

        assertEquals("YWJj", encodedStr);
    }
    
}
