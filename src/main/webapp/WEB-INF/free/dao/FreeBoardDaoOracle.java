package com.study.free.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.study.common.util.ConnectionProvider;
import com.study.exception.DaoException;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

public class FreeBoardDaoOracle implements IFreeBoardDao {

	@Override
	public int getTotalRowCount(FreeBoardSearchVO searchVO) {
		// TODO Auto-generated method stub
		// DB에 있는 총 rowCount 구하는 메소드
		// 아직 검색은 안하지만 검색 조건에 맞게 총 rowCount를 구한다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionProvider.getConnectionByProvier();
			StringBuffer sb = new StringBuffer();
			// 조건에 맞는 글 개수를 얻어야함
			// searchVO에는 searchType은 무조건 있음
			// searchWord, category는 사용자가 입력해야 있다.
			sb.append("SELECT count(*)	");
			sb.append("FROM free_board	");
			sb.append("WHERE 1=1			"); // 아래 검색어 입력할 때 분류나 검색어 둘 다 사용하기 위해 우선 WHERE 썬턖햬뚬
			/*
			 * if(searchVO.getSearchWord()!=null && searchVO.getSearchWord().equals("")) {
			 * }
			 */
			if (!StringUtils.isBlank(searchVO.getSearchWord())) {
				// 입력함
				switch (searchVO.getSearchType()) {
				case "T":
					sb.append("AND bo_title LIKE '%'|| ? || '%' ");
					break;
				case "W":
					sb.append("AND bo_writer LIKE '%' || ? || '%' ");

					break;
				case "C":
					sb.append("AND bo_content LIKE '%' || ? || '%' ");

					break;
				default:
					break;
				}
			}

			// 검색어 가지고 검색, 분류만 가지고 검색, 검색어랑 분류 둘 다 검색
			if (!StringUtils.isBlank(searchVO.getSearchCategory())) {
				sb.append("AND bo_category =?			");
			}
			
			pstmt = conn.prepareStatement(sb.toString());
			int i=1;
			
			if (!StringUtils.isBlank(searchVO.getSearchWord())) {
				pstmt.setString(i++, searchVO.getSearchWord());
			}
			if (!StringUtils.isBlank(searchVO.getSearchCategory())) {
				pstmt.setString(i++, searchVO.getSearchCategory());
			}
		
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int cnt = rs.getInt(1);
				return cnt;
			}
			return 0;
		} catch (SQLException e) {
			throw new DaoException("totalRowCount " + e.getMessage(), e);
		} finally{
			if(rs!=null){try{rs.close();} catch(Exception e){}};
			if(pstmt!=null){try{pstmt.close();} catch(Exception e){}};
			if(conn!=null){try{conn.close();} catch(Exception e){}}
		}
	}

	@Override
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionProvider.getConnectionByProvier();

			StringBuffer sb = new StringBuffer();
			sb.append("SELECT b.* from (												 ");
			sb.append("SELECT a.*, rownum as rnum from (							 ");
			sb.append("SELECT   														 ");
			sb.append("    bo_no,           bo_title,       bo_category			 ");
			sb.append("    , bo_writer,     bo_pass,        bo_content			 ");
			sb.append("    , bo_ip,         bo_hit,         bo_reg_date			 ");
			sb.append("    , bo_mod_date,   bo_del_yn,							 ");
			sb.append("		b.comm_nm			AS bo_category_nm					 ");
			sb.append("FROM  free_board	a											 ");
			sb.append("	, comm_code b 			 		 						 ");
			sb.append("WHERE 			a.bo_category = b.comm_cd					 ");
			if (!StringUtils.isBlank(searchVO.getSearchWord())) {
				// 입력함
				switch (searchVO.getSearchType()) {
				case "T":
					sb.append("AND bo_title LIKE '%'|| ? || '%' ");
					break;
				case "W":
					sb.append("AND bo_writer LIKE '%' || ? || '%' ");

					break;
				case "C":
					sb.append("AND bo_content LIKE '%' || ? || '%' ");

					break;
				default:
					break;
				}
			}
			if (!StringUtils.isBlank(searchVO.getSearchCategory())) {
				sb.append("AND bo_category =?			");
			}
			
			sb.append("ORDER BY 			bo_no	DESC		) 		a)	b				 ");
			sb.append("WHERE rnum between ? and ? 								 ");
			pstmt = conn.prepareStatement(sb.toString());
			int i = 1;
			if (!StringUtils.isBlank(searchVO.getSearchWord())) {
				pstmt.setString(i++, searchVO.getSearchWord());
			}
			if (!StringUtils.isBlank(searchVO.getSearchCategory())) {
				pstmt.setString(i++, searchVO.getSearchCategory());
			}
		
			pstmt.setInt(i++, searchVO.getFirstRow());
			pstmt.setInt(i++, searchVO.getLastRow());
			rs = pstmt.executeQuery();

			List<FreeBoardVO> boardList = new ArrayList<FreeBoardVO>();
			while (rs.next()) {
				FreeBoardVO freeBoard = new FreeBoardVO();

				freeBoard.setBoNo(rs.getInt("bo_no"));
				freeBoard.setBoTitle(rs.getString("bo_title"));
				freeBoard.setBoCategory(rs.getString("bo_category"));
				freeBoard.setBoWriter(rs.getString("bo_writer"));
				freeBoard.setBoPass(rs.getString("bo_pass"));
				freeBoard.setBoContent(rs.getString("bo_content"));
				freeBoard.setBoIp(rs.getString("bo_ip"));
				freeBoard.setBoHit(rs.getInt("bo_hit"));
				freeBoard.setBoRegDate(rs.getString("bo_reg_date"));
				freeBoard.setBoModDate(rs.getString("bo_mod_date"));
				freeBoard.setBoDelYn(rs.getString("bo_del_yn"));
				freeBoard.setBoCategoryNm(rs.getString("bo_category_nm"));
				boardList.add(freeBoard);
			}
			return boardList;
		} catch (SQLException e) {
			throw new DaoException("getBoardList: " + e.getMessage(), e);
		} finally{
			if(rs!=null){try{rs.close();} catch(Exception e){}};
			if(pstmt!=null){try{pstmt.close();} catch(Exception e){}};
			if(conn!=null){try{conn.close();} catch(Exception e){}}
		}
	}

	@Override
	public FreeBoardVO getBoard(int boNo) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = ConnectionProvider.getConnectionByProvier();
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT   				");
			sb.append("    bo_no,           bo_title,       bo_category			");
			sb.append("    , bo_writer,     bo_pass,        bo_content			");
			sb.append("    , bo_ip,         bo_hit,         bo_reg_date			");
			sb.append("    , bo_mod_date,   bo_del_yn,    b.comm_nm			AS bo_category_nm					");
			sb.append("FROM  free_board	a											 ");
			sb.append("	, comm_code b 			 		 ");
			sb.append("WHERE  bo_no = ?			and a.bo_category = b.comm_cd		");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, boNo);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				FreeBoardVO freeBoard = new FreeBoardVO();
				freeBoard.setBoNo(rs.getInt("bo_no"));
				freeBoard.setBoTitle(rs.getString("bo_title"));
				freeBoard.setBoCategory(rs.getString("bo_category"));
				freeBoard.setBoWriter(rs.getString("bo_writer"));
				freeBoard.setBoPass(rs.getString("bo_pass"));
				freeBoard.setBoContent(rs.getString("bo_content"));
				freeBoard.setBoIp(rs.getString("bo_ip"));
				freeBoard.setBoHit(rs.getInt("bo_hit"));
				freeBoard.setBoRegDate(rs.getString("bo_reg_date"));
				freeBoard.setBoModDate(rs.getString("bo_mod_date"));
				freeBoard.setBoDelYn(rs.getString("bo_del_yn"));
				freeBoard.setBoCategoryNm(rs.getString("bo_category_nm"));
				return freeBoard;
			}
			return null; // 글 번호가 DB에 없는 경우
		} catch (SQLException e) {
			throw new DaoException("getBoardList: " + e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			;
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			;
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
			;
		}
	}

	@Override
	public int increaseHit(int boNo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			conn = ConnectionProvider.getConnectionByProvier();
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE free_board SET bo_hit = bo_hit + 1");
			sb.append("WHERE bo_no = ?");
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, boNo);
			int cnt = pstmt.executeUpdate();
			return cnt;
		} catch (SQLException e) {
			// TODO: handle exception
			throw new DaoException("increaseHit" + e.getMessage(), e);
		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			;
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			;
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
			;
		}
	}

	@Override
	public int updateBoard(FreeBoardVO freeBoard) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			conn = ConnectionProvider.getConnectionByProvier();
			StringBuffer sb = new StringBuffer();

			// 업데이트 하기전에 넘어온 파라미터 (사용자가 수정할 때 입력한) 비밀번호랑
			// 현재 DB에 있는 비밀번호 (사용자가 글 쓸 때 입력한 비밀번호)
			// update를 해야한다.

			sb.append(" UPDATE free_board           			   ");
			sb.append(" SET            						   ");
			sb.append("   bo_title = ?,      bo_category = ?,      bo_content = ? ");
			sb.append(" WHERE bo_no = ?               ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, freeBoard.getBoTitle());
			pstmt.setString(2, freeBoard.getBoCategory());
			pstmt.setString(3, freeBoard.getBoContent());
			pstmt.setInt(4, freeBoard.getBoNo());

			int cnt = pstmt.executeUpdate();
			return cnt;

		} catch (SQLException e) {
			throw new DaoException("updateBoard" + e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			;
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			;
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
			;
		}
	}

	@Override
	public int deleteBoard(FreeBoardVO freeBoard) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = ConnectionProvider.getConnectionByProvier();
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE free_board						");
			sb.append("SET	bo_del_yn = 'Y'					");
			sb.append("WHERE bo_no = ?							");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, freeBoard.getBoNo());

			int cnt = pstmt.executeUpdate();

			return cnt;
		} catch (SQLException e) {
			throw new DaoException("deleteBoard" + e.getMessage());
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			;
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public int insertBoard(FreeBoardVO freeBoard) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionProvider.getConnectionByProvier();
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO free_board (												");
			sb.append("    bo_no, 			bo_title, 		bo_category						");
			sb.append("    , bo_writer, 	bo_pass, 		bo_content									");
			sb.append("    , bo_ip, 			bo_hit,		bo_reg_date					");
			sb.append("    ,bo_mod_date	 , bo_del_yn											");
			sb.append(") VALUES (																");
			sb.append("	seq_free_board.NEXTVAL  ,		?    ,						?     ");
			sb.append("	,?							,	    ?    ,						?     ");
			sb.append("	,?							, 		?    ,						sysdate");
			sb.append("	,null						,										'N'	)");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, freeBoard.getBoTitle());
			pstmt.setString(2, freeBoard.getBoCategory());
			pstmt.setString(3, freeBoard.getBoWriter());
			pstmt.setString(4, freeBoard.getBoPass());
			pstmt.setString(5, freeBoard.getBoContent());
			pstmt.setString(6, freeBoard.getBoIp());
			pstmt.setInt(7, freeBoard.getBoHit());

			int cnt = pstmt.executeUpdate();
			return cnt;
		} catch (SQLException e) {
			throw new DaoException("insertBoard" + e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			;
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			;
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
