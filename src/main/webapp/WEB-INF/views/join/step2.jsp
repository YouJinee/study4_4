<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@include file="/WEB-INF/inc/header.jsp"%>
<title>회원가입 2단계</title>
</head>
<body>

	<%@include file="/WEB-INF/inc/top.jsp"%>
	<div class="container">

		<form:form modelAttribute="member" method="post" action="step3.wow"
			name="join">
			<div class="row col-md-8 col-md-offset-2">
				<div class="page-header">
					<h3>회원가입 2단계</h3>
				</div>

				<table class="table">
					<colgroup>
						<col width="20%" />
						<col />
					</colgroup>
					<tr>
						<th>ID</th>
						<td><form:input path="memId" cssClass="form-control input-sm" />
							<button type="button" onclick="fn_idCheck()">ID 중복 체크</button>
							<form:errors path="memId" /></td>
					</tr>
					<tr>
						<th>비밀번호</th>
						<td><form:password path="memPass"
								cssClass="form-control input-sm" /> <form:errors path="memPass" />
						</td>
					</tr>

					<tr class="form-group-sm">
						<th>회원명</th>
						<td><form:input path="memName"
								cssClass="form-control input-sm" /> <form:errors path="memName" />
						</td>
					</tr>
					<tr class="form-group-sm">
						<th>이메일</th>
						<td><form:input id="emailAdd" path="memMail"
								cssClass="form-control input-sm" />
							<button type="button" onclick="fn_emailCheck()">이메일인증</button>
							<form:errors 	path="memMail" /> <input type="text" id="randomKey"
							class="form-control input-sm"
							placeholder="이메일에 받은 6자리 랜덤숫자를 입력하세요."></td>
					</tr>
					<tr>
						<td colspan="2">
							<div class="pull-left">
								<a href="cancel" class="btn btn-sm btn-default"> <span
									class="glyphicon glyphicon-remove" aria-hidden="true"></span>
									&nbsp;&nbsp;취 소
								</a>
							</div>
							<div class="pull-right">
								<button type="submit" class="btn btn-sm btn-primary"
									onclick="fn_randomkeyCheck()">
									<span class="glyphicon glyphicon-chevron-right"
										aria-hidden="true"></span> &nbsp;&nbsp;다 음
								</button>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</form:form>
	</div>
	<!-- END : 메인 콘텐츠  컨테이너  -->
</body>

<script type="text/javascript">
	$form = $("form[name='join']");
	var randomKey = "";
	var idCheck = 'N';
	
	
	function fn_emailCheck() {

		//selector => find, closest
		//val, html, text
		//attr
		//ajax : url(주소 : 보통 같은 서버에 있는 곳 요청할 때는 c:url 태그와 함께)
		//data : 파라미터들
		//type : get, post,
		//dataType : 요청한 곳으로부터 받는 데이터 타입
		//success : function(data) 성공하고 나서 실행되는 함수, data는 요청한 곳으로 부터 받은 데이터

		//서버는 브라우저한테 html 태그 전달해주고 역할 끝
		//html 태그는 브라우저가 실행해준다. button 이벤트, ajax 전부 등등 전부 브라우저가 실행해준다. 
		event.preventDefault(); //form:form태그 안에 버튼이라서 submit 되는거 막아준다.
		var emailAdd = $("#emailAdd").val();

		$.ajax({
			url : "<c:url value='/join/emailSend.wow'/>",
			type : "post",
			data : {
				"emailAdd" : emailAdd
			},
			success : function(data) {
				alert(data);
				randomKey = data;

				//다음버튼 눌렀을 때 사용자가 입력한 값이랑 random값이 같아야만 step으로 넘어가도록
				//다르면 alert("이메일 인증키가 다릅니다.")
			}
		});
	}

	function fn_randomkeyCheck() {
		event.preventDefault();
		var insertKey = $("#randomKey").val();
	if(idCheck=='Y'){
		if (randomKey == insertKey) {
			alert("이메일 인증 완료");
			$form.submit();
		} else {
			alert("이메일 인증키가 다릅니다.");
		}
	}else if(idCheck=='N'){
		alert("ID 중복체크는 필수입니다.")
	}
	}
	
	
	function fn_idCheck(){
		event.preventDefault();
		var insertId = $("#memId").val();
		alert(insertId);
		$.ajax({
			url : "<c:url value='/join/checkId.wow'/>",
			type : "post",
			data : {"insertId" : insertId}
			,success : function (data) {
				if(data=='N'){
					idCheck = 'N';
					alert("이미 존재하는 ID 입니다.");
				}else if(data=='Y'){
					idCheck = 'Y';
					alert("사용 가능한 ID 입니다.");
				}
			}
		})
	}
	//id 중복체크 버튼 만들고 함수 만들기
	//함수에서 ajax 호출해서 내가 입력한 id 값이 DB에 있는지 없는지 확인
	//idCheckYn = 'N', 없으면 idCheckYn='Y'
	//밑예 다음 버튼 이벤트에서 이메일 인증과 함께 idCheckYn도 확인해서
	//이메일도 인증했고 id도 체크해서 submit 되도록
</script>

</html>



