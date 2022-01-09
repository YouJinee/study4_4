package com.study.free.service;

import java.util.List;

import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.dao.FreeBoardDaoOracle;
import com.study.free.dao.IFreeBoardDao;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

public class FreeBoardServiceImpl implements IFreeBoardService {

	/*
	 * private FreeBoardServiceImpl() { }
	 * 
	 * private static FreeBoardServiceImpl freeBoardService = new
	 * FreeBoardServiceImpl();
	 * 
	 * public FreeBoardServiceImpl getInstace() { return freeBoardService; }
	 */
	IFreeBoardDao freeBoardDao = new FreeBoardDaoOracle();

	@Override
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO ) {
		//searchVO는 curPage, rowSizePerPage, 
		//searchVO는 list.jsp에서 파라미터로 넘어온 값인데
		//totalRowCOunt는 파라미터로 안 넘어와요.
		int totalRowCount = freeBoardDao.getTotalRowCount(searchVO);
		searchVO.setTotalRowCount(totalRowCount);
		searchVO.pageSetting();
		
		//curPage, rowSizePerPage, firstRow, lastRow, totalRowCount 등등 전부 세팅 가능
		return freeBoardDao.getBoardList(searchVO);
	}

	@Override
	public FreeBoardVO getBoard(int boNo) throws BizNotFoundException {
		FreeBoardVO freeBoard = freeBoardDao.getBoard(boNo);
		// freeBoard가 null (글 번호를 사용자가 url에 이상하게 입력한 경우)
		if (freeBoard == null) {
			throw new BizNotFoundException();
		}
		return freeBoard;
	}

	@Override
	public void increaseHit(int boNo) throws BizNotEffectedException {
		int cnt = freeBoardDao.increaseHit(boNo);
		if (cnt == 0) { // 업데이트가 제대로 안됨. 근데 사실 일어날 수 없는 일임
			throw new BizNotEffectedException();
		}

	}

	@Override
	public void modifyBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		// freeBoard.getBoPass : 입력한 비번
		// freeBoard.getBoNo : edit 에서 넘어온 데이터, 수정할 글 번호
		FreeBoardVO vo = freeBoardDao.getBoard(freeBoard.getBoNo());// vo는 DB에 있는 데이터
		if (vo == null)
			throw new BizNotFoundException(); // 글번호에 맞는 데이터가 데베에 X

		if (freeBoard.getBoPass().equals(vo.getBoPass())) {
			// 사용자가 입력한 비밀번호랑 해당 글의 현재 DB에 있는 비번이 같다.
			// ---> 본인이다 ---> 수정해도 된다.
			int cnt = freeBoardDao.updateBoard(freeBoard);
			if (cnt == 0)
				throw new BizNotEffectedException();
		} else { // 비번이 다른 경우
			throw new BizPasswordNotMatchedException();
		}
	}

	@Override
	public void removeBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		int cnt = freeBoardDao.insertBoard(freeBoard);
		if (cnt == 0)
			throw new BizNotEffectedException();
	}

}
