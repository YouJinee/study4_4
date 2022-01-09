package com.study.mypage.web;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.valid.Modify;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.login.vo.UserVO;
import com.study.member.service.IMemberService;
import com.study.member.vo.MemberVO;



@Controller
public class myPageController {

	@Inject
	IMemberService memberService;
	@Inject
	ICommCodeService codeService;

	@RequestMapping("/mypage/info.wow")
	public String Info(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		HttpSession session = req.getSession();
		UserVO user = (UserVO) session.getAttribute("USER_INFO");
		// 로그인 됨, user로 member테이블 조회하고 -- 개인정보화면으로 이동
		try {
			MemberVO member = memberService.getMember(user.getUserId());
			model.addAttribute("member", member);
			return "mypage/info";
		} catch (BizNotFoundException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "회원없음", "회원이 없습니다.", "/", "홈으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
	}

	@RequestMapping("/mypage/edit.wow")
	public String edit(Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {

		HttpSession session = req.getSession();
		UserVO user = (UserVO) session.getAttribute("USER_INFO");
		try {
			MemberVO member = memberService.getMember(user.getUserId());
			req.setAttribute("member", member);
			List<CodeVO> jobList = codeService.getCodeListByParent("JB00");
			List<CodeVO> hobbyList = codeService.getCodeListByParent("HB00");

			req.setAttribute("jobList", jobList);
			req.setAttribute("hobbyList", hobbyList);
			return "mypage/edit";
		} catch (BizNotFoundException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "회원없음", "회원이 없습니다.", "/", "홈으로");
			req.setAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
	}
	@RequestMapping("/mypage/modify.wow")
	public String modify(Model model, @ModelAttribute("member") @Validated(value = {Modify.class}) MemberVO member, HttpServletRequest req, HttpServletResponse resp
			, BindingResult error) throws Exception {
		if(error.hasErrors()) {
			return "mypage/edit";
		}
		try {
			memberService.modifyMember(member);
			return "redirect:"+"/";
		}catch (BizNotEffectedException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "수정 실패","내 정보 수정 실패.", "/", "홈으로");
			req.setAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}catch (BizNotFoundException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "정보없음","내 정보가 없습니다.", "/", "홈으로");
			req.setAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
	}
}
