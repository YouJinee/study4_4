package com.study.login.web;

import java.net.URLEncoder;

import javax.inject.Inject;
import javax.mail.Session;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.common.util.CookieUtils;
import com.study.login.ILoginService;
import com.study.login.LoginServiceImpl;
import com.study.login.vo.UserVO;
import com.study.member.vo.MemberVO;

@Controller
public class loginController {

	@Inject
	ILoginService loginService;

	String prePage = "";

	@GetMapping("/login/login.wow")
	public String loginGet(HttpServletRequest req) {
		prePage = req.getHeader("referer");
		System.out.println(prePage);
		return "login/login";
	}
	
	@PostMapping("/login/login.wow")
	public String login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
			// 사용자가 id, pass를 입력해서 로그인 버튼 누름
			String id = req.getParameter("userId");
			String pw = req.getParameter("userPass");
			String save_id = req.getParameter("rememberMe");
			if (save_id == null) {
				CookieUtils cookieUtils = new CookieUtils(req);
				if (cookieUtils.exists("SAVE_ID")) {
					Cookie cookie = CookieUtils.createCookie("SAVE_ID", id, "/", 0);
					resp.addCookie(cookie);
				}
				save_id = "";
			}

			if ((id == null || id.isEmpty()) || (pw == null || pw.isEmpty())) {
				// resp.sendRedirect("login.jsp?msg=" + URLEncoder.encode("입력안했어요", "utf-8"));
				// 직접 reDirect보다는 DS한테 나 redirect 하고싶어! 를 알려주는게 낫다
				return "redirect:" + "/login/login.wow?msg=" + URLEncoder.encode("입력안했어요", "utf-8");
			} else {
				UserVO user = loginService.getUser(id);

				if (user == null) {
					return "redirect:" + "/login/login.wow?msg=" + URLEncoder.encode("아이디 또는 비번확인", "utf-8");
				} else { // id맞았을때
					if (user.getUserPass().equals(pw)) {// 다 맞는경우
						if (save_id.equals("Y")) {
							resp.addCookie(CookieUtils.createCookie("SAVE_ID", id, "/", 3600 * 24 * 7));
						}
						HttpSession session = req.getSession();
						session.setAttribute("USER_INFO", user);
						session.setAttribute("prePage", prePage);
						// 댓글을 등록하려다 온 경우 로그인을 하고 나서 freeView로 까야함
						String sessionPage =(String) session.getAttribute("prePage");
						System.out.println(sessionPage);
						if (sessionPage.contains("freeView")) {
							return "redirect:" + session.getAttribute("prePage");
						}
						// request.getHeader("referer")는 freeView.wow로 가야함

						return "redirect:"+"/";
						// redirect: /study3
					} else {// 비번만 틀린경우
						return "redirect:" + "/login/login.wow?msg=" + URLEncoder.encode("아이디 또는 비번확인", "utf-8");
					}

				}
			}

	}

	@RequestMapping("/login/logout.wow")
	public String logout(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		HttpSession session = req.getSession();
		session.removeAttribute("USER_INFO");
		return "redirect:" + "/member/memberList.wow";
	}

}
