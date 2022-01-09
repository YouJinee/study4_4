package com.study.reply.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.study.common.vo.PagingVO;

//검색보다는 더보기 누를때마다 10개씩 추가하는 용도
//어떤 글으 ㅣ댓글인지 확인하기 위한 용도
public class ReplySearchVO extends PagingVO{
	
	private String reCategory;
	private int reParentNo;
	
	public String getReCategory() {
		return reCategory;
	}
	public void setReCategory(String reCategory) {
		this.reCategory = reCategory;
	}
	public int getReParentNo() {
		return reParentNo;
	}
	public void setReParentNo(int reParentNo) {
		this.reParentNo = reParentNo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
