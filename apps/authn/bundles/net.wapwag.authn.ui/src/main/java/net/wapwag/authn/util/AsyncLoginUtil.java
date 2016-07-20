package net.wapwag.authn.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class AsyncLoginUtil {
	private static Map<String, Long> modUsersMap = new HashMap<String, Long>();
	private static final long ONE_DAY_TIME = 86400000;
	public static boolean isRun = false;
	
	public static void addModeTime(String userName)
	{
		modUsersMap.put(userName, getNowTime());
	}
	
	public static boolean checkSession(HttpSession session){
		String userName = (String)session.getAttribute("userName");
		long modTime = getModeTime(userName);
		if (0L < modTime) {
			long loginTime = (long)session.getAttribute("loginTime");
			if (loginTime < modTime) {
				session.setAttribute("userName", "");
				session.setAttribute("authenticated", "");
				session.setAttribute("userId", "");
				session.setAttribute("loginTime", "");
				return false;
			}
		}
		return true;
	} 
	
	private static long getModeTime(String userName){
		if (null == modUsersMap.get(userName)) {
			return 0L;
		}
		return modUsersMap.get(userName);
	}
	
	private static long getNowTime(){
		Date date = new Date();
		return date.getTime();
	}
	
	private static long getYesterDay(){
		Date date = new Date();
		return date.getTime() - ONE_DAY_TIME;
	}
	
	private static void cleanMap(){
		Iterator<String> iterator = modUsersMap.keySet().iterator();
		long yesterDay = getYesterDay();
		String userName= "";
		while (iterator.hasNext()) {
			userName = iterator.next();
			if (modUsersMap.get(userName) < yesterDay) {
				modUsersMap.remove(userName);
			}
		}
	}
}
