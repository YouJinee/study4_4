package com.study.free.web;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.groups.Default;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.study.attach.dao.IAttachDao;
import com.study.attach.vo.AttachVO;
import com.study.code.service.CommCodeServiceImpl;
import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.util.StudyAttachUtils;
import com.study.common.valid.Modify;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.service.FreeBoardServiceImpl;
import com.study.free.service.IFreeBoardService;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

@Controller
public class FreeController {
	// 데이터 저장 아닌 단순 기능이면 객체를 한개만 만드는게 좋다.
	// singleton 패턴 말고 spring이 객체 한개 만들면 스프링이 만든 객체(빈) 계속 사용
	private Logger logger = LoggerFactory.getLogger(FreeController.class);
	
	@Inject
	IFreeBoardService freeBoardService;
	@Autowired // inject랑 똑같다!
	ICommCodeService codeService;

	@Autowired
	StudyAttachUtils studyAttachUtils;
	
	
	@RequestMapping("/free/freeList.wow")
	public String freeList(Model model, @ModelAttribute("searchVO") FreeBoardSearchVO searchVO, HttpServletRequest req) {
		logger.debug("serarchVO = {}", searchVO);
		HttpSession session = req.getSession();
		session.removeAttribute("RECONTENT");
		// 파라미터들이 searchVO의 필드이면 알아서 다 세팅 + model.addAttribute("searchVO")
		List<FreeBoardVO> freeBoardList = freeBoardService.getBoardList(searchVO);
		model.addAttribute("freeBoardList", freeBoardList);

		List<CodeVO> cateList = codeService.getCodeListByParent("BC00");
		model.addAttribute("freeBoardList", freeBoardList);

		return "free/freeList";
	}

	@RequestMapping("/free/freeView.wow")
	public String freeView(Model model, @RequestParam(required = true) int boNo) {
		try {
			FreeBoardVO freeBoard = freeBoardService.getBoard(boNo);
			model.addAttribute("freeBoard", freeBoard);
			freeBoardService.increaseHit(boNo);
			
		} catch (BizNotFoundException enf) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "글 찾기 실패", "글을 찾는데 실패했습니다. 해당되는 글이 없습니다.", "/free/freeList.wow",
					"목록으로");
			return "common/message";
			// 에러가 났을 때 freeView에 있는 너무 간단한 화면 말고 message.jsp로 이동하자
		} catch (BizNotEffectedException ene) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "조회수 증가 실패", "조회수 증가 실패했습니다.", "/free/freeList.wow", "목록으로");
		}
		return "free/freeView";
	}

	@RequestMapping("/free/freeEdit.wow")
	public String freeEdit(Model model, @RequestParam(required = true) int boNo) {
		try {
			FreeBoardVO freeBoard = freeBoardService.getBoard(boNo);
			model.addAttribute("freeBoard", freeBoard);
			freeBoardService.increaseHit(boNo);
			List<CodeVO> cateList = codeService.getCodeListByParent("BC00");
			model.addAttribute("cateList", cateList);
			return "free/freeEdit";
		} catch (BizNotFoundException enf) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "글 조회 실패", "글 조회 실패.", "/free/freeList.wow", "목록으로");
			return "common/message";
		} catch (BizNotEffectedException ene) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "글 삭제 실패", "글 삭제 실패.", "/free/freeList.wow", "목록으로");
			return "common/message";
		}
	}

	
	//검색결과 객체(Binding error)는 검사 객체(FreeBoardVO) 바로 뒤에 있어야한다.
	// @PostMapping("free/freeModify.wow") 아래는 얘랑 같다
	@RequestMapping(value = "/free/freeModify.wow", method = RequestMethod.POST)
	public String freeModify(Model model, @ModelAttribute("freeBoard") @Validated(value = {Modify.class, Default.class}) FreeBoardVO freeBoard, BindingResult error
			, @RequestParam(value = "boFiles", required = false)MultipartFile[] boFiles) throws IOException {
		logger.debug("serarchVO = {}", freeBoard);
		if (error.hasErrors()) {
			return"free/freeEdit";
		}
		try {
			
			if(boFiles!=null) {
				List<AttachVO> attaches = studyAttachUtils.getAttachListByMultiparts(boFiles, "FREE", "free");
				//실제 파일경로에 선택된 파일 올리고 List<AttachVO> 리턴 
				freeBoard.setAttaches(attaches);
			}
			
			freeBoardService.modifyBoard(freeBoard);
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(true, "글 수정 성공", "글을 수정했습니다.", "/free/freeList.wow", "목록으로");
			return "common/message";
		} catch (BizNotFoundException enf) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "글 찾기 실패", "글을 찾는데 실패했습니다. 해당되는 글이 없습니다.", "/free/freeList.wow",
					"목록으로");
			return "common/message";
		} catch (BizPasswordNotMatchedException epm) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "글 수정 실패", "글 수정 실패했습니다.", "/free/freeList.wow", "목록으로");
			return "common/message";
		} catch (BizNotEffectedException ene) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "조회수 증가 실패", "조회수 증가 실패했습니다.", "/free/freeList.wow", "목록으로");
			return "common/message";
		}
	}

