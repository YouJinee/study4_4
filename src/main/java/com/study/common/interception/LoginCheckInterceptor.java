package com.study.common.interception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.study.login.vo.UserVO;

public class LoginCheckInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// mypage로 뭔가 유청이 왔는데 로그인이 되어있으면 mypage로 가고
		// 안되어있으면 login 페이지로 가도록 하자
		HttpSession session = request.getSession(false);
		System.out.println("request.getrequestURI"+request.getRequestURI());
		
		// getSession() getSession(true) getSession(false)
		// session이 없는 경우는 브라우저에서 맨~ 처음 서버로 요청할 때
		// = 브라우저가 쿠키 JSESSIONID가 없는 사애테서 서버 요청할 때

		String ajax = request.getHeader("X-requested-with");
		// myPage, 일반 로그인 체크
		if (session == null) {
			// 맨 처음 요청
			if (ajax != null) { // ajax 요청일 경우
				response.sendError(401, "로그인 안했어용");
				return false;
			}
			
			response.sendRedirect(request.getContextPath() + "/login/login.wow");
			return false;
		}

		UserVO user = (UserVO) session.getAttribute("USER_INFO");
		if (user == null) {
			if (ajax != null) { // ajax 요청일 경우
				//글 쓰던 내용 유지되게, 바로 freeView로 가게 하자.
				System.out.println("request.getrequestURI"+request.getRequestURI());
				session.setAttribute("RECONTENT", request.getParameter("reContent"));
				response.sendError(401, "로그인 안했어용");
				return false;
			}
			response.sendRedirect(request.getContextPath() + "/login/login.wow");
			return false;
		}

		return true;
	}
}
