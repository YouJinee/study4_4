package com.study.member.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.valid.step1;
import com.study.common.valid.step2;
import com.study.common.valid.step3;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizDuplicateKeyException;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.member.service.IMemberService;
import com.study.member.service.MailSendService;
import com.study.member.vo.MemberVO;



@Controller
@SessionAttributes("member")
public class MemberJoinController {
	//메소드 내에서 @modelAttirbute("member")는 session에 있는 model에 "member"가 있으면
	//새로 생성 안하고 있는거 사용함
	//없으면 에러
	//@SessionAttributes("member")는 Controller안에섬나 유지되는 session
	
	//step1.wow 처음 들어가기 전에 모델에 member 저장
	@ModelAttribute("member")
	public MemberVO member() {
		return new MemberVO();
	}
	@Inject
	IMemberService memberService;
	@Inject
	ICommCodeService codeService;
	
	@ModelAttribute("jobList")
	public List<CodeVO> jobList(){
		return codeService.getCodeListByParent("JB00");
	}
	@ModelAttribute("hobbyList")
	public List<CodeVO> hobbyList(){
		return codeService.getCodeListByParent("HB00");
	}
		@RequestMapping("/join/step1.wow")
		public String step1(@ModelAttribute("member") MemberVO member) {
			return"join/step1";
		}
		@RequestMapping("/join/step2.wow")
		public String step2(@ModelAttribute("member") @Validated(value = {step1.class}) MemberVO member
				,BindingResult error) {
			if(error.hasErrors()) {
				return "join/step1";
			}
			return"join/step2";
		}
		@RequestMapping("/join/step3.wow")
		public String step3(@ModelAttribute("member") @Validated(value = {step2.class}) MemberVO member, BindingResult error) {
			if(error.hasErrors()) {
				return "join/step2";
			}
			return"join/step3";
		}
		
		@RequestMapping("/join/regist.wow")
		public String regist(Model model, @ModelAttribute("member") @Validated(value = {step3.class}) MemberVO member, BindingResult error, SessionStatus sessionStatus) {
			if(error.hasErrors()) {
				return "join/step3";
			}
			try {
				memberService.registMember(member);
				sessionStatus.setComplete();
				ResultMessageVO resultMessageVO = new ResultMessageVO();
				model.addAttribute("resultMessageVO", resultMessageVO);
				resultMessageVO.messageSetting(true, "회원 등록 성공", "회원가입에 성공했습니다.", "/member/memberList.wow", "목록으로");
				return "common/message";
			} catch (BizDuplicateKeyException e) {
				ResultMessageVO resultMessageVO = new ResultMessageVO();
				model.addAttribute("resultMessageVO", resultMessageVO);
				resultMessageVO.messageSetting(false, "이미 등록된 ID", "ID가 이미 등록되었습니다.", "/member/memberList.wow", "목록으로");
				return "common/message";
			} catch (BizNotEffectedException e) {
				ResultMessageVO resultMessageVO = new ResultMessageVO();
				model.addAttribute("resultMessageVO", resultMessageVO);
				resultMessageVO.messageSetting(false, "회원 등록 실패", "회원 등록에 실패했습니다.", "/member/memberList.wow", "목록으로");
				return "common/message";
			}
			//step3에서 입력한 데이터만 있고, step1, 2에서 입력한 값은 저장이 안되어있음
			//step1, 2에서 입력한 값이 계속 유지가 되어야한다. 어떻게 해야할까?
		}
		
		@RequestMapping("/join/cancel")
		public String cancel(SessionStatus sessiontStatus) {
			sessiontStatus.setComplete();
			return "home";
		}
		
		@Autowired
		MailSendService mailSendService;
		@RequestMapping("/join/emailSend.wow")
		@ResponseBody //return 값으로 jsp를 찾는게 아니라 요청 source(ajax)에 데이터를 전달해줌
		public String emailSend(String emailAdd) {
			//이메일을 보내보자!!!!!! mailSendService를 실행하면 되겠져?
			String randomKey = mailSendService.sendAuthMail(emailAdd);
			return randomKey;
		}
	
		@RequestMapping("/join/checkId.wow")
		@ResponseBody
		public String checkId(String insertId) throws BizNotFoundException {
			try{
				MemberVO member = memberService.getMember(insertId);
			}catch(BizNotFoundException e){
				return "Y";
			}
			return "N";
		}
		
}
