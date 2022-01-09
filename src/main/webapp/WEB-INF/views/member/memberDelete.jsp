

<%@page import="com.study.exception.BizNotEffectedException"%>
<%@page import="com.study.exception.BizNotFoundException"%>
<%@page import="com.study.member.service.MemberServiceImpl"%>
<%@page import="com.study.member.service.IMemberService"%>
<%@page import="com.study.member.vo.MemberVO"%>

<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/inc/header.jsp"%>
</head>
<body>
	<%@include file="/WEB-INF/inc/top.jsp"%>
	<%
		String memId = request.getParameter("memId");
		String memName = request.getParameter("memName");
		String memPass = request.getParameter("memPass");
	%>

	<jsp:useBean id="member" class="com.study.member.vo.MemberVO"></jsp:useBean>
	<jsp:setProperty property="*" name="member" />
	<%
		IMemberService memberService = new MemberServiceImpl();
		try {
			memberService.removeMember(member);
			request.setAttribute("notEx", "notEx");
		} catch (BizNotFoundException enf) {
			request.setAttribute("enf", enf);
		} catch (BizNotEffectedException ene) {
			request.setAttribute("ene", ene);
		}
	%>


	<div class="container">
		<h3>회원삭제</h3>
	
	<c:if test="${notEx !=null }">
		<div class="alert alert-warning">
			<h4>삭제 성공</h4>
			정상적으로 회원을 삭제했습니다.
		</div>
</c:if>



	<c:if test="${enf!=null }">
		<div class="alert alert-warning">
			<h4>회원이 존재하지 않습니다.</h4>
			올바르게 접근해주세요.
		</div>
</c:if>

	<c:if test="${ene!=null }">
		<div class="alert alert-warning">
			<h4>삭제에 실패했습니다.</h4>
			삭제 실패
		</div>
</c:if>
		<a href="memberList.jsp?" class="btn btn-default btn-sm"> <span
			class="glyphicon glyphicon-list" aria-hidden="true"></span> &nbsp;목록
		</a>
	</div>
</body>
</html>