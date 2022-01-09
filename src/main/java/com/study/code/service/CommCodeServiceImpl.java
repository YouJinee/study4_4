package com.study.code.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import com.study.code.dao.ICommCodeDao;
import com.study.code.vo.CodeVO;

@Service
public class CommCodeServiceImpl implements ICommCodeService {
	@Inject
	ICommCodeDao code;

	@Override
	public List<CodeVO> getCodeListByParent(String parentCode) {
		return code.getCodeListByParent(parentCode);
	}
}
