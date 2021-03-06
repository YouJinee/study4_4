package com.study.common.interception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.study.login.vo.UserVO;

public class ManagerCheckInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		if(session == null) {
			response.sendRedirect(request.getContextPath()+"/login/login.wow");
			return false;
		}
		
		UserVO user = (UserVO) session.getAttribute("USER_INFO");
		if(user == null) {
			response.sendRedirect(request.getContextPath()+"/login/login.wow");
			return false;
		}
		
		if(!user.getUserRole().equals("MANAGER")) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "매니저만 접근 가능합니다");
			return false;
		}
		
		return true;
	}
}
