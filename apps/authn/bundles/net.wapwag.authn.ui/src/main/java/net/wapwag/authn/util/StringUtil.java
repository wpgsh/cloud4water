package net.wapwag.authn.util;

public class StringUtil {

	public static boolean isEmp(String str)
	{
		if (null == str || "".equals(str.trim())) {
			return true;
		}
		return false;
	}
}
