<%@page import="com.study.exception.BizPasswordNotMatchedException"%>
<%@page import="com.study.exception.BizNotFoundException"%>
<%@page import="com.study.exception.BizNotEffectedException"%>
<%@page import="com.study.free.service.FreeBoardServiceImpl"%>
<%@page import="com.study.free.service.IFreeBoardService"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.SQLException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<%@ include file="/WEB-INF/inc/header.jsp" %>
	<title>freeDelete.jsp </title>
</head>
<body>
<%@include file="/WEB-INF/inc/top.jsp"%>
<jsp:useBean id="freeBoard" class="com.study.free.vo.FreeBoardVO"></jsp:useBean>
<jsp:setProperty property="*" name="freeBoard"/>	

<%

	IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
	try{
		freeBoardService.removeBoard(freeBoard);	
		request.setAttribute("notEx", "notEx");
	}catch(BizNotFoundException enf){
		//글 번호가 없을 때
		request.setAttribute("enf", enf);
	}catch(BizPasswordNotMatchedException epm){
		//글 등록 당시 비밀번호랑 지금 입력한 비밀번호가 다를 때
		request.setAttribute("epm", epm);
	}catch(BizNotEffectedException ene){
		//왠지 모르게 수정 실패임
		request.setAttribute("ene", ene);
	}
	
%>
 <div class="container">	
	<h3>회원 정보 삭제</h3>		
	
	

	<c:if test="${enf!=null }">
		<div class="alert alert-warning">
			해당 글이 존재하지 않습니다.
		</div>	
</c:if>
	
	<c:if test="${ene!=null }">
		<div class="alert alert-warning">
			삭제 실패
		</div>	
	</c:if>
	<c:if test="${epm!=null }">
		<div class="alert alert-warning">
			비밀번호가 틀립니다.
		</div>	
	</c:if>
		
		
	<c:if test="${notEx!=null }">
		<div class="alert alert-success">
			정상적으로 삭제했습니다.
		</div>		
	</c:if>
	<a href="freeList.jsp" class="btn btn-default btn-sm">
		<span class="glyphicon glyphicon-list" aria-hidden="true"></span>
		&nbsp;목록
	</a>
	
		
	

	</div>
</body>
</html>