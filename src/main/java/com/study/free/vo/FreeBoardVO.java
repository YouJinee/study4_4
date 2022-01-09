package com.study.free.vo;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.study.attach.vo.AttachVO;
import com.study.common.valid.Modify;

public class FreeBoardVO {
	//in에는 notEmpty notBlank 안돼용
	@Positive(message = "글 번호는 필수입니다.", groups = {Modify.class})
	private int boNo;                       /* 글 번호 */
	@NotEmpty(message = "글 제목은 필수입니다.")
	private String boTitle;                 /* 글 제목 */
	@NotEmpty (message = "글 카테고리는 필수입니다.")
	private String boCategory;
	@Size(min = 1, max = 10)
	private String boWriter;                /* 작성자명 */
	@Size(min=4, message = "비밀번호는 4글자 이상 입력해주세요.")
	private String boPass;                  /* 비밀번호 */
	@Size(min=10, message = "글 내용은 최소 10글자 이상 입력해주세요.")
	private String boContent;               /* 글 내용 */
	private String boIp;                    /* 등록자 IP */
	private int boHit;                      /* 조회수 */
	private String boRegDate;               /* 등록 일자 */
	private String boModDate;               /* 수정 일자 */
	private String boDelYn;                 /* 삭제 여부 */
	private String boCategoryNm;
	private List<AttachVO> attaches; //첨부파일 리스트, 그럼 댓글은 왜 안하나요? 댓글은 free에 포함되는게 아니라 ajax로 그때그때 요청해서 안함
	private int[] delAtchNos; // 삭제를 위한 글 번호
	
	public List<AttachVO> getAttaches() {
		return attaches;
	}

	public void setAttaches(List<AttachVO> attaches) {
		this.attaches = attaches;
	}

	public int[] getDelAtchNos() {
		return delAtchNos;
	}

	public void setDelAtchNos(int[] delAtchNos) {
		this.delAtchNos = delAtchNos;
	}

	public int getBoNo() {
		return boNo;
	}

	public void setBoNo(int boNo) {
		this.boNo = boNo;
	}

	public String getBoTitle() {
		return boTitle;
	}

	public void setBoTitle(String boTitle) {
		this.boTitle = boTitle;
	}

	public String getBoCategory() {
		return boCategory;
	}

	public void setBoCategory(String boCategory) {
		this.boCategory = boCategory;
	}

	public String getBoWriter() {
		return boWriter;
	}

	public void setBoWriter(String boWriter) {
		this.boWriter = boWriter;
	}

	public String getBoPass() {
		return boPass;
	}

	public void setBoPass(String boPass) {
		this.boPass = boPass;
	}

	public String getBoContent() {
		return boContent;
	}

	public void setBoContent(String boContent) {
		this.boContent = boContent;
	}

	public String getBoIp() {
		return boIp;
	}

	public void setBoIp(String boIp) {
		this.boIp = boIp;
	}

	public int getBoHit() {
		return boHit;
	}

	public void setBoHit(int boHit) {
		this.boHit = boHit;
	}

	public String getBoRegDate() {
		return boRegDate;
	}

	public void setBoRegDate(String boRegDate) {
		this.boRegDate = boRegDate;
	}

	public String getBoModDate() {
		return boModDate;
	}

	public void setBoModDate(String boModDate) {
		this.boModDate = boModDate;
	}

	public String getBoDelYn() {
		return boDelYn;
	}

	public void setBoDelYn(String boDelYn) {
		this.boDelYn = boDelYn;
	}

	public String getBoCategoryNm() {
		return boCategoryNm;
	}

	public void setBoCategoryNm(String boCategoryNm) {
		this.boCategoryNm = boCategoryNm;
	}

	@Override
	public String toString() {
		return "FreeBoardVO [boNo=" + boNo + ", boTitle=" + boTitle + ", boCategory=" + boCategory + ", boWriter="
				+ boWriter + ", boPass=" + boPass + ", boContent=" + boContent + ", boIp=" + boIp + ", boHit=" + boHit
				+ ", boRegDate=" + boRegDate + ", boModDate=" + boModDate + ", boDelYn=" + boDelYn + ", boCategoryNm="
				+ boCategoryNm + ", attaches=" + attaches + ", delAtchNos=" + Arrays.toString(delAtchNos) + "]";
	}


}
