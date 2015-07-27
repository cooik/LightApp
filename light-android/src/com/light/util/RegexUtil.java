package com.light.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 
 * @author podongfeng
 * @date 2015年5月25日 下午9:35:03
 */
public class RegexUtil {

	public static boolean isMobileNo(String code) {
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		Matcher m = p.matcher(code);
		return m.matches();
	}
	
	public static boolean isEmail(String code) {
		Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher m = p.matcher(code);
		return m.matches();
	}
}
