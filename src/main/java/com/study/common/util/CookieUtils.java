package com.study.common.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class CookieUtils {
//생성자,exist, getValue, getCookie  , createCookie
	
	private Map<String,Cookie> cookieMap=
			new HashMap<String,Cookie>();
	
	//request에있는 모든쿠키들을 맵에다 저장 
	public CookieUtils(HttpServletRequest request) {
		Cookie[] cookies= request.getCookies();
		if(cookies !=null) {
			for(Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
	}
	
	// name에 해당하는 쿠키가 있냐 없냐? 있으면 true
	public boolean exists(String name) {
		Cookie cookie=cookieMap.get(name);
		return cookie!=null;
	}
	
	//name이라는 이름의 쿠키의 값 
	// 쿠키유틸 사용하는 곳에서 null체크 알아서 하도록 하자 
	public String getValue(String name) {
		return cookieMap.get(name).getValue(); 	
	}
	//name의 쿠키 
	public Cookie getCookie(String name) {
		return cookieMap.get(name);
	}
	
	
	public static Cookie createCookie(String name,String value) {
		return createCookie(name,value,"/","",-1);
	}
	public static Cookie createCookie(String name,String value,int maxAge)
	{
		return createCookie(name,value,"/","",maxAge);
	}
	public static Cookie createCookie(String name,String value,String path)
	{
		return createCookie(name,value,path,"",-1);
	}
	public static Cookie createCookie(String name,String value,String path,int maxAge)
	{
		return createCookie(name,value,path,"",maxAge);
	}
	public static Cookie createCookie
	(String name,String value, String path,String domain,int maxAge) {
		Cookie cookie= new Cookie(name,value);
		cookie.setDomain(domain); cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		return cookie;
	}
	
	
}
