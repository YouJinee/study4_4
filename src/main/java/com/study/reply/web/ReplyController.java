package com.study.reply.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.exception.BizAccessFailException;
import com.study.exception.BizNotFoundException;
import com.study.reply.service.IReplyService;
import com.study.reply.vo.ReplySearchVO;
import com.study.reply.vo.ReplyVO;


@RestController 
public class ReplyController {
	
	@Inject
	IReplyService replyService;
	
	@RequestMapping(value="/reply/replyList", produces="application/json; charset = utf-8")
	public Map<String, Object> replyList(ReplySearchVO searchVO){
		List<ReplyVO> replyList = replyService.getReplyListByParent(searchVO);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", true);
		map.put("data", replyList);
		map.put("size", replyList.size());
		return map;
	}
	@RequestMapping(value="/reply/replyRegist", produces="application/json; charset = utf-8")
	public Map<String, Object> replyRegist(ReplyVO reply){
		replyService.registReply(reply);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", true);
		map.put("msg", "등록했어염");
		return map;
	}
	
	@RequestMapping(value = "/reply/replyModify", produces = "application/json; charset=utf-8")
	public Map<String, Object> replyModify(ReplyVO reply){
			Map<String, Object> map = new HashMap<String, Object>();
		try {
			replyService.modifyReply(reply);
			map.put("result", true);
			map.put("msg", "수정성공");
			
		} catch (BizNotFoundException e) {
			map.put("result", false);
			map.put("msg", "댓글 작성자만 수정할 수 있습니다.");
			e.printStackTrace();
		} catch (BizAccessFailException e) {
			// TODO Auto-generated catch block
			map.put("result", false);
			map.put("msg", "댓글이 없습니다.");
		}
		return map;
	}
	@RequestMapping(value = "/reply/replyDelete", produces = "application/json; charset=utf-8")
	public Map<String, Object> replyDelete(ReplyVO reply){
		Map<String, Object> map = new HashMap<String, Object>();
	try {
		replyService.removeReply(reply);
		map.put("result", true);
		map.put("msg", "삭제성공");
		
	} catch (BizNotFoundException e) {
		map.put("result", false);
		map.put("msg", "댓글 작성자만 삭제할 수 있습니다.");
		e.printStackTrace();
	} catch (BizAccessFailException e) {
		// TODO Auto-generated catch block
		map.put("result", false);
		map.put("msg", "댓글이 없습니다.");
	}
	return map;
}
}