//	private static boolean checkModify(FreeBoardVO freeBoard) {
//		boolean isError = false;
//		if (freeBoard.getBoNo() == 0)
//			isError = true;
//		if (freeBoard.getBoTitle().isEmpty())
//			isError = true;
//			isError = true;
//			if (!freeBoard.getBoContent().contains("한창희"))
//		// .... 하나하나 다 만들기 힘들다, checkRegist도 만들기 귀찮다.
//		// spring이 @으로 쉽게 할 수 있도록 제공해준다. --> spring validation
//		return isError;
//	}

	@RequestMapping(value = "/free/freeForm.wow")
	public String freeForm(Model model, @ModelAttribute("freeBoard") FreeBoardVO freeBoard) {
		//freeForm.wow올 때 파라미터가 1도 없습니다 슨생님
		//파라미터가 없을 때 @modelAttribute 쓰면
		//FreeBoardVO 생성해서 model에 "freeBoard"라는 이름으로 저장
		List<CodeVO> cateList = codeService.getCodeListByParent("BC00");
		model.addAttribute("cateList", cateList);
		return "free/freeForm";
	}
	//boNo가 파라미터로 안온다 -> int라서 값이 0
	@RequestMapping(value = "/free/freeRegist.wow", method = RequestMethod.POST)
	public String freeRegist(Model model, @ModelAttribute("freeBoard") @Validated(Default.class) FreeBoardVO freeBoard
			,BindingResult error, @RequestParam(value = "boFiles", required = false)MultipartFile[] boFiles) throws IOException {
		//freeVO를 DB에 넣고, AttachVO도 DB에 넣고, 실제 파일 경로에 선택된 파일도 올려야한다.
		//AttachTable에 집어넣을 때 parentNo가 분명히 있다.
		//근데 freeRegist에는 boNo가 파라미터로 안넘어온다. 그럼 어떻게 attachVO에 parentNo를 쓸까?
		logger.debug("serarchVO = {}", freeBoard);
		logger.debug("boFiles = {}", boFiles);
		List<CodeVO> cateList = codeService.getCodeListByParent("BC00");
		if(error.hasErrors()) {
			return "free/freeForm";
		}
		try {
			if(boFiles!=null) {
				List<AttachVO> attaches = studyAttachUtils.getAttachListByMultiparts(boFiles, "FREE", "free");
				//실제 파일경로에 선택된 파일 올리고 List<AttachVO> 리턴 
				freeBoard.setAttaches(attaches);
			}
			freeBoardService.registBoard(freeBoard);
			//freeBoard.getBoNo를 하면 6146이 세팅, 왜냐면 mybatis가 seq한 값을 boNo에 자동세팅 select key
		
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(true, "글 등록 성공", "글을 수정했습니다.", "/free/freeList.wow", "목록으로");
			return "common/message";
		} catch (BizNotEffectedException ebe) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			model.addAttribute("resultMessageVO", resultMessageVO);
			resultMessageVO.messageSetting(false, "조회수 증가 실패", "조회수 증가 실패했습니다.", "/free/freeList.wow", "목록으로");
			return "common/message";
		}
	}

}
