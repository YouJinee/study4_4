package com.study.free.service;

import java.util.List;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.study.attach.dao.IAttachDao;
import com.study.attach.vo.AttachVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.dao.IFreeBoardDao;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

@Service
public class FreeBoardServiceImpl implements IFreeBoardService {

	@Inject
	IFreeBoardDao freeBoardDao;
	@Inject
	IAttachDao attachDao;

	@Override
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO) {
		int totalRowCount = freeBoardDao.getTotalRowCount(searchVO);
		searchVO.setTotalRowCount(totalRowCount);
		searchVO.pageSetting();
		return freeBoardDao.getBoardList(searchVO);
	}

	@Override
	public FreeBoardVO getBoard(int boNo) throws BizNotFoundException {
		FreeBoardVO freeBoard = freeBoardDao.getBoard(boNo); // freeBoard에는 freeBoard 테이블에서 얻은것만 세팅
		if (freeBoard == null) {
			throw new BizNotFoundException();
		}
//		AttachVO attach = new AttachVO();
//		attach.setAtchCategory("FREE");
//		attach.setAtchParentNo(boNo);
//		List<AttachVO> attaches = attachDao.getAttachByParentNoList(attach);
//		freeBoard.setAttaches(attaches);
		return freeBoard;
	}

	@Override
	public void increaseHit(int boNo) throws BizNotEffectedException {
		int cnt = freeBoardDao.increaseHit(boNo);
		if (cnt == 0) {
			throw new BizNotEffectedException();
		}
	}

	@Override
	public void modifyBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		FreeBoardVO vo = freeBoardDao.getBoard(freeBoard.getBoNo());// vo는 DB에있는 데이터
		if (vo == null)
			throw new BizNotFoundException(); // 글번호에 맞는 데이터가 데베에

		if (freeBoard.getBoPass().equals(vo.getBoPass())) {
			int cnt = freeBoardDao.updateBoard(freeBoard);
			if (cnt == 0)
				throw new BizNotEffectedException();
			List<AttachVO> attaches = freeBoard.getAttaches();
			if (attaches != null) {
				for (AttachVO attach : attaches) {
					attach.setAtchParentNo(freeBoard.getBoNo());
					attachDao.insertAttach(attach);
				} // 수정에서 추가한 첨부파일들 db에 넣기
			}
			// 휴지통 버튼 누른 첨부파일들 DB에서 삭제
			int delAtchNo[] = freeBoard.getDelAtchNos(); // 애초에 파라미터로 올 때 세팅이 되어있음
			if (delAtchNo != null) {
				attachDao.deleteAttaches(delAtchNo); // 안만들었옹
			}

		} else {
			throw new BizPasswordNotMatchedException();
		}
	}

	@Override
	public void removeBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {

		FreeBoardVO vo = freeBoardDao.getBoard(freeBoard.getBoNo());
		if (vo == null)
			throw new BizNotFoundException();
		if (freeBoard.getBoPass().equals(vo.getBoPass())) {
			int cnt = freeBoardDao.deleteBoard(freeBoard);
			if (cnt == 0)
				throw new BizNotEffectedException();
		} else { // 비번이 다른 경우
			throw new BizPasswordNotMatchedException();
		}
	}

	@Override
	public void registBoard(FreeBoardVO freeBoard) throws BizNotEffectedException {
		int cnt = freeBoardDao.insertBoard(freeBoard); // 여기를 지난 후에는 freeboard에 boNo 세팅
		if (cnt == 0)
			throw new BizNotEffectedException();
		List<AttachVO> attaches = freeBoard.getAttaches();
		if (attaches != null) {
			for (AttachVO attach : attaches) {
				attach.setAtchParentNo(freeBoard.getBoNo());
				attachDao.insertAttach(attach);
			}
		}
	}

}
