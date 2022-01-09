<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%request.setCharacterEncoding("utf-8"); %>
<%@include file="/WEB-INF/inc/header.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/inc/top.jsp" %>
<div role="tabpanel">
  <!-- Nav tabs -->
  <ul class="nav nav-tabs" role="tablist" id="myTab">
    <li role="presentation" class="active"><a href="changhee" aria-controls="home" role="tab" data-toggle="tab">changhee</a></li>
    <li role="presentation"><a href="ohtani" aria-controls="profile" role="tab" data-toggle="tab">othani</a></li>
    <li role="presentation"><a href="zimerman" aria-controls="messages" role="tab" data-toggle="tab">zimerman</a></li>
  </ul>

  <!-- Tab panes :
  1.div안에 내용이 각각 있고 상위 div(tab-content) 에서 하위 div가 그때그때 active되는 형식 
  2. div는 한개이고 그때 그때 ajax로 div 내용 바꾸는 방법		-->
  <div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="home">...</div>
  </div>
</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$('#myTab a').on("click", function(e){
		e.preventDefault();
		var page ="";
		page = $(this).attr('href');
		alert(page);
	$('#home').load('<c:url value="/about/'+page+'.wow"/>');
				//ajax랑 똑같음. 받은 데이터가 jsp의 body 태그. 
				// 받은 body 태그를 특정 태그에 innerHTML로
	}); //myTab a 클릭
	
});
</script>

</html>