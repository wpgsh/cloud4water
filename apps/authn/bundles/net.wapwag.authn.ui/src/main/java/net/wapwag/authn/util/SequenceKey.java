package net.wapwag.authn.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * reset passwd sequence util
 * @author gongll
 *
 */
public class SequenceKey {
	private static Set<String> resetKeys = new HashSet<String>();
	private static final long ONE_DAY_TIME = 86400000;
	public static String createResetKey(String userId)
	{
		StringBuffer key = new StringBuffer("");
		key.append(getNowTime());
		key.append(userId).append(getRandomKey());
		return key.toString();
	}
	
	public static void addResetKey(String key) {
		resetKeys.add(key);
	}
	
	public static boolean isExit(String key){
		if (resetKeys.contains(key)) {
			if (Long.parseLong(getCreateTime(key)) > getYesterDay()) {
				return true;
			}
			resetKeys.remove(key);
		}
		return false;
		
	}
	
	public static void cleanKey(String key){
		resetKeys.remove(key);
	}
	
	public static String getUserId(String key){
		return key.substring(13,key.length() - 10);
	}
	
	private static String getRandomKey()
	{
		StringBuffer random = new StringBuffer((int)(Math.random() * 100000));
		random.append((int)(Math.random() * 100000));
		while (random.length() < 10) {
			random.append("0");
		}
		return random.toString();
	}
	
	private static long getNowTime(){
		Date date = new Date();
		return date.getTime();
	}
	
	private static long getYesterDay(){
		Date date = new Date();
		return date.getTime() - ONE_DAY_TIME;
	}
	
	private static String getCreateTime(String key){
		return key.substring(0,13);
	}
	
	public static void main(String[] args) {
		System.out.println("14689856664761012773166017".substring(0,13));
	}
}
