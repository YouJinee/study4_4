package com.study.member.web;

import java.util.List;

import javax.inject.Inject;
import javax.validation.groups.Default;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.valid.Modify;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizDuplicateKeyException;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.member.service.IMemberService;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;
import com.sun.scenario.effect.Blend.Mode;

@Controller
public class memberController {

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

	@RequestMapping("/member/memberList.wow")
	public String memberList(Model model, @ModelAttribute("searchVO") MemberSearchVO searchVO) {
		List<MemberVO> memberList = memberService.getMemberList(searchVO);
		model.addAttribute("memberList", memberList);

		return "member/memberList";
	}

	@RequestMapping("/member/memberView.wow")
	public String memberView(Model model, @RequestParam(required = true) String memId) {
		try {
			MemberVO member = memberService.getMember(memId);
			model.addAttribute("member", member);
		} catch (BizNotFoundException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "회원 찾기 실패", "회원을 찾는데 실패했습니다.", "/member/memberList.wow", "목록으로");
			return "common/message";
		}
		return "member/memberView";
	}

	@RequestMapping("/member/memberEdit.wow")
	public String memberEdit(Model model, @RequestParam(required = true) String memId) {
		try {
			MemberVO member = memberService.getMember(memId);
			model.addAttribute("member", member);
			return "member/memberEdit";
		} catch (BizNotFoundException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "회원 찾기 실패", "회원을 찾는데 실패했습니다.", "/member/memberList.wow", "목록으로");
			return "common/message";
		}
	}

	@RequestMapping(value = "/member/memberModify.wow", method = RequestMethod.POST)
	public String memberModify(Model model, @ModelAttribute("member") @Validated(value = {Default.class, Modify.class})MemberVO member,
					BindingResult error) {
		if(error.hasErrors()) {
			return "member/memberEdit";
		}
		try {

			memberService.modifyMember(member);
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(true, "회원 수정 성공", "회원을 수정했습니다.", "/member/memberList.wow", "목록으로");
			return "common/message";
		} catch (BizNotFoundException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "회원 찾기 실패", "회원을 찾는데 실패했습니다.", "/member/memberList.wow", "목록으로");
			return "common/message";
		} catch (BizNotEffectedException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "회원 수정 실패", "회원을 수정하는데 실패했습니다.", "/member/memberList.wow", "목록으로");
			return "common/message";
		}
	}

	@RequestMapping("/member/memberForm.wow")
	public String memberForm(Model model) {
		return "member/memberForm";
	}

	@RequestMapping(value = "/member/memberRegist.wow", method = RequestMethod.POST)
	public String memberRegist(Model model, MemberVO member) {
		model.addAttribute("member", member);
		try {
			memberService.registMember(member);
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(true, "글 등록 성공", "글을 수정했습니다.", "/member/memberList.wow", "목록으로");
			return "common/message";
		} catch (BizDuplicateKeyException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "비밀번호 불일치 실패", "비밀번호가 불일치합니다.", "/member/memberList.wow", "목록으로");
			return "common/message";
		} catch (BizNotEffectedException e) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "회원 등록 실패", "회원 등록에 실패했습니다.", "/member/memberList.wow", "목록으로");
			return "common/message";
		}
	}
}
